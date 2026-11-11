
package com.tiendavirtual.servicios;

import com.tiendavirtual.modelo.Carrito;
import com.tiendavirtual.modelo.Pedido;
import com.tiendavirtual.modelo.Factura;
import java.time.LocalDate;

public class TiendaService {

    private PedidoService pedidoService;
    private HistorialService historialService;
    private FacturaService facturaService;

    public TiendaService(
            PedidoService pedidoService,
            HistorialService historialService,
            FacturaService facturaService,
            LocalDate fechaCompra) {

        this.pedidoService = pedidoService;
        this.historialService = historialService;
        this.facturaService = facturaService;
    }

    public Pedido realizarCompra(Carrito carrito) {

        Pedido pedido = pedidoService.confirmarPedido(carrito);

        if (pedido != null) {

            historialService.registrarPedido(pedido);

            Factura factura = facturaService.generarFactura(
                pedido,
                LocalDate.now()
            );

            if (factura != null) {
                System.out.println("Compra realizada correctamente.");
            }
        }

        return pedido;
    }

    public void consultarHistorial(
            com.tiendavirtual.modelo.Cliente cliente) {

        historialService.mostrarHistorial(cliente);
    }

    public void consultarFacturas() {
        facturaService.mostrarFacturas();
    }
    public Factura obtenerUltimaFactura() {
    java.util.List<Factura> facturas =
        facturaService.obtenerFacturas();

    if (facturas.isEmpty()) {
        return null;
    }

    return facturas.get(facturas.size() - 1);
    }
    public Factura obtenerFacturaPorPedido(int idPedido) {
    return facturaService.buscarFacturaPorPedido(idPedido);
}
}
