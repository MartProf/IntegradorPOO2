package com.example.demo.service;

import com.example.demo.dto.ResultadoFacturacionMasiva;
import com.example.demo.model.*;
import com.example.demo.model.enums.EstadoCuenta;
import com.example.demo.model.enums.EstadoFactura;
import com.example.demo.model.enums.TipoFactura;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementación del servicio de facturas
 * 
 * @Transactional: Garantiza que todas las operaciones de base de datos en este servicio
 * se ejecuten dentro de una transacción. Si algo falla, se hace rollback automáticamente.
 * Esto es crucial para mantener la consistencia de datos cuando se generan facturas,
 * se actualizan deudas, etc.
 */
@Service
@Transactional
public class FacturaServiceImpl implements IFacturaService {
    
    @Autowired
    private FacturaRepository facturaRepository;
    
    @Autowired
    private CuentaClienteRepository cuentaClienteRepository;
    
    @Autowired
    private ServicioContratadoRepository servicioContratadoRepository;
    
    private static final Double IVA_ALICUOTA = 0.21; // 21% de IVA
    private static final int DIAS_VENCIMIENTO = 10; // 10 días para vencer
    
    @Override
    public Factura generarFacturaIndividual(Long cuentaClienteId) {
        // 1. Obtener la cuenta del cliente
        CuentaCliente cuenta = cuentaClienteRepository.findById(cuentaClienteId)
            .orElseThrow(() -> new RuntimeException("Cuenta no encontrada con ID: " + cuentaClienteId));
        
        // 2. Obtener servicios contratados activos de esta cuenta
        List<ServicioContratado> serviciosActivos = servicioContratadoRepository
            .findByCuentaClienteIdAndActivo(cuentaClienteId, true);
        
        System.out.println("=== GENERANDO FACTURA ===");
        System.out.println("Total servicios activos encontrados: " + serviciosActivos.size());
        for (ServicioContratado sc : serviciosActivos) {
            System.out.println("  - ID: " + sc.getId() + 
                             ", Nombre: " + sc.getNombreContratado() + 
                             ", Tipo: " + sc.getTipoContratacion() +
                             ", Precio: " + sc.calcularPrecioFinal());
        }
        
        if (serviciosActivos.isEmpty()) {
            throw new RuntimeException("La cuenta no tiene servicios activos para facturar");
        }
        
        // 3. Determinar el tipo de factura según la condición fiscal del cliente
        Cliente cliente = cuenta.getCliente();
        TipoFactura tipoFactura = TipoFactura.determinarTipo(cliente.getCondicionIVA());
        
        // 4. Crear la factura
        Factura factura = new Factura();
        factura.setNumero(generarNumeroFactura());
        factura.setFechaEmision(LocalDate.now());
        factura.setFechaVencimiento(LocalDate.now().plusDays(DIAS_VENCIMIENTO));
        factura.setEstado(EstadoFactura.PENDIENTE);
        factura.setTipoFactura(tipoFactura);
        factura.setCuentaCliente(cuenta);
        
        // 5. Crear los detalles de factura por cada servicio contratado
        System.out.println("Creando detalles de factura...");
        for (ServicioContratado sc : serviciosActivos) {
            DetalleFactura detalle = new DetalleFactura();
            detalle.setCantidad(1);
            detalle.setDescripcionServicio(sc.getNombreContratado());
            detalle.setServicioContratado(sc);
            
            // Calcular montos según el tipo de factura
            Double precioBase = sc.calcularPrecioFinal();
            
            if (tipoFactura.discriminaIVA()) {
                // Factura A: el precio es sin IVA, se discrimina
                detalle.setSubtotalNeto(precioBase);
                detalle.setMontoIVA(precioBase * IVA_ALICUOTA);
            } else if (tipoFactura == TipoFactura.FACTURA_B) {
                // Factura B: el precio incluye IVA, se calcula inversamente
                detalle.setSubtotalNeto(precioBase / (1 + IVA_ALICUOTA));
                detalle.setMontoIVA(precioBase - detalle.getSubtotalNeto());
            } else {
                // Factura C: sin IVA
                detalle.setSubtotalNeto(precioBase);
                detalle.setMontoIVA(0.0);
            }
            
            System.out.println("  Agregando detalle: " + detalle.getDescripcionServicio() + 
                             " - Subtotal: " + detalle.getSubtotalNeto() + 
                             " - IVA: " + detalle.getMontoIVA());
            
            factura.agregarDetalle(detalle);
        }
        
        System.out.println("Total detalles agregados: " + factura.getDetalles().size());
        
        // 6. Calcular el total
        factura.calcularTotal();
        
        // 7. Guardar la factura (cascade guardará los detalles)
        Factura facturaGuardada = facturaRepository.save(factura);
        
        // 8. Actualizar la deuda de la cuenta
        actualizarDeudaCuenta(cuenta);
        
        return facturaGuardada;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Factura> listarTodas() {
        return facturaRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Factura> buscarPorId(Long id) {
        return facturaRepository.findById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Factura> listarPorCuentaCliente(Long cuentaClienteId) {
        return facturaRepository.findByCuentaClienteId(cuentaClienteId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Factura> listarPorEstado(EstadoFactura estado) {
        return facturaRepository.findByEstado(estado);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Factura> listarFacturasVencidas() {
        return facturaRepository.findFacturasVencidas(LocalDate.now());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Factura> listarPorRangoFechas(LocalDate desde, LocalDate hasta) {
        return facturaRepository.findByFechaEmisionBetween(desde, hasta);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Factura> listarFacturasPendientesPorCuenta(Long cuentaClienteId) {
        return facturaRepository.findFacturasPendientesPorCuenta(cuentaClienteId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Double calcularTotalAdeudado(Long cuentaClienteId) {
        return facturaRepository.calcularTotalAdeudado(cuentaClienteId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Factura> listarPorCuentaYEstados(Long cuentaClienteId, List<EstadoFactura> estados) {
        return facturaRepository.findByCuentaClienteIdAndEstadoIn(cuentaClienteId, estados);
    }
    
    @Override
    public Factura marcarComoPagada(Long facturaId) {
        return actualizarEstado(facturaId, EstadoFactura.PAGADA);
    }
    
    @Override
    public Factura marcarComoAnulada(Long facturaId) {
        return actualizarEstado(facturaId, EstadoFactura.ANULADA);
    }
    
    @Override
    public Factura actualizarEstado(Long facturaId, EstadoFactura nuevoEstado) {
        Factura factura = facturaRepository.findById(facturaId)
            .orElseThrow(() -> new RuntimeException("Factura no encontrada con ID: " + facturaId));
        
        factura.setEstado(nuevoEstado);
        Factura facturaActualizada = facturaRepository.save(factura);
        
        // Actualizar la deuda de la cuenta
        actualizarDeudaCuenta(factura.getCuentaCliente());
        
        return facturaActualizada;
    }
    
    @Override
    public void eliminar(Long id) {
        // Soft delete: marcar como anulada
        marcarComoAnulada(id);
    }
    
    @Override
    public String generarNumeroFactura() {
        Optional<String> ultimoNumero = facturaRepository.findUltimoNumeroFactura();
        
        if (ultimoNumero.isPresent()) {
            // Extraer el número secuencial del último número
            String ultimo = ultimoNumero.get();
            String[] partes = ultimo.split("-");
            
            if (partes.length == 3) {
                int secuencial = Integer.parseInt(partes[2]) + 1;
                return String.format("%s-%s-%08d", partes[0], partes[1], secuencial);
            }
        }
        
        // Si no hay facturas previas, generar el primero
        String año = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy"));
        String mes = LocalDate.now().format(DateTimeFormatter.ofPattern("MM"));
        return String.format("%s-%s-%08d", año, mes, 1);
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
    }
    
    @Override
    public ResultadoFacturacionMasiva generarFacturasMasivas(int mes, int anio) {
        ResultadoFacturacionMasiva resultado = new ResultadoFacturacionMasiva();
        
        String nombreMes = LocalDate.of(anio, mes, 1).getMonth().name();
        System.out.println("=== INICIANDO FACTURACIÓN MASIVA: " + nombreMes + " " + anio + " ===");
        
        // Calcular primer y último día del mes para el filtro
        LocalDate primerDiaMes = LocalDate.of(anio, mes, 1);
        LocalDate ultimoDiaMes = primerDiaMes.withDayOfMonth(primerDiaMes.lengthOfMonth());
        
        // 1. Obtener todas las cuentas de clientes
        List<CuentaCliente> todasLasCuentas = cuentaClienteRepository.findAll();
        System.out.println("Total de cuentas encontradas: " + todasLasCuentas.size());
        
        // 2. Filtrar solo las que tienen servicios activos en el período
        List<CuentaCliente> cuentasConServiciosActivos = todasLasCuentas.stream()
            .filter(cuenta -> {
                List<ServicioContratado> serviciosActivos = servicioContratadoRepository
                    .findByCuentaClienteIdAndActivo(cuenta.getId(), true);
                
                // Filtrar servicios que estaban activos en el mes/año seleccionado
                long serviciosEnPeriodo = serviciosActivos.stream()
                    .filter(sc -> !sc.getFechaInicio().isAfter(ultimoDiaMes))
                    .count();
                    
                return serviciosEnPeriodo > 0;
            })
            .collect(Collectors.toList());
        
        resultado.setTotalCuentasProcesadas(cuentasConServiciosActivos.size());
        System.out.println("Cuentas con servicios activos en " + nombreMes + "/" + anio + ": " + cuentasConServiciosActivos.size());
        
        // 3. Generar factura para cada cuenta
        for (CuentaCliente cuenta : cuentasConServiciosActivos) {
            try {
                System.out.println("Generando factura para: " + cuenta.getCliente().getRazonSocial());
                Factura factura = generarFacturaIndividual(cuenta.getId());
                resultado.agregarExito(factura.getId());
                System.out.println("  ✓ Factura generada: " + factura.getNumero());
            } catch (Exception e) {
                String error = "Error al facturar a " + cuenta.getCliente().getRazonSocial() + ": " + e.getMessage();
                resultado.agregarError(error);
                System.err.println("  ✗ " + error);
            }
        }
        
        System.out.println("=== FACTURACIÓN MASIVA COMPLETADA ===");
        System.out.println("Facturas generadas: " + resultado.getFacturasGeneradas());
        System.out.println("Errores: " + resultado.getErrores());
        
        return resultado;
    }
}
