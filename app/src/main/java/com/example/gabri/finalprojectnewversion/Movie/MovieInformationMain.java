package com.example.gabri.finalprojectnewversion.Movie;

import android.annotation.SuppressLint;
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
import android.widget.AdapterView;
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
    final ArrayList<String> movieList =new ArrayList<>();
    final ArrayList<Bitmap> posterList=new ArrayList<>();

    ProgressBar progressBarMovie;

    Button search;
    MovieAdapter movieAdapter;
    EditText editText;
    ListView listView;
    MovieQuery movieQuery;
    String title="";
    String year="";
    String rating="";
    String runtime="";
    String actors="";
    String plot="";
    String poster="" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_information_main);

        listView=findViewById(R.id.movieList);
        search = findViewById(R.id.search);

        movieAdapter=new MovieAdapter(this);
        listView.setAdapter(movieAdapter);
        progressBarMovie=findViewById(R.id.progress);
        progressBarMovie.setVisibility(View.INVISIBLE);
        editText=findViewById(R.id.enterMovie);
        movieQuery = new MovieQuery();

        Toolbar movieToolBar=findViewById(R.id.movie_toolbar);
        setSupportActionBar(movieToolBar);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(MovieInformationMain.this, MovieDetail.class);
                intent.putExtra("title",movieList.get(position));
                intent.putExtra("year",year);
                intent.putExtra("rating",rating);
                intent.putExtra("runtime",runtime);
                intent.putExtra("actors",actors);
                intent.putExtra("plot",plot);
                startActivityForResult(intent,12);
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                if (editText.getText().toString().equals("")){
                    Toast.makeText(MovieInformationMain.this, "Cannot Find That Movie", Toast.LENGTH_LONG).show();
                }else {
                    progressBarMovie.setVisibility(View.VISIBLE);
                    title=editText.getText().toString();

                    movieQuery.execute();
                    movieAdapter.notifyDataSetChanged();
                    editText.setText("");
                    Snackbar.make(search, "All Movies Searched", Snackbar.LENGTH_LONG).show();
                    movieList.clear();
                }



            }
        });

    }
    class MovieAdapter extends ArrayAdapter<String>{
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

    }

    class MovieQuery extends AsyncTask<String, Integer,String> {
        String titleTemp;
        final String apiKey="c47e9b47";
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
                        String name=xpp.getName();
                        if (name.equals("movie")) {
                            xpp.next();
                            titleTemp= xpp.getAttributeValue(null, "title");
                            year = xpp.getAttributeValue(null, "year");
                            rating=xpp.getAttributeValue(null,"rated");
                            runtime=xpp.getAttributeValue(null,"runtime");
                            actors=xpp.getAttributeValue(null,"actors");
                            plot=xpp.getAttributeValue(null,"plot");
                            //poster = xpp.getAttributeValue(null, "poster");

//                                URL urlIcon=new URL(poster);
//                                picture  = HttpUtils.getImage(urlIcon);
//                                FileOutputStream outputStream = openFileOutput( poster, Context.MODE_PRIVATE);
//                                picture.compress(Bitmap.CompressFormat.JPEG, 80, outputStream);
//                                outputStream.flush();
//                                outputStream.close();

                        }
                    }else if (title!=null){
                        movieList.add(titleTemp);
                    }
                    xpp.next();
                }
            }
            catch(Exception e){}
            return null;
        }
        public boolean fileExistance(String fname){
            File file = getBaseContext().getFileStreamPath(fname);
            return file.exists();
        }

        @Override
        protected void onPostExecute(String s){
            super.onPostExecute(s);
            progressBarMovie.setVisibility(View.INVISIBLE);

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
