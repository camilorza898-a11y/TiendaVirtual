
package com.tiendavirtual.extras;

public class ProductoBase implements ProductoComponent {

    private String descripcion;
    private double precio;

    public ProductoBase(String descripcion, double precio) {
        this.descripcion = descripcion;
        this.precio = precio;
    }

    @Override
    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public double getPrecio() {
        return precio;
    }
}
