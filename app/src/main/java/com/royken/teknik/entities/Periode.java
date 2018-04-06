package com.royken.teknik.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by royken on 05/04/18.
 */
@DatabaseTable(tableName = "periode")
public class Periode implements Serializable{

    private static final long serialVersionUID = -222864131214757024L;

    @DatabaseField
    private int id ;

    @Expose(serialize = true, deserialize = true)
    @SerializedName("id")
    @DatabaseField
    private int idServeur;

    @Expose(serialize = true, deserialize = true)
    @SerializedName("nom")
    @DatabaseField
    private String nom;

    public Periode() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdServeur() {
        return idServeur;
    }

    public void setIdServeur(int idServeur) {
        this.idServeur = idServeur;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}
