package com.example.demo.repository;

import com.example.demo.model.DetalleFactura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para la entidad DetalleFactura
 */
@Repository
public interface DetalleFacturaRepository extends JpaRepository<DetalleFactura, Long> {
    
    /**
     * Encuentra todos los detalles de una factura específica
     */
    List<DetalleFactura> findByFacturaId(Long facturaId);
    
    /**
     * Encuentra detalles que corresponden a un servicio contratado específico
     */
    List<DetalleFactura> findByServicioContratadoId(Long servicioContratadoId);
    
    /**
     * Calcula el subtotal total de una factura
     */
    @Query("SELECT COALESCE(SUM(df.subtotalNeto), 0.0) FROM DetalleFactura df WHERE df.factura.id = :facturaId")
    Double calcularSubtotalPorFactura(@Param("facturaId") Long facturaId);
    
    /**
     * Calcula el IVA total de una factura
     */
    @Query("SELECT COALESCE(SUM(df.montoIVA), 0.0) FROM DetalleFactura df WHERE df.factura.id = :facturaId")
    Double calcularIVATotalPorFactura(@Param("facturaId") Long facturaId);
}
