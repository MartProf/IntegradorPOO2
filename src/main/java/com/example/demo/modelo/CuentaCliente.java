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
public class CuentaCliente {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String numeroCuenta;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoCuenta estado;
    
    private Double deudaPendiente = 0.0;
    private Double saldoAFavor = 0.0; // Crédito Fiscal
    
    // Relación 1-1 con Cliente - CORREGIDO (lado propietario)
    @OneToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;
    
    // Relación 1-N con ServicioContratado - CORREGIDO
    @OneToMany(mappedBy = "cuentaCliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ServicioContratado> serviciosContratados;
    
    // MÉTODOS DE NEGOCIO
    public void realizarABM() {
        // Lógica para Alta, Baja, Modificación (HU 1.1)
    }
    
    public void revertirDeuda(Double monto) {
        if (this.deudaPendiente != null && monto != null) {
            this.deudaPendiente -= monto;
            if (this.deudaPendiente < 0) {
                this.saldoAFavor = Math.abs(this.deudaPendiente);
                this.deudaPendiente = 0.0;
            }
        }
    }
    
    public void agregarDeuda(Double monto) {
        if (monto != null && monto > 0) {
            if (this.saldoAFavor != null && this.saldoAFavor > 0) {
                // Aplicar saldo a favor primero
                if (monto <= this.saldoAFavor) {
                    this.saldoAFavor -= monto;
                } else {
                    monto -= this.saldoAFavor;
                    this.saldoAFavor = 0.0;
                    this.deudaPendiente += monto;
                }
            } else {
                this.deudaPendiente += monto;
            }
        }
    }
}