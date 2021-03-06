package com.royken.teknik.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.royken.teknik.R;
import com.royken.teknik.database.DatabaseHelper;
import com.royken.teknik.entities.Cahier;
import com.royken.teknik.entities.Organe;
import com.royken.teknik.entities.Periode;
import com.royken.teknik.entities.Utilisateur;

import java.sql.SQLException;
import java.util.List;

public class TimeChooseFragment extends Fragment {

    private static final String ARG_ORGANEID = "organeId";
    private static final String ARG_HORAIRE = "horaireId";
    private static final String ARG_CAHIERID = "cahierId";
    private DatabaseHelper databaseHelper = null;

    public static final String PREFS_NAME = "com.royken.teknik.MyPrefsFile";
    SharedPreferences settings ;

    private Button b1;
    private Button b2;
    private Button b3;
    private Button b4;
    int organeId ;
    String cahierId;
    int horaire;
    int id;

    TextView user;
    TextView title;

    Utilisateur u;
    Dao<Utilisateur, Integer> userDao;
    Dao<Organe, Integer> organeDao;

    private Dao<Periode, Integer> periodeDao;
    private List<Periode> periodes;
    LinearLayout linear;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM2 = "cahierNom";
    String nomCahier;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Organe o;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *  1.
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TimeChooseFragment newInstance(String param1, String cahier) {
        TimeChooseFragment fragment = new TimeChooseFragment();
        Bundle args = new Bundle();
        //args.putInt(ARG_CAHIERID,cahierId);
       // args.putInt(ARG_ORGANEID, param1);
        //args.putString(ARG_PARAM2, param2);
        //args.putInt(ARG_PARAM1, param3);
        args.putString(ARG_CAHIERID, param1);
        args.putString(ARG_PARAM2,cahier);
        fragment.setArguments(args);
        return fragment;
    }

    public TimeChooseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
                cahierId = getArguments().getString(ARG_CAHIERID);
                nomCahier = getArguments().getString(ARG_PARAM2);
                //mParam2 = getArguments().getString(ARG_PARAM2);
            //id = getArguments().getInt(ARG_PARAM1);
        }
    }

    @Override
    public void onViewCreated (View view, Bundle savedInstanceState) {
        title.setText(nomCahier + ">Fréquences");
        user.setText(u.getNom());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.activity_time_choose, container, false);
        AppBarLayout bar = (AppBarLayout)getActivity().findViewById(R.id.appbar);
        user = (TextView) bar.findViewById(R.id.user);
        title = (TextView) bar.findViewById(R.id.title);
        CoordinatorLayout coor = (CoordinatorLayout)getActivity().findViewById(R.id.coord);
        coor.setBackgroundResource(R.drawable.b);
        TextView chemin = (TextView) view.findViewById(R.id.chemin);
        linear = (LinearLayout)view.findViewById(R.id.timeLayout);


        try {
            userDao = getHelper().getUtilisateurDao();
            organeDao = getHelper().getOrganeDao();
            periodeDao = getHelper().getPeriodeDao();
            periodes = periodeDao.queryForAll();
            settings = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            int userId = settings.getInt("com.royken.userId", 0);
            u = userDao.queryForId(userId);
            //o = organeDao.queryForId(id);
            chemin.setText("Accueil>"+nomCahier);

            View.OnClickListener btnClicked = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Object tag = v.getTag();
                    Toast.makeText(getActivity(), periodes.get((int)tag).getNom(), Toast.LENGTH_SHORT).show();
                    Fragment fragment = OrganeFragment.newInstance(cahierId,periodes.get((int)tag).getIdServeur(),nomCahier+">"+ periodes.get((int)tag).getNom());
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.mainFrame,fragment);
                    ft.addToBackStack(null);
                    ft.commit();
                }
            };

            for (int i = 0; i < periodes.size() ; i++) {
                Button button = new Button(getActivity());
                button.setText(periodes.get(i).getNom());
                button.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
                button.setHeight(60);
                button.setTextAppearance(getActivity(),android.R.style.TextAppearance_Large);
                button.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.button_bg_rounded_corners));
                button.setPadding(12,12,12,12);
                button.setTextColor(Color.WHITE);
                button.setGravity(Gravity.CENTER_VERTICAL);

                button.setOnClickListener(btnClicked);
                button.setTag(i);///
                linear.addView(button);
                ViewGroup.MarginLayoutParams prms = (ViewGroup.MarginLayoutParams) button.getLayoutParams();
                prms.setMargins(0,15,0,0);
                button.setLayoutParams(prms);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
/*

        b1 = (Button)view.findViewById(R.id.button2);
        b2 = (Button)view.findViewById(R.id.button3);
        b3 = (Button)view.findViewById(R.id.button4);
        b4 = (Button)view.findViewById(R.id.button5);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = OrganeFragment.newInstance(cahierId,2,nomCahier+">Shift");
            //Fragment fragment =  ElementListFragment.newInstance(organeId,2,mParam2+">"+o.getNom()+">Shift");
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.mainFrame, fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"Aucune donnée",Toast.LENGTH_LONG).show();
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = OrganeFragment.newInstance(cahierId,0,nomCahier+">Horaire");
             // Fragment fragment =  ElementListFragment.newInstance(organeId,0,mParam2+">"+o.getNom()+">Horaire");
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.mainFrame, fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = OrganeFragment.newInstance(cahierId,3,nomCahier+">Matin");
           //Fragment fragment =  ElementListFragment.newInstance(organeId,3,mParam2+">"+o.getNom()+">Matin");
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.mainFrame, fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        */
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
}
