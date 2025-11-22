package com.example.demo.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import com.example.demo.modelo.Usuario;
import com.example.demo.servicio.UsuarioService;

@Controller // Usamos @Controller para devolver vistas HTML
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    // 1. Mostrar el formulario de Login
    @GetMapping("/login")
    public String login() {
        return "login/login"; // Busca el archivo login.html en templates
    }

    // 2. Mostrar el formulario de Registro
    @GetMapping("/registro")
    public String mostrarRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "login/registro"; // Busca registro.html
    }

    // 3. Procesar el Registro
    @PostMapping("/registro")
    public String registrarUsuario(@ModelAttribute Usuario usuario) {
        try {
            usuarioService.registrarUsuario(usuario);
            return "redirect:/login?registrado";
        } catch (IllegalArgumentException e) {
            // Diferenciar entre usuario duplicado y datos inválidos
            if (e.getMessage().contains("ya está en uso")) {
                return "redirect:/registro?error";
            }
            return "redirect:/registro?invalid";
        } catch (Exception e) {
            return "redirect:/registro?error";
        }
    }
}