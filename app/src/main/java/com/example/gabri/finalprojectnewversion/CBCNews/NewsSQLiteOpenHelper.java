package com.example.gabri.finalprojectnewversion.CBCNews;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;

/**
 * News database class
 * @author Natalia Nunes
 */
public class NewsSQLiteOpenHelper extends SQLiteOpenHelper {
    /**
     * log Tag
     */
    private String TAG = "CBC-NewsSQL";
    /**
     * database version
     */
    public static final int DATABASE_VERSION = 1;
    /**
     * database name
     */
    public static final String DATABASE_NAME = "News.db";

    /**
     * default constructor
     * @param context app context
     */
    public NewsSQLiteOpenHelper ( Context context ) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * database contract class
     * source:https://developer.android.com/training/data-storage/sqlite
     */
    public final class NewsContract {
        /**
         * create query
         */
        public static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + NewsEntry.TABLE_NAME + " (" +
                        NewsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        NewsEntry.COLUMN_NAME_TITLE + " TEXT," +
                        NewsEntry.COLUMN_NAME_URL + " TEXT," +
                        NewsEntry.COLUMN_NAME_BODY + " TEXT)";

        /**
         * Columns class
         */
        public class NewsEntry implements BaseColumns {
            public static final String TABLE_NAME = "news";
            public static final String COLUMN_NAME_TITLE = "title";
            public static final String COLUMN_NAME_URL = "url";
            public static final String COLUMN_NAME_BODY = "body";
        }
    }

    /**
     * Default onCreate
     * @param db database instance
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(NewsContract.SQL_CREATE_TABLE);
    }

    /**
     * Default upgrade
     * @param db database instance
     * @param oldVersion old version number
     * @param newVersion new version number
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    /**
     * Inserts a new article
     * source: https://developer.android.com/training/data-storage/sqlite
     * @param n news object
     * @return inserted Id
     */
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

    /**
     * deletes new article
     * source: https://developer.android.com/training/data-storage/sqlite
     * @param newsTitle article title
     * @return deleted id
     */
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

    /**
     * selects all news articles
     * source: https://developer.android.com/training/data-storage/sqlite
     * @return  ArrayList of a news objects
     */
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
