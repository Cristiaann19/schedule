package com.example.schedule.Service;

import com.example.schedule.Model.Producto;
import com.example.schedule.Repository.Mongo.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    public List<Producto> listarProductos() {
        return productoRepository.findAll();
    }

    public void guardarProducto(Producto producto) {
        if (producto.getId() != null && producto.getId().isEmpty()) {
            producto.setId(null);
        }
        productoRepository.save(producto);
    }

    public void eliminarProducto(String id) {
        productoRepository.deleteById(id);
    }

    public Producto obtenerPorId(String id) {
        return productoRepository.findById(id).orElse(null);
    }
}