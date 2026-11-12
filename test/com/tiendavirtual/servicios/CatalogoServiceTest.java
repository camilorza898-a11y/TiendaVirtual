
package com.tiendavirtual.servicios;

import com.tiendavirtual.modelo.Producto;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CatalogoServiceTest {

    private Producto crearProducto(int id, String nombre) {
        return new Producto(id, nombre, 50000, 10) {
            @Override
            public String getCategoria() {
                return "Prueba";
            }
        };
    }

    @Test
    public void debeAgregarProductoAlCatalogo() {
        CatalogoService catalogo = new CatalogoService();
        Producto producto = crearProducto(1, "Camiseta");

        catalogo.agregarProducto(producto);

        assertEquals(1, catalogo.obtenerProductos().size());
    }

    @Test
    public void noDebeAgregarProductoNulo() {
        CatalogoService catalogo = new CatalogoService();

        catalogo.agregarProducto(null);

        assertTrue(catalogo.obtenerProductos().isEmpty());
    }

    @Test
    public void debeBuscarProductoPorId() {
        CatalogoService catalogo = new CatalogoService();
        Producto producto = crearProducto(1, "Camiseta");

        catalogo.agregarProducto(producto);

        assertEquals(producto, catalogo.buscarPorId(1));
    }

    @Test
    public void debeRetornarNuloSiNoEncuentraProducto() {
        CatalogoService catalogo = new CatalogoService();

        assertNull(catalogo.buscarPorId(99));
    }

    @Test
    public void debeRetornarCatalogoVacioInicialmente() {
        CatalogoService catalogo = new CatalogoService();

        assertTrue(catalogo.obtenerProductos().isEmpty());
    }

    @Test
    public void debeRetornarUnaCopiaDelCatalogo() {
        CatalogoService catalogo = new CatalogoService();

        catalogo.agregarProducto(crearProducto(1, "Camiseta"));

        List<Producto> copia = catalogo.obtenerProductos();
        copia.clear();

        assertEquals(1, catalogo.obtenerProductos().size());
    }
}
