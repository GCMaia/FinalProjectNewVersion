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

public class OCTranspoFragment extends android.app.Fragment {
    private TextView busDestination;

    private TextView busLatitudeLongitude;
    private TextView busSpeed;
    private TextView busStartTime;
    private TextView busLateTime;



    private Button saveButton;
    private Button backButton;
    private Bundle runningBundle;
    private Context parent;


    private String stopNumber;
    private String routeNumber;
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



    class BusDetailQuery extends AsyncTask<String, Integer, String>{

        final String OCKEY = "0fe84df9e187b080fa03ca6114c05047";
        final String APPID = "6ded7c88";

       String web = "https://api.octranspo1.com/v1.2/GetRouteSummaryForStop?appID=" + APPID +
                "&&apiKey="+ OCKEY + "&routeNo=" + routeNumber + "&stopNo=" + stopNumber;


        @Override
        protected String doInBackground(String... strings) {
            return null;
        }
    }
}
