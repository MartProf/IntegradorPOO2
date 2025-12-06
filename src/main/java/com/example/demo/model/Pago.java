package com.example.demo.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Representa un pago realizado por un cliente.
 * Un pago puede cubrir una o varias facturas.
 */
@Entity
@Table(name = "pagos")
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
    
    @Column(unique = true, nullable = false)
    private String numeroRecibo;
    
    @Column(nullable = false)
    private LocalDate fechaPago;
    
    @Column(nullable = false)
    private Double montoTotal = 0.0;
    
    private String observaciones;
    
    // Relación N-1 con CuentaCliente (pago realizado por una cuenta)
    @ManyToOne
    @JoinColumn(name = "cuenta_cliente_id", nullable = false)
    private CuentaCliente cuentaCliente; 
    
    // Relación 1-N con ItemPago (distribución del pago entre facturas)
    @OneToMany(mappedBy = "pago", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPago> itemsPago = new ArrayList<>();
    
    // Relación 1-1 con Recibo (opcional, deprecado - usar numeroRecibo)
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "recibo_id")
    private Recibo recibo;
    
    // MÉTODOS DE NEGOCIO
    
    /**
     * Agrega un item de pago a este pago
     */
    public void agregarItem(ItemPago item) {
        item.setPago(this);
        this.itemsPago.add(item);
    }
    
    /**
     * Calcula el total del pago sumando todos los items
     */
    public void calcularTotal() {
        this.montoTotal = itemsPago.stream()
            .mapToDouble(ItemPago::getMonto)
            .sum();
    }
}