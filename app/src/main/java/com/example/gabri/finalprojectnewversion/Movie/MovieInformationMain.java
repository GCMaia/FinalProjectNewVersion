package com.example.gabri.finalprojectnewversion.Movie;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gabri.finalprojectnewversion.CBCNews.CBCNewsMain;
import com.example.gabri.finalprojectnewversion.FoodNutrition.FoodNutritionMain;
import com.example.gabri.finalprojectnewversion.OCTranspo.OCTranspoMain;
import com.example.gabri.finalprojectnewversion.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class MovieInformationMain extends AppCompatActivity {
    protected static final String ACTIVITY_NAME = "MovieInfoActivity";
    ArrayList<String> movieList =new ArrayList<>();
    ArrayList<Bitmap> posterList=new ArrayList<>();
    String title="";
    ProgressBar progressBar;
    ListView listView;
    Button search;
    MovieAdapter movieAdapter;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_information_main);

        listView=findViewById(R.id.movieList);
        search = findViewById(R.id.search);

        movieAdapter=new MovieAdapter(this);
        progressBar=findViewById(R.id.progress);
        progressBar.setVisibility(View.INVISIBLE);
        editText=findViewById(R.id.enterMovie);
        listView.setAdapter(movieAdapter);
        Toolbar movieToolBar=findViewById(R.id.movie_toolbar);
        setSupportActionBar(movieToolBar);

        search.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                if (editText.getText().toString().equals("")){
                    Toast.makeText(MovieInformationMain.this, "Cannot Find That Movie", Toast.LENGTH_LONG).show();
                }else {
                    progressBar.setVisibility(View.VISIBLE);
                    title=editText.getText().toString();
                    MovieQuery movieQuery = new MovieQuery();
                    movieQuery.execute();
                    editText.setText("");
                    movieAdapter.notifyDataSetChanged();
                    Snackbar.make(search, "All Movies Searched", Snackbar.LENGTH_LONG).show();
                    //movieList.clear();
                }



            }
        });

    }
    private class MovieAdapter extends ArrayAdapter<String>{
        public MovieAdapter(Context ctx){
            super(ctx,0);
        }
        public int getCount(){
            return movieList.size();
        }
        public String getMovie(int position){
            return movieList.get(position);
        }
        public Bitmap getPoster(int position){ return posterList.get(position);}
        public View getView(int position, View convertView, ViewGroup parent){
            LayoutInflater inflater=MovieInformationMain.this.getLayoutInflater();
            View result;
            result=inflater.inflate(R.layout.movie_item,null);
            TextView movieTitle=result.findViewById(R.id.movieTitle);
            movieTitle.setText(getMovie(position));
//            ImageView poster=result.findViewById(R.id.poster);
//            posterList.add(getPoster(position));
            return result;
        }
        public long getItemId(int position){
            return position;
        }
    }

    private class MovieQuery extends AsyncTask<String, Integer,String> {
        String title1, year,rating, runtime, actors, plot, poster ;
        String apiKey="c47e9b47";
        Bitmap picture;
        @Override
        protected String doInBackground(String... strings) {
            //http://www.omdbapi.com/?apikey=c47e9b47&r=xml&t=titanic

            try{
                String web = "http://www.omdbapi.com/?apikey="+ apiKey+ "&r=xml&t="+URLEncoder.encode(title,"UTF-8");
                URL url=new URL(web);
                HttpURLConnection conn=(HttpURLConnection) url.openConnection();
                InputStream input=conn.getInputStream();

                XmlPullParserFactory factory=XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp=factory.newPullParser();
                xpp.setInput(input,"UTF-8");

                while (xpp.getEventType()!=XmlPullParser.END_DOCUMENT) {
                    if (xpp.getEventType()==XmlPullParser.START_TAG) {
                            if (xpp.getName().equals("movie")) {
                                xpp.next();
                                title1 = xpp.getAttributeValue(null, "title");
                                year = xpp.getAttributeValue(null, "year");
                                rating=xpp.getAttributeValue(null,"rated");
                                runtime=xpp.getAttributeValue(null,"runtime");
                                actors=xpp.getAttributeValue(null,"actors");
                                plot=xpp.getAttributeValue(null,"plot");
                                poster = xpp.getAttributeValue(null, "poster");

//                                URL urlIcon=new URL(poster);
//                                picture  = HttpUtils.getImage(urlIcon);
//                                FileOutputStream outputStream = openFileOutput( poster, Context.MODE_PRIVATE);
//                                picture.compress(Bitmap.CompressFormat.JPEG, 80, outputStream);
//                                outputStream.flush();
//                                outputStream.close();

                            }
//

                    }
                    xpp.next();
//
                }
                if (!title1.equals("")){
                    movieList.add(title1);
                    //posterList.add(picture);
                }
            }catch(Exception e){}
            return null;
        }
        public boolean fileExistance(String fname){
            File file = getBaseContext().getFileStreamPath(fname);
            return file.exists();
        }
        public void onPostExecute(String s){
            super.onPostExecute(s);
            progressBar.setVisibility(View.INVISIBLE);
            TextView movieTitle=findViewById(R.id.movieTitle);
            ImageView poster=findViewById(R.id.poster);
            if (movieList.size()==0){
                Toast.makeText(MovieInformationMain.this, "No Movies Found",Toast.LENGTH_LONG).show();
            }
            else{
                movieTitle.setText(title1);
                //poster.setImageBitmap(picture);
            }
        }
    }

    public boolean onCreateOptionsMenu(Menu m){
        getMenuInflater().inflate(R.menu.menu_movie_appbar,m);
        return true;
    }
    public boolean onOptionsItemSelected (MenuItem mi){
        int id=mi.getItemId();
        AlertDialog.Builder builder=new AlertDialog.Builder(MovieInformationMain.this);
        AlertDialog dialog;
        switch (id){
            case R.id.action_one:
                builder.setTitle("Do you want to go to CBC News?");
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent next=new Intent(MovieInformationMain.this, CBCNewsMain.class);
                        startActivity(next);
                    }
                });
                builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                dialog=builder.create();
                dialog.show();
                break;
            case R.id.action_two:
                builder.setTitle("Do you want to go to OC Transpo?");
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent next=new Intent(MovieInformationMain.this, OCTranspoMain.class);
                        startActivity(next);
                    }
                });
                builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                dialog=builder.create();
                dialog.show();
                break;
            case R.id.action_three:
                builder.setTitle("Do you want to go to Food Nutrition?");
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent next=new Intent(MovieInformationMain.this, FoodNutritionMain.class);
                        startActivity(next);
                    }
                });
                builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                dialog=builder.create();
                dialog.show();
                break;
            case R.id.action_four:
                Toast.makeText(MovieInformationMain.this,"Movie Information by Mary Anne Bernardino",Toast.LENGTH_LONG).show();
                break;
            case R.id.action_five:
                builder.setTitle("Movie Information Help").setMessage("To begin looking for a movie, type the name of the movie into the textbox and click the search button").show();

        }
        return true;
    }

}
