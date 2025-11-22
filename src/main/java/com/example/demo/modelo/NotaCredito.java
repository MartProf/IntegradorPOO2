package com.example.demo.modelo;

import java.time.LocalDate;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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
public class NotaCredito 
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    private String numero;
    private LocalDate fechaEmision;
    private Double montoTotalCredito;
    private String motivo; // Registro Obligatorio del motivo

    // Relación N-1 (CLAVE PARA TRAZABILIDAD)
    @ManyToOne
    private Factura facturaAnulada; 
    
    // Relación N-1 con CuentaCliente
    @ManyToOne
    private CuentaCliente cuentaCliente; 

    // MÉTODO DE NEGOCIO (HU 1.7)
    public void generar(Factura factura, String motivo) { 
        // Lógica: Actualiza estado de Factura, llama a CuentaCliente.revertirDeuda()
    }

}
