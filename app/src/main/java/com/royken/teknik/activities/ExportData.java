package com.royken.teknik.activities;

import android.content.Context;
import android.os.Environment;

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
        Calendar gc = new GregorianCalendar();
        gc.set(Calendar.HOUR_OF_DAY, gc.get(Calendar.HOUR_OF_DAY) - 1);
        String date = gc.get(Calendar.DAY_OF_MONTH)+"_"+(gc.get(Calendar.MONTH)+1)+"_"+gc.get(Calendar.YEAR)+"_"+(gc.get(Calendar.HOUR_OF_DAY)+1)+"_"+gc.get(Calendar.MINUTE);
        String Fnamexls ="donnees_"+date  + ".xls";
        File sdCard = Environment.getExternalStorageDirectory();
        if(sdCard == null) {
           sdCard =  Environment.getDataDirectory();
        }
        File directory = new File (sdCard.getAbsolutePath() + "/teknikExport");
        directory.mkdirs();
        File file = new File(directory, Fnamexls);
        WorkbookSettings wbSettings = new WorkbookSettings();
        wbSettings.setLocale(new Locale("en", "EN"));
        WritableWorkbook workbook;
        try {
            workbook = Workbook.createWorkbook(file, wbSettings);
            WritableSheet sheet = workbook.createSheet("First Sheet", 0);
            Label label = new Label(0, 0, "#");
            Label label5 = new Label(1,0,"CAHIER");
            Label label1 = new Label(2,0,"CODE ECMS");
            Label label7 = new Label(3,0,"DESIGNATION ECMS");
            Label label4 = new Label(4,0,"DATE");
            Label label0 = new Label(5,0,"VALEUR");
            Label label6 = new Label(6,0,"Correcte ?");
            Label label3 = new Label(7,0,"IDENTIFIANT");


            try {
                sheet.addCell(label);
                sheet.addCell(label1);
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
            for (int i = 0, k=1; i < reponses.size(); i++,k++) {
                int j = 0;
                label = new Label(j++,k,i+1+"");
                label5 = new Label(j++,k,reponses.get(i).getCahier());
                label1 = new Label(j++,k,reponses.get(i).getCode());
                label7 = new Label(j++,k,reponses.get(i).getNom());
                label4 = new Label(j++,k,getDateString(reponses.get(i).getDate()));
                label0 = new Label(j++,k,reponses.get(i).getValeur().replace('.', ','));
                label6 = new Label(j++,k,reponses.get(i).isValeurCorrecte()? "1":"0");
                label3 = new Label(j++,k,reponses.get(i).getUser());


                try {
                    sheet.addCell(label);
                    sheet.addCell(label1);
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
            workbook.write();
            try {
                workbook.close();
            } catch (WriteException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (databaseHelper == null){
                //databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
                databaseHelper = new DatabaseHelper(context);
            }
            //TableUtils.dropTable(databaseHelper.getConnectionSource(), Reponse.class, true);
            TableUtils.clearTable(databaseHelper.getConnectionSource(), Reponse.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void produceDocument(){
        String Fnamexls ="testfile"  + ".xls";
        File sdCard = Environment.getExternalStorageDirectory();
        File directory = new File (sdCard.getAbsolutePath() + "/teknikExport");
        directory.mkdirs();
        File file = new File(directory, Fnamexls);
        WorkbookSettings wbSettings = new WorkbookSettings();
        wbSettings.setLocale(new Locale("en", "EN"));
        WritableWorkbook workbook;

        try {
            int a = 1;
            workbook = Workbook.createWorkbook(file, wbSettings);
            WritableSheet sheet = workbook.createSheet("First Sheet", 0);
            Label label = new Label(0, 2, "SECOND");
            Label label1 = new Label(0,1,"first");
            Label label0 = new Label(0,0,"HEADING");
            Label label3 = new Label(1,0,"Heading2");
            Label label4 = new Label(1,1,String.valueOf(a));

            try {
                sheet.addCell(label);
                sheet.addCell(label1);
                sheet.addCell(label0);
                sheet.addCell(label4);
                sheet.addCell(label3);
            } catch (RowsExceededException e) {
                e.printStackTrace();
            } catch (WriteException e) {
                e.printStackTrace();
            }
            workbook.write();

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
