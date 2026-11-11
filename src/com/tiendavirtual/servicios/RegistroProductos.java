package com.tiendavirtual.servicios;

import com.tiendavirtual.modelo.Ropa;
import com.tiendavirtual.modelo.Tecnologia;
import com.tiendavirtual.modelo.Alimento;
import java.time.LocalDate;

public class RegistroProductos {

    public static void registrar(CatalogoService catalogoService) {

        catalogoService.agregarProducto(
            new Ropa(1, "Camiseta negra", 45000, 15, "M", "Algodon")
        );

        catalogoService.agregarProducto(
            new Ropa(2, "Sudadera deportiva", 85000, 10, "L", "Poliester")
        );

        catalogoService.agregarProducto(
            new Tecnologia(3, "Audifonos Bluetooth", 120000, 8, "JBL", 12)
        );

        catalogoService.agregarProducto(
            new Tecnologia(4, "Mouse inalambrico", 65000, 12, "Logitech", 6)
        );

        catalogoService.agregarProducto(
            new Alimento(5, "Arroz 1 kg", 5000, 30,
                LocalDate.of(2027, 12, 31), "Grano")
        );

        catalogoService.agregarProducto(
            new Alimento(6, "Galletas de chocolate", 3500, 25,
                LocalDate.of(2027, 6, 30), "Dulce")
        );
    }
}