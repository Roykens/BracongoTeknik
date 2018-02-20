package com.royken.teknik.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.royken.teknik.R;
import com.royken.teknik.database.DatabaseHelper;
import com.royken.teknik.entities.CahierDummy;
import com.royken.teknik.entities.Element;
import com.royken.teknik.entities.Reponse;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by royken on 20/07/17.
 */
public class ReponseAdapter extends BaseAdapter {

    private DatabaseHelper databaseHelper = null;
    private Context mContext;
    private LayoutInflater mInflater;
    private Dao<Reponse,Integer> reponseDao;
    private Dao<Element,Integer> elementDao;

    private List<Reponse> reponses;


    public ReponseAdapter(Context mContext, List<Reponse> reponses) {
        this.mContext = mContext;
        this.reponses = reponses;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return reponses.size();
    }

    @Override
    public Object getItem(int position) {
        return reponses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return reponses.get(position).getId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder ;
        final Reponse r = reponses.get(position);
        View v = convertView;

        if (v == null || v.getTag() == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.reponse_item,parent,false);
            holder.tv_Valeur = (TextView)convertView.findViewById(R.id.textValeur);
            holder.tv_Heure = (TextView)convertView.findViewById(R.id.textDate);
            holder.btn_Edit = (Button)convertView.findViewById(R.id.btnEDit);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.btn_Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position == 0)
                showChangeLangDialog(r);
            }
        });
        holder.btn_Edit.setTag(position);

        if(holder != null) {
            if(!r.isValeurCorrecte()){
                holder.tv_Valeur.setTextColor(Color.RED);
            }
            else{
                holder.tv_Valeur.setTextColor(Color.parseColor("#00796B"));
            }
            holder.tv_Valeur.setText(r.getValeur());
            holder.tv_Heure.setText(getDateString(r.getDate()));

        }
        return   convertView;
    }

    private String getDateString(Date date){
        Calendar gc = new GregorianCalendar();
        gc.setTime(date);
        String result = gc.get(Calendar.DAY_OF_MONTH)+"/"+(gc.get(Calendar.MONTH)+1)+"/"+gc.get(Calendar.YEAR)+"   "+gc.get(Calendar.HOUR_OF_DAY)+":"+gc.get(Calendar.MINUTE);
        return result;
    }

    @Override
    public boolean isEmpty() {
        return reponses.isEmpty();
    }

    public void showChangeLangDialog(final Reponse r) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext);
        //LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = mInflater.inflate(R.layout.custom_dialog, null);
        dialogBuilder.setView(dialogView);
        Log.i("REPONSE...BEFORE", r.toString());
        final EditText edt = (EditText) dialogView.findViewById(R.id.value);
        edt.setText(r.getValeur());
        dialogBuilder.setTitle("Modification Valeur");
        dialogBuilder.setMessage("Valeur");
        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //do something with edt.getText().toString();
                if(edt.getText().length() > 1 )
                    enregistrerValeur(r, edt.getText().toString().trim());
            }
        });
        dialogBuilder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    public class ViewHolder {

        TextView tv_Valeur;
        TextView tv_Heure;
        Button btn_Edit;
        int index;
    }

    private DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            databaseHelper = new DatabaseHelper(mContext);
        }
        return databaseHelper;
    }

    private void enregistrerValeur(Reponse r, String valeur){
        Log.i("REPONSE...UPDATE",r.toString());
        try {
            reponseDao = getHelper().getReponseDao();
            elementDao = getHelper().getElementDao();
            Element e = elementDao.queryBuilder().where().eq("idServeur",r.getIdElement()).queryForFirst();


            if (e.isHasBorn()) {
                if (!isNumeric(valeur)) {
                    GregorianCalendar gc = new GregorianCalendar();
                    gc.set(Calendar.HOUR_OF_DAY, gc.get(Calendar.HOUR_OF_DAY) - 1);
                    r.setDate(gc.getTime());
                    r.setValeurCorrecte(false);
                    r.setValeur(valeur);
                    reponseDao.update(r);
                    //holder.tv_Guide.setTextColor(Color.GREEN);
                    // holder.tv_Guide.setText("Enregistré");
                    Toast.makeText(mContext, "Enregistré : Valeur incorrecte", Toast.LENGTH_SHORT).show();
                   // updateUI();
                }

                else {

                    Double valeur1 = Double.parseDouble(valeur);

                    if (r != null) {

                        if (!isNumeric(r.getValeur())) {
                            GregorianCalendar gc = new GregorianCalendar();
                            gc.set(Calendar.HOUR_OF_DAY, gc.get(Calendar.HOUR_OF_DAY) - 1);
                            r.setDate(gc.getTime());
                            r.setValeurCorrecte(false);
                            r.setValeur(valeur);
                            reponseDao.update(r);
                            Toast.makeText(mContext,"Enregistré : : Valeur incorrecte",Toast.LENGTH_SHORT).show();
                            //updateUI();
                        }
                        ///////////////////////////////////////
                        else {

                            if (e.isCriteriaAlpha()) {
                                if (Double.parseDouble(r.getValeur()) > valeur1 || (Double.parseDouble(r.getValeur()) + e.getValMax()) < valeur1) {
                                    GregorianCalendar gc = new GregorianCalendar();
                                    gc.set(Calendar.HOUR_OF_DAY, gc.get(Calendar.HOUR_OF_DAY) - 1);
                                    r.setDate(gc.getTime());
                                    r.setValeurCorrecte(false);
                                    //      Log.i("CAHIEEEEEEERRRRRRRRR", cahier);
                                    r.setValeur(valeur + "");
                                    reponseDao.update(r);
                                    Toast.makeText(mContext,"Enregistré : Valeur incorrecte",Toast.LENGTH_SHORT).show();
                                    //updateUI();
                                } else {
                                    GregorianCalendar gc = new GregorianCalendar();
                                    gc.set(Calendar.HOUR_OF_DAY, gc.get(Calendar.HOUR_OF_DAY) - 1);
                                    r.setDate(gc.getTime());
                                    r.setValeurCorrecte(true);
                                    r.setValeur(valeur + "");
                                    reponseDao.update(r);
                                    Toast.makeText(mContext,"Enregistré",Toast.LENGTH_SHORT).show();
                                    //updateUI();
                                }
                            } else {

                                if (e.getValMin() > valeur1 || e.getValMax() < valeur1) {
                                    GregorianCalendar gc = new GregorianCalendar();
                                    gc.set(Calendar.HOUR_OF_DAY, gc.get(Calendar.HOUR_OF_DAY) - 1);
                                    r.setDate(gc.getTime());
                                    r.setValeurCorrecte(false);
                                    //  Log.i("CAHIEEEEEEERRRRRRRRR", cahier);
                                    r.setValeur(valeur + "");
                                    reponseDao.update(r);
                                   Toast.makeText(mContext,"Enregistré : Valeur incorrecte",Toast.LENGTH_SHORT).show();
                                   // updateUI();
                                } else {
                                    GregorianCalendar gc = new GregorianCalendar();
                                    gc.set(Calendar.HOUR_OF_DAY, gc.get(Calendar.HOUR_OF_DAY) - 1);
                                    r.setDate(gc.getTime());
                                    r.setValeurCorrecte(true);
                                    // Log.i("CAHIEEEEEEERRRRRRRRR", cahier);
                                    r.setValeur(valeur + "");
                                    reponseDao.update(r);
                                    Toast.makeText(mContext,"Enregistré",Toast.LENGTH_SHORT).show();
                                    //updateUI();
                                }
                            }

                        }
                    }
                }
            } else {
                GregorianCalendar gc = new GregorianCalendar();
                gc.set(Calendar.HOUR_OF_DAY, gc.get(Calendar.HOUR_OF_DAY) - 1);
                r.setDate(gc.getTime());
                r.setValeurCorrecte(true);
                r.setValeur(valeur);
                reponseDao.update(r);
                Toast.makeText(mContext,"Enregistré ",Toast.LENGTH_SHORT).show();
                //updateUI();

            }

        } catch (SQLException e1) {
            e1.printStackTrace();
        }


    }

    private boolean isNumeric(String str){
        return str.trim().matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
    }
}
