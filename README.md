# Sistema de Gestión de Facturación de Servicios (SGFS)

## 📋 Descripción del Proyecto

Sistema integral de facturación de servicios desarrollado para una empresa que gestiona cuentas de clientes con diferentes condiciones fiscales según la legislación argentina (IVA). El sistema permite la gestión completa del ciclo de facturación: desde la contratación de servicios hasta el cobro y anulación de comprobantes.

### Características Principales

El sistema ofrece:
- ✅ Gestión completa de clientes y sus cuentas
- ✅ Administración de servicios y planes
- ✅ Facturación individual y masiva por período
- ✅ Registro de pagos con múltiples medios
- ✅ Generación de notas de crédito
- ✅ Control de estado de cuenta de clientes
- ✅ Sistema de autenticación y seguridad

Este proyecto fue desarrollado como trabajo integrador para la materia **Programación Orientada a Objetos II**.

## 👥 Integrantes del Equipo

- **Ramos Federico Javier**
- **Maidana Martin**
- **Nuñez Gabriel**

## 🎯 Funcionalidades Principales

### ✅ Implementadas

#### Gestión de Clientes y Cuentas
- **ABM completo de Clientes**
  - Alta, baja y modificación de clientes
  - Datos: Razón Social, Nombre, Apellido, CUIT/DNI, Domicilio, Teléfono, Condición IVA
  - Creación automática de CuentaCliente asociada
  - Validación de CUIT/DNI único
  - Listado paginado de clientes

#### Gestión de Servicios y Planes
- **ABM completo de Servicios**
  - Alta, baja y modificación de servicios
  - Definición de precio base y alícuota de IVA
  - Validación de datos

- **ABM completo de Planes**
  - Alta, baja y modificación de planes
  - Definición de precio mensual
  - Gestión de planes activos/inactivos

- **Gestión de Servicios Contratados**
  - Asignación de servicios y planes a cuentas
  - Definición de fecha de inicio y fin
  - Edición y cancelación de servicios contratados
  - Visualización del estado de servicios por cliente

#### Sistema de Facturación
- **Facturación Individual**
  - Generación de facturas por cuenta específica
  - Cálculo automático de importes con IVA según condición fiscal
  - Detalle de items facturados
  - Visualización de facturas generadas

- **Facturación Masiva por Período**
  - Generación automática de facturas para todas las cuentas
  - Selección de período (mes/año)
  - Filtrado de servicios vigentes en el período
  - Reporte de resultados de facturación masiva

- **Consulta de Facturas**
  - Listado de facturas con filtros
  - Visualización de detalles de factura
  - Búsqueda de facturas por número, cliente o período
  - Exportación de información

#### Sistema de Pagos
- **Registro de Pagos**
  - Registro de pagos por cuenta
  - Soporte de múltiples medios de pago en un mismo pago
  - Medios soportados: Efectivo, Transferencia, Cheque, Tarjeta
  - Emisión automática de recibo
  - Actualización del saldo de la cuenta

- **Gestión de Pagos**
  - Listado de pagos registrados
  - Detalle completo del pago y medios utilizados
  - Visualización de recibos

#### Notas de Crédito
- **Generación de Notas de Crédito**
  - Anulación de facturas mediante nota de crédito
  - Ajuste automático del saldo de cuenta
  - Registro de motivo de anulación
  - Trazabilidad de facturas anuladas

- **Consulta de Notas de Crédito**
  - Listado de notas de crédito generadas
  - Detalle de nota de crédito con factura asociada
  - Visualización del impacto en el saldo

#### Sistema de Autenticación y Seguridad
- **Spring Security**
  - Login con autenticación de usuarios
  - Registro de nuevos usuarios
  - Encriptación de contraseñas con BCrypt
  - Protección de rutas y recursos
  - Sesiones seguras

#### Dashboard y Navegación
- **Dashboard Principal**
  - Menú de navegación centralizado
  - Acceso rápido a todas las funcionalidades
  - Interfaz responsiva y amigable
  - Layout modular con Thymeleaf

## 🛠️ Tecnologías Utilizadas

### Backend
- **Java 17**
- **Spring Boot 3.5.8**
- **Spring Data JPA** - Persistencia y mapeo objeto-relacional
- **Spring Security** - Autenticación y autorización
- **Lombok** 
- **PostgreSQL** - Base de datos relacional

### Frontend
- **Thymeleaf** - Motor de plantillas
- **HTML5 / CSS3** - Interfaz de usuario

### Herramientas
- **Maven** - Gestión de dependencias
- **Git / GitHub** - Control de versiones
- **VS Code** - IDE

## 📁 Estructura del Proyecto

```
IntegradorPOO2/
├── docs/                           # Documentación del proyecto
│   ├── img/                        # Capturas de pantalla e imágenes
│   ├── erp.md                      # Especificación de requisitos
│   ├── roadmap.md                  # Planificación de iteraciones
│   ├── dp-iteracion-1.md           # Diseño y planificación - Iteración 1
│   ├── dp-iteracion-2.md           # Diseño y planificación - Iteración 2
│   ├── retrospectiva-iteracion-1.md
│   └── retrospectiva-iteracion-2.md
├── src/
│   ├── main/
│   │   ├── java/com/example/demo/
│   │   │   ├── security/           # Configuración de Spring Security
│   │   │   ├── controller/         # Controladores MVC
│   │   │   ├── model/              # Entidades del dominio
│   │   │   ├── repository/         # Repositorios JPA
│   │   │   ├── service/            # Lógica de negocio
│   │   │   └── DemoApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── static/             # Recursos estáticos (CSS, JS)
│   │       │   └── css/
│   │       └── templates/          # Plantillas Thymeleaf
│   │           ├── clientes/
│   │           ├── planes/
│   │           ├── servicios/
│   │           ├── servicios-contratados/
│   │           ├── facturas/
│   │           ├── pagos/
│   │           ├── notas-credito/
│   │           ├── login/
│   │           ├── dashboard/
│   │           └── layout/
│   └── test/                       # Tests unitarios
├── pom.xml                         # Configuración de Maven
└── README.md
```

## 🚀 Requisitos Previos

Antes de ejecutar el proyecto, asegúrate de tener instalado:

- **Java JDK 17** o superior
- **Maven 3.6+**
- **PostgreSQL 12+**
- **Git**
- Un IDE compatible (IntelliJ IDEA, Eclipse, VS Code)

## ⚙️ Configuración e Instalación

### 1. Clonar el repositorio

```bash
git clone https://github.com/MartProf/IntegradorPOO2.git
cd IntegradorPOO2
```

### 2. Configurar la base de datos

1. Crear una base de datos en PostgreSQL:

```sql
CREATE DATABASE facturacion_db;
```

2. Editar el archivo `src/main/resources/application.properties`:

```properties
# Configuración de la base de datos
spring.datasource.url=jdbc:postgresql://localhost:5432/facturacion_db
spring.datasource.username=tu_usuario
spring.datasource.password=tu_contraseña

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# Thymeleaf
spring.thymeleaf.cache=false
```

### 3. Compilar el proyecto

```bash
mvn clean install
```

### 4. Ejecutar la aplicación

```bash
mvn spring-boot:run
```

O desde tu IDE, ejecutar la clase `DemoApplication.java`.

### 5. Acceder a la aplicación

Abrir un navegador y navegar a:

```
http://localhost:8080
```

## 👤 Usuario por Defecto

Para acceder al sistema, primero debes registrar un usuario en:

```
http://localhost:8080/registro
```

O si ya existe un usuario en la base de datos, iniciar sesión en:

```
http://localhost:8080/login
```

## 🗺️ Rutas Principales del Sistema

Una vez autenticado, el sistema ofrece las siguientes rutas:

### Gestión
- `/dashboard` - Panel principal con navegación
- `/clientes` - Listado de clientes
- `/clientes/nuevo` - Formulario de alta de cliente
- `/planes` - Listado de planes
- `/servicios` - Listado de servicios
- `/servicios-contratados` - Gestión de servicios contratados

### Facturación
- `/facturas` - Listado de facturas
- `/facturas/generar` - Facturación individual
- `/facturas/generar-masivas` - Facturación masiva por período
- `/facturas/buscar` - Búsqueda de facturas
- `/facturas/{id}` - Detalle de factura

### Pagos
- `/pagos` - Listado de pagos
- `/pagos/registrar` - Registro de nuevo pago
- `/pagos/{id}` - Detalle de pago y recibo

### Notas de Crédito
- `/notas-credito` - Listado de notas de crédito
- `/notas-credito/generar` - Generación de nota de crédito
- `/notas-credito/{id}` - Detalle de nota de crédito

### Autenticación
- `/login` - Inicio de sesión
- `/registro` - Registro de nuevo usuario
- `/logout` - Cerrar sesión

## 📊 Modelo de Datos

El sistema implementa las siguientes entidades principales:

- **Cliente**: Datos fiscales y personales de los clientes
- **CuentaCliente**: Estado de cuenta, saldo deudor, saldo acreedor
- **Plan**: Planes de servicios con precio mensual y estado activo/inactivo
- **Servicio**: Servicios individuales con precio base y alícuota de IVA
- **ServicioContratado**: Relación entre cuenta y servicios/planes con fechas de vigencia
- **Factura**: Comprobantes de facturación con fecha y total
- **DetalleFactura**: Items de cada factura con descripción, cantidad y precio unitario
- **Pago**: Registro de pagos realizados con fecha y total
- **ItemPago**: Detalle de medios de pago utilizados (efectivo, transferencia, cheque, tarjeta)
- **NotaCredito**: Anulaciones de facturas con motivo y ajuste de saldo
- **Usuario**: Usuarios del sistema con autenticación

### Relaciones Principales

- Un Cliente tiene una CuentaCliente
- Una CuentaCliente puede tener múltiples ServiciosContratados
- Una Factura pertenece a una CuentaCliente y contiene múltiples DetalleFactura
- Un Pago se registra en una CuentaCliente y puede tener múltiples ItemPago
- Una NotaCredito anula una Factura específica

Para más detalles, consultar el diagrama de clases en `docs/img/Diagrama de clases.jpg`.

## 📖 Documentación

La documentación completa del proyecto se encuentra en la carpeta `docs/`:

- **[erp.md](docs/erp.md)**: Especificación de requisitos de software
- **[roadmap.md](docs/roadmap.md)**: Planificación de iteraciones
- **[dp-iteracion-1.md](docs/dp-iteracion-1.md)**: Diseño y planificación de la Iteración 1
- **[dp-iteracion-2.md](docs/dp-iteracion-2.md)**: Diseño y planificación de la Iteración 2
- **[retrospectiva-iteracion-1.md](docs/retrospectiva-iteracion-1.md)**: Retrospectiva de la Iteración 1
- **[retrospectiva-iteracion-2.md](docs/retrospectiva-iteracion-2.md)**: Retrospectiva de la Iteración 2

## 🎥 Capturas de Pantalla

Las capturas de pantalla del sistema en funcionamiento se encuentran en `docs/img/`.

## 🧪 Pruebas

El proyecto incluye tests unitarios para los componentes principales del sistema.

Para ejecutar los tests:

```bash
mvn test
```

### Tests Implementados

- **FacturaControllerTest**: Tests para el controlador de facturación
- Tests de servicios y repositorios
- Validación de lógica de negocio

## 🔧 Solución de Problemas

### Error de conexión a la base de datos

- Verificar que PostgreSQL esté ejecutándose
- Verificar credenciales en `application.properties`
- Asegurar que la base de datos `facturacion_db` exista
- Verificar que el puerto 5432 esté disponible

### Puerto 8080 ocupado

Cambiar el puerto en `application.properties`:

```properties
server.port=8081
```

### Problemas con Maven

Limpiar y reinstalar dependencias:

```bash
mvn clean install -U
```

### Error al iniciar sesión

- Verificar que el usuario esté registrado en la base de datos
- Limpiar cookies y caché del navegador
- Verificar que Spring Security esté configurado correctamente

## 📝 Metodología de Desarrollo

El proyecto se desarrolló siguiendo una metodología ágil con iteraciones de 2-3 semanas:

- **Iteración 1**: Setup inicial del proyecto, modelo de datos básico, ABM de Clientes/Planes/Servicios, configuración de Spring Boot y PostgreSQL
- **Iteración 2**: Implementación de Spring Security, autenticación y autorización, mejoras en el modelo de datos, layout modular con Thymeleaf, gestión de servicios contratados
- **Iteración 3**: Sistema de facturación (individual y masiva), gestión de pagos con múltiples medios, notas de crédito, consultas y reportes

Cada iteración incluyó:
- Planificación y diseño detallado
- Implementación de historias de usuario
- Pruebas y validación
- Retrospectiva del equipo
- Documentación técnica

## ✨ Características Destacadas

- **Cálculo automático de IVA** según condición fiscal del cliente (Responsable Inscripto, Monotributista, Exento)
- **Facturación masiva** eficiente para procesamiento de múltiples cuentas
- **Múltiples medios de pago** en una misma transacción
- **Trazabilidad completa** de facturas, pagos y anulaciones
- **Interfaz intuitiva** con diseño responsivo
- **Seguridad robusta** con Spring Security y encriptación BCrypt
- **Arquitectura escalable** con separación de capas (Controller, Service, Repository)

## 🤝 Contribución

Este es un proyecto académico. Para contribuir:

1. Fork del repositorio
2. Crear una rama feature: `git checkout -b feature/nueva-funcionalidad`
3. Commit de cambios: `git commit -am 'Agregar nueva funcionalidad'`
4. Push a la rama: `git push origin feature/nueva-funcionalidad`
5. Crear Pull Request

## 📜 Licencia

Este proyecto es un trabajo académico desarrollado para la materia Programación Orientada a Objetos II.

## 📞 Contacto

Para consultas sobre el proyecto, contactar a los integrantes del equipo a través del repositorio de GitHub.

---
 
**Materia:** Programación Orientada a Objetos II  
**Año:** 2025
