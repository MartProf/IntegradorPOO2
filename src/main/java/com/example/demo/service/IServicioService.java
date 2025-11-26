package com.example.demo.service;

import com.example.demo.model.Servicio;

import java.util.List;
import java.util.Optional;

public interface IServicioService {
    
    List<Servicio> listarTodos();
    
    Optional<Servicio> buscarPorId(Long id);
    
    Servicio guardar(Servicio servicio);
    
    void eliminar(Long id);
}
