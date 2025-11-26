package com.example.demo.controller;

import com.example.demo.model.Servicio;
import com.example.demo.service.IServicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/servicios")
public class ServicioController {

    @Autowired
    private IServicioService servicioService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("servicios", servicioService.listarTodos());
        return "servicios/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        Servicio servicio = new Servicio();
        servicio.setAlicuotaIVA(21.0); // IVA por defecto 21%
        model.addAttribute("servicio", servicio);
        model.addAttribute("titulo", "Nuevo Servicio");
        return "servicios/form";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model, RedirectAttributes flash) {
        Servicio servicio = servicioService.buscarPorId(id).orElse(null);
        
        if (servicio == null) {
            flash.addFlashAttribute("error", "El servicio no existe");
            return "redirect:/servicios";
        }
        
        model.addAttribute("servicio", servicio);
        model.addAttribute("titulo", "Editar Servicio");
        return "servicios/form";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Servicio servicio, RedirectAttributes flash) {
        try {
            servicioService.guardar(servicio);
            String mensaje = (servicio.getId() != null) ? "Servicio actualizado correctamente" : "Servicio creado correctamente";
            flash.addFlashAttribute("success", mensaje);
        } catch (IllegalArgumentException e) {
            flash.addFlashAttribute("error", e.getMessage());
            return "redirect:/servicios/" + (servicio.getId() != null ? "editar/" + servicio.getId() : "nuevo");
        }
        return "redirect:/servicios";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes flash) {
        try {
            servicioService.eliminar(id);
            flash.addFlashAttribute("success", "Servicio eliminado correctamente");
        } catch (IllegalArgumentException e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/servicios";
    }
}
