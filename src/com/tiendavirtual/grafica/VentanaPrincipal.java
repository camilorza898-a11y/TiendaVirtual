package com.tiendavirtual.grafica;

import javax.swing.*;
import java.awt.*;
import com.tiendavirtual.servicios.CatalogoService;
import com.tiendavirtual.servicios.RegistroProductos;
import com.tiendavirtual.modelo.Producto;
import java.util.List;
import com.tiendavirtual.modelo.Cliente;
import com.tiendavirtual.modelo.Carrito;
import com.tiendavirtual.descuentos.*;
import com.tiendavirtual.pagos.*;
import com.tiendavirtual.servicios.PedidoService;
import com.tiendavirtual.servicios.HistorialService;
import com.tiendavirtual.servicios.FacturaService;
import com.tiendavirtual.servicios.TiendaService;
import java.time.LocalDate;
import java.time.DayOfWeek;
import com.tiendavirtual.servicios.InventarioService;
import com.tiendavirtual.pagos.PagoTarjeta;
import com.tiendavirtual.pagos.PagoTransferencia;
import com.tiendavirtual.pagos.PagoContraEntrega;
import javax.swing.JComboBox;

public class VentanaPrincipal extends JFrame {

    private JPanel contenido;
    private CatalogoService catalogoService;
    private Cliente cliente;
    private Carrito carrito;
    private CalculadoraDescuentos calculadoraDescuentos;
    private ProcesadorPagos procesadorPagos;
    private PedidoService pedidoService;
    private HistorialService historialService;
    private FacturaService facturaService;
    private TiendaService tiendaService;
    private InventarioService inventarioService;
    private boolean cuponAplicado = false;

    public VentanaPrincipal() {
        configurarVentana();
        mostrarInicioSesion();
    }

    private void mostrarInicioSesion() {
        contenido.removeAll();
        contenido.setLayout(new GridBagLayout());
        contenido.setBackground(new Color(35, 35, 35));

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(55, 55, 55));
        panel.setBorder(
            BorderFactory.createEmptyBorder(35, 40, 35, 40)
        );

        JLabel titulo = new JLabel("TIENDA VIRTUAL");
        titulo.setFont(new Font("Arial", Font.BOLD, 28));
        titulo.setForeground(Color.WHITE);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitulo = new JLabel("INICIAR SESIÓN");
        subtitulo.setFont(new Font("Arial", Font.BOLD, 18));
        subtitulo.setForeground(Color.LIGHT_GRAY);
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField campoNombre = new JTextField(20);
        JTextField campoTelefono = new JTextField(20);

        JLabel etiquetaNombre = new JLabel("Nombre completo");
        JLabel etiquetaTelefono = new JLabel("Número de teléfono");

        etiquetaNombre.setForeground(Color.WHITE);
        etiquetaTelefono.setForeground(Color.WHITE);

        JButton ingresar = new JButton("INGRESAR");
        ingresar.setBackground(new Color(170, 25, 35));
        ingresar.setForeground(Color.WHITE);
        ingresar.setFocusPainted(false);
        ingresar.setAlignmentX(Component.CENTER_ALIGNMENT);

        ingresar.addActionListener(e -> {
            String nombre = campoNombre.getText().trim();
            String telefono = campoTelefono.getText().trim();

            if (nombre.isEmpty() || telefono.isEmpty()) {
                JOptionPane.showMessageDialog(
                    this,
                    "Completa todos los campos."
                );
                return;
            }

            cliente = new Cliente(1, nombre, telefono);
            carrito = new Carrito(cliente);

            configurarServicios();
            mostrarMenuPrincipal();
            cambiarPantalla("Catálogo");
        });

        panel.add(titulo);
        panel.add(Box.createVerticalStrut(10));
        panel.add(subtitulo);
        panel.add(Box.createVerticalStrut(30));
        panel.add(etiquetaNombre);
        panel.add(campoNombre);
        panel.add(Box.createVerticalStrut(15));
        panel.add(etiquetaTelefono);
        panel.add(campoTelefono);
        panel.add(Box.createVerticalStrut(25));
        panel.add(ingresar);

        contenido.add(panel);
        contenido.revalidate();
        contenido.repaint();
    }

    private void mostrarMenuPrincipal() {
        JPanel menu = new JPanel(new GridLayout(7, 1, 10, 10));
        menu.setBackground(new Color(20, 20, 20));
        menu.setPreferredSize(new Dimension(220, 650));

        JLabel logo = new JLabel(
            "TIENDA VIRTUAL",
            SwingConstants.CENTER
        );
        logo.setForeground(Color.WHITE);
        logo.setFont(new Font("Arial", Font.BOLD, 20));
        menu.add(logo);

        String[] opciones = {
            "Inicio",
            "Catálogo",
            "Carrito",
            "Mis compras",
            "Salir"
        };

        for (String opcion : opciones) {
            JButton boton = new JButton(opcion);
            boton.setFont(new Font("Arial", Font.BOLD, 15));
            boton.setForeground(Color.WHITE);
            boton.setBackground(new Color(170, 25, 35));
            boton.setFocusPainted(false);

            boton.addActionListener(e -> cambiarPantalla(opcion));

            menu.add(boton);
        }

        add(menu, BorderLayout.WEST);
        revalidate();
        repaint();
    }

    private void configurarServicios() {
        catalogoService = new CatalogoService();
        RegistroProductos.registrar(catalogoService);

        calculadoraDescuentos = new CalculadoraDescuentos();

        calculadoraDescuentos.agregarDescuento(
            new DescuentoPorCantidad(5, 10)
        );

        LocalDate fechaCompra = LocalDate.now();

        configurarDescuentosTemporada(fechaCompra);

        procesadorPagos = new ProcesadorPagos(
            new PagoContraEntrega(cliente.getDireccion())
        );

        inventarioService = new InventarioService();

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
    }
    
    
private boolean seleccionarMetodoPago() {

    String[] opciones = {
        "Visa",
        "Mastercard",
        "Nequi",
        "Daviplata",
        "Bre-B",
        "Pago contra entrega"
    };

    JComboBox<String> selector = new JComboBox<>(opciones);

    int resultado = JOptionPane.showConfirmDialog(
        this,
        selector,
        "Seleccionar método de pago",
        JOptionPane.OK_CANCEL_OPTION,
        JOptionPane.QUESTION_MESSAGE
    );

    if (resultado != JOptionPane.OK_OPTION) {
        return false;
    }

    String metodo = (String) selector.getSelectedItem();

    if (metodo.equals("Visa") || metodo.equals("Mastercard")) {

        String numero = JOptionPane.showInputDialog(
            this, "Número de tarjeta:"
        );

        if (numero == null) return false;

        String titular = JOptionPane.showInputDialog(
            this, "Nombre del titular:"
        );

        if (titular == null) return false;

        String[] tipos = {"Débito", "Crédito"};

        String tipo = (String) JOptionPane.showInputDialog(
            this,
            "Selecciona el tipo de tarjeta:",
            "Tipo de tarjeta",
            JOptionPane.QUESTION_MESSAGE,
            null,
            tipos,
            tipos[0]
        );

        if (tipo == null) return false;

        procesadorPagos.setMetodoPago(
            new PagoTarjeta(numero, titular, metodo, tipo)
        );

    } else if (
        metodo.equals("Nequi")
        || metodo.equals("Daviplata")
        || metodo.equals("Bre-B")
    ) {

        String referencia = JOptionPane.showInputDialog(
            this, "Número o referencia de la transferencia:"
        );

        if (referencia == null) return false;

        procesadorPagos.setMetodoPago(
            new PagoTransferencia(metodo, referencia)
        );

    } else if (metodo.equals("Pago contra entrega")) {

        String direccion = JOptionPane.showInputDialog(
            this,
            "Ingresa la dirección donde recibirás el pedido:",
            cliente.getDireccion()
        );

        if (direccion == null || direccion.isBlank()) {
            JOptionPane.showMessageDialog(
                this, "Debes ingresar una dirección válida."
            );
            return false;
        }

        cliente.setDireccion(direccion);

        procesadorPagos.setMetodoPago(
            new PagoContraEntrega(direccion)
        );
    }

    return true;
}


    private void configurarDescuentosTemporada(LocalDate fechaCompra) {
        calculadoraDescuentos.agregarDescuento(
            new DescuentoTemporada(
                "Navidad",
                10,
                LocalDate.of(fechaCompra.getYear(), 12, 1),
                LocalDate.of(fechaCompra.getYear(), 12, 25),
                fechaCompra
            )
        );

        calculadoraDescuentos.agregarDescuento(
            new DescuentoTemporada(
                "San Valentín",
                10,
                LocalDate.of(fechaCompra.getYear(), 2, 14),
                LocalDate.of(fechaCompra.getYear(), 2, 14),
                fechaCompra
            )
        );

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
    }


private void configurarVentana() {
    setTitle("Tienda Virtual");

    setSize(1280, 850);
    setMinimumSize(new Dimension(1000, 700));

    setLocationRelativeTo(null);
    setResizable(true);

    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new BorderLayout());

    contenido = new JPanel(new BorderLayout());
    contenido.setBackground(new Color(35, 35, 35));

    add(contenido, BorderLayout.CENTER);
}


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
        new VentanaPrincipal().setVisible(true);
    });
}




private void cambiarPantalla(String opcion) {
    if (opcion.equals("Salir")) {
        System.exit(0);
        return;
    }

    contenido.removeAll();
    contenido.setLayout(new BorderLayout());

    switch (opcion) {
        case "Inicio":
            mostrarInicio();
            break;

        case "Catálogo":
            mostrarCatalogo();
            break;

        case "Carrito":
            mostrarCarrito();
            break;

        case "Mis compras":
            mostrarMisCompras();
            break;

        default:
            JLabel titulo = new JLabel(opcion, SwingConstants.CENTER);
            titulo.setFont(new Font("Arial", Font.BOLD, 30));
            titulo.setForeground(Color.WHITE);
            contenido.add(titulo, BorderLayout.CENTER);
            break;
    }

    contenido.revalidate();
    contenido.repaint();
}


private void mostrarInicio() {
    JPanel inicio = new JPanel();
    inicio.setBackground(new Color(35, 35, 35));
    inicio.setLayout(new BoxLayout(inicio, BoxLayout.Y_AXIS));

    JLabel titulo = new JLabel("BIENVENIDO A TIENDA VIRTUAL");
    titulo.setFont(new Font("Arial", Font.BOLD, 30));
    titulo.setForeground(Color.WHITE);
    titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

    JLabel subtitulo = new JLabel(
        "Todo lo que necesitas en un solo lugar"
    );
    subtitulo.setFont(new Font("Arial", Font.PLAIN, 18));
    subtitulo.setForeground(Color.LIGHT_GRAY);
    subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

    JLabel descripcion = new JLabel(
        "Compra ropa, tecnología y alimentos fácilmente."
    );
    descripcion.setFont(new Font("Arial", Font.PLAIN, 16));
    descripcion.setForeground(Color.WHITE);
    descripcion.setAlignmentX(Component.CENTER_ALIGNMENT);

    inicio.add(Box.createVerticalGlue());
    inicio.add(titulo);
    inicio.add(Box.createVerticalStrut(20));
    inicio.add(subtitulo);
    inicio.add(Box.createVerticalStrut(15));
    inicio.add(descripcion);
    inicio.add(Box.createVerticalGlue());

    contenido.add(inicio, BorderLayout.CENTER);
}


private void mostrarCatalogo() {

    JPanel catalogo = new JPanel(new BorderLayout());
    catalogo.setBackground(new Color(35, 35, 35));

    JLabel titulo = new JLabel(
        "NUESTRO CATÁLOGO",
        SwingConstants.CENTER
    );

    titulo.setFont(new Font("Arial", Font.BOLD, 28));
    titulo.setForeground(Color.WHITE);
    titulo.setBorder(
        BorderFactory.createEmptyBorder(20, 10, 20, 10)
    );

    catalogo.add(titulo, BorderLayout.NORTH);

    JPanel productosPanel = new JPanel(
        new GridLayout(0, 3, 15, 15)
    );

    productosPanel.setBackground(new Color(35, 35, 35));
    productosPanel.setBorder(
        BorderFactory.createEmptyBorder(15, 20, 20, 20)
    );

    List<Producto> productos = catalogoService.obtenerProductos();

    for (Producto producto : productos) {

        JPanel tarjeta = new JPanel(new BorderLayout(5, 10));
        tarjeta.setBackground(new Color(55, 55, 55));
        tarjeta.setBorder(
            BorderFactory.createEmptyBorder(15, 10, 15, 10)
        );

        // Identificar la imagen correspondiente
        String rutaImagen = "";

        switch (producto.getId()) {
            case 1:
                rutaImagen = "/imagenes/camisa.png";
                break;
            case 2:
                rutaImagen = "/imagenes/sudadera.jpg";
                break;
            case 3:
                rutaImagen = "/imagenes/jbl.jpg";
                break;
            case 4:
                rutaImagen = "/imagenes/mouse.png";
                break;
            case 5:
                rutaImagen = "/imagenes/arroz.png";
                break;
            case 6:
                rutaImagen = "/imagenes/galleta.jpg";
                break;
        }

        JLabel imagen = crearImagenProducto(rutaImagen);

        JLabel nombre = new JLabel(
            producto.getNombre(),
            SwingConstants.CENTER
        );

        nombre.setFont(new Font("Arial", Font.BOLD, 17));
        nombre.setForeground(Color.WHITE);

        JLabel precio = new JLabel(
            "$" + String.format("%,.0f", producto.getPrecio()),
            SwingConstants.CENTER
        );

        precio.setFont(new Font("Arial", Font.BOLD, 16));
        precio.setForeground(new Color(100, 220, 130));

        JLabel id = new JLabel(
            "Producto #" + producto.getId(),
            SwingConstants.CENTER
        );

        id.setForeground(Color.LIGHT_GRAY);

        JLabel stock = new JLabel(
            "Stock disponible: " + producto.getStock(),
            SwingConstants.CENTER
        );

        stock.setForeground(Color.LIGHT_GRAY);
        stock.setFont(new Font("Arial", Font.PLAIN, 13));

        JSpinner cantidad = new JSpinner(
            new SpinnerNumberModel(
                0,
                0,
                producto.getStock(),
                1
            )
        );

        JButton agregar = new JButton("AGREGAR AL CARRITO");
        agregar.setBackground(new Color(170, 25, 35));
        agregar.setForeground(Color.WHITE);
        agregar.setFocusPainted(false);

        if (producto.getStock() <= 0) {
            agregar.setEnabled(false);
            stock.setText("PRODUCTO AGOTADO");
            stock.setForeground(Color.RED);
        }

        agregar.addActionListener(e -> {

            int seleccion = (int) cantidad.getValue();

            if (seleccion <= 0) {
                JOptionPane.showMessageDialog(
                    this,
                    "Selecciona una cantidad."
                );
                return;
            }

            int cantidadEnCarrito = carrito.getProductos()
                .getOrDefault(producto, 0);

            if (cantidadEnCarrito + seleccion > producto.getStock()) {
                JOptionPane.showMessageDialog(
                    this,
                    "No hay suficientes existencias."
                );
                return;
            }

            carrito.agregarProducto(producto, seleccion);

            JOptionPane.showMessageDialog(
                this,
                seleccion + " unidad(es) de "
                + producto.getNombre()
                + " agregadas al carrito."
            );

            cantidad.setValue(0);
        });

        JPanel informacion = new JPanel(
            new GridLayout(3, 1, 5, 5)
        );

        informacion.setBackground(new Color(55, 55, 55));

        informacion.add(precio);
        informacion.add(stock);
        informacion.add(id);

        JPanel parteInferior = new JPanel(
            new BorderLayout(5, 5)
        );

        parteInferior.setBackground(new Color(55, 55, 55));
        parteInferior.add(cantidad, BorderLayout.CENTER);
        parteInferior.add(agregar, BorderLayout.SOUTH);

        tarjeta.add(imagen, BorderLayout.NORTH);
        tarjeta.add(nombre, BorderLayout.CENTER);
        tarjeta.add(informacion, BorderLayout.WEST);
        tarjeta.add(parteInferior, BorderLayout.SOUTH);

        productosPanel.add(tarjeta);
    }

    JScrollPane scroll = new JScrollPane(productosPanel);
    scroll.setBorder(null);
    scroll.getViewport().setBackground(new Color(35, 35, 35));

    catalogo.add(scroll, BorderLayout.CENTER);

    contenido.add(catalogo, BorderLayout.CENTER);
}




private void mostrarCarrito() {

    JPanel panelCarrito = new JPanel();
    panelCarrito.setLayout(
        new BoxLayout(panelCarrito, BoxLayout.Y_AXIS)
    );
    panelCarrito.setBackground(new Color(35, 35, 35));
    panelCarrito.setBorder(
        BorderFactory.createEmptyBorder(25, 35, 30, 35)
    );

    // TITULO
    JLabel titulo = new JLabel("MI CARRITO");
    titulo.setFont(new Font("Arial", Font.BOLD, 30));
    titulo.setForeground(Color.WHITE);
    titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

    panelCarrito.add(titulo);
    panelCarrito.add(Box.createVerticalStrut(30));

    if (carrito.estaVacio()) {

        JLabel vacio = new JLabel("Tu carrito está vacío.");
        vacio.setForeground(Color.LIGHT_GRAY);
        vacio.setFont(new Font("Arial", Font.PLAIN, 18));
        vacio.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelCarrito.add(Box.createVerticalStrut(60));
        panelCarrito.add(vacio);

    } else {

        // SUBTOTAL GENERAL
        JLabel total = new JLabel(
            "SUBTOTAL: $" + String.format(
                "%,.0f",
                carrito.calcularSubtotal()
            )
        );

        total.setForeground(new Color(100, 220, 120));
        total.setFont(new Font("Arial", Font.BOLD, 23));
        total.setAlignmentX(Component.CENTER_ALIGNMENT);

        // PRODUCTOS DEL CARRITO
        for (Producto producto : carrito.getProductos().keySet()) {

            int cantidad = carrito.getProductos().get(producto);

            JPanel tarjeta = new JPanel();
            tarjeta.setLayout(
                new BoxLayout(tarjeta, BoxLayout.Y_AXIS)
            );
            tarjeta.setBackground(new Color(48, 48, 48));

            tarjeta.setBorder(
                BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(
                        new Color(75, 75, 75), 1
                    ),
                    BorderFactory.createEmptyBorder(
                        18, 22, 18, 22
                    )
                )
            );

            tarjeta.setMaximumSize(
                new Dimension(650, 230)
            );
            tarjeta.setAlignmentX(Component.CENTER_ALIGNMENT);

            // NOMBRE
            JLabel nombre = new JLabel(producto.getNombre());
            nombre.setForeground(Color.WHITE);
            nombre.setFont(new Font("Arial", Font.BOLD, 19));
            nombre.setAlignmentX(Component.LEFT_ALIGNMENT);

            // CANTIDAD
            JLabel cantidadLabel = new JLabel(
                "Cantidad: " + cantidad
            );
            cantidadLabel.setForeground(Color.LIGHT_GRAY);
            cantidadLabel.setFont(
                new Font("Arial", Font.PLAIN, 14)
            );
            cantidadLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            // EXTRAS
            JCheckBox garantia = new JCheckBox(
                "Garantía extendida (+$8.000)"
            );

            JCheckBox empaque = new JCheckBox(
                "Empaque de regalo (+$5.000)"
            );

            JCheckBox envio = new JCheckBox(
                "Envío exprés (+$10.000)"
            );

            JCheckBox[] opciones = {
                garantia, empaque, envio
            };

            for (JCheckBox opcion : opciones) {
                opcion.setBackground(new Color(48, 48, 48));
                opcion.setForeground(Color.WHITE);
                opcion.setFont(
                    new Font("Arial", Font.PLAIN, 14)
                );
                opcion.setFocusPainted(false);
                opcion.setAlignmentX(Component.LEFT_ALIGNMENT);
            }

            // PRECIO UNITARIO
            JLabel precio = new JLabel();
            precio.setForeground(new Color(100, 220, 120));
            precio.setFont(
                new Font("Arial", Font.BOLD, 16)
            );
            precio.setAlignmentX(Component.LEFT_ALIGNMENT);

            // ACTUALIZAR PRECIOS Y SUBTOTAL
            Runnable actualizarPrecio = () -> {

                double valor = producto.getPrecio();

                if (garantia.isSelected()) {
                    valor += 8000;
                }

                if (empaque.isSelected()) {
                    valor += 5000;
                }

                if (envio.isSelected()) {
                    valor += 10000;
                }

                precio.setText(
                    "Precio unitario con extras: $"
                    + String.format("%,.0f", valor)
                );

                total.setText(
                    "SUBTOTAL: $" + String.format(
                        "%,.0f",
                        carrito.calcularSubtotal()
                    )
                );
            };

            // EVENTOS DE LOS EXTRAS
            for (JCheckBox opcion : opciones) {

                opcion.addActionListener(e -> {

                    actualizarDecoradores(
                        producto,
                        garantia.isSelected(),
                        empaque.isSelected(),
                        envio.isSelected()
                    );

                    actualizarPrecio.run();
                });
            }

            actualizarPrecio.run();

            // ORGANIZAR TARJETA
            tarjeta.add(nombre);
            tarjeta.add(Box.createVerticalStrut(7));
            tarjeta.add(cantidadLabel);
            tarjeta.add(Box.createVerticalStrut(12));
            tarjeta.add(garantia);
            tarjeta.add(empaque);
            tarjeta.add(envio);
            tarjeta.add(Box.createVerticalStrut(10));
            tarjeta.add(precio);

            panelCarrito.add(tarjeta);
            panelCarrito.add(Box.createVerticalStrut(18));
        }

        // SUBTOTAL
        JPanel panelTotal = new JPanel();
        panelTotal.setBackground(new Color(48, 48, 48));
        panelTotal.setBorder(
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        );
        panelTotal.setMaximumSize(
            new Dimension(650, 65)
        );
        panelTotal.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelTotal.add(total);

        panelCarrito.add(panelTotal);
        panelCarrito.add(Box.createVerticalStrut(25));

        // SECCION CUPON
        JPanel panelCupon = new JPanel();
        panelCupon.setLayout(
            new BoxLayout(panelCupon, BoxLayout.Y_AXIS)
        );
        panelCupon.setBackground(new Color(48, 48, 48));
        panelCupon.setBorder(
            BorderFactory.createEmptyBorder(18, 22, 18, 22)
        );
        panelCupon.setMaximumSize(
            new Dimension(450, 190)
        );
        panelCupon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel tituloCupon = new JLabel("¿TIENES UN CUPÓN?");
        tituloCupon.setForeground(Color.WHITE);
        tituloCupon.setFont(
            new Font("Arial", Font.BOLD, 17)
        );
        tituloCupon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField campoCupon = new JTextField();
        campoCupon.setMaximumSize(
            new Dimension(300, 35)
        );
        campoCupon.setHorizontalAlignment(
            JTextField.CENTER
        );
        campoCupon.setFont(
            new Font("Arial", Font.PLAIN, 14)
        );

        JButton aplicarCupon = new JButton("APLICAR CUPÓN");
        aplicarCupon.setBackground(new Color(170, 25, 35));
        aplicarCupon.setForeground(Color.WHITE);
        aplicarCupon.setFocusPainted(false);
        aplicarCupon.setFont(
            new Font("Arial", Font.BOLD, 13)
        );
        aplicarCupon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel estadoCupon = new JLabel(
            cuponAplicado
                ? "Cupón TIENDA10 aplicado: 10% de descuento"
                : " "
        );
        estadoCupon.setForeground(new Color(100, 220, 120));
        estadoCupon.setFont(
            new Font("Arial", Font.PLAIN, 12)
        );
        estadoCupon.setAlignmentX(Component.CENTER_ALIGNMENT);

        aplicarCupon.setEnabled(!cuponAplicado);
        campoCupon.setEnabled(!cuponAplicado);

        aplicarCupon.addActionListener(e -> {

            String codigo = campoCupon.getText().trim();

            if (codigo.equalsIgnoreCase("TIENDA10")) {

                calculadoraDescuentos.eliminarDescuentoCupon();

                calculadoraDescuentos.agregarDescuento(
                    new DescuentoCupon("TIENDA10", 10)
                );

                cuponAplicado = true;

                estadoCupon.setText(
                    "¡Cupón aplicado! Tienes 10% de descuento."
                );

                aplicarCupon.setEnabled(false);
                campoCupon.setEnabled(false);

            } else {

                JOptionPane.showMessageDialog(
                    this,
                    "El código ingresado no es válido.",
                    "Cupón inválido",
                    JOptionPane.WARNING_MESSAGE
                );
            }
        });

        panelCupon.add(tituloCupon);
        panelCupon.add(Box.createVerticalStrut(12));
        panelCupon.add(campoCupon);
        panelCupon.add(Box.createVerticalStrut(10));
        panelCupon.add(aplicarCupon);
        panelCupon.add(Box.createVerticalStrut(8));
        panelCupon.add(estadoCupon);

        panelCarrito.add(panelCupon);
        panelCarrito.add(Box.createVerticalStrut(25));

        // CONFIRMAR COMPRA
        JButton confirmar = new JButton("CONFIRMAR COMPRA");
        confirmar.setBackground(new Color(180, 25, 38));
        confirmar.setForeground(Color.WHITE);
        confirmar.setFont(
            new Font("Arial", Font.BOLD, 17)
        );
        confirmar.setFocusPainted(false);
        confirmar.setBorder(
            BorderFactory.createEmptyBorder(12, 35, 12, 35)
        );
        confirmar.setAlignmentX(Component.CENTER_ALIGNMENT);

        confirmar.addActionListener(e -> confirmarCompra());

        panelCarrito.add(confirmar);
    }

    // SCROLL
    JScrollPane scroll = new JScrollPane(panelCarrito);
    scroll.setBorder(null);
    scroll.setHorizontalScrollBarPolicy(
        JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
    );
    scroll.getVerticalScrollBar().setUnitIncrement(16);
    scroll.getViewport().setBackground(new Color(35, 35, 35));

    contenido.add(scroll, BorderLayout.CENTER);
}





private void confirmarCompra() {

    if (carrito.estaVacio()) {
        JOptionPane.showMessageDialog(
            this,
            "El carrito está vacío."
        );
        return;
    }

    int respuesta = JOptionPane.showConfirmDialog(
        this,
        "¿Deseas confirmar tu compra?",
        "Confirmar compra",
        JOptionPane.YES_NO_OPTION
    );

    if (respuesta != JOptionPane.YES_OPTION) {
        return;
    }
    
    // AQUÍ SE ABRE LA SELECCIÓN DE PAGO
    if (!seleccionarMetodoPago()) {
    return;
    }

    com.tiendavirtual.modelo.Pedido pedido =
        tiendaService.realizarCompra(carrito);

    if (pedido == null) {
        JOptionPane.showMessageDialog(
            this,
            "No se pudo completar la compra. Verifica el stock y el pago."
        );
        return;
    }

    com.tiendavirtual.modelo.Factura factura =
        tiendaService.obtenerUltimaFactura();

    if (factura != null) {

        JTextArea areaFactura = new JTextArea(
            factura.generarContenidoFactura()
        );

        areaFactura.setEditable(false);
        areaFactura.setFont(
            new Font("Monospaced", Font.PLAIN, 13)
        );
        areaFactura.setCaretPosition(0);

        JScrollPane scroll = new JScrollPane(areaFactura);
        scroll.setPreferredSize(new Dimension(520, 550));

        JOptionPane.showMessageDialog(
            this,
            scroll,
            "Factura - Tienda Virtual",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    calculadoraDescuentos.eliminarDescuentoCupon();
    cuponAplicado = false;

    carrito = new Carrito(cliente);
    cambiarPantalla("Catálogo");
}

private void mostrarMisCompras() {

    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setBackground(new Color(35, 35, 35));

    JLabel titulo = new JLabel("MIS COMPRAS");
    titulo.setFont(new Font("Arial", Font.BOLD, 28));
    titulo.setForeground(Color.WHITE);
    titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

    panel.add(Box.createVerticalStrut(25));
    panel.add(titulo);
    panel.add(Box.createVerticalStrut(25));

    java.util.List<com.tiendavirtual.modelo.Pedido> historial =
        historialService.consultarHistorial(cliente);

    if (historial.isEmpty()) {

        JLabel vacio = new JLabel("Todavía no tienes compras registradas.");
        vacio.setForeground(Color.LIGHT_GRAY);
        vacio.setFont(new Font("Arial", Font.PLAIN, 16));
        vacio.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(vacio);

    } else {

        for (com.tiendavirtual.modelo.Pedido pedido : historial) {

            JButton botonPedido = new JButton(
                "PEDIDO #" + pedido.getId()
                + " | Total: $" + String.format("%,.0f", pedido.getTotal())
            );

            botonPedido.setBackground(new Color(170, 25, 35));
            botonPedido.setForeground(Color.WHITE);
            botonPedido.setFont(new Font("Arial", Font.BOLD, 15));
            botonPedido.setFocusPainted(false);
            botonPedido.setAlignmentX(Component.CENTER_ALIGNMENT);

            botonPedido.addActionListener(e -> {

                StringBuilder detalle = new StringBuilder();

                detalle.append("PEDIDO #")
                       .append(pedido.getId()).append("\n\n");

                detalle.append("Fecha: ")
                       .append(pedido.getFecha()).append("\n");

                detalle.append("Estado: ")
                       .append(pedido.getEstado()).append("\n\n");

                detalle.append("PRODUCTOS\n");

                for (java.util.Map.Entry<
                        com.tiendavirtual.modelo.Producto, Integer> entrada
                        : pedido.getCantidades().entrySet()) {

                    detalle.append(entrada.getKey().getNombre())
                           .append(" x")
                           .append(entrada.getValue())
                           .append("\n");
                }

                detalle.append("\nSubtotal: $")
                       .append(String.format("%,.0f", pedido.getSubtotal()));

                detalle.append("\nTotal pagado: $")
                       .append(String.format("%,.0f", pedido.getTotal()));

                detalle.append("\nMétodo de pago: ")
                       .append(pedido.getMetodoPago());

                detalle.append("\nDetalles: ")
                       .append(pedido.getDetallePago());

                Object[] opciones = {"Ver factura", "Cerrar"};

                int opcion = JOptionPane.showOptionDialog(
                    this,
                    detalle.toString(),
                    "Detalle de compra",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    opciones,
                    opciones[0]
                );

                if (opcion == 0) {

                    com.tiendavirtual.modelo.Factura factura =
                        tiendaService.obtenerFacturaPorPedido(pedido.getId());

                    if (factura != null) {

                        JTextArea area = new JTextArea(
                            factura.generarContenidoFactura()
                        );

                        area.setEditable(false);
                        area.setFont(
                            new Font("Monospaced", Font.PLAIN, 13)
                        );

                        JScrollPane scroll = new JScrollPane(area);
                        scroll.setPreferredSize(new Dimension(520, 550));

                        JOptionPane.showMessageDialog(
                            this,
                            scroll,
                            "Factura de compra",
                            JOptionPane.INFORMATION_MESSAGE
                        );

                    } else {
                        JOptionPane.showMessageDialog(
                            this,
                            "No se encontró la factura de este pedido."
                        );
                    }
                }
            });

            panel.add(botonPedido);
            panel.add(Box.createVerticalStrut(15));
        }
    }

    JScrollPane scroll = new JScrollPane(panel);
    scroll.setBorder(null);

    contenido.add(scroll, BorderLayout.CENTER);
}

private void actualizarDecoradores(
        Producto producto,
        boolean garantia,
        boolean empaque,
        boolean envio) {

    // Reiniciar el producto decorado
    carrito.getProductosDecorados().remove(producto);

    // Volver a crear el producto base
    com.tiendavirtual.extras.ProductoComponent decorado =
        new com.tiendavirtual.extras.ProductoBase(
            producto.getNombre(),
            producto.getPrecio()
        );

    if (garantia) {
        decorado = new com.tiendavirtual.extras.GarantiaExtendida(
            decorado, 8000
        );
    }

    if (empaque) {
        decorado = new com.tiendavirtual.extras.EmpaqueRegalo(
            decorado, 5000
        );
    }

    if (envio) {
        decorado = new com.tiendavirtual.extras.EnvioExpress(
            decorado, 10000
        );
    }

    // Guardar el producto decorado
    carrito.establecerProductoDecorado(producto, decorado);
}


private JLabel crearImagenProducto(String ruta) {

    JLabel imagen = new JLabel();
    imagen.setHorizontalAlignment(SwingConstants.CENTER);

    java.net.URL recurso = getClass().getResource(ruta);

    if (recurso != null) {

        ImageIcon icono = new ImageIcon(recurso);

        Image original = icono.getImage();

        Image escalada = original.getScaledInstance(
            180,
            140,
            Image.SCALE_SMOOTH
        );

        imagen.setIcon(new ImageIcon(escalada));

    } else {

        imagen.setText("Imagen no disponible");
        imagen.setForeground(Color.LIGHT_GRAY);
    }

    return imagen;
}







    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

}