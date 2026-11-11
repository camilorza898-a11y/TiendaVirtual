package com.tiendavirtual.servicios;

import com.tiendavirtual.modelo.Factura;
import com.tiendavirtual.modelo.Pedido;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FacturaService {

    private List<Factura> facturas;
    private int siguienteNumero;

    public FacturaService() {
        facturas = new ArrayList<>();
        siguienteNumero = 1;
    }

    // Método original
    public Factura generarFactura(Pedido pedido) {
        return generarFactura(pedido, LocalDate.now());
    }

    // Nuevo método que recibe la fecha de compra
    public Factura generarFactura(Pedido pedido, LocalDate fechaCompra) {

        if (pedido == null || !"Confirmado".equals(pedido.getEstado())) {
            System.out.println("No se puede facturar este pedido.");
            return null;
        }

        Factura factura = new Factura(
            siguienteNumero++,
            pedido,
            fechaCompra
        );

        facturas.add(factura);

        System.out.println("Factura generada correctamente.");

        return factura;
    }

    public void mostrarFacturas() {

        System.out.println("\n===== FACTURAS GENERADAS =====");

        if (facturas.isEmpty()) {
            System.out.println("No hay facturas registradas.");
            return;
        }

        for (Factura factura : facturas) {
            factura.mostrarFactura();
        }
    }

    public List<Factura> obtenerFacturas() {
        return new ArrayList<>(facturas);
    }
    public Factura buscarFacturaPorPedido(int idPedido) {

    for (Factura factura : facturas) {

        if (factura.getPedido().getId() == idPedido) {
            return factura;
        }
    }

    return null;
    }
}