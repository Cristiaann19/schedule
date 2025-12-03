package com.example.schedule.Controller;

import com.example.schedule.Service.EnfermedadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.schedule.Model.Enfermedad;

@Controller
@RequestMapping("/admin/enfermedades")
public class EnfermedadController {
    @Autowired
    private EnfermedadService enfermedadService;

    @PostMapping("/guardar")
    public String guardar(Enfermedad enfermedad) {
        try {
            enfermedadService.guardar(enfermedad);
            return "redirect:/admin/dashboard?success=Enfermedad+guardada+correctamente";
        } catch (Exception e) {
            return "redirect:/admin/dashboard?error=Error+al+guardar+enfermedad";
        }
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable String id) {
        try {
            enfermedadService.eliminar(id);
            return "redirect:/admin/dashboard?success=Enfermedad+eliminada+correctamente";
        } catch (Exception e) {
            return "redirect:/admin/dashboard?error=Error+al+eliminar+enfermedad";
        }
    }

    @GetMapping("/api/{id}")
    @ResponseBody
    public Enfermedad apiBuscar(@PathVariable String id) {
        return enfermedadService.buscarPorId(id);
    }
}
