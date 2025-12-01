package com.example.demo.repositorio;

import com.example.demo.modelo.ServicioContratado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServicioContratadoRepository extends JpaRepository<ServicioContratado, Long> {
    // Usado para la Facturación Individual por Servicio (HU 1.4)
}