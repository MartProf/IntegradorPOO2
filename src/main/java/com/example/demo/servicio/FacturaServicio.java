package com.example.demo.servicio;

import com.example.demo.modelo.*;
import com.example.demo.repositorio.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FacturaServicio {

    private final CuentaClienteRepository cuentaClienteRepository;
    private final FacturaRepository facturaRepository;
    private final NotaCreditoRepository notaCreditoRepository;
    private final ServicioContratadoRepository servicioContratadoRepository;
    private final CalcularFactura calcularFactura;

    @Transactional
    public List<Factura> ejecutarFacturacionMasiva(LocalDate periodo) {
        List<CuentaCliente> cuentasActivas = cuentaClienteRepository.findByEstado(EstadoCuenta.ACTIVA).stream()
            .filter(cuenta -> 
                cuenta.getCliente() != null && 
                cuenta.getCliente().getCondicionIVA() == CondicionFiscal.RESPONSABLE_INSCRIPTO &&
                cuenta.getServiciosContratados() != null && 
                !cuenta.getServiciosContratados().isEmpty()
            )
            .collect(Collectors.toList());

        return cuentasActivas.stream()
            .map(cuenta -> generarFacturaConsolidadaPorCuenta(Objects.requireNonNull(cuenta.getId()), periodo))
            .collect(Collectors.toList());
    }

    public List<Factura> obtenerFacturasPorCuenta(Long cuentaId) {
        return facturaRepository.findByCuentaClienteId(Objects.requireNonNull(cuentaId));
    }

    private Factura generarFacturaConsolidadaPorCuenta(Long cuentaId, LocalDate fechaEmision) {
        CuentaCliente cuenta = cuentaClienteRepository.findById(Objects.requireNonNull(cuentaId))
            .orElseThrow(() -> new RuntimeException("Cuenta no encontrada."));

        Set<ServicioContratado> serviciosParaFacturar = new HashSet<>(cuenta.getServiciosContratados());

        Factura factura = new Factura();
        factura.setCuentaCliente(cuenta);
        factura.setFechaEmision(fechaEmision);
        factura.setFechaVencimiento(fechaEmision.plusDays(15));
        factura.setEstado(EstadoFactura.PENDIENTE);
        factura.setNumero("FAC-" + System.currentTimeMillis());

        Set<DetalleFactura> detalles = new HashSet<>();
        double totalFinal = 0.0;

        for (ServicioContratado servicio : serviciosParaFacturar) {
            DetalleFactura detalle = calcularFactura.crearDetalleFactura(servicio);
            detalle.setFactura(factura);
            detalles.add(detalle);
            totalFinal += detalle.getMontoTotalBruto();
        }

        factura.setDetalles(detalles);
        factura.setMontoTotalFinal(totalFinal);

        cuenta.setDeudaPendiente((cuenta.getDeudaPendiente() != null ? cuenta.getDeudaPendiente() : 0.0) + totalFinal);

        cuentaClienteRepository.save(cuenta);
        return facturaRepository.save(factura);
    }

    @Transactional
    public Factura generarFacturaIndividualPorServicio(Long servicioContratadoId, LocalDate fechaEmision) {
        ServicioContratado contrato = servicioContratadoRepository.findById(Objects.requireNonNull(servicioContratadoId))
            .orElseThrow(() -> new RuntimeException("Servicio no encontrado."));

        CuentaCliente cuenta = contrato.getCuentaCliente();

        Factura factura = new Factura();
        factura.setCuentaCliente(cuenta);
        factura.setFechaEmision(fechaEmision);
        factura.setFechaVencimiento(fechaEmision.plusDays(15));
        factura.setEstado(EstadoFactura.PENDIENTE);
        factura.setNumero("FAC-" + System.currentTimeMillis());

        DetalleFactura detalle = calcularFactura.crearDetalleFactura(contrato);
        detalle.setFactura(factura);

        factura.setDetalles(Set.of(detalle));
        factura.setMontoTotalFinal(detalle.getMontoTotalBruto());

        cuenta.setDeudaPendiente((cuenta.getDeudaPendiente() != null ? cuenta.getDeudaPendiente() : 0.0) + factura.getMontoTotalFinal());

        cuentaClienteRepository.save(cuenta);
        return facturaRepository.save(factura);
    }

    @Transactional
    public NotaCredito anularFactura(Long facturaId, String motivo) {
        Factura factura = facturaRepository.findById(Objects.requireNonNull(facturaId))
            .orElseThrow(() -> new RuntimeException("Factura no encontrada."));

        NotaCredito notaCredito = new NotaCredito();
        notaCredito.setFacturaAnulada(factura);
        notaCredito.setCuentaCliente(factura.getCuentaCliente());
        notaCredito.setMontoTotalCredito(factura.getMontoTotalFinal());
        notaCredito.setMotivo(motivo);
        notaCredito.setFechaEmision(LocalDate.now());
        notaCredito.setNumero("NC-" + System.currentTimeMillis());

        factura.setEstado(EstadoFactura.ANULADA_POR_NC);
        factura.setMotivoAnulacion(motivo);

        CuentaCliente cuenta = factura.getCuentaCliente();
        cuenta.setDeudaPendiente(cuenta.getDeudaPendiente() - factura.getMontoTotalFinal());

        facturaRepository.save(factura);
        cuentaClienteRepository.save(cuenta);
        
        return notaCreditoRepository.save(notaCredito);
    }
}