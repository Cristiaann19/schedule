package com.example.schedule.Model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Document(collection = "historias_clinicas")
public class HistorialClinico {
    @Id
    private String id;

    private Long citaId;
    private Long mascotaId;

    private LocalDateTime fechaRegistro;

    private String enfermedadDetectada;
    private String vacunaAplicada;

    private String diagnostico;
    private String tratamiento;
    private Double peso;
}