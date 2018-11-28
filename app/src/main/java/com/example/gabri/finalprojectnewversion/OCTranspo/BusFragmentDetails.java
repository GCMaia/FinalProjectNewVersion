package com.example.gabri.finalprojectnewversion.OCTranspo;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.gabri.finalprojectnewversion.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class BusFragmentDetails extends AppCompatActivity {

    String routeNumber;
    String stopNumber;
    String tempSpeed;
    String tempLatitude;
    String tempLongitude;
    String tempStart;
    String tempLate;

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

        mf.setArguments(fragmentArgs);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.empty_frame, mf);
        ft.commit();
    }

    class BusDetailQuery extends AsyncTask<String, Integer, String> {

        final String OCKEY = "0fe84df9e187b080fa03ca6114c05047";
        final String APPID = "6ded7c88";



        @Override
        protected String doInBackground(String... strings) {

            String web = "https://api.octranspo1.com/v1.2/GetNextTripsForStop?appID=" + APPID +
                    "&&apiKey="+ OCKEY + "&routeNo=" + routeNumber + "&stopNo=" + stopNumber;

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
                            tempSpeed = xpp.getText();
                        }
                        else if (xpp.getName().equals("TripStartTime")){
                            xpp.next();
                            tempStart = xpp.getText();
                        }
                        else if (xpp.getName().equals("AdjustedScheduleTime")){
                            xpp.next();
                            tempLate = xpp.getText();
                        }
                        else if (xpp.getName().equals("Latitude")){
                            xpp.next();
                            tempLatitude = xpp.getText();
                        }
                        else if (xpp.getName().equals("Longitude")){
                            xpp.next();
                            tempLongitude = xpp.getText();
                        }
                    }
                    else if (xpp.getEventType() == XmlPullParser.END_TAG){
                        if (xpp.getName().equals("Trip")){
                            break;
                        }
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

            TextView speedView = findViewById(R.id.OCTRanspoSpeed);
            speedView.setText(String.format(getResources().getString(R.string.OCTranspoSpeed), tempSpeed));

            TextView latitudeView = findViewById(R.id.OCTranspoLatitude);
            latitudeView.setText(String.format(getResources().getString(R.string.OCTranspoLatitude), tempLatitude));

            TextView longitudeView = findViewById(R.id.OCTranspoLongitude);
            longitudeView.setText(String.format(getResources().getString(R.string.OCTranspoLongitude), tempLongitude));

            TextView startView = findViewById(R.id.OCTranspoStartTime);
            startView.setText(String.format(getResources().getString(R.string.OCTranspoStartTime), tempStart));

            TextView lateView = findViewById(R.id.OCTranspoLateTime);
            lateView.setText(String.format(getResources().getString(R.string.OCTranspoDelayedTime), tempLate));

        }
    }
}

