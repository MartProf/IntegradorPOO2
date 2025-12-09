package com.example.demo.service;

import com.example.demo.model.*;
import com.example.demo.model.enums.EstadoFactura;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio de notas de crédito
 * 
 * @Transactional: Garantiza consistencia en las operaciones de anulación,
 * reversión de deuda y generación de documentos.
 */
@Service
@Transactional
public class NotaCreditoServiceImpl implements INotaCreditoService {
    
    @Autowired
    private NotaCreditoRepository notaCreditoRepository;
    
    @Autowired
    private FacturaRepository facturaRepository;
    
    @Autowired
    private CuentaClienteRepository cuentaClienteRepository;
    
    @Autowired
    private ItemPagoRepository itemPagoRepository;
    
    @Override
    public NotaCredito generarNotaCredito(Long facturaId, String motivo) {
        System.out.println("=== GENERANDO NOTA DE CRÉDITO ===");
        
        // 1. Validar motivo
        if (motivo == null || motivo.trim().length() < 10) {
            throw new RuntimeException("El motivo debe tener al menos 10 caracteres");
        }
        
        // 2. Buscar la factura
        Factura factura = facturaRepository.findById(facturaId)
            .orElseThrow(() -> new RuntimeException("Factura no encontrada con ID: " + facturaId));
        
        System.out.println("Factura encontrada: " + factura.getNumero());
        System.out.println("Estado actual: " + factura.getEstado());
        
        // 3. Validar que se puede anular
        validarAnulacion(factura);
        
        // 4. Generar número de NC
        String numeroNC = generarNumeroNotaCredito();
        System.out.println("Número NC generado: " + numeroNC);
        
        // 5. Crear la nota de crédito
        NotaCredito notaCredito = new NotaCredito();
        notaCredito.setNumero(numeroNC);
        notaCredito.setFechaEmision(LocalDate.now());
        notaCredito.setMontoTotalCredito(factura.getMontoTotalFinal());
        notaCredito.setMotivo(motivo.trim());
        notaCredito.setFacturaAnulada(factura);
        notaCredito.setCuentaCliente(factura.getCuentaCliente());
        
        // 6. Marcar factura como anulada por NC
        factura.setEstado(EstadoFactura.ANULADA_POR_NC);
        facturaRepository.save(factura);
        System.out.println("Factura marcada como ANULADA_POR_NC");
        
        // 7. Revertir deuda de la cuenta
        CuentaCliente cuenta = factura.getCuentaCliente();
        Double deudaAnterior = cuenta.getDeudaPendiente();
        cuenta.setDeudaPendiente(deudaAnterior - factura.getMontoTotalFinal());
        
        // Si la deuda queda negativa (puede pasar si hubo ajustes), ponerla en 0
        if (cuenta.getDeudaPendiente() < 0) {
            cuenta.setDeudaPendiente(0.0);
        }
        
        cuentaClienteRepository.save(cuenta);
        System.out.println("Deuda revertida: " + deudaAnterior + " → " + cuenta.getDeudaPendiente());
        
        // 8. Guardar la nota de crédito
        NotaCredito notaCreditoGuardada = notaCreditoRepository.save(notaCredito);
        
        System.out.println("=== NOTA DE CRÉDITO GENERADA EXITOSAMENTE ===");
        System.out.println("NC: " + notaCreditoGuardada.getNumero());
        System.out.println("Monto: $" + notaCreditoGuardada.getMontoTotalCredito());
        
        return notaCreditoGuardada;
    }
    
    /**
     * Valida si una factura puede ser anulada
     */
    private void validarAnulacion(Factura factura) {
        // No se puede anular si ya está anulada
        if (factura.getEstado() == EstadoFactura.ANULADA || 
            factura.getEstado() == EstadoFactura.ANULADA_POR_NC) {
            throw new RuntimeException("La factura ya está anulada");
        }
        
        // No se puede anular si está pagada (total o parcial)
        if (factura.getEstado() == EstadoFactura.PAGADA) {
            throw new RuntimeException("No se puede anular una factura que ya fue pagada completamente");
        }
        
        if (factura.getEstado() == EstadoFactura.PAGO_PARCIAL) {
            throw new RuntimeException("No se puede anular una factura con pagos parciales. Debe devolverse el dinero primero.");
        }
        
        // Verificar que no tenga pagos registrados (doble validación)
        List<ItemPago> itemsPago = itemPagoRepository.findByFacturaId(factura.getId());
        if (!itemsPago.isEmpty()) {
            throw new RuntimeException("La factura tiene " + itemsPago.size() + " pago(s) registrado(s). No se puede anular.");
        }
        
        // Verificar que no tenga ya una NC asociada
        Optional<NotaCredito> ncExistente = notaCreditoRepository.findByFacturaAnuladaId(factura.getId());
        if (ncExistente.isPresent()) {
            throw new RuntimeException("La factura ya tiene una nota de crédito asociada: " + ncExistente.get().getNumero());
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<NotaCredito> listarTodas() {
        return notaCreditoRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<NotaCredito> buscarPorId(Long id) {
        return notaCreditoRepository.findById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<NotaCredito> listarPorCuentaCliente(Long cuentaClienteId) {
        return notaCreditoRepository.findByCuentaClienteId(cuentaClienteId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<NotaCredito> buscarPorNumero(String numero) {
        return notaCreditoRepository.findByNumero(numero);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<NotaCredito> buscarPorFacturaAnulada(Long facturaId) {
        return notaCreditoRepository.findByFacturaAnuladaId(facturaId);
    }
    
    @Override
    public String generarNumeroNotaCredito() {
        // Obtener el año actual
        int anioActual = LocalDate.now().getYear();
        
        // Buscar la última nota de crédito
        Optional<NotaCredito> ultimaNC = notaCreditoRepository.findUltimaNotaCredito();
        
        int siguienteNumero = 1;
        
        if (ultimaNC.isPresent()) {
            String ultimoNumero = ultimaNC.get().getNumero();
            // Formato: NC-2025-00000001
            // Extraer la parte numérica (últimos 8 dígitos)
            String parteNumerica = ultimoNumero.substring(ultimoNumero.lastIndexOf('-') + 1);
            int numeroActual = Integer.parseInt(parteNumerica);
            siguienteNumero = numeroActual + 1;
        }
        
        // Formatear como NC-YYYY-NNNNNNNN
        return String.format("NC-%d-%08d", anioActual, siguienteNumero);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean puedeAnularse(Long facturaId) {
        try {
            Factura factura = facturaRepository.findById(facturaId)
                .orElseThrow(() -> new RuntimeException("Factura no encontrada"));
            
            // Verificar estado
            if (factura.getEstado() != EstadoFactura.PENDIENTE) {
                return false;
            }
            
            // Verificar pagos
            List<ItemPago> itemsPago = itemPagoRepository.findByFacturaId(factura.getId());
            if (!itemsPago.isEmpty()) {
                return false;
            }
            
            // Verificar NC existente
            Optional<NotaCredito> ncExistente = notaCreditoRepository.findByFacturaAnuladaId(factura.getId());
            if (ncExistente.isPresent()) {
                return false;
            }
            
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public String obtenerMotivoNoAnulable(Long facturaId) {
        try {
            Factura factura = facturaRepository.findById(facturaId)
                .orElse(null);
            
            if (factura == null) {
                return "Factura no encontrada";
            }
            
            // Verificar estado
            if (factura.getEstado() == EstadoFactura.ANULADA || 
                factura.getEstado() == EstadoFactura.ANULADA_POR_NC) {
                return "La factura ya está anulada";
            }
            
            if (factura.getEstado() == EstadoFactura.PAGADA) {
                return "La factura ya fue pagada completamente";
            }
            
            if (factura.getEstado() == EstadoFactura.PAGO_PARCIAL) {
                return "La factura tiene pagos parciales registrados";
            }
            
            // Verificar pagos
            List<ItemPago> itemsPago = itemPagoRepository.findByFacturaId(factura.getId());
            if (!itemsPago.isEmpty()) {
                return "La factura tiene " + itemsPago.size() + " pago(s) registrado(s)";
            }
            
            // Verificar NC existente
            Optional<NotaCredito> ncExistente = notaCreditoRepository.findByFacturaAnuladaId(factura.getId());
            if (ncExistente.isPresent()) {
                return "Ya existe una nota de crédito para esta factura: " + ncExistente.get().getNumero();
            }
            
            return "La factura puede anularse";
        } catch (Exception e) {
            return "Error al verificar: " + e.getMessage();
        }
    }
}
