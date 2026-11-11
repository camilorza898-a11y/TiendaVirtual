
package com.tiendavirtual.extras;

public class EmpaqueRegalo extends DecoradorProducto {

    private double costoEmpaque;

    public EmpaqueRegalo(ProductoComponent producto,
                         double costoEmpaque) {
        super(producto);
        this.costoEmpaque = costoEmpaque;
    }

    @Override
    public String getDescripcion() {
        return producto.getDescripcion() + " + Empaque de regalo";
    }

    @Override
    public double getPrecio() {
        return producto.getPrecio() + costoEmpaque;
    }
}
