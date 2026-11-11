
package com.tiendavirtual.extras;

public abstract class DecoradorProducto implements ProductoComponent {

    protected ProductoComponent producto;

    public DecoradorProducto(ProductoComponent producto) {
        this.producto = producto;
    }

    @Override
    public String getDescripcion() {
        return producto.getDescripcion();
    }

    @Override
    public double getPrecio() {
        return producto.getPrecio();
    }
}
