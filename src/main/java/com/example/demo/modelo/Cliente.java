package com.example.demo.modelo;

public class Cliente {
    private Long id;
    private String razonSocial;
    private String cuitDni;
    private String domicilio;
    private String contacto;
    private CondicionFiscal condicionIVA;

    public Cliente(Long id, String razonSocial, String cuitDni, String domicilio, String contacto, CondicionFiscal condicionIVA) {
        this.id = id;
        this.razonSocial = razonSocial;
        this.cuitDni = cuitDni;
        this.domicilio = domicilio;
        this.contacto = contacto;
        this.condicionIVA = condicionIVA;
    }

    public CondicionFiscal getCondicionIVA() {
        return condicionIVA;
    }
}
