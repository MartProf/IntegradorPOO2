# Roadmap del Proyecto SGFS

Planificación tentativa de historias de usuario distribuidas por iteración.

## Iteración 1: Cimientos y Gestión de Datos Maestros (MVP 1)
**Objetivo:** Establecer la arquitectura base, la conexión a la base de datos y permitir la administración de las entidades principales (Clientes, Servicios, Planes).

* **HU 2.3:** Configuración del proyecto Spring Boot, Arquitectura Cliente-Servidor y Repositorio.
* **HU 2.2:** Conexión segura a base de datos PostgreSQL con JPA.
* **HU 1.1:** ABM de Clientes con sus respectivas cuentas (Alta, Baja, Modificación).
* **HU 2.1:** Validación de restricción fiscal (Solo Responsable Inscripto).
* **HU 1.2:** ABM de Servicios y Planes.

## Iteración 2: Seguridad y Ciclo de Facturación (MVP 2)
**Objetivo:** Implementar la seguridad del sistema (Login) y desarrollar el núcleo de la lógica de facturación y cobranza.

* **HU (Seguridad):** Implementación de Login y roles (Admin/Operador) con Spring Security.
* **HU 1.3:** Lógica de Facturación Masiva (Generación de comprobantes).
* **HU 1.5:** Registro de Pagos (Parciales o Totales).
* **HU 1.6:** Emisión de Recibos.
* **Refactoring:** Mejoras en el código de la Iteración 1 e integración de validaciones robustas.