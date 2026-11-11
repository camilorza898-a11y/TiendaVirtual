
package com.tiendavirtual.descuentos;

import java.time.LocalDate;

public class DescuentoTemporada implements EstrategiaDescuento {

    private String nombreTemporada;
    private double porcentaje;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private LocalDate fechaCompra;

    public DescuentoTemporada(
            String nombreTemporada,
            double porcentaje,
            LocalDate fechaInicio,
            LocalDate fechaFin,
            LocalDate fechaCompra) {

        this.nombreTemporada = nombreTemporada;
        this.porcentaje = porcentaje;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.fechaCompra = fechaCompra;
    }

    public boolean estaVigente() {
        if (fechaInicio == null || fechaFin == null
                || fechaCompra == null) {
            return false;
        }

        return !fechaCompra.isBefore(fechaInicio)
                && !fechaCompra.isAfter(fechaFin);
    }

    @Override
    public double calcularDescuento(double subtotal) {

        if (!estaVigente()
                || porcentaje <= 0
                || porcentaje > 100
                || subtotal <= 0) {
            return 0;
        }

        return subtotal * (porcentaje / 100);
    }

    @Override
    public String getNombreDescuento() {
        return "Descuento de temporada: " + nombreTemporada;
    }
}
