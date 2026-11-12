
package com.tiendavirtual.servicios;

import com.tiendavirtual.modelo.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FacturaServiceTest {

    private Pedido crearPedido(int id) {
        Cliente cliente = new Cliente(
                1, "Juan Perez", "3001234567"
        );

        Producto producto = new Producto(
                1, "Camiseta", 50000, 10
        ) {
            @Override
            public String getCategoria() {
                return "Ropa";
            }
        };

        Pedido pedido = new Pedido(
                id, cliente, Arrays.asList(producto), 50000
        );

        pedido.cambiarEstado("Confirmado");
        pedido.setMetodoPago("Tarjeta", "Visa - Crédito");

        return pedido;
    }

    @Test
    public void debeGenerarFacturaParaPedidoConfirmado() {
        FacturaService servicio = new FacturaService();

        Factura factura = servicio.generarFactura(crearPedido(1));

        assertNotNull(factura);
        assertEquals(1, factura.getNumero());
    }

    @Test
    public void noDebeFacturarPedidoPendiente() {
        FacturaService servicio = new FacturaService();
        Pedido pedido = crearPedido(1);

        pedido.cambiarEstado("Pendiente");

        assertNull(servicio.generarFactura(pedido));
        assertTrue(servicio.obtenerFacturas().isEmpty());
    }

    @Test
    public void noDebeFacturarPedidoNulo() {
        FacturaService servicio = new FacturaService();

        assertNull(servicio.generarFactura(null));
    }

    @Test
    public void debeAsignarNumerosConsecutivos() {
        FacturaService servicio = new FacturaService();

        Factura primera = servicio.generarFactura(crearPedido(1));
        Factura segunda = servicio.generarFactura(crearPedido(2));

        assertEquals(1, primera.getNumero());
        assertEquals(2, segunda.getNumero());
    }

    @Test
    public void debeRespetarLaFechaDeCompra() {
        FacturaService servicio = new FacturaService();
        LocalDate fecha = LocalDate.of(2026, 11, 10);

        Factura factura = servicio.generarFactura(
                crearPedido(1), fecha
        );

        assertEquals(fecha, factura.getFechaEmision());
    }

    @Test
    public void debeBuscarFacturaPorIdDelPedido() {
        FacturaService servicio = new FacturaService();
        Pedido pedido = crearPedido(25);

        Factura factura = servicio.generarFactura(pedido);

        assertEquals(
                factura,
                servicio.buscarFacturaPorPedido(25)
        );
    }

    @Test
    public void debeRetornarNuloSiNoEncuentraFactura() {
        FacturaService servicio = new FacturaService();

        assertNull(servicio.buscarFacturaPorPedido(99));
    }

    @Test
    public void debeGenerarContenidoCompletoDeFactura() {
        FacturaService servicio = new FacturaService();
        Pedido pedido = crearPedido(1);

        Factura factura = servicio.generarFactura(pedido);
        String contenido = factura.generarContenidoFactura();

        assertTrue(contenido.contains("TIENDA VIRTUAL"));
        assertTrue(contenido.contains("Juan Perez"));
        assertTrue(contenido.contains("Camiseta"));
        assertTrue(contenido.contains("TOTAL A PAGAR"));
        assertTrue(contenido.contains("Visa - Crédito"));
        assertTrue(contenido.contains("GRACIAS POR SU COMPRA"));
    }
}
