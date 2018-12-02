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

/**
 * Activity that shows the news article
 * @author Natalia Nunes
 */
public class CBCReadNewsActivity extends AppCompatActivity {
    /**
     * log tag
     */
    private String TAG = "CBC-ReadNewsActivity";
    /**
     * received Intent
     */
    private Intent receivedIntent;
    /**
     * News object
     */
    private News news;
    /**
     * Intent arguments with news data
     */
    public static final String INTENT_NEWS_ID = "CBCReadNewsActivity-Intent-News";
    public static final String INTENT_NEWS_BODY = "CBCReadNewsActivity-Intent-Body";
    public static final String INTENT_NEWS_URL = "CBCReadNewsActivity-Intent-Url";
    public static final String INTENT_BUTTON_SAVE_DISPLAY = "CBCReadNewsActivity-Intent-Button-Save";
    /**
     * Buttons objects
     */
    Button buttonDelete, buttonSave, buttonRead;

    /**
     * activity onCreate - loads toolbar and fragments, creates buttons, receives Intent , and loads news data
     * @param savedInstanceState   saved instance
     */
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
     * loads saved button if the Intent comes from the Main Activity
     */
    private void loadButtonSave ( ) {
        Boolean buttonSaveDisplay = receivedIntent.getBooleanExtra(CBCReadNewsActivity.INTENT_BUTTON_SAVE_DISPLAY, false);
        if (buttonSaveDisplay == false) {
            buttonSave.setVisibility(View.GONE);
        } else {
            setSaveClick();
        }
    }

    /**
     * loads delete button if the Intent comes from the SavedNews Activity
     */
    private void loadButtonDelete ( ) {
        Boolean buttonSaveDisplay = receivedIntent.getBooleanExtra(CBCReadNewsActivity.INTENT_BUTTON_SAVE_DISPLAY, false);
        if (buttonSaveDisplay == false) {
            buttonDelete.setVisibility(View.VISIBLE);
            setDeleteClick();
        }
    }

    /**
     * loads news object with Intent arguments
     * @return News object
     */
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

    /**
     * sets news title text
     */
    private void bindNewsTitle ( ) {
        TextView newsTitle = findViewById(R.id.cbc_textview_news_title);
        newsTitle.setText(news.getTitle());
    }

    /**
     * sets news body text using a webview
     * source: https://stackoverflow.com/questions/8987509/how-to-pass-html-string-to-webview-on-android
     */
    private void bindNewsBody ( ) {
        WebView newsBody = findViewById(R.id.cbc_webview_news_body);
        newsBody.loadDataWithBaseURL(null, news.getBody(), "text/html", "utf-8", null);
    }

    /**
     * reads full article button to open the CBC website
     */
    private void setReadClick ( ) {
       buttonRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri u = Uri.parse(news.getUrl());
                //Intent to open the URL
                Intent i = new Intent(Intent.ACTION_VIEW, u);
                startActivity(i);
            }
        });
    }

    /**
     * saves the news when save button is clicked and creates a snackbar
     */
    private void setSaveClick ( ) {
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

            }
        });
    }
    /**
     * deletes the news from the database when delete button
     * also open a dialog to ask deletion confirmation
     * source: https://developer.android.com/guide/topics/ui/dialogs
     */
    private void setDeleteClick ( ) {
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Clicked to delete an article");

                Bundle args = new Bundle();
                args.putString("NEWS_TITLE", news.getTitle());
                FireDeleteDialog fdd = new FireDeleteDialog();
                fdd.setArguments(args);
                fdd.show(getSupportFragmentManager(), "CBCReadNewsActivity-Dialog-Delete-News");

            }
        });
    }

    /**
     * DialogFragment class that creates a dialog
     * source: https://developer.android.com/guide/topics/ui/dialogs
     */
    public static class FireDeleteDialog extends DialogFragment {
        /**
         * Activity context
         */
        Context parentContext;

        /**
         * Default on Attach
         * @param context Activity context
         */
        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
            parentContext = context;
        }

        /**
         * creates a dialog
         * @param savedInstanceState instance state
         * @return Builder object
         */
        @Override
        public Dialog onCreateDialog(final Bundle savedInstanceState) {

            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.cbc_news_delete_dialog_question)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            NewsSQLiteOpenHelper db = new NewsSQLiteOpenHelper(parentContext);
                            String newsTitle = getArguments().getString("NEWS_TITLE");
                            db.deleteNewsEntry(newsTitle);

                            Toast.makeText(getActivity().getApplicationContext(), getString(R.string.cbc_read_news_toast_deleted), Toast.LENGTH_SHORT).show();

                            Intent i = new Intent(parentContext, CBCNewsMain.class);
                            startActivity(i);


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
