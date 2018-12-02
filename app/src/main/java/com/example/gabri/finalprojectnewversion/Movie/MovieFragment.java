package com.example.gabri.finalprojectnewversion.Movie;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gabri.finalprojectnewversion.R;

public class MovieFragment extends android.app.Fragment { //or android.app.Fragment

    Bundle bundle;
    Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle=this.getArguments();
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle savedInstance){
        View result=layoutInflater.inflate(R.layout.activity_movie_fragment,viewGroup,false);
//        ImageView poster=result.findViewById(R.id.moviePoster);
//        Bitmap picture=HttpUtils.getImage(bundle.getString("poster"));
//        poster.setImageBitmap(picture);
        Resources res=getResources();
        Button save=result.findViewById(R.id.savedMovieButton);

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

        String origin = bundle.getString("origin");
//        if (origin==null){
//            save.setVisibility(View.VISIBLE);
//        }else{
//            save.setVisibility(View.GONE);
//        }

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
                db.insert(MovieDatabase.TABLE_NAME, null, contentValues);
                db.close();
                Toast.makeText(context, "Movie Saved", Toast.LENGTH_LONG).show();
            }
        });
        return result;
    }

    @Override
    public void onAttach(Context ctx){
        super.onAttach(ctx);
        bundle=this.getArguments();
        context=ctx;
    }
}
