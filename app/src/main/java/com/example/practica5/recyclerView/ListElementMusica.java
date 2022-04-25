package com.example.practica5.recyclerView;

import java.io.Serializable;

public class ListElementMusica implements Serializable {

    String titol;
    int durada;
    int id;

    public ListElementMusica(String titol, int durada, int path) {
        this.titol = titol;
        this.durada = durada;
        this.id = path;
    }

    public String getTitol() {
        return titol;
    }

    public void setTitol(String titol) {
        this.titol = titol;
    }

    public int getDurada() {
        return durada;
    }

    public void setDurada(int durada) {
        this.durada = durada;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
