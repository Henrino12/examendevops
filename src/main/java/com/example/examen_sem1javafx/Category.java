package com.example.examen_sem1javafx;

public class Category {
    private int id;
    private String libelle;

    // Constructeur
    public Category(int id, String libelle) {
        this.id = id;
        this.libelle = libelle;
    }

    // Méthodes d'accès pour l'ID
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Méthodes d'accès pour le libellé
    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }
}
