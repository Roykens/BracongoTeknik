package com.royken.teknik.activities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.royken.teknik.R;
import com.royken.teknik.adapter.ElementItemAdapter;
import com.royken.teknik.database.DatabaseHelper;
import com.royken.teknik.entities.Element;
import com.royken.teknik.entities.SousOrgane;
import com.royken.teknik.entities.Utilisateur;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ElementListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ElementListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ElementListFragment extends ListFragment implements SearchView.OnQueryTextListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_ORGANEID = "organeId";
    private static final String ARG_HORAIRE = "horaireId";
    private static final String ARG_ELEMENT = "elementId";

    public static final String PREFS_NAME = "com.royken.teknik.MyPrefsFile";
    SharedPreferences settings ;

    private DatabaseHelper databaseHelper = null;
    Dao<Utilisateur, Integer> userDao;
    Dao<Element, Integer> elementDao;
    Dao<SousOrgane, Integer> sousOrganeDao;

    private List<Element> elements;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    int cahierId;
    int horaireId;
    int organeId;

    TextView user;
    TextView title;

    Utilisateur u;

    private OnFragmentInteractionListener mListener;
    private SearchView mSearchView;

    private ElementItemAdapter elementItemAdapter;
    String chemins;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param organeId Parameter 1.
     * @param horaire Parameter 2.
     * @return A new instance of fragment ElementListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ElementListFragment newInstance(int organeId, int horaire, String mParam2) {
        ElementListFragment fragment = new ElementListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ORGANEID, organeId);
        args.putInt(ARG_HORAIRE, horaire);
        args.putString(ARG_PARAM2, mParam2);

        fragment.setArguments(args);
        return fragment;
    }

    public ElementListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated (View view, Bundle savedInstanceState) {
        getListView().setTextFilterEnabled(true);
        setupSearchView();
        AppBarLayout bar = (AppBarLayout)getActivity().findViewById(R.id.appbar);
        user = (TextView) bar.findViewById(R.id.user);
        title = (TextView) bar.findViewById(R.id.title);
        title.setText("Eléments");
        user.setText(u.getNom());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
           // mParam1 = getArguments().getString(ARG_PARAM1);
           // mParam2 = getArguments().getString(ARG_PARAM2);
            organeId = getArguments().getInt(ARG_ORGANEID);
            horaireId = getArguments().getInt(ARG_HORAIRE);
            chemins = getArguments().getString(ARG_PARAM2);
        }
        try {
            userDao = getHelper().getUtilisateurDao();
            settings = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            int userId = settings.getInt("com.royken.userId", 0);
            u = userDao.queryForId(userId);
            elementDao =  getHelper().getElementDao();
            sousOrganeDao = getHelper().getSousOrganeDao();
           // Toast.makeText(getActivity(),"ID : "+ organeId,Toast.LENGTH_LONG).show();

           List<SousOrgane> sos = sousOrganeDao.queryBuilder().where().eq("idOrgane",organeId).query();


            elements = new ArrayList<>();
            for (SousOrgane so : sos){
                List<Element> els = elementDao.queryBuilder().where().eq("sousOrganeId", so.getIdServeur()).and().eq("periode",horaireId).query();
                for (Element e : els){
                    elements.add(e);
                }
            }
            setHasOptionsMenu(true);

            elementItemAdapter = new ElementItemAdapter(getActivity(),elements);
            setListAdapter(elementItemAdapter);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Fragment fragment = ElementFragment.newInstance(elements.get(position).getId());
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_element_list, container, false);
        mSearchView=(SearchView) view.findViewById(R.id.searchView);
        CoordinatorLayout coor = (CoordinatorLayout)getActivity().findViewById(R.id.coord);
        coor.setBackgroundColor(Color.parseColor("#f3f3f3f3"));
        TextView chemin = (TextView) view.findViewById(R.id.chemin);
        chemin.setText("Accueil>"+chemins);
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

}
