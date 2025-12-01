package com.example.demo.modelo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class NotaCredito {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    
    @Column(unique = true)
    private String numero;
    
    private LocalDate fechaEmision;
    private Double montoTotalCredito;
    
    @Column(nullable = false)
    private String motivo; // Registro Obligatorio del motivo (HU 1.7)

    // Relación N-1 con Factura (CLAVE PARA TRAZABILIDAD)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "factura_anulada_id", nullable = false)
    private Factura facturaAnulada; 
    
    // Relación N-1 con CuentaCliente
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cuenta_cliente_id", nullable = false)
    private CuentaCliente cuentaCliente;

    // MÉTODO DE NEGOCIO (HU 1.7)
    public void generarDesdeFactura(Factura factura, String motivo) { 
        this.facturaAnulada = factura;
        this.cuentaCliente = factura.getCuentaCliente();
        this.montoTotalCredito = factura.getMontoTotalFinal();
        this.motivo = motivo;
        this.fechaEmision = LocalDate.now();
        
        // Anular la factura
        factura.anular(motivo);
        
        // Revertir deuda en cuenta cliente
        if (this.cuentaCliente != null && this.montoTotalCredito != null) {
            this.cuentaCliente.revertirDeuda(this.montoTotalCredito);
        }
    }
    
    // Validación de negocio
    public boolean esValida() {
        return this.motivo != null && !this.motivo.trim().isEmpty() &&
               this.montoTotalCredito != null && this.montoTotalCredito > 0 &&
               this.facturaAnulada != null;
    }
}