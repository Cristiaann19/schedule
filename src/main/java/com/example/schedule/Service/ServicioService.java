package com.example.schedule.Service;

import com.example.schedule.Model.Servicio;
import com.example.schedule.Repository.Mongo.ServicioRepository; // Ajusta el paquete si es necesario
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ServicioService {

    @Autowired
    private ServicioRepository servicioRepository;

    public List<Servicio> listarServicios() {
        return servicioRepository.findAll();
    }

    public Servicio obtenerPorId(String id) {
        return servicioRepository.findById(id).orElse(null);
    }

    public void eliminarServicio(String id) {
        servicioRepository.deleteById(id);
    }

    public void guardarServicio(Servicio servicio) {
        if (servicio.getId() != null && servicio.getId().isEmpty()) {
            servicio.setId(null);
        }
        servicioRepository.save(servicio);
    }

    public void cambiarEstado(String id) {
        Servicio s = servicioRepository.findById(id).orElse(null);

        if (s != null) {
            if ("ACTIVO".equals(s.getEstado())) {
                s.setEstado("INACTIVO");
            } else {
                s.setEstado("ACTIVO");
            }
            servicioRepository.save(s);
        }
    }

    public List<Servicio> listarSoloActivos() {
        return servicioRepository.findByEstado("ACTIVO");
    }
}