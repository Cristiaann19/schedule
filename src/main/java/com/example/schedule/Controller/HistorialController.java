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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/admin/historial")
public class HistorialController {

    @Autowired
    private HistorialService historialService;

    @PostMapping("/guardar")
    public String guardarHistorial(HistorialClinico historial, @RequestParam Long citaId) {
        try {
            // Aseguramos que el objeto Historial tenga el ID de la cita (VITAL PARA EL
            // COBRO)
            historial.setCitaId(citaId);

            // Llamamos al servicio (que ya tiene la lógica de guardar en Mongo)
            historialService.guardarHistorial(historial, citaId);

            return "redirect:/admin/dashboard?success=Consulta+registrada+correctamente";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/admin/dashboard?error=Error+al+guardar+historial";
        }
    }

    @GetMapping("/api/mascota/{id}")
    @ResponseBody
    public List<HistorialClinico> obtenerHistorialPorMascota(@PathVariable Long id) {
        return historialService.obtenerHistoriaMascota(id);
    }
}