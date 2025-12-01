package com.example.demo.modelo;

import jakarta.persistence.*;
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
public class DetalleFactura {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    
    private Integer cantidad = 1;
    private Double precioUnitario;
    private Double subtotalNeto;
    private Double montoIVA;
    private Double tasaIVA = 21.0; // IVA por defecto 21%
    private Double montoTotalBruto;
    private String descripcion;
    
    // Relación N-1 con Factura - CORREGIDO
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "factura_id", nullable = false)
    private Factura factura;
    
    // Relación N-1 con ServicioContratado - CORREGIDO
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "servicio_contratado_id", nullable = false)
    private ServicioContratado servicioContratado;
    
    // MÉTODOS DE NEGOCIO PARA CÁLCULOS
    public void calcularSubtotalNeto() {
        if (this.cantidad != null && this.precioUnitario != null) {
            this.subtotalNeto = this.cantidad * this.precioUnitario;
        }
    }
    
    public void calcularIVA() {
        if (this.subtotalNeto != null && this.tasaIVA != null) {
            this.montoIVA = this.subtotalNeto * (this.tasaIVA / 100);
        }
    }
    
    public void calcularTotalBruto() {
        if (this.subtotalNeto != null && this.montoIVA != null) {
            this.montoTotalBruto = this.subtotalNeto + this.montoIVA;
        }
    }
    
    // Método que ejecuta todos los cálculos
    public void calcularTotales() {
        this.calcularSubtotalNeto();
        this.calcularIVA();
        this.calcularTotalBruto();
    }
}