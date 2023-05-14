package com.gina.takecare4u.modelos;

public class Users {
    private String id;
    private String email;
    private String zipCode;
    private String nombre;
    private String password;
    private String imageProfile;
    private String imageCover;
    private String phone;
    private long timestamp;
    private long lastConnection;

    private boolean online;


    // constructor clase user con getter y setter
    // constructor vacio
    public Users(){

    }

    public Users(String id, String email, String zipCode, String nombre, String password, String imageProfile, String imageCover, String phone, long timestamp, long lastConnection, boolean online) {
        this.id = id;
        this.email = email;
        this.zipCode = zipCode;
        this.nombre = nombre;
        this.password = password;
        this.imageProfile = imageProfile;
        this.imageCover = imageCover;
        this.phone = phone;
        this.timestamp = timestamp;
        this.lastConnection = lastConnection;
        this.online = online;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getImageProfile() {
        return imageProfile;
    }

    public void setImageProfile(String imageprofile) {
        this.imageProfile = imageprofile;
    }

    public String getImageCover() {
        return imageCover;
    }

    public void setImageCover(String imageCover) {
        this.imageCover = imageCover;
    }

    public long getLastConnection() {
        return lastConnection;
    }

    public void setLastConnection(long lastConnection) {
        this.lastConnection = lastConnection;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }
}
