package com.royken.teknik.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by royken on 22/12/16.
 */
@DatabaseTable(tableName = "organe")
public class Organe implements Serializable {

    private static final long serialVersionUID = -22286413121477024L;

    @DatabaseField
    private int id;

    @Expose(serialize = true, deserialize = true)
    @SerializedName("id")
    @DatabaseField
    private int idServeur;

    @Expose(serialize = true, deserialize = true)
    @SerializedName("idBloc")
    @DatabaseField
    private int idBloc;

    @Expose(serialize = true, deserialize = true)
    @SerializedName("code")
    @DatabaseField
    private String code;

    @Expose(serialize = true, deserialize = true)
    @SerializedName("nom")
    @DatabaseField
    private String nom;

    public Organe() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdBloc() {
        return idBloc;
    }

    public void setIdBloc(int idBloc) {
        this.idBloc = idBloc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getIdServeur() {
        return idServeur;
    }

    public void setIdServeur(int idServeur) {
        this.idServeur = idServeur;
    }

    @Override
    public String toString() {
        return "Organe{" +
                "id=" + id +
                ", idServeur=" + idServeur +
                ", idBloc=" + idBloc +
                ", code='" + code + '\'' +
                ", nom='" + nom + '\'' +
                '}';
    }
}
