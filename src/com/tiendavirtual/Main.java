
package com.tiendavirtual;

import com.tiendavirtual.consola.MenuConsola;

public class Main {

    public static void main(String[] args) {

        System.out.println("Iniciando Tienda Virtual...");

        MenuConsola menu = new MenuConsola();

        menu.mostrarMenu();
    }
}
