package com.example.demo.model;

import java.util.HashSet;
import java.util.Set;

import com.example.demo.model.enums.EstadoCuenta;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "cuentas_clientes")
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
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoCuenta estado;
    
    @Column(nullable = false)
    private Double deudaPendiente = 0.0;
    
    @Column(nullable = false)
    private Double saldoAFavor = 0.0; // Crédito Fiscal
    
    // Soft Delete
    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean activo = true;
    
    // Relación 1-1 inversa con Cliente
    @OneToOne
    @JoinColumn(name = "cliente_id", unique = true)
    private Cliente cliente;
    
    // Relación 1-N con ServicioContratado
    @OneToMany(mappedBy = "cuentaCliente", cascade = CascadeType.ALL)
    private Set<ServicioContratado> serviciosContratados = new HashSet<>();
    
    // MÉTODOS DE NEGOCIO
    public void realizarABM() { /* ... */ } // HU 1.1
    public void revertirDeuda(Double monto) { 
        this.deudaPendiente -= monto;
        this.saldoAFavor += monto;
    }
}
