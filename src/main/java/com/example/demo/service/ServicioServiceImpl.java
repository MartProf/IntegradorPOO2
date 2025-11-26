package com.example.demo.service;

import com.example.demo.model.Servicio;
import com.example.demo.repository.ServicioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ServicioServiceImpl implements IServicioService {

    @Autowired
    private ServicioRepository servicioRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Servicio> listarTodos() {
        return servicioRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Servicio> buscarPorId(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return servicioRepository.findById(id);
    }

    @Override
    @Transactional
    public Servicio guardar(Servicio servicio) {
        // Validaciones
        if (servicio.getNombre() == null || servicio.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del servicio es obligatorio");
        }

        if (servicio.getPrecioBase() == null || servicio.getPrecioBase() <= 0) {
            throw new IllegalArgumentException("El precio base debe ser mayor a 0");
        }

        if (servicio.getAlicuotaIVA() == null || servicio.getAlicuotaIVA() < 0) {
            throw new IllegalArgumentException("La alícuota de IVA no puede ser negativa");
        }

        if (servicio.getAlicuotaIVA() > 100) {
            throw new IllegalArgumentException("La alícuota de IVA no puede ser mayor a 100%");
        }

        // Verificar duplicados (solo si es un servicio nuevo o cambió el nombre)
        if (servicio.getId() == null) {
            if (servicioRepository.existsByNombre(servicio.getNombre())) {
                throw new IllegalArgumentException("Ya existe un servicio con ese nombre");
            }
        } else {
            Long servicioId = servicio.getId();
            if (servicioId != null) {
                String nombreExistente = servicioRepository.findById(servicioId)
                        .map(Servicio::getNombre)
                        .orElse(null);
                if (!servicio.getNombre().equals(nombreExistente)) {
                    if (servicioRepository.existsByNombre(servicio.getNombre())) {
                        throw new IllegalArgumentException("Ya existe un servicio con ese nombre");
                    }
                }
            }
        }

        return servicioRepository.save(servicio);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }
        if (!servicioRepository.existsById(id)) {
            throw new IllegalArgumentException("No existe un servicio con el ID especificado");
        }
        servicioRepository.deleteById(id);
    }
}
