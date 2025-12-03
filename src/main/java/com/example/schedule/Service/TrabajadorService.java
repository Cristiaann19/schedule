package com.example.schedule.Service;

import com.example.schedule.Model.Trabajador;
import com.example.schedule.Repository.JPA.TrabajadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TrabajadorService {

    @Autowired
    private TrabajadorRepository trabajadorRepository;

    public List<Trabajador> listarTrabajadores() {
        return trabajadorRepository.findAll();
    }

    public void guardarTrabajador(Trabajador trabajador) {
        trabajadorRepository.save(trabajador);
    }

    public void eliminarTrabajador(Long id) {
        trabajadorRepository.deleteById(id);
    }

    public Trabajador buscarPorId(Long id) {
        return trabajadorRepository.findById(id).orElse(null);
    }
}