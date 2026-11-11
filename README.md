# Proyecto-Ingenieria-de-software2
# TiendaDP
Tienda en línea hecha en Java para el parcial de Patrones de Diseño de Ingeniería de Software 2 (propuesta 1). Se puede usar por consola o con una interfaz gráfica en Swing, y las dos usan la misma lógica por debajo.

**Integrantes:** Juan Ospina, Juan Rodriguez y Erick Penagos

**Entrega final:** 23 de noviembre de 2026

> Este proyecto todavía está en desarrollo. Lo que está acá es el plan que hicimos antes de programar, así que algunos nombres de clases o detalles pueden cambiar. Cuando terminemos el código lo revisamos contra esto y lo actualizamos.

---

## De qué trata

La idea es un sistema de compras con catálogo, carrito y cierre de pedido. El cliente escoge productos, les agrega extras, el sistema le calcula los descuentos, paga con un medio de pago simulado y al final se genera una factura. También hay un modo administrador para registrar productos, reponer stock y crear cupones.

Todo está en Java 17 y los precios son en pesos colombianos (COP) con IVA incluido. Para el dinero usamos `BigDecimal` y no `double`, para no tener problemas con los decimales.

---

## Alcance

La propuesta del profe trae una lista de opciones y cada grupo decide cuáles hace. Esto es lo que decidimos nosotros.

### Lo que sí vamos a hacer

- **Catálogo con tres categorías:** ropa, tecnología y alimentos. Cada una tiene sus propios atributos, y esa es la primera parte del sistema donde hay variación.
- **Carrito de compras:** agregar, quitar y cambiar cantidad. Es el centro de todo el flujo de compra, y de ahí dependen los descuentos y los extras.
- **Descuentos:** por cantidad, por temporada, por cliente frecuente y por cupón. Algunos se pueden acumular y otros no, y esas reglas pueden cambiar con el tiempo.
- **Medios de pago simulados:** tarjeta, transferencia y contra entrega. Cada uno valida cosas distintas y cobra recargos distintos.
- **Extras sobre un producto:** garantía extendida, empaque de regalo y envío express. Se pueden combinar como uno quiera.
- **Aviso de reposición:** si un producto está agotado y vuelve a tener stock, los clientes que se suscribieron reciben un aviso.
- **Historial de pedidos y factura:** la factura se genera como texto formateado y se ve en consola y en ventana.
- **Modo administrador** (sin contraseña): registrar productos, reponer stock y crear cupones. Lo necesitamos para poder demostrar las categorías y los avisos de reposición.

### Lo que dejamos por fuera

| Qué | Por qué |
|---|---|
| Base de datos o archivos | Los datos viven en memoria mientras corre el programa. Dejamos una interfaz `Repositorio` para poder cambiarlo después sin romper nada. |
| Registro de usuarios con contraseña | El cliente se elige o se crea solo con un nombre. Una contraseña no aporta nada a los patrones. |
| Pasarelas de pago reales | El enunciado pide que los pagos sean simulados. |
| Logística de envíos, devoluciones y cancelaciones | El envío express es solo un extra con costo. Lo demás agranda el sistema pero no agrega variación nueva. |
| Correos o SMS reales | Los avisos llegan a una bandeja de avisos del cliente, que se ve en consola y en la GUI. |
| Aplicación web | El enunciado dice que no es obligatoria y no da puntos extra. |

### Dónde hay variación o cambio frecuente

El enunciado pide mínimo tres zonas y nosotros tenemos cinco:

1. Las categorías de producto
2. Los descuentos
3. Los medios de pago
4. Los extras
5. Los avisos de reposición

Los porcentajes, recargos y demás valores que aparecen más abajo son una propuesta inicial nuestra. En el código van a quedar como constantes que se puedan cambiar fácil.

---

## Requisitos funcionales

**RF-01.** Registrar productos con su categoría y los atributos de esa categoría:
- Ropa: talla y color
- Tecnología: marca y meses de garantía
- Alimentos: fecha de vencimiento y si necesita refrigeración

Todo producto tiene código, nombre y precio base.

**RF-02.** Ver el catálogo completo y filtrarlo por categoría.

**RF-03.** Agregar productos al carrito, cambiar la cantidad y quitarlos. No se puede pedir más de lo que hay en existencias.

**RF-04.** Agregar extras a un producto del carrito, uno o varios a la vez. Hay reglas de dónde aplica cada uno:
- Garantía extendida: solo tecnología
- Empaque de regalo: ropa y tecnología
- Envío express: cualquier categoría

Cada extra suma su costo por cada unidad.

**RF-05.** Calcular los descuentos automáticamente:
- Por cantidad: 10 % desde 5 unidades del mismo producto
- Por temporada: 15 % sobre una categoría, dentro de un rango de fechas que se puede configurar
- Por cliente frecuente: 5 % desde 3 pedidos anteriores
- Por cupón: un código con valor fijo y fecha de vigencia

**RF-06.** Reglas de acumulación de descuentos:
- Cliente frecuente y cupón sí se acumulan.
- Cantidad y temporada no se acumulan entre ellos. Si aplican los dos, se toma solo el mayor.
- El descuento total no puede pasar del 50 % del subtotal de los productos.
- Los descuentos se calculan sobre el precio base, no sobre los extras.

**RF-07.** Pagar con tres medios simulados:
- **Tarjeta:** número de 16 dígitos, de 1 a 36 cuotas, y 3 % de recargo si es más de una cuota. Las tarjetas que terminan en 0000 se rechazan, así podemos simular un pago rechazado.
- **Transferencia:** pide un número de referencia y no tiene recargo.
- **Contra entrega:** solo para pedidos de hasta $1.000.000 y con un recargo fijo de $5.000.

**RF-08.** Cerrar el pedido. Se verifican las existencias, se calcula el total, se procesa el pago, se descuenta del inventario, se genera la factura y se vacía el carrito. Si el pago es rechazado, el pedido no se confirma, el inventario no cambia y el cliente puede intentar de nuevo con otro medio de pago.

**RF-09.** Generar la factura con número consecutivo, fecha, los productos con sus extras, cada descuento aplicado con su valor, el recargo del medio de pago y el total.

**RF-10.** Consultar el historial de pedidos de un cliente y ver la factura de cada uno.

**RF-11.** Suscribirse al aviso de un producto agotado y poder cancelar esa suscripción.

**RF-12.** Cuando se repone un producto agotado, avisar a todos los clientes suscritos. El aviso queda en la bandeja de avisos de cada cliente.

**RF-13.** La consola y la interfaz gráfica ofrecen las mismas operaciones y usan exactamente el mismo núcleo.

**RF-14.** En modo administrador: registrar productos, reponer existencias y crear cupones.

---

## Requisitos no funcionales

| ID | Requisito | Cómo lo vamos a verificar |
|---|---|---|
| RNF-01 | **Extensibilidad.** Para agregar una categoría, un descuento, un medio de pago o un extra solo se debe crear una clase nueva y registrarla en un solo lugar, sin modificar las clases que ya existen. | Probando agregar una variante nueva, como si nos pidieran un cambio en vivo. |
| RNF-02 | **Separación de capas.** El núcleo no depende de la consola ni de Swing. Las dos interfaces solo se comunican con `TiendaServicio`. | Revisando las dependencias entre paquetes. |
| RNF-03 | **Exactitud con el dinero.** Todo valor monetario se maneja con `BigDecimal`, nunca con `double`. | Pruebas unitarias del cálculo de totales. |
| RNF-04 | **Pruebas.** JUnit 5 para las clases de cada patrón, con meta de 70 % de cobertura en los paquetes de patrones. | Informe de cobertura del build. |
| RNF-05 | **Mantenibilidad.** Paquetes con una responsabilidad clara, clases pequeñas y Javadoc en las interfaces de los patrones. | Revisión de código entre los tres. |
| RNF-06 | **Usabilidad.** Menú de consola con validación de entradas y mensajes claros. GUI en Swing con pestañas para catálogo, carrito, pedidos y administración. | Prueba manual de los flujos principales. |
| RNF-07 | **Portabilidad.** Java 17 y Maven, sin dependencias externas salvo JUnit. Tiene que correr en Windows, Linux y macOS. | Compilando en los computadores de los tres. |
| RNF-08 | **Desempeño.** Cada operación responde en menos de un segundo con un catálogo de hasta 1.000 productos en memoria. | Prueba con un catálogo de muestra grande. |
| RNF-09 | **Trazabilidad.** Repositorio Git con commits de cada integrante y un README con las instrucciones de compilación, ejecución y las funcionalidades. | Historial de commits. |

---

## Cómo compilar y ejecutar

Todavía no hay código para ejecutar. Cuando esté listo, acá van a ir los pasos para compilar y correr la versión de consola y la de Swing.

## Funcionalidades implementadas

Pendiente. Esta lista se va a llenar a medida que avancemos.
