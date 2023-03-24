package com.gina.takecare4u.modelos;

public class Users {
    private String id;
    private String email;
    private String zipCode;
    private String nombre;
    private String password;

    // constructor clase user con getter y setter
    // constructor vacio
    public Users(){

    }

    public Users(String id, String nombre, String zipCode, String email) {
        this.id = id;
        this.email = email;
        this.zipCode = zipCode;
        this.nombre = nombre;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
