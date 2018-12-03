package com.example.gabri.finalprojectnewversion.FoodNutrition;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.gabri.finalprojectnewversion.R;

import java.util.ArrayList;

import javax.xml.transform.Result;

public class FoodNutrition_favourites_list extends AppCompatActivity {

    FoodDatabaseHelp myDB;
    private Bundle resultBundle;
    private FoodNutritionSearchResult parent;
    private TextView resultId;
    private TextView foodName;
    private Button deleteResult;
    private long id;
    private String msg;
    private boolean isTablet;
    private double total;
    private Handler handler;
    private int viewposition;
    public  Button AverageCal;
    public  Button TotalCal;
    public  Button MinimumCal;
    public  Button MaximumCal;
    private Cursor cursor;
    private SQLiteDatabase db;
    private FoodDatabaseHelp foodDatabaseHelp;
    private Runnable runnable;
    private ListView foodListView;
    private FrameLayout tabletLayOut;
    private FoodNutritionMain.FoodAdapter foodAdapter;
    ArrayList<Result>foodList = new ArrayList<>();

    public static final String Average_Calories = "AverageCal";
    public static final String Total_Calories = "TotalCal";
    public static final String Minimum_Calories = "MinimumCal";
    public static final String Maximum_Calories = "MaximumCal";
    ArrayList<String> arrayListFoodName = new ArrayList<>();
    ArrayList<Integer> arrayListId = new ArrayList<>();
    ArrayList<String> arrayListCal = new ArrayList<>();
    ArrayList<String> arrayListFat = new ArrayList<>();

    ListViewSavedFoods adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_nutrition_favourites_list);

        AverageCal = findViewById(R.id.average_calories);
        TotalCal = findViewById(R.id.total_calories);
        MinimumCal = findViewById(R.id.Calminimum);
        MaximumCal = findViewById(R.id.Calmaximum);


        myDB = new FoodDatabaseHelp(this);

        getSavedFood();

//        List<String> data = myDB.getAllResults();

//        AverageCal.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                cursor = db.rawQuery("select * from " + foodDatabaseHelp, null);
//                total = 0.00;
//                Result result = foodList.get(5);
//                String[]tempArray = new String[5];
//
//
//
//                }
//        });

//        if(data.size()==0){
//            Toast.makeText(FoodNutrition_favourites_list.this,"The database was empty: (.",Toast.LENGTH_LONG).show();
//        } else {
//        }


    }

    private void getSavedFood(){
        Cursor cursor = myDB.getAllSavedFoods();

        while (cursor.moveToNext()){
            arrayListFoodName.add(cursor.getString(1));
            arrayListId.add(cursor.getInt(0));
            arrayListCal.add(cursor.getString(3));
            arrayListFat.add(cursor.getString(2));
        }

        adapter = new ListViewSavedFoods(this);
        foodListView = findViewById(R.id.listViewForSaved);
        foodListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    class ListViewSavedFoods extends ArrayAdapter<String>{

        public ListViewSavedFoods(Context context) {
            super(context, 0);
        }

        @NonNull
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = FoodNutrition_favourites_list.this.getLayoutInflater();
            View view = inflater.inflate(R.layout.food_nutrition_saved_food, null);
            TextView foodName = view.findViewById(R.id.savedFoodOnDatabase);
            foodName.setText(arrayListFoodName.get(position));
            Button deleteButton = view.findViewById(R.id.buttonForRemovingFromDatabase);

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    FoodDatabaseHelp helper = new FoodDatabaseHelp(getContext());
                    SQLiteDatabase db = helper.getWritableDatabase();
                    db.delete(FoodDatabaseHelp.FOODNUTRITIONRESULT_TABLE_NAME, FoodDatabaseHelp.Key_ID
                            + " = ?", new String[]{Integer.toString(arrayListId.get(position))});

                    arrayListFoodName.remove(position);

                    adapter.notifyDataSetChanged();
                }


            });


            foodListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(FoodNutrition_favourites_list.this, FoodNutritionSearchResult.class);
                    intent.putExtra("foodname", getSaveFoodName(position));
                    intent.putExtra("cal",getSaveFoodCal(position));
                    intent.putExtra("fat",getSaveFoodFat(position));
                    startActivity(intent);
                }
            });


            return view;
        }


        public int getCount(){
            return arrayListFoodName.size();
        }

        public String getSaveFoodName(int position){
            return arrayListFoodName.get(position);
        }

        public String getSaveFoodCal(int position){
            return arrayListCal.get(position);
        }

        public String getSaveFoodFat(int position){
            return arrayListFat.get(position);
        }
    }
}
