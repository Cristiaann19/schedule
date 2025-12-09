package com.example.schedule.Controller;

import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.schedule.DTO.CompraDTO;
import com.example.schedule.Exception.ValidacionException;
import com.example.schedule.Model.Cita;
import com.example.schedule.Model.Cliente;
import com.example.schedule.Model.Mascota;
import com.example.schedule.Repository.JPA.TrabajadorRepository;
import com.example.schedule.Service.CitaService;
import com.example.schedule.Service.ClienteService;
import com.example.schedule.Service.MascotaService;
import com.example.schedule.Service.ProductoService;
import com.example.schedule.Service.ServicioService;
import com.example.schedule.Service.TestimonioService;
import com.example.schedule.Service.VentaService;
import com.example.schedule.Service.ConfiguracionService;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
public class PublicController {

    @Autowired
    private ClienteService clienteService;
    @Autowired
    private ProductoService productoService;
    @Autowired
    private ServicioService servicioService;
    @Autowired
    private CitaService citaService;
    @Autowired
    private MascotaService mascotaService;
    @Autowired
    private VentaService ventaService;
    @Autowired
    private TestimonioService testimonioService;
    @Autowired
    private TrabajadorRepository trabajadorRepository;

    @Autowired
    private ConfiguracionService configService;

    @GetMapping({ "/", "/index" })
    public String index(Model model, Principal principal, HttpServletRequest request) {

        request.getSession(true);

        model.addAttribute("productos", productoService.listarProductos());
        model.addAttribute("servicios", servicioService.listarSoloActivos());

        model.addAttribute("testimonios", testimonioService.listarTestimonios());

        if (principal != null) {
            String email = principal.getName();
            Cliente cliente = clienteService.buscarPorCorreo(email);

            System.out.println("USUARIO DETECTADO EN INDEX: " + principal.getName());

            if (cliente != null) {
                model.addAttribute("nombreUsuario", cliente.getNombres());
                model.addAttribute("misMascotas", mascotaService.listarPorCliente(cliente.getId()));
                model.addAttribute("misCitas", citaService.listarPorCliente(cliente.getId()));
                model.addAttribute("misCompras", ventaService.listarPorCliente(cliente.getId()));
            }
        } else {
            System.out.println("INDEX: Usuario es NULL (Visitante)");
        }
        model.addAttribute("trabajadores", trabajadorRepository.findAll());
        model.addAttribute("configWeb", configService.obtenerConfiguracion());
        return "index_publico";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/citas/reservar")
    public String reservarCita(Cita cita, Principal principal, RedirectAttributes redirectAttributes) {
        try {
            citaService.guardarCita(cita);
            return "redirect:/index?reservada=true";
        } catch (ValidacionException e) {
            return "redirect:/index?error=" + URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8);
        }
    }

    @PostMapping("/mascotas/publica/guardar")
    public String guardarMascotaPublica(Mascota mascota, Principal principal) {
        if (principal != null) {
            String email = principal.getName();
            Cliente cliente = clienteService.buscarPorCorreo(email);

            if (cliente != null) {
                mascota.setCliente(cliente);
                mascotaService.guardarMascota(mascota);
            }
        }
        return "redirect:/index";
    }

    @PostMapping("/testimonios/guardar")
    public String guardarTestimonio(@RequestParam String contenido,
            @RequestParam Integer estrellas,
            Principal principal) {
        if (principal != null) {
            String email = principal.getName();
            Cliente cliente = clienteService.buscarPorCorreo(email);

            if (cliente != null) {
                // Format name: "Juan P."
                String nombreCompleto = cliente.getNombres() + " " + cliente.getApellidos().charAt(0) + ".";
                testimonioService.guardarTestimonio(nombreCompleto, contenido, estrellas);
            }
        }
        return "redirect:/index?testimonio=ok#testimonios";
    }

    @PostMapping("/carrito/checkout")
    @ResponseBody
    public ResponseEntity<?> procesarCheckout(@RequestBody CompraDTO compraData, Principal principal) {

        if (principal == null) {
            return ResponseEntity.status(401).body("{\"error\": \"Debes iniciar sesión\"}");
        }

        String email = principal.getName();
        Cliente cliente = clienteService.buscarPorCorreo(email);

        try {
            ventaService.registrarVentaCarrito(cliente, compraData);
            return ResponseEntity.ok().body("{\"success\": true}");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        request.getSession().invalidate();
        return "redirect:/index?logout";
    }
}