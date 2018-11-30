package com.example.gabri.finalprojectnewversion.OCTranspo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
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

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class OCTranspoFragment extends android.app.Fragment {
    private Button saveButton;
    private Bundle runningBundle;
    private Context parent;



    private String nameFinalStation;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        runningBundle = this.getArguments();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.activity_octranspo_fragment,container,false);
        TextView busDestination = result.findViewById(R.id.OCTranspoDestination);
        final String routeNumber;

        saveButton = result.findViewById(R.id.OCTranspoSaveButton);




        nameFinalStation = runningBundle.getString("busDestination");
        routeNumber = runningBundle.getString("busRouteNo");
        busDestination.setText(String.format(getResources().getString(R.string.hasBuses), routeNumber, nameFinalStation));

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OCTranspoDatabase helperDatabase = new OCTranspoDatabase(parent);
                SQLiteDatabase db = helperDatabase.getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                contentValues.put(OCTranspoDatabase.KEY_BUS_NAME, nameFinalStation);
                contentValues.put(OCTranspoDatabase.KEY_BUS_NUM, routeNumber);
                db.insert(OCTranspoDatabase.TABLE_NAME, null, contentValues);
                db.close();
                Toast toast = Toast.makeText(parent, "saved on your bus list", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        return result;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        runningBundle = this.getArguments();
        parent = context;
    }





}
