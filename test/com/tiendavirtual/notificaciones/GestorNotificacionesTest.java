
package com.tiendavirtual.notificaciones;

import com.tiendavirtual.modelo.Producto;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GestorNotificacionesTest {

    private Producto crearProducto() {
        return new Producto(1, "Camiseta negra", 45000, 10) {
            @Override
            public String getCategoria() {
                return "Ropa";
            }
        };
    }

    @Test
    public void debeSuscribirObservador() {
        GestorNotificaciones gestor = new GestorNotificaciones();

        ObservadorStock observador = producto -> {};

        gestor.suscribir(observador);

        assertEquals(1, gestor.cantidadSuscriptores());
    }

    @Test
    public void noDebeDuplicarSuscripciones() {
        GestorNotificaciones gestor = new GestorNotificaciones();

        ObservadorStock observador = producto -> {};

        gestor.suscribir(observador);
        gestor.suscribir(observador);

        assertEquals(1, gestor.cantidadSuscriptores());
    }

    @Test
    public void debeCancelarSuscripcion() {
        GestorNotificaciones gestor = new GestorNotificaciones();

        ObservadorStock observador = producto -> {};

        gestor.suscribir(observador);
        gestor.cancelarSuscripcion(observador);

        assertEquals(0, gestor.cantidadSuscriptores());
    }

    @Test
    public void debeNotificarAlObservador() {
        GestorNotificaciones gestor = new GestorNotificaciones();

        final int[] notificaciones = {0};

        ObservadorStock observador = producto -> {
            notificaciones[0]++;
        };

        gestor.suscribir(observador);
        gestor.notificar(crearProducto());

        assertEquals(1, notificaciones[0]);
    }

    @Test
    public void noDebeNotificarSiElProductoEsNulo() {
        GestorNotificaciones gestor = new GestorNotificaciones();

        final int[] notificaciones = {0};

        ObservadorStock observador = producto -> {
            notificaciones[0]++;
        };

        gestor.suscribir(observador);
        gestor.notificar(null);

        assertEquals(0, notificaciones[0]);
    }

    @Test
    public void noDebeSuscribirObservadorNulo() {
        GestorNotificaciones gestor = new GestorNotificaciones();

        gestor.suscribir(null);

        assertEquals(0, gestor.cantidadSuscriptores());
    }
}
