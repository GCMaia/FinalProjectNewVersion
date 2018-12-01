package com.example.gabri.finalprojectnewversion.Movie;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
        ImageView poster=result.findViewById(R.id.moviePoster);
        poster.setImageBitmap((Bitmap)bundle.getParcelable("poster"));
        TextView title=result.findViewById(R.id.title);
        title.setText(bundle.getString("title"));
        TextView year=result.findViewById(R.id.year);
        year.setText(bundle.getString("year"));
        TextView rating=result.findViewById(R.id.rating);
        rating.setText(bundle.getString("rating"));
        TextView runtime=result.findViewById(R.id.runtime);
        runtime.setText(bundle.getString("runtime"));
        TextView actors=result.findViewById(R.id.actors);
        actors.setText(bundle.getString("actors"));
        TextView plot=result.findViewById(R.id.plot);
        plot.setText(bundle.getString("plot"));
        return result;
    }

    @Override
    public void onAttach(Context ctx){
        super.onAttach(ctx);
        bundle=this.getArguments();
        context=ctx;
    }
}
