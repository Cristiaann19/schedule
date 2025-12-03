package com.example.schedule.Controller;

import com.example.schedule.Model.Cliente;
import com.example.schedule.Service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/admin/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @PostMapping("/guardar")
    public String guardarCliente(Cliente cliente) {
        try {
            clienteService.guardarCliente(cliente);
            return "redirect:/admin/dashboard?success=Cliente+guardado+correctamente";
        } catch (Exception e) {
            return "redirect:/admin/dashboard?error=Error+al+guardar+cliente";
        }
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarCliente(@PathVariable Long id) {
        clienteService.eliminarCliente(id);
        return "redirect:/admin/dashboard?success=Cliente+eliminado+correctamente";
    }

    @GetMapping("/api/{id}")
    @ResponseBody
    public Cliente obtenerClientePorId(@PathVariable Long id) {
        return clienteService.buscarPorId(id);
    }

    @GetMapping("/reniec/{dni}")
    @ResponseBody
    public Map<String, Object> consultarReniec(@PathVariable String dni) {
        return clienteService.consultarReniec(dni);
    }

}