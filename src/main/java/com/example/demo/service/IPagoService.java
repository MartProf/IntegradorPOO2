package com.example.demo.service;

import com.example.demo.model.Pago;
import com.example.demo.model.enums.MedioPago;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Interfaz del servicio de pagos
 */
public interface IPagoService {
    
    /**
     * Registra un pago aplicado a una o varias facturas
     * @param cuentaClienteId ID de la cuenta del cliente que paga
     * @param distribucionPagos Map de facturaId -> monto a pagar de esa factura
     * @param medioPago Medio de pago utilizado
     * @param referencia Referencia del pago (número de transferencia, cheque, etc.)
     * @return El pago registrado
     */
    Pago registrarPago(Long cuentaClienteId, Map<Long, Double> distribucionPagos, MedioPago medioPago, String referencia);
    
    /**
     * Lista todos los pagos
     */
    List<Pago> listarTodos();
    
    /**
     * Busca un pago por ID
     */
    Optional<Pago> buscarPorId(Long id);
    
    /**
     * Lista pagos de una cuenta específica
     */
    List<Pago> listarPorCuentaCliente(Long cuentaClienteId);
    
    /**
     * Lista pagos en un rango de fechas
     */
    List<Pago> listarPorRangoFechas(LocalDate desde, LocalDate hasta);
    
    /**
     * Busca un pago por número de recibo
     */
    Optional<Pago> buscarPorNumeroRecibo(String numeroRecibo);
    
    /**
     * Calcula el total pagado por una cuenta
     */
    Double calcularTotalPagado(Long cuentaClienteId);
    
    /**
     * Genera el próximo número de recibo
     */
    String generarNumeroRecibo();
}
