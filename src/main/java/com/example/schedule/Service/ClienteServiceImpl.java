package com.example.schedule.Service;

import com.example.schedule.Model.Cliente;
import com.example.schedule.Repository.JPA.ClienteRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

import java.util.List;

@Service
@SuppressWarnings("null")
public class ClienteServiceImpl implements ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private AuditoriaService auditoriaService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<Cliente> listarClientes() {
        return clienteRepository.findAll();
    }

    @Override
    public Cliente guardarCliente(Cliente cliente) {
        if (cliente.getId() != null) {
            Cliente clienteExistente = clienteRepository.findById(cliente.getId()).orElse(null);

            if (clienteExistente != null) {
                if (cliente.getPassword() == null || cliente.getPassword().isEmpty()) {
                    cliente.setPassword(clienteExistente.getPassword());
                } else {
                    if (!cliente.getPassword().startsWith("$2a$")) {
                        cliente.setPassword(passwordEncoder.encode(cliente.getPassword()));
                    }
                }

                if (cliente.getRol() == null) {
                    cliente.setRol(clienteExistente.getRol());
                }
            }
        } else {
            if (cliente.getPassword() != null && !cliente.getPassword().isEmpty()) {
                cliente.setPassword(passwordEncoder.encode(cliente.getPassword()));
            }

            if (cliente.getRol() == null) {
                cliente.setRol("CLIENTE");
            }
        }

        Cliente clienteGuardado = clienteRepository.save(cliente);

        String accion = (cliente.getId() != null) ? "CLIENTE_ACTUALIZADO" : "CLIENTE_CREADO";
        auditoriaService.registrarEvento(accion, "ADMIN", "DNI: " + cliente.getDni(), null);

        return clienteGuardado;
    }

    public Cliente registrarNuevoCliente(Cliente cliente) {
        String passEncriptada = passwordEncoder.encode(cliente.getPassword());
        cliente.setPassword(passEncriptada);

        cliente.setRol("CLIENTE");

        Cliente nuevoCliente = clienteRepository.save(cliente);

        auditoriaService.registrarEvento("AUTO_REGISTRO", "WEB_PUBLIC", "Usuario se registró: " + cliente.getCorreo(),
                null);

        return nuevoCliente;
    }

    @Override
    public Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id).orElse(null);
    }

    @Override
    public Cliente buscarPorDni(String dni) {
        return clienteRepository.findByDni(dni);
    }

    @Override
    public Cliente buscarPorCorreo(String correo) {
        return clienteRepository.findByCorreo(correo).orElse(null);
    }

    @Override
    public void eliminarCliente(Long id) {
        clienteRepository.deleteById(id);
        auditoriaService.registrarEvento("CLIENTE_ELIMINADO", "ADMIN", "Se eliminó al cliente ID " + id, null);
    }

    private final String API_URL = "https://miapi.cloud/v1/dni/";
    private final String TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjo2MjMsImV4cCI6MTc2NTA4MjQyM30.syuuoYlMvxHXakCtMHYghqsNNBJCEJeaP6VHRa_ZHvY";

    @Override
    public Map<String, Object> consultarReniec(String dni) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            String url = API_URL + dni + "?token=" + TOKEN;

            return restTemplate.getForObject(url, Map.class);

        } catch (Exception e) {
            System.err.println("Error en API Reniec: " + e.getMessage());
            return Map.of("success", false, "mensaje", "Error interno o DNI no encontrado");
        }
    }
}
