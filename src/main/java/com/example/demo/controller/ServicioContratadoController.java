package com.example.demo.controller;

import com.example.demo.model.CuentaCliente;
import com.example.demo.model.ServicioContratado;
import com.example.demo.service.IServicioContratadoService;
import com.example.demo.service.ICuentaClienteService;
import com.example.demo.service.IServicioService;
import com.example.demo.service.IPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/servicios-contratados")
public class ServicioContratadoController {

    @Autowired
    private IServicioContratadoService servicioContratadoService;
    
    @Autowired
    private ICuentaClienteService cuentaClienteService;
    
    @Autowired
    private IServicioService servicioService;
    
    @Autowired
    private IPlanService planService;

    // Listar todos los servicios contratados (opcional, para admin)
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("serviciosContratados", servicioContratadoService.listarTodos());
        return "servicios-contratados/lista";
    }

    // Listar servicios contratados de una cuenta específica
    @GetMapping("/cuenta/{cuentaId}")
    public String listarPorCuenta(@PathVariable Long cuentaId, Model model, RedirectAttributes redirect) {
        try {
            CuentaCliente cuenta = cuentaClienteService.buscarPorId(cuentaId)
                    .orElseThrow(() -> new RuntimeException("Cuenta no encontrada con ID: " + cuentaId));
            model.addAttribute("cuenta", cuenta);
            model.addAttribute("serviciosContratados", servicioContratadoService.listarPorCuenta(cuentaId));
            return "servicios-contratados/lista-por-cuenta";
        } catch (RuntimeException e) {
            redirect.addFlashAttribute("error", e.getMessage());
            return "redirect:/clientes";
        }
    }

    // Formulario para contratar un nuevo servicio a una cuenta
    @GetMapping("/cuenta/{cuentaId}/nuevo")
    public String mostrarFormularioNuevo(@PathVariable Long cuentaId, Model model, RedirectAttributes redirect) {
        try {
            CuentaCliente cuenta = cuentaClienteService.buscarPorId(cuentaId)
                    .orElseThrow(() -> new RuntimeException("Cuenta no encontrada con ID: " + cuentaId));
            model.addAttribute("cuenta", cuenta);
            model.addAttribute("servicios", servicioService.listarTodos());
            model.addAttribute("planes", planService.listarTodos());
            model.addAttribute("servicioContratado", new ServicioContratado());
            return "servicios-contratados/form";
        } catch (RuntimeException e) {
            redirect.addFlashAttribute("error", e.getMessage());
            return "redirect:/clientes";
        }
    }

    // Guardar servicio contratado (puede ser servicio individual o plan)
    @PostMapping("/cuenta/{cuentaId}/guardar")
    public String guardar(@PathVariable Long cuentaId,
                         @RequestParam(required = false) Long servicioId,
                         @RequestParam(required = false) Long planId,
                         @RequestParam(required = false) Double precioPersonalizado,
                         @RequestParam(required = false) Double descuento,
                         RedirectAttributes redirect) {
        try {
            // Validar que al menos uno esté presente
            if (servicioId == null && planId == null) {
                redirect.addFlashAttribute("error", "Debe seleccionar un servicio o un plan");
                return "redirect:/servicios-contratados/cuenta/" + cuentaId + "/nuevo";
            }
            
            // Contratar servicio o plan según corresponda
            if (servicioId != null) {
                servicioContratadoService.contratarServicio(cuentaId, servicioId, precioPersonalizado, descuento);
                redirect.addFlashAttribute("success", "Servicio contratado exitosamente");
            } else {
                servicioContratadoService.contratarPlan(cuentaId, planId, precioPersonalizado, descuento);
                redirect.addFlashAttribute("success", "Plan contratado exitosamente");
            }
            
            return "redirect:/servicios-contratados/cuenta/" + cuentaId;
        } catch (RuntimeException e) {
            redirect.addFlashAttribute("error", e.getMessage());
            return "redirect:/servicios-contratados/cuenta/" + cuentaId + "/nuevo";
        }
    }

    // Formulario para editar servicio contratado (cambiar precio/descuento)
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model, RedirectAttributes redirect) {
        try {
            ServicioContratado servicioContratado = servicioContratadoService.buscarPorId(id);
            model.addAttribute("servicioContratado", servicioContratado);
            return "servicios-contratados/form-editar";
        } catch (RuntimeException e) {
            redirect.addFlashAttribute("error", e.getMessage());
            return "redirect:/servicios-contratados";
        }
    }

    // Modificar servicio contratado
    @PostMapping("/modificar/{id}")
    public String modificar(@PathVariable Long id,
                           @RequestParam(required = false) Double precioPersonalizado,
                           @RequestParam(required = false) Double descuento,
                           RedirectAttributes redirect) {
        try {
            ServicioContratado servicioContratado = servicioContratadoService.modificar(id, precioPersonalizado, descuento);
            redirect.addFlashAttribute("success", "Servicio contratado modificado exitosamente");
            return "redirect:/servicios-contratados/cuenta/" + servicioContratado.getCuentaCliente().getId();
        } catch (RuntimeException e) {
            redirect.addFlashAttribute("error", e.getMessage());
            return "redirect:/servicios-contratados/editar/" + id;
        }
    }

    // Cancelar/eliminar servicio contratado
    @GetMapping("/cancelar/{id}")
    public String cancelar(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            ServicioContratado servicioContratado = servicioContratadoService.buscarPorId(id);
            Long cuentaId = servicioContratado.getCuentaCliente().getId();
            servicioContratadoService.cancelar(id);
            redirect.addFlashAttribute("success", "Servicio contratado cancelado exitosamente");
            return "redirect:/servicios-contratados/cuenta/" + cuentaId;
        } catch (RuntimeException e) {
            redirect.addFlashAttribute("error", e.getMessage());
            return "redirect:/servicios-contratados";
        }
    }
}
