package com.gina.takecare4u.modelos;

import android.widget.Toast;

import com.gina.takecare4u.activities.SecondActivity;

public class Publicaciones {

    private String id;
    private String nombre;
    private String descripcion;
    private String precio;
    private String imagen1;
    private String imagen2;
    private String idUser;
    private String servicio;
    private long timestamp;


    // constructor vacío
    public Publicaciones(){

    }

    public Publicaciones(String id, String nombre, String descripcion, String precio, String imagen1, String imagen2, String idUser, String servicio, long timestamp) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.imagen1 = imagen1;
        this.imagen2 = imagen2;
        this.idUser = idUser;
        this.servicio = servicio;
        this.timestamp = timestamp;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getImagen1() {
        return imagen1;
    }

    public void setImagen1(String imagen1) {
        this.imagen1 = imagen1;
    }

    public String getImagen2() {
        return imagen2;
    }

    public void setImagen2(String imagen2) {
        this.imagen2 = imagen2;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getServicio() {
        return servicio;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
    }

   public  double ParseDouble(String strNumber) {
        if (strNumber != null && strNumber.length() > 0) {
            try {
                return Double.parseDouble(strNumber);
            } catch(Exception e) {

                return -1.0;
            }
        }
        else return 0.0;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
