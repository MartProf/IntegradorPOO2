package com.example.demo.modelo;

import java.util.Set;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
public class CuentaCliente 
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    private EstadoCuenta estado;
    private Double deudaPendiente;
    private Double saldoAFavor; // Crédito Fiscal
    
    // Relación 1-N con ServicioContratado
    @OneToMany
    private Set<ServicioContratado> serviciosContratados; 
    
    // MÉTODOS DE NEGOCIO
    public void realizarABM() { /* ... */ } // HU 1.1
    public void revertirDeuda(Double monto) { /* ... */ }
}
