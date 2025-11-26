package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
    
    private String referencia;
    
    // Relación N-1 con Pago
    @ManyToOne
    @JoinColumn(name = "pago_id", nullable = false)
    private Pago pago; 
    
    // Relación con MedioPago (enum)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MedioPago medioPago;
}
