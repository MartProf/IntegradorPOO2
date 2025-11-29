package com.example.demo.service;

import com.example.demo.model.Cliente;
import java.util.List;
import java.util.Optional;

public interface IClienteService {
    
    /**
     * Obtiene todos los clientes
     * @return Lista de todos los clientes
     */
    List<Cliente> listarTodos();
    
    /**
     * Busca un cliente por ID
     * @param id El ID del cliente
     * @return Optional con el cliente si existe
     */
    Optional<Cliente> buscarPorId(Long id);
    
    /**
     * Busca un cliente por CUIT/DNI
     * @param cuitDni El CUIT o DNI del cliente
     * @return Optional con el cliente si existe
     */
    Optional<Cliente> buscarPorCuitDni(String cuitDni);
    
    /**
     * Guarda un nuevo cliente o actualiza uno existente
     * @param cliente El cliente a guardar
     * @return El cliente guardado
     * @throws IllegalArgumentException Si los datos son inválidos
     */
    Cliente guardar(Cliente cliente);
    
    /**
     * Elimina un cliente por ID
     * @param id El ID del cliente a eliminar
     * @throws IllegalArgumentException Si el cliente no existe o tiene dependencias
     */
    void eliminar(Long id);
    
    /**
     * Verifica si existe un cliente con el CUIT/DNI dado
     * @param cuitDni El CUIT o DNI a verificar
     * @return true si existe, false en caso contrario
     */
    boolean existeCuitDni(String cuitDni);
    
    /**
     * Verifica si existe un cliente con el CUIT/DNI dado, excluyendo un ID específico
     * @param cuitDni El CUIT o DNI a verificar
     * @param idExcluir El ID a excluir de la búsqueda
     * @return true si existe otro cliente con ese CUIT/DNI, false en caso contrario
     */
    boolean existeCuitDniExcluyendoId(String cuitDni, Long idExcluir);
}
