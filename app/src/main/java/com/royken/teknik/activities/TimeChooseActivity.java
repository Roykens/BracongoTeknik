package com.royken.teknik.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.royken.teknik.R;
import com.royken.teknik.database.DatabaseHelper;

public class TimeChooseActivity extends AppCompatActivity {

    private static final String ARG_ORGANEID = "organeId";
    private static final String ARG_HORAIRE = "horaireId";
    private static final String ARG_CAHIERID = "cahierId";

    private Button b1;
    private Button b2;
    private Button b3;
    private Button b4;
    int organeId ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_choose);
        getSupportActionBar().setTitle("Choix de la période");
        getSupportActionBar().setSubtitle("");
        Intent intent = getIntent();
        if (intent != null) {
            organeId = intent.getIntExtra(ARG_ORGANEID,0);
        }

        b1 = (Button)findViewById(R.id.button2);
        b2 = (Button)findViewById(R.id.button3);
        b3 = (Button)findViewById(R.id.button4);
        b4 = (Button)findViewById(R.id.button5);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplication(),"Aucune donnée",Toast.LENGTH_LONG).show();
            }
        });

        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplication(),"Aucune donnée",Toast.LENGTH_LONG).show();
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TimeChooseActivity.this, ElementActivity.class);
                intent.putExtra(ARG_ORGANEID,organeId);
                intent.putExtra(ARG_HORAIRE,0);
                startActivity(intent);
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TimeChooseActivity.this, ElementActivity.class);
                intent.putExtra(ARG_ORGANEID,organeId);
                intent.putExtra(ARG_HORAIRE,3);
                startActivity(intent);
            }
        });


    }
}
