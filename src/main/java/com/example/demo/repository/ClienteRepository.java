package com.example.demo.repository;

import com.example.demo.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    
    /**
     * Busca un cliente por su CUIT/DNI (solo activos)
     * @param cuitDni El CUIT o DNI del cliente
     * @return Optional con el cliente si existe
     */
    Optional<Cliente> findByCuitDniAndActivoTrue(String cuitDni);
    
    /**
     * Verifica si existe un cliente con el CUIT/DNI dado (solo activos)
     * @param cuitDni El CUIT o DNI a verificar
     * @return true si existe, false en caso contrario
     */
    boolean existsByCuitDniAndActivoTrue(String cuitDni);
    
    /**
     * Lista todos los clientes activos
     * @return Lista de clientes activos
     */
    List<Cliente> findByActivoTrue();
    
    /**
     * Busca cliente por ID (solo si está activo)
     * @param id ID del cliente
     * @return Optional con el cliente si existe y está activo
     */
    Optional<Cliente> findByIdAndActivoTrue(Long id);
}
