
package com.tiendavirtual.servicios;

import com.tiendavirtual.modelo.Producto;
import com.tiendavirtual.notificaciones.GestorNotificaciones;
import com.tiendavirtual.notificaciones.ObservadorStock;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class InventarioServiceTest {

    private Producto crearProducto() {
        return new Producto(1, "Producto de prueba", 50000, 10) {
            @Override
            public String getCategoria() {
                return "Prueba";
            }
        };
    }

    @Test
    public void debeDetectarExistenciasSuficientes() {
        InventarioService inventario = new InventarioService();
        Producto producto = crearProducto();

        assertTrue(inventario.hayExistencias(producto, 5));
    }

    @Test
    public void debeRechazarExistenciasInsuficientes() {
        InventarioService inventario = new InventarioService();
        Producto producto = crearProducto();

        assertFalse(inventario.hayExistencias(producto, 15));
    }

    @Test
    public void debeReducirStockCorrectamente() {
        InventarioService inventario = new InventarioService();
        Producto producto = crearProducto();

        assertTrue(inventario.reducirStock(producto, 3));
        assertEquals(7, producto.getStock());
    }

    @Test
    public void noDebeReducirStockSiNoHayExistencias() {
        InventarioService inventario = new InventarioService();
        Producto producto = crearProducto();

        assertFalse(inventario.reducirStock(producto, 15));
        assertEquals(10, producto.getStock());
    }

    @Test
    public void debeAumentarStockCorrectamente() {
        InventarioService inventario = new InventarioService();
        Producto producto = crearProducto();

        inventario.aumentarStock(producto, 5);

        assertEquals(15, producto.getStock());
    }

    @Test
    public void debeNotificarCuandoAumentaElStock() {
        GestorNotificaciones gestor = new GestorNotificaciones();
        InventarioService inventario = new InventarioService(gestor);
        Producto producto = crearProducto();

        int[] notificaciones = {0};

        ObservadorStock observador = new ObservadorStock() {
            @Override
            public void actualizar(Producto producto) {
                notificaciones[0]++;
            }
        };

        gestor.suscribir(observador);
        inventario.aumentarStock(producto, 5);

        assertEquals(1, notificaciones[0]);
    }

    @Test
    public void noDebeNotificarSiElAumentoEsInvalido() {
        GestorNotificaciones gestor = new GestorNotificaciones();
        InventarioService inventario = new InventarioService(gestor);
        Producto producto = crearProducto();

        int[] notificaciones = {0};

        ObservadorStock observador = new ObservadorStock() {
            @Override
            public void actualizar(Producto producto) {
                notificaciones[0]++;
            }
        };

        gestor.suscribir(observador);
        inventario.aumentarStock(producto, 0);

        assertEquals(0, notificaciones[0]);
        assertEquals(10, producto.getStock());
    }
}
