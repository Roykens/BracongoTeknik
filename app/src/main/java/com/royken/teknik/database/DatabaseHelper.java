package com.royken.teknik.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.royken.teknik.R;
import com.royken.teknik.entities.Bloc;
import com.royken.teknik.entities.Cahier;
import com.royken.teknik.entities.Element;
import com.royken.teknik.entities.Organe;
import com.royken.teknik.entities.Periode;
import com.royken.teknik.entities.Reponse;
import com.royken.teknik.entities.SousOrgane;
import com.royken.teknik.entities.Utilisateur;
import com.royken.teknik.entities.Zone;

import java.sql.SQLException;

/**
 * Created by royken on 30/12/16.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "teknik";
    private static final int DATABASE_VERSION = 1;

    private Dao<Bloc, Integer> blocDao;
    private Dao<Element, Integer> elementDao;
    private Dao<Organe, Integer> organeDao;
    private Dao<Reponse, Integer> reponseDao;
    private Dao<SousOrgane, Integer> sousOrganeDao;
    private Dao<Utilisateur, Integer> utilisateurDao;
    private Dao<Zone, Integer> zoneDao;
    private Dao<Cahier, Integer> cahierDao;
    private Dao<Periode, Integer> periodeDao;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
    }

    @Override
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
            TableUtils.createTable(connectionSource, Cahier.class);
            TableUtils.createTable(connectionSource, Periode.class);

        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Unable to create datbases", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource, int oldVer, int newVer) {
        try {

            // In case of change in database of next version of application, please increase the value of DATABASE_VERSION variable, then this method will be invoked
            //automatically. Developer needs to handle the upgrade logic here, i.e. create a new table or a new column to an existing table, take the backups of the
            // existing database etc.

            TableUtils.dropTable(connectionSource, Bloc.class, true);
            TableUtils.dropTable(connectionSource, Element.class, true);
            TableUtils.dropTable(connectionSource, Organe.class, true);
            TableUtils.dropTable(connectionSource, Reponse.class, true);
            TableUtils.dropTable(connectionSource, SousOrgane.class, true);
            TableUtils.dropTable(connectionSource, Utilisateur.class, true);
            TableUtils.dropTable(connectionSource, Zone.class, true);
            TableUtils.dropTable(connectionSource, Cahier.class, true);
            TableUtils.dropTable(connectionSource, Periode.class, true);
            onCreate(sqliteDatabase, connectionSource);

        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Unable to upgrade database from version " + oldVer + " to new "
                    + newVer, e);
        }
    }

    // Create the getDao methods of all database tables to access those from android code.
    // Insert, delete, read, update everything will be happened through DAOs

    public Dao<Bloc, Integer> getBlocDao() throws SQLException {
        if (blocDao == null) {
            blocDao = getDao(Bloc.class);
        }
        return blocDao;
    }

    public Dao<Element, Integer> getElementDao() throws SQLException {
        if (elementDao == null) {
            elementDao = getDao(Element.class);
        }
        return elementDao;
    }

    public Dao<Organe, Integer> getOrganeDao() throws SQLException {
        if (organeDao == null) {
            organeDao = getDao(Organe.class);
        }
        return organeDao;
    }

    public Dao<Reponse, Integer> getReponseDao() throws SQLException {
        if (reponseDao == null) {
            reponseDao = getDao(Reponse.class);
        }
        return reponseDao;
    }

    public Dao<SousOrgane, Integer> getSousOrganeDao() throws SQLException {
        if (sousOrganeDao == null) {
            sousOrganeDao = getDao(SousOrgane.class);
        }
        return sousOrganeDao;
    }

    public Dao<Utilisateur, Integer> getUtilisateurDao() throws SQLException {
        if (utilisateurDao == null) {
            utilisateurDao = getDao(Utilisateur.class);
        }
        return utilisateurDao;
    }

    public Dao<Zone, Integer> getZoneDao() throws SQLException {
        if (zoneDao == null) {
            zoneDao = getDao(Zone.class);
        }
        return zoneDao;
    }

    public Dao<Cahier, Integer> getCahierDao() throws SQLException {
        if (cahierDao == null) {
            cahierDao = getDao(Cahier.class);
        }
        return cahierDao;
    }

    public Dao<Periode, Integer> getPeriodeDao() throws SQLException {
        if (periodeDao == null) {
            periodeDao = getDao(Periode.class);
        }
        return periodeDao;
    }
}
