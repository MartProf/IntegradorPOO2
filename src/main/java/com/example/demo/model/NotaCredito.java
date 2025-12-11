package com.example.demo.model;

import java.time.LocalDate;

import com.example.demo.model.enums.EstadoFactura;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "notas_credito")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class NotaCredito 
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String numero;
    
    @Column(nullable = false)
    private LocalDate fechaEmision;
    
    @Column(nullable = false)
    private Double montoTotalCredito;
    
    @Column(nullable = false, length = 500)
    private String motivo; // Registro Obligatorio del motivo
    
    // Soft Delete
    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean activo = true;

    // Relación 1-1 con Factura (CLAVE PARA TRAZABILIDAD)
    @OneToOne
    @JoinColumn(name = "factura_anulada_id", unique = true, nullable = false)
    private Factura facturaAnulada; 
    
    // Relación N-1 con CuentaCliente
    @ManyToOne
    @JoinColumn(name = "cuenta_cliente_id", nullable = false)
    private CuentaCliente cuentaCliente; 

    // MÉTODO DE NEGOCIO (HU 1.7)
    public void generar(Factura factura, String motivo) { 
        this.facturaAnulada = factura;
        this.motivo = motivo;
        this.montoTotalCredito = factura.getMontoTotalFinal();
        factura.setEstado(EstadoFactura.ANULADA_POR_NC);
        cuentaCliente.revertirDeuda(montoTotalCredito);
    }
}
