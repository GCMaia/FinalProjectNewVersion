package com.example.gabri.finalprojectnewversion.CBCNews;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gabri.finalprojectnewversion.FoodNutrition.FoodNutritionMain;
import com.example.gabri.finalprojectnewversion.Movie.MovieInformationMain;
import com.example.gabri.finalprojectnewversion.OCTranspo.OCTranspoMain;
import com.example.gabri.finalprojectnewversion.R;

import org.w3c.dom.Text;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * News Main Activity
 * @author Natalia Nunes
 */
public class CBCNewsMain extends AppCompatActivity {
    /**
     * log tag
     */
    private String TAG = "CBC-MainActivity";
    /**
     * ArrayList of news objects
     */
    private List<News> newsList = new ArrayList<>();
    /**
     * progress bar
     */
    private ProgressBar progressBar;

    /**
     * activity onCreate - loads toolbar and fragments
     * @param savedInstanceState   saved instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cbcnews_main);
        loadAppBar("CBC News Reader");
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
        //sets that data has to come from HTTP (CBC website)
        args.putBoolean("GET_FROM_DATABASE", false);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        CBCNewsListFragment fragment = new CBCNewsListFragment();
        fragment.setArguments(args);
        fragmentTransaction.add(R.id.cbc_fragment_framelayout, fragment);
        fragmentTransaction.commit();
    }

    /**
     * creates menu options
     * @param menu menu object
     * @return boolean
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cbcnews_appbar, menu);
        Log.d(TAG, "Inflated Menu Options");
        return true;
    }

    /**
     * sets menu items listeners
     * @param item MenuItem object
     * @return boolean
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.cbc_action_save:
                Intent is = new Intent(this, CBCSavedNewsActivity.class);
                startActivity(is);
                return true;
            case R.id.action_bus:

                android.app.AlertDialog.Builder busBuilder = new android.app.AlertDialog.Builder(this);
                busBuilder.setMessage(R.string.questionOCTranspo).setTitle(R.string.OCTranspoTitle)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                Intent ib = new Intent(CBCNewsMain.this, OCTranspoMain.class);
                                startActivity(ib);

                            }
                        }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) { }
                }).show();

                return true;
            case R.id.action_movie:


                android.app.AlertDialog.Builder movieBuilder = new android.app.AlertDialog.Builder(this);
                movieBuilder.setMessage(R.string.questionMovies).setTitle(R.string.MoviesTitle)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                Intent ib = new Intent(CBCNewsMain.this,  MovieInformationMain.class);
                                startActivity(ib);

                            }
                        }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) { }
                }).show();


                return true;
            case R.id.action_food:


                android.app.AlertDialog.Builder foodBuilder = new android.app.AlertDialog.Builder(this);
                foodBuilder.setMessage(R.string.questionNutrition).setTitle(R.string.NutritionTitle)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                Intent ib = new Intent(CBCNewsMain.this, FoodNutritionMain.class);
                                startActivity(ib);

                            }
                        }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) { }
                }).show();

                return true;
            case R.id.cbc_action_help:

                LayoutInflater inflater = getLayoutInflater();
                AlertDialog.Builder builderCustom = new AlertDialog.Builder(CBCNewsMain.this);
                builderCustom.setView(inflater.inflate(R.layout.cbc_news_help_dialog, null));
                // Add the buttons
                builderCustom.setPositiveButton(R.string.cbc_news_help_dialog_close, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                        //finish();
                    }
                });
                // Create the AlertDialog
                AlertDialog dialogCustom = builderCustom.create();
                dialogCustom.show();

                return true;
            case R.id.cbc_action_about:
                Toast.makeText(getApplicationContext(), getString(R.string.cbc_news_main_about_toast), Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



}