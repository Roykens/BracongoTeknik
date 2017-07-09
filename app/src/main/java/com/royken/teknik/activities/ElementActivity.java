package com.royken.teknik.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.royken.teknik.R;
import com.royken.teknik.adapter.ElementAdapter;
import com.royken.teknik.adapter.SousOrganeAdapter;
import com.royken.teknik.database.DatabaseHelper;
import com.royken.teknik.entities.Bloc;
import com.royken.teknik.entities.Element;
import com.royken.teknik.entities.Organe;
import com.royken.teknik.entities.Reponse;
import com.royken.teknik.entities.SousOrgane;
import com.royken.teknik.entities.Utilisateur;
import com.royken.teknik.entities.Zone;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by royken on 03/01/17.
 */
public class ElementActivity extends AppCompatActivity {

    private static final String ARG_SOUSORGANEID = "sousOrganeId";
    private static final String ARG_ZONEID = "zoneId";
    private static final String ARG_ORGANEID = "organeId";
    private static final String ARG_HORAIRE = "horaireId";
    private static final String ARG_CAHIERID = "cahierId";
    private DatabaseHelper databaseHelper = null;
    private ElementAdapter elementAdapter;
    private List<Element> elements;
    private ListView listView;
    public static final String PREFS_NAME = "com.royken.teknik.MyPrefsFile";
    SharedPreferences settings ;
    private List<Reponse> reponses;
    Dao<Utilisateur, Integer> userDao;
    private Dao<Reponse, Integer> reponseDao;
    private boolean isExporting = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.element_activity);
        getSupportActionBar().setTitle("Elements");
        getSupportActionBar().setSubtitle("");
        listView = (ListView)findViewById(android.R.id.list);
        Intent intent = getIntent();
        //int sousorganeId = 0;
       // int zoneId = 0;
        int organeId = 0;
        int horaire = 0;
        int cahierId = 0;
        if (intent != null) {
            //sousorganeId = intent.getIntExtra(ARG_SOUSORGANEID,0);
            //zoneId = intent.getIntExtra(ARG_ZONEID,0);
           organeId = intent.getIntExtra(ARG_ORGANEID,0);
            horaire = intent.getIntExtra(ARG_HORAIRE,0);
            cahierId = intent.getIntExtra(ARG_CAHIERID,0);
        }
        final Dao<Element, Integer> elementDao;
        final Dao<Bloc, Integer> blocDao;
        final Dao<Organe,Integer> organeDao;
        final Dao<SousOrgane , Integer> sousOrganeDao;
        final Dao<Zone , Integer> zoneDao;
        try {
            userDao = getHelper().getUtilisateurDao();
            settings = getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            int userId = settings.getInt("com.royken.userId", 0);
            Utilisateur u = userDao.queryForId(userId);
            elementDao =  getHelper().getElementDao();
            blocDao = getHelper().getBlocDao();
            organeDao = getHelper().getOrganeDao();
            zoneDao = getHelper().getZoneDao();
            sousOrganeDao = getHelper().getSousOrganeDao();
            List<Zone> zones = new ArrayList<>();
           /* if(u.getRole().equalsIgnoreCase("admin")){
                zones= zoneDao.queryForAll();
            }
            */
           // else {
                if(cahierId == 0){
                    zones = zoneDao.queryBuilder().where().like("cahierCode", "OTE").query();
                }
                if(cahierId == 1) {
                    zones = zoneDao.queryBuilder().where().like("cahierCode", "GPE").or().like("cahierCode", "ELE").or().like("cahierCode", "CHA").or().like("cahierCode", "COM").query();
                }
            if(cahierId == 2){
                zones = zoneDao.queryBuilder().where().like("cahierCode", "UAG").query();
            }
            if(cahierId == 3) {
                zones = zoneDao.queryBuilder().where().like("cahierCode", "AIR").or().like("cahierCode", "FRO").or().like("cahierCode", "EAU").or().like("cahierCode", "CO2").query();
            }

           // }
            List<Bloc> blocss = new ArrayList<>();
            for (Zone z : zones) {
                List<Bloc> blocs = blocDao.queryBuilder().where().eq("idZone", z.getIdServeur()).query();
                for (Bloc b : blocs){
                    blocss.add(b);
                }
            }
            List<Organe> organes = new ArrayList<>();
            for (Bloc b : blocss){
                List<Organe> org = organeDao.queryBuilder().where().eq("idBloc", b.getIdServeur()).query();
                for (Organe o : org){
                    organes.add(o);
                }
            }

            List<SousOrgane> sos = new ArrayList<>();
            //sos = sousOrganeDao.queryBuilder().where().eq("idOrgane",organeId).query();
            for (Organe o : organes){
                List<SousOrgane> so = sousOrganeDao.queryBuilder().where().eq("idOrgane",o.getIdServeur()).query();
                for (SousOrgane s: so){
                    sos.add(s);
                }
            }

            elements = new ArrayList<>();
            for (SousOrgane so : sos){
                List<Element> els = elementDao.queryBuilder().where().eq("sousOrganeId", so.getIdServeur()).and().eq("periode",horaire).query();
                for (Element e : els){
                    elements.add(e);
                }
            }

            //elements = elementDao.queryBuilder().where().eq("sousOrganeId", sousorganeId).query();
            elementAdapter = new ElementAdapter(this,elements);

            listView.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
            listView.setAdapter(elementAdapter);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

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
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                    dialogBuilder.setTitle(" !!!! EXPORTATION");
                    dialogBuilder.setMessage("Veuillez confirmer l'exportation. Celà supprimera vos données");
                    dialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            if(isExporting  == false){
                                isExporting = true;
                                try {
                                    long offset = settings.getLong("com.royken.offset", 0);
                                    reponseDao = getHelper().getReponseDao();
                                    long nombre = reponseDao.countOf();
                                    if (offset == 0) {
                                        reponses = reponseDao.queryForAll();
                                    } else {
                                        reponses = reponseDao.queryBuilder().offset(offset).limit(nombre - offset).query();
                                    }

                                    SharedPreferences.Editor editor = settings.edit();
                                    editor.putLong("com.royken.offset", nombre);
                                    editor.commit();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }

                                ExportData data = new ExportData(getApplicationContext(), reponses);
                                data.exportReponse();
                                Toast.makeText(getApplicationContext(), "Données exportées avec succès", Toast.LENGTH_LONG).show();
                                isExporting = false;
                            }   }
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
            dialogBuilder.setMessage("Veuillez confirmer la deconnexion");
            dialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("com.royken.login", "");
                    editor.putInt("com.royken.userId", 0);
                    editor.putBoolean("com.royken.haslogged", false);
                    editor.commit();
                    Intent intent = new Intent(ElementActivity.this, Login.class);
                    startActivity(intent);
                    ElementActivity.this.finish();
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

}
