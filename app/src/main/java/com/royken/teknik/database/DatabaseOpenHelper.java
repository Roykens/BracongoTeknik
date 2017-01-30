package com.royken.teknik.database;

import android.database.sqlite.SQLiteOpenHelper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.royken.teknik.activities.BlocActivity;
import com.royken.teknik.entities.Element;
import com.royken.teknik.entities.Organe;
import com.royken.teknik.entities.Reponse;
import com.royken.teknik.entities.SousOrgane;
import com.royken.teknik.entities.Utilisateur;
import com.royken.teknik.entities.Zone;

/**
 * Created by royken on 23/12/16.
 */
public class DatabaseOpenHelper  {

 /*   private static final String DATABASE_NAME = "teknik";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "utilisateurs";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAME = "login";
    private static final String COLUMN_INITIALS = "mdp";
    private static final String COLUMN_ROLE = "role";
    private static final String COLUMN_CAHIER = "cahier";

   // private static final String CREATE_TABLE_UTILISATEUR = "CREATE TABLE " + TABLE_NAME + " ( "+ COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+ COLUMN_NAME + " TEXT, "+ COLUMN_INITIALS + " TEXT NOT NULL, "+COLUMN_ROLE + " TEXT, "  +COLUMN_CAHIER + " TEXT);";

    private static String TABLE_ZONE = "zones";
    private static String COLUMN_ZID = "id";
    private static String COLUMN_ZIDSERVEUR = "idServeur";
    private static String COLUM_ZNOM = "nom";
    private static String COLUMN_ZCODE = "code";

    private static final String CREATE_TABLE_UTILISATEUR = "CREATE TABLE " + TABLE_NAME + " ( "+ COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+ COLUMN_NAME + " TEXT, "+ COLUMN_INITIALS + " TEXT NOT NULL, "+COLUMN_ROLE + " TEXT, "  +COLUMN_CAHIER + " TEXT);";

    private static String TABLE_BLOC = "blocs";
    private static String COLUMN_BID = "id";
    private static String COLUMN_BIDSERVEUR = "idServeur";
    private static String COLUMN_BIDZONE = "idZOne";
    private static String COLUMN_BNOM = "nom";
    private static String COLUMN_CODE = "code";

    private static String TABLE_ORGANE = "organes";
    private static String COLUMN_OID = "id";
    private static String COLUMN_OIDSERVEUR = "idServeur";
    private static String COLUMN_OIDBLOC = "idBloc";
    private static String COLUMN_ONOM = "nom";
    private static String COLUMN_OCODE = "code";

    private static String TABLE_SORGANE = "sousOrganes";
    private static String COLUMN_SID = "id";
    private static String COLUMN_SIDSERVEUR = "idServeur";
    private static String COLUMN_SIDORGANE = "idOrgane";
    private static String COLUMN_SNOM = "nom";
    private static String COLUMN_SCODE = "codSe";

    private static String TABLE_ELEMENT = "elements";
    private static String COLUMN_EID = "id";
    private static String COLUMN_EIDSERVEUR = "idServeur";
    private static String COLUMN_ESOID = "idSousOrgane";
    private static String COLUMN_ENOM = "nom";
    private static String COLUMN_ECODE = "code";
    private static String COLUMN_EALPHA = "alpha";
    private static String COLUMN_EVALMIN = "valMin";
    private static String COLUMN_EVALMAX = "valMax";
    private static String COLUMN_EGUIDE = "guide";
    private static String COLUMN_ENORMALE = "normale";
    private static String COLUMN_ETYPE = "type";
    private static String COLUMN_EBORNE = "borne";

    private static String TABLE_REPONSE = "reponses";
    private static String COLUMN_RID = "id";
    private static String COLUMN_RELEMENT = "idElement";
    private static String COLUMN_RVALEUR = "valeur";
    private static String COLUMN_RCOMPTEUR = "compteur";
    private static String COLUMN_RCODE = "code";
    private static String COLUMN_USER = "user";


    private SQLiteDatabase database;

    private final Context context;

    // database path
    private static String DATABASE_PATH;

    private Dao<Bloc, Integer> blocDao;
    private Dao<Element, Integer> elementDao;
    private Dao<Organe, Integer> organeDao;
    private Dao<Reponse, Integer> reponseDao;
    private Dao<SousOrgane, Integer> sousOrganeDao;
    private Dao<Utilisateur, Integer> utilisateurDao;
    private Dao<Zone, Integer> zoneDao;

    /** constructor */
 /*   public DatabaseOpenHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = ctx;
        DATABASE_PATH = context.getFilesDir().getParentFile().getPath()
                + "/databases/";

    }


    public void onCreate(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource) {
        try {

            // Create tables. This onCreate() method will be invoked only once of the application life time i.e. the first time when the application starts.
            TableUtils.createTable(connectionSource, Bloc.class);
            TableUtils.createTable(connectionSource, Element.class);
            TableUtils.createTable(connectionSource, Organe.class);
            TableUtils.createTable(connectionSource, Reponse.class);
            TableUtils.createTable(connectionSource, SousOrgane.class);
            TableUtils.createTable(connectionSource, Utilisateur.class);
            TableUtils.createTable(connectionSource, Zone.class);

        } catch (SQLException e) {
            Log.e(DatabaseOpenHelper.class.getName(), "Unable to create datbases", e);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a empty database on the system and rewrites it with your own
     * database.
     * */
 /*   public void create() throws IOException {
        boolean check = checkDataBase();

        SQLiteDatabase db_Read = null;

        // Creates empty database default system path
        db_Read = this.getWritableDatabase();
        db_Read.close();
        try {
            if (!check) {
                copyDataBase();
            }
        } catch (IOException e) {
            throw new Error("Error copying database");
        }
    }

    /**
     * Check if the database already exist to avoid re-copying the file each
     * time you open the application.
     *
     * @return true if it exists, false if it doesn't
     */
 /*   private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            String myPath = DATABASE_PATH + DATABASE_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null,
                    SQLiteDatabase.OPEN_READWRITE);
        } catch (SQLiteException e) {
            // database does't exist yet.
        }

        if (checkDB != null) {
            checkDB.close();
        }
        return checkDB != null ? true : false;
    }

    /**
     * Copies your database from your local assets-folder to the just created
     * empty database in the system folder, from where it can be accessed and
     * handled. This is done by transfering bytestream.
     * */
 /*   private void copyDataBase() throws IOException {

        // Open your local db as the input stream
        InputStream myInput = context.getAssets().open(DATABASE_NAME);

        // Path to the just created empty db
        String outFileName = DATABASE_PATH + DATABASE_NAME;

        // Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        // transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        // Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    /** open the database */
 /*   public void open() throws SQLException {
        String myPath = DATABASE_PATH + DATABASE_NAME;
        database = SQLiteDatabase.openDatabase(myPath, null,
                SQLiteDatabase.OPEN_READWRITE);
    }

    /** close the database */
 /*   @Override
    public synchronized void close() {
        if (database != null)
            database.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
    }

    // retrieves a particular user
    public Cursor getUser(long rowId) throws SQLException {
        if(!database.isOpen()){
            open();
        }
        Cursor mCursor = database.query(true, TABLE_NAME, new String[] {
                        COLUMN_ID, COLUMN_NAME, COLUMN_INITIALS },
                COLUMN_ID + " = " + rowId, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }

        return mCursor;
    }
*/

}
