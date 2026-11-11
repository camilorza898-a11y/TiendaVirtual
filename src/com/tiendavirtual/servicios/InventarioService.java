
package com.tiendavirtual.servicios;

import com.tiendavirtual.modelo.Producto;

public class InventarioService {

    public boolean hayExistencias(Producto producto, int cantidad) {

        if (producto == null || cantidad <= 0) {
            return false;
        }

        return producto.getStock() >= cantidad;
    }

    public boolean reducirStock(Producto producto, int cantidad) {

        if (!hayExistencias(producto, cantidad)) {
            System.out.println("No hay suficientes existencias.");
            return false;
        }

        producto.setStock(producto.getStock() - cantidad);

        System.out.println("Inventario actualizado correctamente.");

        return true;
    }

    public void aumentarStock(Producto producto, int cantidad) {

        if (producto == null || cantidad <= 0) {
            System.out.println("Datos de inventario inválidos.");
            return;
        }

        producto.setStock(producto.getStock() + cantidad);

        System.out.println("Stock actualizado. Existencias actuales: "
                + producto.getStock());
    }
}
