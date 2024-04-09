package com.example.examen_sem1javafx;

public class Produit {


    public void setId(int id) {
        this.id = id;
    }

    private int id;

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public void setCategorieId(int categorieId) {
        this.categorieId = categorieId;
    }

    private String libelle;
    private int quantite;
    private double prix;
    private int categorieId;

    // Constructeur
    public Produit(int id, String libelle, int quantite, double prix, int categorieId) {
        this.id=id;
        this.libelle = libelle;
        this.quantite = quantite;
        this.prix = prix;
        this.categorieId = categorieId;
    }

    public int getId() {
        return id;
    }
    public String getLibelle() {
        return libelle;
    }

    public int getQuantite() {
        return quantite;
    }

    public double getPrix() {
        return prix;
    }

    public int getCategorieId() {
        return categorieId;
    }


}


