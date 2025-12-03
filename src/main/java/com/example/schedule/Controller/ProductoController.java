package com.example.schedule.Controller;

import com.example.schedule.Model.Producto;
import com.example.schedule.Service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

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
}