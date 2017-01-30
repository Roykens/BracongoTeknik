package com.royken.teknik.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.royken.teknik.R;
import com.royken.teknik.entities.SousOrgane;

import java.util.List;

/**
 * Created by royken on 03/01/17.
 */
public class SousOrganeAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<SousOrgane> sousOrganes;

    public SousOrganeAdapter(Context mContext, List<SousOrgane> sousOrganes) {
        this.mContext = mContext;
        this.sousOrganes = sousOrganes;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return sousOrganes.size();
    }

    @Override
    public Object getItem(int position) {
        return sousOrganes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return sousOrganes.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout layout;
        if (convertView == null) {
            layout = (LinearLayout) mInflater.inflate(R.layout.card_repartition_item, parent, false);
        } else {
            layout = (LinearLayout) convertView;
        }
        TextView nom = (TextView)layout.findViewById(R.id.nom);
        nom.setText(sousOrganes.get(position).getNom());
        nom.setTag(position);
        return layout;
    }
}
