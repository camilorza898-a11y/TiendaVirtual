
package com.tiendavirtual.descuentos;

public class DescuentoClienteFrecuente implements EstrategiaDescuento {

    private int comprasRealizadas;
    private double porcentaje;

    public DescuentoClienteFrecuente(int comprasRealizadas, double porcentaje) {
        this.comprasRealizadas = comprasRealizadas;
        this.porcentaje = porcentaje;
    }

    @Override
    public double calcularDescuento(double subtotal) {

        if (comprasRealizadas >= 5 && porcentaje > 0
                && porcentaje <= 100 && subtotal > 0) {

            return subtotal * (porcentaje / 100);
        }

        return 0;
    }

    @Override
    public String getNombreDescuento() {
        return "Descuento para compra mayo frecuente";
    }
}
