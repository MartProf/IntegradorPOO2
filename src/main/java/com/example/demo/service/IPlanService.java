package com.example.demo.service;

import com.example.demo.model.Plan;

import java.util.List;
import java.util.Optional;

public interface IPlanService {
    
    List<Plan> listarTodos();
    
    Optional<Plan> buscarPorId(Long id);
    
    Plan guardar(Plan plan);
    
    void eliminar(Long id);
    
    // Gestión de servicios incluidos en el plan
    Plan agregarServicio(Long planId, Long servicioId);
    
    Plan removerServicio(Long planId, Long servicioId);
    
    Plan actualizarServiciosIncluidos(Long planId, List<Long> serviciosIds);
}
