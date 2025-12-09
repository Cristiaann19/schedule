package com.example.schedule.Repository.Mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.schedule.Model.ConfiguracionWeb;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfiguracionWebRepository extends MongoRepository<ConfiguracionWeb, String> {

}
