package com.example.demo.controller;

import com.example.demo.model.Factura;
import com.example.demo.model.NotaCredito;
import com.example.demo.service.INotaCreditoService;
import com.example.demo.service.IFacturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Controlador para gestionar las notas de crédito
 */
@Controller
@RequestMapping("/notas-credito")
public class NotaCreditoController {
    
    @Autowired
    private INotaCreditoService notaCreditoService;
    
    @Autowired
    private IFacturaService facturaService;
    
    /**
     * Lista todas las notas de crédito
     */
    @GetMapping
    public String listarNotasCredito(Model model) {
        List<NotaCredito> notasCredito = notaCreditoService.listarTodas();
        model.addAttribute("notasCredito", notasCredito);
        model.addAttribute("titulo", "Listado de Notas de Crédito");
        return "notas-credito/lista";
    }
    
    /**
     * Muestra el detalle de una nota de crédito
     */
    @GetMapping("/{id}")
    public String verDetalle(@PathVariable Long id, Model model, RedirectAttributes flash) {
        NotaCredito notaCredito = notaCreditoService.buscarPorId(id)
            .orElse(null);
        
        if (notaCredito == null) {
            flash.addFlashAttribute("error", "La nota de crédito no existe");
            return "redirect:/notas-credito";
        }
        
        model.addAttribute("notaCredito", notaCredito);
        model.addAttribute("titulo", "Detalle de Nota de Crédito " + notaCredito.getNumero());
        return "notas-credito/detalle";
    }
    
    /**
     * Muestra el formulario para generar una nota de crédito
     */
    @GetMapping("/generar/{facturaId}")
    public String mostrarFormularioGenerar(@PathVariable Long facturaId, 
                                          Model model, 
                                          RedirectAttributes flash) {
        Factura factura = facturaService.buscarPorId(facturaId)
            .orElse(null);
        
        if (factura == null) {
            flash.addFlashAttribute("error", "La factura no existe");
            return "redirect:/facturas";
        }
        
        // Verificar si se puede anular
        boolean puedeAnularse = notaCreditoService.puedeAnularse(facturaId);
        
        if (!puedeAnularse) {
            String motivo = notaCreditoService.obtenerMotivoNoAnulable(facturaId);
            flash.addFlashAttribute("error", "No se puede anular la factura: " + motivo);
            return "redirect:/facturas/" + facturaId;
        }
        
        model.addAttribute("factura", factura);
        model.addAttribute("titulo", "Generar Nota de Crédito");
        return "notas-credito/generar";
    }
    
    /**
     * Genera una nueva nota de crédito
     */
    @PostMapping("/generar")
    public String generarNotaCredito(@RequestParam Long facturaId,
                                     @RequestParam String motivo,
                                     RedirectAttributes flash) {
        try {
            NotaCredito notaCredito = notaCreditoService.generarNotaCredito(facturaId, motivo);
            flash.addFlashAttribute("success", 
                "Nota de crédito generada exitosamente: " + notaCredito.getNumero());
            return "redirect:/notas-credito/" + notaCredito.getId();
        } catch (Exception e) {
            flash.addFlashAttribute("error", 
                "Error al generar nota de crédito: " + e.getMessage());
            return "redirect:/facturas/" + facturaId;
        }
    }
}
