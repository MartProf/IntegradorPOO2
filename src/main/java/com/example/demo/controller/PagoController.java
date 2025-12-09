package com.example.demo.controller;

import com.example.demo.model.Factura;
import com.example.demo.model.Pago;
import com.example.demo.model.enums.EstadoFactura;
import com.example.demo.model.enums.MedioPago;
import com.example.demo.service.IPagoService;
import com.example.demo.service.IFacturaService;
import com.example.demo.service.ICuentaClienteService;
import com.example.demo.repository.ItemPagoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controlador para gestionar los pagos
 */
@Controller
@RequestMapping("/pagos")
public class PagoController {
    
    @Autowired
    private IPagoService pagoService;
    
    @Autowired
    private IFacturaService facturaService;
    
    @Autowired
    private ICuentaClienteService cuentaClienteService;
    
    @Autowired
    private ItemPagoRepository itemPagoRepository;
    
    /**
     * Lista todos los pagos
     */
    @GetMapping
    public String listarPagos(Model model) {
        List<Pago> pagos = pagoService.listarTodos();
        model.addAttribute("pagos", pagos);
        model.addAttribute("titulo", "Listado de Pagos");
        return "pagos/lista";
    }
    
    /**
     * Muestra el detalle de un pago
     */
    @GetMapping("/{id}")
    public String verDetalle(@PathVariable Long id, Model model, RedirectAttributes flash) {
        Pago pago = pagoService.buscarPorId(id)
            .orElse(null);
        
        if (pago == null) {
            flash.addFlashAttribute("error", "El pago no existe");
            return "redirect:/pagos";
        }
        
        model.addAttribute("pago", pago);
        model.addAttribute("titulo", "Detalle de Pago - Recibo " + pago.getNumeroRecibo());
        return "pagos/detalle";
    }
    
    /**
     * Muestra el formulario para registrar un nuevo pago
     */
    @GetMapping("/registrar")
    public String mostrarFormularioRegistrar(Model model) {
        model.addAttribute("cuentas", cuentaClienteService.listarTodas());
        model.addAttribute("mediosPago", MedioPago.values());
        model.addAttribute("titulo", "Registrar Pago");
        return "pagos/registrar";
    }
    
    /**
     * Obtiene las facturas pendientes de una cuenta (AJAX)
     */
    @GetMapping("/facturas-pendientes/{cuentaId}")
    @ResponseBody
    public List<Map<String, Object>> obtenerFacturasPendientes(@PathVariable Long cuentaId) {
        List<Factura> facturas = facturaService.listarPorCuentaYEstados(cuentaId, 
            List.of(EstadoFactura.PENDIENTE, EstadoFactura.PAGO_PARCIAL));
        
        return facturas.stream().map(f -> {
            // Calcular cuánto se ha pagado de esta factura
            Double totalPagado = itemPagoRepository.calcularTotalPagadoPorFactura(f.getId());
            if (totalPagado == null) {
                totalPagado = 0.0;
            }
            
            // Calcular el saldo pendiente
            Double saldoPendiente = f.getMontoTotalFinal() - totalPagado;
            
            Map<String, Object> map = new HashMap<>();
            map.put("id", f.getId());
            map.put("numero", f.getNumero());
            map.put("fechaEmision", f.getFechaEmision().toString());
            map.put("montoTotal", f.getMontoTotalFinal());
            map.put("montoPagado", totalPagado);
            map.put("saldoPendiente", saldoPendiente);
            map.put("estado", f.getEstado().name());
            return map;
        }).collect(Collectors.toList());
    }
    
    /**
     * Registra un nuevo pago
     */
    @PostMapping("/registrar")
    public String registrarPago(
            @RequestParam Long cuentaClienteId,
            @RequestParam(required = false) Long[] facturaIds,
            @RequestParam(required = false) Double[] montos,
            @RequestParam MedioPago medioPago,
            @RequestParam(required = false) String referencia,
            RedirectAttributes flash) {
        
        try {
            // Validar que hay facturas seleccionadas
            if (facturaIds == null || facturaIds.length == 0) {
                flash.addFlashAttribute("error", "Debe seleccionar al menos una factura");
                return "redirect:/pagos/registrar";
            }
            
            // Crear el mapa de distribución de pagos
            Map<Long, Double> distribucionPagos = new HashMap<>();
            for (int i = 0; i < facturaIds.length; i++) {
                distribucionPagos.put(facturaIds[i], montos[i]);
            }
            
            // Registrar el pago
            Pago pago = pagoService.registrarPago(cuentaClienteId, distribucionPagos, 
                medioPago, referencia);
            
            flash.addFlashAttribute("success", 
                "Pago registrado exitosamente. Recibo: " + pago.getNumeroRecibo());
            return "redirect:/pagos/" + pago.getId();
            
        } catch (Exception e) {
            flash.addFlashAttribute("error", 
                "Error al registrar el pago: " + e.getMessage());
            return "redirect:/pagos/registrar";
        }
    }
    
    /**
     * Lista pagos de una cuenta específica
     */
    @GetMapping("/cuenta/{cuentaId}")
    public String listarPagosPorCuenta(@PathVariable Long cuentaId, Model model, 
                                       RedirectAttributes flash) {
        try {
            List<Pago> pagos = pagoService.listarPorCuentaCliente(cuentaId);
            model.addAttribute("pagos", pagos);
            model.addAttribute("cuenta", cuentaClienteService.buscarPorId(cuentaId).orElse(null));
            model.addAttribute("titulo", "Pagos de la Cuenta");
            return "pagos/lista";
        } catch (Exception e) {
            flash.addFlashAttribute("error", "Error al buscar pagos: " + e.getMessage());
            return "redirect:/pagos";
        }
    }
}
