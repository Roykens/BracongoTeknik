package com.royken.teknik.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.royken.teknik.R;
import com.royken.teknik.database.DatabaseHelper;
import com.royken.teknik.entities.Bloc;
import com.royken.teknik.entities.Cahier;
import com.royken.teknik.entities.Element;
import com.royken.teknik.entities.Organe;
import com.royken.teknik.entities.Periode;
import com.royken.teknik.entities.ServerStatus;
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

public class GetDataActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper = null;
    private Button dataBtn;
    private Button test;
    private List<Utilisateur> utilisateurs;
    private List<Bloc> blocs;
    private List<Element> elements;
    private List<Organe> organes;
    private List<SousOrgane> sousorganes;
    private List<Zone> zones;
    private List<Cahier> cahiers;
    private List<Periode> periodes;
    TextView _urlLink;
    private String url;
    private ServerStatus status;
    private boolean serverAvailability ;

    public static final String PREFS_NAME = "com.royken.teknik.MyPrefsFile";

    SharedPreferences settings ;
    SharedPreferences.Editor editor;
    private boolean loadData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_data);
        dataBtn = (Button)findViewById(R.id.button);
        _urlLink = (TextView) findViewById(R.id.link_url);
        settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        loadData = settings.getBoolean("com.royken.data",false);
        if(loadData == true){
            Intent intent = new Intent(GetDataActivity.this,
                    Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            GetDataActivity.this.finish();
        }
        else {
            dataBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AndroidNetworkUtility androidNetworkUtility = new AndroidNetworkUtility();
                    SharedPreferences settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                    url = settings.getString("com.royken.url", "");
                    if(url.isEmpty()){
                        Toast.makeText(getApplicationContext(), "Serveur manquant", Toast.LENGTH_LONG).show();
                    }
                    if (!androidNetworkUtility.isConnected(getApplicationContext())) {
                        Toast.makeText(getApplicationContext(), "Aucune connexion au serveur. Veuillez reéssayer plus tard", Toast.LENGTH_LONG).show();
                    } else {
                        new ServerTask().execute();
                    }


                }
            });
            _urlLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Finish the registration screen and return to the Login activity
                    // finish();
                    showChangeLangDialog();
                }
            });

           /* test.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dao<Utilisateur, Integer> userDao;
                    try {
                        userDao = getHelper().getUtilisateurDao();
                        List<Utilisateur> users = userDao.queryForAll();
                        Toast.makeText(getApplicationContext(), "J'ai " + users.size() + " users ", Toast.LENGTH_LONG).show();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                }
            });
            */
        }
    }

    private class ServerTask extends AsyncTask<String, Void, Void> {
        // Required initialization

         private ProgressDialog Dialog = new ProgressDialog(GetDataActivity.this);
        private boolean data;


        protected void onPreExecute() {
            Dialog.setMessage("Test disponibilité du serveur...");
            Dialog.show();
        }

        // Call after onPreExecute method
        protected Void doInBackground(String... urls) {
            Retrofit retrofit = RetrofitBuilder.getRetrofit(url+"/");
            WebService service = retrofit.create(WebService.class);
            Call<ServerStatus> call = service.getServerStatus();
            call.enqueue(new Callback<ServerStatus>() {
                @Override
                public void onResponse(Call<ServerStatus> call, Response<ServerStatus> response) {
                    //  Log.i("Result....", response.toString());
                    status = response.body();
                    if(status == null){
                        serverAvailability = false;
                        Toast.makeText(getApplicationContext(),"Serveur injoignable !!!",Toast.LENGTH_LONG).show();
                    }
                    else {
                        if(status.getServer() == 200 && status.isStatus()){
                            new CahierTask().execute();
                            serverAvailability = true;
                        }
                    }
                }
                @Override
                public void onFailure(Call<ServerStatus> call, Throwable t) {
                    serverAvailability = false;
                    Toast.makeText(getApplicationContext(),"Serveur injoignable !!!",Toast.LENGTH_LONG).show();
                    Log.i("Error...", t.toString());
                }
            });
            return null;
        }

        protected void onPostExecute(Void unused) {
            Dialog.dismiss();
            Log.i("DATAAAAAAAAAAA",data+"");

        }

    }

    private class CahierTask extends AsyncTask<String, Void, Void> {
        // Required initialization

        private final HttpClient Client = new DefaultHttpClient();
        private ProgressDialog Dialog = new ProgressDialog(GetDataActivity.this);
        private boolean data;


        protected void onPreExecute() {
            Dialog.setMessage("Récupération des cahiers...");
            Dialog.show();
        }

        // Call after onPreExecute method
        protected Void doInBackground(String... urls) {
            Retrofit retrofit = RetrofitBuilder.getRetrofit(url+"/");
            WebService service = retrofit.create(WebService.class);
            Call<List<Cahier>> call = service.getAllCahiers();
            call.enqueue(new Callback<List<Cahier>>() {
                @Override
                public void onResponse(Call<List<Cahier>> call, Response<List<Cahier>> response) {
                    //  Log.i("Result....", response.toString());
                    cahiers = response.body();
                    final Dao<Cahier, Integer> cahierDao;
                    try {
                        cahierDao = getHelper().getCahierDao();
                        if(!cahiers.isEmpty()){
                            data  = true;
                            cahierDao.create(cahiers);
                        }
                        else {
                            data = false;
                            Toast.makeText(getApplicationContext(),"Aucun cahier trouvé, Vérifiez la connexion !!!",Toast.LENGTH_LONG).show();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onFailure(Call<List<Cahier>> call, Throwable t) {
                    Log.i("Error...", t.toString());
                }
            });
            return null;
        }

        protected void onPostExecute(Void unused) {
            Dialog.dismiss();
            Log.i("DATAAAAAAAAAAA",data+"");
            // if(data){
            new UtilisateurTask().execute();
            // }

        }

    }

    private class UtilisateurTask extends AsyncTask<String, Void, Void> {
        // Required initialization

        private final HttpClient Client = new DefaultHttpClient();
        private String Content;
        private String Error = null;
        private ProgressDialog Dialog = new ProgressDialog(GetDataActivity.this);
        private boolean data;


        protected void onPreExecute() {
            Dialog.setMessage("Récupération des utilisateurs..");
            Dialog.show();
        }

        // Call after onPreExecute method
        protected Void doInBackground(String... urls) {
            Retrofit retrofit = RetrofitBuilder.getRetrofit(url+"/");
            WebService service = retrofit.create(WebService.class);
            Call<List<Utilisateur>> call = service.getAllUtilisateurs();
            call.enqueue(new Callback<List<Utilisateur>>() {
                @Override
                public void onResponse(Call<List<Utilisateur>> call, Response<List<Utilisateur>> response) {
                    //  Log.i("Result....", response.toString());
                    utilisateurs = response.body();
                    final Dao<Utilisateur, Integer> userDao;
                    try {
                        userDao = getHelper().getUtilisateurDao();
                        if(!utilisateurs.isEmpty()){
                            data  = true;
                            userDao.create(utilisateurs);
                        }
                        else {
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
            Log.i("DATAAAAAAAAAAA",data+"");
           // if(data){
                new ZonesTask().execute();
           // }

        }

    }

    private class ZonesTask  extends AsyncTask<String, Void, Void> {
        // Required initialization

        private ProgressDialog Dialog = new ProgressDialog(GetDataActivity.this);

        protected void onPreExecute() {
            Dialog.setMessage("Récupération des zones...");
            Dialog.show();
        }

        // Call after onPreExecute method
        protected Void doInBackground(String... urls) {
            Retrofit retrofit = RetrofitBuilder.getRetrofit(url+"/");
            WebService service = retrofit.create(WebService.class);
            Call<List<Zone>> call = service.getAllZones();
            //List<Boisson> result = call.execute().body();
            //final List<Boisson> result;
            call.enqueue(new Callback<List<Zone>>() {
                @Override
                public void onResponse(Call<List<Zone>> call, Response<List<Zone>> response) {
                    //  Log.i("Result....", response.toString());
                    zones = response.body();
                    final Dao<Zone, Integer> zoneDao;
                    try {
                        zoneDao = getHelper().getZoneDao();
                        if(!zones.isEmpty()){
                            zoneDao.create(zones);
                        }
                        else {
                            Toast.makeText(getBaseContext(),"Aucune zone",Toast.LENGTH_LONG).show();
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

        private ProgressDialog Dialog = new ProgressDialog(GetDataActivity.this);


        protected void onPreExecute() {
            Dialog.setMessage("Récupération des blocs...");
            Dialog.show();
        }

        // Call after onPreExecute method
        protected Void doInBackground(String... urls) {
            Retrofit retrofit = RetrofitBuilder.getRetrofit(url+"/");
            WebService service = retrofit.create(WebService.class);
            Call<List<Bloc>> call = service.getAllBlocs();
            //List<Boisson> result = call.execute().body();
            //final List<Boisson> result;
            call.enqueue(new Callback<List< Bloc>>() {
                @Override
                public void onResponse(Call<List<Bloc>> call, Response<List<Bloc>> response) {
                    //  Log.i("Result....", response.toString());
                    blocs = response.body();
                    final Dao<Bloc, Integer> blocDao;
                    try {
                        blocDao = getHelper().getBlocDao();
                        if(!blocs.isEmpty()){
                            blocDao.create(blocs);
                        }
                        else {
                            Toast.makeText(getBaseContext(),"Aucun bloc",Toast.LENGTH_LONG).show();
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

        private ProgressDialog Dialog = new ProgressDialog(GetDataActivity.this);


        protected void onPreExecute() {
            Dialog.setMessage("Récupération des Éléments...");
            Dialog.show();
        }

        // Call after onPreExecute method
        protected Void doInBackground(String... urls) {
            Retrofit retrofit = RetrofitBuilder.getRetrofit(url+"/");
            WebService service = retrofit.create(WebService.class);
            Call<List<Element>> call = service.getAllElements();
            //List<Boisson> result = call.execute().body();
            //final List<Boisson> result;
            call.enqueue(new Callback<List< Element>>() {
                @Override
                public void onResponse(Call<List<Element>> call, Response<List<Element>> response) {
                    //  Log.i("Result....", response.toString());
                   elements = response.body();
                    final Dao<Element, Integer> elementDao;
                    try {
                        elementDao = getHelper().getElementDao();
                        if(!elements.isEmpty()){
                            elementDao.create(elements);
                        }
                        else{
                            Toast.makeText(getBaseContext(),"Aucun élément",Toast.LENGTH_LONG).show();
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

        private ProgressDialog Dialog = new ProgressDialog(GetDataActivity.this);

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
                            organeDao.create(organes);
                        }
                        else {
                            Toast.makeText(getBaseContext(),"Aucun organe",Toast.LENGTH_LONG).show();
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
            new PeriodeTask().execute();
        }

    }

    private class PeriodeTask  extends AsyncTask<String, Void, Void> {
        // Required initialization

        private ProgressDialog Dialog = new ProgressDialog(GetDataActivity.this);

        protected void onPreExecute() {
            Dialog.setMessage("Récupération des Periodes...");
            Dialog.show();
        }

        // Call after onPreExecute method
        protected Void doInBackground(String... urls) {
            Retrofit retrofit = RetrofitBuilder.getRetrofit(url+"/");
            WebService service = retrofit.create(WebService.class);
            Call<List<Periode>> call = service.getAllPeriodes();
            call.enqueue(new Callback<List<Periode>>() {
                @Override
                public void onResponse(Call<List<Periode>> call, Response<List<Periode>> response) {
                    periodes = response.body();
                    final Dao<Periode, Integer> periodeDao;
                    try {
                        periodeDao = getHelper().getPeriodeDao();
                        if(!periodes.isEmpty()){
                            periodeDao.create(periodes);
                        }
                        else {
                            Toast.makeText(getBaseContext(),"Aucune périodes",Toast.LENGTH_LONG).show();
                        }

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onFailure(Call<List<Periode>> call, Throwable t) {
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

        private ProgressDialog Dialog = new ProgressDialog(GetDataActivity.this);

        protected void onPreExecute() {
            Dialog.setMessage("Récupération des Sous organes...");
            Dialog.show();
        }

        // Call after onPreExecute method
        protected Void doInBackground(String... urls) {
            Retrofit retrofit = RetrofitBuilder.getRetrofit(url+"/");
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
                            sousorganeDao.create(sousorganes);
                        }
                        else {
                            Toast.makeText(getBaseContext(),"Aucun sous organe",Toast.LENGTH_LONG).show();
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
            //Toast.makeText(getApplicationContext(),"J'ai Fini", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(GetDataActivity.this,
                    Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            GetDataActivity.this.finish();
        }
    }

    private DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            //databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
            databaseHelper = new DatabaseHelper(this);
        }
        return databaseHelper;
    }

    private void showChangeLangDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.url_dialog, null);
        dialogBuilder.setView(dialogView);
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        url = settings.getString("com.royken.url", "");

        final EditText edt = (EditText) dialogView.findViewById(R.id.input_url);
        if(url.length() > 0){
            edt.setText(url);
        }
        editor = settings.edit();
        dialogBuilder.setTitle("Modification de l'URL");
        dialogBuilder.setMessage("Entrer l'URL du serveur");
        dialogBuilder.setPositiveButton("Valider", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String txt = edt.getText().toString().trim();
                if (txt.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "URL Manquante", Toast.LENGTH_LONG).show();
                } else {
                    editor.putString("com.royken.url", txt);
                    editor.commit();
                    Toast.makeText(getApplicationContext(), txt, Toast.LENGTH_LONG).show();
                }

            }
        });
        dialogBuilder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
                dialog.cancel();
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }
}
