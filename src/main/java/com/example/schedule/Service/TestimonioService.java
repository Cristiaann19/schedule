package com.example.schedule.Service;

import com.example.schedule.Model.Testimonio;
import com.example.schedule.Repository.Mongo.TestimonioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class TestimonioService {

    @Autowired
    private TestimonioRepository testimonioRepository;

    public List<Testimonio> listarTestimonios() {
        return testimonioRepository.findAll();
    }

    public void guardarTestimonio(String autor, String contenido, Integer estrellas) {
        Testimonio t = new Testimonio();
        t.setAutor(autor);
        t.setContenido(contenido);
        t.setEstrellas(estrellas);
        t.setFecha(LocalDate.now());
        testimonioRepository.save(t);
    }
}