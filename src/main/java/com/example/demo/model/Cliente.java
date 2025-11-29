package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "clientes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ToString.Include
    private String razonSocial;

    private String nombre;
    private String apellido;

    @Column(unique = true, nullable = false)
    private String cuitDni;

    private String domicilio;
    private String telefono;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CondicionFiscal condicionIVA;

    // Relación 1-1 con CuentaCliente (Cliente es dueño)
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "cliente")
    private CuentaCliente cuentaCliente;
}
