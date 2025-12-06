package com.example.demo.controller;

import com.example.demo.model.Plan;
import com.example.demo.service.IPlanService;
import com.example.demo.service.IServicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/planes")
public class PlanController {
    
    @Autowired
    private IPlanService planService;
    
    @Autowired
    private IServicioService servicioService;
    
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("planes", planService.listarTodos());
        model.addAttribute("titulo", "Gestión de Planes");
        model.addAttribute("seccion", "planes");
        return "planes/lista";
    }
    
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("plan", new Plan());
        model.addAttribute("servicios", servicioService.listarTodos());
        model.addAttribute("titulo", "Nuevo Plan");
        model.addAttribute("seccion", "planes");
        return "planes/form";
    }
    
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model, RedirectAttributes redirect) {
        Plan plan = planService.buscarPorId(id).orElse(null);
        
        if (plan == null) {
            redirect.addFlashAttribute("error", "El plan no existe");
            return "redirect:/planes";
        }
        
        model.addAttribute("plan", plan);
        model.addAttribute("servicios", servicioService.listarTodos());
        model.addAttribute("titulo", "Editar Plan");
        model.addAttribute("seccion", "planes");
        return "planes/form";
    }
    
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Plan plan, 
                         @RequestParam(required = false) List<Long> serviciosIds,
                         RedirectAttributes redirect) {
        try {
            // Guardar el plan primero
            Plan planGuardado = planService.guardar(plan);
            
            // Actualizar servicios incluidos (incluso si la lista está vacía, para limpiar)
            planService.actualizarServiciosIncluidos(planGuardado.getId(), serviciosIds);
            
            redirect.addFlashAttribute("mensaje", "Plan guardado correctamente");
        } catch (IllegalArgumentException e) {
            redirect.addFlashAttribute("error", e.getMessage());
            return "redirect:/planes/nuevo";
        }
        return "redirect:/planes";
    }
    
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            planService.eliminar(id);
            redirect.addFlashAttribute("mensaje", "Plan eliminado correctamente");
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "No se pudo eliminar el plan: " + e.getMessage());
        }
        return "redirect:/planes";
    }
}
