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

public class CBCSavedNewsActivity extends AppCompatActivity {

  private String TAG = "CBC-SavedNewsActivity";

  public static final String INTENT_NEWS_ID = "CBCNewsSavedNews-Intent-News";
  public static final String INTENT_NEWS_BODY = "CBCNewsSavedNews-Intent-Body";
  public static final String INTENT_NEWS_URL = "CBCNewsSavedNews-Intent-Url";

//    private ArrayList<News> newsList = new ArrayList<>();
//    private NewsAdapter adapterListNews;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_cbcsaved_news);
    loadAppBar("Saved News");

    loadFragment();
    //loadDbSavedNews();
    //loadListNewsAdapter();
  }

  private void loadAppBar ( String title ) {
    Toolbar tb = findViewById(R.id.cbc_toolbar);
    setSupportActionBar(tb);
    ActionBar ab = getSupportActionBar();
    ab.setDisplayHomeAsUpEnabled(true);
    ab.setTitle(title);
    Log.d(TAG, "Loaded App Bar");
  }

  private void loadFragment () {
    Bundle args = new Bundle();
    args.putBoolean("GET_FROM_DATABASE", true);

    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    CBCNewsListFragment fragment = new CBCNewsListFragment();
    fragment.setArguments(args);
    fragmentTransaction.add(R.id.cbc_fragment_framelayout, fragment);
    fragmentTransaction.commit();
  }

//    private ArrayList<News> loadDbSavedNews ( ) {
//        NewsSQLiteOpenHelper db = new NewsSQLiteOpenHelper(CBCSavedNewsActivity.this);
//        newsList = db.selectAllNewsEntry();
//        Log.d(TAG, "Selected News entries from DB");
//        return newsList;
//    }

//    private void loadListNewsAdapter ( ) {
//        adapterListNews = new NewsAdapter(this);
//        ListView listViewNews = findViewById(R.id.cbc_listview_newslist);
//        listViewNews.setAdapter(adapterListNews);
//        Log.d(TAG, "Loaded List News Adapter");
//
//        listViewNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Log.d(TAG, "Item List Clicked");
//
//                launchActivityReadNews(newsList.get(position));
//            }
//        });
//    }

//    private class NewsAdapter extends ArrayAdapter<News> {
//
//        NewsAdapter(Context c) {
//            super(c, 0);
//        }
//
//        public int getCount() {
//            return newsList.size();
//        }
//
//        public News getItem(int position) {
//            return newsList.get(position);
//        }
//
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @NonNull
//        public View getView(int position, View convertView, ViewGroup parent) {
//            LayoutInflater inflater = CBCSavedNewsActivity.this.getLayoutInflater();
//            View result = inflater.inflate(R.layout.adapter_cbcnews_main, null);
//
//            TextView newsListItem = result.findViewById(R.id.cbc_textview_main_newslist_item);
//            newsListItem.setText(newsList.get(position).getTitle());
//
//            return result;
//        }
//    }

//    private void launchActivityReadNews ( News news ) {
//        Intent i = new Intent(this, CBCReadNewsActivity.class);
//        i.putExtra(INTENT_NEWS_ID, news.getTitle());
//        i.putExtra(INTENT_NEWS_BODY, news.getBody());
//        i.putExtra(INTENT_NEWS_URL, news.getUrl());
//        startActivity(i);
//        Log.d(TAG, "Launched Read News activity");
//    }

}