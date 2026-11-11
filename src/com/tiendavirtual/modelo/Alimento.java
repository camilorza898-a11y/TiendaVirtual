
package com.tiendavirtual.modelo;

import java.time.LocalDate;

public class Alimento extends Producto {

    private LocalDate fechaVencimiento;
    private String tipoAlimento;

    public Alimento(int id, String nombre, double precio, int stock,
                    LocalDate fechaVencimiento, String tipoAlimento) {

        super(id, nombre, precio, stock);
        this.fechaVencimiento = fechaVencimiento;
        this.tipoAlimento = tipoAlimento;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public String getTipoAlimento() {
        return tipoAlimento;
    }

    @Override
    public String getCategoria() {
        return "Alimentos";
    }

    @Override
    public void mostrarInformacion() {
        super.mostrarInformacion();
        System.out.println("Fecha de vencimiento: " + fechaVencimiento);
        System.out.println("Tipo de alimento: " + tipoAlimento);
    }
}
