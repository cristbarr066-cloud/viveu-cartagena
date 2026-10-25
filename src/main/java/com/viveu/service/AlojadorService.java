package com.viveu.service;

import com.viveu.model.Alojador;
import com.viveu.repository.AlojadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AlojadorService {

    @Autowired
    private AlojadorRepository alojadorRepository;

    public Alojador registrar(Alojador alojador) {
        // En una app real se aplicaría BCrypt, aquí almacenamos para autenticación rápida
        alojador.setEstadoVerificacion("VERIFICADO");
        alojador.setAntecedentesVerificados(true);
        alojador.setAceptaTerminosConvivencia(true);
        return alojadorRepository.save(alojador);
    }

    public Optional<Alojador> autenticar(String correo, String password) {
        if (correo == null || password == null) return Optional.empty();
        return alojadorRepository.findByCorreoIgnoreCase(correo.trim())
                .filter(a -> a.getPassword().equals(password.trim()));
    }

    public Optional<Alojador> buscarPorCorreo(String correo) {
        if (correo == null) return Optional.empty();
        return alojadorRepository.findByCorreoIgnoreCase(correo.trim());
    }

    public boolean existeCorreo(String correo) {
        if (correo == null) return false;
        return alojadorRepository.existsByCorreoIgnoreCase(correo.trim());
    }

    public boolean existeCedula(String numeroCedula) {
        if (numeroCedula == null) return false;
        return alojadorRepository.existsByNumeroCedula(numeroCedula.trim());
    }

    public Optional<Alojador> obtenerPorId(Long id) {
        return alojadorRepository.findById(id);
    }

    public long totalAlojadores() {
        return alojadorRepository.count();
    }
}
