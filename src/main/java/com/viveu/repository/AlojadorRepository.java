package com.viveu.repository;

import com.viveu.model.Alojador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AlojadorRepository extends JpaRepository<Alojador, Long> {

    Optional<Alojador> findByCorreoIgnoreCase(String correo);

    boolean existsByCorreoIgnoreCase(String correo);

    boolean existsByNumeroCedula(String numeroCedula);
}
