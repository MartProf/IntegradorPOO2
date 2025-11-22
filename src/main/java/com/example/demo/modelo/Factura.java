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
@Table(name = "facturas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
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
    
    // Relación N-1 con CuentaCliente
    @ManyToOne
    @JoinColumn(name = "cuenta_cliente_id", nullable = false)
    private CuentaCliente cuentaCliente; 
    
    // Relación 1-N con DetalleFactura
    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<DetalleFactura> detalles = new HashSet<>();
    
    // Relación 1-1 con NotaCredito (Opcional, para navegación)
    @OneToOne(mappedBy = "facturaAnulada")
    private NotaCredito notaCredito; 
    
    // MÉTODO DE NEGOCIO
    public void calcularTotal() { 
        this.montoTotalFinal = detalles.stream()
            .mapToDouble(d -> d.getSubtotalNeto() + d.getMontoIVA())
            .sum();
    }
}
