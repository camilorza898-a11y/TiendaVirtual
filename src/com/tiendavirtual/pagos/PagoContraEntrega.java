
package com.tiendavirtual.pagos;

public class PagoContraEntrega implements MetodoPago {

    private String direccionEntrega;

    public PagoContraEntrega(String direccionEntrega) {
        this.direccionEntrega = direccionEntrega;
    }

    @Override
    public boolean procesarPago(double monto) {

        if (direccionEntrega == null || direccionEntrega.isBlank()
                || monto <= 0) {

            System.out.println("Dirección de entrega inválida.");
            return false;
        }

        System.out.println("Pago contra entrega seleccionado.");
        System.out.println("Dirección: " + direccionEntrega);
        System.out.println("El cliente pagará al recibir el pedido.");

        return true;
    }

    @Override
    public String getNombreMetodo() {
        return "Pago contra entrega";
    }
    public String getDetallePago() {
    return "Dirección de entrega: " + direccionEntrega;
    }
}
