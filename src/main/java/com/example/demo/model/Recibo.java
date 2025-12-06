package com.example.demo.model;

import java.time.LocalDate;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Comprobante de pago (ya no se usa - el Pago tiene su número de recibo)
 * Mantenido por compatibilidad con diseño inicial
 * @deprecated Usar Pago.numeroRecibo en su lugar
 */
@Entity
@Table(name = "recibos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@Deprecated
public class Recibo 
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String numero;
    
    private LocalDate fechaEmision;
    private String concepto;
    private Double montoPagado;
    
    // Relación 1-1 con Pago
    @OneToOne(mappedBy = "recibo")
    private Pago pago;
}
