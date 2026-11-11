
package com.tiendavirtual.extras;

public class GarantiaExtendida extends DecoradorProducto {

    private double costoGarantia;

    public GarantiaExtendida(ProductoComponent producto,
                             double costoGarantia) {
        super(producto);
        this.costoGarantia = costoGarantia;
    }

    @Override
    public String getDescripcion() {
        return producto.getDescripcion() + " + Garantía extendida";
    }

    @Override
    public double getPrecio() {
        return producto.getPrecio() + costoGarantia;
    }
}
