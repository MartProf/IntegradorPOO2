package com.example.demo.model.enums;

/**
 * Medios de pago disponibles
 */
public enum MedioPago 
{
    EFECTIVO("Efectivo"),
    TRANSFERENCIA("Transferencia Bancaria"),
    DEBITO("Tarjeta de Débito"),
    CREDITO("Tarjeta de Crédito"),
    CHEQUE("Cheque");
    
    private final String descripcion;
    
    MedioPago(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
}
