package com.example.schedule.Service;

import com.example.schedule.Model.Cliente;
import com.example.schedule.Repository.JPA.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Primary;

import java.util.Collections;

@Service
@Primary
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        Cliente cliente = clienteRepository.findByCorreo(correo)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con correo: " + correo));

        String rol = cliente.getRol();
        if (rol == null) {
            rol = "CLIENTE";
        }

        if (!rol.startsWith("ROLE_")) {
            rol = "ROLE_" + rol;
        }

        GrantedAuthority authority = new SimpleGrantedAuthority(rol);

        return new User(
                cliente.getCorreo(),
                cliente.getPassword(),
                Collections.singletonList(authority));
    }
}