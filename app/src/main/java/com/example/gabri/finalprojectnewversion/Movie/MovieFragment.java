package com.example.gabri.finalprojectnewversion.Movie;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        TextView title=result.findViewById(R.id.title);
        String titleText=bundle.getString("title");
        title.setText(titleText);
        return result;
    }

    @Override
    public void onAttach(Context ctx){
        super.onAttach(ctx);
        bundle=this.getArguments();
        context=ctx;
    }
}
