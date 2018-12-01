package com.example.gabri.finalprojectnewversion.OCTranspo;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

class OCTranspoDatabase extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "OCTranspo.db";
    public static final String TABLE_NAME = "SavedBuses";
    public static final String KEY_ID = "_id";
    public static final String KEY_BUS_NAME = "busName";
    public static final String KEY_BUS_NUM = "busNum";
    public static final String KEY_BUS_STOP = "busStop";


    public OCTranspoDatabase ( Context context ) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_BUS_NAME +
                " TEXT NOT NULL, " + KEY_BUS_NUM + " TEXT NOT NULL, " + KEY_BUS_STOP + " TEXT NOT NULL );");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public Cursor getAllSavedBusesTest(){
        final SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select * from " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);

        return cursor;
    }



}
