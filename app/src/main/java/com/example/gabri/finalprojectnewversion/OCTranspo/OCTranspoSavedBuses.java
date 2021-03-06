package com.example.gabri.finalprojectnewversion.OCTranspo;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gabri.finalprojectnewversion.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * class used to show the user all the buses saved while using the application
 * @author Gabriel Cardoso Maia
 * @since November, 08/2018
 */
public class OCTranspoSavedBuses extends AppCompatActivity {

    /**
     * attributes declaration for holding the information that comes from the database
     */
    ListViewSavedBusesClassesAdapter messageAdapter;
    OCTranspoDatabase database;
    ArrayList<String> busesName;
    ArrayList<String> busesNumber;
    ArrayList<Integer> busesIDs;
    ArrayList<String> busesStopNumber;
    ListView listview;

    /**
     * method used to start the activity, and call the getSavedBuses method and initialize the class arrays
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_octranspo_saved_buses);

        database = new OCTranspoDatabase(this);
        busesIDs = new ArrayList<>();
        busesName = new ArrayList<>();
        busesNumber = new ArrayList<>();
        busesStopNumber = new ArrayList<>();
        getSavedBuses();


    }

    /**
     * method used to get all the buses that need to be shown at the saved screen
     */
    private void getSavedBuses(){
        Cursor cursor = database.getAllSavedBusesTest();

        if (cursor.getCount() == 0){
            Toast toast = Toast.makeText(this, R.string.noBuses, Toast.LENGTH_SHORT);
            toast.show();
        } else {
            while (cursor.moveToNext()){
                busesIDs.add(cursor.getInt(0));
                busesName.add(cursor.getString(1));
                busesNumber.add(cursor.getString(2));
                busesStopNumber.add(cursor.getString(3));
            }
            messageAdapter = new ListViewSavedBusesClassesAdapter(this);
            listview = findViewById(R.id.searchListSavedBuses);
            listview.setAdapter(messageAdapter);
            messageAdapter.notifyDataSetChanged();

        }
    }

    /**
     * inner class used to adapat the information from the arrays to the listview being used in this activity
     */
    class ListViewSavedBusesClassesAdapter extends ArrayAdapter<String> {

        ListViewSavedBusesClassesAdapter(Context context) {
            super(context, 0);
        }


        @NonNull
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = OCTranspoSavedBuses.this.getLayoutInflater();
            View result = inflater.inflate(R.layout.bus_saved_options, null);
            Button deleteButton = result.findViewById(R.id.OCTrasnpoRemoveButton);
            TextView savedBusName = result.findViewById(R.id.savedRouteHeading);
            TextView savedBusNo = result.findViewById(R.id.savedBusNo);
            savedBusName.setText(getSavedBusHeading(position));
            savedBusNo.setText(getSavedBusNo(position));
            final TextView hasBus = findViewById(R.id.savedBusesNoSavedBusFound);


            if (busesNumber.size() != 0){
                hasBus.setText("");
            }


            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    OCTranspoDatabase helperDatabase = new OCTranspoDatabase(getContext());
                    SQLiteDatabase db = helperDatabase.getWritableDatabase();
                    db.delete(OCTranspoDatabase.TABLE_NAME, OCTranspoDatabase.KEY_ID+" = ?",new String[]{Integer.toString( busesIDs.get(position) )});

                    busesIDs.remove(position);
                    busesName.remove(position);
                    busesNumber.remove(position);

                    if (busesNumber.size() == 0){
                        hasBus.setText(R.string.noBuses);
                    }

                    messageAdapter.notifyDataSetChanged();

                }

            });

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent fragmentPiece = new Intent(OCTranspoSavedBuses.this, BusFragmentDetails.class);
                    fragmentPiece.putExtra("busDestination", busesName.get(position));
                    fragmentPiece.putExtra("busRouteNo", busesNumber.get(position));
                    fragmentPiece.putExtra("stopNumber", busesStopNumber.get(position));
                    fragmentPiece.putExtra("origin", "saved");
                    startActivityForResult(fragmentPiece, 2);
                }
            });



            return result;
        }


        /**
         * gets the count of the arraylist "busesName"
         * @return the size
         */
        public int getCount() {
            return busesName.size();
        }

        /**
         * gets the specific item on the busesName array
         * @param position where the item/element is
         * @return the item/element
         */
        String getSavedBusHeading(int position) {
            return busesName.get(position);
        }

        /**
         * gets the specific item on the busesNumber array
         * @param position where the item/element is
         * @return the item/element
         */
        String getSavedBusNo(int position){
            return busesNumber.get(position);
        }
    }
}
