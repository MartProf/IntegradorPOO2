package com.example.demo.controller;

import com.example.demo.model.enums.EstadoFactura;
import com.example.demo.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @Autowired
    private IClienteService clienteService;
    
    @Autowired
    private IFacturaService facturaService;
    
    @Autowired
    private IPagoService pagoService;
    
    @Autowired
    private INotaCreditoService notaCreditoService;
    
    @Autowired
    private ICuentaClienteService cuentaClienteService;

    @GetMapping("/")
    public String redirectToDashboard() {
        return "redirect:/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication authentication) {
        // Obtener el nombre del usuario autenticado
        if (authentication != null) {
            model.addAttribute("usuario", authentication.getName());
        }
        
        // Estadísticas
        model.addAttribute("totalClientes", clienteService.listarTodos().size());
        model.addAttribute("totalFacturas", facturaService.listarTodas().size());
        model.addAttribute("facturasPendientes", facturaService.listarPorEstado(EstadoFactura.PENDIENTE).size());
        model.addAttribute("facturasVencidas", facturaService.listarFacturasVencidas().size());
        model.addAttribute("totalPagos", pagoService.listarTodos().size());
        model.addAttribute("totalNotasCredito", notaCreditoService.listarTodas().size());
        
        // Calcular deuda total
        Double deudaTotal = cuentaClienteService.listarTodas().stream()
            .mapToDouble(cuenta -> cuenta.getDeudaPendiente())
            .sum();
        model.addAttribute("deudaTotal", deudaTotal);
        
        return "dashboard/index";
    }
}
