package com.example.schedule.Service;

import com.example.schedule.Model.Auditoria;
import com.example.schedule.Repository.Mongo.AuditoriaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class AuditoriaService {

    @Autowired
    private AuditoriaRepository auditoriaRepository;

    public void registrarEvento(String tipo, String usuario, String descripcion, Map<String, Object> detalles) {
        Auditoria auditoria = new Auditoria();
        auditoria.setTipoEvento(tipo);
        auditoria.setUsuario(usuario);
        auditoria.setDescripcion(descripcion);
        auditoria.setDetalles(detalles);
        auditoria.setFechaHora(LocalDateTime.now());
        auditoriaRepository.save(auditoria);
    }
}
