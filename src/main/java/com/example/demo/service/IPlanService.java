package com.example.demo.service;

import com.example.demo.model.Plan;

import java.util.List;
import java.util.Optional;

public interface IPlanService {
    
    List<Plan> listarTodos();
    
    Optional<Plan> buscarPorId(Long id);
    
    Plan guardar(Plan plan);
    
    void eliminar(Long id);
}
