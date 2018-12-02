package com.example.gabri.finalprojectnewversion.Movie;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MovieDatabase extends SQLiteOpenHelper {
    private static final String TAG="MovieDatabase";
    private static final String DATABASE_NAME="SavedMovies.db";
    private static int VERSION_NUM=1;
    final static String TABLE_NAME="SavedMovies";
    final static String KEY_ID="ID";
    final static String KEY_TITLE="Title";


    public MovieDatabase(Context ctx){
        super(ctx, DATABASE_NAME,null,VERSION_NUM);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+ TABLE_NAME+"("+KEY_ID+"INTEGER PRIMARY KEY AUTOINCREMENT,"+KEY_TITLE+"text);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(db);
        Log.i(TAG,"Calling onUpgrade, oldVersion=" + oldVersion+"newVersion="+newVersion);

    }

    public Cursor getAllSavedMovies(){
        SQLiteDatabase movies=this.getReadableDatabase();
        Cursor cursor=movies.rawQuery("SELECT * FROM "+TABLE_NAME,null);
        return cursor;
    }
}
