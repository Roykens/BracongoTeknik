package com.royken.teknik.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by royken on 22/12/16.
 */
@DatabaseTable(tableName = "element")
public class Element implements Serializable {

    private static final long serialVersionUID = -22286131214757024L;

    @DatabaseField
    private int id;

    @Expose(serialize = true, deserialize = true)
    @SerializedName("id")
    @DatabaseField
    private int idServeur;

    @Expose(serialize = true, deserialize = true)
    @SerializedName("sousOrganeId")
    @DatabaseField
    private int sousOrganeId;

    @Expose(serialize = true, deserialize = true)
    @SerializedName("code")
    @DatabaseField
    private String code;

    @Expose(serialize = true, deserialize = true)
    @SerializedName("nom")
    @DatabaseField
    private String nom;

    @Expose(serialize = true, deserialize = true)
    @SerializedName("criteriaAlpha")
    @DatabaseField
    private boolean criteriaAlpha;

    @Expose(serialize = true, deserialize = true)
    @SerializedName("valMin")
    @DatabaseField
    private Double valMin;

    @Expose(serialize = true, deserialize = true)
    @SerializedName("valMax")
    @DatabaseField
    private Double valMax;

    @Expose(serialize = true, deserialize = true)
    @SerializedName("guideSaisie")
    @DatabaseField
    private String guideSaisie;

    @Expose(serialize = true, deserialize = true)
    @SerializedName("valeurNormale")
    @DatabaseField
    private String valeurNormale;

    @Expose(serialize = true, deserialize = true)
    @SerializedName("valeurType")

    @DatabaseField
    private String valeurType;

    @Expose(serialize = true, deserialize = true)
    @SerializedName("hasBorn")
    @DatabaseField
    private boolean hasBorn;

    @Expose(serialize = true, deserialize = true)
    @SerializedName("periode")
    @DatabaseField
    private int periode;

    @Expose(serialize = true, deserialize = true)
    @SerializedName("unite")
    @DatabaseField
    private String unite;

    public Element() {
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

    public boolean isCriteriaAlpha() {
        return criteriaAlpha;
    }

    public void setCriteriaAlpha(boolean criteriaAlpha) {
        this.criteriaAlpha = criteriaAlpha;
    }

    public Double getValMin() {
        return valMin;
    }

    public void setValMin(Double valMin) {
        this.valMin = valMin;
    }

    public Double getValMax() {
        return valMax;
    }

    public void setValMax(Double valMax) {
        this.valMax = valMax;
    }

    public String getGuideSaisie() {
        return guideSaisie;
    }

    public void setGuideSaisie(String guideSaisie) {
        this.guideSaisie = guideSaisie;
    }

    public String getValeurNormale() {
        return valeurNormale;
    }

    public void setValeurNormale(String valeurNormale) {
        this.valeurNormale = valeurNormale;
    }

    public int getSousOrganeId() {
        return sousOrganeId;
    }

    public void setSousOrganeId(int sousOrganeId) {
        this.sousOrganeId = sousOrganeId;
    }

    public String getValeurType() {
        return valeurType;
    }

    public void setValeurType(String valeurType) {
        this.valeurType = valeurType;
    }

    public boolean isHasBorn() {
        return hasBorn;
    }

    public void setHasBorn(boolean hasBorn) {
        this.hasBorn = hasBorn;
    }

    public int getIdServeur() {
        return idServeur;
    }

    public void setIdServeur(int idServeur) {
        this.idServeur = idServeur;
    }

    public int getPeriode() {
        return periode;
    }

    public void setPeriode(int periode) {
        this.periode = periode;
    }

    public String getUnite() {
        return unite;
    }

    public void setUnite(String unite) {
        this.unite = unite;
    }

    @Override
    public String toString() {
        return "Element{" +
                "id=" + id +
                ", idServeur=" + idServeur +
                ", sousOrganeId=" + sousOrganeId +
                ", code='" + code + '\'' +
                ", nom='" + nom + '\'' +
                ", criteriaAlpha=" + criteriaAlpha +
                ", valMin=" + valMin +
                ", valMax=" + valMax +
                ", guideSaisie='" + guideSaisie + '\'' +
                ", valeurNormale='" + valeurNormale + '\'' +
                ", valeurType='" + valeurType + '\'' +
                ", hasBorn=" + hasBorn +
                ", periode=" + periode +
                ", unite='" + unite + '\'' +
                '}';
    }
}
