package com.royken.teknik.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.royken.teknik.R;
import com.royken.teknik.entities.Organe;

import java.util.List;

/**
 * Created by royken on 03/01/17.
 */
public class OrganeAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<Organe> organes;

    public OrganeAdapter(Context mContext, List<Organe> organes) {
        this.mContext = mContext;
        this.organes = organes;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return organes.size();
    }

    @Override
    public Object getItem(int position) {
        return organes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return organes.get(position).getId();
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
        nom.setText(organes.get(position).getNom());
        nom.setTag(position);
        return layout;
    }
}
