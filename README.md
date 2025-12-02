# Sistema de Gestión de Facturación de Servicios (SGFS)

## 📋 Descripción del Proyecto

Sistema de facturación de servicios desarrollado para una empresa que gestiona cuentas de clientes con diferentes condiciones fiscales según la legislación argentina (IVA). El sistema permite la gestión completa de cuentas y clientes, facturación masiva por período, facturación individual, anulación de facturas y registro de pagos.

Este proyecto fue desarrollado como trabajo integrador para la materia **Programación Orientada a Objetos II**.

## 👥 Integrantes del Equipo

- **Ramos Federico Javier**
- **Maidana Martin**
- **Nuñez Gabriel**

## 🎯 Funcionalidades Principales

### ✅ Implementadas (Iteración 1 y 2)

- **Gestión de Clientes (ABM completo)**
  - Alta, baja y modificación de clientes
  - Datos: Razón Social, Nombre, Apellido, CUIT/DNI, Domicilio, Teléfono, Condición IVA
  - Creación automática de CuentaCliente asociada
  - Validación de CUIT/DNI único

- **Gestión de Servicios (ABM completo)**
  - Alta, baja y modificación de servicios
  - Definición de precio base y alícuota de IVA

- **Gestión de Planes (ABM completo)**
  - Alta, baja y modificación de planes
  - Definición de precio mensual

- **Sistema de Autenticación y Seguridad**
  - Login con Spring Security
  - Registro de usuarios
  - Encriptación de contraseñas con BCrypt
  - Protección de rutas

- **Dashboard Principal**
  - Menú de navegación centralizado
  - Acceso rápido a todas las secciones

### 🚧 En Desarrollo (Iteración 3)

- Facturación masiva por período
- Facturación individual
- Registro de pagos con múltiples medios de pago
- Emisión de recibos
- Anulación de facturas
- Reportes y consultas de estado de cuenta

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

## 📊 Modelo de Datos

El sistema implementa las siguientes entidades principales:

- **Cliente**: Datos fiscales y personales de los clientes
- **CuentaCliente**: Estado de cuenta, deuda, saldo a favor
- **Plan**: Planes de servicios con precio mensual
- **Servicio**: Servicios individuales con precio base e IVA
- **ServicioContratado**: Relación entre cuenta y servicios
- **Factura**: Comprobantes de facturación
- **DetalleFactura**: Items de cada factura
- **Pago**: Registro de pagos realizados
- **ItemPago**: Detalle de medios de pago utilizados
- **NotaCredito**: Anulaciones de facturas
- **Recibo**: Comprobantes de pago
- **Usuario**: Usuarios del sistema

Para más detalles, consultar el diagrama de clases en `docs/img/Diagrama de clases.jpg`.

## 📖 Documentación

La documentación completa del proyecto se encuentra en la carpeta `docs/`:

- **[erp.md](docs/erp.md)**: Especificación de requisitos de software
- **[roadmap.md](docs/roadmap.md)**: Planificación de iteraciones
- **[dp-iteracion-1.md](docs/dp-iteracion-1.md)**: Diseño y planificación de la Iteración 1
- **[dp-iteracion-2.md](docs/dp-iteracion-2.md)**: Diseño y planificación de la Iteración 2
- **[retrospectiva-iteracion-1.md](docs/retrospectiva-iteracion-1.md)**: Retrospectiva de la Iteración 1
- **[retrospectiva-iteracion-2.md](docs/retrospectiva-iteracion-2.md)**: Retrospectiva de la Iteración 2

## 🧪 Pruebas (Nos falta)

Para ejecutar los tests:

```bash
mvn test
```

## 🔧 Solución de Problemas

### Error de conexión a la base de datos


- Verificar credenciales en `application.properties`
- Asegurar que la base de datos `facturacion_db` exista

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

## 📝 Metodología de Desarrollo

El proyecto se desarrolló siguiendo una metodología ágil con iteraciones de 2 semanas:

- **Iteración 1**: Setup inicial, ABM de Clientes/Planes/Servicios, modelo de datos básico
- **Iteración 2**: Spring Security, mejoras en el modelo, layout Thymeleaf modular
- **Iteración 3** (En progreso): Facturación, pagos y reportes

Cada iteración incluye:
- Planificación y diseño
- Implementación de historias de usuario
- Retrospectiva del equipo

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
