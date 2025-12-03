package com.example.schedule.Repository.Mongo;

import com.example.schedule.Model.Auditoria;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditoriaRepository extends MongoRepository<Auditoria, String> {
}
