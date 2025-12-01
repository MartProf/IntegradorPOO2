package com.example.demo.repositorio;

import com.example.demo.modelo.CuentaCliente;
import com.example.demo.modelo.EstadoCuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CuentaClienteRepository extends JpaRepository<CuentaCliente, Long> {
    
    // Permite obtener una lista de cuentas por su estado (usada en facturación masiva)
    List<CuentaCliente> findByEstado(EstadoCuenta estado);
    
    // Métodos adicionales opcionales:
    
    // Buscar por número de cuenta
    Optional<CuentaCliente> findByNumeroCuenta(String numeroCuenta);
    
    // Buscar cuentas con deuda mayor a un valor específico
    List<CuentaCliente> findByDeudaPendienteGreaterThan(Double deudaMinima);
    
    // Consulta personalizada con JPQL
    @Query("SELECT c FROM CuentaCliente c WHERE c.estado = :estado AND c.deudaPendiente > 0")
    List<CuentaCliente> findCuentasActivasConDeuda(@Param("estado") EstadoCuenta estado);
}