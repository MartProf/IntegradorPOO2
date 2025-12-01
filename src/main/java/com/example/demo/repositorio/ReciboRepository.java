package com.example.demo.repositorio;

import com.example.demo.modelo.Recibo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReciboRepository extends JpaRepository<Recibo, Long> {
    // Consulta simple por ahora
}