package com.example.gabri.finalprojectnewversion.CBCNews;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.gabri.finalprojectnewversion.R;

import java.util.ArrayList;

/**
 * Activity that holds all saved news articles
 * @author Natalia Nunes
 */
public class CBCSavedNewsActivity extends AppCompatActivity {
    /**
     * log tag
     */
    private String TAG = "CBC-SavedNewsActivity";
    /**
     * Intent news´ id argument key
     */
    public static final String INTENT_NEWS_ID = "CBCNewsSavedNews-Intent-News";
    /**
     * Intent news´ body argument key
     */
    public static final String INTENT_NEWS_BODY = "CBCNewsSavedNews-Intent-Body";
    /**
     * Intent news´ url argument key
     */
    public static final String INTENT_NEWS_URL = "CBCNewsSavedNews-Intent-Url";

    /**
     * activity onCreate - loads toolbar and fragments
     * @param savedInstanceState   saved instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cbcsaved_news);
        loadAppBar("Saved News");
        loadFragment();
    }

    /**
     * sets appBar
     * @param title appBar title
     */
    private void loadAppBar ( String title ) {
        Toolbar tb = findViewById(R.id.cbc_toolbar);
        setSupportActionBar(tb);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle(title);
        Log.d(TAG, "Loaded App Bar");
    }

    /**
     * loads the fragment that makes list of news
     */
    private void loadFragment () {
        Bundle args = new Bundle();
        //sets that data has to come from database
        args.putBoolean("GET_FROM_DATABASE", true);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        CBCNewsListFragment fragment = new CBCNewsListFragment();
        fragment.setArguments(args);
        fragmentTransaction.add(R.id.cbc_fragment_framelayout, fragment);
        fragmentTransaction.commit();
    }

}
