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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.royken.teknik.R;
import com.royken.teknik.database.DatabaseHelper;
import com.royken.teknik.entities.Utilisateur;

import java.sql.SQLException;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ManageFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ManageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ManageFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private DatabaseHelper databaseHelper = null;
    public static final String PREFS_NAME = "com.royken.teknik.MyPrefsFile";
    SharedPreferences settings ;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private Button enregBtn;
    private EditText urlTxt;
    private CheckBox indexChb;
    private CheckBox fichier;

    TextView user;
    TextView title;
    Dao<Utilisateur, Integer> userDao;
    Utilisateur u;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *
     *
     * @return A new instance of fragment ManageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ManageFragment newInstance() {
        ManageFragment fragment = new ManageFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public ManageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated (View view, Bundle savedInstanceState) {
        title.setText("Paramètres");
        user.setText(u.getNom());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_manage, container, false);
        enregBtn = (Button)view.findViewById(R.id.btn_parametre);
        urlTxt = (EditText) view.findViewById(R.id.txtUrl);
        indexChb = (CheckBox)view.findViewById(R.id.index);
        fichier = (CheckBox)view.findViewById(R.id.fichier);
        settings = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        boolean file= settings.getBoolean("com.royken.file", false);
        boolean index0= settings.getBoolean("com.royken.index",false);
        urlTxt.setText(settings.getString("com.royken.url",""));
        indexChb.setChecked(index0);
        fichier.setChecked(file);
        AppBarLayout bar = (AppBarLayout)getActivity().findViewById(R.id.appbar);
        user = (TextView) bar.findViewById(R.id.user);
        title = (TextView) bar.findViewById(R.id.title);
        CoordinatorLayout coor = (CoordinatorLayout)getActivity().findViewById(R.id.coord);
        coor.setBackgroundColor(Color.parseColor("#f3f3f3f3"));

        int userId = settings.getInt("com.royken.userId", 0);
        try {
            userDao = getHelper().getUtilisateurDao();
            u = userDao.queryForId(userId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        enregBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(urlTxt.getText().length() < 3){
                    Toast.makeText(getActivity(),"L'URL est vide",Toast.LENGTH_LONG).show();
                }
                else{
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("com.royken.url", urlTxt.getText().toString().trim());
                    editor.commit();
                    editor.putBoolean("com.royken.file", fichier.isChecked());
                    editor.commit();
                    editor.putBoolean("com.royken.index", indexChb.isChecked());
                    editor.commit();
                    Toast.makeText(getActivity(),"Valeurs enregistrées",Toast.LENGTH_LONG).show();
                    android.support.v4.app.Fragment fragment = CahierFragment.newInstance();
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.mainFrame,fragment);
                    ft.addToBackStack(null);
                    ft.commit();
                }
            }
        });
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

    private DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            //databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
            databaseHelper = new DatabaseHelper(getActivity());
        }
        return databaseHelper;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
