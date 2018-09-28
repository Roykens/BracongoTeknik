package com.royken.teknik.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.j256.ormlite.dao.Dao;
import com.royken.teknik.R;
import com.royken.teknik.database.DatabaseHelper;
import com.royken.teknik.entities.Bloc;
import com.royken.teknik.entities.Organe;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by royken on 03/01/17.
 */
public class OrganeAdapter extends BaseAdapter {

    private DatabaseHelper databaseHelper = null;
    private Context mContext;
    private LayoutInflater mInflater;
    private List<Organe> organes;
    Bloc b = null;
    private Dao<Bloc,Integer> blocDao;

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
        try {
            blocDao = getHelper().getBlocDao();
            b = blocDao.queryBuilder().where().eq("idServeur", organes.get(position).getIdBloc()).queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        TextView nom = (TextView)layout.findViewById(R.id.nom);
        TextView bloc = (TextView)layout.findViewById(R.id.nomBloc);
        nom.setText(organes.get(position).getNom());
        Log.i("LE BLOCCCCCCCCC  : " , b.getNom());
        bloc.setText(b.getNom());
        nom.setTag(position);
        bloc.setTag(position);
        return layout;
    }

    private DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            //databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
            databaseHelper = new DatabaseHelper(mContext);
        }
        return databaseHelper;
    }
}
