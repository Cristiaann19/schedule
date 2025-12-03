package com.example.schedule.Controller;

import com.example.schedule.Model.Servicio;
import com.example.schedule.Service.ServicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/servicios")
public class ServicioController {

    @Autowired
    private ServicioService servicioService;

    @PostMapping("/guardar")
    public String guardarServicio(Servicio servicio) {
        try {
            servicioService.guardarServicio(servicio);
            return "redirect:/admin/dashboard?success=Servicio+guardado+correctamente";
        } catch (Exception e) {
            return "redirect:/admin/dashboard?error=Error+al+guardar+servicio";
        }
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarServicio(@PathVariable String id) {
        try {
            servicioService.eliminarServicio(id);
            return "redirect:/admin/dashboard?success=Servicio+eliminado+correctamente";
        } catch (Exception e) {
            return "redirect:/admin/dashboard?error=Error+al+eliminar+servicio";
        }
    }

    @GetMapping("/api/{id}")
    @ResponseBody
    public Servicio obtenerServicio(@PathVariable String id) {
        return servicioService.obtenerPorId(id);
    }
}