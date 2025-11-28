package com.example.demo.modelo;

import java.time.LocalDate;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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
public class ServicioContratado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private Double precioPersonalizado;
    private Double montoDescuento;
    private LocalDate fechaInicio;
    
    // Relación N-1 con CuentaCliente
    @ManyToOne
    private CuentaCliente cuentaCliente; 
    // Relación N-1 con Servicio
    @ManyToOne
    private Servicio servicio;
    // Relación N-1 con Plan
    @ManyToOne
    private Plan plan;
    
    // MÉTODO DE NEGOCIO
    public Double calcularPrecioFinal() { 
        // Lógica de precioBase - montoDescuento
        return 0.0; 
    }

    public static Object findById(Long servicioContratadoId) 
    {
        throw new UnsupportedOperationException("Unimplemented method 'findById'");
    }
}
