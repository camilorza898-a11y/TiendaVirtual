package com.tiendavirtual.pagos;

public class PagoTransferencia implements MetodoPago {

    private String plataforma;
    private String referencia;

    public PagoTransferencia(String plataforma, String referencia) {
        this.plataforma = plataforma;
        this.referencia = referencia;
    }

    @Override
    public boolean procesarPago(double monto) {

        if (plataforma == null || plataforma.isBlank()
                || referencia == null || referencia.isBlank()
                || monto <= 0) {

            System.out.println("Datos de transferencia inválidos.");
            return false;
        }

        System.out.println("Procesando transferencia...");
        System.out.println("Plataforma: " + plataforma);
        System.out.println("Referencia: " + referencia);
        System.out.println("Transferencia simulada exitosamente.");

        return true;
    }

    @Override
    public String getNombreMetodo() {
        return "Transferencia - " + plataforma;
    }
    public String getDetallePago() {
    return "Plataforma: " + plataforma
            + " | Referencia: " + referencia;
    }
}
