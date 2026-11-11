
package com.tiendavirtual.extras;

public class EnvioExpress extends DecoradorProducto {

    private double costoEnvio;

    public EnvioExpress(ProductoComponent producto,
                        double costoEnvio) {
        super(producto);
        this.costoEnvio = costoEnvio;
    }

    @Override
    public String getDescripcion() {
        return producto.getDescripcion() + " + Envío exprés";
    }

    @Override
    public double getPrecio() {
        return producto.getPrecio() + costoEnvio;
    }
}
