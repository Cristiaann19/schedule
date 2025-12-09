package com.example.schedule.Controller;

import com.example.schedule.Model.Cliente;
import com.example.schedule.Service.*;
import com.example.schedule.Repository.JPA.*;
import com.example.schedule.Repository.Mongo.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ClienteService clienteService;
    @Autowired
    private MascotaService mascotaService;
    @Autowired
    private CitaService citaService;
    @Autowired
    private ProductoService productoService;
    @Autowired
    private ServicioService servicioService;
    @Autowired
    private TrabajadorService trabajadorService;
    @Autowired
    private EnfermedadService enfermedadService;
    @Autowired
    private VacunaService vacunaService;
    @Autowired
    private VentaService ventaService;
    @Autowired
    private ConfiguracionService configService;

    @Autowired
    private CitaRepository citaRepository;
    @Autowired
    private VentaRepository ventaRepository;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private MascotaRepository mascotaRepository;

    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal) {

        if (principal != null) {
            String email = principal.getName();
            Cliente usuario = clienteService.buscarPorCorreo(email);
            if (usuario != null) {
                model.addAttribute("usuario", usuario.getNombres());
            } else {
                model.addAttribute("usuario", "Admin");
            }
        }

        LocalDateTime hoyInicio = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime hoyFin = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);

        LocalDateTime mesInicio = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0);
        LocalDateTime mesFin = LocalDateTime.now();

        long citasHoy = citaRepository.countByFechaHoraBetween(hoyInicio, hoyFin);
        model.addAttribute("statsCitasHoy", citasHoy);

        long totalClientes = clienteRepository.count();
        model.addAttribute("statsClientes", totalClientes);
        Double ventasMes = ventaRepository.sumarVentasPorFecha(mesInicio, mesFin);
        if (ventasMes == null)
            ventasMes = 0.0;
        model.addAttribute("statsVentasMes", ventasMes);

        long totalMascotas = mascotaRepository.count();
        model.addAttribute("statsMascotas", totalMascotas);

        model.addAttribute("clientes", clienteService.listarClientes());
        model.addAttribute("mascotas", mascotaService.listarMascotas());
        model.addAttribute("citas", citaService.listarCitas());
        model.addAttribute("productos", productoService.listarProductos());
        model.addAttribute("servicios", servicioService.listarServicios());
        model.addAttribute("trabajadores", trabajadorService.listarTrabajadores());
        model.addAttribute("enfermedades", enfermedadService.listarEnfermedades());
        model.addAttribute("ventas", ventaService.listarTodas());
        model.addAttribute("vacunas", vacunaService.listarCatalogo());
        model.addAttribute("listaCitasHoy", citaService.obtenerCitasDelDia());
        model.addAttribute("configWeb", configService.obtenerConfiguracion());

        return "dashboard_admin";
    }
}