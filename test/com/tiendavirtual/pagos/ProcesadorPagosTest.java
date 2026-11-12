
package com.tiendavirtual.pagos;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProcesadorPagosTest {

    @Test
    public void debeProcesarPagoCorrectamente() {
        MetodoPago metodo = new MetodoPago() {
            public boolean procesarPago(double monto) {
                return monto > 0;
            }

            public String getNombreMetodo() {
                return "Prueba";
            }
        };

        ProcesadorPagos procesador = new ProcesadorPagos(metodo);

        assertTrue(procesador.ejecutarPago(50000));
    }

    @Test
    public void debeCambiarMetodoDePago() {
        MetodoPago metodo = new MetodoPago() {
            public boolean procesarPago(double monto) {
                return true;
            }

            public String getNombreMetodo() {
                return "Nuevo método";
            }
        };

        ProcesadorPagos procesador =
                new ProcesadorPagos(metodo);

        assertEquals(
                "Nuevo método",
                procesador.getNombreMetodoPago()
        );
    }

    @Test
    public void noDebeAceptarUnMetodoNuloAlCambiar() {
        MetodoPago metodo = new MetodoPago() {
            public boolean procesarPago(double monto) {
                return true;
            }

            public String getNombreMetodo() {
                return "Prueba";
            }
        };

        ProcesadorPagos procesador =
                new ProcesadorPagos(metodo);

        procesador.setMetodoPago(null);

        assertEquals("Prueba", procesador.getNombreMetodoPago());
    }

    @Test
    public void debeRechazarPagoSinMetodo() {
        ProcesadorPagos procesador =
                new ProcesadorPagos(null);

        assertFalse(procesador.ejecutarPago(50000));
        assertEquals(
                "No especificado",
                procesador.getNombreMetodoPago()
        );
    }

    @Test
    public void debeObtenerElNombreDelMetodo() {
        MetodoPago metodo = new MetodoPago() {
            public boolean procesarPago(double monto) {
                return true;
            }

            public String getNombreMetodo() {
                return "Tarjeta de prueba";
            }
        };

        ProcesadorPagos procesador =
                new ProcesadorPagos(metodo);

        assertEquals(
                "Tarjeta de prueba",
                procesador.getNombreMetodoPago()
        );
    }
}
