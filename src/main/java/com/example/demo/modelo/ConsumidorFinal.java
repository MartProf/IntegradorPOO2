package com.example.demo.modelo;

public class ConsumidorFinal implements CondicionFiscal {
    @Override
    public double calcularIVA(double montoBase) {
        return montoBase * 0.21; // Agrega IVA final en factura
    }
}
