
package com.tiendavirtual.modelo;

import com.tiendavirtual.extras.ProductoComponent;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CarritoTest {

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
    public void carritoDebeIniciarVacio() {
        Carrito carrito = new Carrito(crearCliente());

        assertTrue(carrito.estaVacio());
    }

    @Test
    public void debeAgregarProducto() {
        Carrito carrito = new Carrito(crearCliente());

        carrito.agregarProducto(crearProducto());

        assertFalse(carrito.estaVacio());
        assertEquals(1, carrito.getProductos().size());
    }

    @Test
    public void debeAgregarCantidadCorrectamente() {
        Carrito carrito = new Carrito(crearCliente());
        Producto producto = crearProducto();

        carrito.agregarProducto(producto, 3);

        assertEquals(3, carrito.getProductos().get(producto));
    }

    @Test
    public void debeAcumularCantidadesDelMismoProducto() {
        Carrito carrito = new Carrito(crearCliente());
        Producto producto = crearProducto();

        carrito.agregarProducto(producto, 2);
        carrito.agregarProducto(producto, 3);

        assertEquals(5, carrito.getProductos().get(producto));
    }

    @Test
    public void noDebeAgregarProductoNulo() {
        Carrito carrito = new Carrito(crearCliente());

        carrito.agregarProducto(null);

        assertTrue(carrito.estaVacio());
    }

    @Test
    public void noDebeAgregarCantidadInvalida() {
        Carrito carrito = new Carrito(crearCliente());

        carrito.agregarProducto(crearProducto(), 0);

        assertTrue(carrito.estaVacio());
    }

    @Test
    public void debeCalcularSubtotalCorrectamente() {
        Carrito carrito = new Carrito(crearCliente());

        carrito.agregarProducto(crearProducto(), 2);

        assertEquals(100000, carrito.calcularSubtotal(), 0.001);
    }

    @Test
    public void debeActualizarCantidad() {
        Carrito carrito = new Carrito(crearCliente());
        Producto producto = crearProducto();

        carrito.agregarProducto(producto, 2);
        carrito.actualizarCantidad(producto, 5);

        assertEquals(5, carrito.getProductos().get(producto));
    }

    @Test
    public void debeEliminarProductoPorId() {
        Carrito carrito = new Carrito(crearCliente());
        Producto producto = crearProducto();

        carrito.agregarProducto(producto);
        carrito.eliminarProducto(1);

        assertTrue(carrito.estaVacio());
    }

    @Test
    public void debeEliminarProductoAlActualizarCantidadACero() {
        Carrito carrito = new Carrito(crearCliente());
        Producto producto = crearProducto();

        carrito.agregarProducto(producto);
        carrito.actualizarCantidad(producto, 0);

        assertTrue(carrito.estaVacio());
    }

    @Test
    public void debeAgregarGarantiaExtendida() {
        Carrito carrito = new Carrito(crearCliente());
        Producto producto = crearProducto();

        carrito.agregarProducto(producto);
        carrito.agregarGarantia(producto, 8000);

        assertEquals(58000, carrito.calcularSubtotal(), 0.001);
    }

    @Test
    public void debeAcumularExtrasEnElProducto() {
        Carrito carrito = new Carrito(crearCliente());
        Producto producto = crearProducto();

        carrito.agregarProducto(producto);
        carrito.agregarGarantia(producto, 8000);
        carrito.agregarEmpaque(producto, 5000);
        carrito.agregarEnvioExpress(producto, 10000);

        assertEquals(73000, carrito.calcularSubtotal(), 0.001);
    }

    @Test
    public void debeConservarClienteDelCarrito() {
        Cliente cliente = crearCliente();
        Carrito carrito = new Carrito(cliente);

        assertEquals(cliente, carrito.getCliente());
    }

    @Test
    public void debeRetornarUnaCopiaDeLosProductos() {
        Carrito carrito = new Carrito(crearCliente());
        carrito.agregarProducto(crearProducto());

        carrito.getProductos().clear();

        assertFalse(carrito.estaVacio());
    }
}
