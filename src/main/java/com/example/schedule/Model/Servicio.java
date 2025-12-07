package com.example.schedule.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

@Data
@Document(collection = "servicios")
public class Servicio {
    @Id
    private String id;

    private String nombre;
    private String descripcion;
    private String icono;
    private String colorInicio;
    private String colorFin;
    private Double precio;
    private String estado;
}