package com.example.schedule.Service;

import com.example.schedule.Model.Cliente;
import java.util.List;
import java.util.Map;

public interface ClienteService {
    List<Cliente> listarClientes();

    Cliente guardarCliente(Cliente cliente);

    Cliente buscarPorDni(String dni);

    Cliente registrarNuevoCliente(Cliente cliente);

    void eliminarCliente(Long id);

    Cliente buscarPorCorreo(String correo);

    Map<String, Object> consultarReniec(String dni);

    Cliente buscarPorId(Long id);
}
