package com.example.schedule.Controller;

import com.example.schedule.Model.DetalleVenta;
import com.example.schedule.Model.Producto;
import com.example.schedule.Service.ProductoService;
import com.example.schedule.Repository.JPA.DetalleVentaRepository;

import java.time.format.DateTimeFormatter;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private DetalleVentaRepository detalleVentaRepository;

    @PostMapping("/guardar")
    public String guardarProducto(Producto producto) {
        try {
            productoService.guardarProducto(producto);
            return "redirect:/admin/dashboard?success=Producto+guardado+correctamente";
        } catch (Exception e) {
            return "redirect:/admin/dashboard?error=Error+al+guardar+producto";
        }
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarProducto(@PathVariable String id) {
        try {
            productoService.eliminarProducto(id);
            return "redirect:/admin/dashboard?success=Producto+eliminado+correctamente";
        } catch (Exception e) {
            return "redirect:/admin/dashboard?error=Error+al+eliminar+producto";
        }
    }

    @GetMapping("/api/{id}")
    @ResponseBody
    public Producto obtenerProducto(@PathVariable String id) {
        return productoService.obtenerPorId(id);
    }

    @GetMapping("/historial/{id}")
    @ResponseBody
    public ResponseEntity<?> obtenerHistorialProducto(@PathVariable String id) {
        try {
            List<DetalleVenta> movimientos = detalleVentaRepository.findByProductoId(id);

            List<Map<String, Object>> historial = new ArrayList<>();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

            for (DetalleVenta d : movimientos) {
                Map<String, Object> item = new HashMap<>();
                item.put("fecha", d.getVenta().getFecha().format(formatter));
                item.put("documento",
                        d.getVenta().getCodigoOperacion() != null ? d.getVenta().getCodigoOperacion() : "N/A");
                item.put("precio", d.getPrecioUnitario());
                item.put("cantidad", d.getCantidad());
                item.put("subtotal", d.getSubtotal());
                historial.add(item);
            }

            return ResponseEntity.ok(historial);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error al obtener historial: " + e.getMessage());
        }
    }
}