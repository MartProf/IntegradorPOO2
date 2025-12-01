package com.example.demo.controladores;

import com.example.demo.modelo.Factura;
import com.example.demo.modelo.NotaCredito;
import com.example.demo.servicio.FacturaServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/facturacion")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // Para desarrollo frontend
public class FacturacionController {

    private final FacturaServicio facturaServicio;

    // HU 1.3 - Facturación Masiva
    @PostMapping("/masiva")
    public ResponseEntity<List<Factura>> ejecutarFacturacionMasiva(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate periodo) {
        try {
            List<Factura> facturasGeneradas = facturaServicio.ejecutarFacturacionMasiva(periodo);
            return ResponseEntity.ok(facturasGeneradas);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // HU 1.4 - Facturación Individual por Servicio
    @PostMapping("/individual/{servicioContratadoId}")
    public ResponseEntity<Factura> generarFacturaIndividual(
            @PathVariable Long servicioContratadoId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaEmision) {
        try {
            Factura factura = facturaServicio.generarFacturaIndividualPorServicio(servicioContratadoId, fechaEmision);
            return ResponseEntity.ok(factura);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // HU 1.7 - Anulación de Factura con Nota de Crédito
    @PostMapping("/anular/{facturaId}")
    public ResponseEntity<NotaCredito> anularFactura(
            @PathVariable Long facturaId,
            @RequestParam String motivo) {
        try {
            NotaCredito notaCredito = facturaServicio.anularFactura(facturaId, motivo);
            return ResponseEntity.ok(notaCredito);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Endpoint adicional para obtener facturas por cuenta
    @GetMapping("/cuenta/{cuentaId}")
    public ResponseEntity<List<Factura>> obtenerFacturasPorCuenta(@PathVariable Long cuentaId) {
        try {
            // Este método deberías agregarlo en FacturaServicio
            // List<Factura> facturas = facturaServicio.obtenerFacturasPorCuenta(cuentaId);
            // return ResponseEntity.ok(facturas);
            return ResponseEntity.ok(List.of()); // Temporal
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}