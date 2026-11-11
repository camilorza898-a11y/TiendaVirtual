
package com.tiendavirtual.pagos;

public interface MetodoPago {

    boolean procesarPago(double monto);

    String getNombreMetodo();
}
