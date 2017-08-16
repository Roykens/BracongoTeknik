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
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.royken.teknik.R;
import com.royken.teknik.adapter.ReponseAdapter;
import com.royken.teknik.database.DatabaseHelper;
import com.royken.teknik.entities.Bloc;
import com.royken.teknik.entities.CahierDummy;
import com.royken.teknik.entities.Element;
import com.royken.teknik.entities.Organe;
import com.royken.teknik.entities.Reponse;
import com.royken.teknik.entities.SousOrgane;
import com.royken.teknik.entities.Utilisateur;
import com.royken.teknik.entities.Zone;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ElementFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ElementFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ElementFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_ELEMENTID = "elementId";
    public static final String PREFS_NAME = "com.royken.teknik.MyPrefsFile";
    SharedPreferences settings ;

    private DatabaseHelper databaseHelper = null;
    Dao<Element, Integer> elementDao;
    Dao<Utilisateur, Integer> userDao;
    Dao<Reponse,Integer> reponseDao;
    private Dao<SousOrgane,Integer> sousODao;
    private Dao<Organe,Integer> organeDao;
    private Dao<Bloc,Integer>blocDao;
    private Dao<Zone,Integer>zoneDao;

    private List<Reponse> reponses;
    Utilisateur u;
    Element e;
    EditText valeurTxt;
    Button enreg;
    Button photo;
    TextView minTvw;
    TextView maxTvw;
    TextView uniteTvw;
    ListView list;

    private Button pic;
    private String cahier;
    private String organe;
    private String sousOrgane;
    SousOrgane so = null;
    Organe o = null;
    Bloc b = null;
    Zone z = null;
    TextView user;
    TextView title;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private int elementId;

    private OnFragmentInteractionListener mListener;

    private ReponseAdapter reponseAdapter;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param elementId Parameter 1.
     * @return A new instance of fragment ElementFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ElementFragment newInstance(int elementId) {
        ElementFragment fragment = new ElementFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ELEMENTID, elementId);
        fragment.setArguments(args);
        return fragment;
    }

    public ElementFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated (View view, Bundle savedInstanceState) {

        try {
            sousODao = getHelper().getSousOrganeDao();
            organeDao = getHelper().getOrganeDao();
            blocDao = getHelper().getBlocDao();
            zoneDao = getHelper().getZoneDao();
            so = sousODao.queryBuilder().where().eq("idServeur", e.getSousOrganeId()).queryForFirst();
            o = organeDao.queryBuilder().where().eq("idServeur", so.getIdOrgane()).queryForFirst();
            b = blocDao.queryBuilder().where().eq("idServeur", o.getIdBloc()).queryForFirst();
            z = zoneDao.queryBuilder().where().eq("idServeur", b.getIdZone()).queryForFirst();
            reponseDao = getHelper().getReponseDao();
            long offset = settings.getLong("com.royken.offset", 0);
            reponseDao = getHelper().getReponseDao();
            //long nombre = reponseDao.countOf();
            if (offset == 0) {
                reponses =  reponseDao.queryBuilder().where().eq("idElement", e.getId()).query();
            } else {
                reponses = reponseDao.queryBuilder().limit(24L).where().eq("idElement", e.getId()).query();
            }
            Collections.reverse(reponses);
            reponseAdapter = new ReponseAdapter(getActivity(), reponses);

            list.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
            list.setAdapter(reponseAdapter);

        } catch (SQLException e1) {
            e1.printStackTrace();
        }

       title.setText(o.getNom() + " : " + e.getNom());
        user.setText(u.getNom());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            elementId = getArguments().getInt(ARG_ELEMENTID);
        }
        try {
            userDao = getHelper().getUtilisateurDao();
            settings = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            int userId = settings.getInt("com.royken.userId", 0);
            u = userDao.queryForId(userId);
            elementDao =  getHelper().getElementDao();
            e = elementDao.queryForId(elementId);


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_element, container, false);
        valeurTxt = (EditText) view.findViewById(R.id.valeurTxt);
        enreg = (Button) view.findViewById(R.id.buttonEnreg);
        photo = (Button)view.findViewById(R.id.buttonPhoto);
        minTvw = (TextView) view.findViewById(R.id.textMin);
        maxTvw = (TextView) view.findViewById(R.id.textMax);
        uniteTvw = (TextView) view.findViewById(R.id.unite);
        list = (ListView) view.findViewById(R.id.listReponse1);
        AppBarLayout bar = (AppBarLayout)getActivity().findViewById(R.id.appbar);
        user = (TextView) bar.findViewById(R.id.user);
        title = (TextView) bar.findViewById(R.id.title);
        CoordinatorLayout coor = (CoordinatorLayout)getActivity().findViewById(R.id.coord);
        coor.setBackgroundColor(Color.parseColor("#f3f3f3f3"));


        uniteTvw.setText("( "+e.getUnite()+" )");
        LinearLayout borne = (LinearLayout) view.findViewById(R.id.bornes);
        if(e.isHasBorn()){
            if(e.isCriteriaAlpha()){
                minTvw.setVisibility(View.INVISIBLE);
                TextView minLabel = (TextView)view.findViewById(R.id.textM);
                minLabel.setVisibility(View.INVISIBLE);
                maxTvw.setText("N - 1 + " + e.getValMax());
            }
            else {
              //  maxTvw.setText(e.getValMax());
               // minTvw.setText(e.getValMin());

            }
        }
        else {
            borne.setVisibility(View.INVISIBLE);
        }
        if(e.getValeurType().equalsIgnoreCase("int") || e.getValeurType().equalsIgnoreCase("double")){
            valeurTxt.setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        }

        enreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (valeurTxt.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Valeur manquante", Toast.LENGTH_SHORT).show();
                } else {
                    enregistrerValeur();
                }

            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (reponses != null) {
            reponseAdapter = new ReponseAdapter(getActivity(),reponses);

            list.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
            list.setAdapter(reponseAdapter);
           // ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("La liste des Boissons");


        }
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    private void enregistrerValeur(){

        try {
            reponseDao = getHelper().getReponseDao();
            cahier = CahierDummy.getCahierByCode(z.getCahierCode());
            organe = o.getNom();
            sousOrgane = so.getNom();
            Reponse r = reponseDao.queryBuilder()
                    .orderBy("id", false).where().eq("idElement", e.getId()).queryForFirst();


            if (e.isHasBorn()) {
                if (!isNumeric(valeurTxt.getText().toString().trim())) {
                    Reponse re = new Reponse();
                    re.setNom(e.getNom());
                    re.setCode(e.getCode());

                    re.setCompteur(1);
                    re.setDate(new Date());
                    re.setUser(u.getNom());
                    re.setIdElement(e.getIdServeur());
                    re.setValeurCorrecte(false);
                    re.setCahier(cahier);
                    re.setOrgane(organe);
                    re.setIdUser(u.getIdServeur());
                    re.setSousOrgane(sousOrgane);
                    re.setValeur(valeurTxt.getText().toString().trim());
                    reponseDao.create(re);
                    //holder.tv_Guide.setTextColor(Color.GREEN);
                   // holder.tv_Guide.setText("Enregistré");
                    Toast.makeText(getActivity(),"Enregistré : Valeur incorrecte",Toast.LENGTH_SHORT).show();
                    valeurTxt.setText("");
                    updateUI();
                }

                else {

                    Double valeur = Double.parseDouble(valeurTxt.getText().toString().trim());

                    if (r != null) {

                        if (!isNumeric(r.getValeur())) {
                            Reponse re = new Reponse();
                            re.setNom(e.getNom());
                            re.setCode(e.getCode());
                            re.setCompteur(r.getCompteur() + 1);
                            re.setDate(new Date());
                            re.setUser(u.getNom());
                            re.setIdElement(e.getIdServeur());
                            re.setValeurCorrecte(false);
                            re.setCahier(cahier);
                            re.setOrgane(organe);
                            re.setIdUser(u.getIdServeur());
                            re.setSousOrgane(sousOrgane);
                            re.setValeur(valeurTxt.getText().toString().trim());
                            reponseDao.create(re);
                           // holder.tv_Guide.setTextColor(Color.GREEN);
                           // holder.tv_Guide.setText("Enregistré");
                            Toast.makeText(getActivity(),"Enregistré : : Valeur incorrecte",Toast.LENGTH_SHORT).show();
                            valeurTxt.setText("");
                            updateUI();
                        }
                        ///////////////////////////////////////
                        else {

                            if (e.isCriteriaAlpha()) {
                                if (Double.parseDouble(r.getValeur()) > valeur || (Double.parseDouble(r.getValeur()) + e.getValMax()) < valeur) {
                                    Reponse re = new Reponse();
                                    re.setNom(e.getNom());
                                    re.setCode(e.getCode());
                                    re.setCompteur(r.getCompteur() + 1);
                                    re.setDate(new Date());
                                    re.setUser(u.getNom());
                                    re.setIdUser(u.getIdServeur());
                                    re.setIdElement(e.getIdServeur());
                                    re.setValeurCorrecte(false);
                                    //      Log.i("CAHIEEEEEEERRRRRRRRR", cahier);
                                    re.setCahier(cahier);
                                    re.setOrgane(organe);
                                    re.setSousOrgane(sousOrgane);
                                    re.setValeur(valeur + "");
                                    reponseDao.create(re);
                                    //holder.tv_Guide.setTextColor(Color.GREEN);
                                    //holder.tv_Guide.setText("Enregistré");
                                    Toast.makeText(getActivity(),"Enregistré : Valeur incorrecte",Toast.LENGTH_SHORT).show();
                                    valeurTxt.setText("");
                                    updateUI();
                                } else {
                                    Reponse re = new Reponse();
                                    re.setNom(e.getNom());
                                    re.setCode(e.getCode());
                                    re.setCompteur(r.getCompteur() + 1);
                                    re.setDate(new Date());
                                    re.setUser(u.getNom());
                                    //  Log.i("CAHIEEEEEEERRRRRRRRR", cahier);
                                    re.setCahier(cahier);
                                    re.setIdUser(u.getIdServeur());
                                    re.setOrgane(organe);
                                    re.setSousOrgane(sousOrgane);
                                    re.setValeurCorrecte(true);
                                    re.setIdElement(e.getIdServeur());
                                    re.setValeur(valeur + "");
                                    reponseDao.create(re);
                                    //holder.tv_Guide.setTextColor(Color.GREEN);
                                    //holder.tv_Guide.setText("Enregistré");
                                     Toast.makeText(getActivity(),"Enregistré",Toast.LENGTH_SHORT).show();
                                    valeurTxt.setText("");
                                    updateUI();
                                }
                            } else {

                                if (e.getValMin() > valeur || e.getValMax() < valeur) {
                                    Reponse re = new Reponse();
                                    re.setNom(e.getNom());
                                    re.setCode(e.getCode());
                                    re.setCompteur(r.getCompteur() + 1);
                                    re.setDate(new Date());
                                    re.setUser(u.getNom());
                                    re.setIdUser(u.getIdServeur());
                                    re.setIdElement(e.getIdServeur());
                                    re.setValeurCorrecte(false);
                                    //  Log.i("CAHIEEEEEEERRRRRRRRR", cahier);
                                    re.setCahier(cahier);
                                    re.setOrgane(organe);
                                    re.setSousOrgane(sousOrgane);
                                    re.setValeur(valeur + "");
                                    reponseDao.create(re);
                                    //holder.tv_Guide.setTextColor(Color.GREEN);
                                    //holder.tv_Guide.setText("Enregistré");
                                    Toast.makeText(getActivity(),"Enregistré : Valeur incorrecte",Toast.LENGTH_SHORT).show();
                                    valeurTxt.setText("");
                                    updateUI();
                                } else {
                                    Reponse re = new Reponse();
                                    re.setNom(e.getNom());
                                    re.setCode(e.getCode());
                                    re.setCompteur(r.getCompteur() + 1);
                                    re.setDate(new Date());
                                    re.setUser(u.getNom());
                                    re.setIdElement(e.getIdServeur());
                                    re.setIdUser(u.getIdServeur());
                                    re.setValeurCorrecte(true);
                                    // Log.i("CAHIEEEEEEERRRRRRRRR", cahier);
                                    re.setCahier(cahier);
                                    re.setOrgane(organe);
                                    re.setSousOrgane(sousOrgane);
                                    re.setValeur(valeur + "");
                                    reponseDao.create(re);
                                    //holder.tv_Guide.setTextColor(Color.GREEN);
                                    //holder.tv_Guide.setText("Enregistré");
                                    Toast.makeText(getActivity(),"Enregistré",Toast.LENGTH_SHORT).show();
                                    valeurTxt.setText("");
                                    updateUI();
                                }
                            }

                        }
                    } else {
                        //Toast.makeText(mContext,"Pas de valeur",Toast.LENGTH_LONG).show();
                        if (!e.isCriteriaAlpha()) {
                            if (e.getValMin() > valeur || e.getValMax() < valeur) {
                                Reponse re = new Reponse();
                                re.setNom(e.getNom());
                                re.setCode(e.getCode());
                                re.setCompteur(1);
                                re.setDate(new Date());
                                re.setUser(u.getNom());
                                re.setIdElement(e.getIdServeur());
                                re.setIdUser(u.getIdServeur());
                                re.setValeurCorrecte(false);
                                //Log.i("CAHIEEEEEEERRRRRRRRR", cahier);
                                re.setCahier(cahier);
                                re.setOrgane(organe);
                                re.setSousOrgane(sousOrgane);
                                re.setValeur(valeur + "");
                                reponseDao.create(re);
                                //holder.tv_Guide.setTextColor(Color.GREEN);
                                //holder.tv_Guide.setText("Enregistré");
                                Toast.makeText(getActivity(),"Enregistré : Valeur incorrecte",Toast.LENGTH_SHORT).show();
                                valeurTxt.setText("");
                                updateUI();
                            } else {
                                Reponse re = new Reponse();
                                re.setNom(e.getNom());
                                re.setCode(e.getCode());
                                re.setCompteur(1);
                                re.setDate(new Date());
                                re.setIdUser(u.getIdServeur());
                                re.setUser(u.getNom());
                                re.setValeurCorrecte(true);
                                //Log.i("CAHIEEEEEEERRRRRRRRR", cahier);
                                re.setCahier(cahier);
                                re.setOrgane(organe);
                                re.setSousOrgane(sousOrgane);
                                re.setIdElement(e.getIdServeur());
                                re.setValeur(valeur + "");
                                reponseDao.create(re);
                                Toast.makeText(getActivity(),"Enregistré ",Toast.LENGTH_SHORT).show();
                                valeurTxt.setText("");
                                updateUI();
                            }

                        } else {
                            Reponse re = new Reponse();
                            re.setNom(e.getNom());
                            re.setCode(e.getCode());
                            re.setCompteur(1);
                            re.setDate(new Date());
                            re.setUser(u.getNom());
                            re.setValeurCorrecte(true);
                            re.setIdElement(e.getIdServeur());
                            //Log.i("CAHIEEEEEEERRRRRRRRR", cahier);
                            re.setCahier(cahier);
                            re.setOrgane(organe);
                            re.setIdUser(u.getIdServeur());
                            re.setSousOrgane(sousOrgane);
                            re.setValeur(valeur + "");
                            Toast.makeText(getActivity(),"Enregistré ",Toast.LENGTH_SHORT).show();
                            reponseDao.create(re);
                            valeurTxt.setText("");
                            updateUI();
                        }
                    }
                }
            } else {
                Reponse re = new Reponse();
                re.setNom(e.getNom());
                re.setCode(e.getCode());
                re.setCompteur(1);
                re.setDate(new Date());
                re.setUser(u.getNom());
                re.setValeurCorrecte(true);
                //Log.i("CAHIEEEEEEERRRRRRRRR", cahier);
                re.setCahier(cahier);
                re.setOrgane(organe);
                re.setIdUser(u.getIdServeur());
                re.setSousOrgane(sousOrgane);
                re.setIdElement(e.getIdServeur());
                re.setValeur(valeurTxt.getText().toString().trim());
                reponseDao.create(re);
                Toast.makeText(getActivity(),"Enregistré ",Toast.LENGTH_SHORT).show();
                valeurTxt.setText("");
                updateUI();

            }

        } catch (SQLException e1) {
            e1.printStackTrace();
        }


    }

    private boolean isNumeric(String str){
        return str.trim().matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
    }

    private DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            //databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
            databaseHelper = new DatabaseHelper(getActivity());
        }
        return databaseHelper;
    }

    private void updateUI() {
        try {
            long offset = settings.getLong("com.royken.offset", 0);
            reponseDao = getHelper().getReponseDao();
            //long nombre = reponseDao.countOf();
            if (offset == 0) {
                //reponses = reponseDao.queryForAll();
                reponses =  reponseDao.queryBuilder().where().eq("idElement", e.getId()).query();
            } else {
                //reponses = reponseDao.queryBuilder().offset(offset).limit(nombre - offset).query();
                reponses = reponseDao.queryBuilder().limit(24L).where().eq("idElement", e.getId()).query();
            }

           // SharedPreferences.Editor editor = settings.edit();
           // editor.putLong("com.royken.offset", nombre);
           // editor.commit();
            Collections.reverse(reponses);
            reponseAdapter = new ReponseAdapter(getActivity(), reponses);

            list.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
            list.setAdapter(reponseAdapter);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
