
package com.tiendavirtual.modelo;

public class Tecnologia extends Producto {

    private String marca;
    private int garantiaMeses;

    public Tecnologia(int id, String nombre, double precio, int stock,
                      String marca, int garantiaMeses) {

        super(id, nombre, precio, stock);
        this.marca = marca;
        this.garantiaMeses = garantiaMeses;
    }

    public String getMarca() {
        return marca;
    }

    public int getGarantiaMeses() {
        return garantiaMeses;
    }

    @Override
    public String getCategoria() {
        return "Tecnología";
    }

    @Override
    public void mostrarInformacion() {
        super.mostrarInformacion();
        System.out.println("Marca: " + marca);
        System.out.println("Garantía: " + garantiaMeses + " meses");
    }
}
