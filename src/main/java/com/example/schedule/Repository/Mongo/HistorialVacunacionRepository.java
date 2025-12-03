package com.example.schedule.Repository.Mongo;

import com.example.schedule.Model.HistorialVacunacion;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HistorialVacunacionRepository extends MongoRepository<HistorialVacunacion, String> {
    List<HistorialVacunacion> findByMascotaId(Long mascotaId);
}
