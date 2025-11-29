package com.example.demo.repository;

import com.example.demo.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    
    /**
     * Busca un cliente por su CUIT/DNI
     * @param cuitDni El CUIT o DNI del cliente
     * @return Optional con el cliente si existe
     */
    Optional<Cliente> findByCuitDni(String cuitDni);
    
    /**
     * Verifica si existe un cliente con el CUIT/DNI dado
     * @param cuitDni El CUIT o DNI a verificar
     * @return true si existe, false en caso contrario
     */
    boolean existsByCuitDni(String cuitDni);
}
