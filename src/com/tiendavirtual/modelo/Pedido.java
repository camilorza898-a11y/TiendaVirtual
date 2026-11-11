
package com.tiendavirtual.modelo;

import com.tiendavirtual.extras.ProductoComponent;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

public class Pedido {

    private int id;
    private Cliente cliente;
    private List<Producto> productos;
    private Map<Producto, Integer> cantidades;
    private Map<Producto, ProductoComponent> productosDecorados;
    private List<String> detallesExtras;
    private double subtotal;
    private double total;
    private Map<String, Double> descuentosAplicados;
    private String estado;
    private LocalDateTime fecha;
    private String metodoPago;
    private String detallePago;

    public Pedido(int id, Cliente cliente, List<Producto> productos,
                  double total) {

        this.id = id;
        this.cliente = cliente;
        this.productos = new ArrayList<>(productos);
        this.cantidades = new LinkedHashMap<>();
        this.productosDecorados = new LinkedHashMap<>();
        this.detallesExtras = new ArrayList<>();
        this.descuentosAplicados = new LinkedHashMap<>();

        for (Producto producto : productos) {
            cantidades.put(
                producto,
                cantidades.getOrDefault(producto, 0) + 1
            );
        }

        this.subtotal = 0;

        for (Producto producto : productos) {
            this.subtotal += producto.getPrecio();
        }

        this.total = total;
        this.estado = "Pendiente";
        this.fecha = LocalDateTime.now();
    }

    public Pedido(int id, Cliente cliente,
                  Map<Producto, Integer> cantidades,
                  Map<Producto, ProductoComponent> productosDecorados,
                  double total) {

        this.id = id;
        this.cliente = cliente;
        this.cantidades = new LinkedHashMap<>(cantidades);
        this.productosDecorados = new LinkedHashMap<>(productosDecorados);
        this.productos = new ArrayList<>();
        this.detallesExtras = new ArrayList<>();
        this.subtotal = 0;

        for (Map.Entry<Producto, Integer> entrada : cantidades.entrySet()) {

            Producto producto = entrada.getKey();
            int cantidad = entrada.getValue();

            for (int i = 0; i < cantidad; i++) {
                productos.add(producto);
            }

            ProductoComponent decorado = productosDecorados.get(producto);

            if (decorado != null) {
                subtotal += decorado.getPrecio() * cantidad;

                if (!decorado.getDescripcion().equals(producto.getNombre())) {
                    detallesExtras.add(
                        decorado.getDescripcion()
                        + " x" + cantidad
                    );
                }
            } else {
                subtotal += producto.getPrecio() * cantidad;
            }
        }

        this.total = total;
        this.estado = "Pendiente";
        this.fecha = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public List<Producto> getProductos() {
        return new ArrayList<>(productos);
    }

    public Map<Producto, Integer> getCantidades() {
        return new LinkedHashMap<>(cantidades);
    }

    public Map<Producto, ProductoComponent> getProductosDecorados() {
        return new LinkedHashMap<>(productosDecorados);
    }

    public List<String> getDetallesExtras() {
        return new ArrayList<>(detallesExtras);
    }

    public double getSubtotal() {
        return subtotal;
    }

    public double getTotal() {
        return total;
    }
    public void setDescuentosAplicados(Map<String, Double> descuentos) {
    this.descuentosAplicados = new LinkedHashMap<>(descuentos);
    }

    public Map<String, Double> getDescuentosAplicados() {
        return new LinkedHashMap<>(descuentosAplicados);
    }

    public String getEstado() {
        return estado;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }
    
    public void setMetodoPago(String metodoPago, String detallePago) {
    this.metodoPago = metodoPago;
    this.detallePago = detallePago;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public String getDetallePago() {
        return detallePago;
    }

    public void agregarDetalleExtra(String detalle) {
        detallesExtras.add(detalle);
    }

    public void cambiarEstado(String estado) {
        this.estado = estado;
    }

    public void mostrarPedido() {

        System.out.println("\n===== DETALLE DEL PEDIDO =====");
        System.out.println("Pedido: " + id);
        System.out.println("Cliente: " + cliente.getNombre());

        for (Map.Entry<Producto, Integer> entrada : cantidades.entrySet()) {

            Producto producto = entrada.getKey();
            int cantidad = entrada.getValue();

            ProductoComponent decorado = productosDecorados.get(producto);

            if (decorado != null) {
                System.out.println(
                    decorado.getDescripcion()
                    + " x" + cantidad
                    + " - $" + (decorado.getPrecio() * cantidad)
                );
            } else {
                System.out.println(
                    producto.getNombre()
                    + " x" + cantidad
                    + " - $" + (producto.getPrecio() * cantidad)
                );
            }
        }

        System.out.println("Subtotal: $" + subtotal);
        System.out.println("Total: $" + total);
        System.out.println("Estado: " + estado);
    }
}
