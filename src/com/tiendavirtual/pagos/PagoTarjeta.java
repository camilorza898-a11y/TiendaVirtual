package com.tiendavirtual.pagos;

public class PagoTarjeta implements MetodoPago {

    private String numeroTarjeta;
    private String titular;
    private String franquicia;
    private String tipoTarjeta;

    public PagoTarjeta(
            String numeroTarjeta,
            String titular,
            String franquicia,
            String tipoTarjeta) {

        this.numeroTarjeta = numeroTarjeta;
        this.titular = titular;
        this.franquicia = franquicia;
        this.tipoTarjeta = tipoTarjeta;
    }

    @Override
    public boolean procesarPago(double monto) {

        if (numeroTarjeta == null || numeroTarjeta.isBlank()
                || titular == null || titular.isBlank()
                || franquicia == null || franquicia.isBlank()
                || tipoTarjeta == null || tipoTarjeta.isBlank()
                || monto <= 0) {

            System.out.println("Datos de tarjeta inválidos.");
            return false;
        }

        System.out.println("Procesando pago con tarjeta...");
        System.out.println("Franquicia: " + franquicia);
        System.out.println("Tipo: " + tipoTarjeta);
        System.out.println("Titular: " + titular);
        System.out.println("Pago simulado exitosamente.");

        return true;
    }

    @Override
    public String getNombreMetodo() {
        return franquicia + " - " + tipoTarjeta;
    }
    public String getDetallePago() {
    return "Franquicia: " + franquicia
            + " | Modalidad: " + tipoTarjeta;
    }
}
