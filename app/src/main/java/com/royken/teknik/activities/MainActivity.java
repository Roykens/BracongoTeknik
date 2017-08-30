package com.royken.teknik.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.table.TableUtils;
import com.royken.teknik.R;
import com.royken.teknik.database.DatabaseHelper;
import com.royken.teknik.entities.Bloc;
import com.royken.teknik.entities.Element;
import com.royken.teknik.entities.Organe;
import com.royken.teknik.entities.PostAnswer;
import com.royken.teknik.entities.Reponse;
import com.royken.teknik.entities.SousOrgane;
import com.royken.teknik.entities.Utilisateur;
import com.royken.teknik.entities.Zone;
import com.royken.teknik.network.RetrofitBuilder;
import com.royken.teknik.network.WebService;
import com.royken.teknik.network.util.AndroidNetworkUtility;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import java.sql.SQLException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity implements CahierFragment.OnFragmentInteractionListener,TimeChooseFragment.OnFragmentInteractionListener,OrganeFragment.OnFragmentInteractionListener,ElementListFragment.OnFragmentInteractionListener,ElementFragment.OnFragmentInteractionListener, ManageFragment.OnFragmentInteractionListener{

    private DatabaseHelper databaseHelper = null;
    public static final String PREFS_NAME = "com.royken.teknik.MyPrefsFile";
    SharedPreferences settings ;
    private Dao<Reponse, Integer> reponseDao;
    private List<Reponse> reponses;
    private boolean isExporting = false;
    long offset;
    long nombre ;
    private String url;

    private List<Utilisateur> utilisateurs;
    private List<Bloc> blocs;
    private List<Element> elements;
    private List<Organe> organes;
    private List<SousOrgane> sousorganes;
    private List<Zone> zones;
    ExportData data;
    Dao<Utilisateur, Integer> userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Fragment fragment = CahierFragment.newInstance();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.mainFrame,fragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.menu_update){
            try {
                userDao = getHelper().getUtilisateurDao();
                settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                int userId = settings.getInt("com.royken.userId", 0);
                Utilisateur u = userDao.queryForId(userId);
                if(!u.getRole().equalsIgnoreCase("admin")){
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                    dialogBuilder.setTitle("DROITS");
                    dialogBuilder.setMessage("Vous n'avez pas les droits");
                    dialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog b = dialogBuilder.create();
                    b.show();
                }
                else{
                    AndroidNetworkUtility androidNetworkUtility = new AndroidNetworkUtility();
                    SharedPreferences settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                    url = settings.getString("com.royken.url", "");
                    if(url.isEmpty()){
                        Toast.makeText(getApplicationContext(), "Serveur manquant", Toast.LENGTH_LONG).show();
                    }
                    if (!androidNetworkUtility.isConnected(getApplicationContext())) {
                        Toast.makeText(getApplicationContext(), "Aucune connexion au serveur. Veuillez reéssayer plus tard", Toast.LENGTH_LONG).show();
                    } else {
                        new UtilisateurTask().execute();
                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        if(id == R.id.menu_settings){
            try {
                userDao = getHelper().getUtilisateurDao();
                settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                int userId = settings.getInt("com.royken.userId", 0);
                Utilisateur u = userDao.queryForId(userId);
                if(!u.getRole().equalsIgnoreCase("admin")){
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                    dialogBuilder.setTitle("DROITS");
                    dialogBuilder.setMessage("Vous n'avez pas les droits");
                    dialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog b = dialogBuilder.create();
                    b.show();
                }
                else{
                    Fragment fragment = ManageFragment.newInstance();
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.mainFrame,fragment);
                    ft.addToBackStack(null);
                    ft.commit();
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }


        }
        if (id == R.id.menu_exporter) {

            settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            final boolean file = settings.getBoolean("com.royken.file", false);
            try {
                userDao = getHelper().getUtilisateurDao();

                int userId = settings.getInt("com.royken.userId", 0);
                Utilisateur u = userDao.queryForId(userId);
                if(!u.getRole().equalsIgnoreCase("admin")){
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                    dialogBuilder.setTitle("DROITS");
                    dialogBuilder.setMessage("Vous n'avez pas les droits");
                    dialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog b = dialogBuilder.create();
                    b.show();
                }
                else {
                    String output = file ? "Fichier":" Réseau";
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                    dialogBuilder.setTitle(" !!!! EXPORTATION : " + output);
                    dialogBuilder.setMessage("Veuillez confirmer l'exportation. Celà supprimera vos données");
                    dialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            if (isExporting == false) {
                                isExporting = true;
                                try {

                                    boolean index0 = settings.getBoolean("com.royken.index", false);
                                    offset = settings.getLong("com.royken.offset", 0);
                                    reponseDao = getHelper().getReponseDao();
                                    nombre = reponseDao.countOf();
                                    if (index0 || offset == 0) {
                                        reponses = reponseDao.queryForAll();

                                    } else {
                                        reponses = reponseDao.queryBuilder().offset(offset).limit(nombre - offset).query();
                                    }
                                    if (file) {
                                        data = new ExportData(getBaseContext(), reponses);
                                        data.exportReponseForImport();
                                        Toast.makeText(getApplicationContext(), "Données exportées avec succès", Toast.LENGTH_LONG).show();
                                       // new ExportTask().execute();
                                        isExporting = false;
                                        SharedPreferences.Editor editor = settings.edit();
                                        editor.putLong("com.royken.offset", nombre);
                                        editor.commit();
                                        editor.putBoolean("com.royken.file", false);
                                        editor.commit();
                                        editor.putBoolean("com.royken.index", false);
                                        editor.commit();

                                    } else {
                                        AndroidNetworkUtility androidNetworkUtility = new AndroidNetworkUtility();
                                        SharedPreferences settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                                        url = settings.getString("com.royken.url", "");
                                        if(url.isEmpty()){
                                            Toast.makeText(getApplicationContext(), "Serveur manquant", Toast.LENGTH_LONG).show();
                                            Fragment fragment = ManageFragment.newInstance();
                                            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                                            ft.replace(R.id.mainFrame,fragment);
                                            ft.addToBackStack(null);
                                            ft.commit();
                                        }
                                        if (!androidNetworkUtility.isConnected(getApplicationContext())) {
                                            Toast.makeText(getApplicationContext(), "Aucune connexion au serveur. Veuillez reéssayer plus tard", Toast.LENGTH_LONG).show();
                                        } else {
                                            new BackgroundTask().execute();
                                            isExporting = false;
                                        }

                                    }


                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }


                            }
                        }
                    });
                    dialogBuilder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog b = dialogBuilder.create();
                    b.show();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return true;
        }

        if(id == R.id.menu_deconnexion){
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            dialogBuilder.setTitle("DECONNEXION");
            dialogBuilder.setMessage("Veuillez confirmer la déconnexion");
            dialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("com.royken.login", "");
                    editor.putInt("com.royken.userId", 0);
                    editor.putBoolean("com.royken.haslogged", false);
                    editor.commit();
                    Intent intent = new Intent(getApplicationContext(), Login.class);
                    startActivity(intent);
                    MainActivity.this.finish();
                }
            });
            dialogBuilder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    dialog.cancel();
                }
            });
            AlertDialog b = dialogBuilder.create();
            b.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            //databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
            databaseHelper = new DatabaseHelper(this);
        }
        return databaseHelper;
    }


    private class BackgroundTask extends AsyncTask<String, Void,
            Void> {
        private ProgressDialog Dialog = new ProgressDialog(MainActivity.this);


        @Override
        protected void onPreExecute() {
            Dialog.setMessage("Envoi en cours ...");
            Dialog.show();

        }

        @Override
        protected Void doInBackground(String... urls) {

            Log.i("Bacground", "background test");
            final SharedPreferences settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            String url = settings.getString("com.royken.url", "");
            Retrofit retrofit = RetrofitBuilder.getRetrofit(url + "/");
            WebService service = retrofit.create(WebService.class);
            for (Reponse r : reponses){
                Log.i("Reponse",r.toString());
            }

            if (!reponses.isEmpty()) {
                Call<PostAnswer> call = service.envoyerReponse(reponses);
                call.enqueue(new Callback<PostAnswer>() {
                    @Override
                    public void onResponse(Call<PostAnswer> call, Response<PostAnswer> response) {
                        PostAnswer ans = response.body();
                        if(ans != null && ans.isSuccess()){
                            Toast.makeText(getApplicationContext(), "Donnée envoyée avec succès", Toast.LENGTH_LONG).show();
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putLong("com.royken.offset", nombre);
                            editor.commit();
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Echec d'envoi !!!", Toast.LENGTH_LONG).show();
                        }

                    }
                    @Override
                    public void onFailure(Call<PostAnswer> call, Throwable t) {
                        //Log.e("Retrofit Logging2", t.toString());
                        Toast.makeText(getApplicationContext(), "Echec d'envoi", Toast.LENGTH_LONG).show();
                        //  storeInDatabase(reponseProjection);
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            Dialog.dismiss();
            //Toast.makeText(getApplicationContext(), "Envoi terminé", Toast.LENGTH_LONG).show();
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("com.royken.index", false);
            editor.commit();
            editor.putLong("com.royken.offset", nombre);
            editor.commit();
        }
    }

    private class UtilisateurTask extends AsyncTask<String, Void, Void> {
        // Required initialization


        private ProgressDialog Dialog = new ProgressDialog(MainActivity.this);
        private boolean data;


        protected void onPreExecute() {
            Dialog.setMessage("Récupération des utilisateurs..");
            Dialog.show();
        }

        // Call after onPreExecute method
        protected Void doInBackground(String... urls) {
            Retrofit retrofit = RetrofitBuilder.getRetrofit(url + "/");
            WebService service = retrofit.create(WebService.class);
            Call<List<Utilisateur>> call = service.getAllUtilisateurs();
            call.enqueue(new Callback<List<Utilisateur>>() {
                @Override
                public void onResponse(Call<List<Utilisateur>> call, Response<List<Utilisateur>> response) {
                    //  Log.i("Result....", response.toString());
                    final Dao<Utilisateur, Integer> userDao;

                    utilisateurs = response.body();

                    try {
                        if(!utilisateurs.isEmpty()){
                            userDao = getHelper().getUtilisateurDao();
                            //List<Utilisateur> users = userDao.queryForAll();
                            //userDao.delete(users);
                            TableUtils.dropTable(userDao.getConnectionSource(), Utilisateur.class, true);
                            TableUtils.createTable(userDao.getConnectionSource(), Utilisateur.class);
                            //TableUtils.clearTable(userDao.getConnectionSource(), Utilisateur.class);
                           // userDao.deleteBuilder().delete();
                            //This is the way to insert data into a database table
                            userDao.create(utilisateurs);
                            data = true;
                        }
                        else{
                            data = false;
                            Toast.makeText(getApplicationContext(),"Aucun utilisateur trouvé, Vérifiez la connexion !!!",Toast.LENGTH_LONG).show();
                        }


                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onFailure(Call<List<Utilisateur>> call, Throwable t) {
                    Log.i("Error...", t.toString());
                }
            });
            return null;
        }

        protected void onPostExecute(Void unused) {
            Dialog.dismiss();
          //  if(data){
                new ZonesTask().execute();
           // }
        }

    }

    private class ZonesTask  extends AsyncTask<String, Void, Void> {
        // Required initialization

        private ProgressDialog Dialog = new ProgressDialog(MainActivity.this);

        protected void onPreExecute() {
            Dialog.setMessage("Récupération des zones...");
            Dialog.show();
        }

        // Call after onPreExecute method
        protected Void doInBackground(String... urls) {
            Retrofit retrofit = RetrofitBuilder.getRetrofit(url + "/");
            WebService service = retrofit.create(WebService.class);
            Call<List<Zone>> call = service.getAllZones();
            call.enqueue(new Callback<List<Zone>>() {
                @Override
                public void onResponse(Call<List<Zone>> call, Response<List<Zone>> response) {
                    //  Log.i("Result....", response.toString());
                    zones = response.body();
                    final Dao<Zone, Integer> zoneDao;
                    try {
                        zoneDao = getHelper().getZoneDao();
                        if(!zones.isEmpty()){
                           // List<Zone> zoneList = zoneDao.queryForAll();
                            //zoneDao.delete(zoneList);
                            TableUtils.dropTable(zoneDao.getConnectionSource(), Zone.class, true);
                            TableUtils.createTable(zoneDao.getConnectionSource(), Zone.class);
                            //TableUtils.clearTable(zoneDao.getConnectionSource(), Zone.class);
                            zoneDao.create(zones);
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"Aucune zone récupérée",Toast.LENGTH_LONG).show();
                        }

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onFailure(Call<List<Zone>> call, Throwable t) {
                    Log.i("Error...", t.toString());
                }
            });
            return null;
        }

        protected void onPostExecute(Void unused) {
            Dialog.dismiss();
            new BlocTask().execute();
        }
    }

    private class BlocTask  extends AsyncTask<String, Void, Void> {
        // Required initialization

        private ProgressDialog Dialog = new ProgressDialog(MainActivity.this);


        protected void onPreExecute() {
            Dialog.setMessage("Récupération des blocs...");
            Dialog.show();
        }

        // Call after onPreExecute method
        protected Void doInBackground(String... urls) {
            Retrofit retrofit = RetrofitBuilder.getRetrofit(url+"/");
            WebService service = retrofit.create(WebService.class);
            Call<List<Bloc>> call = service.getAllBlocs();
            call.enqueue(new Callback<List< Bloc>>() {
                @Override
                public void onResponse(Call<List<Bloc>> call, Response<List<Bloc>> response) {
                    //  Log.i("Result....", response.toString());
                    blocs = response.body();
                    final Dao<Bloc, Integer> blocDao;
                    try {
                        blocDao = getHelper().getBlocDao();
                        if(!blocs.isEmpty()){
                            //List<Bloc> blocList = blocDao.queryForAll();
                            //blocDao.delete(blocList);
                            TableUtils.dropTable(blocDao.getConnectionSource(), Bloc.class, true);
                            TableUtils.createTable(blocDao.getConnectionSource(), Bloc.class);
                            //TableUtils.clearTable(blocDao.getConnectionSource(), Bloc.class);
                            blocDao.create(blocs);
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Aucun bloc récupéré",Toast.LENGTH_LONG).show();
                        }

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onFailure(Call<List<Bloc>> call, Throwable t) {
                    Log.i("Error...", t.toString());
                }
            });
            return null;
        }

        protected void onPostExecute(Void unused) {
            Dialog.dismiss();
            new ElementTask().execute();
        }

    }

    private class ElementTask  extends AsyncTask<String, Void, Void> {
        // Required initialization

        private ProgressDialog Dialog = new ProgressDialog(MainActivity.this);


        protected void onPreExecute() {
            Dialog.setMessage("Récupération des Éléments...");
            Dialog.show();
        }

        // Call after onPreExecute method
        protected Void doInBackground(String... urls) {
            Retrofit retrofit = RetrofitBuilder.getRetrofit(url+"/");
            WebService service = retrofit.create(WebService.class);
            Call<List<Element>> call = service.getAllElements();
            call.enqueue(new Callback<List< Element>>() {
                @Override
                public void onResponse(Call<List<Element>> call, Response<List<Element>> response) {
                    //  Log.i("Result....", response.toString());
                    elements = response.body();
                    final Dao<Element, Integer> elementDao;
                    try {
                        elementDao = getHelper().getElementDao();
                        if(!elements.isEmpty()){
                          //  List<Element> elementList = elementDao.queryForAll();
                          //  elementDao.delete(elementList);
                            TableUtils.dropTable(elementDao.getConnectionSource(), Element.class, true);
                            TableUtils.createTable(elementDao.getConnectionSource(), Element.class);
                            //TableUtils.clearTable(elementDao.getConnectionSource(), Element.class);
                            elementDao.create(elements);
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Aucun élémént récupéré",Toast.LENGTH_LONG).show();
                        }

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onFailure(Call<List<Element>> call, Throwable t) {
                    Log.i("Error...", t.toString());
                }
            });
            return null;
        }

        protected void onPostExecute(Void unused) {
            Dialog.dismiss();
            new OrganeTask().execute();
        }

    }

    private class OrganeTask  extends AsyncTask<String, Void, Void> {
        // Required initialization

        private ProgressDialog Dialog = new ProgressDialog(MainActivity.this);

        protected void onPreExecute() {
            Dialog.setMessage("Récupération des Organes...");
            Dialog.show();
        }

        // Call after onPreExecute method
        protected Void doInBackground(String... urls) {
            Retrofit retrofit = RetrofitBuilder.getRetrofit(url+"/");
            WebService service = retrofit.create(WebService.class);
            Call<List<Organe>> call = service.getAllOrganes();
            call.enqueue(new Callback<List<Organe>>() {
                @Override
                public void onResponse(Call<List<Organe>> call, Response<List<Organe>> response) {
                    //  Log.i("Result....", response.toString());
                    organes = response.body();
                    final Dao<Organe, Integer> organeDao;
                    try {
                        organeDao = getHelper().getOrganeDao();
                        if(!organes.isEmpty()){
                            //List<Organe> organeList = organeDao.queryForAll();
                            //organeDao.delete(organeList);
                            TableUtils.dropTable(organeDao.getConnectionSource(), Organe.class, true);
                            TableUtils.createTable(organeDao.getConnectionSource(), Organe.class);
                            //TableUtils.clearTable(organeDao.getConnectionSource(), Organe.class);
                            organeDao.create(organes);
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Aucun organe récupéré",Toast.LENGTH_LONG).show();
                        }

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onFailure(Call<List<Organe>> call, Throwable t) {
                    Log.i("Error...", t.toString());
                }
            });
            return null;
        }

        protected void onPostExecute(Void unused) {
            Dialog.dismiss();
            new SousOrganeTask().execute();
        }

    }

    private class SousOrganeTask  extends AsyncTask<String, Void, Void> {
        // Required initialization

        private ProgressDialog Dialog = new ProgressDialog(MainActivity.this);

        protected void onPreExecute() {
            Dialog.setMessage("Récupération des Sous organes...");
            Dialog.show();
        }

        // Call after onPreExecute method
        protected Void doInBackground(String... urls) {
            Retrofit retrofit = RetrofitBuilder.getRetrofit(url + "/");
            WebService service = retrofit.create(WebService.class);
            Call<List<SousOrgane>> call = service.getAllSousOrganes();
            call.enqueue(new Callback<List<SousOrgane>>() {
                @Override
                public void onResponse(Call<List<SousOrgane>> call, Response<List<SousOrgane>> response) {
                    //  Log.i("Result....", response.toString());
                    sousorganes = response.body();
                    final Dao<SousOrgane, Integer> sousorganeDao;
                    try {
                        sousorganeDao = getHelper().getSousOrganeDao();
                        if(!sousorganes.isEmpty()){
                            //List<SousOrgane> sousOrganeList = sousorganeDao.queryForAll();
                            //sousorganeDao.delete(sousOrganeList);
                            TableUtils.dropTable(sousorganeDao.getConnectionSource(), SousOrgane.class, true);
                            TableUtils.createTable(sousorganeDao.getConnectionSource(), SousOrgane.class);
                            //TableUtils.clearTable(sousorganeDao.getConnectionSource(), SousOrgane.class);
                            sousorganeDao.create(sousorganes);
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Aucun sous organe récupéré",Toast.LENGTH_LONG).show();
                        }

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onFailure(Call<List<SousOrgane>> call, Throwable t) {
                    Log.i("Error...", t.toString());
                }
            });
            return null;
        }

        protected void onPostExecute(Void unused) {
            Dialog.dismiss();
            settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("com.royken.data", true);
            editor.commit();
            Toast.makeText(getApplicationContext(),"Fin récupération", Toast.LENGTH_LONG).show();
            editor.putString("com.royken.login", "");
            editor.putInt("com.royken.userId", 0);
            editor.putBoolean("com.royken.haslogged", false);
            editor.commit();
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            MainActivity.this.finish();


        }
    }

    private class ExportTask  extends AsyncTask<String, Void, Void> {
        // Required initialization

        private ProgressDialog Dialog = new ProgressDialog(MainActivity.this);

        protected void onPreExecute() {
            Dialog.setMessage("Exportation en cours...");
            Dialog.show();
        }

        protected Void doInBackground(String... urls) {
            data.exportReponseForImport();
            return null;
        }

        protected void onPostExecute(Void unused) {
            Dialog.dismiss();
            Toast.makeText(getApplicationContext(), "Données exportées avec succès", Toast.LENGTH_LONG).show();
        }

    }
}
