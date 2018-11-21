package com.example.gabri.finalprojectnewversion.CBCNews;

import android.support.v7.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.example.gabri.finalprojectnewversion.CBCNews.News;
import com.example.gabri.finalprojectnewversion.CBCNews.NewsSQLiteOpenHelper;
import com.example.gabri.finalprojectnewversion.R;

public class CBCReadNewsActivity extends AppCompatActivity {

    private String TAG = "CBC-ReadNewsActivity";

    private Intent receivedIntent;

    private News news;

    public static final String INTENT_NEWS_ID = "CBCReadNewsActivity-Intent-News";
    public static final String INTENT_NEWS_BODY = "CBCReadNewsActivity-Intent-Body";
    public static final String INTENT_NEWS_URL = "CBCReadNewsActivity-Intent-Url";
    public static final String INTENT_BUTTON_SAVE_DISPLAY = "CBCReadNewsActivity-Intent-Button-Save";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cbcread_news);

        loadAppBar("News");

        receivedIntent = getIntent();

        news = loadNews();

        bindNewsTitle();
        bindNewsBody();

        setReadClick();
        loadButtonSave();
    }

    private void loadAppBar ( String title ) {
        Toolbar tb = findViewById(R.id.cbc_toolbar);
        setSupportActionBar(tb);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle(title);
        Log.d(TAG, "Loaded App Bar");
    }

    private void loadButtonSave ( ) {
        Boolean buttonSaveDisplay = receivedIntent.getBooleanExtra(CBCReadNewsActivity.INTENT_BUTTON_SAVE_DISPLAY, false);
        if (buttonSaveDisplay == false) {
            Button buttonSave = findViewById(R.id.cbc_button_save_news);
            buttonSave.setVisibility(View.GONE);
        } else {
            setSaveClick();
        }
    }

    private News loadNews ( ) {
        String title = receivedIntent.getStringExtra(CBCReadNewsActivity.INTENT_NEWS_ID);
        String body = receivedIntent.getStringExtra(CBCReadNewsActivity.INTENT_NEWS_BODY);
        String url = receivedIntent.getStringExtra(CBCReadNewsActivity.INTENT_NEWS_URL);

        News n = new News(title);
        n.setBody(body);
        n.setUrl(url);

        Log.d(TAG, "Loaded News from Intent - Title: " + n.getTitle());
        Log.d(TAG, "Loaded News from Intent - URL: " + n.getUrl());
        Log.d(TAG, "Loaded News from Intent - Body: " + n.getBody());
        return n;
    }

    private void bindNewsTitle ( ) {
        TextView newsTitle = findViewById(R.id.cbc_textview_news_title);
        newsTitle.setText(news.getTitle());
    }

    private void bindNewsBody ( ) {
        WebView newsBody = findViewById(R.id.cbc_webview_news_body);
        newsBody.loadDataWithBaseURL(null, news.getBody(), "text/html", "utf-8", null);
    }

    private void setReadClick ( ) {
        Button b = findViewById(R.id.cbc_button_open_news);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri u = Uri.parse(news.getUrl());
                Intent i = new Intent(Intent.ACTION_VIEW, u);
                startActivity(i);
            }
        });
    }

    private void setSaveClick ( ) {
        Button b = findViewById(R.id.cbc_button_save_news);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Clicked to save an article");

                NewsSQLiteOpenHelper db = new NewsSQLiteOpenHelper(CBCReadNewsActivity.this);
                db.insertNewsEntry(news);

                Button b = (Button) v;
                b.setEnabled(false);
                b.setText("Saved");
            }
        });


    }
}
