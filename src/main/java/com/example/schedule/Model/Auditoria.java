package com.example.schedule.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@Document(collection = "auditoria")
public class Auditoria {
    @Id
    private String id;

    private String tipoEvento;
    private String usuario;
    private LocalDateTime fechaHora;
    private String descripcion;
    private Map<String, Object> detalles;
}
