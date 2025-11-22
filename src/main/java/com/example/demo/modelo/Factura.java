package com.example.demo.modelo;

import java.time.LocalDate;
import java.util.Set;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
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
public class Factura 
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    private String numero;
    private LocalDate fechaEmision;
    private LocalDate fechaVencimiento;
    private Double montoTotalFinal;
    private EstadoFactura estado;
    
    // Relación N-1 con CuentaCliente
    @ManyToOne
    private CuentaCliente cuentaCliente; 
    // Relación 1-N con DetalleFactura
    @OneToMany
    private Set<DetalleFactura> detalles;
    // Relación 1-1 con NotaCredito (Opcional, para navegación)
    @OneToOne
    private NotaCredito notaCredito; 
    
    // MÉTODO DE NEGOCIO
    public void calcularTotal() { /* ... */ }
}
