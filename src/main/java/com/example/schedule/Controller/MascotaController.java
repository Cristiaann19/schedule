package com.example.schedule.Controller;

import com.example.schedule.Model.Mascota;
import com.example.schedule.Service.MascotaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/mascotas")
public class MascotaController {

    @Autowired
    private MascotaService mascotaService;

    @PostMapping("/guardar")
    public String guardarMascota(Mascota mascota) {
        try {
            mascotaService.guardarMascota(mascota);
            return "redirect:/admin/dashboard?success=Mascota+guardada+correctamente";
        } catch (Exception e) {
            return "redirect:/admin/dashboard?error=Error+al+guardar+mascota";
        }
    }

    @PostMapping("/api/guardar")
    @ResponseBody
    public ResponseEntity<?> guardarMascotaAjax(Mascota mascota) {
        try {
            Mascota nueva = mascotaService.guardarMascota(mascota);
            return ResponseEntity.ok(nueva);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al guardar: " + e.getMessage());
        }
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarMascota(@PathVariable Long id) {
        try {
            mascotaService.eliminarMascota(id);
            return "redirect:/admin/dashboard?success=Mascota+eliminada+correctamente";
        } catch (Exception e) {
            return "redirect:/admin/dashboard?error=Error+al+eliminar+mascota";
        }
    }

    @GetMapping("/api/{id}")
    @ResponseBody
    public Mascota obtenerMascota(@PathVariable Long id) {
        return mascotaService.buscarPorId(id);
    }
}