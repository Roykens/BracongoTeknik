package com.royken.teknik.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.royken.teknik.R;
import com.royken.teknik.activities.OrganeActivity;
import com.royken.teknik.activities.TimeChooseActivity;
import com.royken.teknik.database.DatabaseHelper;
import com.royken.teknik.entities.Reponse;
import com.royken.teknik.entities.Utilisateur;

import java.sql.SQLException;
import java.util.List;

public class CahierActivity extends AppCompatActivity {


    private static final String ARG_ZONEID = "zoneId";
    private static final String ARG_CAHIERID = "cahierId";
    private DatabaseHelper databaseHelper = null;
    public static final String PREFS_NAME = "com.royken.teknik.MyPrefsFile";
    SharedPreferences settings ;
    Dao<Utilisateur, Integer> userDao;

    private Button tratEau;
    private Button electricite;
    private Button mecanique;
    //private Button eau;
    private Button usineAglace;
    //private Button compresseur;
    Utilisateur u;
    private Dao<Reponse, Integer> reponseDao;
    private List<Reponse> reponses;
    private boolean isExporting = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cahier);
        tratEau = (Button)findViewById(R.id.button2);
        electricite = (Button)findViewById(R.id.button3);
        //elect = (Button)findViewById(R.id.button4);
        //chaud = (Button)findViewById(R.id.button5);
        //air = (Button)findViewById(R.id.button6);
        mecanique = (Button)findViewById(R.id.button5);
        //eau = (Button)findViewById(R.id.button8);
        usineAglace = (Button)findViewById(R.id.button4);
        //compresseur = (Button)findViewById(R.id.button10);

        try {
            userDao = getHelper().getUtilisateurDao();
            settings = getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            int userId = settings.getInt("com.royken.userId", 0);
           u = userDao.queryForId(userId);



        tratEau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // if(u.getRole().equalsIgnoreCase("admin") || u.getCahier().equalsIgnoreCase("OTE")){
                    Intent intent = new Intent(CahierActivity.this, TimeChooseActivity.class);
                    intent.putExtra(ARG_CAHIERID, 0);
                    startActivity(intent);

             //   }
               /* else {
                    Toast.makeText(getApplicationContext(),"Vous n'avez pas les droits",Toast.LENGTH_LONG).show();
                }*/
            }
        });

        electricite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  if (u.getRole().equalsIgnoreCase("admin") || u.getCahier().equalsIgnoreCase("GPE")) {
                    Intent intent = new Intent(CahierActivity.this, TimeChooseActivity.class);
                    intent.putExtra(ARG_CAHIERID, 1);
                    startActivity(intent);

              //  }
               /* else {
                    Toast.makeText(getApplicationContext(), "Vous n'avez pas les droits", Toast.LENGTH_LONG).show();
                }
                */

            }
        });

            /*
        elect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(u.getRole().equalsIgnoreCase("admin") || u.getCahier().equalsIgnoreCase("ELE") || u.getCahier().equalsIgnoreCase("GPE")){
                    Intent intent = new Intent(CahierActivity.this, TimeChooseActivity.class);
                    intent.putExtra(ARG_CAHIERID, 2);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getApplicationContext(),"Vous n'avez pas les droits",Toast.LENGTH_LONG).show();
                }

            }
        });

        chaud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(u.getRole().equalsIgnoreCase("admin") || u.getCahier().equalsIgnoreCase("CHA") || u.getCahier().equalsIgnoreCase("GPE")){
                    Intent intent = new Intent(CahierActivity.this, TimeChooseActivity.class);
                    intent.putExtra(ARG_CAHIERID, 3);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getApplicationContext(),"Vous n'avez pas les droits",Toast.LENGTH_LONG).show();
                }

            }
        });

        air.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(u.getRole().equalsIgnoreCase("admin") || u.getCahier().equalsIgnoreCase("AIR")){
                    Intent intent = new Intent(CahierActivity.this, TimeChooseActivity.class);
                    intent.putExtra(ARG_CAHIERID, 4);
                    startActivity(intent);

                }
                else {
                    Toast.makeText(getApplicationContext(),"Vous n'avez pas les droits",Toast.LENGTH_LONG).show();
                }

            }
        });
            */

        mecanique.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /* if (u.getRole().equalsIgnoreCase("admin") || u.getCahier().equalsIgnoreCase("FRO")) {
                    Intent intent = new Intent(CahierActivity.this, TimeChooseActivity.class);
                    intent.putExtra(ARG_CAHIERID, 2);
                    startActivity(intent);

                } else {
                    Toast.makeText(getApplicationContext(), "Vous n'avez pas les droits", Toast.LENGTH_LONG).show();
                }
                */
                Intent intent = new Intent(CahierActivity.this, TimeChooseActivity.class);
                intent.putExtra(ARG_CAHIERID, 3);
                startActivity(intent);

            }
        });

      /*  eau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(u.getRole().equalsIgnoreCase("admin") || u.getCahier().equalsIgnoreCase("EAU")){
                    Intent intent = new Intent(CahierActivity.this, TimeChooseActivity.class);
                    intent.putExtra(ARG_CAHIERID, 6);
                    startActivity(intent);

                }
                else {
                    Toast.makeText(getApplicationContext(),"Vous n'avez pas les droits",Toast.LENGTH_LONG).show();
                }

            }
        });
            */

        usineAglace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //  if(u.getRole().equalsIgnoreCase("admin") || u.getCahier().equalsIgnoreCase("UAG")){
                    Intent intent = new Intent(CahierActivity.this, TimeChooseActivity.class);
                    intent.putExtra(ARG_CAHIERID, 2);
                    startActivity(intent);

              //  }
               /* else {
                    Toast.makeText(getApplicationContext(),"Vous n'avez pas les droits",Toast.LENGTH_LONG).show();
                }
                */
               // Toast.makeText(getApplicationContext(),"Aucune donnée",Toast.LENGTH_LONG).show();

            }
        });

       /* compresseur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(u.getRole().equalsIgnoreCase("admin") || u.getCahier().equalsIgnoreCase("COM")){
                    Intent intent = new Intent(CahierActivity.this, TimeChooseActivity.class);
                    intent.putExtra(ARG_CAHIERID, 8);
                    startActivity(intent);

                }
                else {
                    Toast.makeText(getApplicationContext(),"Vous n'avez pas les droits",Toast.LENGTH_LONG).show();
                }

            }
        });
            */

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
                    Intent intent = new Intent(CahierActivity.this, Login.class);
                    startActivity(intent);
                    CahierActivity.this.finish();
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
