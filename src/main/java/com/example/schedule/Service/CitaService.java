package com.example.schedule.Service;

import com.example.schedule.Exception.ValidacionException;
import com.example.schedule.Model.Cita;
import com.example.schedule.Model.Cita.EstadoCita;
import com.example.schedule.Model.DetalleVenta;
import com.example.schedule.Model.HistorialClinico;
import com.example.schedule.Model.Servicio;
import com.example.schedule.Model.Trabajador;
import com.example.schedule.Model.VacunaCatalogo;
import com.example.schedule.Model.Venta;
import com.example.schedule.Repository.JPA.CitaRepository;
import com.example.schedule.Repository.JPA.TrabajadorRepository;
import com.example.schedule.Repository.JPA.VentaRepository;
import com.example.schedule.Repository.Mongo.HistorialRepository;
import com.example.schedule.Repository.Mongo.VacunaCatalogoRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CitaService {

    @Autowired
    private CitaRepository citaRepository;
    @Autowired
    private ServicioService servicioService;

    @Autowired
    private TrabajadorRepository trabajadorRepository;

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private VacunaCatalogoRepository vacunaCatalogoRepository;

    @Autowired
    private HistorialRepository HistorialRepository;

    public List<Cita> listarCitas() {
        return citaRepository.findAll();
    }

    public void guardarCita(Cita cita) {

        // 1. Completar datos del Servicio
        if (cita.getServicioId() != null) {
            Servicio servicio = servicioService.obtenerPorId(cita.getServicioId());
            if (servicio != null) {
                cita.setServicioNombre(servicio.getNombre());
                if (cita.getPrecioAcordado() == null) {
                    cita.setPrecioAcordado(servicio.getPrecio());
                }
            }
        }

        if (cita.getFechaHora().isBefore(LocalDateTime.now())) {
            throw new ValidacionException("No puedes agendar citas en el pasado.");
        }
        int hora = cita.getFechaHora().getHour();
        if (hora < 8 || hora >= 20) {
            throw new ValidacionException("El horario de atención es de 8:00 AM a 8:00 PM.");
        }

        if (cita.getMascota() != null) {
            List<Cita> conflictosMascota = citaRepository.findByMascotaIdAndFechaHoraAndEstadoNot(
                    cita.getMascota().getId(),
                    cita.getFechaHora(),
                    Cita.EstadoCita.CANCELADA);

            for (Cita existente : conflictosMascota) {
                if (!existente.getId().equals(cita.getId())) {
                    throw new ValidacionException("La mascota " + existente.getMascota().getNombre() +
                            " ya tiene una cita programada a las " + cita.getFechaHora().toLocalTime());
                }
            }
        }

        if (cita.getVeterinario() == null) {
            asignarTrabajadorAutomatico(cita);
        } else {
            validarTrabajadorEspecifico(cita.getVeterinario(), cita.getFechaHora());
        }

        if (cita.getEstado() == null) {
            cita.setEstado(Cita.EstadoCita.PENDIENTE);
        }

        citaRepository.save(cita);
    }

    private void asignarTrabajadorAutomatico(Cita cita) {

        String especialidadRequerida = "Veterinario";
        if (cita.getServicioNombre() != null &&
                (cita.getServicioNombre().contains("Grooming") || cita.getServicioNombre().contains("Baño"))) {
            especialidadRequerida = "Estilista";
        }

        List<Trabajador> candidatos = trabajadorRepository.findByEspecialidadAndEstadoLaboral(especialidadRequerida,
                "ACTIVO");

        if (candidatos.isEmpty()) {
            throw new ValidacionException("No hay personal " + especialidadRequerida + " disponible en este momento.");
        }

        for (Trabajador t : candidatos) {
            boolean ocupado = citaRepository.existsByVeterinarioIdAndFechaHoraAndEstadoNot(
                    t.getId(),
                    cita.getFechaHora(),
                    Cita.EstadoCita.CANCELADA);

            if (!ocupado) {
                cita.setVeterinario(t);
                return;
            }
        }

        throw new ValidacionException("No hay disponibilidad para esa hora. Por favor intenta otro horario.");
    }

    private void validarTrabajadorEspecifico(Trabajador t, LocalDateTime fecha) {
        Trabajador dbTrabajador = trabajadorRepository.findById(t.getId()).orElse(null);
        if (dbTrabajador == null || !"ACTIVO".equals(dbTrabajador.getEstadoLaboral())) {
            throw new ValidacionException("El especialista seleccionado no está disponible o inactivo.");
        }
        boolean ocupado = citaRepository.existsByVeterinarioIdAndFechaHoraAndEstadoNot(
                t.getId(), fecha, Cita.EstadoCita.CANCELADA);

        if (ocupado) {
            throw new ValidacionException("El especialista ya tiene una cita a esa hora.");
        }
    }

    public Cita buscarPorId(Long id) {
        return citaRepository.findById(id).orElse(null);
    }

    public void eliminarCita(Long id) {
        citaRepository.deleteById(id);
    }

    public List<Cita> listarPorCliente(Long clienteId) {
        return citaRepository.findByMascota_Cliente_Id(clienteId);
    }

    public List<Cita> obtenerCitasDelDia() {
        LocalDateTime inicio = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime fin = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);
        return citaRepository.findByFechaHoraBetweenOrderByFechaHoraAsc(inicio, fin);
    }

    public void actualizarCitasVencidas() {
        LocalDateTime ahora = LocalDateTime.now();

        List<Cita> citasViejas = citaRepository.findByEstadoAndFechaHoraBefore(EstadoCita.PENDIENTE, ahora);

        for (Cita cita : citasViejas) {
            cita.setEstado(EstadoCita.CANCELADA);
            cita.setMotivo(cita.getMotivo() + " [Cerrada autom.]"); // Opcional: Agregar nota
            citaRepository.save(cita);
            System.out.println("Cita actualizada automáticamente ID: " + cita.getId());
        }
    }

    @Transactional
    public void cobrarYFinalizarCita(Long citaId) {
        System.out.println("--- COBRANDO CITA ID: " + citaId + " ---");

        Cita cita = citaRepository.findById(citaId)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        if (cita.getEstado() == Cita.EstadoCita.REALIZADA) {
            throw new RuntimeException("Cita ya cobrada.");
        }

        Venta venta = new Venta();
        if (cita.getMascota() != null)
            venta.setCliente(cita.getMascota().getCliente());
        venta.setFecha(LocalDateTime.now());
        venta.setMetodoPago("EFECTIVO");
        venta.setCodigoOperacion("CITA-" + cita.getId());
        venta.setEstado("COMPLETADA");

        List<DetalleVenta> detalles = new ArrayList<>();
        double totalVenta = 0.0;

        // 1. SERVICIO BASE
        Double precioServicio = (cita.getPrecioAcordado() != null) ? cita.getPrecioAcordado() : 0.0;
        DetalleVenta detServ = new DetalleVenta();
        detServ.setNombreProducto("Servicio: " + cita.getServicioNombre());
        detServ.setPrecioUnitario(precioServicio);
        detServ.setCantidad(1);
        detServ.setSubtotal(precioServicio);
        detServ.setVenta(venta);
        if (cita.getServicioId() != null)
            detServ.setProductoId(String.valueOf(cita.getServicioId()));

        detalles.add(detServ);
        totalVenta += precioServicio;

        // 2. EXTRAS DEL HISTORIAL (Ahora soporta múltiples registros)
        // Obtenemos la LISTA de fichas (pueden ser 1, 2 o más)
        List<HistorialClinico> fichas = HistorialRepository.findByCitaId(citaId);

        System.out.println("Fichas encontradas: " + fichas.size());

        for (HistorialClinico ficha : fichas) {
            String vacuna = ficha.getVacunaAplicada();

            // Validar si tiene vacuna
            if (vacuna != null && !vacuna.trim().isEmpty() &&
                    !vacuna.contains("Ninguna") && !vacuna.equals("null")) {

                VacunaCatalogo catalogo = vacunaCatalogoRepository.findByNombre(vacuna);

                double precioVacuna = (catalogo != null && catalogo.getPrecio() != null) ? catalogo.getPrecio() : 35.00;

                DetalleVenta detVac = new DetalleVenta();
                detVac.setNombreProducto("Vacuna: " + vacuna);
                detVac.setPrecioUnitario(precioVacuna);
                detVac.setCantidad(1);
                detVac.setSubtotal(precioVacuna);
                detVac.setVenta(venta);

                detalles.add(detVac);
                totalVenta += precioVacuna;

                System.out.println("Sumando vacuna: " + vacuna + " - S/ " + precioVacuna);
            }
        }

        // 3. FINALIZAR
        venta.setDetalles(detalles);
        venta.setTotal(totalVenta);

        ventaRepository.save(venta);

        cita.setEstado(Cita.EstadoCita.REALIZADA);
        citaRepository.save(cita);
    }
}