package com.example.gabri.finalprojectnewversion.OCTranspo;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.gabri.finalprojectnewversion.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Class used for getting bus specific information to be presented on the fragment class
 */
public class BusFragmentDetails extends AppCompatActivity {



    String routeNumber;
    String stopNumber;

    ArrayList<String> tempSpeedArray = new ArrayList<>();
    String tempSpeed;
    ArrayList<String> tempLatitudeArray = new ArrayList<>();
    String tempLatitude;
    ArrayList<String> tempLongitudeArray = new ArrayList<>();
    String tempLongitude;
    ArrayList<String> tempStartArray = new ArrayList<>();
    String tempStart;
    ArrayList<String> tempLateArray = new ArrayList<>();
    String tempLate;

    /**
     * starts the query and saves the information that needs to be passed for the next screen
     */
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_fragment_details);

        Intent i = getIntent();


        BusDetailQuery query = new BusDetailQuery();
        query.execute();

        routeNumber = i.getStringExtra("busRouteNo");
        stopNumber = i.getStringExtra("stopNumber");

        OCTranspoFragment mf = new OCTranspoFragment();
        Bundle fragmentArgs = new Bundle();

        fragmentArgs.putString("busDestination", i.getStringExtra("busDestination")  );
        fragmentArgs.putString("stopNumber", i.getStringExtra("stopNumber"));
        fragmentArgs.putString("busRouteNo", i.getStringExtra("busRouteNo"));
        fragmentArgs.putString("origin", i.getStringExtra("origin"));
        mf.setArguments(fragmentArgs);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.empty_frame, mf);
        ft.commit();
    }

    /**
     * inner class used to make the query on the 2nd part of the API, for the specific bus
     */
    class BusDetailQuery extends AsyncTask<String, Integer, String> {

        final String OCKEY = "0fe84df9e187b080fa03ca6114c05047";
        final String APPID = "6ded7c88";

        /**
         * method used for making the research for the specific requested bus
         */
        @Override
        protected String doInBackground(String... strings) {

            String web = "https://api.octranspo1.com/v1.2/GetNextTripsForStop?appID=" + APPID +
                    "&&apiKey="+ OCKEY + "&routeNo=" + routeNumber + "&stopNo=" + stopNumber;

            //https://api.octranspo1.com/v1.2/GetNextTripsForStop?appID=6ded7c88&&apiKey=0fe84df9e187b080fa03ca6114c05047&routeNo=111&stopNo=3017
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
                        if (xpp.getName().equals("GPSSpeed")) {
                            xpp.next();
                            tempSpeedArray.add(xpp.getText());
                        }
                        else if (xpp.getName().equals("TripStartTime")){
                            xpp.next();
                            tempStartArray.add(xpp.getText());
                        }
                        else if (xpp.getName().equals("AdjustedScheduleTime")){
                            xpp.next();
                            tempLateArray.add(xpp.getText());
                        }
                        else if (xpp.getName().equals("Latitude")){
                            xpp.next();
                            tempLatitudeArray.add(xpp.getText());
                        }
                        else if (xpp.getName().equals("Longitude")){
                            xpp.next();
                            tempLongitudeArray.add(xpp.getText());
                        }
                    }
                    xpp.next();
                }
            }
            catch (Exception e){

            }
            return null;
        }

        /**
         * method called after the end of the Async task, responsible for showing the bus information in the screen
         */
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            TextView speedView = findViewById(R.id.OCTRanspoSpeed);
            speedView.setText(String.format(getResources().getString(R.string.OCTranspoSpeed), tempSpeedArray.get(0)));

            TextView latitudeView = findViewById(R.id.OCTranspoLatitude);
            latitudeView.setText(String.format(getResources().getString(R.string.OCTranspoLatitude), tempLatitudeArray.get(0)));

            TextView longitudeView = findViewById(R.id.OCTranspoLongitude);
            longitudeView.setText(String.format(getResources().getString(R.string.OCTranspoLongitude), tempLongitudeArray.get(0)));

            TextView startView = findViewById(R.id.OCTranspoStartTime);
            startView.setText(String.format(getResources().getString(R.string.OCTranspoStartTime), tempStartArray.get(0)));

            TextView lateView = findViewById(R.id.OCTranspoLateTime);
            lateView.setText(String.format(getResources().getString(R.string.OCTranspoDelayedTime), tempLateArray.get(0)));

            TextView averageDelayedTime = findViewById(R.id.averageDelayedTime);
            int sum = Integer.parseInt(tempLateArray.get(0)) + Integer.parseInt(tempLateArray.get(1)) + Integer.parseInt(tempLateArray.get(2));
            int result = sum/3;
            averageDelayedTime.setText(String.format(getResources().getString(R.string.averagedelayedtime), Integer.toString(result)));

        }
    }
}

