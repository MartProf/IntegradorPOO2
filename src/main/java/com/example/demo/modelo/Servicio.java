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
public class Servicio {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String nombre;
    
    private String descripcion;
    
    @Column(nullable = false)
    private Double precioMensual;
    
    @Column(nullable = false)
    private Double tasaIVA = 21.0; // IVA por defecto 21%

    // Relación 1-N con ServicioContratado - CORREGIDO
    @OneToMany(mappedBy = "servicio")
    private Set<ServicioContratado> contratos;
    
    // MÉTODOS DE NEGOCIO PARA FACTURACIÓN
    public Double calcularPrecioConIVA() {
        return this.precioMensual * (1 + (this.tasaIVA / 100));
    }
    
    public Double getMontoIVA() {
        return this.precioMensual * (this.tasaIVA / 100);
    }
    
    public boolean estaActivo() {
        return this.contratos != null && 
               this.contratos.stream().anyMatch(contrato -> 
                   contrato.getCuentaCliente().getEstado() == EstadoCuenta.ACTIVA);
    }
}