package com.royken.teknik.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.j256.ormlite.dao.Dao;
import com.royken.teknik.R;
import com.royken.teknik.database.DatabaseHelper;
import com.royken.teknik.entities.Element;
import com.royken.teknik.entities.Reponse;

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
    public View getView(int position, View convertView, ViewGroup parent) {
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
}
