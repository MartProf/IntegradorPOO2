package com.example.demo.service;

import com.example.demo.model.CuentaCliente;
import com.example.demo.model.Plan;
import com.example.demo.model.Servicio;
import com.example.demo.model.ServicioContratado;
import com.example.demo.model.enums.EstadoCuenta;
import com.example.demo.repository.CuentaClienteRepository;
import com.example.demo.repository.PlanRepository;
import com.example.demo.repository.ServicioRepository;
import com.example.demo.repository.ServicioContratadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class ServicioContratadoServiceImpl implements IServicioContratadoService {

    @Autowired
    private ServicioContratadoRepository servicioContratadoRepository;
    
    @Autowired
    private CuentaClienteRepository cuentaClienteRepository;
    
    @Autowired
    private ServicioRepository servicioRepository;
    
    @Autowired
    private PlanRepository planRepository;

    @Override
    public List<ServicioContratado> listarTodos() {
        return servicioContratadoRepository.findAll();
    }

    @Override
    public ServicioContratado buscarPorId(Long id) {
        return servicioContratadoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Servicio contratado no encontrado con ID: " + id));
    }

    @Override
    public List<ServicioContratado> listarPorCuenta(Long cuentaClienteId) {
        return servicioContratadoRepository.findByCuentaClienteId(cuentaClienteId);
    }

    @Override
    public ServicioContratado contratarServicio(Long cuentaClienteId, Long servicioId, 
                                                Double precioPersonalizado, Double descuento) {
        // Validar que la cuenta existe y está activa
        CuentaCliente cuenta = cuentaClienteRepository.findById(cuentaClienteId)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada con ID: " + cuentaClienteId));
        
        if (cuenta.getEstado() != EstadoCuenta.ACTIVA) {
            throw new RuntimeException("La cuenta debe estar ACTIVA para contratar servicios");
        }
        
        // Validar que el servicio existe
        Servicio servicio = servicioRepository.findById(servicioId)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado con ID: " + servicioId));
        
        // Validar que no esté duplicado (opcional, según tu lógica de negocio)
        if (servicioContratadoRepository.existsByCuentaClienteIdAndServicioId(cuentaClienteId, servicioId)) {
            throw new RuntimeException("La cuenta ya tiene contratado este servicio");
        }
        
        // Crear el servicio contratado
        ServicioContratado servicioContratado = new ServicioContratado();
        servicioContratado.setCuentaCliente(cuenta);
        servicioContratado.setServicio(servicio);
        servicioContratado.setPrecioPersonalizado(precioPersonalizado);
        servicioContratado.setMontoDescuento(descuento != null ? descuento : 0.0);
        servicioContratado.setFechaInicio(LocalDate.now());
        
        return servicioContratadoRepository.save(servicioContratado);
    }

    @Override
    public ServicioContratado contratarPlan(Long cuentaClienteId, Long planId, 
                                           Double precioPersonalizado, Double descuento) {
        // Validar que la cuenta existe y está activa
        CuentaCliente cuenta = cuentaClienteRepository.findById(cuentaClienteId)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada con ID: " + cuentaClienteId));
        
        if (cuenta.getEstado() != EstadoCuenta.ACTIVA) {
            throw new RuntimeException("La cuenta debe estar ACTIVA para contratar planes");
        }
        
        // Validar que el plan existe
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Plan no encontrado con ID: " + planId));
        
        // Crear el servicio contratado asociado al plan
        // NOTA: En este caso, servicio puede ser null o podríamos usar un servicio genérico
        // Según tu modelo, un plan puede tener múltiples servicios o ser independiente
        ServicioContratado servicioContratado = new ServicioContratado();
        servicioContratado.setCuentaCliente(cuenta);
        servicioContratado.setPlan(plan);
        // Si el plan tiene un servicio asociado, lo asignamos, sino dejamos null
        // servicioContratado.setServicio(plan.getServicio()); // Si existiera esta relación
        servicioContratado.setPrecioPersonalizado(precioPersonalizado);
        servicioContratado.setMontoDescuento(descuento != null ? descuento : 0.0);
        servicioContratado.setFechaInicio(LocalDate.now());
        
        return servicioContratadoRepository.save(servicioContratado);
    }

    @Override
    public ServicioContratado modificar(Long id, Double precioPersonalizado, Double descuento) {
        ServicioContratado servicioContratado = buscarPorId(id);
        
        // Modificar solo los campos permitidos
        if (precioPersonalizado != null) {
            servicioContratado.setPrecioPersonalizado(precioPersonalizado);
        }
        if (descuento != null) {
            servicioContratado.setMontoDescuento(descuento);
        }
        
        return servicioContratadoRepository.save(servicioContratado);
    }

    @Override
    public void cancelar(Long id) {
        ServicioContratado servicioContratado = buscarPorId(id);
        
        // Validación: No se puede cancelar si tiene facturas pendientes (implementar después)
        // Por ahora, simplemente eliminamos
        servicioContratadoRepository.delete(servicioContratado);
    }

    @Override
    public boolean cuentaTieneServicio(Long cuentaClienteId, Long servicioId) {
        return servicioContratadoRepository.existsByCuentaClienteIdAndServicioId(cuentaClienteId, servicioId);
    }
}
