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
public class Recibo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ToString.Include
    private LocalDate fechaEmision = LocalDate.now();

    private String concepto;
    private Double montoPagado;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pago_id", nullable = false)
    private Pago pago;

    @Column(length = 1024)
    private String detalleGeneral;

    // MÉTODOS DE NEGOCIO (HU 1.6)
    public void generarDetalleDesdePago() {
        if (this.pago != null && this.pago.getItemPagos() != null) {
            StringBuilder detalle = new StringBuilder();
            detalle.append("RECIBO DE PAGO\n");
            detalle.append("================\n");
            detalle.append("Factura: ").append(this.pago.getFactura().getNumero()).append("\n");
            detalle.append("Cliente: ").append(this.pago.getFactura().getCuentaCliente().getCliente().getRazonSocial()).append("\n");
            detalle.append("Fecha: ").append(this.fechaEmision).append("\n");
            detalle.append("Monto Total: $").append(String.format("%.2f", this.montoPagado)).append("\n\n");
            detalle.append("DETALLE DE MEDIOS DE PAGO:\n");
            
            for (ItemPago item : this.pago.getItemPagos()) {
                detalle.append(String.format("- %s: $%.2f", 
                    item.getMedio().toString(), 
                    item.getMonto()));
                
                if (item.getReferencia() != null && !item.getReferencia().isEmpty()) {
                    detalle.append(" (Ref: ").append(item.getReferencia()).append(")");
                }
                detalle.append("\n");
            }
            
            this.detalleGeneral = detalle.toString();
            this.concepto = "Pago de servicios - Factura " + this.pago.getFactura().getNumero();
        }
    }
    
    public static Recibo crearDesdePago(Pago pago) {
        Recibo recibo = new Recibo();
        recibo.setPago(pago);
        recibo.setMontoPagado(pago.getMontoPagadoTotal());
        recibo.generarDetalleDesdePago();
        return recibo;
    }
}