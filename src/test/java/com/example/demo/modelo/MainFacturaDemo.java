package com.example.demo.modelo;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MainFacturaDemo {
    public static void main(String[] args) {
        // Crear cliente con CondicionFiscal (Responsable Inscripto)
        Cliente cliente = new Cliente(1L, "ACME SRL", "30-12345678-9", "Calle Falsa 123", "contacto@acme.com", new ResponsableInscripto());

        // Crear servicio con precio base y alicuota IVA 21%
        Servicio servicioInternet = new Servicio(1L, "Internet 100Mbps", "Plan empresarial", new BigDecimal("2500.00"), new BigDecimal("0.21"));

        // Servicio contratado (sin precio personalizado, usará precioBase)
        ServicioContratado contratado = new ServicioContratado(servicioInternet, 0.0, 1); // 0.0 indica no personalizado en esta estructura

        // Crear factura
        Factura factura = new Factura(cliente, "F0001-00001234", LocalDate.now().plusDays(30));

        // Agregar 1 unidad del servicio contratado
        factura.agregarDetalleDesdeServicioContratado(contratado, 1);

        // Mostrar resultados
        System.out.println("Factura nro: " + factura.getNumero());
        System.out.println("Fecha: " + factura.getFechaEmision());
        System.out.println("Estado: " + factura.getEstado());
        System.out.println("Subtotal neto: $" + factura.getSubtotalNeto());
        System.out.println("IVA total: $" + factura.getIvaTotal());
        System.out.println("Monto total: $" + factura.getMontoTotal());

        // Anular factura (ejemplo)
        factura.anular("Error en emisión - cliente solicitó baja");
        System.out.println("Estado luego de anular: " + factura.getEstado());
        System.out.println("Motivo anulación: " + factura.getMotivoAnulacion());
    }
}
