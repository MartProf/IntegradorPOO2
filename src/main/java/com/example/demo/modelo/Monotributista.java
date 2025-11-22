package com.example.demo.modelo;

public class Monotributista implements CondicionFiscal {
    @Override
    public double calcularIVA(double montoBase) {
        return 0; // No discrimina IVA
    }
}
