package com.royken.teknik.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.table.TableUtils;
import com.royken.teknik.database.DatabaseHelper;
import com.royken.teknik.entities.Reponse;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.CellFormat;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

/**
 * Created by royken on 03/01/17.
 */
public class ExportData {
    private Context context;
    private DatabaseHelper databaseHelper = null;
    private List<Reponse> reponses;


    public ExportData(Context context, List<Reponse> reponses) {
        this.context = context;
        this.reponses = reponses;
    }

    private String getDateString(Date date){
        Calendar gc = new GregorianCalendar();
        gc.setTime(date);
        String result = gc.get(Calendar.DAY_OF_MONTH)+"/"+(gc.get(Calendar.MONTH)+1)+"/"+gc.get(Calendar.YEAR)+" "+gc.get(Calendar.HOUR_OF_DAY)+":"+gc.get(Calendar.MINUTE);
        return result;
    }

    public void exportReponse(){
        ProgressDialog Dialog = new ProgressDialog(context);
        Calendar gc = new GregorianCalendar();
        gc.set(Calendar.HOUR_OF_DAY, gc.get(Calendar.HOUR_OF_DAY) - 1);
        String date = gc.get(Calendar.DAY_OF_MONTH)+"_"+(gc.get(Calendar.MONTH)+1)+"_"+gc.get(Calendar.YEAR)+"_"+(gc.get(Calendar.HOUR_OF_DAY)+1)+"_"+gc.get(Calendar.MINUTE);
        String Fnamexls ="donnees_"+date  + ".xls";
        File sdCard = Environment.getExternalStorageDirectory();
        File directory;
       /* if(isExternalStorageWritable()){
            Toast.makeText(context,"La carte est dispo",Toast.LENGTH_LONG).show();
            directory = getFolderStorageDir("teknikExport");
        }
        */
       // else {
           sdCard =  Environment.getExternalStorageDirectory();
            directory = new File (sdCard.getAbsolutePath() + "/teknikExport");
            directory.mkdirs();
       // }

        File file = new File(directory, Fnamexls);
        WorkbookSettings wbSettings = new WorkbookSettings();
        wbSettings.setLocale(new Locale("en", "EN"));
        WritableWorkbook workbook;
        //Dialog.setMessage("Exportation .....");
        //Dialog.show();
        try {
            workbook = Workbook.createWorkbook(file, wbSettings);
            int chunk = 0;
            int taille = reponses.size();
            int nombreIteration = (taille/60000) + 1;
            for (int l = 0; l < nombreIteration; l++){
                WritableSheet sheet = workbook.createSheet("Sheet : " + l, 0);
                Label label = new Label(0, 0, "#");
                Label label5 = new Label(1,0,"CAHIER");
                Label label8 = new Label(2,0,"ORGANE");
                Label label9 = new Label(3,0,"SOUS ORGANE");
                Label label1 = new Label(4,0,"CODE ECMS");
                Label label7 = new Label(5,0,"DESIGNATION ECMS");
                Label label4 = new Label(6,0,"DATE");
                Label label0 = new Label(7,0,"VALEUR");
                Label label6 = new Label(8,0,"Correcte ?");
                Label label3 = new Label(9,0,"IDENTIFIANT");

                try {
                    sheet.addCell(label);
                    sheet.addCell(label1);
                    sheet.addCell(label8);
                    sheet.addCell(label9);
                    sheet.addCell(label0);
                    sheet.addCell(label4);
                    sheet.addCell(label3);
                    sheet.addCell(label5);
                    sheet.addCell(label6);
                    sheet.addCell(label7);
                } catch (RowsExceededException e) {
                    e.printStackTrace();
                } catch (WriteException e) {
                    e.printStackTrace();
                }
                //reponses.

                for (int i = l * 60000, k=1, p = 0; i < reponses.size() && (i < (l * 60000) + 60000); i++,k++,p++) {
                    int j = 0;
                    label = new Label(j++,k,p+1+"");
                    label5 = new Label(j++,k,reponses.get(i).getCahier());
                    label8 = new Label(j++,k,reponses.get(i).getOrgane());
                    label9 = new Label(j++,k,reponses.get(i).getSousOrgane());
                    label1 = new Label(j++,k,reponses.get(i).getCode());
                    label7 = new Label(j++,k,reponses.get(i).getNom());
                    label4 = new Label(j++,k,getDateString(reponses.get(i).getDate()));
                    label0 = new Label(j++,k,reponses.get(i).getValeur().replace('.', ','));
                    label6 = new Label(j++,k,reponses.get(i).isValeurCorrecte()? "1":"0");
                    label3 = new Label(j++,k,reponses.get(i).getUser());


                    try {
                        sheet.addCell(label);
                        sheet.addCell(label1);
                        sheet.addCell(label8);
                        sheet.addCell(label9);
                        sheet.addCell(label0);
                        sheet.addCell(label4);
                        sheet.addCell(label3);
                        sheet.addCell(label5);
                        sheet.addCell(label6);
                        sheet.addCell(label7);
                    } catch (WriteException e) {
                        e.printStackTrace();
                    }
                }

            }
//////////////////////////////////////////////////////////////////////////////////////////////////////////
           /* WritableSheet sheet = workbook.createSheet("First Sheet", 0);
            Label label = new Label(0, 0, "#");
            Label label5 = new Label(1,0,"CAHIER");
            Label label8 = new Label(2,0,"ORGANE");
            Label label9 = new Label(3,0,"SOUS ORGANE");
            Label label1 = new Label(4,0,"CODE ECMS");
            Label label7 = new Label(5,0,"DESIGNATION ECMS");
            Label label4 = new Label(6,0,"DATE");
            Label label0 = new Label(7,0,"VALEUR");
            Label label6 = new Label(8,0,"Correcte ?");
            Label label3 = new Label(9,0,"IDENTIFIANT");


            try {
                sheet.addCell(label);
                sheet.addCell(label1);
                sheet.addCell(label8);
                sheet.addCell(label9);
                sheet.addCell(label0);
                sheet.addCell(label4);
                sheet.addCell(label3);
                sheet.addCell(label5);
                sheet.addCell(label6);
                sheet.addCell(label7);
            } catch (RowsExceededException e) {
                e.printStackTrace();
            } catch (WriteException e) {
                e.printStackTrace();
            }
            //reponses.

            for (int i = 0, k=1; i < reponses.size(); i++,k++) {
                int j = 0;
                label = new Label(j++,k,i+1+"");
                label5 = new Label(j++,k,reponses.get(i).getCahier());
                label8 = new Label(j++,k,reponses.get(i).getOrgane());
                label9 = new Label(j++,k,reponses.get(i).getSousOrgane());
                label1 = new Label(j++,k,reponses.get(i).getCode());
                label7 = new Label(j++,k,reponses.get(i).getNom());
                label4 = new Label(j++,k,getDateString(reponses.get(i).getDate()));
                label0 = new Label(j++,k,reponses.get(i).getValeur().replace('.', ','));
                label6 = new Label(j++,k,reponses.get(i).isValeurCorrecte()? "1":"0");
                label3 = new Label(j++,k,reponses.get(i).getUser());


                try {
                    sheet.addCell(label);
                    sheet.addCell(label1);
                    sheet.addCell(label8);
                    sheet.addCell(label9);
                    sheet.addCell(label0);
                    sheet.addCell(label4);
                    sheet.addCell(label3);
                    sheet.addCell(label5);
                    sheet.addCell(label6);
                    sheet.addCell(label7);
                } catch (WriteException e) {
                    e.printStackTrace();
                }
            }
            */
            workbook.write();
          //  Dialog.dismiss();
            try {
                workbook.close();
            } catch (WriteException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

       /* try {
            if (databaseHelper == null){
                //databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
                databaseHelper = new DatabaseHelper(context);
            }
            //TableUtils.dropTable(databaseHelper.getConnectionSource(), Reponse.class, true);
            TableUtils.clearTable(databaseHelper.getConnectionSource(), Reponse.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        */
    }

    public File getFolderStorageDir(String albumName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+ albumName);
        if (!file.mkdirs()) {
           // Log.e(LOG_TAG, "Directory not created");
        }
        return file;
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public void exportReponseForImport(){
        ProgressDialog Dialog = new ProgressDialog(context);
        Calendar gc = new GregorianCalendar();
        gc.set(Calendar.HOUR_OF_DAY, gc.get(Calendar.HOUR_OF_DAY) - 1);
        String date = gc.get(Calendar.DAY_OF_MONTH)+"_"+(gc.get(Calendar.MONTH)+1)+"_"+gc.get(Calendar.YEAR)+"_"+(gc.get(Calendar.HOUR_OF_DAY)+1)+"_"+gc.get(Calendar.MINUTE);
        String Fnamexls ="donneesPourImport_"+date  + ".xls";
        File sdCard = Environment.getExternalStorageDirectory();
        File directory;
        if(isExternalStorageWritable()){
            //Toast.makeText(context,"La carte est dispo",Toast.LENGTH_LONG).show();
            directory = getFolderStorageDir("/teknikExport");
        }

        else {
            //sdCard =  Environment.getExternalStorageDirectory();
            directory = new File (sdCard.getAbsolutePath() + "/teknikExport");
            directory.mkdirs();
         }

        File file = new File(directory, Fnamexls);
        WorkbookSettings wbSettings = new WorkbookSettings();
        wbSettings.setLocale(new Locale("en", "EN"));
        WritableWorkbook workbook;
        //Dialog.setMessage("Exportation .....");
        //Dialog.show();
        try {
            workbook = Workbook.createWorkbook(file, wbSettings);
            int chunk = 0;
            int taille = reponses.size();
            int nombreIteration = (taille/60000) + 1;
            for (int l = 0; l < nombreIteration; l++){
                WritableSheet sheet = workbook.createSheet("Sheet : " + l, 0);
                Label label1 = new Label(0, 0, "#");
                Label label2 = new Label(1,0,"IdEl");
                Label label3 = new Label(2,0,"Valeur");
                Label label4 = new Label(3,0,"Correcte ?");
                Label label5 = new Label(4,0,"Date");
                Label label6 = new Label(5,0,"IdUser");

                try {
                    sheet.addCell(label1);
                    sheet.addCell(label2);
                    sheet.addCell(label3);
                    sheet.addCell(label4);
                    sheet.addCell(label5);
                    sheet.addCell(label6);
                } catch (RowsExceededException e) {
                    e.printStackTrace();
                } catch (WriteException e) {
                    e.printStackTrace();
                }
                //reponses.

                for (int i = l * 60000, k=1, p = 0; i < reponses.size() && (i < (l * 60000) + 60000); i++,k++,p++) {
                    int j = 0;
                    label1 = new Label(j++,k,p+1+"");
                    label2 = new Label(j++,k,reponses.get(i).getIdElement()+"");
                    label3 = new Label(j++,k,reponses.get(i).getValeur().replace('.', ','));
                    label4 = new Label(j++,k,reponses.get(i).isValeurCorrecte()? "true":"false");
                    label5 = new Label(j++,k,reponses.get(i).getDate().getTime()+"");
                    label6 = new Label(j++, k, reponses.get(i).getIdUser()+"");
                   // new Label(1,1,"", CellFormat.)


                    try {
                        sheet.addCell(label1);
                        sheet.addCell(label2);
                        sheet.addCell(label3);
                        sheet.addCell(label4);
                        sheet.addCell(label5);
                        sheet.addCell(label6);
                    } catch (WriteException e) {
                        e.printStackTrace();
                    }
                }

            }
            workbook.write();
            //  Dialog.dismiss();
            try {
                workbook.close();
            } catch (WriteException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
