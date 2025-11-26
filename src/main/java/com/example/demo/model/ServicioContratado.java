package com.example.demo.model;

import java.time.LocalDate;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "servicios_contratados")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class ServicioContratado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private Double precioPersonalizado;
    
    private Double montoDescuento = 0.0;
    
    @Column(nullable = false)
    private LocalDate fechaInicio;
    
    // Relación N-1 con CuentaCliente
    @ManyToOne
    @JoinColumn(name = "cuenta_cliente_id", nullable = false)
    private CuentaCliente cuentaCliente; 
    
    // Relación N-1 con Servicio
    @ManyToOne
    @JoinColumn(name = "servicio_id", nullable = false)
    private Servicio servicio;
    
    // Relación N-1 con Plan
    @ManyToOne
    @JoinColumn(name = "plan_id")
    private Plan plan;
    
    // MÉTODO DE NEGOCIO
    public Double calcularPrecioFinal() { 
        Double precioBase = (precioPersonalizado != null) ? precioPersonalizado : 
                           (plan != null ? plan.getPrecioBase() : servicio.getPrecioBase());
        return precioBase - (montoDescuento != null ? montoDescuento : 0.0);
    }
}
