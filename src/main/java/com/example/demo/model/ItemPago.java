package com.example.demo.model;

import com.example.demo.model.enums.MedioPago;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Representa la aplicación de un monto de pago a una factura específica.
 * Permite registrar pagos parciales y múltiples medios de pago.
 */
@Entity
@Table(name = "items_pago")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class ItemPago  
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false)
    private Double monto;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MedioPago medioPago;
    
    private String referencia; // Número de cheque, número de transferencia, etc.
    
    // Relación N-1 con Pago
    @ManyToOne
    @JoinColumn(name = "pago_id", nullable = false)
    private Pago pago; 
    
    // Relación N-1 con Factura (a qué factura se aplica este pago)
    @ManyToOne
    @JoinColumn(name = "factura_id", nullable = false)
    private Factura factura;
}
