package com.example.gabri.finalprojectnewversion.OCTranspo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.gabri.finalprojectnewversion.R;

import java.util.ArrayList;

public class OCTranspoSavedBuses extends AppCompatActivity {

    ListViewSavedBusesClassesAdapter messageAdapter;
    OCTranspoDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_octranspo_saved_buses);

        database = new OCTranspoDatabase(this);




        final ListView listview = findViewById(R.id.searchList);
        messageAdapter = new ListViewSavedBusesClassesAdapter(this);
        listview.setAdapter(messageAdapter);
        messageAdapter.notifyDataSetChanged();

    }


    class ListViewSavedBusesClassesAdapter extends ArrayAdapter<String> {

        ListViewSavedBusesClassesAdapter(Context context) {
            super(context, 0);
        }


        @NonNull
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = OCTranspoSavedBuses.this.getLayoutInflater();
            View result = inflater.inflate(R.layout.bus_saved_options, null);
            Button deleteButton = result.findViewById(R.id.OCTrasnpoRemoveButton);

            ArrayList<String> elements = database.getAllSavedBuses();
            String numHelper = elements.get(0);
            String nameHelper = elements.get(1);


            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            return result;
        }
    }
}
