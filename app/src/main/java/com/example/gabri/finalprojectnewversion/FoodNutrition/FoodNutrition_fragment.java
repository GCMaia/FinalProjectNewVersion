package com.example.gabri.finalprojectnewversion.FoodNutrition;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.gabri.finalprojectnewversion.R;

public class FoodNutrition_fragment extends Fragment {

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

    public static final String Average_Calories = "AverageCal";
    public static final String Total_Calories = "TotalCal";
    public static final String Minimum_Calories = "MinimumCal";
    public static final String Maximum_Calories = "MaximumCal";

    public FoodNutrition_fragment(){

    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        resultBundle = this.getArguments();
//
//
//       // id = resultBundle.getLong("resultId");
//        msg = resultBundle.getString("foodType");
//        isTablet = resultBundle.getBoolean("isTablet");
//    }
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
//        View view = inflater.inflate(R.layout.activity_food_nutrition_fragment, container, false);
//        //resultId = view.findViewById(R.id.textView);
//        foodName = view.findViewById(R.id.foodName);
//        //resultId.setText("ID = " + Long.toString(id));
//        foodName.setText("FoodType: + foodName");
//        deleteResult = view.findViewById(R.id.button_delete);
//        deleteResult.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                if (isTablet) {
//                    parent = (FoodNutritionSearchResult) getActivity();
//
//                    getFragmentManager().popBackStack();
//                }
//                else {
//                    Intent i = new Intent();
//                    i.putExtra("deleteid", id);
//                    getActivity().setResult(Activity.RESULT_OK, i);
//                    getActivity().finish();
//                }
//            }
//        });
//
//
//        AverageCal = view.findViewById(R.id.average_calories);
//        AverageCal.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                cursor = db.rawQuery("select * from" + foodDatabaseHelp.getAllResults(),null);
//                total =0.00;
//
//
////            }
////        });
//
//
//        saveButton.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.putExtra("saveInfo", getId());
//                getActivity().setResult(Activity.RESULT_OK, intent);
//                getActivity().finish();
//            }
//        });
//
//
//
//
//        return view;
//        cancelButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
//                builder.setTitle(R.string.cancel_button_dialog);
//                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        String phone = null;
//
//                        if (phone == true) getActivity().finish();
//                        else
//                            getActivity().getFragmentManager().beginTransaction().remove(NutritionDetails.this).commit();
//                    }
//                });
//                builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                    }
//                });
//                AlertDialog dialog = builder.create();

//
}
//
//
//
