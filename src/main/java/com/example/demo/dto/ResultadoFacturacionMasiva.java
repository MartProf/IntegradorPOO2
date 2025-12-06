package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO para el resultado de la facturación masiva
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResultadoFacturacionMasiva {
    
    private int totalCuentasProcesadas;
    private int facturasGeneradas;
    private int errores;
    private List<String> mensajesError = new ArrayList<>();
    private List<Long> idsFacturasGeneradas = new ArrayList<>();
    
    public void agregarExito(Long facturaId) {
        facturasGeneradas++;
        idsFacturasGeneradas.add(facturaId);
    }
    
    public void agregarError(String mensaje) {
        errores++;
        mensajesError.add(mensaje);
    }
}
