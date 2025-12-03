package com.example.schedule.Controller;

import com.example.schedule.Model.Trabajador;
import com.example.schedule.Service.TrabajadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/trabajadores")
public class TrabajadorController {

    @Autowired
    private TrabajadorService trabajadorService;

    @PostMapping("/guardar")
    public String guardar(Trabajador trabajador) {
        if (trabajador.getEstadoLaboral() == null) {
            trabajador.setEstadoLaboral("ACTIVO");
        }
        try {
            trabajadorService.guardarTrabajador(trabajador);
            return "redirect:/admin/dashboard?success=Trabajador+guardado+correctamente";
        } catch (Exception e) {
            return "redirect:/admin/dashboard?error=Error+al+guardar+trabajador";
        }
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        try {
            trabajadorService.eliminarTrabajador(id);
            return "redirect:/admin/dashboard?success=Trabajador+eliminado+correctamente";
        } catch (Exception e) {
            return "redirect:/admin/dashboard?error=Error+al+eliminar+trabajador";
        }
    }

    @GetMapping("/api/{id}")
    @ResponseBody
    public Trabajador obtenerPorId(@PathVariable Long id) {
        return trabajadorService.buscarPorId(id);
    }
}