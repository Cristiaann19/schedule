package com.example.schedule.Controller;

import com.example.schedule.Service.VacunaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.example.schedule.Model.VacunaCatalogo;

@Controller
@RequestMapping("/admin/vacunas-catalogo")
public class VacunaCatalogoController {
    @Autowired
    private VacunaService vacunaService;

    @PostMapping("/guardar")
    public String guardar(VacunaCatalogo vacuna) {
        try {
            vacunaService.guardarCatalogo(vacuna);
            return "redirect:/admin/dashboard?success=Vacuna+guardada+correctamente";
        } catch (Exception e) {
            return "redirect:/admin/dashboard?error=Error+al+guardar+vacuna";
        }
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable String id) {
        try {
            vacunaService.eliminarCatalogo(id);
            return "redirect:/admin/dashboard?success=Vacuna+eliminada+correctamente";
        } catch (Exception e) {
            return "redirect:/admin/dashboard?error=Error+al+eliminar+vacuna";
        }
    }

    @GetMapping("/api/{id}")
    @ResponseBody
    public VacunaCatalogo apiBuscar(@PathVariable String id) {
        return vacunaService.buscarCatalogoPorId(id);
    }
}