package com.example.demo.repository;

import com.example.demo.model.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServicioRepository extends JpaRepository<Servicio, Long> {
    
    Optional<Servicio> findByNombre(String nombre);
    
    boolean existsByNombre(String nombre);
    
    // Métodos para soft delete
    List<Servicio> findByActivoTrue();
    
    Optional<Servicio> findByIdAndActivoTrue(Long id);
    
    Optional<Servicio> findByNombreAndActivoTrue(String nombre);
    
    boolean existsByNombreAndActivoTrue(String nombre);
}
