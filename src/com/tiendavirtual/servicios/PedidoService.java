
package com.tiendavirtual.servicios;

import com.tiendavirtual.modelo.Carrito;
import com.tiendavirtual.modelo.Pedido;
import com.tiendavirtual.modelo.Producto;
import com.tiendavirtual.descuentos.CalculadoraDescuentos;
import com.tiendavirtual.pagos.ProcesadorPagos;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class PedidoService {

    private static final AtomicInteger CONTADOR = new AtomicInteger(1);

    private InventarioService inventarioService;
    private CalculadoraDescuentos calculadoraDescuentos;
    private ProcesadorPagos procesadorPagos;
    private List<Pedido> pedidos;

    public PedidoService(InventarioService inventarioService,
                         CalculadoraDescuentos calculadoraDescuentos,
                         ProcesadorPagos procesadorPagos) {

        this.inventarioService = inventarioService;
        this.calculadoraDescuentos = calculadoraDescuentos;
        this.procesadorPagos = procesadorPagos;
        this.pedidos = new ArrayList<>();
    }

    public Pedido confirmarPedido(Carrito carrito) {

        if (carrito == null || carrito.estaVacio()) {
            return null;
        }

        Map<Producto, Integer> cantidades = carrito.getProductos();

        // 1. Verificar inventario
        for (Map.Entry<Producto, Integer> entrada : cantidades.entrySet()) {

            if (!inventarioService.hayExistencias(
                    entrada.getKey(), entrada.getValue())) {

                System.out.println(
                    "No hay suficiente stock de: "
                    + entrada.getKey().getNombre()
                );

                return null;
            }
        }

        // 2. Calcular subtotal y cantidad
        double subtotal = carrito.calcularSubtotal();

        int cantidadProductos = cantidades.values()
                .stream()
                .mapToInt(Integer::intValue)
                .sum();

        // 3. Calcular descuentos una sola vez
        Map<String, Double> descuentosAplicados =
                calculadoraDescuentos.obtenerDetalleDescuentos(
                    subtotal, cantidadProductos
                );

        double totalDescuentos = 0;

        for (double descuento : descuentosAplicados.values()) {
            totalDescuentos += descuento;
        }

        double total = Math.max(0, subtotal - totalDescuentos);

        // 4. Procesar pago
        boolean pagoAprobado = procesadorPagos.ejecutarPago(total);

        if (!pagoAprobado) {
            System.out.println("El pago no pudo ser aprobado.");
            return null;
        }

        // 5. Descontar inventario
        for (Map.Entry<Producto, Integer> entrada : cantidades.entrySet()) {
            inventarioService.reducirStock(
                entrada.getKey(), entrada.getValue()
            );
        }

        // 6. Crear pedido
        Pedido pedido = new Pedido(
            CONTADOR.getAndIncrement(),
            carrito.getCliente(),
            cantidades,
            carrito.getProductosDecorados(),
            total
        );

        // 7. Guardar información del pago y descuentos
        pedido.setMetodoPago(
            procesadorPagos.getNombreMetodoPago(),
            procesadorPagos.getDetalleMetodoPago()
        );

        pedido.setDescuentosAplicados(descuentosAplicados);

        // 8. Confirmar pedido
        pedido.cambiarEstado("Confirmado");

        pedidos.add(pedido);

        return pedido;
    }

    public List<Pedido> obtenerPedidos() {
        return new ArrayList<>(pedidos);
    }
}
