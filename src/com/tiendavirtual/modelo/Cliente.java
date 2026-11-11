
package com.tiendavirtual.modelo;

public class Cliente {

    private int id;
    private String nombre;
    private String correo;
    private String telefono;
    private String direccion;

    public Cliente(int id, String nombre, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.telefono = telefono;
        this.correo = "No registrado";
        this.direccion = "No registrada";
    }

    public Cliente(int id, String nombre, String correo,
                   String telefono, String direccion) {

        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.telefono = telefono;
        this.direccion = direccion;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        if (direccion != null && !direccion.isBlank()) {
            this.direccion = direccion;
        }
    }

    public void mostrarInformacion() {
        System.out.println("ID: " + id);
        System.out.println("Nombre: " + nombre);
        System.out.println("Correo: " + correo);
        System.out.println("Teléfono: " + telefono);
        System.out.println("Dirección: " + direccion);
    }
}

