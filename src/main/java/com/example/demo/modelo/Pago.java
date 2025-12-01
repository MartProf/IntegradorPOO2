package com.example.demo.modelo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class Pago {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    
    private LocalDate fechaPago = LocalDate.now();
    
    @Column(nullable = false)
    private Double montoPagadoTotal;

    // Relación 1-N con ItemPago (HU 1.5 - Múltiples medios de pago)
    @OneToMany(mappedBy = "pago", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPago> itemPagos;
    
    // Relación N-1 con Factura
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "factura_id", nullable = false)
    private Factura factura;
    
    // Relación 1-1 con Recibo
    @OneToOne(mappedBy = "pago", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Recibo recibo;

    // MÉTODOS DE NEGOCIO (HU 1.5)
    public void calcularTotalDesdeItems() {
        if (this.itemPagos != null) {
            this.montoPagadoTotal = this.itemPagos.stream()
                .filter(item -> item.validarMonto())
                .mapToDouble(ItemPago::getMonto)
                .sum();
        }
    }
    
    public void agregarItemPago(ItemPago itemPago) {
        if (this.itemPagos != null) {
            itemPago.setPago(this);
            this.itemPagos.add(itemPago);
            this.calcularTotalDesdeItems();
        }
    }
    
    public boolean validarPago() {
        return this.montoPagadoTotal != null && 
               this.montoPagadoTotal > 0 && 
               this.factura != null;
    }
}