package com.example.gabri.finalprojectnewversion.Movie;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;


/**
 * class that displays initial movie search screen
 */
public class MovieInformationMain extends AppCompatActivity {
    /**
     * class variables
     */
    protected static final String ACTIVITY_NAME = "MovieInfoActivity";
    final ArrayList<String> movieList =new ArrayList<>();
    final ArrayList<Bitmap> posterList=new ArrayList<>();

    ProgressBar progressBarMovie;

    Button search;
    MovieAdapter movieAdapter;
    EditText editText;
    ListView listView;
    SharedPreferences pref;
    String title="";
    String year="";
    String rating="";
    String runtime="";
    String actors="";
    String plot="";
    String poster="" ;
    Bitmap picture;

    String posterName;


    /**
     * onCreate method initializes various class variables, toolbar, progress bar, and onclick listeners for list item and search button
     * @param savedInstanceState
     */
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
        pref=getSharedPreferences("prevMovie",Context.MODE_PRIVATE);
        String prevMovie=pref.getString("prevTitle","empty");
//        if (prevMovie!="empty"){
//            title=prevMovie;
//            MovieQuery movieQuery=new MovieQuery();
//            movieQuery.execute();
//        }
        Toolbar movieToolBar=findViewById(R.id.movie_toolbar);
        setSupportActionBar(movieToolBar);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(MovieInformationMain.this, MovieDetail.class);
                intent.putExtra("id",id);
                intent.putExtra("poster",posterName);
                intent.putExtra("title",movieList.get(position));
                intent.putExtra("year",year);
                intent.putExtra("rating",rating);
                intent.putExtra("runtime",runtime);
                intent.putExtra("actors",actors);
                intent.putExtra("plot",plot);
                intent.putExtra("origin", "origin");
                startActivity(intent);
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
//                    SharedPreferences.Editor prefEdit=pref.edit();
//                    prefEdit.putString("prevTitle",title);
//                    prefEdit.apply();

                    MovieQuery movieQuery = new MovieQuery();
                    movieQuery.execute();
                    movieAdapter.notifyDataSetChanged();
                    editText.setText("");
                    Snackbar.make(search, "All Movies Searched", Snackbar.LENGTH_LONG).show();
                    movieList.clear();
                }



            }
        });

    }

    /**
     * inner class that handles arraylist to be display in list view
     */
    class MovieAdapter extends ArrayAdapter<String>{
        /**
         * constructor of movie adapter
         * @param ctx context
         */
        public MovieAdapter(Context ctx){
            super(ctx,0);
        }

        /**
         * getter for number of objects in arraylist
         * @return size of arraylist
         */
        public int getCount(){
            return movieList.size();
        }

        /**
         * getter for returning movie on list at specific position
         * @param position index of movie in array list
         * @return movie title
         */
        public String getMovie(int position){
            return movieList.get(position);
        }

        /**
         * getter for movie poster
         * @param position index of movie poster in array list
         * @return bitmap of poster
         */
        public Bitmap getPoster(int position){ return posterList.get(position);}

        /**
         * getter for current view
         * @param position specific index
         * @param convertView
         * @param parent
         * @return view with set xml layout and text
         */
        public View getView(int position, View convertView, ViewGroup parent){
            LayoutInflater inflater=MovieInformationMain.this.getLayoutInflater();
            View result;
            result=inflater.inflate(R.layout.movie_item,null);
            TextView movieTitle=result.findViewById(R.id.movieTitle);
            movieTitle.setText(getMovie(position));
            ImageView poster=result.findViewById(R.id.poster);
            poster.setImageBitmap(getPoster(position));
            return result;
        }

    }

    /**
     * method to retrieve information from movie api
     */
    class MovieQuery extends AsyncTask<String, Integer,String> {
        String titleTemp;
        final String apiKey="c47e9b47";

        /**
         * method retrieves movie information from api using an encoded movie title entered by user
         * @param strings
         * @return
         */
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
                            poster = xpp.getAttributeValue(null, "poster");

                            URL urlPic=new URL(poster);
                            posterName=titleTemp+".JPEG";

                            if (fileExistance(posterName)) {
                                FileInputStream fis = null;
                                try {
                                    fis = openFileInput(posterName);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                picture = BitmapFactory.decodeStream(fis);
                                Log.i(ACTIVITY_NAME, "Image Found Locally");
                            }
                            else {
                                Log.i(ACTIVITY_NAME, "Image Downloaded");

                                picture  = HttpUtils.getImage(urlPic);
                                FileOutputStream outputStream = openFileOutput( posterName, Context.MODE_PRIVATE);
                                if (picture != null) {
                                    picture.compress(Bitmap.CompressFormat.JPEG, 80, outputStream);
                                }
                                outputStream.flush();
                                outputStream.close();
                            }
//                            picture = HttpUtils.getImage(urlPic);
//                            FileOutputStream outputStream = openFileOutput( title+".JPEG", Context.MODE_PRIVATE);
//                            picture.compress(Bitmap.CompressFormat.JPEG, 80, outputStream);
//                            outputStream.flush();
//                            outputStream.close();

                        }
                    }else if (title!=null && poster!=null){
                        movieList.add(titleTemp);
                        posterList.add(picture);
                    }
                    else{
                        movieList.add(titleTemp);
                    }
                    xpp.next();
                }
            }
            catch(Exception e){}
            return null;
        }

        /**
         * method checks if file exists in local directory
         * @param fname name of file
         * @return true if file exists in directory
         */
        public boolean fileExistance(String fname){
            File file = getBaseContext().getFileStreamPath(fname);
            return file.exists();
        }

        /**
         * method executes after query is done, sets progress bar to invisible
         * @param s
         */
        @Override
        protected void onPostExecute(String s){
            super.onPostExecute(s);
            progressBarMovie.setVisibility(View.INVISIBLE);

        }
    }

    /**
     * method inflates the toolbar
     * @param m menu layout to inflate
     * @return true if toolbar is inflated
     */
    public boolean onCreateOptionsMenu(Menu m){
        getMenuInflater().inflate(R.menu.menu_movie_appbar,m);
        return true;
    }

    /**
     * method sets actions for each item selected in toolbar
     * @param mi menu item clicked by user
     * @return true if action is successfully executed
     */
    public boolean onOptionsItemSelected (MenuItem mi){
        int id=mi.getItemId();
        AlertDialog.Builder builder=new AlertDialog.Builder(MovieInformationMain.this);
        AlertDialog dialog;
        switch (id){
            case R.id.action_one:
                builder.setTitle("Do you want to see your saved Movies?");
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent next=new Intent(MovieInformationMain.this, MoviesSaved.class);
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
            case R.id.action_three:
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
            case R.id.action_four:
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
            case R.id.action_five:
                Toast.makeText(MovieInformationMain.this,"Movie Information Version 1.0 by Mary Anne Bernardino",Toast.LENGTH_LONG).show();
                break;
            case R.id.action_six:
                builder.setTitle("Movie Information Help").setMessage("To begin looking for a movie, type the name of the movie into the textbox and click the search button").show();

        }
        return true;
    }

}
