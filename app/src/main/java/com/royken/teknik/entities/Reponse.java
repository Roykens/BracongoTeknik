package com.royken.teknik.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by royken on 29/12/16.
 */
@DatabaseTable(tableName = "reponse")
public class Reponse implements Serializable{

    private static final long serialVersionUID = -22286413121475724L;

    @Expose(serialize = false, deserialize = false)
    @DatabaseField
    private int id;

    @DatabaseField
    @Expose(serialize = false, deserialize = false)
    private String code;

    @DatabaseField
    @Expose(serialize = false, deserialize = false)
    private String nom;

    @DatabaseField
    @Expose
    @SerializedName("valeur")
    private String valeur;

    @DatabaseField
    @Expose(serialize = false, deserialize = false)
    private int compteur;

    @DatabaseField
    @Expose
    @SerializedName("idElement")
    private int idElement;

    @DatabaseField(dataType = DataType.DATE_STRING, format = "yyyy-MM-dd HH:mm:ss")
    @Expose
    @SerializedName("date")
    private Date date;

    @DatabaseField
    private String user;

    @DatabaseField
    @Expose
    @SerializedName("idUser")
    private int idUser;

    @DatabaseField
    @Expose(serialize = false, deserialize = false)
    private String cahier;

    @DatabaseField
    @Expose(serialize = false, deserialize = false)
    private String organe;

    @DatabaseField
    @Expose(serialize = false, deserialize = false)
    private String sousOrgane;

    @DatabaseField
    @Expose
    @SerializedName("valeurCorrecte")
    private boolean valeurCorrecte;

    public Reponse() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getValeur() {
        return valeur;
    }

    public void setValeur(String valeur) {
        this.valeur = valeur;
    }

    public int getCompteur() {
        return compteur;
    }

    public void setCompteur(int compteur) {
        this.compteur = compteur;
    }

    public int getIdElement() {
        return idElement;
    }

    public void setIdElement(int idElement) {
        this.idElement = idElement;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getCahier() {
        return cahier;
    }

    public void setCahier(String cahier) {
        this.cahier = cahier;
    }

    public boolean isValeurCorrecte() {
        return valeurCorrecte;
    }

    public void setValeurCorrecte(boolean valeurCorrecte) {
        this.valeurCorrecte = valeurCorrecte;
    }

    public String getOrgane() {
        return organe;
    }

    public void setOrgane(String organe) {
        this.organe = organe;
    }

    public String getSousOrgane() {
        return sousOrgane;
    }

    public void setSousOrgane(String sousOrgane) {
        this.sousOrgane = sousOrgane;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    @Override
    public String toString() {
        return "Reponse{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", nom='" + nom + '\'' +
                ", valeur='" + valeur + '\'' +
                ", compteur=" + compteur +
                ", idElement=" + idElement +
                ", date=" + date +
                ", user='" + user + '\'' +
                '}';
    }
}
