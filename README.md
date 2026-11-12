# Proyecto-Ingenieria-de-software2

# TiendaDP – Tienda Virtual

Tienda en línea desarrollada en Java para el parcial de Patrones de Diseño de Ingeniería de Software 2 (propuesta 1). El sistema cuenta con una interfaz gráfica desarrollada en Java Swing y una versión por consola, ambas utilizando el mismo núcleo de lógica de negocio.

**Integrantes:** Juan Ospina, Juan Rodríguez y Erick Penagos.

**Entrega final:** 23 de noviembre de 2026.

---

## De qué trata

La idea es desarrollar un sistema de compras con catálogo, carrito y cierre de pedido. El cliente escoge productos, les agrega extras, el sistema calcula los descuentos, procesa el pago mediante un medio de pago simulado y al final genera una factura.

También se contempla un modo administrador para registrar productos, reponer existencias y crear cupones.

El proyecto está desarrollado en Java 17 y los precios se manejan en pesos colombianos (COP). Para el manejo de valores monetarios se utiliza `BigDecimal`, evitando problemas de precisión en los cálculos.

## Alcance

La propuesta del profesor trae una lista de opciones y cada grupo decide cuáles desarrolla. Este es el alcance definido para el proyecto.

### Lo que sí vamos a hacer

- **Catálogo con tres categorías:** ropa, tecnología y alimentos. Cada una tiene sus propios atributos, y esta es una de las partes del sistema donde existe variación.
- **Carrito de compras:** agregar, quitar y cambiar la cantidad de los productos. Es el centro del flujo de compra, y de ahí dependen los descuentos y los extras.
- **Descuentos:** por cantidad, por temporada y por cupón. Algunas reglas pueden cambiar con el tiempo y se manejan mediante clases independientes.
- **Medios de pago simulados:** tarjeta, transferencia y contra entrega. Cada uno valida información diferente y puede tener recargos distintos.
- **Extras sobre un producto:** garantía extendida, empaque de regalo y envío express. Se pueden combinar según las condiciones de cada producto.
- **Aviso de reposición:** si un producto está agotado y vuelve a tener existencias, los clientes que se suscribieron reciben un aviso.
- **Historial de pedidos y factura:** la factura se genera como texto formateado y se puede consultar desde el sistema.
- **Modo administrador:** registrar productos, reponer existencias y crear cupones. Estas operaciones permiten demostrar las diferentes funcionalidades del sistema.

### Lo que dejamos por fuera

| Qué | Por qué |
|---|---|
| Base de datos o archivos | Los datos viven en memoria mientras corre el programa. Se busca mantener una estructura que permita incorporar almacenamiento posteriormente. |
| Registro de usuarios con contraseña | El cliente se elige o se crea utilizando información básica. Una contraseña no aporta directamente a los patrones de diseño. |
| Pasarelas de pago reales | Los pagos son simulados, tal como permite la propuesta del proyecto. |
| Logística de envíos, devoluciones y cancelaciones | El envío express es solamente un extra con costo. Lo demás ampliaría el sistema sin aportar variaciones necesarias para los patrones. |
| Correos o SMS reales | Los avisos se manejan dentro del sistema, sin utilizar servicios externos. |
| Aplicación web | El proyecto se desarrolla como aplicación de escritorio en Java y no necesita una versión web. |

## Dónde hay variación o cambio frecuente

El proyecto trabaja con cinco zonas donde pueden existir cambios o variaciones:

1. Las categorías de producto.
2. Los descuentos.
3. Los medios de pago.
4. Los extras.
5. Los avisos de reposición.

Los porcentajes, recargos y demás valores se manejan de forma que puedan modificarse fácilmente sin tener que cambiar toda la lógica del sistema.

## Requisitos funcionales

**RF-01. Registrar productos con su categoría y los atributos correspondientes:**

- Ropa: talla y color.
- Tecnología: marca y meses de garantía.
- Alimentos: fecha de vencimiento y si necesita refrigeración.

Todo producto tiene código, nombre y precio base.

**RF-02.** Ver el catálogo completo y filtrarlo por categoría.

**RF-03.** Agregar productos al carrito, cambiar la cantidad y quitarlos. No se puede pedir más de lo que hay en existencias.

**RF-04.** Agregar extras a un producto del carrito, uno o varios a la vez. Hay reglas de dónde aplica cada uno:

- Garantía extendida: solo tecnología.
- Empaque de regalo: ropa y tecnología.
- Envío express: cualquier categoría.

Cada extra suma su costo por cada unidad.

**RF-05. Calcular los descuentos automáticamente:**

- Por cantidad: 10 % desde 5 unidades del mismo producto.
- Por temporada: 10 % sobre una categoría, dentro de un rango de fechas que se puede configurar.
- Por cupón: un código con un valor fijo y fecha de vigencia.

**RF-06. Reglas de acumulación de descuentos:**

- Los descuentos por cantidad y temporada no se acumulan entre ellos. Si aplican los dos, se toma solamente el mayor.
- Los descuentos por cupón se aplican de acuerdo con sus condiciones.
- El descuento total no puede pasar del 50 % del subtotal de los productos.
- Los descuentos se calculan sobre el precio base, no sobre los extras.

**RF-07. Pagar con tres medios simulados:**

- Tarjeta: número de 16 dígitos, de 1 a 36 cuotas y 3 % de recargo si es más de una cuota. Las tarjetas que terminan en 0000 se rechazan para simular un pago no aprobado.
- Transferencia: pide un número de referencia y no tiene recargo.
- Contra entrega: solo para pedidos de hasta $1.000.000 y con un recargo fijo de $5.000.

**RF-08.** Cerrar el pedido. Se verifican las existencias, se calcula el total, se procesa el pago, se descuenta del inventario, se genera la factura y se vacía el carrito. Si el pago es rechazado, el pedido no se confirma, el inventario no cambia y el cliente puede intentar nuevamente con otro medio de pago.

**RF-09.** Generar la factura con número consecutivo, fecha, los productos con sus extras, cada descuento aplicado con su valor, el recargo del medio de pago y el total.

**RF-10.** Consultar el historial de pedidos de un cliente y ver la factura de cada uno.

**RF-11.** Suscribirse al aviso de un producto agotado y poder cancelar esa suscripción.

**RF-12.** Cuando se repone un producto agotado, avisar a todos los clientes suscritos. El aviso queda en la bandeja de avisos de cada cliente.

**RF-13.** La consola y la interfaz gráfica utilizan el mismo núcleo de lógica de negocio, evitando duplicar las reglas principales del sistema.

**RF-14.** En modo administrador: registrar productos, reponer existencias y crear cupones.

## Requisitos no funcionales

| ID | Requisito | Cómo lo vamos a verificar |
|---|---|---|
| RNF-01 | Extensibilidad. Para agregar una categoría, un descuento, un medio de pago o un extra se debe poder crear una clase nueva y registrarla sin modificar innecesariamente las clases existentes. | Probando agregar una variante nueva, como si nos pidieran un cambio en vivo. |
| RNF-02 | Separación de capas. El núcleo no debe depender de la consola ni de Swing. Las interfaces se comunican con los servicios del sistema. | Revisando las dependencias entre paquetes. |
| RNF-03 | Exactitud con el dinero. Los valores monetarios se manejan con BigDecimal, evitando utilizar double para los cálculos. | Pruebas unitarias del cálculo de totales. |
| RNF-04 | Pruebas. Se utiliza JUnit 5 para verificar las clases relacionadas con los patrones y otros componentes principales del sistema. | Ejecución de las pruebas unitarias y revisión de sus resultados. |
| RNF-05 | Mantenibilidad. Paquetes con responsabilidades claras, clases pequeñas y documentación en las interfaces de los patrones. | Revisión de código entre los integrantes. |
| RNF-06 | Usabilidad. Menú de consola con validación de entradas y mensajes claros. Interfaz gráfica en Swing para facilitar la navegación por el catálogo, carrito y demás funcionalidades. | Prueba manual de los flujos principales. |
| RNF-07 | Portabilidad. Java 17 y herramientas de compilación compatibles con el proyecto. Debe poder ejecutarse en los computadores de los integrantes. | Compilando y ejecutando el proyecto en los computadores de los tres. |
| RNF-08 | Desempeño. Cada operación debe responder en menos de un segundo con un catálogo de hasta 1.000 productos en memoria. | Prueba con un catálogo de muestra grande. |
| RNF-09 | Trazabilidad. Repositorio Git con commits de cada integrante y un README con las instrucciones de compilación, ejecución y funcionalidades. | Revisión del historial de commits en GitHub. |

## Patrones de diseño utilizados

### Strategy

Permite definir diferentes comportamientos mediante clases independientes que pueden intercambiarse según la necesidad.

En el proyecto se utiliza para manejar las estrategias de descuentos y los diferentes métodos de pago. Así se pueden agregar o cambiar comportamientos sin modificar directamente el procesador principal.

### Decorator

Permite agregar responsabilidades y funcionalidades adicionales a un objeto sin modificar directamente su clase original.

En el proyecto se utiliza para agregar extras a los productos, como garantía extendida, empaque de regalo y envío express. Los extras pueden combinarse y sus costos se suman al valor del producto.

### Observer

Permite establecer una relación de notificación entre un objeto que genera eventos y los objetos interesados en recibirlos.

En el proyecto se utiliza para manejar los avisos relacionados con la reposición de productos y las suscripciones de los clientes. Cuando se actualiza el inventario, los observadores suscritos reciben la notificación correspondiente.

## Organización del código

El código fuente está organizado en paquetes, cada uno con responsabilidades específicas:

| Paquete | Responsabilidad |
|---|---|
| `com.tiendavirtual` | Contiene la clase principal de ejecución del proyecto. |
| `com.tiendavirtual.consola` | Contiene el menú y las operaciones disponibles desde la consola. |
| `com.tiendavirtual.modelo` | Contiene las clases principales del sistema, como Cliente, Producto, Carrito, Pedido y Factura. |
| `com.tiendavirtual.grafica` | Contiene las ventanas y componentes de la interfaz gráfica desarrollada con Swing. |
| `com.tiendavirtual.servicios` | Contiene las clases encargadas de gestionar las operaciones y la lógica de negocio. |
| `com.tiendavirtual.descuentos` | Contiene las clases relacionadas con las diferentes estrategias de descuentos. |
| `com.tiendavirtual.pagos` | Contiene las clases encargadas de los métodos de pago y su procesamiento. |
| `com.tiendavirtual.extras` | Contiene los componentes adicionales que se pueden agregar a los productos mediante Decorator. |
| `com.tiendavirtual.notificaciones` | Contiene las clases encargadas de gestionar las suscripciones y notificaciones mediante Observer. |

También se cuenta con un paquete de pruebas unitarias (Test Packages) para verificar el comportamiento de las diferentes clases.

## Tecnologías utilizadas

- **Java 17:** lenguaje principal de desarrollo.
- **Java Swing:** desarrollo de la interfaz gráfica.
- **JUnit 5.10.3:** ejecución de pruebas unitarias.
- **NetBeans:** entorno de desarrollo utilizado.
- **Git:** control de versiones.
- **GitHub:** alojamiento del repositorio y trabajo colaborativo.
- **BigDecimal:** manejo de valores monetarios.

## Cómo compilar y ejecutar

### Desde NetBeans

1. Descargar o clonar el repositorio de GitHub.
2. Abrir NetBeans.
3. Seleccionar **File → Open Project**.
4. Buscar la carpeta del proyecto TiendaVirtual.
5. Abrir el proyecto.
6. Esperar a que NetBeans reconozca la estructura del proyecto.
7. Ejecutar la clase principal del sistema mediante **Run Project**.

### Para ejecutar las pruebas unitarias

1. Abrir el proyecto en NetBeans.
2. Ubicar la carpeta **Test Packages**.
3. Seleccionar la clase de prueba que se desea ejecutar.
4. Hacer clic derecho y seleccionar **Test File**.
5. Revisar los resultados en la ventana de pruebas.

## Funcionalidades implementadas

La aplicación cuenta con las siguientes funcionalidades desarrolladas:

- Inicio de sesión de clientes mediante información básica.
- Catálogo de productos de ropa, tecnología y alimentos.
- Visualización de seis productos con imágenes.
- Visualización de nombres, precios, identificadores y existencias.
- Agregar productos al carrito.
- Seleccionar y modificar cantidades de productos.
- Validar las existencias disponibles.
- Gestionar los productos del carrito.
- Agregar extras a los productos.
- Aplicar descuentos disponibles.
- Procesar pagos simulados mediante tarjeta, transferencia y contra entrega.
- Gestionar el inventario y la reposición de existencias.
- Manejar suscripciones y notificaciones de reposición.
- Generar facturas con información del cliente y los productos comprados.
- Consultar información de las compras.
- Interfaz gráfica desarrollada con Java Swing.
- Versión por consola que reutiliza el núcleo de lógica de negocio.
- Organización del código en paquetes según sus responsabilidades.

## Pruebas unitarias

El proyecto utiliza **JUnit 5.10.3** para verificar el comportamiento de las clases relacionadas con los patrones de diseño y otros componentes principales del sistema.

Se han ejecutado correctamente **81 pruebas unitarias**, distribuidas de la siguiente manera:

| Módulo | Pruebas exitosas |
|---|---:|
| Descuentos (Strategy) | 8 |
| Extras (Decorator) | 5 |
| Notificaciones (Observer) | 6 |
| Procesador de pagos | 5 |
| Métodos de pago | 12 |
| Inventario | 7 |
| Catálogo | 6 |
| Carrito | 14 |
| Pedidos | 10 |
| Facturación | 8 |
| **Total** | **81** |

Las pruebas permiten verificar, entre otros aspectos:

- El cálculo y la acumulación de descuentos.
- La validación de los métodos de pago.
- La incorporación de extras y sus costos.
- La suscripción, cancelación y envío de notificaciones.
- La disponibilidad y actualización de existencias.
- La administración del catálogo de productos.
- La gestión de cantidades y productos en el carrito.
- La creación y gestión de pedidos.
- La generación y consulta de facturas.

**Resultado de las pruebas ejecutadas: 81 pruebas exitosas.**

## Control de versiones

El proyecto utiliza Git y GitHub para almacenar el código fuente y llevar un registro de los cambios realizados por los integrantes.

La rama principal de trabajo es `master`, donde se encuentra el código fuente del proyecto.

**Repositorio:** [TiendaVirtual en GitHub](https://github.com/camilorza898-a11y/TiendaVirtual)

## Estado del proyecto

El proyecto cuenta con una estructura organizada por paquetes y dos modalidades de interacción: una interfaz gráfica funcional desarrollada en Java Swing y una versión por consola que reutiliza el núcleo de lógica de negocio.

Se encuentran desarrolladas las funcionalidades principales de catálogo, carrito de compras, gestión de productos, inventario, extras, descuentos, pagos simulados, notificaciones y generación de facturas.

También se implementaron los patrones de diseño Strategy, Decorator y Observer, aplicados a diferentes componentes del sistema.

Se han ejecutado correctamente **81 pruebas unitarias con JUnit 5.10.3**, verificando el comportamiento de los descuentos, extras, notificaciones, pagos, inventario, catálogo, carrito, pedidos y facturación.

El código fuente se encuentra organizado en paquetes y el proyecto utiliza Git y GitHub para el control de versiones y el trabajo colaborativo.

