package com.example.demo.servicio;

import com.example.demo.modelo.DetalleFactura;
import com.example.demo.modelo.ServicioContratado;
import org.springframework.stereotype.Service;

@Service
public class CalcularFactura {

    public DetalleFactura crearDetalleFactura(ServicioContratado servicioContratado) {
        DetalleFactura detalle = new DetalleFactura();
        detalle.setServicioContratado(servicioContratado);
        
        // Usar métodos básicos si los métodos personalizados no funcionan
        detalle.setDescripcion(generarDescripcionBasica(servicioContratado));
        detalle.setCantidad(1);
        detalle.setPrecioUnitario(calcularPrecioBasico(servicioContratado));
        detalle.setTasaIVA(servicioContratado.getServicio().getTasaIVA());
        
        detalle.calcularTotales();
        
        return detalle;
    }
    
    private String generarDescripcionBasica(ServicioContratado servicio) {
        return servicio.getServicio().getNombre() + 
               (servicio.getPlan() != null ? " - " + servicio.getPlan().getNombre() : "");
    }
    
    private Double calcularPrecioBasico(ServicioContratado servicio) {
        return servicio.getPrecioPersonalizado() != null ? 
               servicio.getPrecioPersonalizado() : 
               servicio.getServicio().getPrecioMensual();
    }
}