package com.viveu.controller;

import com.viveu.model.Alojador;
import com.viveu.model.Alojamiento;
import com.viveu.service.AlojadorService;
import com.viveu.service.AlojamientoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class AlojamientoController {

    @Autowired
    private AlojamientoService alojamientoService;

    @Autowired
    private AlojadorService alojadorService;

    @GetMapping("/")
    public String index(HttpSession session, Model model) {
        List<Alojamiento> todos = alojamientoService.obtenerTodos();
        List<Alojamiento> destacados = alojamientoService.obtenerDestacados();
        model.addAttribute("alojamientos", todos);
        model.addAttribute("destacados", destacados);
        model.addAttribute("totalAlojamientos", todos.size());
        
        Alojador alojadorLogueado = (Alojador) session.getAttribute("alojadorLogueado");
        model.addAttribute("alojadorLogueado", alojadorLogueado);
        return "index";
    }

    // ==========================================
    // FLUJO DE REGISTRO CON FILTROS Y VERIFICACIÓN
    // ==========================================
    @GetMapping("/registro-alojador")
    public String mostrarRegistroAlojador(HttpSession session, Model model) {
        if (session.getAttribute("alojadorLogueado") != null) {
            return "redirect:/mis-alojamientos";
        }
        model.addAttribute("nuevoAlojador", new Alojador());
        return "registro-alojador";
    }

    @PostMapping("/registro-alojador")
    public String procesarRegistroAlojador(@ModelAttribute Alojador nuevoAlojador,
                                           @RequestParam(required = false) String fotoRostroData,
                                           @RequestParam(required = false) String fotoCedulaData,
                                           HttpSession session,
                                           Model model) {
        // Validación de correo existente
        if (alojadorService.existeCorreo(nuevoAlojador.getCorreo())) {
            model.addAttribute("errorCorreo", "Ya existe una cuenta registrada con este correo electrónico.");
            model.addAttribute("nuevoAlojador", nuevoAlojador);
            return "registro-alojador";
        }

        // Validación de cédula existente
        if (alojadorService.existeCedula(nuevoAlojador.getNumeroCedula())) {
            model.addAttribute("errorCedula", "Este número de documento ya tiene un perfil de alojador asociado.");
            model.addAttribute("nuevoAlojador", nuevoAlojador);
            return "registro-alojador";
        }

        // Si se subió imagen en base64 desde el preview interactivo
        if (fotoRostroData != null && !fotoRostroData.isBlank()) {
            nuevoAlojador.setFotoRostroUrl(fotoRostroData);
        } else if (nuevoAlojador.getFotoRostroUrl() == null || nuevoAlojador.getFotoRostroUrl().isBlank()) {
            nuevoAlojador.setFotoRostroUrl("https://images.unsplash.com/photo-1544005313-94ddf0286df2?auto=format&fit=crop&w=400&q=80");
        }

        if (fotoCedulaData != null && !fotoCedulaData.isBlank()) {
            nuevoAlojador.setFotoCedulaUrl(fotoCedulaData);
        } else if (nuevoAlojador.getFotoCedulaUrl() == null || nuevoAlojador.getFotoCedulaUrl().isBlank()) {
            nuevoAlojador.setFotoCedulaUrl("https://images.unsplash.com/photo-1589330694653-dad6bc0140ad?auto=format&fit=crop&w=500&q=80");
        }

        // Guardar con verificación aprobada
        Alojador guardado = alojadorService.registrar(nuevoAlojador);
        
        // Iniciar sesión automáticamente
        session.setAttribute("alojadorLogueado", guardado);

        return "redirect:/publicar?bienvenido=true";
    }

    // ==========================================
    // INICIO Y CIERRE DE SESIÓN
    // ==========================================
    @GetMapping("/login")
    public String mostrarLogin(@RequestParam(required = false) String necesitaCuenta,
                               @RequestParam(required = false) String error,
                               HttpSession session,
                               Model model) {
        if (session.getAttribute("alojadorLogueado") != null) {
            return "redirect:/mis-alojamientos";
        }
        if (necesitaCuenta != null) {
            model.addAttribute("infoMsg", "Para publicar alojamientos debes iniciar sesión o registrarte como arrendador verificado.");
        }
        if (error != null) {
            model.addAttribute("errorMsg", "Correo electrónico o contraseña incorrectos. Verifica tus datos.");
        }
        return "login";
    }

    @PostMapping("/login")
    public String procesarLogin(@RequestParam String correo,
                                @RequestParam String password,
                                HttpSession session,
                                Model model) {
        Optional<Alojador> auth = alojadorService.autenticar(correo, password);
        if (auth.isPresent()) {
            session.setAttribute("alojadorLogueado", auth.get());
            return "redirect:/publicar";
        } else {
            return "redirect:/login?error=true";
        }
    }

    @GetMapping("/logout")
    public String cerrarSesion(HttpSession session) {
        session.invalidate();
        return "redirect:/?sesionCerrada=true";
    }

    // ==========================================
    // PUBLICACIÓN (PROTEGIDA POR CUENTA)
    // ==========================================
    @GetMapping("/publicar")
    public String mostrarFormularioPublicar(HttpSession session, Model model) {
        Alojador alojador = (Alojador) session.getAttribute("alojadorLogueado");
        if (alojador == null) {
            return "redirect:/login?necesitaCuenta=true";
        }

        Alojamiento nuevo = new Alojamiento();
        nuevo.setNombrePropietario(alojador.getNombreCompleto());
        nuevo.setTelefonoContacto(alojador.getTelefonoWhatsapp());

        model.addAttribute("alojadorLogueado", alojador);
        model.addAttribute("nuevoAlojamiento", nuevo);
        return "publicar";
    }

    @PostMapping("/publicar")
    public String procesarPublicacion(@ModelAttribute Alojamiento nuevoAlojamiento, 
                                      @RequestParam(defaultValue = "false") Boolean planDestacado,
                                      HttpSession session,
                                      Model model) {
        Alojador alojador = (Alojador) session.getAttribute("alojadorLogueado");
        if (alojador == null) {
            return "redirect:/login?necesitaCuenta=true";
        }

        nuevoAlojamiento.setAlojadorId(alojador.getId());
        nuevoAlojamiento.setNombrePropietario(alojador.getNombreCompleto());
        nuevoAlojamiento.setTelefonoContacto(alojador.getTelefonoWhatsapp());
        nuevoAlojamiento.setVerificadoViveU(true);
        nuevoAlojamiento.setDestacado(planDestacado);

        if (nuevoAlojamiento.getImagenUrl() == null || nuevoAlojamiento.getImagenUrl().isBlank()) {
            nuevoAlojamiento.setImagenUrl("https://images.unsplash.com/photo-1595526114035-0d45ed16cfbf?auto=format&fit=crop&w=800&q=80");
        }

        alojamientoService.guardar(nuevoAlojamiento);
        return "redirect:/mis-alojamientos?publicado=true";
    }

    // ==========================================
    // PANEL DEL ALOJADOR: MIS ALOJAMIENTOS
    // ==========================================
    @GetMapping("/mis-alojamientos")
    public String verMisAlojamientos(HttpSession session, Model model) {
        Alojador alojador = (Alojador) session.getAttribute("alojadorLogueado");
        if (alojador == null) {
            return "redirect:/login?necesitaCuenta=true";
        }

        List<Alojamiento> misAlojamientos = alojamientoService.obtenerPorAlojador(alojador.getId());
        model.addAttribute("alojadorLogueado", alojador);
        model.addAttribute("misAlojamientos", misAlojamientos);
        model.addAttribute("totalMisAlojamientos", misAlojamientos.size());
        return "mis-alojamientos";
    }

    @GetMapping({"/modelo-negocio", "/shark-tank"})
    public String verPresentacionModeloNegocio(HttpSession session, Model model) {
        model.addAttribute("equipo", List.of("Verónica Arrieta", "Maydee Hernández", "Luisa Cervantes"));
        model.addAttribute("asignatura", "Desarrollo del Espíritu Emprendedor - Universidad de Cartagena");
        model.addAttribute("alojadorLogueado", session.getAttribute("alojadorLogueado"));
        return "sharktank";
    }
}
