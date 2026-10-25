package com.viveu.service;

import com.viveu.model.Alojamiento;
import com.viveu.repository.AlojamientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class AlojamientoService {

    @Autowired
    private AlojamientoRepository alojamientoRepository;

    public List<Alojamiento> obtenerTodos() {
        return alojamientoRepository.findAll();
    }

    public List<Alojamiento> obtenerDestacados() {
        return alojamientoRepository.findByDestacadoTrue();
    }

    public Optional<Alojamiento> obtenerPorId(Long id) {
        return alojamientoRepository.findById(id);
    }

    public List<Alojamiento> obtenerPorAlojador(Long alojadorId) {
        if (alojadorId == null) return Collections.emptyList();
        return alojamientoRepository.findByAlojadorId(alojadorId);
    }

    public Alojamiento guardar(Alojamiento alojamiento) {
        if (alojamiento.getPuntuacionSeguridad() == null) {
            alojamiento.setPuntuacionSeguridad(4.7);
        }
        if (alojamiento.getVerificadoViveU() == null) {
            alojamiento.setVerificadoViveU(true);
        }
        return alojamientoRepository.save(alojamiento);
    }

    public List<Alojamiento> filtrar(String sede, String tipo, Double precioMax, Boolean soloVerificados, 
                                     Boolean aireAcondicionado, Boolean incluyeServicios) {
        return alojamientoRepository.filtrarAlojamientos(
                (sede != null && !sede.isBlank()) ? sede : null,
                (tipo != null && !tipo.isBlank()) ? tipo : null,
                precioMax,
                soloVerificados,
                aireAcondicionado,
                incluyeServicios
        );
    }

    public List<Alojamiento> comparar(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }
        return alojamientoRepository.findAllById(ids);
    }

    /**
     * Procesador Inteligente del Asistente Estudiantil IA
     * Analiza texto libre del estudiante (ej: "estudio medicina y busco algo de 400 mil con wifi cerca")
     * y genera una recomendación contextualizada con puntuación de afinidad.
     */
    public Map<String, Object> consultarAsistenteIA(String consulta) {
        Map<String, Object> respuesta = new HashMap<>();
        String consultaLower = consulta.toLowerCase();

        List<Alojamiento> todos = alojamientoRepository.findAll();
        
        // Extracción heurística de presupuesto
        Double presupuestoDetectado = null;
        Pattern patternNumero = Pattern.compile("(\\d{3})[.\\s]?(\\d{3})|(\\d{3})k|(\\d{3})\\s?mil");
        Matcher matcher = patternNumero.matcher(consultaLower);
        if (matcher.find()) {
            String match = matcher.group();
            match = match.replace(".", "").replace("k", "000").replace("mil", "000").replaceAll("\\s", "");
            try {
                presupuestoDetectado = Double.parseDouble(match);
            } catch (NumberFormatException ignored) {}
        }

        // Detección de sede por carreras o nombres comunes
        String sedePrioritaria = null;
        if (consultaLower.contains("medicina") || consultaLower.contains("salud") || consultaLower.contains("enfermer") || consultaLower.contains("zaragocilla")) {
            sedePrioritaria = "Zaragocilla";
        } else if (consultaLower.contains("ingenier") || consultaLower.contains("quimica") || consultaLower.contains("bolivar") || consultaLower.contains("piedra")) {
            sedePrioritaria = "Piedra de Bolívar";
        } else if (consultaLower.contains("derecho") || consultaLower.contains("filosofia") || consultaLower.contains("centro") || consultaLower.contains("agustin")) {
            sedePrioritaria = "San Agustín";
        } else if (consultaLower.contains("san pablo")) {
            sedePrioritaria = "San Pablo";
        }

        boolean requiereAire = consultaLower.contains("aire") || consultaLower.contains("acondicionado");
        boolean buscaEconomico = consultaLower.contains("barato") || consultaLower.contains("economico") || consultaLower.contains("bajo costo");

        // Ponderación de recomendaciones
        final Double finalPresupuesto = presupuestoDetectado;
        final String finalSede = sedePrioritaria;

        List<Alojamiento> recomendados = todos.stream()
                .sorted((a1, a2) -> {
                    int score1 = calcularScore(a1, finalSede, finalPresupuesto, requiereAire, buscaEconomico);
                    int score2 = calcularScore(a2, finalSede, finalPresupuesto, requiereAire, buscaEconomico);
                    return Integer.compare(score2, score1);
                })
                .limit(3)
                .collect(Collectors.toList());

        StringBuilder mensajeIA = new StringBuilder();
        mensajeIA.append("¡Hola! He analizado tu solicitud para Cartagena. ");
        if (sedePrioritaria != null) {
            mensajeIA.append("Detecté que te conviene estar cerca de la **Sede ").append(sedePrioritaria).append("**. ");
        }
        if (presupuestoDetectado != null) {
            mensajeIA.append("Consideré tu rango de presupuesto de aprox. **$")
                     .append(String.format("%,.0f", presupuestoDetectado)).append(" COP**. ");
        }
        if (requiereAire) {
            mensajeIA.append("Prioricé opciones con aire acondicionado para el clima cartagenero. ");
        }
        mensajeIA.append("Encontré las opciones ideales que optimizan tu tiempo de traslado y gastos de manutención:");

        respuesta.put("mensaje", mensajeIA.toString());
        respuesta.put("alojamientos", recomendados);
        respuesta.put("sedeDetectada", sedePrioritaria);
        respuesta.put("presupuestoDetectado", presupuestoDetectado);

        return respuesta;
    }

    private int calcularScore(Alojamiento a, String sede, Double presupuesto, boolean aire, boolean economico) {
        int score = 0;
        if (a.getVerificadoViveU()) score += 15;
        if (a.getDestacado()) score += 10;
        if (sede != null && a.getSedeCercana().toLowerCase().contains(sede.toLowerCase())) {
            score += 50;
        }
        if (presupuesto != null && a.getPrecioMensual() <= presupuesto) {
            score += 40;
        } else if (presupuesto != null && a.getPrecioMensual() <= (presupuesto + 50000)) {
            score += 20;
        }
        if (aire && a.getAireAcondicionado()) score += 25;
        if (economico && a.getPrecioMensual() <= 400000) score += 30;
        if (a.getIncluyeServicios()) score += 15;
        if (a.getWifiFibraOptica()) score += 10;
        return score;
    }
}
