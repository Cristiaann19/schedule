package com.example.schedule.Controller;

import com.example.schedule.Exception.ValidacionException;
import com.example.schedule.Model.Cita;
import com.example.schedule.Service.CitaService;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.net.URLEncoder;

@Controller
@RequestMapping("/admin/citas")
public class CitaController {

    @Autowired
    private CitaService citaService;

    @PostMapping("/guardar")
    public String guardarCita(Cita cita, RedirectAttributes redirectAttributes) {
        try {
            citaService.guardarCita(cita);
            return "redirect:/admin/dashboard?success=Cita+agendada";
        } catch (Exception e) {
            String mensajeError = URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8);
            return "redirect:/admin/dashboard?error=" + mensajeError;
        }
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarCita(@PathVariable Long id) {
        citaService.eliminarCita(id);
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/api/{id}")
    @ResponseBody
    public Cita obtenerCita(@PathVariable Long id) {
        return citaService.buscarPorId(id);
    }

    @PostMapping("/cobrar/{id}")
    @ResponseBody
    public ResponseEntity<?> cobrarCita(@PathVariable Long id) {
        try {
            citaService.cobrarYFinalizarCita(id);
            return ResponseEntity.ok().body(Collections.singletonMap("success", true));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}