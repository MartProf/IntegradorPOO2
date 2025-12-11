package com.example.demo.repository;

import com.example.demo.model.Factura;
import com.example.demo.model.enums.EstadoFactura;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad Factura
 */
@Repository
public interface FacturaRepository extends JpaRepository<Factura, Long> {
    
    // Métodos para soft delete
    List<Factura> findByActivoTrue();
    
    Optional<Factura> findByIdAndActivoTrue(Long id);
    
    /**
     * Encuentra todas las facturas de una cuenta de cliente
     */
    List<Factura> findByCuentaClienteId(Long cuentaClienteId);
    
    List<Factura> findByCuentaClienteIdAndActivoTrue(Long cuentaClienteId);
    
    /**
     * Encuentra facturas por estado
     */
    List<Factura> findByEstado(EstadoFactura estado);
    
    List<Factura> findByEstadoAndActivoTrue(EstadoFactura estado);
    
    /**
     * Encuentra facturas vencidas y pendientes
     */
    @Query("SELECT f FROM Factura f WHERE f.fechaVencimiento < :fecha AND f.estado = 'PENDIENTE'")
    List<Factura> findFacturasVencidas(@Param("fecha") LocalDate fecha);
    
    @Query("SELECT f FROM Factura f WHERE f.fechaVencimiento < :fecha AND f.estado = 'PENDIENTE' AND f.activo = true")
    List<Factura> findFacturasVencidasActivas(@Param("fecha") LocalDate fecha);
    
    /**
     * Encuentra facturas emitidas en un rango de fechas
     */
    List<Factura> findByFechaEmisionBetween(LocalDate desde, LocalDate hasta);
    
    /**
     * Encuentra facturas de un cliente por estado
     */
    List<Factura> findByCuentaClienteIdAndEstado(Long cuentaClienteId, EstadoFactura estado);
    
    /**
     * Encuentra facturas de un cliente con múltiples estados
     */
    List<Factura> findByCuentaClienteIdAndEstadoIn(Long cuentaClienteId, List<EstadoFactura> estados);
    
    /**
     * Encuentra una factura por su número
     */
    Optional<Factura> findByNumero(String numero);
    
    /**
     * Encuentra facturas pendientes de una cuenta
     */
    @Query("SELECT f FROM Factura f WHERE f.cuentaCliente.id = :cuentaId AND f.estado = 'PENDIENTE' ORDER BY f.fechaVencimiento ASC")
    List<Factura> findFacturasPendientesPorCuenta(@Param("cuentaId") Long cuentaClienteId);
    
    /**
     * Calcula el total adeudado por una cuenta (todas las facturas pendientes)
     */
    @Query("SELECT COALESCE(SUM(f.montoTotalFinal), 0.0) FROM Factura f WHERE f.cuentaCliente.id = :cuentaId AND f.estado = 'PENDIENTE'")
    Double calcularTotalAdeudado(@Param("cuentaId") Long cuentaClienteId);
    
    /**
     * Encuentra el último número de factura generado (para generar el siguiente)
     */
    @Query("SELECT f.numero FROM Factura f ORDER BY f.id DESC LIMIT 1")
    Optional<String> findUltimoNumeroFactura();
}
