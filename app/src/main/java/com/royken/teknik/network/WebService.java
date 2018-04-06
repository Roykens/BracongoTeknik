package com.royken.teknik.network;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.royken.teknik.entities.Bloc;
import com.royken.teknik.entities.Cahier;
import com.royken.teknik.entities.Donnees;
import com.royken.teknik.entities.Element;
import com.royken.teknik.entities.Organe;
import com.royken.teknik.entities.Periode;
import com.royken.teknik.entities.PostAnswer;
import com.royken.teknik.entities.Reponse;
import com.royken.teknik.entities.ServerStatus;
import com.royken.teknik.entities.SousOrgane;
import com.royken.teknik.entities.Utilisateur;
import com.royken.teknik.entities.Zone;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by royken on 30/12/16.
 */
public interface WebService {

    @GET("/BracongoTeknik/api/teknik/blocs")
    Call<List<Bloc>> getAllBlocs();

    @GET("/BracongoTeknik/api/teknik/zones")
    Call<List<Zone>> getAllZones();

    @GET("/BracongoTeknik/api/teknik/organes")
    Call<List<Organe>> getAllOrganes();

    @GET("/BracongoTeknik/api/teknik/sousOrganes")
    Call<List<SousOrgane>> getAllSousOrganes();

    @GET("/BracongoTeknik/api/teknik/elements")
    Call<List<Element>> getAllElements();

    @GET("/BracongoTeknik/api/teknik/cahiers")
    Call<List<Cahier>> getAllCahiers();

    @GET("/BracongoTeknik/api/teknik/users")
    Call<List<Utilisateur>> getAllUtilisateurs();

    @GET("/BracongoTeknik/api/teknik/periodes")
    Call<List<Periode>> getAllPeriodes();

    @GET("/BracongoTeknik/api/teknik/status")
    Call<ServerStatus> getServerStatus();

    @POST("/BracongoTeknik/api/teknik/reponses")
    Call<Donnees> envoyerReponse(@Body Donnees donnees);

    @POST("/BracongoTeknik/api/teknik/reponse")
    Call<PostAnswer> envoyerReponse(@Body List<Reponse> reponse);



    public static Gson gson = new GsonBuilder()
            .disableHtmlEscaping()
            .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
            .setDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'")
            .setPrettyPrinting()
            .serializeNulls()
            .excludeFieldsWithoutExposeAnnotation().create();

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://192.168.1.100:8080/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();
}
