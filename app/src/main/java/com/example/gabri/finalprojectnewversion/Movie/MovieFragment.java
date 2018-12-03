package com.example.gabri.finalprojectnewversion.Movie;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gabri.finalprojectnewversion.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * class creating Movie Fragment
 */
public class MovieFragment extends android.app.Fragment {
    protected static final String ACTIVITY_NAME = "MovieFragment";
    /**
     * class variables
     */
    Bundle bundle; //holds information retrieved from MovieInformationMain (API)
    Context context;
    String posterName;
    Bitmap picture;

    /**
     * onCreate method initializes class, and retrieves bundle from MovieInformationMain class
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle=this.getArguments();
    }

    /**
     * method sets information from API to fragment and passes information when user wants to save a movie
     * @param layoutInflater inflates the movie fragment xml
     * @param viewGroup
     * @param savedInstance
     * @return movie fragment view
     */
    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle savedInstance){
        View result=layoutInflater.inflate(R.layout.activity_movie_fragment,viewGroup,false);
        Resources res=getResources();
        Button save=result.findViewById(R.id.savedMovieButton);
        ImageView poster=result.findViewById(R.id.moviePoster);
        posterName=bundle.getString("poster");
        try {
        if (fileExistance(posterName)) {
            FileInputStream fis = null;

            try {
                fis = context.openFileInput(posterName);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            picture = BitmapFactory.decodeStream(fis);
            Log.i(ACTIVITY_NAME, "Image Found Locally");
        }
        else {
            Log.i(ACTIVITY_NAME, "Image Downloaded");

            picture  = HttpUtils.getImage(posterName);
            FileOutputStream outputStream = null;
            try {
                outputStream = context.openFileOutput( posterName, Context.MODE_PRIVATE);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            if (picture != null) {
                picture.compress(Bitmap.CompressFormat.JPEG, 80, outputStream);
            }
                outputStream.flush();
                outputStream.close();
        }}catch(Exception e){};
        poster.setImageBitmap(picture);
        TextView title=result.findViewById(R.id.title);
        title.setText(String.format(res.getString(R.string.titleMovie),bundle.getString("title")));
        TextView year=result.findViewById(R.id.year);
        year.setText(String.format(res.getString(R.string.year),bundle.getString("year")));
        TextView rating=result.findViewById(R.id.rating);
        rating.setText(String.format(res.getString(R.string.Rated),bundle.getString("rating")));
        TextView runtime=result.findViewById(R.id.runtime);
        runtime.setText(String.format(res.getString(R.string.Runtime),bundle.getString("runtime")));
        TextView actors=result.findViewById(R.id.actors);
        actors.setText(String.format(res.getString(R.string.Actors),bundle.getString("actors")));
        TextView plot=result.findViewById(R.id.plot);
        plot.setText(String.format(res.getString(R.string.Plot),bundle.getString("plot")));


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MovieDatabase movieDatabase = new MovieDatabase(context);
                SQLiteDatabase db = movieDatabase.getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                contentValues.put(MovieDatabase.KEY_TITLE,bundle.getString("title"));
                contentValues.put(MovieDatabase.KEY_YEAR, bundle.getString("year"));
                contentValues.put(MovieDatabase.KEY_RATED, bundle.getString("rating"));
                contentValues.put(MovieDatabase.KEY_RUNTIME, bundle.getString("runtime"));
                contentValues.put(MovieDatabase.KEY_ACTORS, bundle.getString("actors"));
                contentValues.put(MovieDatabase.KEY_PLOT, bundle.getString("plot"));
                contentValues.put(MovieDatabase.KEY_POSTER, bundle.getString("poster"));
                db.insert(MovieDatabase.TABLE_NAME, null, contentValues);
                db.close();
                Toast.makeText(context, "Movie Saved", Toast.LENGTH_LONG).show();
            }
        });
        return result;
    }
    public boolean fileExistance(String fname){
        File file = context.getFileStreamPath(fname);
        return file.exists();
    }
    /**
     * method called when fragment class is called
     * @param ctx current context
     */
    @Override
    public void onAttach(Context ctx){
        super.onAttach(ctx);
        bundle=this.getArguments();
        context=ctx;
    }
}
