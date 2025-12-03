package com.example.schedule.Model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "enfermedades")
public class Enfermedad {
    @Id
    private String id;

    private String nombre;
    private String descripcion;
    private String especie;
    private String gravedad;
}