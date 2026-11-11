
package com.tiendavirtual.notificaciones;

import com.tiendavirtual.modelo.Producto;

public class NotificacionSMS implements ObservadorStock {

    private String telefono;

    public NotificacionSMS(String telefono) {
        this.telefono = telefono;
    }

    @Override
    public void actualizar(Producto producto) {

        System.out.println("\n================================");
        System.out.println("        NOTIFICACION POR SMS");
        System.out.println("================================");

        System.out.println("Telefono: " + telefono);

        System.out.println(
            "El producto " + producto.getNombre()
            + " ya esta disponible nuevamente."
        );

        System.out.println("Stock disponible: " + producto.getStock());

        System.out.println("================================");
    }
}
