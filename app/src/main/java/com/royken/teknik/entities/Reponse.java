package com.royken.teknik.entities;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by royken on 29/12/16.
 */
@DatabaseTable
public class Reponse implements Serializable{

    private static final long serialVersionUID = -22286413121475724L;

    @DatabaseField
    private int id;

    @DatabaseField
    private String code;

    @DatabaseField
    private String nom;

    @DatabaseField
    private String valeur;

    @DatabaseField
    private int compteur;

    @DatabaseField
    private int idElement;

    @DatabaseField(dataType = DataType.DATE_STRING, format = "yyyy-MM-dd HH:mm:ss")
    private Date date;

    @DatabaseField
    private String user;

    @DatabaseField
    private String cahier;

    @DatabaseField
    private String organe;

    @DatabaseField
    private String sousOrgane;

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
