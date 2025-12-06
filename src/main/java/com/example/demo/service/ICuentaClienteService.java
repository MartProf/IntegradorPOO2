package com.example.demo.service;

import com.example.demo.model.CuentaCliente;
import com.example.demo.model.enums.EstadoCuenta;

import java.util.List;
import java.util.Optional;

public interface ICuentaClienteService {
    
    /**
     * Obtiene todas las cuentas
     * @return Lista de todas las cuentas
     */
    List<CuentaCliente> listarTodas();
    
    /**
     * Busca una cuenta por ID
     * @param id El ID de la cuenta
     * @return Optional con la cuenta si existe
     */
    Optional<CuentaCliente> buscarPorId(Long id);
    
    /**
     * Busca una cuenta por ID de cliente
     * @param clienteId El ID del cliente
     * @return Optional con la cuenta si existe
     */
    Optional<CuentaCliente> buscarPorClienteId(Long clienteId);
    
    /**
     * Guarda una nueva cuenta o actualiza una existente
     * @param cuenta La cuenta a guardar
     * @return La cuenta guardada
     */
    CuentaCliente guardar(CuentaCliente cuenta);
    
    /**
     * Elimina una cuenta por ID
     * @param id El ID de la cuenta a eliminar
     */
    void eliminar(Long id);
    
    /**
     * Busca cuentas por estado
     * @param estado El estado de la cuenta
     * @return Lista de cuentas con ese estado
     */
    List<CuentaCliente> buscarPorEstado(EstadoCuenta estado);
    
    /**
     * Busca cuentas con deuda pendiente
     * @return Lista de cuentas con deuda mayor a cero
     */
    List<CuentaCliente> buscarConDeuda();
    
    /**
     * Actualiza la deuda de una cuenta
     * @param cuentaId El ID de la cuenta
     * @param monto El monto a sumar o restar de la deuda
     */
    void actualizarDeuda(Long cuentaId, Double monto);
    
    /**
     * Actualiza el saldo a favor de una cuenta
     * @param cuentaId El ID de la cuenta
     * @param monto El monto a sumar al saldo a favor
     */
    void actualizarSaldoAFavor(Long cuentaId, Double monto);
}
