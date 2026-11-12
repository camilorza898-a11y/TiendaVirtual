
package com.tiendavirtual.pagos;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MetodosPagoTest {

    // PRUEBAS DE PAGO CON TARJETA

    @Test
    public void tarjetaDebeProcesarPagoValido() {
        PagoTarjeta tarjeta = new PagoTarjeta(
                "1234567890123456",
                "Juan Perez",
                "Visa",
                "Credito"
        );

        assertTrue(tarjeta.procesarPago(100000));
    }

    @Test
    public void tarjetaDebeRechazarDatosInvalidos() {
        PagoTarjeta tarjeta = new PagoTarjeta(
                "",
                "Juan Perez",
                "Visa",
                "Credito"
        );

        assertFalse(tarjeta.procesarPago(100000));
    }

    @Test
    public void tarjetaDebeRechazarMontoCero() {
        PagoTarjeta tarjeta = new PagoTarjeta(
                "1234567890123456",
                "Juan Perez",
                "Visa",
                "Credito"
        );

        assertFalse(tarjeta.procesarPago(0));
    }

    @Test
    public void tarjetaDebeMostrarSusDetalles() {
        PagoTarjeta tarjeta = new PagoTarjeta(
                "1234567890123456",
                "Juan Perez",
                "Visa",
                "Credito"
        );

        assertEquals("Visa - Credito", tarjeta.getNombreMetodo());
        assertEquals(
                "Franquicia: Visa | Modalidad: Credito",
                tarjeta.getDetallePago()
        );
    }

    // PRUEBAS DE TRANSFERENCIA

    @Test
    public void transferenciaDebeProcesarPagoValido() {
        PagoTransferencia transferencia =
                new PagoTransferencia("Nequi", "REF123");

        assertTrue(transferencia.procesarPago(50000));
    }

    @Test
    public void transferenciaDebeRechazarDatosInvalidos() {
        PagoTransferencia transferencia =
                new PagoTransferencia("", "REF123");

        assertFalse(transferencia.procesarPago(50000));
    }

    @Test
    public void transferenciaDebeRechazarMontoCero() {
        PagoTransferencia transferencia =
                new PagoTransferencia("Nequi", "REF123");

        assertFalse(transferencia.procesarPago(0));
    }

    @Test
    public void transferenciaDebeMostrarSusDetalles() {
        PagoTransferencia transferencia =
                new PagoTransferencia("Nequi", "REF123");

        assertEquals(
                "Transferencia - Nequi",
                transferencia.getNombreMetodo()
        );

        assertEquals(
                "Plataforma: Nequi | Referencia: REF123",
                transferencia.getDetallePago()
        );
    }

    // PRUEBAS DE PAGO CONTRA ENTREGA

    @Test
    public void contraEntregaDebeProcesarPagoValido() {
        PagoContraEntrega pago =
                new PagoContraEntrega("Calle 10 # 20-30");

        assertTrue(pago.procesarPago(80000));
    }

    @Test
    public void contraEntregaDebeRechazarDireccionInvalida() {
        PagoContraEntrega pago =
                new PagoContraEntrega("");

        assertFalse(pago.procesarPago(80000));
    }

    @Test
    public void contraEntregaDebeRechazarMontoCero() {
        PagoContraEntrega pago =
                new PagoContraEntrega("Calle 10 # 20-30");

        assertFalse(pago.procesarPago(0));
    }

    @Test
    public void contraEntregaDebeMostrarSusDetalles() {
        PagoContraEntrega pago =
                new PagoContraEntrega("Calle 10 # 20-30");

        assertEquals(
                "Pago contra entrega",
                pago.getNombreMetodo()
        );

        assertEquals(
                "Dirección de entrega: Calle 10 # 20-30",
                pago.getDetallePago()
        );
    }
}
