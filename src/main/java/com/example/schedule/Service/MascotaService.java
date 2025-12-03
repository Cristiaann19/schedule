package com.example.schedule.Service;

import com.example.schedule.Model.Mascota;
import com.example.schedule.Repository.JPA.MascotaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MascotaService {

    @Autowired
    private MascotaRepository mascotaRepository;

    public List<Mascota> listarMascotas() {
        return mascotaRepository.findAll();
    }

    public void guardarMascota(Mascota mascota) {
        mascotaRepository.save(mascota);
    }

    public void eliminarMascota(Long id) {
        mascotaRepository.deleteById(id);
    }

    public Mascota buscarPorId(Long id) {
        return mascotaRepository.findById(id).orElse(null);
    }

    public List<Mascota> listarPorCliente(Long idCliente) {
        return mascotaRepository.findByClienteId(idCliente);
    }
}