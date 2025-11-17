package com.example.demo.modelo;

public class ResponsableInscripto implements CondicionFiscal {
    @Override
    public double calcularIVA(double montoBase) {
        return montoBase * 0.21;
    }
}

