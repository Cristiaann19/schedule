package com.example.schedule.DTO;

import lombok.Data;
import java.util.List;

@Data
public class CompraDTO {
    private List<ItemCarrito> items;
    private String metodoPago;
    private String codigoOperacion;
}