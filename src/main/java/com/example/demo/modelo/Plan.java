package com.example.demo.modelo;

public class Plan {
    private Long id;
    private String nombre;
    private double precioBase;

    public Plan(Long id, String nombre, double precioBase) {
        this.id = id;
        this.nombre = nombre;
        this.precioBase = precioBase;
    }

    public double getPrecioBase() {
        return precioBase;
    }
}

