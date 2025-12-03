package com.example.schedule.Repository.JPA;

import com.example.schedule.Model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Cliente findByDni(String dni);

    Optional<Cliente> findByCorreo(String correo);

    boolean existsByCorreo(String correo);

    boolean existsByDni(String dni);
}
