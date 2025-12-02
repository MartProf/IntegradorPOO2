# Especificación de requisitos de software

## Enunciado del Problema

El problema por resolver es la ineficiencia operativa y el alto riesgo de error fiscal en la gestión del ciclo de facturación de servicios. La empresa necesita un mecanismo que automatice la Facturación Masiva Mensual para sus cuentas recurrentes y que aplique de manera rigurosa la lógica fiscal argentina (IVA) en la emisión de comprobantes (Factura A, B, C, etc.).

### Posibles clientes

Los usuarios potenciales que se ven afectados por este problema principalmente son administradores/operadores de facturación y el área de contabilidad o gerencia:

- **Administrador/Operador de Facturación:** Son los usuarios primarios que se benefician de la automatización. Necesitan dar de alta clientes, gestionar sus condiciones fiscales, ejecutar la facturación masiva y registrar los pagos con emisión de recibo.
- **Área de Contabilidad/Gerencia:** Se benefician de la precisión y la integridad de los datos fiscales y transaccionales. Requieren un sistema que soporte la anulación de facturas con motivos y el correcto registro del estado de la cuenta.

## Solución propuesta

Se propone desarrollar un **Sistema de Gestión de Facturación de Servicios (SGFS)** implementado como una **Aplicación Web** robusta. Esta solución abordará el problema mediante:

- **Gestión Centralizada (ABM):** Un módulo para el alta, baja y modificación de las Cuentas de clientes y la asociación de servicios con sus respectivos Planes (Simple, Premium, etc.)
- **Automatización de Ingresos:** Implementación de la Facturación Masiva y Facturación Individual, con capacidad para generar comprobantes discriminados por cada servicio contratado dentro de una cuenta
- **Flexibilidad en Cobranza:** Un sistema de pagos que permite el registro de pagos parciales (en partes) y soporta la asociación de múltiples medios de pago a una sola transacción, emitiendo un Recibo detallado por el monto ingresado
- **Integridad Fiscal:** El sistema garantizará la correcta aplicación de la lógica fiscal argentina (IVA) al enfocarse únicamente en clientes con la condición fiscal de **Responsable Inscripto**.

## Requisitos (Historias de Usuario)

### Requisitos Funcionales (RF)

| ID | Historia de Usuario | Tipo de Requisito |
|:---|:---|:---|
| **HU 1.1** | Como **Administrador**, quiero realizar un **ABM (Alta, Baja, Modificación)** de las Cuentas de Clientes, incluyendo sus datos fiscales y personales. | Gestión de Clientes/ABM |
| **HU 1.2** | Como **Administrador**, quiero **definir y asociar múltiples Servicios y Planes** (ej. Simple, Premium) a la Cuenta de un Cliente, para poder facturar correctamente. | Gestión de Servicios/Cuentas |
| **HU 1.3** | Como **Administrador**, quiero **ejecutar la Facturación Masiva por período** a todos los clientes activos, generando un único comprobante que discrimine cada servicio/plan. | Facturación Masiva |
| **HU 1.4** | Como **Administrador**, quiero **generar Facturas Individuales** para servicios puntuales fuera del ciclo masivo. | Facturación Individual |
| **HU 1.5** | Como **Operador**, quiero **registrar un Pago Parcial o Total** sobre una factura, indicando la cantidad y los **distintos Medios de Pago** utilizados (ej. $X$ con tarjeta, $Y$ con transferencia). | Gestión de Pagos |
| **HU 1.6** | Como **Operador**, quiero **emitir un Recibo** que especifique la factura saldada, el importe pagado, el medio de pago, y el detalle de los servicios/períodos que corresponden a ese pago. | Recibos |
| **HU 1.7** | Como **Administrador**, quiero **anular una Factura emitida**, registrando el motivo, para corregir errores en el sistema. | Anulación |

### Requisitos No Funcionales (RNF)

| ID | Historia de Usuario | Tipo de Requisito |
|:---|:---|:---|
| **HU 2.1** | El sistema debe **restringir el registro de Clientes** a aquellos que posean la condición fiscal de **Responsable Inscripto**, para cumplir con el alcance del proyecto. | Restricción Fiscal |
| **HU 2.2** | El sistema debe utilizar una **conexión segura y persistente** a la base de datos para garantizar la integridad de todos los registros transaccionales (facturas, pagos). | Seguridad/Técnico |
| **HU 2.3** | Como Usuario, quiero que el sistema sea accesible a través de un **navegador web**, con una arquitectura centralizada y robusta. | Arquitectura/Usabilidad |

## Arquitectura de software

### Tipo de Aplicación

Se desarrollará una **Aplicación Web**.

### Arquitectura del Software

La arquitectura se ajustará al modelo **Cliente-Servidor (o 3 capas)**, donde el navegador (Cliente) consume servicios de una aplicación back-end (Servidor) que gestiona la lógica de negocio y la persistencia.

- **Front-end (Cliente):** Interfaz de usuario web.
- **Back-end (Servidor/Lógica):** Implementado con **Spring Boot y Java** para manejar la lógica de negocio orientada a objetos (POO).
- **Capa de Persistencia:** Utilizará **JPA (Java Persistence API)** para mapear los objetos del modelo a la base de datos.
- **Base de Datos:** Se utilizará **PostgreSQL** como servidor de bases de datos.