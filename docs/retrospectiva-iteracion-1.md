# Retrospectiva - Iteración 1

## Qué cosas hemos cumplido
* ✅ Inicialización exitosa del proyecto Spring Boot con todas las dependencias necesarias.
* ✅ Configuración completa del entorno de desarrollo en todas las máquinas del equipo (IDE, PostgreSQL, Maven).
* ✅ Conexión funcional a la base de datos PostgreSQL con configuración de `application.properties`.
* ✅ Creación de entidades básicas del dominio: Cliente, Plan, Servicio, CuentaCliente.
* ✅ Implementación de repositorios JPA y capa de servicios para las entidades principales.
* ✅ ABM (Alta) de Clientes, Planes y Servicios con persistencia en base de datos.
* ✅ Vistas Thymeleaf funcionales para todos los ABM.

## Qué cosas NO hemos cumplido
* Nos demoramos en definir el modelo exacto de facturación, lo que retrasó el inicio de la lógica de negocio compleja.
* No implementamos sistema de seguridad (quedó para la siguiente iteración).
* Faltó definir con claridad las validaciones de negocio desde el principio.

## Retos encontrados
* Curva de aprendizaje inicial con Thymeleaf para el manejo de formularios y paso de parámetros.
* Coordinación en el uso de Git: conflictos al trabajar en archivos similares simultáneamente.
* Dificultad para establecer las relaciones JPA correctas entre entidades (OneToOne, OneToMany).
* Tiempo invertido en configurar correctamente PostgreSQL y resolver problemas de conexión inicial.

## Aprendizajes
* La estructura de carpetas y separación en capas (modelo, repositorio, servicio, controlador) facilita el trabajo en equipo.
* Thymeleaf es más poderoso de lo esperado una vez que se comprende su sintaxis.
* Es fundamental definir bien el modelo de datos antes de comenzar con la implementación.

## Plan de mejora para la próxima iteración
* Mejorar la comunicación sobre los cambios en el modelo de datos usando diagramas actualizados.
* Implementar sistema de seguridad (Spring Security) para proteger las rutas.
* Definir el modelo completo de facturación y pagos antes de empezar a codificar.
* Establecer convenciones de código y buenas prácticas de Git (branches, commits descriptivos).
* Realizar commits más frecuentes y pull/push regulares para evitar conflictos grandes.