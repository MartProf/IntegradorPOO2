# Retrospectiva - Iteración 2

## Qué cosas hemos cumplido
* ✅ Implementación exitosa de Spring Security con autenticación basada en base de datos.
* ✅ Sistema completo de Login, Logout y Registro de usuarios con encriptación BCrypt.
* ✅ El modelo de datos ahora soporta toda la complejidad de la facturación (Factura, DetalleFactura, Pago, ItemPago, NotaCredito, Recibo, ServicioContratado).
* ✅ ABM de Clientes ampliado con creación automática de CuentaCliente.
* ✅ ABM de Planes y Servicios completamente funcionales con vistas Thymeleaf consistentes.
* ✅ Mejora significativa en la estructura del layout Thymeleaf con fragmentos CSS modulares y reutilizables.
* ✅ Integración completa con PostgreSQL y persistencia de todas las entidades.
* ✅ Dashboard funcional con menú de navegación y acceso a todas las secciones del sistema.
* ✅ Tenemos la logica de facturación masiva y cálculo automático de totales.

## Qué cosas NO hemos cumplido
* No llegamos a completar las vistas HTML para la gestión de facturas y pagos, aunque el modelo de datos está listo.
* Falta implementar las vistas de facturación masiva y cálculo automático de totales.
* No se desarrollaron los reportes y consultas de estado de cuenta.
* Falta agregar validaciones del lado del cliente (JavaScript) en los formularios.

## Retos y Problemas
* Integrar Spring Security con las vistas existentes de Thymeleaf requirió más tiempo del esperado, especialmente en la configuración de rutas protegidas.
* Resolver problemas de CSS y fragmentos de Thymeleaf consumió tiempo que podría haberse usado en otras funcionalidades.
* La coordinación en Git mejoró, pero aún hubo algunos conflictos menores que resolver.
* El diseño del modelo de datos de facturación fue complejo y requirió varias iteraciones para llegar a la estructura final.

## Aprendizajes
* Spring Security es más flexible de lo que pensábamos, pero requiere una configuración cuidadosa.
* Los fragmentos de Thymeleaf con inyección de CSS mejoran significativamente la modularidad y mantenibilidad del código.
* La creación automática de entidades relacionadas (como CuentaCliente al crear Cliente) simplifica la experiencia del usuario.
* Es fundamental definir bien el modelo de datos antes de comenzar con la implementación de lógica de negocio.

## Plan de mejora para la próxima iteración
* Priorizar la funcionalidad de facturación y pagos, comenzando por las vistas frontend.
* Agregar validaciones JavaScript en los formularios para mejorar la experiencia de usuario.
* Documentar mejor el código y las decisiones de diseño.
* Mejorar la comunicación sobre cambios en el modelo de datos y dependencias entre componentes.