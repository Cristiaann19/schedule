package com.example.schedule.Repository.Mongo;

import com.example.schedule.Model.Servicio;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServicioRepository extends MongoRepository<Servicio, String> {
    List<Servicio> findByEstado(String estado);
}
