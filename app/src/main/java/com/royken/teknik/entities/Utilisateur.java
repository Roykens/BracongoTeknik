package com.royken.teknik.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by royken on 22/12/16.
 */
@DatabaseTable(tableName = "utilisateur")
public class Utilisateur implements Serializable {

    private static final long serialVersionUID = -222864131214757023L;

    @DatabaseField
    private int id;

    @Expose(serialize = true, deserialize = true)
    @SerializedName("id")
    @DatabaseField
    private int idServeur;

    @Expose(serialize = true, deserialize = true)
    @SerializedName("login")
    @DatabaseField
    private String login;

    @Expose(serialize = true, deserialize = true)
    @SerializedName("password")
    @DatabaseField
    private String mdp;

    @Expose(serialize = true, deserialize = true)
    @SerializedName("role")
    @DatabaseField
    private String role;

    @Expose(serialize = true, deserialize = true)
    @SerializedName("cahier")
    @DatabaseField
    private String cahier;

    @Expose(serialize = true, deserialize = true)
    @SerializedName("nom")
    @DatabaseField
    private String nom;

    public Utilisateur() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getMdp() {
        return mdp;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getCahier() {
        return cahier;
    }

    public void setCahier(String cahier) {
        this.cahier = cahier;
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
}
