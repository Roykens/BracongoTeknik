package com.royken.teknik.entities;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by royken on 03/07/17.
 */
public class Donnees implements Serializable{

    private static final long serialVersionUID = -22286131214757024L;

    @SerializedName("reponses")
    private List<Reponse> reponses;

    public List<Reponse> getReponses() {
        return reponses;
    }

    public void setReponses(List<Reponse> reponses) {
        this.reponses = reponses;
    }
}
