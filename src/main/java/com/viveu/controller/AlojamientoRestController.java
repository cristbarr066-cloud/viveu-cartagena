package com.viveu.controller;

import com.viveu.model.Alojamiento;
import com.viveu.service.AlojamientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class AlojamientoRestController {

    @Autowired
    private AlojamientoService alojamientoService;

    @GetMapping("/alojamientos")
    public List<Alojamiento> listarAlojamientos(
            @RequestParam(required = false) String sede,
            @RequestParam(required = false) Double precioMax,
            @RequestParam(required = false) Boolean soloVerificados,
            @RequestParam(required = false) Boolean aireAcondicionado,
            @RequestParam(required = false) Boolean incluyeServicios) {
        
        return alojamientoService.filtrar(sede, precioMax, soloVerificados, aireAcondicionado, incluyeServicios);
    }

    @GetMapping("/alojamientos/destacados")
    public List<Alojamiento> listarDestacados() {
        return alojamientoService.obtenerDestacados();
    }

    @GetMapping("/alojamientos/{id}")
    public ResponseEntity<Alojamiento> obtenerPorId(@PathVariable Long id) {
        return alojamientoService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/alojamientos/comparar")
    public List<Alojamiento> compararAlojamientos(@RequestBody List<Long> ids) {
        return alojamientoService.comparar(ids);
    }

    @PostMapping("/alojamientos")
    public ResponseEntity<Alojamiento> registrarAlojamiento(@RequestBody Alojamiento nuevo) {
        Alojamiento guardado = alojamientoService.guardar(nuevo);
        return ResponseEntity.ok(guardado);
    }

    @PostMapping("/asistente-ia")
    public ResponseEntity<Map<String, Object>> consultarAsistenteIA(@RequestBody Map<String, String> body) {
        String consulta = body.getOrDefault("consulta", "");
        if (consulta.trim().isEmpty()) {
            Map<String, Object> error = new HashMap<>();
            error.put("mensaje", "Por favor cuéntame qué sede o presupuesto buscas.");
            error.put("alojamientos", Collections.emptyList());
            return ResponseEntity.badRequest().body(error);
        }
        return ResponseEntity.ok(alojamientoService.consultarAsistenteIA(consulta));
    }

    @GetMapping("/simulador-transporte")
    public Map<String, Object> simularPresupuesto(
            @RequestParam Double precioArriendo,
            @RequestParam Integer viajesDiarios,
            @RequestParam(defaultValue = "22") Integer diasMes,
            @RequestParam(defaultValue = "3900") Double tarifaPasaje) {
        
        // Tarifa actual Transcaribe Cartagena 2026: 3.900 COP
        double costoPasaje = (tarifaPasaje != null && tarifaPasaje > 0) ? tarifaPasaje : 3900.0;
        int viajes = (viajesDiarios != null && viajesDiarios >= 0) ? viajesDiarios : 0;
        int dias = (diasMes != null && diasMes > 0) ? diasMes : 22;

        double gastoTransporteMensual = viajes * costoPasaje * dias;
        double gastoTotal = (precioArriendo != null ? precioArriendo : 0) + gastoTransporteMensual;

        Map<String, Object> resultado = new HashMap<>();
        resultado.put("precioArriendo", precioArriendo);
        resultado.put("viajesDiarios", viajes);
        resultado.put("diasMes", dias);
        resultado.put("tarifaPasaje", costoPasaje);
        resultado.put("totalViajesMes", viajes * dias);
        resultado.put("gastoTransporteMensual", gastoTransporteMensual);
        resultado.put("gastoTotalEstimado", gastoTotal);

        return resultado;
    }

    /**
     * Endpoint ligero para monitoreo y pings de keep-alive (cron-job.org)
     * Responde de inmediato con estado 200 sin consumir recursos del servidor.
     */
    @GetMapping({"/health", "/ping"})
    public ResponseEntity<Map<String, String>> healthCheck() {
        Map<String, String> status = new HashMap<>();
        status.put("status", "UP");
        status.put("app", "ViveU-Cartagena");
        return ResponseEntity.ok(status);
    }
}
