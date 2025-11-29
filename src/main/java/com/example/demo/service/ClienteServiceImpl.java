package com.example.demo.service;

import com.example.demo.model.Cliente;
import com.example.demo.model.CuentaCliente;
import com.example.demo.model.EstadoCuenta;
import com.example.demo.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ClienteServiceImpl implements IClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Cliente> buscarPorId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }
        return clienteRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Cliente> buscarPorCuitDni(String cuitDni) {
        return clienteRepository.findByCuitDni(cuitDni);
    }

    @Override
    public Cliente guardar(Cliente cliente) {
        // Validaciones
        validarCliente(cliente);
        
        // Verificar si el CUIT/DNI ya existe (excepto si es el mismo cliente)
        if (cliente.getId() == null) {
            // Cliente nuevo
            if (clienteRepository.existsByCuitDni(cliente.getCuitDni())) {
                throw new IllegalArgumentException("Ya existe un cliente con el CUIT/DNI: " + cliente.getCuitDni());
            }
        } else {
            // Cliente existente - verificar que no haya otro con el mismo CUIT/DNI
            Optional<Cliente> clienteExistente = clienteRepository.findByCuitDni(cliente.getCuitDni());
            if (clienteExistente.isPresent() && !clienteExistente.get().getId().equals(cliente.getId())) {
                throw new IllegalArgumentException("Ya existe otro cliente con el CUIT/DNI: " + cliente.getCuitDni());
            }
        }
        
        // Si es un cliente nuevo, crear su cuenta asociada
        if (cliente.getId() == null && cliente.getCuentaCliente() == null) {
            CuentaCliente cuenta = new CuentaCliente();
            cuenta.setCliente(cliente);
            cuenta.setEstado(EstadoCuenta.ACTIVA);
            cuenta.setDeudaPendiente(0.0);
            cuenta.setSaldoAFavor(0.0);
            cliente.setCuentaCliente(cuenta);
        }
        
        return clienteRepository.save(cliente);
    }

    @Override
    public void eliminar(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado con ID: " + id));
        
        // Verificar si tiene cuenta con servicios contratados
        if (cliente.getCuentaCliente() != null && 
            cliente.getCuentaCliente().getServiciosContratados() != null &&
            !cliente.getCuentaCliente().getServiciosContratados().isEmpty()) {
            throw new IllegalArgumentException("No se puede eliminar el cliente porque tiene servicios contratados");
        }
        
        // Verificar si tiene deuda pendiente
        if (cliente.getCuentaCliente() != null && 
            cliente.getCuentaCliente().getDeudaPendiente() > 0) {
            throw new IllegalArgumentException("No se puede eliminar el cliente porque tiene deuda pendiente");
        }
        
        clienteRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeCuitDni(String cuitDni) {
        return clienteRepository.existsByCuitDni(cuitDni);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeCuitDniExcluyendoId(String cuitDni, Long idExcluir) {
        Optional<Cliente> cliente = clienteRepository.findByCuitDni(cuitDni);
        return cliente.isPresent() && !cliente.get().getId().equals(idExcluir);
    }

    private void validarCliente(Cliente cliente) {
        if (cliente.getRazonSocial() == null || cliente.getRazonSocial().trim().isEmpty()) {
            throw new IllegalArgumentException("La razón social no puede estar vacía");
        }
        
        if (cliente.getCuitDni() == null || cliente.getCuitDni().trim().isEmpty()) {
            throw new IllegalArgumentException("El CUIT/DNI no puede estar vacío");
        }
        
        if (cliente.getCondicionIVA() == null) {
            throw new IllegalArgumentException("Debe especificar una condición fiscal");
        }
        
        // Validar formato CUIT/DNI (básico)
        String cuitDni = cliente.getCuitDni().replaceAll("[^0-9]", "");
        if (cuitDni.length() < 7 || cuitDni.length() > 11) {
            throw new IllegalArgumentException("El CUIT/DNI debe tener entre 7 y 11 dígitos");
        }
    }
}
