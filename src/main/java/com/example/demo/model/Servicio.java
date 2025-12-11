package com.example.demo.model;

import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "servicios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@JsonIgnoreProperties({"contratos", "hibernateLazyInitializer", "handler"})
public class Servicio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    
    @Column(nullable = false)
    private String nombre;
    
    private String descripcion;
    
    @Column(nullable = false)
    private Double alicuotaIVA;
    
    @Column(nullable = false)
    private Double precioBase;
    
    // Soft Delete
    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean activo = true;

    // Relación 1-N con ServicioContratado
    @OneToMany(mappedBy = "servicio")
    private Set<ServicioContratado> contratos = new HashSet<>();
    
    // Métodos para soft delete
    public void eliminar() {
        this.activo = false;
    }
    
    public void reactivar() {
        this.activo = true;
    }
}
