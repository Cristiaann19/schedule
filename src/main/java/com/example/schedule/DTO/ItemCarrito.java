package com.example.schedule.DTO;

import lombok.Data;

@Data
public class ItemCarrito {
    private String productoId;
    private Integer cantidad;
    private Double precio;
}