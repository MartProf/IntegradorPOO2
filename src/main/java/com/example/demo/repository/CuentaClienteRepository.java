package com.example.demo.repository;

import com.example.demo.model.CuentaCliente;
import com.example.demo.model.enums.EstadoCuenta;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CuentaClienteRepository extends JpaRepository<CuentaCliente, Long> {
    
    /**
     * Busca una cuenta por el ID del cliente
     * @param clienteId El ID del cliente
     * @return Optional con la cuenta si existe
     */
    Optional<CuentaCliente> findByClienteId(Long clienteId);
    
    /**
     * Busca todas las cuentas por estado
     * @param estado El estado de la cuenta
     * @return Lista de cuentas con ese estado
     */
    List<CuentaCliente> findByEstado(EstadoCuenta estado);
    
    // Métodos para soft delete
    List<CuentaCliente> findByActivoTrue();
    
    Optional<CuentaCliente> findByIdAndActivoTrue(Long id);
    
    Optional<CuentaCliente> findByClienteIdAndActivoTrue(Long clienteId);
    
    List<CuentaCliente> findByEstadoAndActivoTrue(EstadoCuenta estado);
    
    /**
     * Busca cuentas con deuda pendiente mayor a cero
     * @return Lista de cuentas con deuda
     */
    List<CuentaCliente> findByDeudaPendienteGreaterThan(Double monto);
}
