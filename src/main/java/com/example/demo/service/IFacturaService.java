package com.example.demo.service;

import com.example.demo.dto.ResultadoFacturacionMasiva;
import com.example.demo.model.Factura;
import com.example.demo.model.enums.EstadoFactura;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz del servicio de facturas
 */
public interface IFacturaService {
    
    /**
     * Genera una factura individual para una cuenta de cliente
     * @param cuentaClienteId ID de la cuenta del cliente
     * @return La factura generada
     */
    Factura generarFacturaIndividual(Long cuentaClienteId);
    
    /**
     * Lista todas las facturas
     */
    List<Factura> listarTodas();
    
    /**
     * Busca una factura por ID
     */
    Optional<Factura> buscarPorId(Long id);
    
    /**
     * Busca facturas por cuenta de cliente
     */
    List<Factura> listarPorCuentaCliente(Long cuentaClienteId);
    
    /**
     * Busca facturas por estado
     */
    List<Factura> listarPorEstado(EstadoFactura estado);
    
    /**
     * Lista facturas vencidas
     */
    List<Factura> listarFacturasVencidas();
    
    /**
     * Lista facturas en un rango de fechas
     */
    List<Factura> listarPorRangoFechas(LocalDate desde, LocalDate hasta);
    
    /**
     * Lista facturas pendientes de una cuenta
     */
    List<Factura> listarFacturasPendientesPorCuenta(Long cuentaClienteId);
    
    /**
     * Lista facturas de una cuenta con estados específicos
     */
    List<Factura> listarPorCuentaYEstados(Long cuentaClienteId, List<EstadoFactura> estados);
    
    /**
     * Calcula el total adeudado por una cuenta
     */
    Double calcularTotalAdeudado(Long cuentaClienteId);
    
    /**
     * Marca una factura como pagada
     */
    Factura marcarComoPagada(Long facturaId);
    
    /**
     * Marca una factura como anulada
     */
    Factura marcarComoAnulada(Long facturaId);
    
    /**
     * Actualiza el estado de una factura
     */
    Factura actualizarEstado(Long facturaId, EstadoFactura nuevoEstado);
    
    /**
     * Elimina una factura (soft delete - marca como anulada)
     */
    void eliminar(Long id);
    
    /**
     * Genera el próximo número de factura
     */
    String generarNumeroFactura();
    
    /**
     * Genera facturas para todos los clientes con servicios activos
     * @return Resultado con estadísticas de la facturación masiva
     */
    ResultadoFacturacionMasiva generarFacturasMasivas();
}
