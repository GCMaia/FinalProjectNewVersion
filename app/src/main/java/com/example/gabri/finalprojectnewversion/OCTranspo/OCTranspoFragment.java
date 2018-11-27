package com.example.gabri.finalprojectnewversion.OCTranspo;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.gabri.finalprojectnewversion.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class OCTranspoFragment extends android.app.Fragment {
    private TextView busDestination;

    private TextView busLatitudeLongitude;
    private TextView busSpeed;
    private TextView busStartTime;
    private TextView busLateTime;



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
        busDestination = result.findViewById(R.id.OCTranspoDestination);

        String stopNumber;
        String routeNumber;

        saveButton = result.findViewById(R.id.OCTranspoSaveButton);


        stopNumber = runningBundle.getString("stopNumber");
        nameFinalStation = runningBundle.getString("busDestination");
        routeNumber = runningBundle.getString("busRouteNo");
        busDestination.setText(routeNumber + " - " + nameFinalStation);
        return result;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        runningBundle = this.getArguments();
        parent = context;
    }




}
