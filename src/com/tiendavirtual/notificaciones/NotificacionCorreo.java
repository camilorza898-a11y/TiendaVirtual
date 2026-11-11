
package com.tiendavirtual.notificaciones;

import com.tiendavirtual.modelo.Producto;

public class NotificacionCorreo implements ObservadorStock {

    private String correo;

    public NotificacionCorreo(String correo) {
        this.correo = correo;
    }

    @Override
    public void actualizar(Producto producto) {

        System.out.println("\n================================");
        System.out.println("       NOTIFICACION POR CORREO");
        System.out.println("================================");

        System.out.println("Para: " + correo);
        System.out.println("Asunto: Producto nuevamente disponible");

        System.out.println(
            "Hola, el producto " + producto.getNombre()
            + " ya se encuentra disponible nuevamente."
        );

        System.out.println("Stock disponible: " + producto.getStock());

        System.out.println("================================");
    }
}
