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
        fragmentArgs.putLong("ID",   i.getLongExtra("ID", 99)   );
        fragmentArgs.putString("message", i.getStringExtra("message")  );
        fragmentArgs.putBoolean("phone", true);
        mf.setArguments(fragmentArgs);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.empty_frame, mf);
        ft.commit();
    }
}
