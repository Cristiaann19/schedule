package com.example.schedule.Model;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Id;
import lombok.Data;

@Data
@Document(collection = "configuracion_web")
public class ConfiguracionWeb {

    @Id
    private String id;

    private String urlLogo;
    private List<String> imagenesSlider;
}
