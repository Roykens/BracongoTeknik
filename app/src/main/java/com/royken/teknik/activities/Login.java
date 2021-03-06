package com.royken.teknik.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.royken.teknik.R;
import com.royken.teknik.database.DatabaseHelper;
import com.royken.teknik.database.DatabaseOpenHelper;
import com.royken.teknik.entities.Bloc;
import com.royken.teknik.entities.Utilisateur;
import com.royken.teknik.network.RetrofitBuilder;
import com.royken.teknik.network.WebService;
import com.royken.teknik.util.PasswordUtil;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class Login extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    public static final String PREFS_NAME = "com.royken.teknik.MyPrefsFile";
    public static final String PREFS_LOGIN_NAME = "login";
    public static final String PREFS_PASSWD_NAME = "password";
    private boolean isValide;
    SharedPreferences.Editor editor;
    String login;
    String password;
    private String url;
    private int loggedUser;
    private DatabaseHelper databaseHelper = null;
    private EditText lgnTxt;
    private EditText passTxt;
    private Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        lgnTxt = (EditText)findViewById(R.id.input_login);
        passTxt = (EditText)findViewById(R.id.input_password);
        loginBtn = (Button)findViewById(R.id.btn_login);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              boolean value =   login(lgnTxt.getText().toString(),passTxt.getText().toString());
                if(value){
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    startActivity(intent);
                    Login.this.finish();
                }
            }
        });
    }

    private boolean login(String login, String mdp){
        final Dao<Utilisateur, Integer> userDao;
        try {
            userDao = getHelper().getUtilisateurDao();
           //Utilisateur u = userDao.queryBuilder().where().eq("login", login).and().eq("mdp", mdp).queryForFirst();
            Utilisateur u = userDao.queryBuilder().where().eq("login", login).queryForFirst();
           if(u == null){
               Toast.makeText(getApplicationContext(),"Utilisateur non trouvé",Toast.LENGTH_LONG).show();
           }
            else {
               String pass = PasswordUtil.getSecurePassword(mdp, Base64.decode(u.getSalt(),0));
               if(pass.equalsIgnoreCase(u.getMdp())){
                   SharedPreferences settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                   SharedPreferences.Editor editor = settings.edit();
                   editor.putString("com.royken.login", login);
                   editor.putInt("com.royken.userId", u.getId());
                   editor.putBoolean("com.royken.haslogged", true);
                   editor.commit();
               }
               else {
                   Toast.makeText(getApplicationContext(),"Login/MDP incorrect", Toast.LENGTH_LONG).show();
                   return false;
               }
               //Toast.makeText(getApplicationContext(),"Connecté : "+ u.getLogin(), Toast.LENGTH_LONG).show();
               return true;
           }

        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            //databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
            databaseHelper = new DatabaseHelper(this);
        }
        return databaseHelper;
    }
}