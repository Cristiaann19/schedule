package com.example.schedule.Service;

import com.example.schedule.DTO.CompraDTO;
import com.example.schedule.DTO.ItemCarrito;
import com.example.schedule.Model.*;
import com.example.schedule.Repository.JPA.VentaRepository;
import com.example.schedule.Repository.Mongo.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class VentaService {

    @Autowired
    private VentaRepository ventaRepository;
    @Autowired
    private ProductoRepository productoRepository;

    @Transactional
    public void registrarVentaCarrito(Cliente cliente, CompraDTO compraDTO) {

        Venta venta = new Venta();
        venta.setCliente(cliente);
        venta.setFecha(LocalDateTime.now());
        venta.setEstado("COMPLETADA");
        venta.setMetodoPago(compraDTO.getMetodoPago());
        venta.setCodigoOperacion(compraDTO.getCodigoOperacion());

        double totalVenta = 0;

        for (ItemCarrito item : compraDTO.getItems()) {
            Producto prodMongo = productoRepository.findById(item.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + item.getProductoId()));

            // 2. Verificar Stock
            if (prodMongo.getStock() < item.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para: " + prodMongo.getNombre());
            }

            // 3. Crear Detalle
            DetalleVenta detalle = new DetalleVenta();
            detalle.setProductoId(prodMongo.getId());
            detalle.setNombreProducto(prodMongo.getNombre());
            detalle.setPrecioUnitario(prodMongo.getPrecio());
            detalle.setCantidad(item.getCantidad());

            double subtotal = prodMongo.getPrecio() * item.getCantidad();
            detalle.setSubtotal(subtotal);
            detalle.setVenta(venta);

            venta.getDetalles().add(detalle);
            totalVenta += subtotal;

            prodMongo.setStock(prodMongo.getStock() - item.getCantidad());
            productoRepository.save(prodMongo);
        }

        venta.setTotal(totalVenta);
        ventaRepository.save(venta);
    }

    public List<Venta> listarTodas() {
        return ventaRepository.findAll();
    }

    public void aprobarVenta(Long id) {
        Venta venta = ventaRepository.findById(id).orElse(null);
        if (venta != null) {
            venta.setEstado("COMPLETADA");
            ventaRepository.save(venta);
        }
    }

    public List<Venta> listarPorCliente(Long idCliente) {
        return ventaRepository.findByClienteIdOrderByFechaDesc(idCliente);
    }

    public Venta obtenerPorId(Long id) {
        return ventaRepository.findById(id).orElse(null);
    }

}