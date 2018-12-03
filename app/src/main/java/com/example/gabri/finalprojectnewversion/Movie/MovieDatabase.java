package com.example.gabri.finalprojectnewversion.Movie;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * class for creating the movie database that holds all the movie details
 */
public class MovieDatabase extends SQLiteOpenHelper {
    /**
     * class variables
     */
    private static final String TAG="MovieDatabase";
    private static final String DATABASE_NAME="SavedMovies.db";
    private static int VERSION_NUM=6;
    final static String TABLE_NAME="SavedMovies";
    final static String KEY_ID="ID";
    final static String KEY_TITLE="Title";
    final static String KEY_YEAR="Year";
    final static String KEY_RATED="Rated";
    final static String KEY_RUNTIME="Runtime";
    final static String KEY_ACTORS="Actors";
    final static String KEY_PLOT="Plot";
    final static String KEY_POSTER="Poster";

    /**
     * constructor for database class
     * @param ctx context
     */
    public MovieDatabase(Context ctx){
        super(ctx, DATABASE_NAME,null,VERSION_NUM);
    }

    /**
     * method to create database table
     * @param db database to be create
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(" CREATE TABLE " + TABLE_NAME + " (" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_TITLE +
                " TEXT NOT NULL, " + KEY_YEAR + " TEXT NOT NULL, " + KEY_RATED + " TEXT NOT NULL, " + KEY_RUNTIME + " TEXT NOT NULL, " + KEY_ACTORS + " TEXT NOT NULL, " + KEY_PLOT + " TEXT NOT NULL, " + KEY_POSTER + " TEXT NOT NULL );");
    }

    /**
     * method to recreate database when version number changes
     * @param db database that is being manipulated
     * @param oldVersion old version number
     * @param newVersion new version number
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(db);
        Log.i(TAG,"Calling onUpgrade, oldVersion=" + oldVersion+"newVersion="+newVersion);

    }

    /**
     * method executes SQL query to retrieve all information from database
     * @return cursor object that holds all the information of the saved movies table
     */
    public Cursor getAllSavedMovies(){
        SQLiteDatabase movies=this.getReadableDatabase();
        Cursor cursor=movies.rawQuery("SELECT * FROM "+TABLE_NAME,null);
        return cursor;

    }
}
