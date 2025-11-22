<<<<<<< HEAD

/*
package com.example.demo.modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Pago {
    private Long id;
    private LocalDate fechaPago = LocalDate.now();
    private double montoPagadoTotal;
    private List<ItemPago> items = new ArrayList<>();

    public void registrarPago(double monto, MedioPago medio) {
        items.add(new ItemPago(monto, medio.name()));
        montoPagadoTotal += monto;
    }
}

 */
=======
package com.example.demo.modelo;

import java.time.LocalDate;
import java.util.Set;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
public class Pago 
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    private LocalDate fechaPago;
    private Double montoPagadoTotal;
    
    // Relación N-1 con Factura
    @ManyToOne
    private Factura factura;
    // Relación N-1 con CuentaCliente
    @ManyToOne
    private CuentaCliente cuentaCliente; 
    // Relación 1-N con ItemPago (Pagos parciales/múltiples medios)
    @OneToMany
    private Set<ItemPago> itemsPago; 
    // Relación 1-1 con Recibo
    @OneToOne
    private Recibo recibo; 

    // MÉTODO DE NEGOCIO (HU 1.5)
    public void registrarPago(Double monto, MedioPago medio) { 
        // Lógica: crea ItemPago y actualiza Factura y CuentaCliente
    }
}
>>>>>>> origin/pike
