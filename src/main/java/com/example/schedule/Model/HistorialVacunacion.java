package com.example.schedule.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@Document(collection = "historial_vacunacion")
public class HistorialVacunacion {
    @Id
    private String id;

    private Long mascotaId;
    private Long trabajadorId;
    private String nombreVacuna;
    private LocalDateTime fechaAplicacion;
    private String lote;
    private String reacciones;
    private String observaciones;
    private Map<String, Object> documentoClinico;
}
