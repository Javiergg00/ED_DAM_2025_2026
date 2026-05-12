package com.model;

public class Mascota {
    private int id;
    private String nombre;
    private String especie;
    private String raza;
    private String estado;
    private String sintomas;
    private String receta;
    private int edad;
    private int idCliente;
    private String veterinario;
    private boolean enREIAC;

    public Mascota(int id, String nombre, String especie, String raza, int edad, int idCliente) {
        this.id = id;
        this.nombre = nombre;
        this.especie = especie;
        this.raza = raza;
        this.edad = edad;
        this.idCliente = idCliente;
        this.veterinario = "Sin asignar";
        this.enREIAC = false;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getEspecie() { return especie; }
    public String getRaza() { return raza; }
    public int getEdad() { return edad; }
    public int getIdCliente() { return idCliente; }
    public String getVeterinario() { return veterinario; }
    public boolean isEnREIAC() { return enREIAC; }

    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setEspecie(String especie) { this.especie = especie; }
    public void setRaza(String raza) { this.raza = raza; }
    public void setEdad(int edad) { this.edad = edad; }
    public void setVeterinario(String veterinario) { this.veterinario = veterinario; }
    public void setEnREIAC(boolean enREIAC) { this.enREIAC = enREIAC; }

    @Override
    public String toString() {
        return id + " - " + nombre + " (" + especie + ")";
    }
}