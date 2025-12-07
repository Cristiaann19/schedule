package com.example.schedule.Repository.Mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.schedule.Model.VacunaCatalogo;

@Repository
public interface VacunaCatalogoRepository extends MongoRepository<VacunaCatalogo, String> {
    VacunaCatalogo findByNombre(String nombre);
}
