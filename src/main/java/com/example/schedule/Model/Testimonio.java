package com.example.schedule.Model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;

@Data
@Document(collection = "testimonios")
public class Testimonio {
    @Id
    private String id;

    private String autor;
    private String contenido;
    private Integer estrellas;
    private LocalDate fecha;
    private String avatarUrl;
}