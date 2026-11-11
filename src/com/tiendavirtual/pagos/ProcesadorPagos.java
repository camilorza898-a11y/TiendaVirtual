
package com.tiendavirtual.pagos;

public class ProcesadorPagos {

    private MetodoPago metodoPago;

    public ProcesadorPagos(MetodoPago metodoPago) {
        this.metodoPago = metodoPago;
    }

    public void setMetodoPago(MetodoPago metodoPago) {

        if (metodoPago != null) {
            this.metodoPago = metodoPago;
        }
    }

    public void cambiarMetodoPago(MetodoPago metodoPago) {
        setMetodoPago(metodoPago);
    }

    public boolean ejecutarPago(double monto) {

        if (metodoPago == null) {
            System.out.println("No se ha seleccionado un metodo de pago.");
            return false;
        }

        System.out.println("Procesando pago mediante: "
                + metodoPago.getNombreMetodo());

        return metodoPago.procesarPago(monto);
    }
    public String getNombreMetodoPago() {
    if (metodoPago == null) {
        return "No especificado";
    }

    return metodoPago.getNombreMetodo();
    }
    public String getDetalleMetodoPago() {

    if (metodoPago instanceof PagoTransferencia) {
        return ((PagoTransferencia) metodoPago).getDetallePago();
    }

    if (metodoPago instanceof PagoTarjeta) {
        return ((PagoTarjeta) metodoPago).getDetallePago();
    }

    if (metodoPago instanceof PagoContraEntrega) {
        return ((PagoContraEntrega) metodoPago).getDetallePago();
    }

    return "Sin detalles adicionales";
    }
}
