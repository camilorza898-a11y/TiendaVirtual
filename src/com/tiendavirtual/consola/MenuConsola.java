
package com.tiendavirtual.consola;

import com.tiendavirtual.servicios.*;
import com.tiendavirtual.modelo.*;
import com.tiendavirtual.descuentos.CalculadoraDescuentos;
import com.tiendavirtual.descuentos.DescuentoClienteFrecuente;
import com.tiendavirtual.descuentos.DescuentoCupon;
import com.tiendavirtual.pagos.*;
import com.tiendavirtual.descuentos.DescuentoPorCantidad;
import com.tiendavirtual.descuentos.DescuentoTemporada;
import com.tiendavirtual.modelo.Producto;
import java.util.Map;
import java.time.LocalDate;
import java.util.Scanner;
import com.tiendavirtual.notificaciones.*;
import java.util.HashMap;
import java.time.DayOfWeek;
import com.tiendavirtual.servicios.RegistroProductos;

public class MenuConsola {

    private Scanner scanner;
    private CatalogoService catalogoService;
    private InventarioService inventarioService;
    private Cliente cliente;
    private Carrito carrito;

    private CalculadoraDescuentos calculadoraDescuentos;
    private ProcesadorPagos procesadorPagos;
    private PedidoService pedidoService;
    private HistorialService historialService;
    private FacturaService facturaService;
    private TiendaService tiendaService;
    private DisponibilidadService disponibilidadService;
    private Map<Integer, ObservadorStock> misSuscripciones;

    public MenuConsola() {

        scanner = new Scanner(System.in);
        LocalDate fechaCompra = solicitarFechaCompra();

        catalogoService = new CatalogoService();
        inventarioService = new InventarioService();

        System.out.println("\n===== DATOS DEL CLIENTE =====");

        System.out.print("Ingrese su nombre completo: ");
        String nombre = scanner.nextLine();

        System.out.print("Ingrese su correo electrónico: ");
        String correo = scanner.nextLine();

        System.out.print("Ingrese su teléfono: ");
        String telefono = scanner.nextLine();

        System.out.print("Ingrese su dirección: ");
        String direccion = scanner.nextLine();

        cliente = new Cliente(
            1,
            nombre,
            correo,
            telefono,
            direccion
        );

        carrito = new Carrito(cliente);

        registrarProductos();

    calculadoraDescuentos = new CalculadoraDescuentos();

    // Descuento por cantidad
    calculadoraDescuentos.agregarDescuento(
        new DescuentoPorCantidad(5, 10)
    );

    // Navidad: del 1 al 25 de diciembre
    calculadoraDescuentos.agregarDescuento(
        new DescuentoTemporada(
            "Navidad",
            10,
            LocalDate.of(fechaCompra.getYear(), 12, 1),
            LocalDate.of(fechaCompra.getYear(), 12, 25),
            fechaCompra
        )
    );

    // San Valentín: 14 de febrero
    calculadoraDescuentos.agregarDescuento(
        new DescuentoTemporada(
            "San Valentín",
            10,
            LocalDate.of(fechaCompra.getYear(), 2, 14),
            LocalDate.of(fechaCompra.getYear(), 2, 14),
            fechaCompra
        )
    );

    // Día de la Madre: segundo domingo de mayo
    LocalDate primerDiaMayo = LocalDate.of(
        fechaCompra.getYear(), 5, 1
    );

    while (primerDiaMayo.getDayOfWeek() != DayOfWeek.SUNDAY) {
        primerDiaMayo = primerDiaMayo.plusDays(1);
    }

    LocalDate diaMadre = primerDiaMayo.plusDays(7);

    calculadoraDescuentos.agregarDescuento(
        new DescuentoTemporada(
            "Día de la Madre",
            10,
            diaMadre,
            diaMadre,
            fechaCompra
        )
    );

    // Descuento para cliente frecuente
    calculadoraDescuentos.agregarDescuento(
        new DescuentoClienteFrecuente(5, 5)
    );

            procesadorPagos = new ProcesadorPagos(
                new PagoContraEntrega("Direccion de prueba")
            );

            pedidoService = new PedidoService(
                inventarioService,
                calculadoraDescuentos,
                procesadorPagos
            );

            historialService = new HistorialService();
            facturaService = new FacturaService();

            tiendaService = new TiendaService(
                pedidoService,
                historialService,
                facturaService,
                fechaCompra
            );
            disponibilidadService = new DisponibilidadService();
            misSuscripciones = new HashMap<>();
        }
        private void agregarExtras() {

        if (carrito.estaVacio()) {
            System.out.println("El carrito esta vacio.");
            return;
        }

    carrito.mostrarCarrito();

    System.out.print("\nIngrese el ID del producto: ");
    int id = scanner.nextInt();

    Producto seleccionado = null;

    for (Producto producto : carrito.getProductos().keySet()) {
        if (producto.getId() == id) {
            seleccionado = producto;
            break;
        }
    }

    if (seleccionado == null) {
        System.out.println("Producto no encontrado en el carrito.");
        return;
    }

    System.out.println("\n===== EXTRAS DISPONIBLES =====");
    System.out.println("1. Garantia extendida (+$15000)");
    System.out.println("2. Empaque de regalo (+$5000)");
    System.out.println("3. Envio express (+$10000)");
    System.out.println("4. Volver");

    System.out.print("Seleccione un extra: ");
    int opcion = scanner.nextInt();

    switch (opcion) {

        case 1:
            carrito.agregarGarantia(seleccionado, 15000);
            System.out.println("Garantia agregada.");
            break;

        case 2:
            carrito.agregarEmpaque(seleccionado, 5000);
            System.out.println("Empaque de regalo agregado.");
            break;

        case 3:
            carrito.agregarEnvioExpress(seleccionado, 10000);
            System.out.println("Envio express agregado.");
            break;

        case 4:
            return;

        default:
            System.out.println("Opcion invalida.");
    }
}
    private LocalDate solicitarFechaCompra() {

    while (true) {
        try {
            System.out.println("\n===== FECHA DE COMPRA =====");
            System.out.print("Ingrese la fecha (AAAA-MM-DD): ");

            String fecha = scanner.nextLine();

            return LocalDate.parse(fecha);

        } catch (Exception e) {
            System.out.println(
                "Fecha inválida. Intente nuevamente. Ejemplo: 2026-12-15"
            );
        }
    }
}

        private void registrarProductos() {
            RegistroProductos.registrar(catalogoService);
        }

    public void mostrarMenu() {

        int opcion;

        do {
            System.out.println("\n================================");
            System.out.println("          TIENDA VIRTUAL");
            System.out.println("================================");
            System.out.println("1. Ver catalogo de productos");
            System.out.println("2. Buscar producto");
            System.out.println("3. Agregar producto al carrito");
            System.out.println("4. Ver y administrar carrito");
            System.out.println("5. Realizar compra");
            System.out.println("6. Consultar historial de pedidos");
            System.out.println("7. Consultar facturas");
            System.out.println("8. Salir");
            System.out.println("9. Agregar extras a productos");
            System.out.println("10. Suscribirse a notificaciones de stock");
            System.out.println("11. Cancelar suscripcion");
            System.out.println("12. Simular agotamiento de producto");
            System.out.println("13. Reponer inventario");
            System.out.println("================================");
            System.out.print("Seleccione una opcion: ");

            opcion = leerEntero();

            switch (opcion) {

                case 1:
                    catalogoService.mostrarCatalogo();
                    break;

                case 2:
                    buscarProducto();
                    break;

                case 3:
                    agregarAlCarrito();
                    break;

                case 4:
                    administrarCarrito();
                    break;

                case 5:
                    realizarCompra();
                    break;

                case 6:
                    tiendaService.consultarHistorial(cliente);
                    break;

                case 7:
                    tiendaService.consultarFacturas();
                    break;

                case 8:
                    System.out.println("Gracias por utilizar nuestra tienda.");
                    break;
                case 9:
                    agregarExtras();
                    break;    
                case 10:
                    suscribirseNotificaciones();
                    break;

                case 11:
                    cancelarNotificacion();
                    break;

                case 12:
                    simularAgotamiento();
                    break;

                case 13:
                    reponerInventario();
                    break;

                default:
                    System.out.println("Opcion no valida.");
            }

        } while (opcion != 8);
    }

    private void buscarProducto() {

        System.out.print("\nIngrese el ID del producto: ");

        int id = leerEntero();

        Producto producto = catalogoService.buscarPorId(id);

        if (producto != null) {
            producto.mostrarInformacion();
        } else {
            System.out.println("Producto no encontrado.");
        }
    }

    private void agregarAlCarrito() {

        System.out.println("\n===== AGREGAR AL CARRITO =====");

        System.out.print("Ingrese el ID del producto: ");
        int id = leerEntero();

        Producto producto = catalogoService.buscarPorId(id);

        if (producto == null) {
            System.out.println("Producto no encontrado.");
            return;
        }

        System.out.print("Ingrese la cantidad: ");
        int cantidad = leerEntero();

        if (cantidad <= 0) {
            System.out.println("Cantidad no valida.");
            return;
        }

        int cantidadEnCarrito = carrito.getProductos()
            .getOrDefault(producto, 0);

        if (!inventarioService.hayExistencias(
                producto, cantidadEnCarrito + cantidad)) {
            System.out.println("No hay suficientes existencias.");
            return;
        }

        carrito.agregarProducto(producto, cantidad);

        System.out.println("Producto agregado correctamente.");
    }

    private void administrarCarrito() {
    System.out.println("\n===== ADMINISTRAR CARRITO =====");
    carrito.mostrarCarrito();

    if (carrito.estaVacio()) {
        return;
    }

    System.out.println("\n1. Eliminar producto");
    System.out.println("2. Modificar cantidad");
    System.out.println("3. Volver");
    System.out.print("Seleccione una opcion: ");
    
    

    int opcion = leerEntero();

    switch (opcion) {
        case 1:
            System.out.print("Ingrese el ID del producto: ");
            int idEliminar = leerEntero();

            carrito.eliminarProducto(idEliminar);
            System.out.println("Producto eliminado del carrito.");
            break;

        case 2:
            System.out.print("Ingrese el ID del producto: ");
            int idModificar = leerEntero();

            Producto producto = catalogoService.buscarPorId(idModificar);

            if (producto == null
                    || !carrito.getProductos().containsKey(producto)) {
                System.out.println("El producto no esta en el carrito.");
                break;
            }

            System.out.print("Ingrese la nueva cantidad: ");
            int cantidad = leerEntero();

            if (cantidad < 0) {
                System.out.println("La cantidad no puede ser negativa.");
            } else if (cantidad == 0) {
                carrito.eliminarProducto(idModificar);
                System.out.println("Producto eliminado del carrito.");
            } else if (!inventarioService.hayExistencias(producto, cantidad)) {
                System.out.println("No hay suficiente inventario.");
            } else {
                carrito.actualizarCantidad(producto, cantidad);
                System.out.println("Cantidad actualizada correctamente.");
            }
            break;

        case 3:
            System.out.println("Volviendo al menu...");
            break;

        default:
            System.out.println("Opcion no valida.");
    }
}

    private void realizarCompra() {

    if (carrito.estaVacio()) {
        System.out.println("No puedes comprar con el carrito vacio.");
        return;
    }

    // ===== CUPON DE DESCUENTO =====

    calculadoraDescuentos.eliminarDescuentoCupon();

    scanner.nextLine();

    System.out.println("\n===== CUPON DE DESCUENTO =====");
    System.out.println("¿Tienes un cupon? (S/N)");
    String respuesta = scanner.nextLine();

    if (respuesta.equalsIgnoreCase("S")) {

        System.out.print("Ingresa el codigo del cupon: ");
        String codigo = scanner.nextLine();

        if (codigo.equalsIgnoreCase("TIENDA10")) {

            calculadoraDescuentos.agregarDescuento(
                new DescuentoCupon("TIENDA10", 10)
            );

            System.out.println("¡Cupon valido! Se aplico un 10% de descuento.");

        } else {

            System.out.println("Cupon invalido. No se aplicara descuento.");
        }
    }

    // ===== METODO DE PAGO =====

// ===== METODO DE PAGO =====

System.out.println("\n===== METODO DE PAGO =====");
System.out.println("1. Tarjeta");
System.out.println("2. Transferencia");
System.out.println("3. Contra entrega");
System.out.print("Seleccione el metodo de pago: ");

int opcion = leerEntero();
scanner.nextLine();

switch (opcion) {

    case 1:

        System.out.println("\n===== PAGO CON TARJETA =====");
        System.out.println("1. Visa");
        System.out.println("2. Mastercard");
        System.out.print("Seleccione la franquicia: ");

        int franquiciaOpcion = leerEntero();
        scanner.nextLine();

        String franquicia;

        if (franquiciaOpcion == 1) {
            franquicia = "Visa";
        } else if (franquiciaOpcion == 2) {
            franquicia = "Mastercard";
        } else {
            System.out.println("Franquicia no valida.");
            return;
        }

        System.out.println("\nSeleccione el tipo de tarjeta:");
        System.out.println("1. Debito");
        System.out.println("2. Credito");
        System.out.print("Opcion: ");

        int tipoOpcion = leerEntero();
        scanner.nextLine();

        String tipoTarjeta;

        if (tipoOpcion == 1) {
            tipoTarjeta = "Debito";
        } else if (tipoOpcion == 2) {
            tipoTarjeta = "Credito";
        } else {
            System.out.println("Tipo de tarjeta no valido.");
            return;
        }

        System.out.print("Numero de tarjeta de prueba: ");
        String numero = scanner.nextLine();

        System.out.print("Nombre del titular: ");
        String titular = scanner.nextLine();

        procesadorPagos.setMetodoPago(
            new PagoTarjeta(
                numero,
                titular,
                franquicia,
                tipoTarjeta
            )
        );

        break;

    case 2:

        System.out.println("\n===== TRANSFERENCIA =====");
        System.out.println("1. Nequi");
        System.out.println("2. Daviplata");
        System.out.println("3. Bre-B");
        System.out.print("Seleccione la plataforma: ");

        int plataformaOpcion = leerEntero();
        scanner.nextLine();

        String plataforma;

        if (plataformaOpcion == 1) {
            plataforma = "Nequi";
        } else if (plataformaOpcion == 2) {
            plataforma = "Daviplata";
        } else if (plataformaOpcion == 3) {
            plataforma = "Bre-B";
        } else {
            System.out.println("Plataforma no valida.");
            return;
        }

        System.out.print("Referencia de transferencia: ");
        String referencia = scanner.nextLine();

        procesadorPagos.setMetodoPago(
            new PagoTransferencia(plataforma, referencia)
        );

        break;

    case 3:

        System.out.print("Direccion de entrega: ");
        String direccion = scanner.nextLine();

        procesadorPagos.setMetodoPago(
            new PagoContraEntrega(direccion)
        );

        break;

    default:

        System.out.println("Metodo de pago no valido.");
        calculadoraDescuentos.eliminarDescuentoCupon();
        return;
}

    // ===== PROCESAR COMPRA =====

    Pedido pedido = tiendaService.realizarCompra(carrito);

    // Eliminar el cupon para que no se aplique en otra compra.
    calculadoraDescuentos.eliminarDescuentoCupon();

    if (pedido != null) {

        carrito = new Carrito(cliente);
        System.out.println("Gracias por tu compra.");

    } else {

        System.out.println("No fue posible completar la compra.");
    }
}

private void suscribirseNotificaciones() {

    System.out.println("\n===== SUSCRIPCION A NOTIFICACIONES =====");

    catalogoService.mostrarCatalogo();

    System.out.print("Ingrese el ID del producto: ");
    int id = leerEntero();

    Producto producto = catalogoService.buscarPorId(id);

    if (producto == null) {
        System.out.println("Producto no encontrado.");
        return;
    }

    System.out.println("\nSeleccione el medio de notificacion:");
    System.out.println("1. Correo electronico");
    System.out.println("2. Mensaje SMS");
    System.out.print("Opcion: ");

    int opcion = leerEntero();

    ObservadorStock observador;

    if (opcion == 1) {
        observador = new NotificacionCorreo(cliente.getCorreo());
    } else if (opcion == 2) {
        observador = new NotificacionSMS(cliente.getTelefono());
    } else {
        System.out.println("Opcion no valida.");
        return;
    }

    // Evitar suscripciones duplicadas del mismo cliente al producto.
    if (misSuscripciones.containsKey(id)) {
        disponibilidadService.cancelarSuscripcion(
            producto, misSuscripciones.get(id)
        );
    }

    disponibilidadService.suscribir(producto, observador);
    misSuscripciones.put(id, observador);

    System.out.println("Ya estas suscrito a las notificaciones de "
            + producto.getNombre());
}

private void cancelarNotificacion() {

    System.out.println("\n===== CANCELAR SUSCRIPCION =====");

    System.out.print("Ingrese el ID del producto: ");
    int id = leerEntero();

    Producto producto = catalogoService.buscarPorId(id);

    if (producto == null || !misSuscripciones.containsKey(id)) {
        System.out.println("No tienes una suscripcion para ese producto.");
        return;
    }

    ObservadorStock observador = misSuscripciones.get(id);

    disponibilidadService.cancelarSuscripcion(producto, observador);
    misSuscripciones.remove(id);

    System.out.println("Suscripcion cancelada.");
}

private void simularAgotamiento() {

    System.out.println("\n===== SIMULAR AGOTAMIENTO =====");

    catalogoService.mostrarCatalogo();

    System.out.print("Ingrese el ID del producto: ");
    int id = leerEntero();

    Producto producto = catalogoService.buscarPorId(id);

    if (producto == null) {
        System.out.println("Producto no encontrado.");
        return;
    }

    producto.setStock(0);

    System.out.println("El producto " + producto.getNombre()
            + " ahora tiene 0 existencias.");
    System.out.println("Producto agotado.");
}

private void reponerInventario() {

    System.out.println("\n===== REPONER INVENTARIO =====");

    catalogoService.mostrarCatalogo();

    System.out.print("Ingrese el ID del producto: ");
    int id = leerEntero();

    Producto producto = catalogoService.buscarPorId(id);

    if (producto == null) {
        System.out.println("Producto no encontrado.");
        return;
    }

    System.out.print("Cantidad que desea reponer: ");
    int cantidad = leerEntero();

    if (cantidad <= 0) {
        System.out.println("Cantidad no valida.");
        return;
    }

    disponibilidadService.registrarReposicion(producto, cantidad);
}

    private int leerEntero() {

        while (!scanner.hasNextInt()) {
            System.out.println("Ingrese un numero valido.");
            scanner.next();
        }

        return scanner.nextInt();
    }
}
