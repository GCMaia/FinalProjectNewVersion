package com.example.gabri.finalprojectnewversion.FoodNutrition;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class FoodDatabaseHelp extends SQLiteOpenHelper {
    private static String DATABASE_NAME = "NutritionInformation.db";
    private static int VERSION_NUM = 2;
    private static final String TAG = "FoodDatabaseHelp";
    public static final String Key_ID = "id";
    public static final String Key_FOOD = "FoodType";
    public static final String FOODNUTRITIONRESULT_TABLE_NAME = "results";
    public static final String[] COLUMN_ALL = new String[]{Key_FOOD};
    public static final String Key_CALORIES = "Calories";
    public static final String Key_Fat = "Fat";
    public static final String CREATE_FOODNUTRITIONRESULT_TABLE = " CREATE TABLE " +
            FOODNUTRITIONRESULT_TABLE_NAME + " (" + Key_ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + Key_FOOD +
            " TEXT NOT NULL, " + Key_Fat + " TEXT NOT NULL, " + Key_CALORIES + " TEXT NOT NULL );";
    private static final String DROP_FOODNUTRITIONRESULT_TABLE = "DROP TABLE IF EXISTS " + FOODNUTRITIONRESULT_TABLE_NAME;

    public FoodDatabaseHelp(Context ctx) {

        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("FoodDatabaseHelp","Calling onCreate");
        db.execSQL(CREATE_FOODNUTRITIONRESULT_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.i(TAG,"Calling upgrade");
        Log.i(TAG, "Calling onUpgrade, oldVersion=" + oldVersion + " newVersion=" + newVersion);
        db.execSQL(DROP_FOODNUTRITIONRESULT_TABLE);
        onCreate(db);
    }

    public void insertValue(String foodValue, String fatValue, String calValue) {
        final SQLiteDatabase db = this.getWritableDatabase();
        final ContentValues values = new ContentValues();
        values.put(FoodDatabaseHelp.Key_FOOD, foodValue);
        values.put(FoodDatabaseHelp.Key_Fat,fatValue );
        values.put(FoodDatabaseHelp.Key_CALORIES, calValue);
        long insertId = db.insert(FOODNUTRITIONRESULT_TABLE_NAME, null, values);

        if (insertId > 0) {
            Log.i( TAG,"Result inserted successfully");
        }
        else {
            Log.e(TAG, "Failed to insert result");
        }
        db.close();
    }

    public List<String> getAllResults () {
        final List<String> results = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        final Cursor cursor = db.query(FOODNUTRITIONRESULT_TABLE_NAME, COLUMN_ALL, null, null, null, null, null);
        Log.i(TAG, "Coursor's column count: " + cursor.getColumnCount());

        for (int i = 0; i < cursor.getColumnCount(); i++)
            Log.i(TAG, " Cursor's Column: " + cursor.getColumnName(i));
        while (cursor.moveToNext()) {
            final String information = cursor.getString(cursor.getColumnIndex(Key_FOOD));
            Log.i(TAG, "SQL result: " + Key_FOOD);
            results.add(information);
        }
        cursor.close();
        return results;
    }

    public Cursor getAllSavedFoods(){
        final SQLiteDatabase database = this.getReadableDatabase();
        String query = "Select * from " + FOODNUTRITIONRESULT_TABLE_NAME;
        return database.rawQuery(query, null);
    }



}




