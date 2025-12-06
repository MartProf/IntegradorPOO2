package com.example.demo.model;

import java.time.LocalDate;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "servicios_contratados")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ServicioContratado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private Double precioPersonalizado;
    
    private Double montoDescuento = 0.0;
    
    @Column(nullable = false)
    private LocalDate fechaInicio;
    
    @Column(nullable = false)
    private Boolean activo = true;
    
    // Relación N-1 con CuentaCliente
    @ManyToOne
    @JoinColumn(name = "cuenta_cliente_id", nullable = false)
    private CuentaCliente cuentaCliente; 
    
    // Relación N-1 con Servicio (puede ser null si se contrató un plan)
    @ManyToOne
    @JoinColumn(name = "servicio_id")
    private Servicio servicio;
    
    // Relación N-1 con Plan (puede ser null si se contrató un servicio individual)
    @ManyToOne
    @JoinColumn(name = "plan_id")
    private Plan plan;
    
    // MÉTODOS DE NEGOCIO
    
    /**
     * Calcula el precio final considerando:
     * 1. Si hay precio personalizado, usar ese
     * 2. Si no, usar el precio del plan o servicio contratado
     * 3. Restar el descuento aplicado
     */
    public Double calcularPrecioFinal() { 
        Double precioBase = obtenerPrecioBase();
        Double descuento = (montoDescuento != null) ? montoDescuento : 0.0;
        return precioBase - descuento;
    }
    
    /**
     * Obtiene el precio base según lo contratado
     */
    private Double obtenerPrecioBase() {
        if (precioPersonalizado != null) {
            return precioPersonalizado;
        }
        
        if (plan != null) {
            return plan.getPrecioBase();
        }
        
        if (servicio != null) {
            return servicio.getPrecioBase();
        }
        
        return 0.0;
    }
    
    /**
     * Verifica si se contrató un plan
     */
    public boolean esPlan() {
        return plan != null;
    }
    
    /**
     * Verifica si se contrató un servicio individual
     */
    public boolean esServicioIndividual() {
        return servicio != null && plan == null;
    }
    
    /**
     * Obtiene el nombre de lo contratado (plan o servicio)
     */
    public String getNombreContratado() {
        if (plan != null) {
            return plan.getNombre();
        }
        if (servicio != null) {
            return servicio.getNombre();
        }
        return "Sin definir";
    }
    
    /**
     * Obtiene el tipo de contratación para mostrar en vistas
     */
    public String getTipoContratacion() {
        if (plan != null) {
            return "Plan";
        }
        if (servicio != null) {
            return "Servicio";
        }
        return "Indefinido";
    }
}
