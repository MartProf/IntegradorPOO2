<<<<<<< HEAD
/*
package com.example.demo.modelo;

import java.math.BigDecimal;
import java.util.Objects;


 * Representa un servicio ofrecido por la empresa.
 * Ahora el precio está en BigDecimal para preservar precisión monetaria.

public class Servicio {
    private Long id;
    private String nombre;
    private String descripcion;
    // Alícuota IVA expresada como porcentaje en decimal (ej: 0.21 para 21%)
    private BigDecimal alicuotaIVA;
    private BigDecimal precioBase;

    public Servicio(Long id, String nombre, String descripcion, BigDecimal precioBase, BigDecimal alicuotaIVA) {
        this.id = id;
        this.nombre = Objects.requireNonNull(nombre);
        this.descripcion = descripcion;
        this.precioBase = precioBase != null ? precioBase : BigDecimal.ZERO;
        this.alicuotaIVA = alicuotaIVA != null ? alicuotaIVA : BigDecimal.ZERO;
    }

    public BigDecimal getAlicuotaIVA() {
        return alicuotaIVA;
    }

    public BigDecimal getPrecioBase() {
        return precioBase;
    }

    public String getNombre() {
        return nombre;
    }
}
 */
=======
package com.example.demo.modelo;

import java.util.Set;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
public class Servicio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    private String nombre;
    private String descripcion;
    private double alicuotaIVA;

    // Relación 1-N con ServicioContratado
    @OneToMany
    private Set<ServicioContratado> contratos;
}
>>>>>>> origin/pike
