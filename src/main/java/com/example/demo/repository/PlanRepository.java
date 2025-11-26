package com.example.demo.repository;

import com.example.demo.model.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {
    
    Optional<Plan> findByNombre(String nombre);
    
    boolean existsByNombre(String nombre);
}
