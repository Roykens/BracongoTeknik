package com.royken.teknik.adapter;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.royken.teknik.R;
import com.royken.teknik.database.DatabaseHelper;
import com.royken.teknik.entities.Bloc;
import com.royken.teknik.entities.CahierDummy;
import com.royken.teknik.entities.Element;
import com.royken.teknik.entities.Organe;
import com.royken.teknik.entities.Reponse;
import com.royken.teknik.entities.SousOrgane;
import com.royken.teknik.entities.Utilisateur;
import com.royken.teknik.entities.Zone;

import java.io.File;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by royken on 03/01/17.
 */
public class ElementAdapter extends BaseAdapter {
    private DatabaseHelper databaseHelper = null;
    private Context mContext;
    private LayoutInflater mInflater;
    private List<Element> elements;
    private  Dao<Utilisateur, Integer> userDao;
    private Dao<Reponse,Integer> reponseDao;
    private Dao<Element,Integer> elementDao;
    private Dao<SousOrgane,Integer> sousODao;
    private Dao<Organe,Integer> organeDao;
    private Dao<Bloc,Integer>blocDao;
    private Dao<Zone,Integer>zoneDao;
    SharedPreferences settings ;
    private CharSequence mTitle;
    final int TAKE_PICTURE = 1;
    private Uri imageUri;
    private Button pic;
    private String cahier;
    SousOrgane so = null;
    Organe o = null;
    Bloc b = null;
    Zone z = null;

    public static final String PREFS_NAME = "com.royken.teknik.MyPrefsFile";

    public ElementAdapter(Context mContext, List<Element> elements) {
        this.mContext = mContext;
        this.elements = elements;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return elements.size();
    }

    @Override
    public Element getItem(int position) {
        return elements.get(position);
    }

    @Override
    public long getItemId(int position) {
        return elements.get(position).getId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder ;
        Element e1 = elements.get(position);
        Reponse r = null;
        View v = convertView;

        //final String cahier ;


        if (v == null || v.getTag() == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.card_element_item,parent,false);
            holder.tv_Nom = (TextView)convertView.findViewById(R.id.nom);
            holder.txt_Valeur = (EditText)convertView.findViewById(R.id.editText2);
            holder.tv_Guide = (TextView)convertView.findViewById(R.id.textView5);
            holder.tv_guide = (TextView)convertView.findViewById(R.id.textView8);
            holder.tv_ValeurP = (TextView)convertView.findViewById(R.id.textView7);
            holder.photo = (Button)convertView.findViewById(R.id.btnPhoto);
            holder.btn_Enreg = (Button)convertView.findViewById(R.id.btnEnreg);
            convertView.setTag(holder);
           // convertView.setTag(R.id.nom, holder.tv_Nom);
           // convertView.setTag(R.id.editText2, holder.txt_Valeur);
           // convertView.setTag(R.id.textView5, holder.tv_Guide);
           // convertView.setTag(R.id.btnEnreg, holder.btn_Enreg);
           // convertView.setTag(R.id.textView7, holder.tv_ValeurP);
            //convertView.setTag(R.id.btnPhoto,holder.photo);
            //convertView.setTag(R.id.textView8,holder.tv_guide);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        try {
            reponseDao = getHelper().getReponseDao();
            sousODao = getHelper().getSousOrganeDao();
            organeDao = getHelper().getOrganeDao();
            blocDao = getHelper().getBlocDao();
            zoneDao = getHelper().getZoneDao();
            r = reponseDao.queryBuilder()
                    .orderBy("id", false).where().eq("idElement", e1.getId()).queryForFirst();
            so = sousODao.queryBuilder().where().eq("idServeur", elements.get(position).getSousOrganeId()).queryForFirst();
            o = organeDao.queryBuilder().where().eq("idServeur", so.getIdOrgane()).queryForFirst();
            // b= blocDao.queryBuilder().where().eq("idServeur",o.getIdBloc()).queryForFirst();
            //z = zoneDao.queryBuilder().where().eq("idServeur",b.getIdZone()).queryForFirst();
            //cahier = CahierDummy.getCahierByCode(z.getCahierCode());
            //Log.i("CAHIEEEEEEERRRRRRRRR",cahier);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(elements.get(position).getValeurType().equalsIgnoreCase("int") || elements.get(position).getValeurType().equalsIgnoreCase("double")){
            holder.txt_Valeur.setRawInputType(InputType.TYPE_CLASS_NUMBER |InputType.TYPE_NUMBER_FLAG_DECIMAL);
        }
        else {
            holder.txt_Valeur.setRawInputType(InputType.TYPE_CLASS_TEXT);
        }
        holder.photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto(elements.get(position).getNom());
            }
        });
        holder.btn_Enreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.txt_Valeur.getText().length() > 0) {

                    try {
                        so = sousODao.queryBuilder().where().eq("idServeur", elements.get(position).getSousOrganeId()).queryForFirst();
                        o = organeDao.queryBuilder().where().eq("idServeur", so.getIdOrgane()).queryForFirst();
                        b = blocDao.queryBuilder().where().eq("idServeur", o.getIdBloc()).queryForFirst();
                        z = zoneDao.queryBuilder().where().eq("idServeur", b.getIdZone()).queryForFirst();
                        cahier = CahierDummy.getCahierByCode(z.getCahierCode());

                        reponseDao = getHelper().getReponseDao();
                        userDao = getHelper().getUtilisateurDao();
                        settings = mContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                        int userId = settings.getInt("com.royken.userId", 0);
                        String log = settings.getString("com.royken.login", "");
                        Utilisateur u = userDao.queryForId(userId);
                        Element e = elements.get(position);
                        Reponse r = reponseDao.queryBuilder()
                                .orderBy("id", false).where().eq("idElement", e.getId()).queryForFirst();

                        if (e.isHasBorn()) {
                            Double valeur = Double.parseDouble(holder.txt_Valeur.getText().toString().trim());

                            if (r != null) {
                                //Log.i("LA REPONSE", r.toString());
                              //  Log.i("L'ELEMENTTTTT ", e.toString());
                                if (e.isCriteriaAlpha()) {
                                    if (Double.parseDouble(r.getValeur()) > valeur || (Double.parseDouble(r.getValeur()) + e.getValMax()) < valeur) {
                                        Reponse re = new Reponse();
                                        re.setNom(e.getNom());
                                        re.setCode(e.getCode());
                                        re.setCompteur(r.getCompteur() + 1);
                                        re.setDate(new Date());
                                        re.setUser(u.getNom());
                                        re.setIdElement(e.getId());
                                        re.setValeurCorrecte(false);
                                  //      Log.i("CAHIEEEEEEERRRRRRRRR", cahier);
                                        re.setCahier(cahier);
                                        re.setValeur(valeur + "");
                                        reponseDao.create(re);
                                        holder.tv_Guide.setTextColor(Color.GREEN);
                                        holder.tv_Guide.setText("Enregistré");
                                        //Toast.makeText(mContext,"Enregistré",Toast.LENGTH_SHORT).show();
                                        holder.txt_Valeur.setText("");
                                        //holder.tv_Guide.setTextColor(Color.RED);
                                        //holder.tv_Guide.setText("Valeur Hors Bornes");
                                        //Toast.makeText(mContext,"Valeur Hors Bornes",Toast.LENGTH_LONG).show();
                                    } else {
                                        Reponse re = new Reponse();
                                        re.setNom(e.getNom());
                                        re.setCode(e.getCode());
                                        re.setCompteur(r.getCompteur() + 1);
                                        re.setDate(new Date());
                                        re.setUser(u.getNom());
                                      //  Log.i("CAHIEEEEEEERRRRRRRRR", cahier);
                                        re.setCahier(cahier);
                                        re.setValeurCorrecte(true);
                                        re.setIdElement(e.getId());
                                        re.setValeur(valeur + "");
                                        reponseDao.create(re);
                                        holder.tv_Guide.setTextColor(Color.GREEN);
                                        holder.tv_Guide.setText("Enregistré");
                                        // Toast.makeText(mContext,"Enregistré",Toast.LENGTH_SHORT).show();
                                        holder.txt_Valeur.setText("");
                                    }
                                } else {

                                    if (e.getValMin() > valeur || e.getValMax() < valeur) {
                                        Reponse re = new Reponse();
                                        re.setNom(e.getNom());
                                        re.setCode(e.getCode());
                                        re.setCompteur(r.getCompteur() + 1);
                                        re.setDate(new Date());
                                        re.setUser(u.getNom());
                                        re.setIdElement(e.getId());
                                        re.setValeurCorrecte(false);
                                      //  Log.i("CAHIEEEEEEERRRRRRRRR", cahier);
                                        re.setCahier(cahier);
                                        re.setValeur(valeur + "");
                                        reponseDao.create(re);
                                        holder.tv_Guide.setTextColor(Color.GREEN);
                                        holder.tv_Guide.setText("Enregistré");
                                        //Toast.makeText(mContext,"Enregistré",Toast.LENGTH_SHORT).show();
                                        holder.txt_Valeur.setText("");
                                       // holder.tv_Guide.setTextColor(Color.RED);
                                       // holder.tv_Guide.setText("Valeur Inférieure à : " + e.getValMin() + " ou Supérieure à : " + e.getValMax());
                                        //Toast.makeText(mContext, "Valeur Inférieure à : " + e.getValMin() + " ou Supérieure à : " + e.getValMax(), Toast.LENGTH_LONG).show();
                                    } else {
                                        Reponse re = new Reponse();
                                        re.setNom(e.getNom());
                                        re.setCode(e.getCode());
                                        re.setCompteur(r.getCompteur() + 1);
                                        re.setDate(new Date());
                                        re.setUser(u.getNom());
                                        re.setIdElement(e.getId());
                                        re.setValeurCorrecte(true);
                                       // Log.i("CAHIEEEEEEERRRRRRRRR", cahier);
                                        re.setCahier(cahier);
                                        re.setValeur(valeur + "");
                                        reponseDao.create(re);
                                        holder.tv_Guide.setTextColor(Color.GREEN);
                                        holder.tv_Guide.setText("Enregistré");
                                        //Toast.makeText(mContext,"Enregistré",Toast.LENGTH_SHORT).show();
                                        holder.txt_Valeur.setText("");
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
                                        re.setIdElement(e.getId());
                                        re.setValeurCorrecte(false);
                                        //Log.i("CAHIEEEEEEERRRRRRRRR", cahier);
                                        re.setCahier(cahier);
                                        re.setValeur(valeur + "");
                                        reponseDao.create(re);
                                        holder.tv_Guide.setTextColor(Color.GREEN);
                                        holder.tv_Guide.setText("Enregistré");
                                        //Toast.makeText(mContext,"Enregistré",Toast.LENGTH_SHORT).show();
                                        holder.txt_Valeur.setText("");
                                        //holder.tv_Guide.setTextColor(Color.RED);
                                        //holder.tv_Guide.setText("Valeur Inférieure à : " + e.getValMin() + " ou Supérieure à : " + e.getValMax());
                                        // Toast.makeText(mContext, "Valeur Inférieure à : " + e.getValMin() + " ou Supérieure à : " + e.getValMax(), Toast.LENGTH_LONG).show();
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
                                        re.setIdElement(e.getId());
                                        re.setValeur(valeur + "");
                                        reponseDao.create(re);
                                        holder.tv_Guide.setTextColor(Color.GREEN);
                                        if (r == null) {
                                            holder.tv_ValeurP.setText(holder.txt_Valeur.getText().toString().trim());
                                        }
                                        holder.tv_Guide.setText("Enregistré");
                                        //Toast.makeText(mContext,"Enregistré",Toast.LENGTH_SHORT).show();
                                        holder.txt_Valeur.setText("");
                                    }

                                } else {
                                    Reponse re = new Reponse();
                                    re.setNom(e.getNom());
                                    re.setCode(e.getCode());
                                    re.setCompteur(1);
                                    re.setDate(new Date());
                                    re.setUser(u.getNom());
                                    re.setValeurCorrecte(true);
                                    re.setIdElement(e.getId());
                                    //Log.i("CAHIEEEEEEERRRRRRRRR", cahier);
                                    re.setCahier(cahier);
                                    re.setValeur(valeur + "");
                                    reponseDao.create(re);
                                    holder.tv_Guide.setTextColor(Color.GREEN);
                                    if (r == null) {
                                        holder.tv_ValeurP.setText(holder.txt_Valeur.getText().toString().trim());
                                    }
                                    holder.tv_Guide.setText("Enregistré");
                                    //Toast.makeText(mContext,"Enregistré",Toast.LENGTH_SHORT).show();
                                    holder.txt_Valeur.setText("");
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
                            re.setIdElement(e.getId());
                            re.setValeur(holder.txt_Valeur.getText().toString().trim());
                            reponseDao.create(re);
                            holder.tv_Guide.setTextColor(Color.GREEN);
                            holder.tv_Guide.setText("Enregistré");
                            if (r == null) {
                                holder.tv_ValeurP.setText(holder.txt_Valeur.getText().toString().trim());
                            }
                            //Toast.makeText(mContext,"Enregistré",Toast.LENGTH_SHORT).show();
                            holder.txt_Valeur.setText("");

                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(mContext, "Valeur manquante", Toast.LENGTH_SHORT).show();
                }
                // Toast.makeText(mContext,holder.txt_Valeur.getText().toString(),Toast.LENGTH_LONG).show();
            }
        });
        holder.index = position;
        holder.tv_Nom.setTag(position);
        holder.txt_Valeur.setTag(position);
        holder.tv_Guide.setTag(position);
        holder.btn_Enreg.setTag(position);
        holder.tv_ValeurP.setTag(position);
        holder.photo.setTag(position);
        holder.tv_guide.setTag(position);
        if(holder != null) {

            holder.tv_Nom.setText(elements.get(position).getId() +" "+o.getNom() + " : "+elements.get(position).getNom()+"("+elements.get(position).getUnite()+")");
            holder.tv_Guide.setText("");
            holder.tv_guide.setText(elements.get(position).getGuideSaisie());
            if(r != null){
                holder.tv_ValeurP.setText(r.getValeur());
            }
            else {
                holder.tv_ValeurP.setText("Aucune valeur");
            }
        }
        return   convertView;
    }

  /*  @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 500;
    }
    */

    public class ViewHolder {

        TextView tv_Nom;
        EditText txt_Valeur;
        TextView tv_Guide;
        TextView tv_guide;
        TextView tv_ValeurP;
        Button btn_Enreg;
        Button photo;
        int index;
    }

    private DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            //databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
            databaseHelper = new DatabaseHelper(mContext);
        }
        return databaseHelper;
    }

    public void takePhoto(String nom) {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        Calendar gc = new GregorianCalendar();
        gc.set(Calendar.HOUR_OF_DAY,gc.get(Calendar.HOUR_OF_DAY)-1);
        String date = gc.get(Calendar.DAY_OF_MONTH)+"_"+(gc.get(Calendar.MONTH)+1)+"_"+gc.get(Calendar.YEAR)+"_"+(gc.get(Calendar.HOUR_OF_DAY)+1)+"_"+gc.get(Calendar.MINUTE);
        File sdCard = Environment.getExternalStorageDirectory();
        if(sdCard == null) {
            sdCard =  Environment.getDataDirectory();
        }
        File directory = new File (sdCard.getAbsolutePath() + "/teknikExportImage");
        directory.mkdirs();
        nom = nom.replace(' ','_');
        File photo = new File(directory,  nom+"_"+date+".jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(photo));
        imageUri = Uri.fromFile(photo);
        ((Activity)mContext).startActivityForResult(intent, TAKE_PICTURE);
    }

}
