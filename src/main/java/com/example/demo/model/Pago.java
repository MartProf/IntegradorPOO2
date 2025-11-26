package com.example.demo.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "pagos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class Pago 
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    
    @Column(nullable = false)
    private LocalDate fechaPago;
    
    @Column(nullable = false)
    private Double montoPagadoTotal = 0.0;
    
    // Relación N-1 con Factura (un pago aplica a una factura específica)
    @ManyToOne
    @JoinColumn(name = "factura_id", nullable = false)
    private Factura factura;
    
    // Relación N-1 con CuentaCliente (pago sale de la cuenta)
    @ManyToOne
    @JoinColumn(name = "cuenta_cliente_id", nullable = false)
    private CuentaCliente cuentaCliente; 
    
    // Relación 1-N con ItemPago (Pagos parciales/múltiples medios)
    @OneToMany(mappedBy = "pago", cascade = CascadeType.ALL)
    private Set<ItemPago> itemsPago = new HashSet<>();
    
    // Relación 1-1 con Recibo
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "recibo_id", unique = true)
    private Recibo recibo; 

    // MÉTODO DE NEGOCIO (HU 1.5)
    public void registrarPago(Double monto, MedioPago medio) { 
        // Crear ItemPago
        ItemPago item = new ItemPago();
        item.setMonto(monto);
        item.setMedioPago(medio);
        item.setPago(this);
        this.itemsPago.add(item);
        
        // Actualizar total del pago
        this.montoPagadoTotal += monto;
        
        // Actualizar estado de la factura
        if (this.factura != null) {
            Double totalPagado = this.factura.getPagos().stream()
                .mapToDouble(Pago::getMontoPagadoTotal)
                .sum();
            
            if (totalPagado >= this.factura.getMontoTotalFinal()) {
                this.factura.setEstado(EstadoFactura.PAGADA_TOTAL);
            } else if (totalPagado > 0) {
                this.factura.setEstado(EstadoFactura.PAGADA_PARCIAL);
            }
        }
        
        // Actualizar cuenta cliente (reducir deuda)
        if (this.cuentaCliente != null) {
            Double deudaActual = this.cuentaCliente.getDeudaPendiente();
            this.cuentaCliente.setDeudaPendiente(deudaActual - monto);
        }
    }
}
