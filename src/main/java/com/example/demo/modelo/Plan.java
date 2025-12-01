package com.example.demo.modelo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class Plan {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String nombre;
    
    @Column(nullable = false)
    private Double precioBase;

    // Relación 1-N con ServicioContratado - CORREGIDO
    @OneToMany(mappedBy = "plan")
    private Set<ServicioContratado> contratos;
    
    // MÉTODOS DE NEGOCIO
    public Double calcularPrecioConIVA(Double tasaIVA) {
        if (this.precioBase != null && tasaIVA != null) {
            return this.precioBase * (1 + (tasaIVA / 100));
        }
        return this.precioBase;
    }
    
    public boolean tieneContratosActivos() {
        return this.contratos != null && !this.contratos.isEmpty();
    }
}