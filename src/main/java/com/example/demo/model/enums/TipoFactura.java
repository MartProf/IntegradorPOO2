package com.example.demo.model.enums;

/**
 * Enum que representa los tipos de facturas según la normativa argentina (AFIP).
 * El tipo de factura se determina según la condición fiscal del emisor y el receptor.
 */
public enum TipoFactura {
    /**
     * Factura A - Discrimina IVA
     * Se emite cuando:
     * - Emisor: Responsable Inscripto
     * - Receptor: Responsable Inscripto
     * Muestra subtotal e IVA por separado
     */
    FACTURA_A("Factura A", true),
    
    /**
     * Factura B - IVA incluido
     * Se emite cuando:
     * - Emisor: Responsable Inscripto
     * - Receptor: Monotributista o Consumidor Final
     * Muestra solo el total con IVA incluido
     */
    FACTURA_B("Factura B", false),
    
    /**
     * Factura C - Sin IVA
     * Se emite cuando:
     * - Emisor: Monotributista
     * - Receptor: Cualquiera
     * No discrimina IVA porque el emisor no lo factura
     */
    FACTURA_C("Factura C", false);
    
    private final String descripcion;
    private final boolean discriminaIVA;
    
    TipoFactura(String descripcion, boolean discriminaIVA) {
        this.descripcion = descripcion;
        this.discriminaIVA = discriminaIVA;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    /**
     * Indica si este tipo de factura debe mostrar el IVA discriminado
     * @return true si discrimina IVA (Factura A), false en caso contrario
     */
    public boolean discriminaIVA() {
        return discriminaIVA;
    }
    
    /**
     * Determina el tipo de factura según la condición fiscal del cliente
     * Asume que el emisor siempre es Responsable Inscripto
     * 
     * @param condicionFiscalCliente La condición fiscal del cliente
     * @return El tipo de factura correspondiente
     */
    public static TipoFactura determinarTipo(CondicionFiscal condicionFiscalCliente) {
        if (condicionFiscalCliente == null) {
            throw new IllegalArgumentException("La condición fiscal del cliente no puede ser nula");
        }
        
        switch (condicionFiscalCliente) {
            case RESPONSABLE_INSCRIPTO:
                return FACTURA_A;
            case MONOTRIBUTISTA:
            case CONSUMIDOR_FINAL:
                return FACTURA_B;
            default:
                throw new IllegalStateException("Condición fiscal no reconocida: " + condicionFiscalCliente);
        }
    }
}
