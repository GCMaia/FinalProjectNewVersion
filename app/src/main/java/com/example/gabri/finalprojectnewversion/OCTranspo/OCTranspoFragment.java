package com.example.gabri.finalprojectnewversion.OCTranspo;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gabri.finalprojectnewversion.R;

/**
 * Class used to create the fragment that is used after selecting a bus
 */
public class OCTranspoFragment extends android.app.Fragment {
    private Bundle runningBundle;
    private Context parent;
    private String nameFinalStation;
    private String busStopNumber;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        runningBundle = this.getArguments();

    }

    /**
     *  method used to create the view that's is going to be used on the fragment
     * @param inflater layout that's is going to be inflated
     * @param container
     * @param savedInstanceState
     * @return view being used
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.activity_octranspo_fragment,container,false);
        TextView busDestination = result.findViewById(R.id.OCTranspoDestination);
        final String routeNumber;
        final String origin;


        Button saveButton = result.findViewById(R.id.OCTranspoSaveButton);

        nameFinalStation = runningBundle.getString("busDestination");
        routeNumber = runningBundle.getString("busRouteNo");
        busStopNumber = runningBundle.getString("stopNumber");



        busDestination.setText(String.format(getResources().getString(R.string.hasBuses), routeNumber, nameFinalStation));

        origin = runningBundle.getString("origin");
        if (origin==null){
            saveButton.setVisibility(View.VISIBLE);
        }else{
            saveButton.setVisibility(View.GONE);
        }


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OCTranspoDatabase helperDatabase = new OCTranspoDatabase(parent);
                SQLiteDatabase db = helperDatabase.getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                contentValues.put(OCTranspoDatabase.KEY_BUS_NAME, nameFinalStation);
                contentValues.put(OCTranspoDatabase.KEY_BUS_NUM, routeNumber);
                contentValues.put(OCTranspoDatabase.KEY_BUS_STOP, busStopNumber);
                db.insert(OCTranspoDatabase.TABLE_NAME, null, contentValues);
                db.close();
                Toast toast = Toast.makeText(parent, R.string.OCTranspoSavedBusList, Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        return result;

    }

    /**
     * attaches the activity context
     * @param context activity context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        runningBundle = this.getArguments();
        parent = context;
    }





}
