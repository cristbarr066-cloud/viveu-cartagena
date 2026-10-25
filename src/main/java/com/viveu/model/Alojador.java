package com.viveu.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "alojadores")
public class Alojador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombreCompleto;

    @Column(nullable = false, unique = true)
    private String correo;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String telefonoWhatsapp;

    @Column(nullable = false)
    private String tipoDocumento; // Cédula de Ciudadanía, Cédula de Extranjería

    @Column(nullable = false)
    private String numeroCedula;

    @Lob
    @Column(columnDefinition = "CLOB")
    private String fotoRostroUrl; // Foto o selfie del arrendador para verificar identidad

    @Lob
    @Column(columnDefinition = "CLOB")
    private String fotoCedulaUrl; // Foto de la cédula de ciudadanía frente

    private String direccionResidencia; // Dirección particular en Cartagena

    private Boolean antecedentesVerificados = true; // Filtro de seguridad estudiantil

    private Boolean aceptaTerminosConvivencia = true;

    private String estadoVerificacion = "PENDIENTE"; // PENDIENTE, APROBADO, DENEGADO

    private String motivoRechazo;

    private LocalDateTime fechaRevision;

    private LocalDateTime fechaRegistro = LocalDateTime.now();

    public Alojador() {
    }

    public Alojador(String nombreCompleto, String correo, String password, String telefonoWhatsapp, 
                    String tipoDocumento, String numeroCedula, String fotoRostroUrl, 
                    String fotoCedulaUrl, String direccionResidencia) {
        this.nombreCompleto = nombreCompleto;
        this.correo = correo;
        this.password = password;
        this.telefonoWhatsapp = telefonoWhatsapp;
        this.tipoDocumento = tipoDocumento;
        this.numeroCedula = numeroCedula;
        this.fotoRostroUrl = fotoRostroUrl;
        this.fotoCedulaUrl = fotoCedulaUrl;
        this.direccionResidencia = direccionResidencia;
        this.antecedentesVerificados = true;
        this.aceptaTerminosConvivencia = true;
        this.estadoVerificacion = "VERIFICADO";
        this.fechaRegistro = LocalDateTime.now();
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTelefonoWhatsapp() {
        return telefonoWhatsapp;
    }

    public void setTelefonoWhatsapp(String telefonoWhatsapp) {
        this.telefonoWhatsapp = telefonoWhatsapp;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getNumeroCedula() {
        return numeroCedula;
    }

    public void setNumeroCedula(String numeroCedula) {
        this.numeroCedula = numeroCedula;
    }

    public String getFotoRostroUrl() {
        return fotoRostroUrl;
    }

    public void setFotoRostroUrl(String fotoRostroUrl) {
        this.fotoRostroUrl = fotoRostroUrl;
    }

    public String getFotoCedulaUrl() {
        return fotoCedulaUrl;
    }

    public void setFotoCedulaUrl(String fotoCedulaUrl) {
        this.fotoCedulaUrl = fotoCedulaUrl;
    }

    public String getDireccionResidencia() {
        return direccionResidencia;
    }

    public void setDireccionResidencia(String direccionResidencia) {
        this.direccionResidencia = direccionResidencia;
    }

    public Boolean getAntecedentesVerificados() {
        return antecedentesVerificados;
    }

    public void setAntecedentesVerificados(Boolean antecedentesVerificados) {
        this.antecedentesVerificados = antecedentesVerificados;
    }

    public Boolean getAceptaTerminosConvivencia() {
        return aceptaTerminosConvivencia;
    }

    public void setAceptaTerminosConvivencia(Boolean aceptaTerminosConvivencia) {
        this.aceptaTerminosConvivencia = aceptaTerminosConvivencia;
    }

    public String getEstadoVerificacion() {
        return estadoVerificacion;
    }

    public void setEstadoVerificacion(String estadoVerificacion) {
        this.estadoVerificacion = estadoVerificacion;
    }

    public String getMotivoRechazo() {
        return motivoRechazo;
    }

    public void setMotivoRechazo(String motivoRechazo) {
        this.motivoRechazo = motivoRechazo;
    }

    public LocalDateTime getFechaRevision() {
        return fechaRevision;
    }

    public void setFechaRevision(LocalDateTime fechaRevision) {
        this.fechaRevision = fechaRevision;
    }

    public boolean isAprobado() {
        return "APROBADO".equalsIgnoreCase(this.estadoVerificacion) || "VERIFICADO".equalsIgnoreCase(this.estadoVerificacion);
    }

    public boolean isPendiente() {
        return "PENDIENTE".equalsIgnoreCase(this.estadoVerificacion);
    }

    public boolean isDenegado() {
        return "DENEGADO".equalsIgnoreCase(this.estadoVerificacion);
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
}
