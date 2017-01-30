package com.royken.teknik.network;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.royken.teknik.entities.Bloc;
import com.royken.teknik.entities.Element;
import com.royken.teknik.entities.Organe;
import com.royken.teknik.entities.SousOrgane;
import com.royken.teknik.entities.Utilisateur;
import com.royken.teknik.entities.Zone;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

/**
 * Created by royken on 30/12/16.
 */
public interface WebService {

    @GET("/teknik/api/teknik/blocs")
    Call<List<Bloc>> getAllBlocs();

    @GET("/teknik/api/teknik/zones")
    Call<List<Zone>> getAllZones();

    @GET("/teknik/api/teknik/organes")
    Call<List<Organe>> getAllOrganes();

    @GET("/teknik/api/teknik/sousOrganes")
    Call<List<SousOrgane>> getAllSousOrganes();

    @GET("/teknik/api/teknik/elements")
    Call<List<Element>> getAllElements();

    @GET("/teknik/api/teknik/users")
    Call<List<Utilisateur>> getAllUtilisateurs();

    public static Gson gson = new GsonBuilder()
            .disableHtmlEscaping()
            .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
            .setDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'")
            .setPrettyPrinting()
            .serializeNulls()
            .excludeFieldsWithoutExposeAnnotation().create();

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://192.168.1.106:8080/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();
}
