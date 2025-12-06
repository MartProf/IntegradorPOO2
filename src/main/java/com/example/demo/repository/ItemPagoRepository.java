package com.example.demo.repository;

import com.example.demo.model.ItemPago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para la entidad ItemPago
 */
@Repository
public interface ItemPagoRepository extends JpaRepository<ItemPago, Long> {
    
    /**
     * Encuentra todos los items de un pago específico
     */
    List<ItemPago> findByPagoId(Long pagoId);
    
    /**
     * Encuentra todos los items aplicados a una factura específica
     */
    List<ItemPago> findByFacturaId(Long facturaId);
    
    /**
     * Calcula el total pagado de una factura específica
     */
    @Query("SELECT COALESCE(SUM(ip.monto), 0.0) FROM ItemPago ip WHERE ip.factura.id = :facturaId")
    Double calcularTotalPagadoPorFactura(@Param("facturaId") Long facturaId);
}
