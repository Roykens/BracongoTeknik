package com.royken.teknik.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.j256.ormlite.dao.Dao;
import com.royken.teknik.R;
import com.royken.teknik.database.DatabaseHelper;
import com.royken.teknik.entities.Element;
import com.royken.teknik.entities.Organe;
import com.royken.teknik.entities.Reponse;
import com.royken.teknik.entities.SousOrgane;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by royken on 20/07/17.
 */
public class ElementItemAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<Element> elements;
    private List<Element> orig;

    private DatabaseHelper databaseHelper = null;
    private Dao<Reponse,Integer> reponseDao;
    private Dao<Element,Integer> elementDao;
    SousOrgane so = null;
    private Dao<SousOrgane,Integer> sousoDao;


    public ElementItemAdapter(Context context, List<Element> elements) {
        mContext = context;
        this.elements = elements;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return elements.size();
    }

    @Override
    public Object getItem(int position) {
        return elements.get(position);
    }

    @Override
    public long getItemId(int position) {
        return elements.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FrameLayout layout;
        if (convertView == null) {
            layout = (FrameLayout) mInflater.inflate(R.layout.element_item, parent, false);
        } else {
            layout = (FrameLayout) convertView;
        }
        /*ImageView img = (ImageView)layout.findViewById(R.id.image_view);
        String firstLetter = String.valueOf(elements.get(position).getNom().charAt(0));

        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
        // generate random color
        int color = generator.getRandomColor();
        TextDrawable drawable = TextDrawable.builder().buildRound(firstLetter, color);

        img.setImageDrawable(drawable);
        */
        try {
            sousoDao = getHelper().getSousOrganeDao();
            so = sousoDao.queryBuilder().where().eq("idServeur", elements.get(position).getSousOrganeId()).queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        TextView tv_Code = (TextView)layout.findViewById(R.id.elementCode);
        tv_Code.setText(elements.get(position).getCode());
        TextView tv_Nom = (TextView)layout.findViewById(R.id.elementNom);
        tv_Nom.setText(elements.get(position).getNom());
        TextView tv_Heure = (TextView)layout.findViewById(R.id.elementHeure);
        TextView tv_SousOrgane = (TextView)layout.findViewById(R.id.nomSousOrgane);
        tv_SousOrgane.setText(so.getNom());
        try {
            Element e = elements.get(position);
            reponseDao = getHelper().getReponseDao();
            Reponse r = reponseDao.queryBuilder()
                    .orderBy("id", false).where().eq("idElement", e.getId()).queryForFirst();
            if(r != null){
                tv_Heure.setText(""+getDateString(r.getDate()) );
            }
            else {
                tv_Heure.setText("");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return layout;
    }

    private DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            //databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
            databaseHelper = new DatabaseHelper(mContext);
        }
        return databaseHelper;
    }

    private String getDateString(Date date){
        Calendar gc = new GregorianCalendar();
        gc.setTime(date);
        String result = gc.get(Calendar.DAY_OF_MONTH)+"/"+(gc.get(Calendar.MONTH)+1)+"/"+gc.get(Calendar.YEAR)+"   "+gc.get(Calendar.HOUR_OF_DAY)+":"+gc.get(Calendar.MINUTE);
        return result;
    }

    @Override
    public boolean isEmpty() {
        return elements.isEmpty();
    }

    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<Element> results = new ArrayList<Element>();
                if (orig == null)
                    orig = elements;
                if (constraint != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final Element g : orig) {
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
                elements = (ArrayList<Element>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}
