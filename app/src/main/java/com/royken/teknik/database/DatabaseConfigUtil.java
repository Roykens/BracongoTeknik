package com.royken.teknik.database;

import java.sql.SQLException;

import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;

import java.io.IOException;

/**
 * Created by royken on 29/12/16.
 */
public class DatabaseConfigUtil extends OrmLiteConfigUtil {

    public static void main(String[] args) throws SQLException, IOException {

        // Provide the name of .txt file which you have already created and kept in res/raw directory
        try {
            writeConfigFile("ormlite_config.txt");
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }
}
