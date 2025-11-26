package com.example.demo.service;

import com.example.demo.model.Plan;
import com.example.demo.repository.PlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PlanServiceImpl implements IPlanService {
    
    @Autowired
    private PlanRepository planRepository;
    
    @Override
    @Transactional(readOnly = true)
    public List<Plan> listarTodos() {
        return planRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Plan> buscarPorId(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return planRepository.findById(id);
    }
    
    @Override
    public Plan guardar(Plan plan) {
        // Validaciones
        if (plan.getNombre() == null || plan.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del plan es obligatorio");
        }
        
        if (plan.getPrecioBase() == null || plan.getPrecioBase() <= 0) {
            throw new IllegalArgumentException("El precio base debe ser mayor a 0");
        }
        
        // Verificar duplicado (solo si es nuevo o cambió el nombre)
        if (plan.getId() == null) {
            if (planRepository.existsByNombre(plan.getNombre())) {
                throw new IllegalArgumentException("Ya existe un plan con ese nombre");
            }
        } else {
            Long planId = plan.getId();
            if (planId != null && !plan.getNombre().equals(
                    planRepository.findById(planId).map(Plan::getNombre).orElse(""))) {
                if (planRepository.existsByNombre(plan.getNombre())) {
                    throw new IllegalArgumentException("Ya existe un plan con ese nombre");
                }
            }
        }
        
        return planRepository.save(plan);
    }
    
    @Override
    public void eliminar(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }
        if (!planRepository.existsById(id)) {
            throw new IllegalArgumentException("El plan no existe");
        }
        planRepository.deleteById(id);
    }
}
