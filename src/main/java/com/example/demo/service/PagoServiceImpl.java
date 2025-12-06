package com.example.demo.service;

import com.example.demo.model.*;
import com.example.demo.model.enums.EstadoCuenta;
import com.example.demo.model.enums.EstadoFactura;
import com.example.demo.model.enums.MedioPago;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Implementación del servicio de pagos
 */
@Service
@Transactional
public class PagoServiceImpl implements IPagoService {
    
    @Autowired
    private PagoRepository pagoRepository;
    
    @Autowired
    private ItemPagoRepository itemPagoRepository;
    
    @Autowired
    private FacturaRepository facturaRepository;
    
    @Autowired
    private CuentaClienteRepository cuentaClienteRepository;
    
    @Override
    public Pago registrarPago(Long cuentaClienteId, Map<Long, Double> distribucionPagos, 
                              MedioPago medioPago, String referencia) {
        
        System.out.println("=== REGISTRANDO PAGO ===");
        
        // 1. Validar que la cuenta existe
        CuentaCliente cuenta = cuentaClienteRepository.findById(cuentaClienteId)
            .orElseThrow(() -> new RuntimeException("Cuenta no encontrada con ID: " + cuentaClienteId));
        
        // 2. Crear el pago
        Pago pago = new Pago();
        pago.setNumeroRecibo(generarNumeroRecibo());
        pago.setFechaPago(LocalDate.now());
        pago.setCuentaCliente(cuenta);
        
        Double montoTotalPago = 0.0;
        
        // 3. Crear items de pago por cada factura
        for (Map.Entry<Long, Double> entry : distribucionPagos.entrySet()) {
            Long facturaId = entry.getKey();
            Double montoPagado = entry.getValue();
            
            // Validar que la factura existe
            Factura factura = facturaRepository.findById(facturaId)
                .orElseThrow(() -> new RuntimeException("Factura no encontrada con ID: " + facturaId));
            
            // Validar que la factura está PENDIENTE o PAGO_PARCIAL
            if (factura.getEstado() == EstadoFactura.PAGADA) {
                throw new RuntimeException("La factura " + factura.getNumero() + " ya está pagada completamente");
            }
            
            if (factura.getEstado() == EstadoFactura.ANULADA) {
                throw new RuntimeException("La factura " + factura.getNumero() + " está anulada");
            }
            
            // Calcular cuánto falta pagar de esta factura
            Double totalPagadoAntes = itemPagoRepository.calcularTotalPagadoPorFactura(facturaId);
            Double saldoPendiente = factura.getMontoTotalFinal() - totalPagadoAntes;
            
            if (montoPagado > saldoPendiente) {
                throw new RuntimeException("El monto a pagar ($" + montoPagado + 
                    ") excede el saldo pendiente ($" + saldoPendiente + ") de la factura " + factura.getNumero());
            }
            
            // Crear el item de pago
            ItemPago item = new ItemPago();
            item.setMonto(montoPagado);
            item.setMedioPago(medioPago);
            item.setReferencia(referencia);
            item.setFactura(factura);
            
            pago.agregarItem(item);
            montoTotalPago += montoPagado;
            
            // Actualizar estado de la factura
            Double totalPagadoDespues = totalPagadoAntes + montoPagado;
            
            if (Math.abs(totalPagadoDespues - factura.getMontoTotalFinal()) < 0.01) {
                // Pagada completamente
                factura.setEstado(EstadoFactura.PAGADA);
                System.out.println("  Factura " + factura.getNumero() + " → PAGADA");
            } else {
                // Pago parcial
                factura.setEstado(EstadoFactura.PAGO_PARCIAL);
                System.out.println("  Factura " + factura.getNumero() + " → PAGO_PARCIAL ($" + 
                    totalPagadoDespues + " de $" + factura.getMontoTotalFinal() + ")");
            }
            
            facturaRepository.save(factura);
        }
        
        // 4. Calcular y establecer el total del pago
        pago.calcularTotal();
        
        System.out.println("Monto total del pago: $" + pago.getMontoTotal());
        
        // 5. Guardar el pago (cascade guardará los items)
        Pago pagoGuardado = pagoRepository.save(pago);
        
        // 6. Actualizar la deuda de la cuenta
        actualizarDeudaCuenta(cuenta);
        
        System.out.println("Pago registrado: " + pagoGuardado.getNumeroRecibo());
        
        return pagoGuardado;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Pago> listarTodos() {
        return pagoRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Pago> buscarPorId(Long id) {
        return pagoRepository.findById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Pago> listarPorCuentaCliente(Long cuentaClienteId) {
        return pagoRepository.findByCuentaClienteId(cuentaClienteId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Pago> listarPorRangoFechas(LocalDate desde, LocalDate hasta) {
        return pagoRepository.findByFechaPagoBetween(desde, hasta);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Pago> buscarPorNumeroRecibo(String numeroRecibo) {
        return pagoRepository.findByNumeroRecibo(numeroRecibo);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Double calcularTotalPagado(Long cuentaClienteId) {
        return pagoRepository.calcularTotalPagado(cuentaClienteId);
    }
    
    @Override
    public String generarNumeroRecibo() {
        Optional<String> ultimoNumero = pagoRepository.findUltimoNumeroRecibo();
        
        if (ultimoNumero.isPresent()) {
            // Extraer el número secuencial del último número
            String ultimo = ultimoNumero.get();
            String[] partes = ultimo.split("-");
            
            if (partes.length == 3) {
                int secuencial = Integer.parseInt(partes[2]) + 1;
                return String.format("REC-%s-%08d", partes[1], secuencial);
            }
        }
        
        // Si no hay pagos previos, generar el primero
        String año = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy"));
        return String.format("REC-%s-%08d", año, 1);
    }
    
    /**
     * Actualiza el monto de deuda de una cuenta basado en sus facturas pendientes
     */
    private void actualizarDeudaCuenta(CuentaCliente cuenta) {
        Double deudaTotal = facturaRepository.calcularTotalAdeudado(cuenta.getId());
        cuenta.setDeudaPendiente(deudaTotal);
        
        // Actualizar el estado de la cuenta según la deuda
        if (deudaTotal > 0) {
            cuenta.setEstado(EstadoCuenta.DEBE);
        } else {
            cuenta.setEstado(EstadoCuenta.AL_DIA);
        }
        
        cuentaClienteRepository.save(cuenta);
        
        System.out.println("Deuda actualizada de la cuenta: $" + deudaTotal);
    }
}
