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
    public String aprobarSolicitud(@PathVariable Long id, HttpSession session) {
        if (session.getAttribute("adminLogueado") == null) {
            return "redirect:/admin/login";
        }
        alojadorService.aprobarAlojador(id);
        return "redirect:/admin/panel?tab=pendientes&aprobado=true";
    }

    @PostMapping("/alojador/{id}/denegar")
    public String denegarSolicitud(@PathVariable Long id,
                                   @RequestParam(required = false) String motivo,
                                   HttpSession session) {
        if (session.getAttribute("adminLogueado") == null) {
            return "redirect:/admin/login";
        }
        alojadorService.denegarAlojador(id, motivo);
        return "redirect:/admin/panel?tab=pendientes&denegado=true";
    }

    @PostMapping("/alojador/{id}/reactivar")
    public String reactivarSolicitud(@PathVariable Long id, HttpSession session) {
        if (session.getAttribute("adminLogueado") == null) {
            return "redirect:/admin/login";
        }
        alojadorService.aprobarAlojador(id);
        return "redirect:/admin/panel?tab=denegados&aprobado=true";
    }
}
