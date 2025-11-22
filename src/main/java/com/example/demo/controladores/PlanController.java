package com.example.demo.controladores;

import com.example.demo.modelo.Plan;
import com.example.demo.servicio.IPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/planes")
public class PlanController {
    
    @Autowired
    private IPlanService planService;
    
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
        model.addAttribute("titulo", "Editar Plan");
        model.addAttribute("seccion", "planes");
        return "planes/form";
    }
    
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Plan plan, RedirectAttributes redirect) {
        try {
            planService.guardar(plan);
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
