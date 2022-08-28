package com.grupom.pm1erc201930010343.Config;

public class Usuario {

    public int id;
    public String nombre;
    public int telefono;
    public String Latitud;
    public String Longitud;
    public String firma;

    public Usuario(int id, String nombre, int telefono, String latitud, String longitud, String firma) {
        this.id = id;
        this.nombre = nombre;
        this.telefono = telefono;
        Latitud = latitud;
        Longitud = longitud;
        this.firma = firma;
    }

    public Usuario() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    public String getLatitud() {
        return Latitud;
    }

    public void setLatitud(String latitud) {
        Latitud = latitud;
    }

    public String getLongitud() {
        return Longitud;
    }

    public void setLongitud(String longitud) {
        Longitud = longitud;
    }

    public String getFirma() {
        return firma;
    }

    public void setFirma(String firma) {
        this.firma = firma;
    }
}
