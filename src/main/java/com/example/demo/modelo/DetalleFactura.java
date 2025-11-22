<<<<<<< HEAD
/*
package com.example.demo.modelo;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Representa una línea de factura: cantidad * precio unitario (neto), monto de IVA y total de la línea.
 * Todas las operaciones monetarias se hacen con BigDecimal y RoundingMode.HALF_UP.

public class DetalleFactura {

    private int cantidad;
    // subtotal neto sin IVA (precio unitario * cantidad)
    private BigDecimal subtotalNeto;
    // monto de IVA para esta linea
    private BigDecimal montoIVA;
    // total = subtotalNeto + montoIVA
    private BigDecimal totalLinea;
    // descripción del servicio (copia para histórico)
    private String descripcionServicio;
    // alícuota IVA aplicada a la línea (ej: 0.21)
    private BigDecimal alicuotaAplicada;

    public DetalleFactura(int cantidad, BigDecimal subtotalNeto,
                          BigDecimal montoIVA, String descripcionServicio, BigDecimal alicuotaAplicada) {
        this.cantidad = cantidad;
        this.subtotalNeto = subtotalNeto.setScale(2, RoundingMode.HALF_UP);
        this.montoIVA = montoIVA.setScale(2, RoundingMode.HALF_UP);
        this.totalLinea = this.subtotalNeto.add(this.montoIVA).setScale(2, RoundingMode.HALF_UP);
        this.descripcionServicio = descripcionServicio;
        this.alicuotaAplicada = alicuotaAplicada.setScale(4, RoundingMode.HALF_UP);
    }

    public int getCantidad() { return cantidad; }
    public BigDecimal getSubtotalNeto() { return subtotalNeto; }
    public BigDecimal getMontoIVA() { return montoIVA; }
    public BigDecimal getTotalLinea() { return totalLinea; }
    public String getDescripcionServicio() { return descripcionServicio; }
    public BigDecimal getAlicuotaAplicada() { return alicuotaAplicada; }
}
 */
=======
package com.example.demo.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class DetalleFactura 
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private Integer cantidad;
    private Double subtotalNeto;
    private Double montoIVA;
    private String descripcionServicio;
    
    // Relación N-1 con Factura
    @ManyToOne
    private Factura factura;
    // Relación N-1 con ServicioContratado
    @ManyToOne
    private ServicioContratado servicioContratado;
}
>>>>>>> origin/pike
