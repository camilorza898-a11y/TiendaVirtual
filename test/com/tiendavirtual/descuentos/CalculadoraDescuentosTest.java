
package com.tiendavirtual.descuentos;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CalculadoraDescuentosTest {

    @Test
    public void debeAcumularVariosDescuentos() {
        CalculadoraDescuentos calculadora =
                new CalculadoraDescuentos();

        calculadora.agregarDescuento(
                new DescuentoCupon("PROMO10", 10)
        );

        calculadora.agregarDescuento(
                new DescuentoPorCantidad(3, 10)
        );

        double resultado =
                calculadora.calcularTotalDescuentos(100000, 3);

        assertEquals(20000, resultado, 0.001);
    }

    @Test
    public void noDebeSuperarElSubtotal() {
        CalculadoraDescuentos calculadora =
                new CalculadoraDescuentos();

        calculadora.agregarDescuento(
                new DescuentoCupon("PROMO150", 150)
        );

        double resultado =
                calculadora.calcularTotalDescuentos(100000);

        assertEquals(100000, resultado, 0.001);
    }

    @Test
    public void debeEliminarElDescuentoCupon() {
        CalculadoraDescuentos calculadora =
                new CalculadoraDescuentos();

        calculadora.agregarDescuento(
                new DescuentoCupon("PROMO10", 10)
        );

        calculadora.eliminarDescuentoCupon();

        double resultado =
                calculadora.calcularTotalDescuentos(100000);

        assertEquals(0, resultado, 0.001);
    }

    @Test
    public void debeCalcularTotalFinal() {
        CalculadoraDescuentos calculadora =
                new CalculadoraDescuentos();

        calculadora.agregarDescuento(
                new DescuentoCupon("PROMO10", 10)
        );

        double resultado =
                calculadora.calcularTotalFinal(100000);

        assertEquals(90000, resultado, 0.001);
    }

    @Test
    public void debeIgnorarEstrategiasNulas() {
        CalculadoraDescuentos calculadora =
                new CalculadoraDescuentos();

        calculadora.agregarDescuento(null);

        assertEquals(
                0,
                calculadora.calcularTotalDescuentos(100000),
                0.001
        );
    }
}
