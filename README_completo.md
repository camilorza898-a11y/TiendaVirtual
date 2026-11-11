# TiendaDP

**Parcial de Patrones de Diseño · Ingeniería de Software 2**

Tienda en línea en Java · Propuesta 1 · Plan del proyecto y documento de diseño

**Integrantes:** Juan Ospina, Juan Rodriguez y Erick Penagos

**Fecha de entrega del proyecto:** 23 de noviembre de 2026

*Versión preliminar, previa a la implementación*

## Contenido

1. [Introducción](#1-introducción)
2. [Alcance](#2-alcance)
3. [Requisitos](#3-requisitos)
4. [Problemas de diseño](#4-problemas-de-diseño)
5. [Patrones de diseño elegidos](#5-patrones-de-diseño-elegidos)
6. [Arquitectura general](#6-arquitectura-general)
7. [Diagramas UML](#7-diagramas-uml)
8. [Documento de diseño por patrón](#8-documento-de-diseño-por-patrón)
9. [Conclusiones (preliminares)](#9-conclusiones-preliminares)
10. [Organización del equipo y declaración de uso de IA](#10-organización-del-equipo-y-declaración-de-uso-de-ia)

---

## 1. Introducción

TiendaDP es el sistema de compras en línea que el grupo construirá para el parcial de Patrones de Diseño (propuesta 1, Tienda en línea). Funciona por consola y con una interfaz gráfica sencilla en Swing, y ambas comparten el mismo núcleo de lógica de negocio. Está escrito en Java 17.

Este documento reúne todo lo que el parcial pide fuera del código: el alcance, los requisitos, los problemas de diseño, los patrones elegidos con su justificación, la arquitectura, los diagramas UML y el documento de diseño de cada patrón. El código fuente, las pruebas unitarias y el README viven en el repositorio Git del proyecto.

El documento se escribe antes de programar, como pide el enunciado para la primera revisión con la docente. Por eso los nombres de clases, métodos y relaciones son el diseño planeado. Cuando el código esté terminado se verificarán uno por uno contra los diagramas, y si algo cambia se actualiza aquí.

## 2. Alcance

La propuesta 1 da una lista de opciones, no una lista cerrada. La tabla muestra qué decidió el grupo implementar y qué deja fuera, con su razón.

| Funcionalidad | Decisión | Razón |
| --- | --- | --- |
| Catálogo con tres categorías (ropa, tecnología, alimentos), cada una con atributos propios | **Incluida** | Es la primera zona de variación: cada categoría valida y guarda datos distintos. |
| Carrito de compras (agregar, quitar, cambiar cantidad) | **Incluida** | Es el centro del flujo de compra y de él dependen descuentos y extras. |
| Descuentos por cantidad, por temporada, por cliente frecuente y por cupón, algunos acumulables | **Incluida** (los cuatro) | Segunda zona de variación, con reglas de acumulación que cambian con frecuencia. |
| Medios de pago simulados: tarjeta, transferencia y contra entrega | **Incluida** (los tres) | Tercera zona de variación: cada medio tiene validaciones y recargos distintos. |
| Extras sobre un producto: garantía extendida, empaque de regalo y envío express | **Incluida** (los tres) | Cuarta zona de variación: los extras se combinan libremente. |
| Aviso a clientes cuando un producto agotado vuelve a tener existencias | **Incluida** | Quinta zona de cambio: quién se entera y cómo debe poder crecer sin tocar el inventario. |
| Historial de pedidos y factura | **Incluida** | La factura se genera como texto formateado y se muestra en consola y en ventana. |
| Modo administrador (registrar productos, reponer stock, crear cupones) | **Incluida**, sin contraseña | Hace falta para demostrar las categorías y el aviso de reposición. La autenticación real no aporta a los patrones. |
| Persistencia en base de datos o archivos | **Fuera** | Los datos viven en memoria durante la ejecución. El acceso a datos queda detrás de una interfaz Repositorio para poder cambiarlo después. |
| Registro de usuarios con contraseña | **Fuera** | El cliente se elige o se crea con solo un nombre. No agrega variación de diseño. |
| Pasarelas de pago reales | **Fuera** | El enunciado pide medios de pago simulados. |
| Logística de envíos, devoluciones y cancelaciones | **Fuera** | El envío express es solo un extra con costo. Lo demás amplía el sistema sin sumar variación nueva. |
| Correo o SMS reales para los avisos | **Fuera** | El aviso llega a una bandeja de avisos del cliente, visible en consola y en la GUI. |
| Aplicación web | **Fuera** | El enunciado dice que no es obligatoria ni da puntos adicionales. |

**Zonas con variación o cambio frecuente.** El sistema tiene cinco, más de las tres que exige el enunciado: las categorías de producto, los descuentos, los medios de pago, los extras y los avisos de reposición.

**Supuestos.** Los precios se manejan en pesos colombianos (COP), con IVA incluido, y los cálculos con `BigDecimal`. Los valores concretos de porcentajes y recargos que aparecen en este documento son una propuesta inicial del grupo y se definen como constantes configurables en el código.

## 3. Requisitos

### 3.1 Requisitos funcionales

| ID | Requisito |
| --- | --- |
| RF-01 | Registrar productos indicando categoría y los atributos propios de esa categoría: ropa (talla, color), tecnología (marca, meses de garantía) y alimentos (fecha de vencimiento, si requiere refrigeración). Todo producto tiene código, nombre y precio base. |
| RF-02 | Consultar el catálogo completo y filtrarlo por categoría. |
| RF-03 | Agregar productos al carrito, cambiar su cantidad y quitarlos. No se permiten cantidades mayores a las existencias. |
| RF-04 | Agregar extras a un producto del carrito, uno o varios a la vez. La garantía extendida solo aplica a tecnología, el empaque de regalo a ropa y tecnología, y el envío express a cualquier categoría. Cada extra suma su costo por unidad. |
| RF-05 | Calcular automáticamente los descuentos aplicables: por cantidad (10 % desde 5 unidades del mismo producto), por temporada (15 % sobre una categoría dentro de un rango de fechas configurable), por cliente frecuente (5 % desde 3 pedidos previos) y por cupón (código con valor fijo y vigencia). |
| RF-06 | Regla de acumulación: los descuentos de cliente frecuente y de cupón son acumulables. Los de cantidad y de temporada no lo son entre sí, y de ellos solo se aplica el mayor. El descuento total no puede superar el 50 % del subtotal de productos. Los descuentos se calculan sobre el precio base de los productos, no sobre los extras. |
| RF-07 | Pagar con tres medios simulados. **Tarjeta:** número de 16 dígitos, cuotas de 1 a 36 y recargo del 3 % si hay más de una cuota. Las tarjetas que terminan en 0000 se rechazan, para poder simular el rechazo. **Transferencia:** exige número de referencia y no tiene recargo. **Contra entrega:** solo para pedidos de hasta $1.000.000 y con recargo fijo de $5.000. |
| RF-08 | Cerrar el pedido: verificar existencias, calcular el total, procesar el pago, descontar el inventario, generar la factura y vaciar el carrito. Si el pago se rechaza, el pedido no se confirma, el inventario no cambia y el cliente puede reintentar con otro medio. |
| RF-09 | Generar la factura con número consecutivo, fecha, productos con sus extras, cada descuento aplicado con su valor, el recargo del medio de pago y el total. |
| RF-10 | Consultar el historial de pedidos de un cliente y ver la factura de cada uno. |
| RF-11 | Suscribirse al aviso de un producto agotado y cancelar esa suscripción. |
| RF-12 | Al reponer existencias de un producto agotado, avisar a todos los clientes suscritos. El aviso queda en la bandeja de avisos del cliente. |
| RF-13 | La consola y la interfaz gráfica ofrecen las mismas operaciones y usan exactamente el mismo núcleo. |
| RF-14 | En modo administrador: registrar productos, reponer existencias y crear cupones. |

### 3.2 Requisitos no funcionales

| ID | Requisito | Cómo se verifica |
| --- | --- | --- |
| RNF-01 | **Extensibilidad.** Agregar una categoría, un descuento, un medio de pago o un extra debe hacerse creando una clase nueva y registrándola en un solo lugar, sin modificar las clases existentes. | Cambio de requisito en vivo y prueba de agregar una variante. |
| RNF-02 | **Separación de capas.** El núcleo no depende de la consola ni de Swing. Ambas interfaces solo hablan con `TiendaServicio`. | Revisión de dependencias entre paquetes. |
| RNF-03 | **Exactitud monetaria.** Todo valor en dinero se maneja con `BigDecimal`, nunca con `double`. | Pruebas unitarias de cálculo de totales. |
| RNF-04 | **Testabilidad.** Pruebas unitarias con JUnit 5 para las clases de cada patrón, con una meta de cobertura del 70 % en los paquetes de patrones. | Informe de cobertura del build. |
| RNF-05 | **Mantenibilidad.** Paquetes con una responsabilidad clara, clases pequeñas y Javadoc en las interfaces de los patrones. | Revisión de código entre integrantes. |
| RNF-06 | **Usabilidad.** Menú de consola con validación de entradas y mensajes claros. GUI en Swing con pestañas para catálogo, carrito, pedidos y administración. | Prueba manual de los flujos principales. |
| RNF-07 | **Portabilidad.** Java 17, Maven y sin dependencias externas salvo JUnit. Debe ejecutar en Windows, Linux y macOS. | Compilación en los equipos del grupo. |
| RNF-08 | **Desempeño.** Cada operación responde en menos de un segundo con un catálogo de hasta 1.000 productos en memoria. | Prueba con catálogo de muestra grande. |
| RNF-09 | **Trazabilidad.** Repositorio Git con commits de cada integrante y README con instrucciones de compilación, ejecución y funcionalidades. | Historial de commits. |

## 4. Problemas de diseño

Son cinco situaciones del sistema en las que un diseño ingenuo produciría código rígido, repetido o difícil de ampliar. Cada una se describe con un ejemplo concreto de TiendaDP.

### 4.1 Problema 1: productos de categorías con atributos distintos

**Situación.** Cada categoría de producto guarda y valida datos diferentes, y el sistema debe poder registrar productos de cualquiera de ellas.

**Ejemplo concreto.** Una camiseta necesita talla y color, un portátil necesita marca y meses de garantía, y un yogur necesita fecha de vencimiento y si requiere frío. Mañana el negocio podría vender libros, con autor y número de páginas.

**Diseño ingenuo y su consecuencia.** Una sola clase `Producto` con todos los atributos posibles, la mayoría vacíos según el caso, y un `switch` sobre la categoría tanto al crear como al validar. Cada categoría nueva obliga a tocar `Producto`, el `switch` y los menús, y la clase termina llena de campos que no significan nada para la mayoría de los productos.

### 4.2 Problema 2: descuentos con reglas distintas y acumulación

**Situación.** Hay varios tipos de descuento, cada uno con su propia condición y su propio cálculo, y algunos se pueden sumar con otros y otros no.

**Ejemplo concreto.** Un cliente frecuente compra 6 camisetas de $50.000 en temporada de ropa y usa el cupón BIENVENIDO de $10.000. Le corresponden el descuento por cantidad (10 %), el de temporada (15 %), el de cliente frecuente (5 %) y el cupón. Cantidad y temporada no se acumulan entre sí, mientras que cliente frecuente y cupón sí.

**Diseño ingenuo y su consecuencia.** Un método `calcularTotal()` en `Carrito` con `if` anidados para cada tipo de descuento y cada combinación posible. Agregar un descuento nuevo, por ejemplo uno de cumpleaños, obliga a editar ese método y a revisar todas las combinaciones. Los errores de acumulación son difíciles de ver y de probar.

### 4.3 Problema 3: medios de pago con reglas distintas

**Situación.** Cada medio de pago valida datos diferentes, cobra recargos distintos y puede rechazar el pago por motivos propios.

**Ejemplo concreto.** La tarjeta exige 16 dígitos, cobra 3 % si hay cuotas y se rechaza si termina en 0000. La transferencia exige un número de referencia. El pago contra entrega solo se permite hasta $1.000.000 y cobra $5.000 fijos. Si la tarjeta es rechazada, el cliente debe poder reintentar con transferencia sobre el mismo pedido.

**Diseño ingenuo y su consecuencia.** Un método `pagar()` con un `switch` sobre el tipo de pago, o subclases del pedido como `PedidoConTarjeta` y `PedidoContraEntrega`. El `switch` crece y mezcla reglas que no se parecen. Las subclases fijan el medio de pago al crear el pedido, así que no permiten cambiarlo tras un rechazo.

### 4.4 Problema 4: extras combinables sobre un producto

**Situación.** El cliente puede añadir a un producto cualquier combinación de extras, y cada extra cambia el precio y la descripción de lo que se vende.

**Ejemplo concreto.** Un portátil de $3.200.000 con garantía extendida (12 % del precio base, $384.000) y envío express ($15.000) cuesta $3.599.000. Una camiseta de $50.000 con empaque de regalo ($8.000) y envío express cuesta $73.000.

**Diseño ingenuo y su consecuencia.** Crear una subclase por cada combinación (`ProductoConGarantiaYExpress`, `ProductoConRegaloYExpress` y así) produce una explosión de clases: con tres extras ya son ocho combinaciones por categoría. La otra salida, banderas booleanas dentro de `Producto`, hace que `Producto` conozca todos los extras y que cambie cada vez que aparece uno nuevo.

### 4.5 Problema 5: avisar a los clientes cuando vuelve un producto agotado

**Situación.** Cuando un producto agotado recibe existencias, todos los clientes interesados deben enterarse, y el inventario no debería saber quiénes son ni cómo se les avisa.

**Ejemplo concreto.** Los audífonos «AirSound X» están agotados y tres clientes se suscribieron al aviso. El administrador repone 20 unidades y los tres deben recibir el aviso en su bandeja, sin que el inventario conozca los detalles de cada cliente.

**Diseño ingenuo y su consecuencia.** Que el inventario guarde una lista de clientes y llame directamente a cada uno, o que cada cliente consulte el stock una y otra vez. En el primer caso el inventario queda atado a la clase `Cliente` y al canal de aviso, y cualquier canal nuevo lo obliga a cambiar. En el segundo se gastan consultas inútiles y el aviso llega tarde.

## 5. Patrones de diseño elegidos

Cada problema se resuelve con un patrón distinto. Strategy aparece en dos problemas porque ambos son familias de algoritmos intercambiables. Los patrones pertenecen a tres categorías, más de las dos que exige el enunciado: creacional (Factory Method), estructural (Decorator) y de comportamiento (Strategy y Observer).

| Problema | Patrón y categoría | Alternativa descartada | Consecuencia principal |
| --- | --- | --- | --- |
| 4.1 Productos por categoría | **Factory Method** (creacional) | Fábrica simple con `switch` sobre la categoría | Cada categoría nueva es una clase creadora y un producto nuevos, a costa de más clases. |
| 4.2 Descuentos | **Strategy** (comportamiento) | `if` anidados en `Carrito`, o Chain of Responsibility | Cada descuento es independiente y fácil de probar. La regla de acumulación queda centralizada en un solo lugar. |
| 4.3 Medios de pago | **Strategy** (comportamiento) | `switch` en `pagar()`, o subclases de `Pedido` por medio de pago | El medio de pago se elige y se cambia en tiempo de ejecución, por ejemplo tras un rechazo. |
| 4.4 Extras | **Decorator** (estructural) | Una subclase por combinación, o banderas booleanas en `Producto` | Los extras se combinan libremente, pero el producto envuelto ya no es el mismo objeto. |
| 4.5 Aviso de reposición | **Observer** (comportamiento) | `Inventario` que llama a cada cliente, o consulta periódica | Inventario y clientes quedan desacoplados, pero el orden de los avisos no está garantizado. |

## 6. Arquitectura general

TiendaDP se organiza en cuatro capas. La lógica de negocio vive en el dominio y se expone a través de una única clase, `TiendaServicio`. La consola y la interfaz gráfica son dos clientes de esa clase, y ninguna de las dos contiene reglas de negocio.

![Figura 1. Arquitectura por capas de TiendaDP. Las dos interfaces comparten el mismo núcleo a través de TiendaServicio.](docs/img/figura-1.png)

| Paquete | Contenido | Patrón |
| --- | --- | --- |
| `tiendadp.ui.consola` | `AppConsola`: menús, lectura de datos y presentación de resultados por consola. | - |
| `tiendadp.ui.gui` | `AppGui`: ventana Swing con pestañas de catálogo, carrito, pedidos y administración. | - |
| `tiendadp.servicio` | `TiendaServicio`: operaciones de la tienda (registrar producto, agregar al carrito, cerrar pedido, reponer stock, historial). | - |
| `tiendadp.modelo` | `Producto` y sus subclases, `Cliente`, `Carrito`, `ItemCarrito`, `Pedido`, `Factura`. | - |
| `tiendadp.fabrica` | `ProductoCreator` y sus creadores concretos por categoría. | Factory Method |
| `tiendadp.extras` | `Vendible`, `ExtraDecorator` y los tres extras. | Decorator |
| `tiendadp.descuentos` | `Descuento`, los cuatro descuentos y `CalculadoraDescuentos`. | Strategy |
| `tiendadp.pagos` | `MedioPago` y los tres medios de pago simulados. | Strategy |
| `tiendadp.inventario` | `SujetoStock`, `ObservadorStock` e `Inventario`. | Observer |
| `tiendadp.repositorio` | Interfaz `Repositorio<T>` y su implementación en memoria. | - |

**Cómo se comparte el núcleo.** Las dos interfaces llaman a los mismos métodos de `TiendaServicio`, que recibe y devuelve objetos del dominio (`Pedido`, `Factura`, listas de productos) y nunca imprime ni abre ventanas. Cada interfaz decide cómo mostrar esos objetos. Los avisos de reposición se guardan en la bandeja del `Cliente`, y cada interfaz la lee cuando quiere mostrarla, así que el núcleo no depende de ninguna de las dos.

**Raíz de composición.** La clase `Main` arma el núcleo en un solo lugar: crea el inventario, el repositorio, el mapa de creadores por categoría, la lista de descuentos y los medios de pago, y se los entrega a `TiendaServicio`. Luego inicia la consola o la GUI según un argumento de línea de comandos. Es también el único sitio donde se registra una variante nueva, lo que cumple RNF-01.

## 7. Diagramas UML

Esta sección reúne los diagramas generales: casos de uso, clases general y secuencia. Los diagramas de clases de cada patrón, con los roles señalados sobre las clases reales del proyecto, están en la sección 8, junto al análisis de cada patrón. En ellos, los recuadros azules son clases que cumplen un rol del patrón y los grises son clases del proyecto que participan sin ser un rol.

Los diagramas muestran el diseño planeado. Antes de la entrega se compararán con el código y se actualizarán si hay diferencias, porque el enunciado exige que coincidan.

### 7.1 Diagrama de casos de uso

Hay dos actores. El cliente compra, consulta su historial y se suscribe a avisos. El administrador gestiona el catálogo, las existencias y los cupones. Cerrar pedido incluye calcular descuentos, procesar el pago y generar la factura. Aplicar cupón extiende el cálculo de descuentos, y notificar a los clientes extiende la reposición de existencias porque solo ocurre cuando el producto estaba agotado.

![Figura 2. Diagrama de casos de uso de TiendaDP.](docs/img/figura-2.png)

### 7.2 Diagrama de clases general

Muestra todas las clases del sistema agrupadas por responsabilidad y cómo se relacionan. Por legibilidad, las clases concretas de cada patrón aparecen solo con su nombre. El detalle de atributos y métodos está en los diagramas de la sección 8.

![Figura 3. Diagrama de clases general de TiendaDP.](docs/img/figura-3.png)

### 7.3 Diagramas de secuencia: cerrar un pedido

Es el flujo principal del sistema y en él participan tres patrones. Se divide en dos diagramas para que sean legibles. El primero cubre el cálculo del total: cada ítem pide su precio a un `Vendible` decorado (Decorator) y luego `CalculadoraDescuentos` recorre las estrategias de descuento y aplica la regla de acumulación (Strategy).

![Figura 4. Cierre de un pedido, parte 1: cálculo del total y de los descuentos.](docs/img/figura-4.png)

El segundo diagrama continúa con el pago. El pedido delega el cobro en el `MedioPago` elegido (Strategy). Si el pago se aprueba, se descuenta el inventario y se genera la factura. Si se rechaza, el inventario no se toca y el cliente puede reintentar con otro medio.

![Figura 5. Cierre de un pedido, parte 2: pago, inventario y factura.](docs/img/figura-5.png)

### 7.4 Diagrama de secuencia: aviso de reposición

Muestra el patrón Observer en acción. Los clientes ya estaban suscritos al producto agotado. Cuando el administrador repone existencias y el stock pasa de 0 a más de 0, el inventario notifica a cada suscrito sin conocer nada de ellos más que la interfaz `ObservadorStock`.

![Figura 6. Reposición de existencias y aviso a los clientes suscritos.](docs/img/figura-6.png)

## 8. Documento de diseño por patrón

Para cada patrón se describe el problema que lo motivó, el patrón con palabras propias, la correspondencia entre sus roles y las clases del proyecto, la alternativa que se consideró, y sus ventajas, desventajas y relación con los principios SOLID. También se indican las pruebas unitarias previstas.

### 8.1 Factory Method (creacional): creación de productos

#### Problema concreto

Es el problema 4.1. TiendaDP vende ropa, tecnología y alimentos, y cada categoría exige datos y validaciones distintos al crear un producto. Los menús y el servicio no deberían saber cómo se construye cada una.

#### Descripción del patrón

Factory Method deja que una clase base declare un método para crear objetos, pero que cada subclase decida qué clase concreta crea. Quien usa la clase base trabaja con el resultado a través de un tipo común y nunca nombra la clase concreta. En TiendaDP, `ProductoCreator` tiene el método público `crear(datos)`, que valida lo común a todos (código, nombre y precio positivo) y después llama al método abstracto `fabricarProducto(datos)`. Cada creador concreto implementa ese método con las reglas de su categoría. Además, `atributosRequeridos()` permite a la consola y a la GUI construir sus formularios sin conocer las categorías.

#### Correspondencia entre roles y clases

| Rol del patrón | Clase del proyecto | Responsabilidad |
| --- | --- | --- |
| Creator | `ProductoCreator` (abstracta) | Declara el método fábrica `fabricarProducto()` y ejecuta la validación común dentro de `crear()`. |
| ConcreteCreator | `RopaCreator`, `TecnologiaCreator`, `AlimentoCreator` | Validan los atributos de su categoría y construyen el producto correspondiente. |
| Product | `Producto` (abstracta) | Tipo común con el que trabaja el resto del sistema. |
| ConcreteProduct | `Ropa`, `Tecnologia`, `Alimento` | Guardan los atributos propios de cada categoría. |
| Cliente del patrón | `TiendaServicio` | Busca el creador de la categoría en un mapa y llama a `crear()`, sin conocer clases concretas. |

![Figura 7. Factory Method: roles sobre las clases del proyecto.](docs/img/figura-7.png)

#### Alternativa considerada

Una fábrica simple, es decir, una clase `ProductoFactory` con un método estático y un `switch` sobre la categoría. Se descartó porque cada categoría nueva obliga a editar esa clase, que además acumularía las validaciones de todas las categorías. También se pensó en Abstract Factory, pero solo hay una familia de productos, no varias familias de objetos relacionados, y esa complejidad no se justifica.

#### Ventajas

- Agregar una categoría (por ejemplo, libros) es crear un creador y un producto nuevos y registrarlos en `Main`, sin modificar nada existente.
- Las validaciones propias de cada categoría quedan aisladas, y la validación común se escribe una sola vez en el Creator.
- `TiendaServicio` solo conoce tipos abstractos, así que se puede probar con creadores falsos.

#### Desventajas

- Aumenta el número de clases: tres creadores y tres productos para tres categorías.
- Hay que acordarse de registrar el creador nuevo en el mapa de `Main`, y si se olvida la categoría simplemente no existe.

#### Relación con SOLID

- **Abierto/cerrado (O):** se agregan categorías sin modificar las existentes.
- **Responsabilidad única (S):** cada creador conoce las reglas de una sola categoría.
- **Inversión de dependencias (D):** `TiendaServicio` depende de `ProductoCreator`, no de clases concretas.
- **Sustitución de Liskov (L):** cualquier `Producto` concreto puede usarse donde se espera un `Producto`.

#### Pruebas unitarias previstas

- Cada creador devuelve una instancia del tipo correcto con datos válidos.
- Faltan atributos obligatorios, o el precio es negativo: se lanza una excepción.
- `atributosRequeridos()` devuelve los campos esperados de cada categoría.

### 8.2 Decorator (estructural): extras de un producto

#### Problema concreto

Es el problema 4.4. Garantía extendida, empaque de regalo y envío express se pueden combinar sobre un mismo producto, y cada uno modifica el precio y la descripción.

#### Descripción del patrón

Decorator envuelve un objeto dentro de otro que implementa la misma interfaz. El envoltorio hace su propio trabajo (aquí, sumar un costo y añadir texto a la descripción) y luego delega en el objeto envuelto. Como los envoltorios son también del mismo tipo, se pueden apilar en cualquier combinación y en tiempo de ejecución. En TiendaDP, un `Producto` es un `Vendible`, y cada extra es un `ExtraDecorator` que recibe otro `Vendible`. El precio final sale de la cadena completa: el producto aporta su precio base y cada extra agrega el suyo. La garantía se calcula como 12 % del precio base del producto, y el empaque y el envío son valores fijos, de modo que el orden de apilado no cambia el resultado.

#### Correspondencia entre roles y clases

| Rol del patrón | Clase del proyecto | Responsabilidad |
| --- | --- | --- |
| Component | `Vendible` (interfaz) | Define `getDescripcion()`, `getPrecio()` y `getProducto()`. |
| ConcreteComponent | `Producto` y sus subclases | Es el objeto base que se decora. Devuelve su precio base. |
| Decorator | `ExtraDecorator` (abstracta) | Guarda el `Vendible` envuelto y suma `costoExtra()` a su precio. |
| ConcreteDecorator | `GarantiaExtendida`, `EmpaqueRegalo`, `EnvioExpress` | Cada uno define su costo con `costoExtra()` y su texto. |
| Cliente del patrón | `ItemCarrito` y `TiendaServicio` | `ItemCarrito` guarda un `Vendible` y `TiendaServicio` lo envuelve al agregar un extra. |

![Figura 8. Decorator: roles sobre las clases del proyecto.](docs/img/figura-8.png)

#### Alternativa considerada

Había dos opciones. La primera era una subclase por cada combinación, que se descartó porque con tres extras son ocho combinaciones por categoría y cada extra nuevo duplica la cantidad. La segunda era banderas booleanas dentro de `Producto` con `if` en `getPrecio()`, que se descartó porque `Producto` tendría que conocer todos los extras y cambiaría cada vez que apareciera uno.

#### Ventajas

- Los extras se combinan libremente y se deciden en tiempo de ejecución, según lo que elija el cliente.
- Un extra nuevo es una clase nueva, sin tocar `Producto` ni los demás extras.
- Cada regla de precio de un extra queda en su propia clase.

#### Desventajas

- Un producto decorado no es el mismo objeto que el producto original, así que no se puede usar `instanceof` ni comparar identidades. Por eso `Vendible` expone `getProducto()`, que usan el inventario y los descuentos.
- Aparecen muchos objetos pequeños y, al depurar, hay que recorrer la cadena de envoltorios.
- Si algún día un extra dependiera del precio acumulado, el orden de apilado importaría. Se evita calculando la garantía sobre el precio base.

#### Relación con SOLID

- **Abierto/cerrado (O):** se extiende el comportamiento sin modificar las clases existentes.
- **Responsabilidad única (S):** cada decorador se ocupa de un solo extra.
- **Liskov (L):** un decorador puede usarse donde se espera un `Vendible` sin que el cliente lo note.
- **Segregación de interfaces (I):** `Vendible` es una interfaz pequeña de tres métodos.

#### Pruebas unitarias previstas

- Precio y descripción de un producto sin extras.
- Precio con cada extra por separado: garantía del 12 %, empaque y envío.
- Combinaciones de dos y tres extras en distinto orden dan el mismo precio.
- `getProducto()` devuelve el producto original a través de varias capas.

### 8.3 Strategy (comportamiento): descuentos

#### Problema concreto

Es el problema 4.2. Los cuatro descuentos tienen condiciones y cálculos distintos, y la regla de acumulación decide cuáles se suman.

#### Descripción del patrón

Strategy encapsula cada variante de un algoritmo en su propia clase, todas detrás de una misma interfaz, para que el código que las usa no dependa de ninguna en particular. En TiendaDP, cada descuento implementa `Descuento`: dice si aplica a un carrito y cliente, cuánto vale y si es acumulable. `CalculadoraDescuentos` guarda la lista de estrategias, pregunta a cada una y aplica la regla de acumulación. Es una variante del patrón en la que el contexto mantiene varias estrategias a la vez en lugar de elegir una sola, porque en un mismo pedido pueden aplicar varios descuentos.

**Ejemplo con valores reales.** Seis camisetas de $50.000 dan un subtotal de $300.000. El descuento por cantidad vale $30.000 y el de temporada $45.000. No son acumulables entre sí, así que solo cuenta el mayor, $45.000. El de cliente frecuente suma $15.000 y el cupón $10.000. El descuento total es $70.000, menor que el tope de $150.000 (50 %), y el total queda en $230.000.

#### Correspondencia entre roles y clases

| Rol del patrón | Clase del proyecto | Responsabilidad |
| --- | --- | --- |
| Strategy | `Descuento` (interfaz) | Contrato común: `getNombre()`, `esAcumulable()`, `esAplicable()` y `calcular()`. |
| ConcreteStrategy | `DescuentoPorCantidad`, `DescuentoTemporada`, `DescuentoClienteFrecuente`, `DescuentoCupon` | Cada una implementa su condición y su cálculo. |
| Context | `CalculadoraDescuentos` | Guarda la lista de estrategias, aplica la regla de acumulación y el tope, y devuelve un `ResultadoDescuentos` detallado. |
| Cliente del patrón | `TiendaServicio` | Pide el cálculo al cerrar el pedido. |

![Figura 9. Strategy aplicado a los descuentos: roles sobre las clases del proyecto.](docs/img/figura-9.png)

#### Alternativa considerada

Se descartaron dos. Los `if` anidados dentro de `Carrito.calcularTotal()` se descartaron por el problema descrito en 4.2. Chain of Responsibility, donde cada descuento pasa el carrito al siguiente, se descartó porque la regla de acumulación necesita ver todos los candidatos a la vez para elegir el mayor entre los no acumulables, y una cadena procesa de a uno, haría que el orden importara y esconde la regla.

#### Ventajas

- Un descuento nuevo es una clase nueva que se registra en `Main`, sin tocar el resto.
- Cada descuento se puede probar solo, sin armar un carrito completo con todos los demás.
- La regla de acumulación y el tope están en un único lugar.

#### Desventajas

- Si las reglas de combinación se vuelven más complejas que acumulable o no acumulable, `CalculadoraDescuentos` podría crecer y habría que repensarla.
- Hay más clases que con un simple método, y quien configura el sistema debe saber qué estrategias registrar.

#### Relación con SOLID

- **Abierto/cerrado (O):** agregar descuentos no modifica a `CalculadoraDescuentos`.
- **Responsabilidad única (S):** cada clase conoce una sola regla.
- **Inversión de dependencias (D):** la calculadora depende de la interfaz `Descuento`.
- **Segregación de interfaces (I):** la interfaz solo tiene lo que cada descuento necesita.

#### Pruebas unitarias previstas

- Cada descuento por separado: cuando aplica, cuando no y cuánto vale.
- Cantidad y temporada juntos: solo se aplica el mayor.
- Cliente frecuente, cupón y uno de los no acumulables: se suman.
- El tope del 50 % se respeta. El ejemplo de $300.000 da un total de $230.000.

### 8.4 Strategy (comportamiento): medios de pago

#### Problema concreto

Es el problema 4.3. Tarjeta, transferencia y contra entrega validan, cobran recargos y rechazan de maneras distintas, y el cliente debe poder cambiar de medio tras un rechazo.

#### Descripción del patrón

Es el mismo patrón de la sección anterior aplicado a otro problema: cada algoritmo vive en su propia clase detrás de una interfaz común, y el contexto usa la que se le entregue. Aquí el contexto es el `Pedido`. Al pagar, recibe un `MedioPago` y los datos del pago. Primero pide validar los datos, luego calcula el recargo, cobra el total más el recargo y guarda el resultado. Como el medio se entrega al momento de pagar y no al crear el pedido, un pedido rechazado con tarjeta puede reintentarse con transferencia.

#### Correspondencia entre roles y clases

| Rol del patrón | Clase del proyecto | Responsabilidad |
| --- | --- | --- |
| Strategy | `MedioPago` (interfaz) | Contrato común: `getNombre()`, `validar()`, `calcularRecargo()` y `pagar()`. |
| ConcreteStrategy | `PagoTarjeta`, `PagoTransferencia`, `PagoContraEntrega` | Tarjeta valida los 16 dígitos, cobra 3 % en cuotas y rechaza las terminadas en 0000. Transferencia exige referencia. Contra entrega aplica el tope y el recargo fijo. |
| Context | `Pedido` | Orquesta validar, recargo y cobro con el medio que reciba, y lleva el estado del pedido. |
| Cliente del patrón | `TiendaServicio` | Elige el medio según la opción del cliente y llama a `pedido.pagar()`. |

![Figura 10. Strategy aplicado a los medios de pago: roles sobre las clases del proyecto.](docs/img/figura-10.png)

#### Alternativa considerada

Un `switch` dentro de `pagar()`, que se descartó porque mezcla reglas sin relación entre sí y crece con cada medio nuevo. También se descartaron las subclases del pedido (`PedidoConTarjeta` y similares), porque el medio quedaría fijado al crear el pedido, no se podría cambiar tras un rechazo y se mezclaría la idea de pedido con la de cobro.

#### Ventajas

- Agregar un medio de pago (por ejemplo, billetera digital) es una clase nueva, sin tocar `Pedido`.
- El medio puede cambiarse en tiempo de ejecución, lo que resuelve el reintento tras un rechazo.
- Las reglas de cada medio se prueban por separado.

#### Desventajas

- Los datos de pago difieren según el medio (tarjeta, referencia, dirección), así que `DatosPago` es un contenedor genérico de campos y cada estrategia debe validar los que necesita.
- Es otra familia de clases que mantener, aunque cada una sea pequeña.

#### Relación con SOLID

- **Abierto/cerrado (O):** nuevos medios sin modificar `Pedido`.
- **Responsabilidad única (S):** cada medio conoce solo sus propias reglas.
- **Inversión de dependencias (D):** `Pedido` depende de `MedioPago`, no de clases concretas.
- **Liskov (L):** cualquier medio puede sustituir a otro sin que `Pedido` lo note.

#### Pruebas unitarias previstas

- Tarjeta: número válido y no válido, recargo del 3 % con cuotas, rechazo de tarjetas terminadas en 0000.
- Transferencia: rechazo sin referencia.
- Contra entrega: rechazo por encima de $1.000.000 y recargo fijo.
- Pedido rechazado con tarjeta que luego se paga con transferencia.

### 8.5 Observer (comportamiento): aviso de reposición

#### Problema concreto

Es el problema 4.5. Cuando un producto agotado recupera existencias, los clientes suscritos deben enterarse sin que el inventario conozca sus detalles.

#### Descripción del patrón

Observer define una relación de uno a muchos: un objeto, el sujeto, avisa automáticamente a una lista de observadores cuando algo cambia, y solo conoce de ellos una interfaz. Aquí el sujeto es el `Inventario` y los observadores son los clientes. Un cliente se suscribe a un código de producto. Cuando `reponer()` lleva el stock de ese producto de 0 a más de 0, el inventario llama a `productoDisponible()` de cada suscrito, y el cliente deja el aviso en su bandeja. Se usa el modelo en que el sujeto envía el dato (el producto) al avisar.

#### Correspondencia entre roles y clases

| Rol del patrón | Clase del proyecto | Responsabilidad |
| --- | --- | --- |
| Subject | `SujetoStock` (interfaz) | Declara `suscribir()` y `cancelar()` por código de producto. |
| ConcreteSubject | `Inventario` | Guarda existencias y suscriptores, y notifica cuando un producto pasa de agotado a disponible. |
| Observer | `ObservadorStock` (interfaz) | Declara `productoDisponible(producto)`. |
| ConcreteObserver | `Cliente` | Implementa la interfaz y guarda el aviso en su bandeja. |

![Figura 11. Observer: roles sobre las clases del proyecto.](docs/img/figura-11.png)

#### Alternativa considerada

Se consideraron tres. Que `Inventario` guarde clientes y los llame directamente, descartado porque lo ata a `Cliente` y a la forma de avisar. Que los clientes consulten el stock repetidamente, descartado por gastar consultas y avisar tarde. Y las clases de la librería de Java: `java.util.Observable` está en desuso desde Java 9, y `PropertyChangeListener` avisa de cualquier cambio, cuando aquí solo importa el paso de agotado a disponible de un producto concreto.

#### Ventajas

- El `Inventario` no conoce a los clientes ni el canal. Un observador nuevo (por ejemplo, un notificador por correo simulado) se agrega sin modificarlo.
- Los suscritos se enteran en el momento exacto del cambio, sin consultas repetidas.

#### Desventajas

- El orden en que se avisa a los observadores no está garantizado y no debe importar.
- Si un cliente no cancela su suscripción puede quedar referenciado de más. Se mitiga eliminando la suscripción tras avisar.
- Un observador que falle no debe impedir el aviso a los demás, así que `Inventario` atrapa las excepciones de cada uno por separado.

#### Relación con SOLID

- **Abierto/cerrado (O):** nuevos observadores sin cambiar el `Inventario`.
- **Responsabilidad única (S):** `Inventario` gestiona existencias y no cómo se avisa.
- **Inversión de dependencias (D):** `Inventario` depende de `ObservadorStock`, no de `Cliente`.
- **Segregación de interfaces (I):** el observador solo tiene un método.

#### Pruebas unitarias previstas

- Un cliente suscrito recibe un aviso cuando el stock pasa de 0 a más de 0.
- No hay aviso si el producto ya tenía existencias.
- Un cliente que canceló la suscripción no recibe aviso, y uno suscrito a otro producto tampoco.
- Varios suscritos reciben cada uno su aviso.

## 9. Conclusiones (preliminares)

Esta sección se escribe antes de programar. Recoge lo que el diseño ha enseñado hasta ahora y los puntos que se podrían cambiar, y se completará con lo aprendido durante la implementación.

### Lo aprendido hasta ahora

- Conviene identificar primero el problema y después el patrón. Al describir los problemas con ejemplos concretos y números, la elección de cada patrón se volvió casi directa.
- Un mismo patrón puede resolver problemas distintos. Strategy sirve tanto para descuentos como para medios de pago, y lo que cambia es el contexto.
- Decorator tiene un costo de identidad: el producto decorado ya no es el original, por lo que fue necesario agregar `getProducto()` a la interfaz.
- Las reglas de negocio con dinero deben escribirse con ejemplos numéricos desde el diseño, para poder convertirlos en pruebas.

### Qué se cambiaría o se vigilará

- `TiendaServicio` podría convertirse en una clase demasiado grande. Si pasa, se dividirá en servicios más pequeños (catálogo, pedidos, administración), manteniendo una sola puerta de entrada para las interfaces.
- Si la regla de acumulación de descuentos se vuelve más compleja, habrá que sacarla de `CalculadoraDescuentos` a su propia clase o estrategia.
- Para construir `Pedido` y `Factura`, que tienen muchos datos, se evaluará un Builder. Solo se incluirá si resuelve un problema real y no es decorativo.
- Se evitó a propósito el patrón Singleton para el inventario y los repositorios. Se entregan por constructor desde `Main`, lo que facilita las pruebas.

## 10. Organización del equipo y declaración de uso de IA

### 10.1 Propuesta de reparto de trabajo

El enunciado exige commits propios de cada integrante y que todos puedan explicar cualquier parte del proyecto en la sustentación. Esta es una propuesta inicial que el grupo puede ajustar. El reparto no exime a nadie de conocer el resto.

| Integrante | Responsabilidad principal | Pruebas y entregables asociados |
| --- | --- | --- |
| Juan Ospina | Catálogo y productos (Factory Method), extras (Decorator), pestaña de catálogo de la GUI. | Pruebas de fábrica y extras. Diagramas de clases de esos dos patrones. |
| Juan Rodriguez | Descuentos y medios de pago (Strategy), cierre de pedido y factura. | Pruebas de descuentos y pagos. Diagramas de esos patrones y secuencia del cierre de pedido. |
| Erick Penagos | Inventario y avisos (Observer), historial, `TiendaServicio`, consola, `Main` y README. | Pruebas de inventario. Diagramas de casos de uso, de clases general y de secuencia de reposición. |

### 10.2 Declaración de uso de herramientas de IA

Conforme a las reglas de integridad académica del parcial, se declara que este borrador se elaboró con ayuda de Claude (Anthropic), usado para estructurar el documento, proponer el diseño inicial de las clases y generar los diagramas. El grupo es responsable de revisar y ajustar el contenido, y de poder explicar cada decisión de diseño en la sustentación. Esta declaración debe actualizarse si se usa IA en otras partes del proyecto, como el código o las pruebas.
