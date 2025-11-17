package com.example.demo.modelo;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Representa un servicio ofrecido por la empresa.
 * Ahora el precio está en BigDecimal para preservar precisión monetaria.
 */
public class Servicio {
    private Long id;
    private String nombre;
    private String descripcion;
    // Alícuota IVA expresada como porcentaje en decimal (ej: 0.21 para 21%)
    private BigDecimal alicuotaIVA;
    private BigDecimal precioBase;

    public Servicio(Long id, String nombre, String descripcion, BigDecimal precioBase, BigDecimal alicuotaIVA) {
        this.id = id;
        this.nombre = Objects.requireNonNull(nombre);
        this.descripcion = descripcion;
        this.precioBase = precioBase != null ? precioBase : BigDecimal.ZERO;
        this.alicuotaIVA = alicuotaIVA != null ? alicuotaIVA : BigDecimal.ZERO;
    }

    public BigDecimal getAlicuotaIVA() {
        return alicuotaIVA;
    }

    public BigDecimal getPrecioBase() {
        return precioBase;
    }

    public String getNombre() {
        return nombre;
    }
}
