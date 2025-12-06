package com.example.demo.controller;

import com.example.demo.model.Factura;
import com.example.demo.model.enums.EstadoFactura;
import com.example.demo.service.IFacturaService;
import com.example.demo.service.ICuentaClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

/**
 * Controlador para gestionar las facturas
 */
@Controller
@RequestMapping("/facturas")
public class FacturaController {
    
    @Autowired
    private IFacturaService facturaService;
    
    @Autowired
    private ICuentaClienteService cuentaClienteService;
    
    /**
     * Lista todas las facturas
     */
    @GetMapping
    public String listarFacturas(Model model) {
        List<Factura> facturas = facturaService.listarTodas();
        model.addAttribute("facturas", facturas);
        model.addAttribute("titulo", "Listado de Facturas");
        return "facturas/lista";
    }
    
    /**
     * Muestra el detalle de una factura
     */
    @GetMapping("/{id}")
    public String verDetalle(@PathVariable Long id, Model model, RedirectAttributes flash) {
        Factura factura = facturaService.buscarPorId(id)
            .orElse(null);
        
        if (factura == null) {
            flash.addFlashAttribute("error", "La factura no existe");
            return "redirect:/facturas";
        }
        
        model.addAttribute("factura", factura);
        model.addAttribute("titulo", "Detalle de Factura " + factura.getNumero());
        return "facturas/detalle";
    }
    
    /**
     * Muestra el formulario para generar una nueva factura
     */
    @GetMapping("/generar")
    public String mostrarFormularioGenerar(Model model) {
        model.addAttribute("cuentas", cuentaClienteService.listarTodas());
        model.addAttribute("titulo", "Generar Factura");
        return "facturas/generar";
    }
    
    /**
     * Genera una nueva factura para una cuenta
     */
    @PostMapping("/generar")
    public String generarFactura(@RequestParam Long cuentaClienteId, 
                                 RedirectAttributes flash) {
        try {
            Factura factura = facturaService.generarFacturaIndividual(cuentaClienteId);
            flash.addFlashAttribute("success", 
                "Factura generada exitosamente: " + factura.getNumero());
            return "redirect:/facturas/" + factura.getId();
        } catch (Exception e) {
            flash.addFlashAttribute("error", 
                "Error al generar la factura: " + e.getMessage());
            return "redirect:/facturas/generar";
        }
    }
    
    /**
     * Lista facturas por estado
     */
    @GetMapping("/estado/{estado}")
    public String listarPorEstado(@PathVariable String estado, Model model) {
        try {
            EstadoFactura estadoFactura = EstadoFactura.valueOf(estado.toUpperCase());
            List<Factura> facturas = facturaService.listarPorEstado(estadoFactura);
            model.addAttribute("facturas", facturas);
            model.addAttribute("titulo", "Facturas - " + estadoFactura);
            return "facturas/lista";
        } catch (IllegalArgumentException e) {
            return "redirect:/facturas";
        }
    }
    
    /**
     * Lista facturas vencidas
     */
    @GetMapping("/vencidas")
    public String listarVencidas(Model model) {
        List<Factura> facturas = facturaService.listarFacturasVencidas();
        model.addAttribute("facturas", facturas);
        model.addAttribute("titulo", "Facturas Vencidas");
        return "facturas/lista";
    }
    
    /**
     * Lista facturas de una cuenta específica
     */
    @GetMapping("/cuenta/{cuentaId}")
    public String listarPorCuenta(@PathVariable Long cuentaId, Model model) {
        List<Factura> facturas = facturaService.listarPorCuentaCliente(cuentaId);
        model.addAttribute("facturas", facturas);
        model.addAttribute("titulo", "Facturas de la Cuenta #" + cuentaId);
        return "facturas/lista";
    }
    
    /**
     * Marca una factura como pagada
     */
    @PostMapping("/{id}/pagar")
    public String marcarComoPagada(@PathVariable Long id, RedirectAttributes flash) {
        try {
            Factura factura = facturaService.marcarComoPagada(id);
            flash.addFlashAttribute("success", 
                "Factura " + factura.getNumero() + " marcada como pagada");
        } catch (Exception e) {
            flash.addFlashAttribute("error", 
                "Error al marcar como pagada: " + e.getMessage());
        }
        return "redirect:/facturas/" + id;
    }
    
    /**
     * Anula una factura
     */
    @PostMapping("/{id}/anular")
    public String anularFactura(@PathVariable Long id, RedirectAttributes flash) {
        try {
            Factura factura = facturaService.marcarComoAnulada(id);
            flash.addFlashAttribute("success", 
                "Factura " + factura.getNumero() + " anulada correctamente");
        } catch (Exception e) {
            flash.addFlashAttribute("error", 
                "Error al anular la factura: " + e.getMessage());
        }
        return "redirect:/facturas/" + id;
    }
    
    /**
     * Busca facturas por rango de fechas
     */
    @GetMapping("/buscar")
    public String buscarPorFechas(@RequestParam(required = false) String desde,
                                   @RequestParam(required = false) String hasta,
                                   Model model) {
        if (desde != null && hasta != null) {
            LocalDate fechaDesde = LocalDate.parse(desde);
            LocalDate fechaHasta = LocalDate.parse(hasta);
            List<Factura> facturas = facturaService.listarPorRangoFechas(fechaDesde, fechaHasta);
            model.addAttribute("facturas", facturas);
            model.addAttribute("titulo", "Facturas desde " + desde + " hasta " + hasta);
            return "facturas/lista";
        }
        
        model.addAttribute("titulo", "Buscar Facturas por Fecha");
        return "facturas/buscar";
    }
    
    /**
     * Muestra la página de confirmación para facturación masiva
     */
    @GetMapping("/generar-masivas")
    public String mostrarConfirmacionMasiva(Model model) {
        model.addAttribute("titulo", "Facturación Masiva");
        return "facturas/generar-masivas";
    }
    
    /**
     * Genera facturas para todos los clientes con servicios activos
     */
    @PostMapping("/generar-masivas")
    public String generarFacturasMasivas(Model model, RedirectAttributes flash) {
        try {
            com.example.demo.dto.ResultadoFacturacionMasiva resultado = facturaService.generarFacturasMasivas();
            
            model.addAttribute("resultado", resultado);
            model.addAttribute("titulo", "Resultado de Facturación Masiva");
            
            if (resultado.getErrores() > 0) {
                flash.addFlashAttribute("warning", 
                    "Facturación completada con " + resultado.getErrores() + " errores");
            } else {
                flash.addFlashAttribute("success", 
                    "Se generaron " + resultado.getFacturasGeneradas() + " facturas exitosamente");
            }
            
            return "facturas/resultado-masivas";
        } catch (Exception e) {
            flash.addFlashAttribute("error", 
                "Error en la facturación masiva: " + e.getMessage());
            return "redirect:/facturas";
        }
    }
}
