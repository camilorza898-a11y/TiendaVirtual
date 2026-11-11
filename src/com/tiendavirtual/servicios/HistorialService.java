
package com.tiendavirtual.servicios;

import com.tiendavirtual.modelo.Cliente;
import com.tiendavirtual.modelo.Pedido;
import java.util.ArrayList;
import java.util.List;

public class HistorialService {

    private List<Pedido> pedidos;

    public HistorialService() {
        pedidos = new ArrayList<>();
    }

    public void registrarPedido(Pedido pedido) {
        if (pedido != null) {
            pedidos.add(pedido);
        }
    }

    public List<Pedido> consultarHistorial(Cliente cliente) {

        List<Pedido> historial = new ArrayList<>();

        if (cliente == null) {
            return historial;
        }

        for (Pedido pedido : pedidos) {

            if (pedido.getCliente().getId() == cliente.getId()) {
                historial.add(pedido);
            }
        }

        return historial;
    }

    public void mostrarHistorial(Cliente cliente) {

        System.out.println("\n===== HISTORIAL DE COMPRAS =====");

        List<Pedido> historial = consultarHistorial(cliente);

        if (historial.isEmpty()) {
            System.out.println("El cliente no tiene compras registradas.");
            return;
        }

        for (Pedido pedido : historial) {
            pedido.mostrarPedido();
            System.out.println("--------------------------");
        }
    }
}
