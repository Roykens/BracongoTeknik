package com.royken.teknik.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.royken.teknik.R;
import com.royken.teknik.entities.Bloc;

import java.util.List;

/**
 * Created by royken on 02/01/17.
 */
public class BlocAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<Bloc> blocs;

    public BlocAdapter(Context context, List<Bloc> blocs) {
        this.mContext = context;
        this.blocs = blocs;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return blocs.size();
    }

    @Override
    public Object getItem(int position) {
        return blocs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return blocs.get(position).getId();
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
        nom.setText(blocs.get(position).getNom());
        nom.setTag(position);
        return layout;
    }

    @Override
    public boolean isEmpty() {
        return blocs.isEmpty();
    }
}
