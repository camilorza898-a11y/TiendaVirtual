package com.tiendavirtual.descuentos;

public class DescuentoPorCantidad implements EstrategiaDescuento {

    private int cantidadMinima;
    private double porcentaje;

    public DescuentoPorCantidad(int cantidadMinima, double porcentaje) {
        this.cantidadMinima = cantidadMinima;
        this.porcentaje = porcentaje;
    }

    @Override
    public double calcularDescuento(double subtotal) {
        return 0;
    }

    public double calcularDescuento(double subtotal, int cantidadProductos) {
        if (cantidadProductos >= cantidadMinima) {
            return subtotal * (porcentaje / 100);
        }

        return 0;
    }

    @Override
    public String getNombreDescuento() {
        return "Descuento por cantidad";
    }
}