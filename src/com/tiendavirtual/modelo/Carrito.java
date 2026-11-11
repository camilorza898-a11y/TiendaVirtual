
package com.tiendavirtual.modelo;

import com.tiendavirtual.extras.ProductoComponent;
import com.tiendavirtual.extras.ProductoBase;
import com.tiendavirtual.extras.GarantiaExtendida;
import com.tiendavirtual.extras.EmpaqueRegalo;
import com.tiendavirtual.extras.EnvioExpress;

import java.util.LinkedHashMap;
import java.util.Map;

public class Carrito {

    private Cliente cliente;
    private Map<Producto, Integer> productos;
    private Map<Producto, ProductoComponent> productosDecorados;

    public Carrito(Cliente cliente) {
        this.cliente = cliente;
        this.productos = new LinkedHashMap<>();
        this.productosDecorados = new LinkedHashMap<>();
    }

    public void agregarProducto(Producto producto) {
        agregarProducto(producto, 1);
    }

    public void agregarProducto(Producto producto, int cantidad) {

        if (producto != null && cantidad > 0) {

            productos.put(
                producto,
                productos.getOrDefault(producto, 0) + cantidad
            );

            if (!productosDecorados.containsKey(producto)) {
                productosDecorados.put(
                    producto,
                    new ProductoBase(
                        producto.getNombre(),
                        producto.getPrecio()
                    )
                );
            }
        }
    }

    public void agregarGarantia(Producto producto, double costo) {

        if (productos.containsKey(producto)) {
            ProductoComponent decorado = obtenerDecorado(producto);
            productosDecorados.put(
                producto,
                new GarantiaExtendida(decorado, costo)
            );
        }
    }

    public void agregarEmpaque(Producto producto, double costo) {

        if (productos.containsKey(producto)) {
            ProductoComponent decorado = obtenerDecorado(producto);
            productosDecorados.put(
                producto,
                new EmpaqueRegalo(decorado, costo)
            );
        }
    }

    public void agregarEnvioExpress(Producto producto, double costo) {

        if (productos.containsKey(producto)) {
            ProductoComponent decorado = obtenerDecorado(producto);
            productosDecorados.put(
                producto,
                new EnvioExpress(decorado, costo)
            );
        }
    }

    private ProductoComponent obtenerDecorado(Producto producto) {

        if (!productosDecorados.containsKey(producto)) {
            productosDecorados.put(
                producto,
                new ProductoBase(
                    producto.getNombre(),
                    producto.getPrecio()
                )
            );
        }

        return productosDecorados.get(producto);
    }

    public ProductoComponent getProductoDecorado(Producto producto) {
        return obtenerDecorado(producto);
    }

    public void actualizarCantidad(Producto producto, int cantidad) {

        if (producto == null) {
            return;
        }

        if (cantidad <= 0) {
            productos.remove(producto);
            productosDecorados.remove(producto);
        } else {
            productos.put(producto, cantidad);
        }
    }

    public void eliminarProducto(int id) {

        productos.keySet().removeIf(producto -> {

            if (producto.getId() == id) {
                productosDecorados.remove(producto);
                return true;
            }

            return false;
        });
    }

    public double calcularSubtotal() {

        double subtotal = 0;

        for (Map.Entry<Producto, Integer> entrada : productos.entrySet()) {

            Producto producto = entrada.getKey();
            int cantidad = entrada.getValue();

            subtotal += obtenerDecorado(producto).getPrecio() * cantidad;
        }

        return subtotal;
    }

    public void mostrarCarrito() {

        System.out.println("\n===== CARRITO DE COMPRAS =====");

        if (productos.isEmpty()) {
            System.out.println("El carrito esta vacio.");
            return;
        }

        for (Map.Entry<Producto, Integer> entrada : productos.entrySet()) {

            Producto producto = entrada.getKey();
            int cantidad = entrada.getValue();

            ProductoComponent decorado = obtenerDecorado(producto);

            System.out.println("----------------------------");
            System.out.println("Producto: " + decorado.getDescripcion());
            System.out.println("Cantidad: " + cantidad);
            System.out.println("Precio unitario: $" + decorado.getPrecio());
            System.out.println("Subtotal: $" + (decorado.getPrecio() * cantidad));
        }

        System.out.println("----------------------------");
        System.out.println("TOTAL: $" + calcularSubtotal());
    }
    
    
    public void establecerProductoDecorado(
            Producto producto,
            ProductoComponent decorado) {

        if (productos.containsKey(producto) && decorado != null) {
            productosDecorados.put(producto, decorado);
        }
    }


    public Cliente getCliente() {
        return cliente;
    }

    public Map<Producto, Integer> getProductos() {
        return new LinkedHashMap<>(productos);
    }

    public Map<Producto, ProductoComponent> getProductosDecorados() {
        return new LinkedHashMap<>(productosDecorados);
    }

    public boolean estaVacio() {
        return productos.isEmpty();
    }
}
