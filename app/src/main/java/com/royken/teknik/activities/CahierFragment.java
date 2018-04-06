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
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.royken.teknik.R;
import com.royken.teknik.database.DatabaseHelper;
import com.royken.teknik.entities.Cahier;
import com.royken.teknik.entities.Organe;
import com.royken.teknik.entities.Reponse;
import com.royken.teknik.entities.Utilisateur;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CahierFragment extends Fragment {


    private static final String ARG_ZONEID = "zoneId";
    private static final String ARG_CAHIERID = "cahierId";
    private DatabaseHelper databaseHelper = null;
    public static final String PREFS_NAME = "com.royken.teknik.MyPrefsFile";
    SharedPreferences settings ;
    Dao<Utilisateur, Integer> userDao;

    private Button tratEau;
    private Button electricite;
    private Button mecanique;
    //private Button eau;
    private Button usineAglace;
    //private Button compresseur;
    Utilisateur u;
    private Dao<Cahier, Integer> cahierDao;
    private List<Cahier> cahiers;
    LinearLayout linear;
    List<Button> buttons;
    private boolean isExporting = false;

    private OnFragmentInteractionListener mListener;
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView user;
    TextView title;

    public static CahierFragment newInstance() {
        CahierFragment fragment = new CahierFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public CahierFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public void onViewCreated (View view, Bundle savedInstanceState) {
        title.setText("Choix Service");
        user.setText(u.getNom());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.activity_cahier, container, false);
        AppBarLayout bar = (AppBarLayout)getActivity().findViewById(R.id.appbar);
        user = (TextView) bar.findViewById(R.id.user);
        title = (TextView) bar.findViewById(R.id.title);
      //  tratEau = (Button)view.findViewById(R.id.button2);
      //  electricite = (Button)view.findViewById(R.id.button3);
      //  mecanique = (Button)view.findViewById(R.id.button5);
        TextView chemin = (TextView)view.findViewById(R.id.chemin);
        chemin.setText("Accueil>");
        //eau = (Button)findViewById(R.id.button8);
       // usineAglace = (Button)view.findViewById(R.id.button4);
        CoordinatorLayout coor = (CoordinatorLayout)getActivity().findViewById(R.id.coord);
        coor.setBackgroundResource(R.drawable.b);
        linear = (LinearLayout)view.findViewById(R.id.cahierLayout);
        //compresseur = (Button)findViewById(R.id.button10);

        try {
            userDao = getHelper().getUtilisateurDao();
            cahierDao = getHelper().getCahierDao();
            cahiers = cahierDao.queryForAll();
            settings = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            int userId = settings.getInt("com.royken.userId", 0);
            u = userDao.queryForId(userId);
           // buttons = new ArrayList<Button>();

            View.OnClickListener btnClicked = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Object tag = v.getTag();
                    Toast.makeText(getActivity(), cahiers.get((int)tag).getNom(), Toast.LENGTH_SHORT).show();
                    Fragment fragment = TimeChooseFragment.newInstance(cahiers.get((int)tag).getCode(),cahiers.get((int)tag).getNom());
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.mainFrame,fragment);
                    ft.addToBackStack(null);
                    ft.commit();
                }
            };

            for (int i = 0; i < cahiers.size() ; i++) {
                Button button = new Button(getActivity());
                button.setText(cahiers.get(i).getNom());
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


/*

            tratEau.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // if(u.getRole().equalsIgnoreCase("admin") || u.getCahier().equalsIgnoreCase("OTE")){
                   // Intent intent = new Intent(getActivity(), TimeChooseActivity.class);
                   // intent.putExtra(ARG_CAHIERID, 0);
                   // startActivity(intent);
                   // Fragment fragment = OrganeFragment.newInstance(0);
                    Fragment fragment = TimeChooseFragment.newInstance(0,"EAU");
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.mainFrame,fragment);
                    ft.addToBackStack(null);
                    ft.commit();

                }
            });

            electricite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //  if (u.getRole().equalsIgnoreCase("admin") || u.getCahier().equalsIgnoreCase("GPE")) {
                  //  Intent intent = new Intent(getActivity(), TimeChooseActivity.class);
                  //  intent.putExtra(ARG_CAHIERID, 1);
                  //  startActivity(intent);
                    Fragment fragment = TimeChooseFragment.newInstance(1,"ELECTRICITE");
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                     ft.replace(R.id.mainFrame,fragment);
                    ft.addToBackStack(null);
                    ft.commit();


                }
            });

            mecanique.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  //  Intent intent = new Intent(getActivity(), TimeChooseActivity.class);
                  //  intent.putExtra(ARG_CAHIERID, 3);
                  //  startActivity(intent);

                    Fragment fragment = TimeChooseFragment.newInstance(3,"MECANIQUE");
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                     ft.replace(R.id.mainFrame,fragment);
                    ft.addToBackStack(null);
                    ft.commit();

                }
            });

            usineAglace.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //  if(u.getRole().equalsIgnoreCase("admin") || u.getCahier().equalsIgnoreCase("UAG")){
                   // Intent intent = new Intent(getActivity(), TimeChooseActivity.class);
                   // intent.putExtra(ARG_CAHIERID, 2);
                   // startActivity(intent);

                    Fragment fragment = TimeChooseFragment.newInstance(2,"GLACE");
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                     ft.replace(R.id.mainFrame,fragment);
                    ft.addToBackStack(null);
                    ft.commit();


                }
            });
*/

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return view;
    }

    private DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            //databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
            databaseHelper = new DatabaseHelper(getActivity());
        }
        return databaseHelper;
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
}
