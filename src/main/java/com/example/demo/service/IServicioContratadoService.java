package com.example.demo.service;

import com.example.demo.model.ServicioContratado;
import java.util.List;

public interface IServicioContratadoService {
    
    // Listar todos los servicios contratados
    List<ServicioContratado> listarTodos();
    
    // Buscar por ID
    ServicioContratado buscarPorId(Long id);
    
    // Listar servicios contratados de una cuenta específica
    List<ServicioContratado> listarPorCuenta(Long cuentaClienteId);
    
    // Contratar un servicio individual a una cuenta
    ServicioContratado contratarServicio(Long cuentaClienteId, Long servicioId, 
                                         Double precioPersonalizado, Double descuento);
    
    // Contratar un plan a una cuenta
    ServicioContratado contratarPlan(Long cuentaClienteId, Long planId, 
                                     Double precioPersonalizado, Double descuento);
    
    // Modificar servicio contratado (cambiar precio, descuento)
    ServicioContratado modificar(Long id, Double precioPersonalizado, Double descuento);
    
    // Cancelar/eliminar servicio contratado
    void cancelar(Long id);
    
    // Validar si una cuenta ya tiene contratado un servicio
    boolean cuentaTieneServicio(Long cuentaClienteId, Long servicioId);
}
