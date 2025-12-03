package com.example.schedule.Controller;

import com.example.schedule.Model.Cliente;
import com.example.schedule.Service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegistroController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/registro")
    public String registrarUsuario(
            @RequestParam String nombres,
            @RequestParam String apellidos,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String dni,
            @RequestParam String direccion,
            @RequestParam(required = false) String telefono) {

        try {
            Cliente nuevoCliente = new Cliente();
            nuevoCliente.setNombres(nombres);
            nuevoCliente.setApellidos(apellidos);
            nuevoCliente.setCorreo(email);
            nuevoCliente.setPassword(password);
            nuevoCliente.setDni(dni);
            nuevoCliente.setDireccion(direccion);
            nuevoCliente.setTelefono(telefono);

            clienteService.registrarNuevoCliente(nuevoCliente);

            return "redirect:/login?registroExitoso=true";

        } catch (Exception e) {
            return "redirect:/login?error=true";
        }
    }
}