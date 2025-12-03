package com.example.schedule.Model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "trabajadores")
public class Trabajador {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String dni;

    private String nombres;
    private String apellidos;
    private String telefono;
    private String correo;

    private String especialidad;
    private String horarioDisponible;
    private String estadoLaboral;
}