
package com.tiendavirtual.notificaciones;

import com.tiendavirtual.modelo.Producto;
import java.util.ArrayList;
import java.util.List;

public class GestorNotificaciones {

    private List<ObservadorStock> observadores;

    public GestorNotificaciones() {
        this.observadores = new ArrayList<>();
    }

    public void suscribir(ObservadorStock observador) {

        if (observador != null && !observadores.contains(observador)) {
            observadores.add(observador);
            System.out.println("Cliente suscrito correctamente.");
        }
    }

    public void cancelarSuscripcion(ObservadorStock observador) {

        if (observadores.remove(observador)) {
            System.out.println("Suscripcion cancelada correctamente.");
        } else {
            System.out.println("El cliente no estaba suscrito.");
        }
    }

    public void notificar(Producto producto) {

        if (producto == null) {
            return;
        }

        System.out.println("\nNotificando a los clientes interesados...");

        for (ObservadorStock observador : observadores) {
            observador.actualizar(producto);
        }
    }

    public int cantidadSuscriptores() {
        return observadores.size();
    }
}
