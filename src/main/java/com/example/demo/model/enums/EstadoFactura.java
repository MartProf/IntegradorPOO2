package com.example.demo.model.enums;

/**
 * Estados posibles de una factura
 */
public enum EstadoFactura 
{
    PENDIENTE("Pendiente"),
    PAGO_PARCIAL("Pago Parcial"),
    PAGADA("Pagada"),
    ANULADA("Anulada"), ANULADA_POR_NC("Anulada por Nota de Crédito");
    
    private final String descripcion;
    
    EstadoFactura(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
}
