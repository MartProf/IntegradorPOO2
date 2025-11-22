<<<<<<< HEAD
/*

package com.example.demo.modelo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class Factura {

    private Long id;
    private String numero;
    private LocalDate fechaEmision = LocalDate.now();
    private LocalDate fechaVencimiento;
    private BigDecimal montoTotal = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
    private EstadoFactura estado = EstadoFactura.PENDIENTE;
    private String motivoAnulacion;

    private Cliente cliente;
    private List<DetalleFactura> detalles = new ArrayList<>();

    // Totales calculados
    private BigDecimal subtotalNeto = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
    private BigDecimal ivaTotal = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);

    public Factura(Cliente cliente, String numero, LocalDate fechaVencimiento) {
        this.cliente = cliente;
        this.numero = numero;
        this.fechaVencimiento = fechaVencimiento;
    }

    
     * Agrega una línea a la factura a partir de un ServicioContratado y la cantidad.
     * Calcula el subtotal neto (precio * cantidad), luego consulta a la CondicionFiscal del cliente
     * para determinar cuánto IVA se aplica sobre ese subtotal.
     *
     * @param servicioContratado servicio contratado (contiene el servicio y precio personalizado)
     * @param cantidad cantidad de unidades
     
    public void agregarDetalleDesdeServicioContratado(ServicioContratado servicioContratado, int cantidad) {
        // 1) Determinar precio unitario: si existe precio personalizado (>0), lo usamos; si no, usamos el precio base del servicio.
        double precioFinal = servicioContratado.getPrecioFinal();
        BigDecimal precioUnitario = precioFinal > 0.0
                ? BigDecimal.valueOf(precioFinal)
                : servicioContratado.getServicio().getPrecioBase();

        // convertir a BigDecimal con escala 2
        BigDecimal precioUnitarioBD = precioUnitario.setScale(2, RoundingMode.HALF_UP);

        // 2) subtotal neto = precioUnitario * cantidad
        BigDecimal subtotal = precioUnitarioBD.multiply(BigDecimal.valueOf(cantidad)).setScale(2, RoundingMode.HALF_UP);

        // 3) Obtener alícuota del servicio (ej: 0.21)
        BigDecimal alicuotaServicio = servicioContratado.getServicio().getAlicuotaIVA();

        // 4) Delegar al cliente (CondicionFiscal) el cálculo del IVA:
        //    la implementación de CondicionFiscal puede devolver 0 si exento, o montoBase * 0.21, etc.
        double ivaCalculadoDouble = cliente.getCondicionIVA().calcularIVA(subtotal.doubleValue());
        BigDecimal montoIVA = BigDecimal.valueOf(ivaCalculadoDouble).setScale(2, RoundingMode.HALF_UP);

        // OBSERVACIÓN: aquí asumimos que CondicionFiscal devuelve el monto de IVA sobre el montoBase.
        // Alternativa (más robusta) sería que CondicionFiscal devolviera un multiplicador o definiera reglas
        // más complejas; en este TP la interfaz devuelve el monto.

        // 5) Crear DetalleFactura y añadirlo
        DetalleFactura detalle = new DetalleFactura(cantidad, subtotal, montoIVA,
                servicioContratado.getServicio().getNombre(), alicuotaServicio);

        detalles.add(detalle);

        // 6) Recalcular totales de la factura
        calcularTotales();
    }

    
     * Recalcula subtotalNeto, ivaTotal y montoTotal a partir de la lista de detalles.
     * Debe llamarse después de cualquier modificación de detalles.
     
    public void calcularTotales() {
        BigDecimal subtotal = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        BigDecimal iva = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);

        for (DetalleFactura d : detalles) {
            subtotal = subtotal.add(d.getSubtotalNeto());
            iva = iva.add(d.getMontoIVA());
        }

        this.subtotalNeto = subtotal.setScale(2, RoundingMode.HALF_UP);
        this.ivaTotal = iva.setScale(2, RoundingMode.HALF_UP);
        this.montoTotal = this.subtotalNeto.add(this.ivaTotal).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Anula la factura: registra motivo y cambia estado a ANULADA.
     * @param motivo texto explicativo
     
    public void anular(String motivo) {
        this.motivoAnulacion = motivo;
        this.estado = EstadoFactura.ANULADA;
        // opcional: limpiar totales o dejar histórico; aquí mantenemos valores para auditoría
    }

    // GETTERS (útiles para presentacion / pruebas)
    public BigDecimal getMontoTotal() { return montoTotal; }
    public BigDecimal getSubtotalNeto() { return subtotalNeto; }
    public BigDecimal getIvaTotal() { return ivaTotal; }
    public List<DetalleFactura> getDetalles() { return detalles; }
    public EstadoFactura getEstado() { return estado; }
    public String getMotivoAnulacion() { return motivoAnulacion; }
    public String getNumero() { return numero; }
    public LocalDate getFechaEmision() { return fechaEmision; }
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.ManyToOne;
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
public class Factura 
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    private String numero;
    private LocalDate fechaEmision;
    private LocalDate fechaVencimiento;
    private Double montoTotalFinal;
    private EstadoFactura estado;
    
    // Relación N-1 con CuentaCliente
    @ManyToOne
    private CuentaCliente cuentaCliente; 
    // Relación 1-N con DetalleFactura
    @OneToMany
    private Set<DetalleFactura> detalles;
    // Relación 1-1 con NotaCredito (Opcional, para navegación)
    @OneToOne
    private NotaCredito notaCredito; 
    
    // MÉTODO DE NEGOCIO
    public void calcularTotal() { /* ... */ }
}
>>>>>>> origin/pike
