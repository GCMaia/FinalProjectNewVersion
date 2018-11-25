package com.example.gabri.finalprojectnewversion.CBCNews;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;

public class NewsSQLiteOpenHelper extends SQLiteOpenHelper {

    private String TAG = "CBC-NewsSQL";

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "News.db";

    public NewsSQLiteOpenHelper ( Context context ) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public final class NewsContract {

        public static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + NewsEntry.TABLE_NAME + " (" +
                        NewsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        NewsEntry.COLUMN_NAME_TITLE + " TEXT," +
                        NewsEntry.COLUMN_NAME_URL + " TEXT," +
                        NewsEntry.COLUMN_NAME_BODY + " TEXT)";

        public class NewsEntry implements BaseColumns {
            public static final String TABLE_NAME = "news";
            public static final String COLUMN_NAME_TITLE = "title";
            public static final String COLUMN_NAME_URL = "url";
            public static final String COLUMN_NAME_BODY = "body";
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(NewsContract.SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    public long insertNewsEntry ( News n ) {
        // Gets the data repository in write mode
        SQLiteDatabase db = getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(NewsContract.NewsEntry.COLUMN_NAME_TITLE, n.getTitle());
        values.put(NewsContract.NewsEntry.COLUMN_NAME_URL, n.getUrl());
        values.put(NewsContract.NewsEntry.COLUMN_NAME_BODY, n.getBody());

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(NewsContract.NewsEntry.TABLE_NAME, null, values);

        Log.d(TAG, "Inserted News Entry");
        return newRowId;
    }

    public int deleteNewsEntry ( News n ) {

        return deleteNewsEntry(n.getTitle());

//        // Gets the data repository in write mode
//        SQLiteDatabase db = getWritableDatabase();
//
//        // Define 'where' part of query.
//        String selection = NewsContract.NewsEntry.COLUMN_NAME_TITLE + " = ?";
//        // Specify arguments in placeholder order.
//        String[] selectionArgs = { n.getTitle() };
//        // Issue SQL statement.
//        int deletedRows = db.delete(NewsContract.NewsEntry.TABLE_NAME, selection, selectionArgs);
//
//        Log.d(TAG, "Deleted News Entry");
//        return deletedRows;
    }

    public int deleteNewsEntry ( String newsTitle ) {
        // Gets the data repository in write mode
        SQLiteDatabase db = getWritableDatabase();

        // Define 'where' part of query.
        String selection = NewsContract.NewsEntry.COLUMN_NAME_TITLE + " = ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = { newsTitle };
        // Issue SQL statement.
        int deletedRows = db.delete(NewsContract.NewsEntry.TABLE_NAME, selection, selectionArgs);

        Log.d(TAG, "Deleted News Entry");
        return deletedRows;
    }

    public ArrayList<News> selectAllNewsEntry ( ) {
        ArrayList<News> nl = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query. (pass null to get all)
        String[] projection = null;

        // A filter declaring which rows to return, formatted as an SQL WHERE clause (excluding the WHERE itself).
        // Passing null will return all rows for the given table.
        String selection = null;
        String[] selectionArgs = null;

        // How you want the results sorted in the resulting Cursor
        String sortOrder = NewsContract.NewsEntry.COLUMN_NAME_TITLE + " DESC";

        Cursor cursor = db.query(
                NewsContract.NewsEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );

        while(cursor.moveToNext()) {
            String title = cursor.getString(cursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_NAME_TITLE));
            String body = cursor.getString(cursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_NAME_BODY));
            String url = cursor.getString(cursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_NAME_URL));
            News n = new News(title);
            n.setBody(body);
            n.setUrl(url);
            nl.add(n);
            Log.d(TAG, "Selecting: " + title);
        }
        cursor.close();

        Log.d(TAG, "Selected News Entries");
        return nl;
    }

}
