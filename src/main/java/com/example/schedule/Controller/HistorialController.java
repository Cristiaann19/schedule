package com.example.schedule.Controller;

import java.util.List;

import com.example.schedule.Model.HistorialClinico;
import com.example.schedule.Service.HistorialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/admin/historial")
public class HistorialController {

    @Autowired
    private HistorialService historialService;

    @PostMapping("/guardar")
    public String guardarConsulta(HistorialClinico historia) {
        historialService.registrarConsulta(historia);
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/api/mascota/{id}")
    @ResponseBody
    public List<HistorialClinico> obtenerHistorialPorMascota(@PathVariable Long id) {
        return historialService.obtenerHistoriaMascota(id);
    }
}