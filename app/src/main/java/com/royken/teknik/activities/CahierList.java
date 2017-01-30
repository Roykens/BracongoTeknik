package com.royken.teknik.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.royken.teknik.R;

public class CahierList extends AppCompatActivity {

    private String[] cahiers = {"RELEVE TRAITEMENT EAU","GROUPE ELECTROGENE", "ELECTRICITE", "CHAUD","AIR","FROID","EAU","USINE A GLACE","COMPRESSEURS 40b"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cahier_list);
        ArrayAdapter adapter = new ArrayAdapter(this,R.layout.cahier_list_item,cahiers);
        ListView list = (ListView)findViewById(R.id.listCahier);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CahierList.this,Zones.class);
                startActivity(intent);
            }
        });
    }
}
