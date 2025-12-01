package com.example.demo.repositorio;

import com.example.demo.modelo.Factura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, Long> {
    
    // Método para buscar facturas por cuenta cliente
    List<Factura> findByCuentaClienteId(Long cuentaClienteId);
}