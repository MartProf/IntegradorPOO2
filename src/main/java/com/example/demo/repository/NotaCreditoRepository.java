package com.example.demo.repository;

import com.example.demo.model.NotaCredito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad NotaCredito
 */
@Repository
public interface NotaCreditoRepository extends JpaRepository<NotaCredito, Long> {
    
    // Métodos para soft delete
    List<NotaCredito> findByActivoTrue();
    
    Optional<NotaCredito> findByIdAndActivoTrue(Long id);
    
    /**
     * Busca una nota de crédito por su número
     */
    Optional<NotaCredito> findByNumero(String numero);
    
    Optional<NotaCredito> findByNumeroAndActivoTrue(String numero);
    
    /**
     * Encuentra todas las notas de crédito de una cuenta de cliente
     */
    List<NotaCredito> findByCuentaClienteId(Long cuentaClienteId);
    
    List<NotaCredito> findByCuentaClienteIdAndActivoTrue(Long cuentaClienteId);
    
    /**
     * Busca la nota de crédito asociada a una factura específica
     */
    Optional<NotaCredito> findByFacturaAnuladaId(Long facturaId);
    
    /**
     * Obtiene la última nota de crédito por número (para generar el siguiente)
     */
    @Query("SELECT nc FROM NotaCredito nc ORDER BY nc.numero DESC LIMIT 1")
    Optional<NotaCredito> findUltimaNotaCredito();
    
    /**
     * Cuenta el total de notas de crédito emitidas
     */
    @Query("SELECT COUNT(nc) FROM NotaCredito nc")
    Long contarTotal();
}
