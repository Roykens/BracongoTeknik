package com.royken.teknik.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.royken.teknik.R;
import com.royken.teknik.adapter.BlocAdapter;
import com.royken.teknik.adapter.ZoneAdapter;
import com.royken.teknik.database.DatabaseHelper;
import com.royken.teknik.entities.Bloc;
import com.royken.teknik.entities.Reponse;
import com.royken.teknik.entities.Utilisateur;
import com.royken.teknik.entities.Zone;

import java.sql.SQLException;
import java.util.List;

public class BlocActivity extends AppCompatActivity {

    private static final String ARG_ZONEID = "zoneId";
    private static final String ARG_BLOCID = "blocId";
    private List<Bloc> blocs;
    private GridView gridView;
    private DatabaseHelper databaseHelper = null;
    private BlocAdapter blocAdapter;
    public static final String PREFS_NAME = "com.royken.teknik.MyPrefsFile";
    SharedPreferences settings ;
    private List<Reponse> reponses;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repartition);
        getSupportActionBar().setTitle("Choix de Bloc");
        getSupportActionBar().setSubtitle("");
        gridView = (GridView)findViewById(R.id.gridView);
        Intent intent = getIntent();
        int zoneId = 0;
        if (intent != null) {
            zoneId = intent.getIntExtra(ARG_ZONEID,0);
        }
        final Dao<Bloc, Integer> blocDao;
        try {
            blocDao =  getHelper().getBlocDao();
            blocs = blocDao.queryBuilder().where().eq("idZone", zoneId).query();
            blocAdapter = new BlocAdapter(this,blocs);
            gridView.setAdapter(blocAdapter);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 //   Toast.makeText(getApplicationContext(), blocs.get(position).getNom(), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(BlocActivity.this,
                            OrganeActivity.class);
                    intent.putExtra(ARG_BLOCID,blocs.get(position).getIdServeur());
                    startActivity(intent);
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_exporter) {
            Dao<Utilisateur, Integer> userDao;
            try {
                userDao = getHelper().getUtilisateurDao();
                settings = getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
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
                    final Dao<Reponse, Integer> reponseDao;
                    reponseDao = getHelper().getReponseDao();
                    reponses = reponseDao.queryForAll();
                    ExportData data = new ExportData(this, reponses);
                    data.exportReponse();
                    Toast.makeText(this, "Données exportées avec succès", Toast.LENGTH_LONG).show();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return true;
        }

        if(id == R.id.menu_deconnexion){
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            dialogBuilder.setTitle("DECONNEXION");
            dialogBuilder.setMessage("Veuillez confirmer la connexion");
            dialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("com.royken.login", "");
                    editor.putInt("com.royken.userId", 0);
                    editor.putBoolean("com.royken.haslogged", false);
                    editor.commit();
                    Intent intent = new Intent(BlocActivity.this, Login.class);
                    startActivity(intent);
                    BlocActivity.this.finish();
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
            databaseHelper = new DatabaseHelper(this);
        }
        return databaseHelper;
    }
}
