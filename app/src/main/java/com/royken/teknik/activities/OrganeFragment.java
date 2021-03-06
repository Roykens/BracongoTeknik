package com.royken.teknik.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.royken.teknik.R;
import com.royken.teknik.adapter.BlocAdapter;
import com.royken.teknik.adapter.OrganeAdapter;
import com.royken.teknik.adapter.OrganeItemAdapter;
import com.royken.teknik.database.DatabaseHelper;
import com.royken.teknik.entities.Bloc;
import com.royken.teknik.entities.Element;
import com.royken.teknik.entities.Organe;
import com.royken.teknik.entities.Reponse;
import com.royken.teknik.entities.SousOrgane;
import com.royken.teknik.entities.Utilisateur;
import com.royken.teknik.entities.Zone;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import android.widget.SearchView;
import android.widget.Toolbar;

public class OrganeFragment extends ListFragment implements AdapterView.OnItemClickListener, SearchView.OnQueryTextListener{
    private static final String ARG_BLOCID = "blocId";
    private static final String ARG_ORGANEID = "organeId";
    private static final String ARG_ZONEID = "zoneId";
    private static final String ARG_HORAIRE = "horaireId";
    private static final String ARG_CAHIERID = "cahierId";
    private static final String ARG_PARAM1 = "chemin";
    private DatabaseHelper databaseHelper = null;
    private OrganeAdapter organeAdapter;
    private OrganeItemAdapter organeItemAdapter;
    private List<Organe> organes;
    List<Organe> organesFiltres;
    public static final String PREFS_NAME = "com.royken.teknik.MyPrefsFile";
    SharedPreferences settings ;
    Dao<Utilisateur, Integer> userDao;

    // TODO: Rename and change types of parameters
    private String chemin;
    private String mParam2;
    String cahierId; // J'utilise le code du cahier comme ID
    int horaireId;

    private OnFragmentInteractionListener mListener;
    private SearchView mSearchView;

    Toolbar toolbar;
    TextView user;
    TextView title;
    String cheminS;

    Utilisateur u;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrganeFragment newInstance(String cahier, int horaire,String chemin) {
        OrganeFragment fragment = new OrganeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CAHIERID, cahier);
        args.putInt(ARG_HORAIRE, horaire);
        args.putString(ARG_PARAM1, chemin);
        fragment.setArguments(args);
        return fragment;
    }

    public OrganeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cahierId = getArguments().getString(ARG_CAHIERID);
            horaireId = getArguments().getInt(ARG_HORAIRE);
            chemin = getArguments().getString(ARG_PARAM1);
        }
    }



    @Override
    public void onViewCreated (View view, Bundle savedInstanceState) {
        getListView().setTextFilterEnabled(true);
        setupSearchView();
        title.setText("Organes");
        user.setText(u.getNom());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_organe, container, false);
        //gridView = (GridView)view.findViewById(R.id.gridView);
        mSearchView=(SearchView) view.findViewById(R.id.searchView);
        //toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        AppBarLayout bar = (AppBarLayout)getActivity().findViewById(R.id.appbar);
        CoordinatorLayout coor = (CoordinatorLayout)getActivity().findViewById(R.id.coord);
        coor.setBackgroundColor(Color.parseColor("#f3f3f3f3"));

        user = (TextView) bar.findViewById(R.id.user);
        title = (TextView) bar.findViewById(R.id.title);
        TextView chemin = (TextView) view.findViewById(R.id.chemin);


        final Dao<Bloc, Integer> blocDao;
        final Dao<Organe, Integer> organeDao;
        Dao<Element, Integer> elementDao;
        Dao<SousOrgane, Integer> sousOrganeDao;
        List<Zone> zones = new ArrayList<>();
        final Dao<Zone , Integer> zoneDao;
        try {
            userDao = getHelper().getUtilisateurDao();
            zoneDao = getHelper().getZoneDao();
            settings = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            int userId = settings.getInt("com.royken.userId", 0);
            u = userDao.queryForId(userId);

                zones = zoneDao.queryBuilder().where().like("cahierCode", cahierId).query();
                chemin.setText("Accueil>Traitement Eau");
                cheminS = "Traitement Eau";

            blocDao = getHelper().getBlocDao();
            List<Bloc> blocss = new ArrayList<>();
            for (Zone z : zones) {
                List<Bloc> blocs = blocDao.queryBuilder().where().eq("idZone", z.getIdServeur()).query();
                for (Bloc b : blocs){
                    blocss.add(b);
                }
            }

            organeDao =  getHelper().getOrganeDao();
            organes = new ArrayList<>();
            for (Bloc b : blocss){
                List<Organe> org = organeDao.queryBuilder().where().eq("idBloc", b.getIdServeur()).query();
                for (Organe o : org){
                    organes.add(o);
                }
            }

            /***********///////////////////////////
            elementDao =  getHelper().getElementDao();
            sousOrganeDao = getHelper().getSousOrganeDao();
            // Toast.makeText(getActivity(),"ID : "+ organeId,Toast.LENGTH_LONG).show();

            List<SousOrgane> sos = new ArrayList<>();

            for (Organe o : organes){
                List<SousOrgane> sorg = sousOrganeDao.queryBuilder().where().eq("idOrgane",o.getIdServeur()).query();
                for (SousOrgane so : sorg){
                    sos.add(so);
                }
            }


            List<Element> elements = new ArrayList<>();
            for (SousOrgane so : sos){
                List<Element> els = elementDao.queryBuilder().where().eq("sousOrganeId", so.getIdServeur()).and().eq("periode",horaireId).query();
                for (Element e : els){
                    elements.add(e);
                }
            }
           /* Log.i("ELEMENTS =======",elements.size()+ " == ELMENTS");
            Log.i("ORGANES =======",organes.size()+ " ===== ORGANES");
            Log.i("CAHIER CODE =======",cahierId + " ===== CAHIER CODE");
            Log.i("ZONES =======",zones.size() + " ===== ZONES");
            Log.i("BLOCS =======",blocss.size() + " ===== BLOCS");
            */
            List<Element> elementsFiltres = reduceListElementToSingleSousOrgane(elements);
            List<SousOrgane> sousOrganesFiltres = listElementConstainsOneElementBySorgane(sos, elementsFiltres);
            organesFiltres = listOrganeFiltres(organes, sousOrganesFiltres);
            //*****/////////////////////////////////
            organeItemAdapter = new OrganeItemAdapter(getActivity(),organesFiltres);
            setListAdapter(organeItemAdapter);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
       Fragment fragment = ElementListFragment.newInstance(((Organe)organeItemAdapter.getItem(position)).getIdServeur(),horaireId,chemin + ">" +((Organe)organeItemAdapter.getItem(position)).getNom());

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.mainFrame, fragment);
        ft.addToBackStack(null);
        ft.commit();

        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            // mListener.onFragmentInteraction(boissons1.get(position).getId());
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }


    private DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            //databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
            databaseHelper = new DatabaseHelper(getActivity());
        }
        return databaseHelper;
    }

    private void setupSearchView()
    {
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setQueryHint("Recherche");
    }

    @Override
    public boolean onQueryTextChange(String newText)
    {

        if (TextUtils.isEmpty(newText)) {
            getListView().clearTextFilter();
        } else {
            getListView().setFilterText(newText);
        }
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query)
    {
        return false;
    }

    /**
     * Prend un seul élément par sous organe
     * */
    private List<Element> reduceListElementToSingleSousOrgane(List<Element> elements){
        List<Element> result = new ArrayList<>();
        for (Element e : elements){
            if(!listElementConstainsOneElement(e,result))
                result.add(e);
        }
        return result;
    }

    /* Si un element est contenu dans une liste d'éléments
    * */
    private boolean listElementConstainsOneElement(Element e, List<Element> elements){
        if(elements.size() < 1)
            return false;
        for (Element el : elements){
            if(el.getSousOrganeId() == e.getSousOrganeId())
                return true;
        }
        return false;
    }

    /***
     * La liste des Sous organes qui ont au moins un élément dans une une liste d'éléments
     */
    private List<SousOrgane>listElementConstainsOneElementBySorgane(List<SousOrgane> sousOrganes, List<Element> elements){
        List<SousOrgane> result = new ArrayList<>();
        for (SousOrgane so : sousOrganes){
            if(sousOrganeHasElement(so,elements))
                result.add(so);
        }
        return result;

    }

    /**
     * Un sous organe a au moins un élément dans une liste d'éléments
     */
    private boolean sousOrganeHasElement(SousOrgane so, List<Element> elements){
        for (Element e : elements){
            if(e.getSousOrganeId() == so.getIdServeur())
                return true;
        }
        return false;
    }

    /* Si un organe est contenu dans une liste d'organes
     * */
    private boolean listOrganesConstainsOneOrgane(Organe o, List<Organe> organes){
        if(organes.size() < 1)
            return false;
        for (Organe or : organes){
            if(or.getIdServeur() == o.getIdServeur())
                return true;
        }
        return false;
    }

    private boolean organeHasSousOrgane(Organe o, List<SousOrgane> sousOrganes){
        for (SousOrgane so : sousOrganes){
            if(so.getIdOrgane() == o.getIdServeur())
                return true;
        }
        return false;
    }

    private List<Organe>listOrganeFiltres(List<Organe> organes, List<SousOrgane> sousOrganes){
        List<Organe> result = new ArrayList<>();
        for (Organe o : organes){
            if(organeHasSousOrgane(o,sousOrganes))
                result.add(o);
        }
        return result;

    }
}
