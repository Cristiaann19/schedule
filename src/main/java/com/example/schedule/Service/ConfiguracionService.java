package com.example.schedule.Service;

import com.example.schedule.Model.ConfiguracionWeb;
import com.example.schedule.Repository.Mongo.ConfiguracionWebRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Arrays;

@Service
public class ConfiguracionService {

    @Autowired
    private ConfiguracionWebRepository configRepo;

    public ConfiguracionWeb obtenerConfiguracion() {
        return configRepo.findById("config_principal").orElseGet(() -> {
            ConfiguracionWeb nueva = new ConfiguracionWeb();
            nueva.setId("config_principal");
            nueva.setUrlLogo("/images/logo-huellitas.png");
            nueva.setImagenesSlider(new ArrayList<>(Arrays.asList(
                    "https://images.unsplash.com/photo-1548199973-03cce0bbc87b?w=800",
                    "https://images.unsplash.com/photo-1599305090598-fe179d501227?w=800")));
            return configRepo.save(nueva);
        });
    }

    public void guardarLogo(String url) {
        ConfiguracionWeb config = obtenerConfiguracion();
        config.setUrlLogo(url);
        configRepo.save(config);
    }

    public void agregarImagenSlider(String url) {
        ConfiguracionWeb config = obtenerConfiguracion();
        if (config.getImagenesSlider() == null)
            config.setImagenesSlider(new ArrayList<>());
        config.getImagenesSlider().add(url);
        configRepo.save(config);
    }

    public void eliminarImagenSlider(String url) {
        ConfiguracionWeb config = obtenerConfiguracion();
        if (config.getImagenesSlider() != null) {
            config.getImagenesSlider().remove(url);
            configRepo.save(config);
        }
    }
}