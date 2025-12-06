package com.example.demo.repository;

import com.example.demo.model.ServicioContratado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServicioContratadoRepository extends JpaRepository<ServicioContratado, Long> {
    
    // Buscar todos los servicios contratados por una cuenta cliente
    List<ServicioContratado> findByCuentaClienteId(Long cuentaClienteId);
    
    // Verificar si una cuenta ya tiene contratado un servicio específico
    boolean existsByCuentaClienteIdAndServicioId(Long cuentaClienteId, Long servicioId);
    
    // Buscar servicios contratados por servicio (útil para reportes)
    List<ServicioContratado> findByServicioId(Long servicioId);
    
    // Buscar servicios contratados por plan
    List<ServicioContratado> findByPlanId(Long planId);

    List<ServicioContratado> findByCuentaClienteIdAndActivo(Long cuentaClienteId, boolean b);
}
