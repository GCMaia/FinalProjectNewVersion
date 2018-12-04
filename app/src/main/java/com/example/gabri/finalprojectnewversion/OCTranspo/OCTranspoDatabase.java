package com.example.gabri.finalprojectnewversion.OCTranspo;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * class used to create and access the application's database
 * @author Gabriel Cardoso Maia
 * @since November, 08/2018
 */
class OCTranspoDatabase extends SQLiteOpenHelper {


    /**
     * Attributes for handling the database
     */
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "OCTranspo.db";
    public static final String TABLE_NAME = "SavedBuses";
    public static final String KEY_ID = "_id";
    public static final String KEY_BUS_NAME = "busName";
    public static final String KEY_BUS_NUM = "busNum";
    public static final String KEY_BUS_STOP = "busStop";


    /**
     *  used for creating the database
     */
    OCTranspoDatabase ( Context context ) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    /**
     * method used for creating the application table
     * @param db database being used
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_BUS_NAME +
                " TEXT NOT NULL, " + KEY_BUS_NUM + " TEXT NOT NULL, " + KEY_BUS_STOP + " TEXT NOT NULL );");
    }

    /**
     * method used to upgrade the database
     * @param db database being used
     * @param oldVersion previous version of the database
     * @param newVersion newest version of the database
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    /**
     * method used to get all the buses on the database
     * @return cursor object with the query to get the information row from the table
     */
    public Cursor getAllSavedBusesTest(){
        final SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select * from " + TABLE_NAME;
        return db.rawQuery(query, null);
    }



}
