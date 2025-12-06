package com.example.demo.model.enums;

/**
 * Estado de la cuenta del cliente respecto a su deuda
 */
public enum EstadoCuenta {
    AL_DIA("Al día"),
    DEBE("Debe"),
    SUSPENDIDA("Suspendida"), ACTIVA("Activa");
    
    private final String descripcion;
    
    EstadoCuenta(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
}
