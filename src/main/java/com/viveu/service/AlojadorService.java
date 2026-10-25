package com.viveu.service;

import com.viveu.model.Alojador;
import com.viveu.repository.AlojadorRepository;
import com.viveu.repository.AlojamientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AlojadorService {

    @Autowired
    private AlojadorRepository alojadorRepository;

    @Autowired
    private AlojamientoRepository alojamientoRepository;

    public Alojador registrar(Alojador alojador) {
        // Al registrarse, la cuenta queda en estado PENDIENTE esperando validación del Administrador
        alojador.setEstadoVerificacion("PENDIENTE");
        alojador.setAntecedentesVerificados(false);
        alojador.setAceptaTerminosConvivencia(true);
        alojador.setFechaRegistro(java.time.LocalDateTime.now());
        return alojadorRepository.save(alojador);
    }

    public Optional<Alojador> aprobarAlojador(Long id) {
        return alojadorRepository.findById(id).map(alojador -> {
            alojador.setEstadoVerificacion("APROBADO");
            alojador.setAntecedentesVerificados(true);
            alojador.setFechaRevision(java.time.LocalDateTime.now());
            alojador.setMotivoRechazo(null);
            return alojadorRepository.save(alojador);
        });
    }

    public Optional<Alojador> denegarAlojador(Long id, String motivo) {
        return alojadorRepository.findById(id).map(alojador -> {
            alojador.setEstadoVerificacion("DENEGADO");
            alojador.setAntecedentesVerificados(false);
            alojador.setFechaRevision(java.time.LocalDateTime.now());
            alojador.setMotivoRechazo((motivo != null && !motivo.isBlank()) ? motivo.trim() : "Documentación o antecedentes no verificados satisfactoriamente.");
            return alojadorRepository.save(alojador);
        });
    }

    public Optional<Alojador> revocarAcceso(Long id, String motivo) {
        return alojadorRepository.findById(id).map(alojador -> {
            alojador.setEstadoVerificacion("DENEGADO");
            alojador.setAntecedentesVerificados(false);
            alojador.setFechaRevision(java.time.LocalDateTime.now());
            alojador.setMotivoRechazo((motivo != null && !motivo.isBlank()) ? motivo.trim() : "Acceso y verificación revocados por la administración de ViveU.");
            return alojadorRepository.save(alojador);
        });
    }

    @Transactional
    public boolean eliminarAlojador(Long id) {
        if (id == null || !alojadorRepository.existsById(id)) {
            return false;
        }
        // Eliminar las habitaciones asociadas publicadas por este alojador
        alojamientoRepository.deleteByAlojadorId(id);
        // Eliminar la cuenta del alojador
        alojadorRepository.deleteById(id);
        return true;
    }

    public java.util.List<Alojador> obtenerPendientes() {
        return alojadorRepository.findByEstadoVerificacionOrderByFechaRegistroDesc("PENDIENTE");
    }

    public java.util.List<Alojador> obtenerAprobados() {
        return alojadorRepository.findByEstadoVerificacionOrderByFechaRegistroDesc("APROBADO");
    }

    public java.util.List<Alojador> obtenerDenegados() {
        return alojadorRepository.findByEstadoVerificacionOrderByFechaRegistroDesc("DENEGADO");
    }

    public java.util.List<Alojador> obtenerTodos() {
        return alojadorRepository.findAllByOrderByFechaRegistroDesc();
    }

    public long contarPendientes() {
        return alojadorRepository.countByEstadoVerificacion("PENDIENTE");
    }

    public long contarAprobados() {
        return alojadorRepository.countByEstadoVerificacion("APROBADO");
    }

    public long contarDenegados() {
        return alojadorRepository.countByEstadoVerificacion("DENEGADO");
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
