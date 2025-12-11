package com.example.demo.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import com.example.demo.model.enums.EstadoFactura;
import com.example.demo.model.enums.TipoFactura;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "facturas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@JsonIgnoreProperties({"itemsPago", "notaCredito", "hibernateLazyInitializer", "handler"})
public class Factura 
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String numero;
    
    @Column(nullable = false)
    private LocalDate fechaEmision;
    
    @Column(nullable = false)
    private LocalDate fechaVencimiento;
    
    @Column(nullable = false)
    private Double montoTotalFinal = 0.0;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoFactura estado;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoFactura tipoFactura;
    
    // Soft Delete
    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean activo = true;
    
    // Relación N-1 con CuentaCliente
    @ManyToOne
    @JoinColumn(name = "cuenta_cliente_id", nullable = false)
    private CuentaCliente cuentaCliente; 
    
    // Relación 1-N con DetalleFactura
    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleFactura> detalles = new ArrayList<>();
    
    // Relación 1-N con ItemPago (una factura puede tener múltiples items de pago)
    @OneToMany(mappedBy = "factura")
    private List<ItemPago> itemsPago = new ArrayList<>();
    
    // Relación 1-1 con NotaCredito (Opcional, para navegación)
    @OneToOne(mappedBy = "facturaAnulada")
    private NotaCredito notaCredito; 
    
    // MÉTODOS DE NEGOCIO
    
    /**
     * Calcula el total de la factura sumando todos los detalles
     */
    public void calcularTotal() { 
        this.montoTotalFinal = detalles.stream()
            .mapToDouble(d -> d.getSubtotalNeto() + d.getMontoIVA())
            .sum();
    }
    
    /**
     * Agrega un detalle a la factura
     */
    public void agregarDetalle(DetalleFactura detalle) {
        detalle.setFactura(this);
        this.detalles.add(detalle);
    }
    
    /**
     * Calcula el subtotal sin IVA de toda la factura
     */
    public Double calcularSubtotal() {
        return detalles.stream()
            .mapToDouble(DetalleFactura::getSubtotalNeto)
            .sum();
    }
    
    /**
     * Calcula el IVA total de la factura
     */
    public Double calcularIVATotal() {
        return detalles.stream()
            .mapToDouble(DetalleFactura::getMontoIVA)
            .sum();
    }
    
    /**
     * Verifica si la factura está vencida
     */
    public boolean estaVencida() {
        return LocalDate.now().isAfter(this.fechaVencimiento) 
               && this.estado == EstadoFactura.PENDIENTE;
    }
    
    /**
     * Verifica si esta factura discrimina IVA según su tipo
     */
    public boolean discriminaIVA() {
        return this.tipoFactura != null && this.tipoFactura.discriminaIVA();
    }
}
