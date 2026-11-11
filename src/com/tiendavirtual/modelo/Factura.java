
package com.tiendavirtual.modelo;

import com.tiendavirtual.extras.ProductoComponent;
import java.time.LocalDate;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Map;

public class Factura {

    private int numero;
    private Pedido pedido;
    private LocalDate fechaEmision;

    public Factura(int numero, Pedido pedido) {
        this(numero, pedido, LocalDate.now());
    }

    public Factura(int numero, Pedido pedido, LocalDate fechaEmision) {
        this.numero = numero;
        this.pedido = pedido;
        this.fechaEmision = fechaEmision;
    }

    public int getNumero() {
        return numero;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public LocalDate getFechaEmision() {
        return fechaEmision;
    }

    private String formatoDinero(double valor) {
        DecimalFormatSymbols simbolos =
            new DecimalFormatSymbols();

        simbolos.setGroupingSeparator('.');

        DecimalFormat formato =
            new DecimalFormat("#,##0.00", simbolos);

        return "$" + formato.format(valor);
    }

    public String generarContenidoFactura() {

        StringBuilder factura = new StringBuilder();

        factura.append("========================================\n");
        factura.append("             TIENDA VIRTUAL\n");
        factura.append("                 FACTURA\n");
        factura.append("========================================\n\n");

        factura.append("Factura N°: ").append(numero).append("\n");
        factura.append("Fecha: ").append(fechaEmision).append("\n");
        factura.append("Cliente: ")
                .append(pedido.getCliente().getNombre()).append("\n");
        factura.append("Teléfono: ")
                .append(pedido.getCliente().getTelefono()).append("\n");

        factura.append("----------------------------------------\n");
        factura.append("              PRODUCTOS\n");
        factura.append("----------------------------------------\n");

        for (Map.Entry<Producto, Integer> entrada
                : pedido.getCantidades().entrySet()) {

            Producto producto = entrada.getKey();
            int cantidad = entrada.getValue();

            ProductoComponent decorado =
                pedido.getProductosDecorados().get(producto);

            String descripcion = decorado != null
                ? decorado.getDescripcion()
                : producto.getNombre();

            double precioBase = producto.getPrecio();
            double precioUnitario = decorado != null
                ? decorado.getPrecio()
                : precioBase;

            factura.append(producto.getNombre()).append("\n");
            factura.append("Cantidad: ")
                    .append(cantidad).append("\n");

            factura.append("Precio base unitario: ")
                    .append(formatoDinero(precioBase)).append("\n");

            if (descripcion.contains("Garantía extendida")) {
                factura.append("Garantía extendida: ")
                        .append(formatoDinero(8000)).append("\n");
            }

            if (descripcion.contains("Empaque de regalo")) {
                factura.append("Empaque de regalo: ")
                        .append(formatoDinero(5000)).append("\n");
            }

            if (descripcion.contains("Envío exprés")) {
                factura.append("Envío exprés: ")
                        .append(formatoDinero(10000)).append("\n");
            }

            factura.append("Precio unitario con extras: ")
                    .append(formatoDinero(precioUnitario)).append("\n");

            factura.append("Subtotal: ")
                    .append(formatoDinero(precioUnitario * cantidad))
                    .append("\n\n");
        }

        factura.append("----------------------------------------\n");
        factura.append("Subtotal: ")
                .append(formatoDinero(pedido.getSubtotal()))
                .append("\n\n");

        factura.append("           DESCUENTOS APLICADOS\n");
        factura.append("----------------------------------------\n");

        Map<String, Double> descuentos =
            pedido.getDescuentosAplicados();

        if (descuentos.isEmpty()) {
            factura.append("No se aplicaron descuentos.\n");
        } else {
            for (Map.Entry<String, Double> entrada
                    : descuentos.entrySet()) {

                factura.append(entrada.getKey()).append(": -")
                        .append(formatoDinero(entrada.getValue()))
                        .append("\n");
            }
        }

        double totalDescuentos =
            pedido.getSubtotal() - pedido.getTotal();

        factura.append("----------------------------------------\n");
        factura.append("TOTAL DESCUENTOS: ")
                .append(formatoDinero(totalDescuentos)).append("\n");

        factura.append("TOTAL A PAGAR: ")
                .append(formatoDinero(pedido.getTotal())).append("\n");

        factura.append("----------------------------------------\n");
        factura.append("             METODO DE PAGO\n");
        factura.append("----------------------------------------\n");

        factura.append("Tipo: ")
                .append(pedido.getMetodoPago() == null
                    ? "No registrado" : pedido.getMetodoPago())
                .append("\n");

        factura.append("Detalle: ")
                .append(pedido.getDetallePago() == null
                    ? "No registrado" : pedido.getDetallePago())
                .append("\n");

        factura.append("----------------------------------------\n");
        factura.append("Estado: ").append(pedido.getEstado()).append("\n");

        factura.append("========================================\n");
        factura.append("          GRACIAS POR SU COMPRA\n");
        factura.append("========================================\n");

        return factura.toString();
    }

    public void mostrarFactura() {
        System.out.println(generarContenidoFactura());
    }
}
