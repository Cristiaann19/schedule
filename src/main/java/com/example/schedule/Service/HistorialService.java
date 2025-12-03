package com.example.schedule.Service;

import com.example.schedule.Model.*;
import com.example.schedule.Repository.JPA.CitaRepository;
import com.example.schedule.Repository.Mongo.HistorialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class HistorialService {

    @Autowired
    private HistorialRepository historialRepository;
    @Autowired
    private CitaRepository citaRepository;

    public void registrarConsulta(HistorialClinico historia) {
        historia.setFechaRegistro(LocalDateTime.now());
        historialRepository.save(historia);

        Cita cita = citaRepository.findById(historia.getCitaId()).orElse(null);
        if (cita != null) {
            cita.setEstado(Cita.EstadoCita.REALIZADA);
            citaRepository.save(cita);
        }
    }

    public List<HistorialClinico> obtenerHistoriaMascota(Long mascotaId) {
        return historialRepository.findByMascotaId(mascotaId);
    }
}