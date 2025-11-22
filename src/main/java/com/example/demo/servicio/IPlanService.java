package com.example.demo.servicio;

import com.example.demo.modelo.Plan;

import java.util.List;
import java.util.Optional;

public interface IPlanService {
    
    List<Plan> listarTodos();
    
    Optional<Plan> buscarPorId(Long id);
    
    Plan guardar(Plan plan);
    
    void eliminar(Long id);
}
