package com.tiendavirtual.descuentos;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

public class CalculadoraDescuentos {

    private List<EstrategiaDescuento> estrategias;

    public CalculadoraDescuentos() {
        estrategias = new ArrayList<>();
    }

    public void agregarDescuento(EstrategiaDescuento estrategia) {
        if (estrategia != null) {
            estrategias.add(estrategia);
        }
    }

    public void eliminarDescuentoCupon() {
        estrategias.removeIf(
            estrategia -> estrategia instanceof DescuentoCupon
        );
    }

    public double calcularTotalDescuentos(double subtotal) {

        double descuentoTotal = 0;

        for (EstrategiaDescuento estrategia : estrategias) {
            descuentoTotal += estrategia.calcularDescuento(subtotal);
        }

        return Math.min(descuentoTotal, subtotal);
    }

    public double calcularTotalDescuentos(
            double subtotal, int cantidadProductos) {

        double descuentoTotal = 0;

        for (EstrategiaDescuento estrategia : estrategias) {

            if (estrategia instanceof DescuentoPorCantidad) {

                DescuentoPorCantidad descuento =
                    (DescuentoPorCantidad) estrategia;

                descuentoTotal += descuento.calcularDescuento(
                    subtotal, cantidadProductos
                );

            } else {

                descuentoTotal += estrategia.calcularDescuento(subtotal);
            }
        }

        return Math.min(descuentoTotal, subtotal);
    }

    public double calcularTotalFinal(double subtotal) {
        return subtotal - calcularTotalDescuentos(subtotal);
    }

    public double calcularTotalFinal(
            double subtotal, int cantidadProductos) {

        return subtotal - calcularTotalDescuentos(
            subtotal, cantidadProductos
        );
    }

    public void mostrarDescuentos(
            double subtotal, int cantidadProductos) {

        System.out.println("\n===== DESCUENTOS =====");

        for (EstrategiaDescuento estrategia : estrategias) {
            System.out.println("- " + estrategia.getNombreDescuento());
        }

        System.out.println(
            "Total descuentos: $"
            + calcularTotalDescuentos(subtotal, cantidadProductos)
        );
    }
    public Map<String, Double> obtenerDetalleDescuentos(
        double subtotal, int cantidadProductos) {

    Map<String, Double> detalles = new LinkedHashMap<>();

    for (EstrategiaDescuento estrategia : estrategias) {

        double valorDescuento;

        if (estrategia instanceof DescuentoPorCantidad) {

            DescuentoPorCantidad descuento =
                    (DescuentoPorCantidad) estrategia;

            valorDescuento = descuento.calcularDescuento(
                    subtotal, cantidadProductos
            );

        } else {

            valorDescuento = estrategia.calcularDescuento(subtotal);
        }

        if (valorDescuento > 0) {
            detalles.put(
                    estrategia.getNombreDescuento(),
                    valorDescuento
            );
        }
    }

    return detalles;
    }
}