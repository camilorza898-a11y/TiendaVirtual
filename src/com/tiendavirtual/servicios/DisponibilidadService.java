
package com.tiendavirtual.servicios;

import com.tiendavirtual.modelo.Producto;
import com.tiendavirtual.notificaciones.GestorNotificaciones;
import com.tiendavirtual.notificaciones.ObservadorStock;

import java.util.HashMap;
import java.util.Map;

public class DisponibilidadService {

    private Map<Integer, GestorNotificaciones> notificaciones;

    public DisponibilidadService() {
        notificaciones = new HashMap<>();
    }

    public void suscribir(Producto producto, ObservadorStock observador) {

        if (producto == null || observador == null) {
            return;
        }

        GestorNotificaciones gestor = notificaciones.computeIfAbsent(
                producto.getId(),
                id -> new GestorNotificaciones()
        );

        gestor.suscribir(observador);
    }

    public void cancelarSuscripcion(Producto producto,
                                    ObservadorStock observador) {

        if (producto == null || observador == null) {
            return;
        }

        GestorNotificaciones gestor = notificaciones.get(producto.getId());

        if (gestor != null) {
            gestor.cancelarSuscripcion(observador);
        }
    }

    public void registrarReposicion(Producto producto, int cantidad) {

        if (producto == null || cantidad <= 0) {
            System.out.println("Datos de reposicion invalidos.");
            return;
        }

        boolean estabaAgotado = producto.getStock() == 0;

        producto.setStock(producto.getStock() + cantidad);

        System.out.println(
                "Stock actualizado: " + producto.getStock()
        );

        if (estabaAgotado && producto.getStock() > 0) {

            GestorNotificaciones gestor =
                    notificaciones.get(producto.getId());

            if (gestor != null) {
                gestor.notificar(producto);
            }
        }
    }
}
