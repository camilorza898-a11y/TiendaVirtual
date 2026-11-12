
package com.tiendavirtual.extras;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DecoradorProductoTest {

    @Test
    public void debeCrearProductoBase() {
        ProductoComponent producto =
                new ProductoBase("Camiseta", 45000);

        assertEquals("Camiseta", producto.getDescripcion());
        assertEquals(45000, producto.getPrecio(), 0.001);
    }

    @Test
    public void debeAgregarGarantiaExtendida() {
        ProductoComponent producto =
                new ProductoBase("Camiseta", 45000);

        producto = new GarantiaExtendida(producto, 8000);

        assertEquals(53000, producto.getPrecio(), 0.001);
        assertEquals(
                "Camiseta + Garantía extendida",
                producto.getDescripcion()
        );
    }

    @Test
    public void debeAgregarEmpaqueRegalo() {
        ProductoComponent producto =
                new ProductoBase("Camiseta", 45000);

        producto = new EmpaqueRegalo(producto, 5000);

        assertEquals(50000, producto.getPrecio(), 0.001);
        assertTrue(producto.getDescripcion().contains("Empaque de regalo"));
    }

    @Test
    public void debeAgregarEnvioExpress() {
        ProductoComponent producto =
                new ProductoBase("Camiseta", 45000);

        producto = new EnvioExpress(producto, 10000);

        assertEquals(55000, producto.getPrecio(), 0.001);
        assertTrue(producto.getDescripcion().contains("Envío exprés"));
    }

    @Test
    public void debeAcumularTodosLosExtras() {
        ProductoComponent producto =
                new ProductoBase("Camiseta", 45000);

        producto = new GarantiaExtendida(producto, 8000);
        producto = new EmpaqueRegalo(producto, 5000);
        producto = new EnvioExpress(producto, 10000);

        assertEquals(68000, producto.getPrecio(), 0.001);

        assertTrue(producto.getDescripcion().contains("Garantía extendida"));
        assertTrue(producto.getDescripcion().contains("Empaque de regalo"));
        assertTrue(producto.getDescripcion().contains("Envío exprés"));
    }
}
