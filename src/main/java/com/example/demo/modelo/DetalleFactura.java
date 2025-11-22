package com.example.demo.modelo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "detalles_factura")
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

    @Column(nullable = false)
    private Integer cantidad = 1;
    
    @Column(nullable = false)
    private Double subtotalNeto = 0.0;
    
    @Column(nullable = false)
    private Double montoIVA = 0.0;
    
    @Column(nullable = false)
    private String descripcionServicio;
    
    // Relación N-1 con Factura
    @ManyToOne
    @JoinColumn(name = "factura_id", nullable = false)
    private Factura factura;
    
    // Relación N-1 con ServicioContratado
    @ManyToOne
    @JoinColumn(name = "servicio_contratado_id", nullable = false)
    private ServicioContratado servicioContratado;
}
