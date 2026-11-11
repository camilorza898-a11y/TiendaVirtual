
package com.tiendavirtual.servicios;

import com.tiendavirtual.modelo.Producto;
import java.util.ArrayList;
import java.util.List;

public class CatalogoService {

    private List<Producto> productos;

    public CatalogoService() {
        productos = new ArrayList<>();
    }

    public void agregarProducto(Producto producto) {

        if (producto != null) {
            productos.add(producto);
            System.out.println("Producto agregado: " + producto.getNombre());
        }
    }

    public void mostrarCatalogo() {

        System.out.println("\n===== CATÁLOGO DE PRODUCTOS =====");

        if (productos.isEmpty()) {
            System.out.println("No hay productos registrados.");
            return;
        }

        for (Producto producto : productos) {
            System.out.println("----------------------------");
            producto.mostrarInformacion();
        }
    }

    public Producto buscarPorId(int id) {

        for (Producto producto : productos) {

            if (producto.getId() == id) {
                return producto;
            }
        }

        return null;
    }

    public List<Producto> obtenerProductos() {
        return new ArrayList<>(productos);
    }
}
