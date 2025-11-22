package com.example.demo.modelo;

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
    
    // Según diagrama: pago "sale de" CuentaCliente e "incluye pagos parciales"
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
        // Lógica: crea ItemPago y actualiza Factura y CuentaCliente
    }
}
