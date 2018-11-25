package com.example.gabri.finalprojectnewversion.CBCNews;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.example.gabri.finalprojectnewversion.R;

public class CBCReadNewsActivity extends AppCompatActivity {

    private String TAG = "CBC-ReadNewsActivity";

    private Intent receivedIntent;
    private News news;

    public static final String INTENT_NEWS_ID = "CBCReadNewsActivity-Intent-News";
    public static final String INTENT_NEWS_BODY = "CBCReadNewsActivity-Intent-Body";
    public static final String INTENT_NEWS_URL = "CBCReadNewsActivity-Intent-Url";
    public static final String INTENT_BUTTON_SAVE_DISPLAY = "CBCReadNewsActivity-Intent-Button-Save";

    Button buttonDelete, buttonSave, buttonRead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cbcread_news);

        loadAppBar("News");

        buttonDelete = findViewById(R.id.cbc_button_delete_news);
        buttonSave = findViewById(R.id.cbc_button_save_news);
        buttonRead = findViewById(R.id.cbc_button_open_news);

        receivedIntent = getIntent();

        news = loadNews();

        bindNewsTitle();
        bindNewsBody();

        setReadClick();
        loadButtonSave();
        loadButtonDelete();
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
            //Button buttonSave = findViewById(R.id.cbc_button_save_news);
            buttonSave.setVisibility(View.GONE);
        } else {
            setSaveClick();
        }
    }

    private void loadButtonDelete ( ) {
        Boolean buttonSaveDisplay = receivedIntent.getBooleanExtra(CBCReadNewsActivity.INTENT_BUTTON_SAVE_DISPLAY, false);
        if (buttonSaveDisplay == false) {
            //Button buttonDelete = findViewById(R.id.cbc_button_delete_news);
            buttonDelete.setVisibility(View.VISIBLE);
            setDeleteClick();
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
        //Button b = findViewById(R.id.cbc_button_open_news);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri u = Uri.parse(news.getUrl());
                Intent i = new Intent(Intent.ACTION_VIEW, u);
                startActivity(i);
            }
        });
    }

    private void setSaveClick ( ) {
        //Button b = findViewById(R.id.cbc_button_save_news);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Clicked to save an article");

                NewsSQLiteOpenHelper db = new NewsSQLiteOpenHelper(CBCReadNewsActivity.this);
                db.insertNewsEntry(news);

                Button b = (Button) v;
                b.setEnabled(false);
                b.setText(getString(R.string.cbc_read_news_button_save_news_after));

                Snackbar.make(buttonSave, getString(R.string.cbc_read_news_toast_saved), Snackbar.LENGTH_LONG).show();

                //Toast.makeText(getApplicationContext(), getString(R.string.cbc_read_news_toast_saved), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setDeleteClick ( ) {

        //Button b = findViewById(R.id.cbc_button_delete_news);

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Clicked to delete an article");

                Bundle args = new Bundle();
                args.putString("NEWS_TITLE", news.getTitle());
                FireDeleteDialog fdd = new FireDeleteDialog();
                fdd.setArguments(args);
                fdd.show(getSupportFragmentManager(), "CBCReadNewsActivity-Dialog-Delete-News");

                //NewsSQLiteOpenHelper db = new NewsSQLiteOpenHelper(CBCReadNewsActivity.this);
                //db.deleteNewsEntry(news);

//                Button b = (Button) v;
//                b.setEnabled(false);
//                b.setText(getString(R.string.cbc_read_news_button_delete_news_after));

//                Toast.makeText(getApplicationContext(), getString(R.string.cbc_read_news_toast_deleted), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static class FireDeleteDialog extends DialogFragment {

        Context parentContext;

        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
            parentContext = context;
        }

        @Override
        public Dialog onCreateDialog(final Bundle savedInstanceState) {

            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Are you sure you want to delete it?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            NewsSQLiteOpenHelper db = new NewsSQLiteOpenHelper(parentContext);
                            String newsTitle = getArguments().getString("NEWS_TITLE");
                            db.deleteNewsEntry(newsTitle);

                            Toast.makeText(getActivity().getApplicationContext(), getString(R.string.cbc_read_news_toast_deleted), Toast.LENGTH_SHORT).show();

                            Intent i = new Intent(parentContext, CBCNewsMain.class);
                            startActivity(i);

                            //Button deleteButton = getActivity().findViewById(R.id.cbc_button_delete_news);
                            //deleteButton.setText(getString(R.string.cbc_read_news_button_delete_news_after));

                            //getActivity().finish();

                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
        }
    }

}
