package com.example.demo.controladores;

import com.example.demo.modelo.Pago;
import com.example.demo.modelo.Recibo;
import com.example.demo.servicio.PagoService;
import com.example.demo.servicio.dto.ItemPagoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // Para desarrollo frontend
public class PagoController {

    private final PagoService pagoService;

    // HU 1.5 - Registrar Pago con Múltiples Medios de Pago
    @PostMapping("/factura/{facturaId}")
    public ResponseEntity<Recibo> registrarPagoFactura(
            @PathVariable Long facturaId,
            @RequestBody List<ItemPagoDTO> itemsPago) {
        try {
            Recibo recibo = pagoService.registrarPagoFactura(facturaId, itemsPago);
            return ResponseEntity.ok(recibo);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // HU 1.6 - Obtener Recibos por Cuenta
    @GetMapping("/recibos/cuenta/{cuentaId}")
    public ResponseEntity<List<Recibo>> obtenerRecibosPorCuenta(@PathVariable Long cuentaId) {
        try {
            List<Recibo> recibos = pagoService.obtenerRecibosPorCuenta(cuentaId);
            return ResponseEntity.ok(recibos);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Endpoint adicional para obtener historial de pagos de una factura
    @GetMapping("/factura/{facturaId}")
    public ResponseEntity<List<Pago>> obtenerPagosPorFactura(@PathVariable Long facturaId) {
        try {
            List<Pago> pagos = pagoService.obtenerPagosPorFactura(facturaId);
            return ResponseEntity.ok(pagos);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Endpoint para obtener un recibo específico
    @GetMapping("/recibos/{reciboId}")
    public ResponseEntity<Recibo> obtenerRecibo(@PathVariable Long reciboId) {
        try {
            // Este método deberías agregarlo en PagoService
            // Recibo recibo = pagoService.obtenerReciboPorId(reciboId);
            // return ResponseEntity.ok(recibo);
            return ResponseEntity.ok(null); // Temporal
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}