
package com.tiendavirtual.descuentos;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DescuentoPorCantidadTest {

    @Test
    public void debeAplicarDescuentoCuandoCumpleCantidadMinima() {
        DescuentoPorCantidad descuento =
                new DescuentoPorCantidad(3, 10);

        double resultado = descuento.calcularDescuento(100000, 3);

        assertEquals(10000, resultado, 0.001);
    }

    @Test
    public void noDebeAplicarDescuentoCuandoNoCumpleCantidad() {
        DescuentoPorCantidad descuento =
                new DescuentoPorCantidad(3, 10);

        double resultado = descuento.calcularDescuento(100000, 2);

        assertEquals(0, resultado, 0.001);
    }

    @Test
    public void debeRetornarNombreDelDescuento() {
        DescuentoPorCantidad descuento =
                new DescuentoPorCantidad(3, 10);

        assertEquals(
                "Descuento por cantidad",
                descuento.getNombreDescuento()
        );
    }
}
