package com.example.demo.servicio.dto;

import com.example.demo.modelo.MedioPago;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemPagoDTO {
    private Double monto;
    private MedioPago medio;
    private String referencia;
}