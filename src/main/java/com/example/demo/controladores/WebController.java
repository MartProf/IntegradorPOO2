package com.example.demo.controladores;

import com.example.demo.modelo.*;
import com.example.demo.servicio.FacturaServicio;
import com.example.demo.servicio.PagoService;
import com.example.demo.servicio.dto.ItemPagoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/web")
@RequiredArgsConstructor
public class WebController {

    private final FacturaServicio facturaServicio;
    private final PagoService pagoService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("fechaActual", LocalDate.now());
        return "dashboard";
    }

    @GetMapping("/facturacion/masiva")
    public String facturacionMasivaForm(Model model) {
        model.addAttribute("periodo", LocalDate.now());
        return "facturacion-masiva";
    }

    @PostMapping("/facturacion/masiva")
    public String ejecutarFacturacionMasiva(@RequestParam LocalDate periodo, Model model) {
        try {
            List<Factura> facturas = facturaServicio.ejecutarFacturacionMasiva(periodo);
            model.addAttribute("success", "Facturación masiva ejecutada correctamente");
            model.addAttribute("facturasGeneradas", facturas);
        } catch (Exception e) {
            model.addAttribute("error", "Error en facturación masiva: " + e.getMessage());
        }
        return "facturacion-masiva";
    }

    @GetMapping("/facturacion/individual")
    public String facturacionIndividualForm(Model model) {
        return "facturacion-individual";
    }

    @PostMapping("/facturacion/individual")
    public String generarFacturaIndividual(
            @RequestParam Long servicioContratadoId,
            @RequestParam LocalDate fechaEmision,
            Model model) {
        try {
            Factura factura = facturaServicio.generarFacturaIndividualPorServicio(servicioContratadoId, fechaEmision);
            model.addAttribute("success", "Factura individual generada correctamente");
            model.addAttribute("factura", factura);
        } catch (Exception e) {
            model.addAttribute("error", "Error al generar factura individual: " + e.getMessage());
        }
        return "facturacion-individual";
    }

    @GetMapping("/facturacion/anular")
    public String anularFacturaForm(Model model) {
        return "anular-factura";
    }

    @PostMapping("/facturacion/anular")
    public String anularFactura(
            @RequestParam Long facturaId,
            @RequestParam String motivo,
            Model model) {
        try {
            NotaCredito notaCredito = facturaServicio.anularFactura(facturaId, motivo);
            model.addAttribute("success", "Factura anulada correctamente");
            model.addAttribute("notaCredito", notaCredito);
        } catch (Exception e) {
            model.addAttribute("error", "Error al anular factura: " + e.getMessage());
        }
        return "anular-factura";
    }

    @GetMapping("/pagos/registrar")
    public String registrarPagoForm(Model model) {
        model.addAttribute("mediosPago", MedioPago.values());
        return "registrar-pago";
    }

    @PostMapping("/pagos/registrar")
    public String registrarPago(
            @RequestParam Long facturaId,
            @RequestParam Double monto,
            @RequestParam MedioPago medio,
            @RequestParam String referencia,
            Model model) {
        try {
            // Crear ItemPagoDTO sin constructor (más seguro)
            ItemPagoDTO itemPago = new ItemPagoDTO();
            itemPago.setMonto(monto);
            itemPago.setMedio(medio);
            itemPago.setReferencia(referencia);
            
            Recibo recibo = pagoService.registrarPagoFactura(facturaId, List.of(itemPago));
            
            model.addAttribute("success", "Pago registrado correctamente");
            model.addAttribute("recibo", recibo);
        } catch (Exception e) {
            model.addAttribute("error", "Error al registrar pago: " + e.getMessage());
        }
        return "registrar-pago";
    }

    @GetMapping("/facturas")
    public String listarFacturas(@RequestParam(required = false) Long cuentaId, Model model) {
        try {
            List<Factura> facturas;
            if (cuentaId != null) {
                facturas = facturaServicio.obtenerFacturasPorCuenta(cuentaId);
            } else {
                facturas = List.of();
            }
            model.addAttribute("facturas", facturas);
        } catch (Exception e) {
            model.addAttribute("error", "Error al obtener facturas: " + e.getMessage());
        }
        return "listar-facturas";
    }

    @GetMapping("/recibos")
    public String listarRecibos(@RequestParam(required = false) Long cuentaId, Model model) {
        try {
            List<Recibo> recibos;
            if (cuentaId != null) {
                recibos = pagoService.obtenerRecibosPorCuenta(cuentaId);
            } else {
                recibos = List.of();
            }
            model.addAttribute("recibos", recibos);
        } catch (Exception e) {
            model.addAttribute("error", "Error al obtener recibos: " + e.getMessage());
        }
        return "listar-recibos";
    }
}