package com.example.schedule.Component;

import com.example.schedule.Service.CitaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CitaScheduler {

    @Autowired
    private CitaService citaService;

    @Scheduled(fixedRate = 3600000)
    public void revisarCitasVencidas() {
        System.out.println("---- Ejecutando tarea programada: Revisar Citas ----");
        citaService.actualizarCitasVencidas();
    }
}