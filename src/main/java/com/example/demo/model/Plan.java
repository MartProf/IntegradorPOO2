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
@Table(name = "planes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@JsonIgnoreProperties({"contratos", "hibernateLazyInitializer", "handler"})
public class Plan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    
    @Column(nullable = false)
    private String nombre;
    
    @Column(nullable = false)
    private Double precioBase;
    
    private String descripcion;

    // Relación N-N con Servicio (servicios incluidos en el plan)
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "plan_servicios",
        joinColumns = @JoinColumn(name = "plan_id"),
        inverseJoinColumns = @JoinColumn(name = "servicio_id")
    )
    private Set<Servicio> serviciosIncluidos = new HashSet<>();

    // Relación 1-N con ServicioContratado
    @OneToMany(mappedBy = "plan")
    private Set<ServicioContratado> contratos = new HashSet<>();
    
    // MÉTODOS DE NEGOCIO
    
    // Agregar un servicio al plan
    public void agregarServicio(Servicio servicio) {
        this.serviciosIncluidos.add(servicio);
    }
    
    // Remover un servicio del plan
    public void removerServicio(Servicio servicio) {
        this.serviciosIncluidos.remove(servicio);
    }
    
    // Verificar si el plan incluye un servicio específico
    public boolean incluyeServicio(Long servicioId) {
        return this.serviciosIncluidos.stream()
                .anyMatch(s -> s.getId().equals(servicioId));
    }
    
    // Obtener precio total de servicios incluidos (para comparar con precioBase)
    public Double calcularPrecioServiciosIndividuales() {
        return this.serviciosIncluidos.stream()
                .mapToDouble(Servicio::getPrecioBase)
                .sum();
    }
    
    // Calcular el ahorro del plan vs servicios individuales
    public Double calcularAhorro() {
        return calcularPrecioServiciosIndividuales() - this.precioBase;
    }
}

