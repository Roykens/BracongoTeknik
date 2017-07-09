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
import com.royken.teknik.entities.Element;
import com.royken.teknik.entities.Organe;
import com.royken.teknik.entities.SousOrgane;
import com.royken.teknik.entities.Utilisateur;
import com.royken.teknik.entities.Zone;
import com.royken.teknik.network.RetrofitBuilder;
import com.royken.teknik.network.WebService;

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
    TextView _urlLink;
    private String url;

    public static final String PREFS_NAME = "com.royken.teknik.MyPrefsFile";

    SharedPreferences settings ;
    SharedPreferences.Editor editor;
    private boolean loadData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_data);
        dataBtn = (Button)findViewById(R.id.button);
       // test = (Button)findViewById(R.id.test);
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
                    new UtilisateurTask().execute();
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

    private class UtilisateurTask extends AsyncTask<String, Void, Void> {
        // Required initialization

        private final HttpClient Client = new DefaultHttpClient();
        private String Content;
        private String Error = null;
        private ProgressDialog Dialog = new ProgressDialog(GetDataActivity.this);
        String data ="";


        protected void onPreExecute() {
            Dialog.setMessage("Récupération des blocs..");
            Dialog.show();
        }

        // Call after onPreExecute method
        protected Void doInBackground(String... urls) {
            Retrofit retrofit = RetrofitBuilder.getRetrofit("http://192.168.1.107:8080" + "/");
            WebService service = retrofit.create(WebService.class);
            Call<List<Utilisateur>> call = service.getAllUtilisateurs();
            //List<Boisson> result = call.execute().body();
            //final List<Boisson> result;
            call.enqueue(new Callback<List<Utilisateur>>() {
                @Override
                public void onResponse(Call<List<Utilisateur>> call, Response<List<Utilisateur>> response) {
                    //  Log.i("Result....", response.toString());
                    utilisateurs = response.body();
                    final Dao<Utilisateur, Integer> userDao;
                    try {
                        userDao = getHelper().getUtilisateurDao();
                        //This is the way to insert data into a database table
                           userDao.create(utilisateurs);
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
            new ZonesTask().execute();
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
            Retrofit retrofit = RetrofitBuilder.getRetrofit("http://192.168.1.107:8080" + "/");
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
                        //This is the way to insert data into a database table
                        zoneDao.create(zones);
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
            Retrofit retrofit = RetrofitBuilder.getRetrofit("http://192.168.1.107:8080" + "/");
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
                        //This is the way to insert data into a database table
                        blocDao.create(blocs);
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
            Retrofit retrofit = RetrofitBuilder.getRetrofit("http://192.168.1.107:8080" + "/");
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
                        //This is the way to insert data into a database table
                        elementDao.create(elements);
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
            Retrofit retrofit = RetrofitBuilder.getRetrofit("http://192.168.1.107:8080" + "/");
            WebService service = retrofit.create(WebService.class);
            Call<List<Organe>> call = service.getAllOrganes();
            //List<Boisson> result = call.execute().body();
            //final List<Boisson> result;
            call.enqueue(new Callback<List<Organe>>() {
                @Override
                public void onResponse(Call<List<Organe>> call, Response<List<Organe>> response) {
                    //  Log.i("Result....", response.toString());
                    organes = response.body();
                    final Dao<Organe, Integer> organeDao;
                    try {
                        organeDao = getHelper().getOrganeDao();
                        //This is the way to insert data into a database table
                        organeDao.create(organes);
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

        private ProgressDialog Dialog = new ProgressDialog(GetDataActivity.this);

        protected void onPreExecute() {
            Dialog.setMessage("Récupération des Sous organes...");
            Dialog.show();
        }

        // Call after onPreExecute method
        protected Void doInBackground(String... urls) {
            Retrofit retrofit = RetrofitBuilder.getRetrofit("http://192.168.1.107:8080" + "/");
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
                        //This is the way to insert data into a database table
                        sousorganeDao.create(sousorganes);
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
                editor.putString("com.royken.url",txt);
                editor.commit();
                Toast.makeText(getApplicationContext(),txt,Toast.LENGTH_LONG).show();
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
