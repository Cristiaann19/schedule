package com.example.schedule.Repository.Mongo;

import com.example.schedule.Model.HistorialClinico;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HistorialRepository extends MongoRepository<HistorialClinico, String> {
    List<HistorialClinico> findByMascotaId(Long mascotaId);

    List<HistorialClinico> findByCitaId(Long citaId);
}