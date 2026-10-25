package com.viveu.controller;

import com.viveu.model.Alojador;
import com.viveu.service.AlojadorService;
import com.viveu.service.AlojamientoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AlojadorService alojadorService;

    @Autowired
    private AlojamientoService alojamientoService;

    /**
     * Acceso Oculto al Login de Administrador
     * Se activa tras hacer 4 clics consecutivos en el logotipo de ViveU.
     */
    @GetMapping("/login")
    public String mostrarLoginAdmin(@RequestParam(required = false) String error,
                                    HttpSession session,
                                    Model model) {
        if (session.getAttribute("adminLogueado") != null) {
            return "redirect:/admin/panel";
        }
        if (error != null) {
            model.addAttribute("errorMsg", "Credenciales de administrador incorrectas. Acceso restringido.");
        }
        return "admin-login";
    }

    @PostMapping("/login")
    public String procesarLoginAdmin(@RequestParam String usuario,
                                     @RequestParam String password,
                                     HttpSession session) {
        // Credenciales seguras para administración de ViveU
        boolean credencialesValidas = 
            ("admin".equalsIgnoreCase(usuario.trim()) || "admin@viveu.com".equalsIgnoreCase(usuario.trim())) &&
            ("viveu2026".equals(password.trim()) || "admin".equals(password.trim()));

        if (credencialesValidas) {
            session.setAttribute("adminLogueado", true);
            session.setAttribute("adminNombre", "Administrador ViveU Cartagena");
            return "redirect:/admin/panel";
        } else {
            return "redirect:/admin/login?error=true";
        }
    }

    @GetMapping("/logout")
    public String cerrarSesionAdmin(HttpSession session) {
        session.removeAttribute("adminLogueado");
        session.removeAttribute("adminNombre");
        return "redirect:/?adminLogout=true";
    }

    /**
     * Panel Principal de Verificación y Moderación
     */
    @GetMapping("/panel")
    public String mostrarPanelAdmin(@RequestParam(required = false) String tab,
                                    @RequestParam(required = false) String aprobado,
                                    @RequestParam(required = false) String denegado,
                                    HttpSession session,
                                    Model model) {
        if (session.getAttribute("adminLogueado") == null) {
            return "redirect:/admin/login";
        }

        List<Alojador> pendientes = alojadorService.obtenerPendientes();
        List<Alojador> aprobados = alojadorService.obtenerAprobados();
        List<Alojador> denegados = alojadorService.obtenerDenegados();

        model.addAttribute("pendientes", pendientes);
        model.addAttribute("aprobados", aprobados);
        model.addAttribute("denegados", denegados);

        model.addAttribute("countPendientes", pendientes.size());
        model.addAttribute("countAprobados", aprobados.size());
        model.addAttribute("countDenegados", denegados.size());
        model.addAttribute("totalAlojamientos", alojamientoService.obtenerTodos().size());

        model.addAttribute("activeTab", (tab != null && !tab.isBlank()) ? tab : "pendientes");
        model.addAttribute("aprobadoExito", aprobado != null);
        model.addAttribute("denegadoExito", denegado != null);

        return "admin-panel";
    }

    @PostMapping("/alojador/{id}/aprobar")
    public String aprobarSolicitud(@PathVariable Long id, 
                                   HttpSession session, 
                                   org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttrs) {
        if (session.getAttribute("adminLogueado") == null) {
            return "redirect:/admin/login";
        }
        java.util.Optional<Alojador> opt = alojadorService.aprobarAlojador(id);
        if (opt.isPresent()) {
            Alojador alojador = opt.get();
            String tel = sanitizarTelefono(alojador.getTelefonoWhatsapp());
            String msg = "¡Hola " + alojador.getNombreCompleto() + "! 🎉 Tu cuenta de arrendador en ViveU Cartagena ha sido VERIFICADA Y APROBADA exitosamente.\n\nYa puedes ingresar con tu correo (" + alojador.getCorreo() + ") y comenzar a publicar tus habitaciones para los estudiantes de la Universidad de Cartagena: https://viveu-cartagena.onrender.com/login\n\n¡Bienvenido a ViveU Cartagena!";
            String encoded = java.net.URLEncoder.encode(msg, java.nio.charset.StandardCharsets.UTF_8);
            String waUrl = "https://api.whatsapp.com/send?phone=" + tel + "&text=" + encoded;

            redirectAttrs.addFlashAttribute("waUrl", waUrl);
            redirectAttrs.addFlashAttribute("waNombre", alojador.getNombreCompleto());
            redirectAttrs.addFlashAttribute("waTelefono", alojador.getTelefonoWhatsapp());
            redirectAttrs.addFlashAttribute("waAccion", "aprobado");
        }
        return "redirect:/admin/panel?tab=pendientes&aprobado=true";
    }

    @PostMapping("/alojador/{id}/denegar")
    public String denegarSolicitud(@PathVariable Long id,
                                   @RequestParam(required = false) String motivo,
                                   HttpSession session,
                                   org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttrs) {
        if (session.getAttribute("adminLogueado") == null) {
            return "redirect:/admin/login";
        }
        String motivoFinal = (motivo != null && !motivo.isBlank()) 
                ? motivo.trim() 
                : "La documentación adjunta o los datos de identidad no pudieron ser verificados satisfactoriamente.";
        
        java.util.Optional<Alojador> opt = alojadorService.denegarAlojador(id, motivoFinal);
        if (opt.isPresent()) {
            Alojador alojador = opt.get();
            String tel = sanitizarTelefono(alojador.getTelefonoWhatsapp());
            String msg = "Hola " + alojador.getNombreCompleto() + ". Te informamos desde ViveU Cartagena que tu solicitud de cuenta como arrendador no pudo ser aprobada en este momento.\n\nMotivo: " + motivoFinal + "\n\nSi deseas corregir tus documentos o comunicarte con nosotros, por favor responde a este mensaje. Atentamente, Equipo de Seguridad ViveU.";
            String encoded = java.net.URLEncoder.encode(msg, java.nio.charset.StandardCharsets.UTF_8);
            String waUrl = "https://api.whatsapp.com/send?phone=" + tel + "&text=" + encoded;

            redirectAttrs.addFlashAttribute("waUrl", waUrl);
            redirectAttrs.addFlashAttribute("waNombre", alojador.getNombreCompleto());
            redirectAttrs.addFlashAttribute("waTelefono", alojador.getTelefonoWhatsapp());
            redirectAttrs.addFlashAttribute("waAccion", "denegado");
        }
        return "redirect:/admin/panel?tab=pendientes&denegado=true";
    }

    @PostMapping("/alojador/{id}/reactivar")
    public String reactivarSolicitud(@PathVariable Long id, 
                                     HttpSession session,
                                     org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttrs) {
        if (session.getAttribute("adminLogueado") == null) {
            return "redirect:/admin/login";
        }
        java.util.Optional<Alojador> opt = alojadorService.aprobarAlojador(id);
        if (opt.isPresent()) {
            Alojador alojador = opt.get();
            String tel = sanitizarTelefono(alojador.getTelefonoWhatsapp());
            String msg = "¡Hola " + alojador.getNombreCompleto() + "! 🎉 Tu cuenta en ViveU Cartagena ha sido reactivada y APROBADA exitosamente. Ya puedes ingresar: https://viveu-cartagena.onrender.com/login";
            String encoded = java.net.URLEncoder.encode(msg, java.nio.charset.StandardCharsets.UTF_8);
            String waUrl = "https://api.whatsapp.com/send?phone=" + tel + "&text=" + encoded;

            redirectAttrs.addFlashAttribute("waUrl", waUrl);
            redirectAttrs.addFlashAttribute("waNombre", alojador.getNombreCompleto());
            redirectAttrs.addFlashAttribute("waTelefono", alojador.getTelefonoWhatsapp());
            redirectAttrs.addFlashAttribute("waAccion", "aprobado");
        }
        return "redirect:/admin/panel?tab=denegados&aprobado=true";
    }

    @PostMapping("/alojador/{id}/revocar")
    public String revocarAcceso(@PathVariable Long id,
                                @RequestParam(required = false) String motivo,
                                HttpSession session,
                                org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttrs) {
        if (session.getAttribute("adminLogueado") == null) {
            return "redirect:/admin/login";
        }
        String motivoFinal = (motivo != null && !motivo.isBlank()) 
                ? motivo.trim() 
                : "Acceso y verificación revocados por la administración de ViveU.";
        
        java.util.Optional<Alojador> opt = alojadorService.revocarAcceso(id, motivoFinal);
        if (opt.isPresent()) {
            Alojador alojador = opt.get();
            String tel = sanitizarTelefono(alojador.getTelefonoWhatsapp());
            String msg = "Hola " + alojador.getNombreCompleto() + ". Te informamos que tu acceso de arrendador en ViveU Cartagena ha sido REVOCADO por la administración. Motivo: " + motivoFinal;
            String encoded = java.net.URLEncoder.encode(msg, java.nio.charset.StandardCharsets.UTF_8);
            String waUrl = "https://api.whatsapp.com/send?phone=" + tel + "&text=" + encoded;

            redirectAttrs.addFlashAttribute("waUrl", waUrl);
            redirectAttrs.addFlashAttribute("waNombre", alojador.getNombreCompleto());
            redirectAttrs.addFlashAttribute("waTelefono", alojador.getTelefonoWhatsapp());
            redirectAttrs.addFlashAttribute("waAccion", "denegado");
        }
        redirectAttrs.addFlashAttribute("revocadoExito", true);
        return "redirect:/admin/panel?tab=aprobados";
    }

    @PostMapping("/alojador/{id}/eliminar")
    public String eliminarAlojador(@PathVariable Long id,
                                   @RequestParam(required = false, defaultValue = "aprobados") String tab,
                                   HttpSession session,
                                   org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttrs) {
        if (session.getAttribute("adminLogueado") == null) {
            return "redirect:/admin/login";
        }
        alojadorService.eliminarAlojador(id);
        redirectAttrs.addFlashAttribute("eliminadoExito", true);
        return "redirect:/admin/panel?tab=" + tab;
    }

    private String sanitizarTelefono(String raw) {
        if (raw == null) return "57";
        String digits = raw.replaceAll("[^0-9]", "");
        if (digits.length() == 10 && digits.startsWith("3")) {
            return "57" + digits; // Prefijo Colombia
        }
        return digits;
    }
}
