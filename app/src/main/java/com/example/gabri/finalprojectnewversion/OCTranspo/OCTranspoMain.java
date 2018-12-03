package com.example.gabri.finalprojectnewversion.OCTranspo;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gabri.finalprojectnewversion.CBCNews.CBCNewsMain;
import com.example.gabri.finalprojectnewversion.FoodNutrition.FoodNutritionMain;
import com.example.gabri.finalprojectnewversion.Movie.MovieInformationMain;
import com.example.gabri.finalprojectnewversion.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Class developed to give the information about the buses used by OCTranspo, showing basic information
 * such as bus number, to more complicated information
 * @author Gabriel Cardoso Maia
 * @since November, 08/2018
 */
public class OCTranspoMain extends AppCompatActivity {


    final ArrayList<String> busInfoHeader = new ArrayList<>();
    final ArrayList<String> busInfoNo = new ArrayList<>();

    ListViewClassesAdapter messageAdapter;

    Button searchButton;
    EditText editText;
    String stopNumber = "";
    String tempRouteNo = "";
    String tempDestination = "";
    String stopName = "";

    SharedPreferences preferences;

    /**
     * method onCreate used for calling the elements being used from the referenced XML, and starts query under the required circumstances
     * @param savedInstanceState checks if needs to restore themselves to a previous state using the data stored in this bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_octranspo_main);

        ProgressBar loader = findViewById(R.id.loader);
        loader.setVisibility(View.INVISIBLE);

        final ListView listview = findViewById(R.id.searchList);
        messageAdapter = new ListViewClassesAdapter(this);
        listview.setAdapter(messageAdapter);
        Toolbar toolbar = findViewById(R.id.oc_toolbar);
        toolbar.setTitle(R.string.OCtranspo);
        setSupportActionBar(toolbar);

        preferences = getSharedPreferences("savedStopNumber", MODE_PRIVATE);


        searchButton = findViewById(R.id.searchButton);
        editText = findViewById(R.id.searchText);

        String tempStopNumber = preferences.getString("stop", "empty");
        if (!tempStopNumber.equals("empty")){
            stopNumber = tempStopNumber;
            OCQuery query = new OCQuery();
            query.execute();
        }

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent fragmentPiece = new Intent(OCTranspoMain.this, BusFragmentDetails.class);
                fragmentPiece.putExtra("busDestination", busInfoHeader.get(position));
                fragmentPiece.putExtra("busRouteNo", busInfoNo.get(position));
                fragmentPiece.putExtra("stopNumber",stopNumber);

                startActivityForResult(fragmentPiece, 1);



            }
        });
        
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().toString().equals("") || editText.getText().toString().length() <= 3){
                    Toast toast = Toast.makeText(OCTranspoMain.this, "invalid stop number", Toast.LENGTH_SHORT);
                    toast.show();
                }else {
                    ProgressBar loader = findViewById(R.id.loader);
                    loader.setVisibility(View.VISIBLE);
                    stopNumber = editText.getText().toString();

                    SharedPreferences.Editor preferencesEdit = preferences.edit();
                    preferencesEdit.putString("stop", stopNumber);
                    preferencesEdit.apply();

                    OCQuery query = new OCQuery();
                    query.execute();
                    editText.setText("");
                    messageAdapter.notifyDataSetChanged();
                    busInfoHeader.clear();
                    busInfoNo.clear();

                }
            }
        });
    }

    /**
     *  class used to control the listView used by the OCTranspoMain class
     */
    class ListViewClassesAdapter extends ArrayAdapter<String> {

        ListViewClassesAdapter(Context context) {
            super(context, 0);
        }

        /**
         * gets the size of the busInfoNo array
         * @return array size
         */
        public int getCount() {
            return busInfoNo.size();
        }

        /**
         * gets the element from the busInfoNo array
         * @param position "id" of the array element
         * @return array element
         */
        String getBusNo(int position) {
            return busInfoNo.get(position);
        }

        /**
         * gets the element from the busInfoHeader array
         * @param position "id" of the array element
         * @return array element
         */
        String getBusHeading(int position){
            return busInfoHeader.get(position);
        }

        @NonNull
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = OCTranspoMain.this.getLayoutInflater();
            View result = inflater.inflate(R.layout.bus_options, null);
            TextView route = result.findViewById(R.id.busNo);
            TextView heading = result.findViewById(R.id.routeHeading);
            route.setText(getBusNo(position));
            heading.setText(getBusHeading(position));
            return result;
        }
    }



    /**
     * Class used to run the required query to go on the provided API and get the required information
     */
    class OCQuery extends AsyncTask<String, Integer, String> {


        final String OCKEY = "0fe84df9e187b080fa03ca6114c05047";
        final String APPID = "6ded7c88";

        String web = "https://api.octranspo1.com/v1.2/GetRouteSummaryForStop?appID=" + APPID +
                "&&apiKey="+ OCKEY + "&stopNo=" + stopNumber;

        @Override
        protected String doInBackground(String... strings) {

            try {
                URL url = new URL(web);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                InputStream input = conn.getInputStream();

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(input, "UTF-8");


                while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
                    if (xpp.getEventType() == XmlPullParser.START_TAG) {
                        if (xpp.getName().equals("RouteNo")) {
                            xpp.next();
                            tempRouteNo = xpp.getText();
                        }
                        else if (xpp.getName().equals("RouteHeading")){
                            xpp.next();
                            tempDestination = xpp.getText();
                        }
                        else if (xpp.getName().equals("StopDescription")){
                            xpp.next();
                            stopName = xpp.getText();
                        }
                    }
                    else if (!tempDestination.equals("") && !tempRouteNo.equals("")){
                        busInfoNo.add(tempRouteNo);
                        busInfoHeader.add(tempDestination);
                        tempRouteNo = "";
                        tempDestination = "";
                    }
                    xpp.next();
                }
            }
            catch (Exception e){

            }
            return null;
        }

        @SuppressLint("StringFormatInvalid")
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            TextView stop = findViewById(R.id.stopName);


            if (busInfoNo.size() == 0){
                stop.setText(R.string.noBuses);
            } else {
                stop.setText(String.format(getResources().getString(R.string.hasBuses), stopNumber, stopName));
            }

            ProgressBar loader = findViewById(R.id.loader);
            loader.setVisibility(View.INVISIBLE);

            Snackbar snackbar = Snackbar.make(searchButton, R.string.ocQuery, Snackbar.LENGTH_SHORT);
            snackbar.show();
        }
    }


    /**
     *  method used to invoke the toolbar being used at the main activity
     * @param menu menu going to be used
     * @return the toolbar
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_octranspo_appbar,menu);
        return true;
    }


    /**
     * method used to invoke the menu options being used at the toolbar
     * @param menuItem toolbar element
     * @return the toolbar items
     */
    public boolean onOptionsItemSelected(MenuItem menuItem){

        switch (menuItem.getItemId()){
            case R.id.action_one:

                AlertDialog.Builder CBCBuilder = new AlertDialog.Builder(OCTranspoMain.this);
                CBCBuilder.setMessage(R.string.questionCBC).setTitle(R.string.CBCTitle)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(OCTranspoMain.this, CBCNewsMain.class);
                                startActivity(intent);
                            }
                        }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) { }
                }).show();

                break;
            case R.id.action_two:

                AlertDialog.Builder movieBuilder = new AlertDialog.Builder(OCTranspoMain.this);
                movieBuilder.setMessage(R.string.questionMovies).setTitle(R.string.MoviesTitle)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(OCTranspoMain.this, MovieInformationMain.class);
                                startActivity(intent);
                            }
                        }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) { }
                }).show();

                break;

            case R.id.action_three:

                AlertDialog.Builder foodBuilder = new AlertDialog.Builder(OCTranspoMain.this);
                foodBuilder.setMessage(R.string.questionNutrition).setTitle(R.string.NutritionTitle)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(OCTranspoMain.this, FoodNutritionMain.class);
                                startActivity(intent);
                            }
                        }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) { }
                }).show();


                break;
            case R.id.action_four:

                Toast aboutToast = Toast.makeText(OCTranspoMain.this, R.string.OCTranspoAbout, Toast.LENGTH_SHORT);
                aboutToast.show();
                break;

            case R.id.action_five:

                AlertDialog.Builder helpBuilder = new AlertDialog.Builder(OCTranspoMain.this);
                helpBuilder.setTitle(R.string.OCTranspoHelp).setMessage(R.string.OCTranspoHelpGuide).show();
                break;
            case R.id.action_six:

                AlertDialog.Builder savedBuilder = new AlertDialog.Builder(OCTranspoMain.this);
                savedBuilder.setMessage("accept will take you to your saved buses page")
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(OCTranspoMain.this, OCTranspoSavedBuses.class);
                                startActivity(intent);
                            }
                        }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                }).show();
                break;

        }
        return true;
    }
}
