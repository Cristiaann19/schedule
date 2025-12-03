package com.example.schedule.Repository.JPA;

import java.util.List;

import com.example.schedule.Model.Trabajador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrabajadorRepository extends JpaRepository<Trabajador, Long> {
    List<Trabajador> findByEspecialidadAndEstadoLaboral(String especialidad, String estado);
}