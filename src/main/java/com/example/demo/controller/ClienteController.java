package com.example.demo.controller;

import com.example.demo.model.Cliente;
import com.example.demo.model.CondicionFiscal;
import com.example.demo.service.ClienteServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteServiceImpl clienteService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("clientes", clienteService.listarTodos());
        model.addAttribute("titulo", "Gestión de Clientes");
        return "clientes/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("cliente", new Cliente());
        model.addAttribute("condicionesIVA", CondicionFiscal.values());
        model.addAttribute("titulo", "Nuevo Cliente");
        return "clientes/form";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model, RedirectAttributes redirect) {
        Cliente cliente = clienteService.buscarPorId(id).orElse(null);
        if (cliente == null) {
            redirect.addFlashAttribute("error", "El cliente no existe");
            return "redirect:/clientes";
        }
        model.addAttribute("cliente", cliente);
        model.addAttribute("condicionesIVA", CondicionFiscal.values());
        model.addAttribute("titulo", "Editar Cliente");
        return "clientes/form";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Cliente cliente, RedirectAttributes redirect) {
        try {
            clienteService.guardar(cliente);
            redirect.addFlashAttribute("mensaje", "Cliente guardado correctamente");
        } catch (IllegalArgumentException e) {
            redirect.addFlashAttribute("error", e.getMessage());
            return "redirect:/clientes/nuevo";
        }
        return "redirect:/clientes";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            clienteService.eliminar(id);
            redirect.addFlashAttribute("mensaje", "Cliente eliminado correctamente");
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "No se pudo eliminar el cliente: " + e.getMessage());
        }
        return "redirect:/clientes";
    }
}
