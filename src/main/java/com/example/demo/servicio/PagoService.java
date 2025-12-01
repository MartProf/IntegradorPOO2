package com.example.demo.servicio;

import com.example.demo.modelo.*;
import com.example.demo.repositorio.*;
import com.example.demo.servicio.dto.ItemPagoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PagoService {

    private final PagoRepository pagoRepository;
    private final ReciboRepository reciboRepository;
    private final FacturaRepository facturaRepository;
    private final CuentaClienteRepository cuentaClienteRepository;

    @Transactional
    public Recibo registrarPagoFactura(Long facturaId, List<ItemPagoDTO> itemPagos) {
        Factura factura = facturaRepository.findById(Objects.requireNonNull(facturaId))
            .orElseThrow(() -> new RuntimeException("Factura no encontrada."));

        double montoPagadoTotal = itemPagos.stream()
            .mapToDouble(ItemPagoDTO::getMonto)
            .sum();

        Pago pago = new Pago();
        pago.setFechaPago(LocalDate.now());
        pago.setFactura(factura);
        pago.setMontoPagadoTotal(montoPagadoTotal);

        List<ItemPago> items = itemPagos.stream().map(dto -> {
            ItemPago item = new ItemPago();
            item.setPago(pago);
            item.setMonto(dto.getMonto());
            item.setMedio(dto.getMedio());
            item.setReferencia(dto.getReferencia());
            return item;
        }).collect(Collectors.toList());

        pago.setItemPagos(items);

        CuentaCliente cuenta = factura.getCuentaCliente();
        cuenta.setDeudaPendiente(cuenta.getDeudaPendiente() - montoPagadoTotal);

        if (montoPagadoTotal >= factura.getMontoTotalFinal()) {
            factura.setEstado(EstadoFactura.PAGADA_TOTAL);
        } else {
            factura.setEstado(EstadoFactura.PAGADA_PARCIAL);
        }

        cuentaClienteRepository.save(cuenta);
        facturaRepository.save(factura);
        Pago pagoGuardado = pagoRepository.save(pago);

        Recibo recibo = new Recibo();
        recibo.setPago(pagoGuardado);
        recibo.setMontoPagado(montoPagadoTotal);
        recibo.setFechaEmision(LocalDate.now());
        recibo.setConcepto("Pago factura " + factura.getNumero());
        recibo.setDetalleGeneral("Pago registrado por $" + montoPagadoTotal);

        return reciboRepository.save(recibo);
    }

    public Recibo obtenerReciboPorId(Long reciboId) {
        return reciboRepository.findById(Objects.requireNonNull(reciboId))
                .orElseThrow(() -> new RuntimeException("Recibo no encontrado"));
    }

    public List<Pago> obtenerPagosPorFactura(Long facturaId) {
        return pagoRepository.findByFacturaId(Objects.requireNonNull(facturaId));
    }

    public List<Recibo> obtenerRecibosPorCuenta(Long cuentaId) {
        return reciboRepository.findAll().stream()
            .filter(recibo -> recibo.getPago() != null && 
                             recibo.getPago().getFactura() != null &&
                             recibo.getPago().getFactura().getCuentaCliente() != null &&
                             recibo.getPago().getFactura().getCuentaCliente().getId().equals(Objects.requireNonNull(cuentaId)))
            .collect(Collectors.toList());
    }
}