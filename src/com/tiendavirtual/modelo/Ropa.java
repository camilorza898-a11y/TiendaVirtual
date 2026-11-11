
package com.tiendavirtual.modelo;

public class Ropa extends Producto {

    private String talla;
    private String material;

    public Ropa(int id, String nombre, double precio, int stock,
                String talla, String material) {

        super(id, nombre, precio, stock);
        this.talla = talla;
        this.material = material;
    }

    public String getTalla() {
        return talla;
    }

    public String getMaterial() {
        return material;
    }

    @Override
    public String getCategoria() {
        return "Ropa";
    }

    @Override
    public void mostrarInformacion() {
        super.mostrarInformacion();
        System.out.println("Talla: " + talla);
        System.out.println("Material: " + material);
    }
}
