package com.example.demo.service;

import com.example.demo.model.*;
import com.example.demo.model.enums.CondicionFiscal;
import com.example.demo.model.enums.EstadoCuenta;
import com.example.demo.model.enums.EstadoFactura;
import com.example.demo.model.enums.TipoFactura;
import com.example.demo.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * TESTS UNITARIOS PARA FacturaServiceImpl
 * 
 * Son pruebas que verifican que un método individual funciona correctamente.
 *
 * 
 * Una vez escritos, los ejecutas con: mvn test
 * Maven corre todos los tests y te dice cuáles pasaron (✅) o fallaron (❌)
 * 
 * ¿POR QUÉ USAR MOCKS?
 * Porque NO queremos acceder a la base de datos real en los tests:
 *   - Los tests serían lentos (conectar a BD toma tiempo)
 *   - Podrían modificar datos reales
 *   - Los mocks son objetos "falsos" que simulan la BD
 * 
 * @ExtendWith(MockitoExtension.class)
 *   → Habilita Mockito, la librería que crea objetos falsos (mocks)
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Tests de FacturaService")
class FacturaServiceTest {

    // ═══════════════════════════════════════════════════════════════════
    // SECCIÓN 1: DEPENDENCIAS FALSAS (MOCKS)
    // ═══════════════════════════════════════════════════════════════════
    
    /**
     * @Mock → Crea un objeto FALSO del repositorio
     * 
     * ¿Qué hace?
     * - NO accede a PostgreSQL
     * - NO guarda datos reales
     * - Simula las respuestas que le indiques con when()
     * 
     * Ejemplo:
     *   when(facturaRepository.findById(1L)).thenReturn(...)
     *   Significa: "Cuando alguien llame a findById(1L), devuelve esto"
     */
    @Mock
    private FacturaRepository facturaRepository;
    
    @Mock
    private CuentaClienteRepository cuentaClienteRepository;
    
    @Mock
    private ServicioContratadoRepository servicioContratadoRepository;
    
    // ═══════════════════════════════════════════════════════════════════
    // SECCIÓN 2: OBJETO REAL A TESTEAR
    // ═══════════════════════════════════════════════════════════════════
    
    /**
     * @InjectMocks → Crea una instancia REAL de FacturaServiceImpl
     * 
     * ¿Qué hace?
     * - Este SÍ es el código real que queremos probar
     * - Pero le inyecta los repositorios FALSOS (@Mock) de arriba
     * - Así probamos la lógica del service sin tocar la BD
     */
    @InjectMocks
    private FacturaServiceImpl facturaService;
    
    // Variables reutilizables en los tests
    private Cliente cliente;
    private CuentaCliente cuentaCliente;
    private Servicio servicio;
    private ServicioContratado servicioContratado;
    
    // ═══════════════════════════════════════════════════════════════════
    // SECCIÓN 3: DATOS DE PRUEBA
    // ═══════════════════════════════════════════════════════════════════
    
    /**
     * @BeforeEach → Se ejecuta ANTES de cada @Test
     * 
     * ¿Por qué?
     * Porque cada test necesita datos limpios y nuevos.
     * Si un test modifica los datos, el siguiente test no se ve afectado.
     * 
     * Flujo de ejecución:
     * 1. setUp() crea datos frescos
     * 2. Se ejecuta test1()
     * 3. setUp() vuelve a crear datos frescos (descarta los anteriores)
     * 4. Se ejecuta test2()
     * ... y así con cada test
     */
    @BeforeEach
    void setUp() {
        // ───────────────────────────────────────────────────────────────
        // Crear un cliente Responsable Inscripto (IVA discriminado)
        // ───────────────────────────────────────────────────────────────
        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setRazonSocial("Empresa Test S.A.");
        cliente.setCuitDni("20-12345678-9");
        cliente.setCondicionIVA(CondicionFiscal.RESPONSABLE_INSCRIPTO);
        
        // Crear una cuenta de cliente activa
        cuentaCliente = new CuentaCliente();
        cuentaCliente.setId(1L);
        cuentaCliente.setCliente(cliente);
        cuentaCliente.setEstado(EstadoCuenta.ACTIVA);
        cuentaCliente.setDeudaPendiente(0.0);
        cuentaCliente.setSaldoAFavor(0.0);
        
        // Crear un servicio base
        servicio = new Servicio();
        servicio.setId(1L);
        servicio.setNombre("Internet Fibra 100MB");
        servicio.setPrecioBase(5000.0);
        servicio.setAlicuotaIVA(0.21);
        
        // Crear un servicio contratado activo
        servicioContratado = new ServicioContratado();
        servicioContratado.setId(1L);
        servicioContratado.setCuentaCliente(cuentaCliente);
        servicioContratado.setServicio(servicio);
        servicioContratado.setActivo(true);
        servicioContratado.setFechaInicio(LocalDate.now().minusMonths(1)); // Activo desde hace 1 mes
        servicioContratado.setPrecioPersonalizado(null); // Usa precio del servicio
        servicioContratado.setMontoDescuento(0.0);
    }
    
    // ═══════════════════════════════════════════════════════════════════
    // SECCIÓN 4: TESTS INDIVIDUALES
    // ═══════════════════════════════════════════════════════════════════
    
    /**
     * ┌───────────────────────────────────────────────────────────────┐
     * │ TEST 1: Generar Factura Individual - Caso de Éxito            │
     * └───────────────────────────────────────────────────────────────┘
     * 
     * ¿QUÉ ESTAMOS PROBANDO?
     * El método generarFacturaIndividual() del service.
     * 
     * ¿CÓMO FUNCIONA UN TEST?
     * Sigue el patrón AAA (Arrange-Act-Assert):
     * 
     * 1. ARRANGE (Preparar): Configuras los datos y mocks
     * 2. ACT (Actuar): Ejecutas el método a probar
     * 3. ASSERT (Afirmar): Verificas que el resultado es correcto
     * 
     * @Test → Marca este método como un test
     * @DisplayName → Nombre descriptivo que aparece en el reporte
     */
    @Test
    @DisplayName("Debe generar factura individual correctamente")
    void testGenerarFacturaIndividual_Exitoso() {
        
        // ═══════════════════════════════════════════════════════════════
        // PASO 1: ARRANGE (Preparar el escenario)
        // ═══════════════════════════════════════════════════════════════
        
        // ┌─ SIMULAR RESPUESTA DE cuentaClienteRepository ─────────────┐
        // when() = "Cuando llamen a este método..."
        // thenReturn() = "...devuelve esto"
        when(cuentaClienteRepository.findById(1L))
            .thenReturn(Optional.of(cuentaCliente));
        // Traducción: "Cuando el service llame a findById(1L), 
        //              devuelve la cuenta que creamos en setUp()"
        // └─────────────────────────────────────────────────────────────┘
        
        // ┌─ SIMULAR LISTA DE SERVICIOS ACTIVOS ───────────────────────┐
        List<ServicioContratado> serviciosActivos = new ArrayList<>();
        serviciosActivos.add(servicioContratado);
        when(servicioContratadoRepository.findByCuentaClienteIdAndActivo(1L, true))
            .thenReturn(serviciosActivos);
        // Traducción: "Cuando busque servicios activos de cuenta 1,
        //              devuelve una lista con 1 servicio"
        // └─────────────────────────────────────────────────────────────┘
        
        // ┌─ SIMULAR GUARDADO EN BASE DE DATOS ────────────────────────┐
        // any(Factura.class) = "Cualquier objeto de tipo Factura"
        // thenAnswer() = "Ejecuta esta lógica personalizada"
        when(facturaRepository.save(any(Factura.class)))
            .thenAnswer(invocation -> {
                // invocation.getArgument(0) obtiene el primer parámetro
                // (la factura que se está guardando)
                Factura f = invocation.getArgument(0);
                f.setId(1L); // Simulamos que la BD le asignó un ID
                return f;    // Devolvemos la misma factura con ID
            });
        // Traducción: "Cuando guarden una factura, asígnale ID=1 y devuélvela"
        // └─────────────────────────────────────────────────────────────┘
        
        // ═══════════════════════════════════════════════════════════════
        // PASO 2: ACT (Ejecutar el código a probar)
        // ═══════════════════════════════════════════════════════════════
        
        // Llamamos al método REAL del service
        // Este código SÍ se ejecuta (no es mock)
        Factura facturaGenerada = facturaService.generarFacturaIndividual(1L);
        
        // ═══════════════════════════════════════════════════════════════
        // PASO 3: ASSERT (Verificar resultados)
        // ═══════════════════════════════════════════════════════════════
        
        // ┌─ VERIFICAR QUE LA FACTURA EXISTE ──────────────────────────┐
        assertNotNull(facturaGenerada, "La factura no debe ser null");
        // Si facturaGenerada es null → Test FALLA ❌
        // Si facturaGenerada existe → Test continúa ✅
        // └─────────────────────────────────────────────────────────────┘
        
        // ┌─ VERIFICAR ESTADO INICIAL ─────────────────────────────────┐
        assertEquals(EstadoFactura.PENDIENTE, facturaGenerada.getEstado(), 
            "El estado inicial debe ser PENDIENTE");
        // assertEquals(esperado, real, mensaje)
        // Si son iguales → Test continúa ✅
        // Si son diferentes → Test FALLA ❌ y muestra el mensaje
        // └─────────────────────────────────────────────────────────────┘
        
        // ┌─ VERIFICAR TIPO DE FACTURA ────────────────────────────────┐
        assertEquals(TipoFactura.FACTURA_A, facturaGenerada.getTipoFactura(), 
            "Para Responsable Inscripto debe ser Factura A");
        // └─────────────────────────────────────────────────────────────┘
        
        // ┌─ VERIFICAR FECHAS ──────────────────────────────────────────┐
        assertEquals(LocalDate.now(), facturaGenerada.getFechaEmision(), 
            "La fecha de emisión debe ser hoy");
        assertEquals(LocalDate.now().plusDays(10), facturaGenerada.getFechaVencimiento(), 
            "La fecha de vencimiento debe ser 10 días después");
        // └─────────────────────────────────────────────────────────────┘
        
        // ┌─ VERIFICAR QUE TIENE DETALLES ─────────────────────────────┐
        assertFalse(facturaGenerada.getDetalles().isEmpty(), 
            "La factura debe tener al menos un detalle");
        // assertFalse() = "Esto debe ser falso"
        // Si la lista está vacía (isEmpty() = true) → FALLA ❌
        // Si la lista tiene elementos (isEmpty() = false) → PASA ✅
        // └─────────────────────────────────────────────────────────────┘
        
        // ┌─ VERIFICAR MONTO TOTAL ────────────────────────────────────┐
        assertTrue(facturaGenerada.getMontoTotalFinal() > 0, 
            "El monto total debe ser mayor a 0");
        // assertTrue() = "Esto debe ser verdadero"
        // └─────────────────────────────────────────────────────────────┘
        
        // ═══════════════════════════════════════════════════════════════
        // PASO 4: VERIFY (Verificar interacciones con mocks)
        // ═══════════════════════════════════════════════════════════════
        
        // ┌─ VERIFICAR QUE SE LLAMARON LOS MÉTODOS ESPERADOS ─────────┐
        // verify() comprueba que el mock fue usado
        // times(1) = "exactamente 1 vez"
        verify(cuentaClienteRepository, times(1)).findById(1L);
        verify(servicioContratadoRepository, times(1)).findByCuentaClienteIdAndActivo(1L, true);
        verify(facturaRepository, times(1)).save(any(Factura.class));
        // Si algún método NO fue llamado o fue llamado más veces → FALLA ❌
        // └─────────────────────────────────────────────────────────────┘
    }
    
    /**
     * TEST 2: Verificar cálculo de IVA para RESPONSABLE INSCRIPTO (Factura A)
     * 
     * OBJETIVO: En Factura A, el IVA se discrimina (se muestra separado)
     *   Precio base: $5000
     *   IVA (21%): $1050
     *   Total: $6050
     */
    @Test
    @DisplayName("Debe calcular IVA correctamente para Responsable Inscripto (Factura A)")
    void testCalculoIVA_ResponsableInscripto() {
        // === ARRANGE ===
        when(cuentaClienteRepository.findById(1L))
            .thenReturn(Optional.of(cuentaCliente));
        
        List<ServicioContratado> serviciosActivos = new ArrayList<>();
        serviciosActivos.add(servicioContratado);
        when(servicioContratadoRepository.findByCuentaClienteIdAndActivo(1L, true))
            .thenReturn(serviciosActivos);
        
        when(facturaRepository.save(any(Factura.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));
        
        // === ACT ===
        Factura factura = facturaService.generarFacturaIndividual(1L);
        
        // === ASSERT ===
        assertEquals(TipoFactura.FACTURA_A, factura.getTipoFactura(), 
            "Debe ser Factura A");
        
        // Verificar el detalle
        DetalleFactura detalle = factura.getDetalles().get(0);
        assertEquals(5000.0, detalle.getSubtotalNeto(), 0.01, 
            "El subtotal neto debe ser $5000");
        assertEquals(1050.0, detalle.getMontoIVA(), 0.01, 
            "El IVA (21% de $5000) debe ser $1050");
        
        // Verificar el total de la factura
        assertEquals(6050.0, factura.getMontoTotalFinal(), 0.01, 
            "El total debe ser $6050 (5000 + 1050)");
    }
    
    /**
     * TEST 3: Verificar cálculo de IVA para CONSUMIDOR FINAL (Factura B)
     * 
     * OBJETIVO: En Factura B, el IVA está incluido en el precio
     *   Precio con IVA: $5000
     *   Neto: $4132.23 (aprox)
     *   IVA: $867.77 (aprox)
     */
    @Test
    @DisplayName("Debe calcular IVA correctamente para Consumidor Final (Factura B)")
    void testCalculoIVA_ConsumidorFinal() {
        // === ARRANGE ===
        // Cambiar la condición fiscal del cliente
        cliente.setCondicionIVA(CondicionFiscal.CONSUMIDOR_FINAL);
        
        when(cuentaClienteRepository.findById(1L))
            .thenReturn(Optional.of(cuentaCliente));
        
        List<ServicioContratado> serviciosActivos = new ArrayList<>();
        serviciosActivos.add(servicioContratado);
        when(servicioContratadoRepository.findByCuentaClienteIdAndActivo(1L, true))
            .thenReturn(serviciosActivos);
        
        when(facturaRepository.save(any(Factura.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));
        
        // === ACT ===
        Factura factura = facturaService.generarFacturaIndividual(1L);
        
        // === ASSERT ===
        assertEquals(TipoFactura.FACTURA_B, factura.getTipoFactura(), 
            "Debe ser Factura B");
        
        // Verificar el detalle
        DetalleFactura detalle = factura.getDetalles().get(0);
        
        // El precio ya incluye IVA, así que se calcula inversamente
        double netoEsperado = 5000.0 / 1.21; // ≈ 4132.23
        double ivaEsperado = 5000.0 - netoEsperado; // ≈ 867.77
        
        assertEquals(netoEsperado, detalle.getSubtotalNeto(), 0.01, 
            "El subtotal neto debe ser ~$4132.23");
        assertEquals(ivaEsperado, detalle.getMontoIVA(), 0.01, 
            "El IVA debe ser ~$867.77");
        assertEquals(5000.0, factura.getMontoTotalFinal(), 0.01, 
            "El total debe ser $5000");
    }
    
    /**
     * TEST 4: Verificar que NO se genera factura si no hay servicios activos
     * 
     * OBJETIVO: Si la cuenta no tiene servicios activos, debe lanzar excepción
     */
    @Test
    @DisplayName("Debe lanzar excepción si no hay servicios activos")
    void testGenerarFactura_SinServiciosActivos() {
        // === ARRANGE ===
        when(cuentaClienteRepository.findById(1L))
            .thenReturn(Optional.of(cuentaCliente));
        
        // Simular que NO hay servicios activos (lista vacía)
        when(servicioContratadoRepository.findByCuentaClienteIdAndActivo(1L, true))
            .thenReturn(new ArrayList<>());
        
        // === ACT & ASSERT ===
        // Verificar que se lanza una excepción con el mensaje esperado
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            facturaService.generarFacturaIndividual(1L);
        });
        
        assertTrue(exception.getMessage().contains("no tiene servicios activos"), 
            "El mensaje de error debe mencionar la falta de servicios activos");
        
        // Verificar que NO se guardó ninguna factura
        verify(facturaRepository, never()).save(any(Factura.class));
    }
    
    /**
     * TEST 5: Verificar que NO se genera factura si la cuenta no existe
     * 
     * OBJETIVO: Si el ID de cuenta es inválido, debe lanzar excepción
     */
    @Test
    @DisplayName("Debe lanzar excepción si la cuenta no existe")
    void testGenerarFactura_CuentaNoExiste() {
        // === ARRANGE ===
        // Simular que findById retorna Optional.empty() (no encontrado)
        when(cuentaClienteRepository.findById(999L))
            .thenReturn(Optional.empty());
        
        // === ACT & ASSERT ===
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            facturaService.generarFacturaIndividual(999L);
        });
        
        assertTrue(exception.getMessage().contains("Cuenta no encontrada"), 
            "El mensaje debe indicar que la cuenta no existe");
        
        // Verificar que NO se intentó buscar servicios ni guardar factura
        verify(servicioContratadoRepository, never())
            .findByCuentaClienteIdAndActivo(anyLong(), anyBoolean());
        verify(facturaRepository, never()).save(any(Factura.class));
    }
    
    /**
     * TEST 6: Verificar aplicación de descuentos en servicios contratados
     * 
     * OBJETIVO: Si un servicio tiene descuento, debe reflejarse en el precio
     */
    @Test
    @DisplayName("Debe aplicar descuentos correctamente")
    void testGenerarFactura_ConDescuento() {
        // === ARRANGE ===
        // Aplicar un descuento de $500 al servicio
        servicioContratado.setMontoDescuento(500.0);
        
        when(cuentaClienteRepository.findById(1L))
            .thenReturn(Optional.of(cuentaCliente));
        
        List<ServicioContratado> serviciosActivos = new ArrayList<>();
        serviciosActivos.add(servicioContratado);
        when(servicioContratadoRepository.findByCuentaClienteIdAndActivo(1L, true))
            .thenReturn(serviciosActivos);
        
        when(facturaRepository.save(any(Factura.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));
        
        // === ACT ===
        Factura factura = facturaService.generarFacturaIndividual(1L);
        
        // === ASSERT ===
        DetalleFactura detalle = factura.getDetalles().get(0);
        
        // Precio base: $5000, descuento: $500 = $4500
        // IVA 21% sobre $4500 = $945
        // Total: $5445
        assertEquals(4500.0, detalle.getSubtotalNeto(), 0.01, 
            "El subtotal debe ser $4500 (5000 - 500)");
        assertEquals(945.0, detalle.getMontoIVA(), 0.01, 
            "El IVA debe ser $945 (21% de 4500)");
        assertEquals(5445.0, factura.getMontoTotalFinal(), 0.01, 
            "El total debe ser $5445");
    }
    
    /**
     * TEST 7: Verificar que listarTodas() llama al repositorio
     * 
     * OBJETIVO: Método simple de consulta, verificar delegación correcta
     */
    @Test
    @DisplayName("Debe listar todas las facturas")
    void testListarTodas() {
        // === ARRANGE ===
        List<Factura> facturasEsperadas = new ArrayList<>();
        facturasEsperadas.add(new Factura());
        facturasEsperadas.add(new Factura());
        
        when(facturaRepository.findAll()).thenReturn(facturasEsperadas);
        
        // === ACT ===
        List<Factura> resultado = facturaService.listarTodas();
        
        // === ASSERT ===
        assertEquals(2, resultado.size(), "Debe retornar 2 facturas");
        verify(facturaRepository, times(1)).findAll();
    }
    
    /**
     * TEST 8: Verificar actualización de estado de factura
     * 
     * OBJETIVO: Cambiar estado de PENDIENTE a PAGADA
     */
    @Test
    @DisplayName("Debe actualizar el estado de una factura")
    void testActualizarEstado() {
        // === ARRANGE ===
        Factura factura = new Factura();
        factura.setId(1L);
        factura.setEstado(EstadoFactura.PENDIENTE);
        factura.setCuentaCliente(cuentaCliente);
        
        when(facturaRepository.findById(1L)).thenReturn(Optional.of(factura));
        when(facturaRepository.save(any(Factura.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));
        
        // === ACT ===
        Factura facturaActualizada = facturaService.actualizarEstado(1L, EstadoFactura.PAGADA);
        
        // === ASSERT ===
        assertEquals(EstadoFactura.PAGADA, facturaActualizada.getEstado(), 
            "El estado debe cambiar a PAGADA");
        verify(facturaRepository, times(1)).save(factura);
    }
}
