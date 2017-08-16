package com.royken.teknik.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by royken on 22/12/16.
 */
@DatabaseTable(tableName = "sousorgane")
public class SousOrgane implements Serializable {

    private static final long serialVersionUID = -22286413121475702L;

    @DatabaseField
    private int id;

    @Expose(serialize = true, deserialize = true)
    @SerializedName("id")
    @DatabaseField
    private int idServeur;

    @Expose(serialize = true, deserialize = true)
    @SerializedName("idOrgane")
    @DatabaseField
    private int idOrgane;

    @Expose(serialize = true, deserialize = true)
    @SerializedName("nom")
    @DatabaseField
    private String nom;

    @Expose(serialize = true, deserialize = true)
    @SerializedName("code")
    @DatabaseField
    private String code;

    public SousOrgane() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdOrgane() {
        return idOrgane;
    }

    public void setIdOrgane(int idOrgane) {
        this.idOrgane = idOrgane;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getIdServeur() {
        return idServeur;
    }

    public void setIdServeur(int idServeur) {
        this.idServeur = idServeur;
    }
}
