package com.example.schedule.Repository.Mongo;

import com.example.schedule.Model.Testimonio;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestimonioRepository extends MongoRepository<Testimonio, String> {
}