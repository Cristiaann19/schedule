package com.example.schedule.Service;

import com.example.schedule.Model.Enfermedad;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.schedule.Repository.Mongo.EnfermedadRepository;

@Service
public class EnfermedadService {
    @Autowired
    private EnfermedadRepository repositorio;

    public List<Enfermedad> listarEnfermedades() {
        return repositorio.findAll();
    }

    public void guardar(Enfermedad e) {
        repositorio.save(e);
    }

    public void eliminar(String id) {
        repositorio.deleteById(id);
    }

    public Enfermedad buscarPorId(String id) {
        return repositorio.findById(id).orElse(null);
    }
}
