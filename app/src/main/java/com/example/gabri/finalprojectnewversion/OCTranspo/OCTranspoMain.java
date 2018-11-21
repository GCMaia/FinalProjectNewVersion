package com.example.gabri.finalprojectnewversion.OCTranspo;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

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


        searchButton = findViewById(R.id.searchButton);
        editText = findViewById(R.id.searchText);



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
                    OCQuery query = new OCQuery();
                    query.execute();
                    editText.setText("");
                    messageAdapter.notifyDataSetChanged();
                    busInfoHeader.clear();
                    busInfoNo.clear();

                    Snackbar snackbar = Snackbar.make(searchButton, "Query Completed", Snackbar.LENGTH_SHORT);
                    snackbar.show();
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

        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            TextView stop = findViewById(R.id.stopName);
            stop.setText(stopNumber);

            ProgressBar loader = findViewById(R.id.loader);
            loader.setVisibility(View.INVISIBLE);

        }
    }

}
