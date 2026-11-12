
package com.tiendavirtual.modelo;

import com.tiendavirtual.extras.ProductoBase;
import com.tiendavirtual.extras.GarantiaExtendida;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PedidoTest {

    private Cliente crearCliente() {
        return new Cliente(1, "Juan", "3001234567");
    }

    private Producto crearProducto() {
        return new Producto(1, "Camiseta", 50000, 10) {
            @Override
            public String getCategoria() {
                return "Ropa";
            }
        };
    }

    @Test
    public void debeCrearPedidoConListaDeProductos() {
        Producto producto = crearProducto();

        Pedido pedido = new Pedido(
                1, crearCliente(),
                Arrays.asList(producto, producto),
                100000
        );

        assertEquals(1, pedido.getId());
        assertEquals(2, pedido.getProductos().size());
        assertEquals(2, pedido.getCantidades().get(producto));
        assertEquals(100000, pedido.getSubtotal(), 0.001);
    }

    @Test
    public void debeCrearPedidoConCantidades() {
        Producto producto = crearProducto();

        Map<Producto, Integer> cantidades = new LinkedHashMap<>();
        cantidades.put(producto, 3);

        Pedido pedido = new Pedido(
                2, crearCliente(), cantidades,
                new LinkedHashMap<>(), 150000
        );

        assertEquals(3, pedido.getCantidades().get(producto));
        assertEquals(3, pedido.getProductos().size());
        assertEquals(150000, pedido.getSubtotal(), 0.001);
    }

    @Test
    public void debeIniciarConEstadoPendiente() {
        Pedido pedido = new Pedido(
                1, crearCliente(),
                Arrays.asList(crearProducto()), 50000
        );

        assertEquals("Pendiente", pedido.getEstado());
    }

    @Test
    public void debeCambiarEstadoDelPedido() {
        Pedido pedido = new Pedido(
                1, crearCliente(),
                Arrays.asList(crearProducto()), 50000
        );

        pedido.cambiarEstado("Entregado");

        assertEquals("Entregado", pedido.getEstado());
    }

    @Test
    public void debeRegistrarMetodoYDetalleDePago() {
        Pedido pedido = new Pedido(
                1, crearCliente(),
                Arrays.asList(crearProducto()), 50000
        );

        pedido.setMetodoPago("Tarjeta", "Visa - Crédito");

        assertEquals("Tarjeta", pedido.getMetodoPago());
        assertEquals("Visa - Crédito", pedido.getDetallePago());
    }

    @Test
    public void debeRegistrarDescuentosAplicados() {
        Pedido pedido = new Pedido(
                1, crearCliente(),
                Arrays.asList(crearProducto()), 45000
        );

        Map<String, Double> descuentos = new LinkedHashMap<>();
        descuentos.put("Cupón", 5000.0);

        pedido.setDescuentosAplicados(descuentos);

        assertEquals(5000.0,
                pedido.getDescuentosAplicados().get("Cupón"));
    }

    @Test
    public void debeCalcularSubtotalConProductoDecorado() {
        Producto producto = crearProducto();

        Map<Producto, Integer> cantidades = new LinkedHashMap<>();
        cantidades.put(producto, 2);

        Map<Producto, com.tiendavirtual.extras.ProductoComponent> extras =
                new LinkedHashMap<>();

        extras.put(
                producto,
                new GarantiaExtendida(
                        new ProductoBase("Camiseta", 50000), 8000
                )
        );

        Pedido pedido = new Pedido(
                1, crearCliente(), cantidades, extras, 116000
        );

        assertEquals(116000, pedido.getSubtotal(), 0.001);
        assertEquals(2, pedido.getProductos().size());
        assertFalse(pedido.getDetallesExtras().isEmpty());
    }

    @Test
    public void debeRegistrarDetalleExtra() {
        Pedido pedido = new Pedido(
                1, crearCliente(),
                Arrays.asList(crearProducto()), 50000
        );

        pedido.agregarDetalleExtra("Empaque de regalo");

        assertTrue(
                pedido.getDetallesExtras().contains("Empaque de regalo")
        );
    }

    @Test
    public void debeConservarElCliente() {
        Cliente cliente = crearCliente();

        Pedido pedido = new Pedido(
                1, cliente,
                Arrays.asList(crearProducto()), 50000
        );

        assertEquals(cliente, pedido.getCliente());
    }

    @Test
    public void debeGenerarFechaDeCreacion() {
        Pedido pedido = new Pedido(
                1, crearCliente(),
                Arrays.asList(crearProducto()), 50000
        );

        assertNotNull(pedido.getFecha());
    }
}
