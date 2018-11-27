package com.example.gabri.finalprojectnewversion.OCTranspo;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.gabri.finalprojectnewversion.R;

public class BusFragmentDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_fragment_details);

        Intent i = getIntent();

        OCTranspoFragment mf = new OCTranspoFragment();
        Bundle fragmentArgs = new Bundle();

        fragmentArgs.putString("busDestination", i.getStringExtra("busDestination")  );
        fragmentArgs.putString("stopNumber", i.getStringExtra("stopNumber"));
        fragmentArgs.putString("busRouteNo", i.getStringExtra("busRouteNo"));

        mf.setArguments(fragmentArgs);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.empty_frame, mf);
        ft.commit();
    }
}
