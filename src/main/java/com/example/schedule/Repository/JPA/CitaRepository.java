package com.example.schedule.Repository.JPA;

import com.example.schedule.Model.Cita;
import com.example.schedule.Model.Cita.EstadoCita;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.schedule.Model.Trabajador;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {
    List<Cita> findByVeterinarioAndFechaHora(Trabajador veterinario, LocalDateTime fechaHora);

    List<Cita> findByMascota_Cliente_Id(Long clienteId);

    long countByFechaHoraBetween(LocalDateTime inicio, LocalDateTime fin);

    List<Cita> findByFechaHoraBetweenOrderByFechaHoraAsc(LocalDateTime inicio, LocalDateTime fin);

    boolean existsByVeterinarioIdAndFechaHoraAndEstadoNot(Long trabajadorId, LocalDateTime fechaHora,
            Cita.EstadoCita estadoCancelado);

    List<Cita> findByEstadoAndFechaHoraBefore(EstadoCita estado, LocalDateTime fechaActual);
}
