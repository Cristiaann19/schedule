package com.example.schedule.Repository.JPA;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import com.example.schedule.Model.DetalleVenta;

public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Long> {

    @Query("SELECT d FROM DetalleVenta d JOIN FETCH d.venta WHERE d.productoId = :prodId ORDER BY d.venta.fecha DESC")
    List<DetalleVenta> findByProductoId(@Param("prodId") String prodId);
}
