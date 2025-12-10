package com.example.demo.controller;

import com.example.demo.dto.ResultadoFacturacionMasiva;
import com.example.demo.model.Cliente;
import com.example.demo.model.CuentaCliente;
import com.example.demo.model.Factura;
import com.example.demo.model.enums.CondicionFiscal;
import com.example.demo.model.enums.EstadoFactura;
import com.example.demo.model.enums.TipoFactura;
import com.example.demo.service.ICuentaClienteService;
import com.example.demo.service.IFacturaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

/**
 * TESTS DE CONTROLLER (Endpoints HTTP)
 * 
 * ¿QUÉ SON LOS TESTS DE CONTROLLER?
 * Prueban los ENDPOINTS (las URLs) de tu aplicación web.
 * Simulan peticiones HTTP como si fueran del navegador.
 * 
 * ¿DIFERENCIA CON TESTS UNITARIOS?
 * - Tests Unitarios: Prueban UN método aislado
 * - Tests Controller: Prueban todo el flujo HTTP (petición → controller → vista)
 * 
 * ¿NECESITO LEVANTAR EL SERVIDOR?
 * NO. MockMvc simula el servidor sin levantarlo realmente.
 * 
 * EJEMPLO DE FLUJO:
 * 1. Test simula: GET /facturas
 * 2. MockMvc llama al FacturaController
 * 3. Controller procesa la petición
 * 4. Test verifica: ¿status 200? ¿vista correcta? ¿modelo tiene datos?
 * 
 * @WebMvcTest(controllers = FacturaController.class)
 *   → Carga SOLO FacturaController (no toda la aplicación)
 *   → NO carga base de datos ni otros controllers
 *   → Tests son RÁPIDOS
 * 
 * @AutoConfigureMockMvc(addFilters = false)
 *   → Desactiva Spring Security en los tests
 *   → Sin esto, todas las peticiones serían bloqueadas (401 Unauthorized)
 *   → Simplifica los tests (no necesitas autenticarte)
 */
@WebMvcTest(controllers = FacturaController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("Tests de FacturaController")
class FacturaControllerTest {

    // SECCIÓN 1: MockMvc - El simulador de peticiones HTTP
    
    /**
     * @Autowired MockMvc
     * 
     * ¿QUÉ ES MockMvc?
     * Es una herramienta que simula un NAVEGADOR WEB.
     * 
     * ¿Qué puede hacer?
     * - Simular clicks en URLs: mockMvc.perform(get("/facturas"))
     * - Simular envío de formularios: mockMvc.perform(post("/facturas/generar"))
     * - Verificar respuestas: ¿status 200? ¿vista correcta?
     * 
     * Ejemplo real:
     *   mockMvc.perform(get("/facturas"))  ← Como escribir "localhost:8080/facturas"
     *      .andExpect(status().isOk())     ← ¿Respondió con 200 OK?
     *      .andExpect(view().name("facturas/lista"))  ← ¿Mostró la vista correcta?
     * 
     * Spring Boot lo crea automáticamente gracias a @Autowired
     */
    @Autowired
    private MockMvc mockMvc;
    
    // ═══════════════════════════════════════════════════════════════════
    // SECCIÓN 2: Servicios falsos (mocks)
    // ═══════════════════════════════════════════════════════════════════
    
    /**
     * @MockBean → Igual que @Mock pero para Spring Boot
     * 
     * ¿Por qué MockBean y no Mock?
     * Porque estamos en un test de Spring (@WebMvcTest).
     * Spring necesita inyectar estos servicios en el controller.
     * 
     * ¿Qué hace?
     * - Crea versiones FALSAS de los servicios
     * - El FacturaController usa estos servicios falsos
     * - NO ejecutan la lógica real (no acceden a BD)
     * - Devuelven lo que tú les indiques con when()
     */
    @MockBean
    private IFacturaService facturaService;
    
    @MockBean
    private ICuentaClienteService cuentaClienteService;
    
    // Datos de prueba
    private Factura factura;
    private CuentaCliente cuenta;
    private Cliente cliente;
    private List<Factura> listaFacturas;
    
    /**
     * @BeforeEach se ejecuta antes de cada test
     * Preparamos datos comunes para todos los tests
     */
    @BeforeEach
    void setUp() {
        // Crear un cliente completo con todos los datos necesarios para las vistas
        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setRazonSocial("Empresa Test S.A.");
        cliente.setNombre("Juan");
        cliente.setApellido("Pérez");
        cliente.setCuitDni("20-12345678-9");
        cliente.setDomicilio("Av. Siempre Viva 123");
        cliente.setTelefono("123456789");
        cliente.setCondicionIVA(CondicionFiscal.RESPONSABLE_INSCRIPTO);
        
        // Crear una cuenta de prueba vinculada al cliente
        cuenta = new CuentaCliente();
        cuenta.setId(1L);
        cuenta.setCliente(cliente); // ← IMPORTANTE: vincular cliente
        
        // Crear una factura de prueba con TODOS los campos necesarios
        factura = new Factura();
        factura.setId(1L);
        factura.setNumero("0001-00000001");
        factura.setEstado(EstadoFactura.PENDIENTE);
        factura.setTipoFactura(TipoFactura.FACTURA_A); // ← IMPORTANTE: necesario para la vista
        factura.setFechaEmision(LocalDate.now());
        factura.setFechaVencimiento(LocalDate.now().plusDays(10));
        factura.setMontoTotalFinal(6050.0);
        factura.setCuentaCliente(cuenta); // ← IMPORTANTE: necesario para la vista
        
        // Crear lista de facturas
        listaFacturas = new ArrayList<>();
        listaFacturas.add(factura);
    }
    
    // ═══════════════════════════════════════════════════════════════════
    // SECCIÓN 4: TESTS DE ENDPOINTS
    // ═══════════════════════════════════════════════════════════════════
    
    /**
     * ┌───────────────────────────────────────────────────────────────┐
     * │ TEST 1: Listar Facturas (GET /facturas)                       │
     * └───────────────────────────────────────────────────────────────┘
     * 
     * ¿QUÉ ESTAMOS PROBANDO?
     * El endpoint GET /facturas que muestra la lista de facturas.
     * 
     * ¿QUÉ ES UN ENDPOINT?
     * Es una URL de tu aplicación web. Ejemplos:
     *   - GET /facturas → Muestra lista
     *   - GET /facturas/1 → Muestra detalle de factura 1
     *   - POST /facturas/generar → Genera una nueva factura
     * 
     * FLUJO DE ESTE TEST:
     * 1. Simula: Usuario abre "localhost:8080/facturas" en el navegador
     * 2. Controller procesa la petición
     * 3. Verifica: ¿Respondió bien? ¿Mostró la vista correcta?
     */
    @Test
    @DisplayName("GET /facturas - Debe listar todas las facturas")
    void testListarFacturas() throws Exception {
        
        // ═══════════════════════════════════════════════════════════════
        // PASO 1: ARRANGE (Preparar datos)
        // ═══════════════════════════════════════════════════════════════
        
        // ┌─ SIMULAR RESPUESTA DEL SERVICE ────────────────────────────┐
        // El controller llama a facturaService.listarTodas()
        // Simulamos que devuelve nuestra lista de prueba
        when(facturaService.listarTodas()).thenReturn(listaFacturas);
        // └─────────────────────────────────────────────────────────────┘
        
        // ═══════════════════════════════════════════════════════════════
        // PASO 2: ACT (Simular petición HTTP) + ASSERT (Verificar)
        // ═══════════════════════════════════════════════════════════════
        
        // ┌─ SIMULAR PETICIÓN GET ─────────────────────────────────────┐
        mockMvc.perform(get("/facturas"))  
        // Traducción: "Simula que el usuario entra a /facturas"
        // Es como abrir el navegador y escribir la URL
        // └─────────────────────────────────────────────────────────────┘
        
            // ┌─ VERIFICAR STATUS CODE ─────────────────────────────────┐
            .andExpect(status().isOk())
            // isOk() = status 200 (petición exitosa)
            // Si el servidor devuelve 404, 500, etc. → Test FALLA ❌
            // └─────────────────────────────────────────────────────────┘
            
            // ┌─ VERIFICAR VISTA THYMELEAF ─────────────────────────────┐
            .andExpect(view().name("facturas/lista"))
            // Verifica que el controller devolvió "facturas/lista"
            // Esto se corresponde con: src/main/resources/templates/facturas/lista.html
            // └─────────────────────────────────────────────────────────┘
            
            // ┌─ VERIFICAR MODELO (datos para la vista) ───────────────┐
            .andExpect(model().attributeExists("facturas"))
            // El controller debe agregar: model.addAttribute("facturas", ...)
            // Este atributo es el que usa Thymeleaf en la vista
            // └─────────────────────────────────────────────────────────┘
            
            // ┌─ VERIFICAR TAMAÑO DE LA LISTA ─────────────────────────┐
            .andExpect(model().attribute("facturas", hasSize(1)))
            // hasSize(1) = "la lista debe tener 1 elemento"
            // └─────────────────────────────────────────────────────────┘
            
            // ┌─ VERIFICAR TÍTULO ──────────────────────────────────────┐
            .andExpect(model().attribute("titulo", "Listado de Facturas"));
            // Verifica que el atributo "titulo" tenga el valor esperado
            // └─────────────────────────────────────────────────────────┘
        
        // ═══════════════════════════════════════════════════════════════
        // PASO 3: VERIFY (Verificar que se usó el service)
        // ═══════════════════════════════════════════════════════════════
        
        // ┌─ VERIFICAR QUE SE LLAMÓ AL SERVICE ────────────────────────┐
        verify(facturaService, times(1)).listarTodas();
        // times(1) = "exactamente 1 vez"
        // Esto confirma que el controller SÍ llamó al service
        // Si no lo llamó → Test FALLA ❌
        // └─────────────────────────────────────────────────────────────┘
    }
    
    /**
     * TEST 2: Ver detalle de una factura (GET /facturas/{id})
     * 
     * OBJETIVO: Cuando la factura existe, debe mostrar su detalle
     */
    @Test
    @DisplayName("GET /facturas/{id} - Debe mostrar detalle de factura existente")
    void testVerDetalle_FacturaExiste() throws Exception {
        // === ARRANGE ===
        when(facturaService.buscarPorId(1L)).thenReturn(Optional.of(factura));
        
        // === ACT & ASSERT ===
        mockMvc.perform(get("/facturas/1"))
            .andExpect(status().isOk())
            .andExpect(view().name("facturas/detalle"))
            .andExpect(model().attributeExists("factura"))
            .andExpect(model().attribute("factura", factura))
            .andExpect(model().attribute("titulo", containsString("0001-00000001")));
        
        verify(facturaService, times(1)).buscarPorId(1L);
    }
    
    /**
     * TEST 3: Ver detalle de factura inexistente (GET /facturas/{id})
     * 
     * OBJETIVO: Si la factura no existe, debe:
     *   - Redirigir a /facturas
     *   - Mostrar mensaje de error en flash
     */
    @Test
    @DisplayName("GET /facturas/{id} - Debe redirigir si factura no existe")
    void testVerDetalle_FacturaNoExiste() throws Exception {
        // === ARRANGE ===
        // Simular que la factura no existe
        when(facturaService.buscarPorId(999L)).thenReturn(Optional.empty());
        
        // === ACT & ASSERT ===
        mockMvc.perform(get("/facturas/999"))
            .andExpect(status().is3xxRedirection())  // ← Status 3xx (redirección)
            .andExpect(redirectedUrl("/facturas"))   // ← Verifica la URL de redirección
            .andExpect(flash().attributeExists("error"))  // ← Verifica mensaje de error
            .andExpect(flash().attribute("error", "La factura no existe"));
        
        verify(facturaService, times(1)).buscarPorId(999L);
    }
    
    /**
     * TEST 4: Mostrar formulario de generación (GET /facturas/generar)
     * 
     * OBJETIVO: Verificar que carga el formulario con lista de cuentas
     */
    @Test
    @DisplayName("GET /facturas/generar - Debe mostrar formulario de generación")
    void testMostrarFormularioGenerar() throws Exception {
        // === ARRANGE ===
        List<CuentaCliente> cuentas = new ArrayList<>();
        cuentas.add(cuenta);
        when(cuentaClienteService.listarTodas()).thenReturn(cuentas);
        
        // === ACT & ASSERT ===
        mockMvc.perform(get("/facturas/generar"))
            .andExpect(status().isOk())
            .andExpect(view().name("facturas/generar"))
            .andExpect(model().attributeExists("cuentas"))
            .andExpect(model().attribute("cuentas", hasSize(1)))
            .andExpect(model().attribute("titulo", "Generar Factura"));
        
        verify(cuentaClienteService, times(1)).listarTodas();
    }
    
    /**
     * TEST 5: Generar factura individual exitosamente (POST /facturas/generar)
     * 
     * OBJETIVO: Cuando se genera una factura correctamente:
     *   - Debe redirigir al detalle de la factura creada
     *   - Debe mostrar mensaje de éxito
     */
    @Test
    @DisplayName("POST /facturas/generar - Debe generar factura correctamente")
    void testGenerarFactura_Exitoso() throws Exception {
        // === ARRANGE ===
        when(facturaService.generarFacturaIndividual(1L)).thenReturn(factura);
        
        // === ACT & ASSERT ===
        mockMvc.perform(post("/facturas/generar")
                .param("cuentaClienteId", "1"))  // ← Envía parámetro del formulario
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/facturas/1"))  // ← Redirige al detalle
            .andExpect(flash().attributeExists("success"))
            .andExpect(flash().attribute("success", 
                containsString("Factura generada exitosamente")));
        
        verify(facturaService, times(1)).generarFacturaIndividual(1L);
    }
    
    /**
     * TEST 6: Error al generar factura (POST /facturas/generar)
     * 
     * OBJETIVO: Si hay error al generar:
     *   - Debe redirigir de vuelta al formulario
     *   - Debe mostrar mensaje de error
     */
    @Test
    @DisplayName("POST /facturas/generar - Debe manejar error al generar")
    void testGenerarFactura_ConError() throws Exception {
        // === ARRANGE ===
        // Simular que el service lanza una excepción
        when(facturaService.generarFacturaIndividual(1L))
            .thenThrow(new RuntimeException("La cuenta no tiene servicios activos"));
        
        // === ACT & ASSERT ===
        mockMvc.perform(post("/facturas/generar")
                .param("cuentaClienteId", "1"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/facturas/generar"))  // ← Vuelve al formulario
            .andExpect(flash().attributeExists("error"))
            .andExpect(flash().attribute("error", 
                containsString("no tiene servicios activos")));
        
        verify(facturaService, times(1)).generarFacturaIndividual(1L);
    }
    
    /**
     * TEST 7: Mostrar formulario de facturación masiva (GET /facturas/generar-masivas)
     * 
     * OBJETIVO: Verificar que muestra el formulario con mes y año actual
     */
    @Test
    @DisplayName("GET /facturas/generar-masivas - Debe mostrar formulario masivo")
    void testMostrarFormularioMasivo() throws Exception {
        // === ACT & ASSERT ===
        mockMvc.perform(get("/facturas/generar-masivas"))
            .andExpect(status().isOk())
            .andExpect(view().name("facturas/generar-masivas"))
            .andExpect(model().attributeExists("mesActual"))
            .andExpect(model().attributeExists("anioActual"))
            .andExpect(model().attribute("titulo", "Facturación Masiva"));
    }
    
    /**
     * TEST 8: Generar facturas masivas exitosamente (POST /facturas/generar-masivas)
     * 
     * OBJETIVO: Verificar que:
     *   - Procesa facturación masiva correctamente
     *   - Muestra la página de resultados
     *   - Incluye el resultado en el modelo
     */
    @Test
    @DisplayName("POST /facturas/generar-masivas - Debe generar facturas masivas")
    void testGenerarFacturasMasivas_Exitoso() throws Exception {
        // === ARRANGE ===
        // Crear resultado de facturación masiva
        ResultadoFacturacionMasiva resultado = new ResultadoFacturacionMasiva();
        resultado.setTotalCuentasProcesadas(10);
        resultado.setFacturasGeneradas(5);
        resultado.setErrores(0);
        
        when(facturaService.generarFacturasMasivas(12, 2025)).thenReturn(resultado);
        
        // === ACT & ASSERT ===
        mockMvc.perform(post("/facturas/generar-masivas")
                .param("mes", "12")   // ← Diciembre
                .param("anio", "2025"))
            .andExpect(status().isOk())
            .andExpect(view().name("facturas/resultado-masivas"))
            .andExpect(model().attributeExists("resultado"))
            .andExpect(model().attribute("resultado", resultado));
        
        verify(facturaService, times(1)).generarFacturasMasivas(12, 2025);
    }
    
    /**
     * TEST 9: Error en facturación masiva (POST /facturas/generar-masivas)
     * 
     * OBJETIVO: Si hay error, debe volver al formulario con mensaje
     */
    @Test
    @DisplayName("POST /facturas/generar-masivas - Debe manejar error")
    void testGenerarFacturasMasivas_ConError() throws Exception {
        // === ARRANGE ===
        when(facturaService.generarFacturasMasivas(12, 2025))
            .thenThrow(new RuntimeException("No hay servicios para facturar"));
        
        // === ACT & ASSERT ===
        mockMvc.perform(post("/facturas/generar-masivas")
                .param("mes", "12")
                .param("anio", "2025"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/facturas"))  // ← Corregido: redirige a /facturas
            .andExpect(flash().attributeExists("error"));
        
        verify(facturaService, times(1)).generarFacturasMasivas(12, 2025);
    }
    
    /**
     * TEST 10: Listar facturas por estado (GET /facturas/estado/{estado})
     * 
     * OBJETIVO: Filtrar facturas por PENDIENTE, PAGADA, etc.
     */
    @Test
    @DisplayName("GET /facturas/estado/PENDIENTE - Debe filtrar por estado")
    void testListarPorEstado() throws Exception {
        // === ARRANGE ===
        when(facturaService.listarPorEstado(EstadoFactura.PENDIENTE))
            .thenReturn(listaFacturas);
        
        // === ACT & ASSERT ===
        mockMvc.perform(get("/facturas/estado/PENDIENTE"))
            .andExpect(status().isOk())
            .andExpect(view().name("facturas/lista"))
            .andExpect(model().attributeExists("facturas"))
            .andExpect(model().attribute("titulo", containsString("PENDIENTE")));
        
        verify(facturaService, times(1)).listarPorEstado(EstadoFactura.PENDIENTE);
    }
    
    /**
     * TEST 11: Listar facturas vencidas (GET /facturas/vencidas)
     * 
     * OBJETIVO: Mostrar solo facturas con fecha vencimiento < hoy
     */
    @Test
    @DisplayName("GET /facturas/vencidas - Debe listar facturas vencidas")
    void testListarVencidas() throws Exception {
        // === ARRANGE ===
        when(facturaService.listarFacturasVencidas()).thenReturn(listaFacturas);
        
        // === ACT & ASSERT ===
        mockMvc.perform(get("/facturas/vencidas"))
            .andExpect(status().isOk())
            .andExpect(view().name("facturas/lista"))
            .andExpect(model().attribute("titulo", "Facturas Vencidas"));
        
        verify(facturaService, times(1)).listarFacturasVencidas();
    }
    
    /**
     * TEST 12: Listar facturas por cuenta (GET /facturas/cuenta/{cuentaId})
     * 
     * OBJETIVO: Filtrar facturas de un cliente específico
     */
    @Test
    @DisplayName("GET /facturas/cuenta/{id} - Debe filtrar por cuenta")
    void testListarPorCuenta() throws Exception {
        // === ARRANGE ===
        when(facturaService.listarPorCuentaCliente(1L)).thenReturn(listaFacturas);
        
        // === ACT & ASSERT ===
        mockMvc.perform(get("/facturas/cuenta/1"))
            .andExpect(status().isOk())
            .andExpect(view().name("facturas/lista"))
            .andExpect(model().attribute("titulo", containsString("Cuenta #1")));
        
        verify(facturaService, times(1)).listarPorCuentaCliente(1L);
    }
    
    /**
     * TEST 13: Marcar factura como pagada (POST /facturas/{id}/pagar)
     * 
     * OBJETIVO: Actualizar estado a PAGADA y redirigir
     */
    @Test
    @DisplayName("POST /facturas/{id}/pagar - Debe marcar como pagada")
    void testMarcarComoPagada() throws Exception {
        // === ARRANGE ===
        factura.setEstado(EstadoFactura.PAGADA);
        when(facturaService.marcarComoPagada(1L)).thenReturn(factura);
        
        // === ACT & ASSERT ===
        mockMvc.perform(post("/facturas/1/pagar"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/facturas/1"))
            .andExpect(flash().attributeExists("success"))
            .andExpect(flash().attribute("success", 
                containsString("marcada como pagada")));
        
        verify(facturaService, times(1)).marcarComoPagada(1L);
    }
    
    /**
     * TEST 14: Anular factura (POST /facturas/{id}/anular)
     * 
     * OBJETIVO: Cambiar estado a ANULADA
     */
    @Test
    @DisplayName("POST /facturas/{id}/anular - Debe anular factura")
    void testAnularFactura() throws Exception {
        // === ARRANGE ===
        factura.setEstado(EstadoFactura.ANULADA);
        when(facturaService.marcarComoAnulada(1L)).thenReturn(factura);
        
        // === ACT & ASSERT ===
        mockMvc.perform(post("/facturas/1/anular"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/facturas/1"))
            .andExpect(flash().attributeExists("success"))
            .andExpect(flash().attribute("success", containsString("anulada")));
        
        verify(facturaService, times(1)).marcarComoAnulada(1L);
    }
}
