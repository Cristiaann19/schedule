package com.example.schedule.Config;

import com.example.schedule.Model.Rol;
import com.example.schedule.Model.Usuario;
import com.example.schedule.Repository.JPA.RolRepository;
import com.example.schedule.Repository.JPA.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        crearRolSiNoExiste("ROLE_ADMIN");
        crearRolSiNoExiste("ROLE_VETERINARIO");
        crearRolSiNoExiste("ROLE_ASISTENTE");
        crearRolSiNoExiste("ROLE_CLIENTE");

        crearUsuarioSiNoExiste("admin", "admin", "ROLE_ADMIN");
        crearUsuarioSiNoExiste("vet", "vet", "ROLE_VETERINARIO");
        crearUsuarioSiNoExiste("asistente", "asistente", "ROLE_ASISTENTE");
    }

    private void crearRolSiNoExiste(String nombre) {
        if (rolRepository.findByNombre(nombre).isEmpty()) {
            Rol rol = new Rol();
            rol.setNombre(nombre);
            rolRepository.save(rol);
        }
    }

    private void crearUsuarioSiNoExiste(String username, String password, String nombreRol) {
        if (usuarioRepository.findByUsername(username).isEmpty()) {
            Usuario usuario = new Usuario();
            usuario.setUsername(username);
            usuario.setPassword(passwordEncoder.encode(password));
            usuario.setEnabled(true);

            Rol rol = rolRepository.findByNombre(nombreRol)
                    .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + nombreRol));

            usuario.setRoles(Collections.singleton(rol));
            usuarioRepository.save(usuario);
        }
    }
}
