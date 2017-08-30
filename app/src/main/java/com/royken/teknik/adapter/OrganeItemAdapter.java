package com.royken.teknik.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.royken.teknik.R;
import com.royken.teknik.entities.Organe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by royken on 21/07/17.
 */
public class OrganeItemAdapter extends BaseAdapter implements Filterable {

    private List<Organe> organes;
    private List<Organe> orig;
    private Context mContext;
    private LayoutInflater mInflater;

    public OrganeItemAdapter(Context mContext, List<Organe> organes) {
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
        FrameLayout layout;
        if (convertView == null) {
            layout = (FrameLayout) mInflater.inflate(R.layout.organe_list_item, parent, false);
        } else {
            layout = (FrameLayout) convertView;
        }

        ImageView img = (ImageView)layout.findViewById(R.id.image_view);
       // ImageView img2 = (ImageView)layout.findViewById(R.id.image_view1);
        String firstLetter = String.valueOf(organes.get(position).getNom().charAt(0));

        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
        // generate random color
        int color = generator.getRandomColor();
        //int color = generator.getRandomColor();
        Log.i("CARACTER",firstLetter);
        TextDrawable drawable = TextDrawable.builder()
                .buildRound(firstLetter, color);

        img.setImageDrawable(drawable);

        TextView tv_Nom = (TextView)layout.findViewById(R.id.nomOrgane);
        tv_Nom.setText(organes.get(position).getNom());

        tv_Nom.setTag(position);
        img.setTag(position);
        return layout;
    }



    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<Organe> results = new ArrayList<Organe>();
                if (orig == null)
                    orig = organes;
                if (constraint != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final Organe g : orig) {
                            if (g.getNom().toLowerCase()
                                    .contains(constraint.toString()))
                                results.add(g);
                        }
                    }
                    oReturn.values = results;
                }
                return oReturn;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
                organes = (ArrayList<Organe>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public boolean isEmpty() {
        return organes.isEmpty();
    }
}
