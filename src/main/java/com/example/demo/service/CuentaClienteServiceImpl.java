package com.example.demo.service;

import com.example.demo.model.CuentaCliente;
import com.example.demo.model.EstadoCuenta;
import com.example.demo.repository.CuentaClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CuentaClienteServiceImpl implements ICuentaClienteService {

    @Autowired
    private CuentaClienteRepository cuentaClienteRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CuentaCliente> listarTodas() {
        return cuentaClienteRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CuentaCliente> buscarPorId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID de la cuenta no puede ser nulo");
        }
        return cuentaClienteRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CuentaCliente> buscarPorClienteId(Long clienteId) {
        return cuentaClienteRepository.findByClienteId(clienteId);
    }

    @Override
    public CuentaCliente guardar(CuentaCliente cuenta) {
        if (cuenta.getEstado() == null) {
            cuenta.setEstado(EstadoCuenta.ACTIVA);
        }
        if (cuenta.getDeudaPendiente() == null) {
            cuenta.setDeudaPendiente(0.0);
        }
        if (cuenta.getSaldoAFavor() == null) {
            cuenta.setSaldoAFavor(0.0);
        }
        return cuentaClienteRepository.save(cuenta);
    }

    @Override
    public void eliminar(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID de la cuenta no puede ser nulo");
        }
        CuentaCliente cuenta = cuentaClienteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cuenta no encontrada con ID: " + id));
        
        if (cuenta.getDeudaPendiente() > 0) {
            throw new IllegalArgumentException("No se puede eliminar una cuenta con deuda pendiente");
        }
        
        if (cuenta.getServiciosContratados() != null && !cuenta.getServiciosContratados().isEmpty()) {
            throw new IllegalArgumentException("No se puede eliminar una cuenta con servicios contratados");
        }
        
        cuentaClienteRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CuentaCliente> buscarPorEstado(EstadoCuenta estado) {
        return cuentaClienteRepository.findByEstado(estado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CuentaCliente> buscarConDeuda() {
        return cuentaClienteRepository.findByDeudaPendienteGreaterThan(0.0);
    }

    @Override
    public void actualizarDeuda(Long cuentaId, Double monto) {
        if (cuentaId == null) {
            throw new IllegalArgumentException("El ID de la cuenta no puede ser nulo");
        }
        CuentaCliente cuenta = cuentaClienteRepository.findById(cuentaId)
                .orElseThrow(() -> new IllegalArgumentException("Cuenta no encontrada con ID: " + cuentaId));
        
        cuenta.setDeudaPendiente(cuenta.getDeudaPendiente() + monto);
        cuentaClienteRepository.save(cuenta);
    }

    @Override
    public void actualizarSaldoAFavor(Long cuentaId, Double monto) {
        if (cuentaId == null) {
            throw new IllegalArgumentException("El ID de la cuenta no puede ser nulo");
        }
        CuentaCliente cuenta = cuentaClienteRepository.findById(cuentaId)
                .orElseThrow(() -> new IllegalArgumentException("Cuenta no encontrada con ID: " + cuentaId));
        
        cuenta.setSaldoAFavor(cuenta.getSaldoAFavor() + monto);
        cuentaClienteRepository.save(cuenta);
    }
}
