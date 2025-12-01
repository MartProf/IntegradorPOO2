package com.example.demo.repositorio;

import com.example.demo.modelo.NotaCredito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotaCreditoRepository extends JpaRepository<NotaCredito, Long> {
    // Para persistir la NotaCredito generada en la anulación (HU 1.7)
}