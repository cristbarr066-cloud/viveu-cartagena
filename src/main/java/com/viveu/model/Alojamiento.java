package com.viveu.model;

import jakarta.persistence.*;

@Entity
@Table(name = "alojamientos")
public class Alojamiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(length = 1000)
    private String descripcion;

    @Column(nullable = false)
    private Double precioMensual;

    @Column(nullable = false)
    private String sector; // Zaragocilla, Piedra de Bolívar, Amberes, Torices, etc.

    @Column(nullable = false)
    private String sedeCercana; // Zaragocilla (Salud), Piedra de Bolívar (Ingeniería), San Agustín (Centro), San Pablo

    private Integer distanciaMinutos; // Minutos a pie o Transcaribe a la sede

    private String tipoHabitacion; // Individual con baño privado, Individual con baño compartido, Compartida doble

    private Boolean incluyeServicios; // Luz, Agua, Gas

    private Boolean wifiFibraOptica;

    private Boolean aireAcondicionado;

    private Boolean abanico;

    private Boolean cocinaDisponible;

    private Boolean zonaLavanderia;

    private Boolean permiteVisitas;

    private Boolean destacado; // Para monetización $15.000 COP

    private Boolean verificadoViveU; // Filtro de calidad y seguridad

    private String imagenUrl;

    private String nombrePropietario;

    private String telefonoContacto;

    @Column(length = 500)
    private String normasConvivencia;

    private Double puntuacionSeguridad; // e.g. 4.8

    private String direccion; // Dirección en Cartagena (ej: Cra 50 # 30-15)

    private Double latitud; // Coordenadas GPS para el mapa

    private Double longitud;

    private Long alojadorId; // ID del alojador registrado y verificado

    public Alojamiento() {
    }

    public Alojamiento(String titulo, String descripcion, Double precioMensual, String sector, 
                       String sedeCercana, Integer distanciaMinutos, String tipoHabitacion, 
                       Boolean incluyeServicios, Boolean wifiFibraOptica, Boolean aireAcondicionado, 
                       Boolean abanico, Boolean cocinaDisponible, Boolean zonaLavanderia, 
                       Boolean permiteVisitas, Boolean destacado, Boolean verificadoViveU, 
                       String imagenUrl, String nombrePropietario, String telefonoContacto, 
                       String normasConvivencia, Double puntuacionSeguridad) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.precioMensual = precioMensual;
        this.sector = sector;
        this.sedeCercana = sedeCercana;
        this.distanciaMinutos = distanciaMinutos;
        this.tipoHabitacion = tipoHabitacion;
        this.incluyeServicios = incluyeServicios;
        this.wifiFibraOptica = wifiFibraOptica;
        this.aireAcondicionado = aireAcondicionado;
        this.abanico = abanico;
        this.cocinaDisponible = cocinaDisponible;
        this.zonaLavanderia = zonaLavanderia;
        this.permiteVisitas = permiteVisitas;
        this.destacado = destacado;
        this.verificadoViveU = verificadoViveU;
        this.imagenUrl = imagenUrl;
        this.nombrePropietario = nombrePropietario;
        this.telefonoContacto = telefonoContacto;
        this.normasConvivencia = normasConvivencia;
        this.puntuacionSeguridad = puntuacionSeguridad;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getPrecioMensual() {
        return precioMensual;
    }

    public void setPrecioMensual(Double precioMensual) {
        this.precioMensual = precioMensual;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getSedeCercana() {
        return sedeCercana;
    }

    public void setSedeCercana(String sedeCercana) {
        this.sedeCercana = sedeCercana;
    }

    public Integer getDistanciaMinutos() {
        return distanciaMinutos;
    }

    public void setDistanciaMinutos(Integer distanciaMinutos) {
        this.distanciaMinutos = distanciaMinutos;
    }

    public String getTipoHabitacion() {
        return tipoHabitacion;
    }

    public void setTipoHabitacion(String tipoHabitacion) {
        this.tipoHabitacion = tipoHabitacion;
    }

    public Boolean getIncluyeServicios() {
        return incluyeServicios;
    }

    public void setIncluyeServicios(Boolean incluyeServicios) {
        this.incluyeServicios = incluyeServicios;
    }

    public Boolean getWifiFibraOptica() {
        return wifiFibraOptica;
    }

    public void setWifiFibraOptica(Boolean wifiFibraOptica) {
        this.wifiFibraOptica = wifiFibraOptica;
    }

    public Boolean getAireAcondicionado() {
        return aireAcondicionado;
    }

    public void setAireAcondicionado(Boolean aireAcondicionado) {
        this.aireAcondicionado = aireAcondicionado;
    }

    public Boolean getAbanico() {
        return abanico;
    }

    public void setAbanico(Boolean abanico) {
        this.abanico = abanico;
    }

    public Boolean getCocinaDisponible() {
        return cocinaDisponible;
    }

    public void setCocinaDisponible(Boolean cocinaDisponible) {
        this.cocinaDisponible = cocinaDisponible;
    }

    public Boolean getZonaLavanderia() {
        return zonaLavanderia;
    }

    public void setZonaLavanderia(Boolean zonaLavanderia) {
        this.zonaLavanderia = zonaLavanderia;
    }

    public Boolean getPermiteVisitas() {
        return permiteVisitas;
    }

    public void setPermiteVisitas(Boolean permiteVisitas) {
        this.permiteVisitas = permiteVisitas;
    }

    public Boolean getDestacado() {
        return destacado;
    }

    public void setDestacado(Boolean destacado) {
        this.destacado = destacado;
    }

    public Boolean getVerificadoViveU() {
        return verificadoViveU;
    }

    public void setVerificadoViveU(Boolean verificadoViveU) {
        this.verificadoViveU = verificadoViveU;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public String getNombrePropietario() {
        return nombrePropietario;
    }

    public void setNombrePropietario(String nombrePropietario) {
        this.nombrePropietario = nombrePropietario;
    }

    public String getTelefonoContacto() {
        return telefonoContacto;
    }

    public void setTelefonoContacto(String telefonoContacto) {
        this.telefonoContacto = telefonoContacto;
    }

    public String getNormasConvivencia() {
        return normasConvivencia;
    }

    public void setNormasConvivencia(String normasConvivencia) {
        this.normasConvivencia = normasConvivencia;
    }

    public Double getPuntuacionSeguridad() {
        return puntuacionSeguridad;
    }

    public void setPuntuacionSeguridad(Double puntuacionSeguridad) {
        this.puntuacionSeguridad = puntuacionSeguridad;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public Long getAlojadorId() {
        return alojadorId;
    }

    public void setAlojadorId(Long alojadorId) {
        this.alojadorId = alojadorId;
    }
}
