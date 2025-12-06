package com.example.demo.repository;

import com.example.demo.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad Pago
 */
@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {
    
    /**
     * Encuentra todos los pagos de una cuenta de cliente
     */
    List<Pago> findByCuentaClienteId(Long cuentaClienteId);
    
    /**
     * Encuentra pagos en un rango de fechas
     */
    List<Pago> findByFechaPagoBetween(LocalDate desde, LocalDate hasta);
    
    /**
     * Encuentra un pago por su número de recibo
     */
    Optional<Pago> findByNumeroRecibo(String numeroRecibo);
    
    /**
     * Encuentra el último número de recibo generado
     */
    @Query("SELECT p.numeroRecibo FROM Pago p ORDER BY p.id DESC LIMIT 1")
    Optional<String> findUltimoNumeroRecibo();
    
    /**
     * Calcula el total pagado por una cuenta
     */
    @Query("SELECT COALESCE(SUM(p.montoTotal), 0.0) FROM Pago p WHERE p.cuentaCliente.id = :cuentaId")
    Double calcularTotalPagado(@Param("cuentaId") Long cuentaClienteId);
}
