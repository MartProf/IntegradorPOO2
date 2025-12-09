package com.example.demo.service;

import com.example.demo.model.NotaCredito;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz del servicio de notas de crédito
 */
public interface INotaCreditoService {
    
    /**
     * Genera una nota de crédito para anular una factura
     * @param facturaId ID de la factura a anular
     * @param motivo Motivo de la anulación (mínimo 10 caracteres)
     * @return La nota de crédito generada
     * @throws RuntimeException si la factura no existe, tiene pagos, o no se puede anular
     */
    NotaCredito generarNotaCredito(Long facturaId, String motivo);
    
    /**
     * Lista todas las notas de crédito
     */
    List<NotaCredito> listarTodas();
    
    /**
     * Busca una nota de crédito por ID
     */
    Optional<NotaCredito> buscarPorId(Long id);
    
    /**
     * Lista notas de crédito de una cuenta de cliente
     */
    List<NotaCredito> listarPorCuentaCliente(Long cuentaClienteId);
    
    /**
     * Busca una nota de crédito por su número
     */
    Optional<NotaCredito> buscarPorNumero(String numero);
    
    /**
     * Busca la nota de crédito asociada a una factura
     */
    Optional<NotaCredito> buscarPorFacturaAnulada(Long facturaId);
    
    /**
     * Genera el próximo número de nota de crédito secuencial
     * Formato: NC-YYYY-NNNNNNNN (ej: NC-2025-00000001)
     */
    String generarNumeroNotaCredito();
    
    /**
     * Valida si una factura puede ser anulada con nota de crédito
     * @param facturaId ID de la factura
     * @return true si se puede anular, false en caso contrario
     */
    boolean puedeAnularse(Long facturaId);
    
    /**
     * Obtiene el mensaje de error de por qué una factura no puede anularse
     * @param facturaId ID de la factura
     * @return Mensaje descriptivo del motivo
     */
    String obtenerMotivoNoAnulable(Long facturaId);
}
