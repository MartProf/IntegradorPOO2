package com.example.demo.modelo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class Factura {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    
    @Column(unique = true)
    private String numero;
    
    private LocalDate fechaEmision;
    private LocalDate fechaVencimiento;
    private Double montoTotalFinal;
    
    @Enumerated(EnumType.STRING)
    private EstadoFactura estado = EstadoFactura.PENDIENTE;
    
    // Campo necesario para la anulación (HU 1.7)
    private String motivoAnulacion; 

    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Pago> pagos;
    
    // Relación N-1 con CuentaCliente
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cuenta_cliente_id", nullable = false)
    private CuentaCliente cuentaCliente; 
    
    // Relación 1-N con DetalleFactura
    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<DetalleFactura> detalles;
    
    // Relación 1-1 con NotaCredito (Opcional, para navegación)
    @OneToOne(mappedBy = "facturaAnulada", fetch = FetchType.LAZY)
    private NotaCredito notaCredito;
    
    // MÉTODOS DE NEGOCIO
    public void calcularTotalFinal() {
        if (this.detalles != null) {
            this.montoTotalFinal = this.detalles.stream()
                .mapToDouble(detalle -> {
                    detalle.calcularTotales(); // Asegurar cálculos
                    return detalle.getMontoTotalBruto() != null ? detalle.getMontoTotalBruto() : 0.0;
                })
                .sum();
        } else {
            this.montoTotalFinal = 0.0;
        }
    }
    
    public void anular(String motivo) {
        this.estado = EstadoFactura.ANULADA_POR_NC;
        this.motivoAnulacion = motivo;
    }
    
    public Double getMontoPagado() {
        if (this.pagos == null) return 0.0;
        return this.pagos.stream()
            .mapToDouble(pago -> pago.getMontoPagadoTotal() != null ? pago.getMontoPagadoTotal() : 0.0)
            .sum();
    }
    
    public Double getSaldoPendiente() {
        if (this.montoTotalFinal == null) return 0.0;
        return this.montoTotalFinal - this.getMontoPagado();
    }
    
    public void actualizarEstado() {
        Double saldoPendiente = this.getSaldoPendiente();
        
        if (saldoPendiente <= 0) {
            this.estado = EstadoFactura.PAGADA_TOTAL;
        } else if (saldoPendiente < this.montoTotalFinal) {
            this.estado = EstadoFactura.PAGADA_PARCIAL;
        } else {
            this.estado = EstadoFactura.PENDIENTE;
        }
    }
}