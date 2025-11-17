package com.example.demo.modelo;

public class Servicio {
    private Long id;
    private String nombre;
    private String descripcion;
    private double alicuotaIVA;

    public Servicio(Long id, String nombre, String descripcion, double alicuotaIVA) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.alicuotaIVA = alicuotaIVA;
    }

    public double getAlicuotaIVA() {
        return alicuotaIVA;
    }

    public String getNombre() {
        return nombre;
    }
}
