package com.example.demo.repository;

import com.example.demo.model.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {
    
    Optional<Plan> findByNombre(String nombre);
    
    boolean existsByNombre(String nombre);
    
    // Métodos para soft delete
    List<Plan> findByActivoTrue();
    
    Optional<Plan> findByIdAndActivoTrue(Long id);
    
    Optional<Plan> findByNombreAndActivoTrue(String nombre);
    
    boolean existsByNombreAndActivoTrue(String nombre);
}
