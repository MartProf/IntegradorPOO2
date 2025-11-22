package com.example.demo.modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Pago {
    private Long id;
    private LocalDate fechaPago = LocalDate.now();
    private double montoPagadoTotal;
    private List<ItemPago> items = new ArrayList<>();

    public void registrarPago(double monto, MedioPago medio) {
        items.add(new ItemPago(monto, medio.name()));
        montoPagadoTotal += monto;
    }
}

