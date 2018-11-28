package com.example.gabri.finalprojectnewversion.OCTranspo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

class OCTranspoDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Messages.db";
    private static final int VERSION_NUM = 1;
    static final String TABLE_NAME = "MessageList";
    static final String KEY_ID = "_id", KEY_MESSAGE = "message";
    protected Cursor c;


    OCTranspoDatabase(Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_MESSAGE + " TEXT NOT NULL);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

}
