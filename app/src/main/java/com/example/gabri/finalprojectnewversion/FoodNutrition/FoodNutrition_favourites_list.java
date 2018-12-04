package com.example.gabri.finalprojectnewversion.FoodNutrition;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
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
import android.widget.Toast;

import com.example.gabri.finalprojectnewversion.R;

import java.util.ArrayList;

import javax.xml.transform.Result;

public class FoodNutrition_favourites_list extends AppCompatActivity {
/*
those are class veriable
 */
    FoodDatabaseHelp myDB;
    private SQLiteDatabase foodDB;
    public  Button AverageCal;
    public  Button TotalCal;
    public  Button MinimumCal;
    public  Button MaximumCal;
    private TextView nutritionInfo;
    private Cursor cursor;
    private ListView foodListView;
/*
create array to hold the food name and values
 */
    ArrayList<String> arrayListFoodName = new ArrayList<>();
    ArrayList<Integer> arrayListId = new ArrayList<>();
    ArrayList<String> arrayListCal = new ArrayList<>();
    ArrayList<String> arrayListFat = new ArrayList<>();
    ListViewSavedFoods adapter;
/*
this is onCreate method , it resopnsible to create the activity, and put initialization code,
and use the super class method
 */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_nutrition_favourites_list);

        AverageCal = findViewById(R.id.average_calories); //to find the averageCal button
        TotalCal = findViewById(R.id.total_calories);  // to find the totalCal button
        MinimumCal = findViewById(R.id.Calminimum);   // to find the minimumCal button
        MaximumCal = findViewById(R.id.Calmaximum);   // to find the maximumCal
        nutritionInfo = findViewById(R.id.nutrition_info);
/*
use on click method to  find TotalCal when user click and bring to the next page
 */
        TotalCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Double calT = getStatistic()[0]; //ToTAL CAL
                String calT1 = "Total Cal: "+calT.toString();
                nutritionInfo.setText(calT1);
            }
        });
/*
use on click method to  find AverageCal when user click and bring to the next page
 */
        AverageCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Double calA = getStatistic()[3];// Average CAL
                String calA1 = "Average Cal: " +calA.toString();
                nutritionInfo.setText(calA1);
            }
        });
/*
use on click method to  find MaximumCal when user click and bring to the next page
 */
        MaximumCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Double calMa = getStatistic()[1];// Max CAL
                String calMa1 = "Max Cal: " +calMa.toString();
                nutritionInfo.setText(calMa1);
            }
        });
/*
use on click method to  find MinimumCal when user click and bring to the next page
 */
        MinimumCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Double calMin = getStatistic()[2];// Min CAL
                String calMin1 = "Min Cal: " +calMin.toString();
                nutritionInfo.setText(calMin1);
            }
        });


        myDB = new FoodDatabaseHelp(this); // create a new object
        foodDB = myDB.getWritableDatabase();

        getSavedFood();

    }
/*
This getSavedFood method is using arrayList to hold the values afetr user to search for
food name , food Id, food calories, and fat
 */
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
/*
ListViewsavedFoods is a sub class, it will save all of the values from user saved
 */
    class ListViewSavedFoods extends ArrayAdapter<String>{

        public ListViewSavedFoods(Context context) {
            super(context, 0);
        }



/*
the getView method will get the view object in food-nutrition_favourites_list
 */
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

                    nutritionInfo.setText("");
                }


            });
/*
use on click method to  find insert the values when user click and bring to the next page
 */

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

/*
use getCount method to hold the number of values
 */
        public int getCount(){
            return arrayListFoodName.size();
        }
    /*
    use getSaveFoodName method to hold the foodName
     */
        public String getSaveFoodName(int position){
            return arrayListFoodName.get(position);
        }
    /*
    use getSaveFoodCal method to hold the calories of values
     */
        public String getSaveFoodCal(int position){
            return arrayListCal.get(position);
        }
    /*
    use getSaveFoodFat method to hold the fat of values
     */
        public String getSaveFoodFat(int position){
            return arrayListFat.get(position);
        }
    }

/*
the method to calculat of total calories, Average calories, maximum calories, minimum calories
 */
    private double[] getStatistic(){
        double[] calInfo = new double[4];
        double max = Double.MIN_VALUE;
        double min = Double.MAX_VALUE;
        double sum = 0;
        cursor = foodDB.query(FoodDatabaseHelp.FOODNUTRITIONRESULT_TABLE_NAME, new String[]{FoodDatabaseHelp.Key_ID, FoodDatabaseHelp.Key_CALORIES}, null, null, null, null, null);
        if (cursor.moveToFirst()!=false){
            while (!cursor.isAfterLast()) {
                Double cal = cursor.getDouble(cursor.getColumnIndex(FoodDatabaseHelp.Key_CALORIES));
                max = cal > max ? cal : max;
                min = cal < min ? cal : min;
                sum += cal;
                cursor.moveToNext();
            }
            double avg = sum / cursor.getCount();
            calInfo[0]=sum;
            calInfo[1]=max;
            calInfo[2]=min;
            calInfo[3]=avg;
}

        else{
            calInfo[0]=0.0;
            calInfo[1]=0.0;
            calInfo[2]=0.0;
            calInfo[3]=0.0;}
        return calInfo; // will get all of information
    }
}
