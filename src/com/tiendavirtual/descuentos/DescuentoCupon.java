package com.tiendavirtual.descuentos;

public class DescuentoCupon implements EstrategiaDescuento {

    private String codigo;
    private double porcentaje;

    public DescuentoCupon(String codigo, double porcentaje) {
        this.codigo = codigo;
        this.porcentaje = porcentaje;
    }

    @Override
    public double calcularDescuento(double subtotal) {
        return subtotal * (porcentaje / 100);
    }

    @Override
    public String getNombreDescuento() {
        return "Cupon " + codigo;
    }
}