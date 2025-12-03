package com.example.schedule.Service;

import com.example.schedule.Model.VacunaCatalogo;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.schedule.Repository.Mongo.VacunaCatalogoRepository;

@Service
public class VacunaService {
    @Autowired
    private VacunaCatalogoRepository catalogoRepo;

    public List<VacunaCatalogo> listarCatalogo() {
        return catalogoRepo.findAll();
    }

    public void guardarCatalogo(VacunaCatalogo v) {
        catalogoRepo.save(v);
    }

    public void eliminarCatalogo(String id) {
        catalogoRepo.deleteById(id);
    }

    public VacunaCatalogo buscarCatalogoPorId(String id) {
        return catalogoRepo.findById(id).orElse(null);
    }

}
