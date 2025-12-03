package com.example.schedule.Model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "vacunas_catalogo")
public class VacunaCatalogo {
    @Id
    private String id;

    private String nombre;
    private String fabricante;
    private String enfermedadAsociada;
    private Integer edadRecomendada;
    private Integer dosis;
}