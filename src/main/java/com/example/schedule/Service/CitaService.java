package com.example.schedule.Service;

import com.example.schedule.Exception.ValidacionException;
import com.example.schedule.Model.Cita;
import com.example.schedule.Model.Cita.EstadoCita;
import com.example.schedule.Model.Servicio;
import com.example.schedule.Model.Trabajador;
import com.example.schedule.Repository.JPA.CitaRepository;
import com.example.schedule.Repository.JPA.TrabajadorRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CitaService {

    @Autowired
    private CitaRepository citaRepository;
    @Autowired
    private ServicioService servicioService;

    @Autowired
    private TrabajadorRepository trabajadorRepository;

    public List<Cita> listarCitas() {
        return citaRepository.findAll();
    }

    public void guardarCita(Cita cita) {

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

            citaRepository.save(cita);
            System.out.println("Cita actualizada automáticamente ID: " + cita.getId());
        }
    }
}