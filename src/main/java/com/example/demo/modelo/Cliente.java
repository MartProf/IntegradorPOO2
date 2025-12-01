package com.example.demo.modelo;

import jakarta.persistence.*;
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
public class Cliente {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    
    @ToString.Include
    private String razonSocial;
    
    @Column(unique = true, nullable = false)
    private String cuitDni;
    
    private String domicilio;
    private String contacto;
    private String email;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CondicionFiscal condicionIVA;
    
    // Relación 1-1 con CuentaCliente - CORREGIDO
    @OneToOne(mappedBy = "cliente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private CuentaCliente cuentaCliente;
}