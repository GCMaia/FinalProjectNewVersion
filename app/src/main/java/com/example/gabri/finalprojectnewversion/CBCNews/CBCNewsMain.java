package com.example.gabri.finalprojectnewversion.CBCNews;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

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
 * TODO: Edit Text
 * TODO: Menu help: Author, Version, Instructions
 * TODO: Summary of data
 * TODO: Number of articles saved
 * TODO: Average, max and min word count among articles
 * TODO: Translation
 * TODO: Java docs
 */

public class CBCNewsMain extends AppCompatActivity {

  private String TAG = "CBC-MainActivity";

  private List<News> newsList = new ArrayList<>();
  //    private NewsAdapter adapterListNews;
  private ProgressBar progressBar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_cbcnews_main);
    loadAppBar("CBC News Reader");

    //loadListNewsAdapter();
    loadFragment();

    //progressBar = findViewById(R.id.cbc_progress_bar);
    //progressBar.setVisibility(View.VISIBLE);

    //new DownloadNewsAsyncTask().execute();
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
    args.putBoolean("GET_FROM_DATABASE", false);

    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    CBCNewsListFragment fragment = new CBCNewsListFragment();
    fragment.setArguments(args);
    fragmentTransaction.add(R.id.cbc_fragment_framelayout, fragment);
    fragmentTransaction.commit();
  }

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
//            LayoutInflater inflater = CBCNewsMain.this.getLayoutInflater();
//            View result = inflater.inflate(R.layout.adapter_cbcnews_main, null);
//
//            TextView newsListItem = result.findViewById(R.id.cbc_textview_main_newslist_item);
//            newsListItem.setText(newsList.get(position).getTitle());
//
//            return result;
//        }
//    }


//    private class DownloadNewsAsyncTask extends AsyncTask<String, Integer, String> {
//
//        private final String API_URL = "https://www.cbc.ca/cmlink/rss-world";
//
//        private ArrayList<News> newsList = new ArrayList<>();
//
//        @Override
//        protected String doInBackground(String... args) {
//
//            try {
//                InputStream is = downloadUrl(API_URL);
//
//                publishProgress(20);
//
//                try {
//                    XmlPullParser parser = Xml.newPullParser();
//                    parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
//                    parser.setInput(is, null);
//                    //parser.nextTag();
//
//                    int tagTitleCounter = 0;
//                    int eventType = parser.getEventType();
//                    while (eventType != XmlPullParser.END_DOCUMENT) {
//                        String tagName;
//
//                        switch (eventType) {
//                            case XmlPullParser.START_TAG: // Open tag
//
//                                String title, body = "", url = "";
//
//                                // Get tag name and check if it's a title
//                                tagName = parser.getName();
//                                Log.d(TAG, "Parsing XML - Tag Names: " + tagName);
//
//                                if ("title".equals(tagName)) {
//
//                                    // Skiping the first 2 titles
//                                    tagTitleCounter++;
//                                    Log.d(TAG, "Parsing XML - tagTitleCounter: " + tagTitleCounter);
//                                    if (tagTitleCounter < 3) {
//                                        break;
//                                    }
//
//                                    // The first text is a title
//                                    title = parser.nextText();
//                                    Log.d(TAG, "Parsing XML - News Object: Title: " + title);
//                                    publishProgress(40);
//
//                                    // Next description tag
//                                    parser.next();
//                                    while (eventType != XmlPullParser.END_DOCUMENT) {
//                                        if ("link".equals(parser.getName())) {
//                                            url = parser.nextText();
//                                            Log.d(TAG, "Parsing XML - News Object: URL: " + url);
//                                            publishProgress(60);
//                                        }
//
//                                        if ("description".equals(parser.getName())) {
//                                            body = parser.nextText();
//                                            Log.d(TAG, "Parsing XML - News Object: Body: " + body);
//                                            publishProgress(80);
//                                            break;
//                                        }
//                                        parser.next();
//                                    }
//
//                                    // Setting up the object
//                                    News n = new News(title);
//                                    n.setBody(body);
//                                    n.setUrl(url);
//
//                                    // Add to list to adapt on PostExecute
//                                    newsList.add(n);
//
//                                    publishProgress(100);
//
//                                } else {
//                                    break;
//                                } // End Check Title
//                        }
//
//                        eventType = parser.next();
//                    }
//
//                } catch (XmlPullParserException e) {
//                    Log.d(TAG, "Could not parse XML");
//                }
//            } catch (IOException e) {
//                Log.d(TAG, "Could not get URL");
//            }
//            return "";
//        }
//
//        @Override
//        protected void onProgressUpdate(Integer... values) {
//            super.onProgressUpdate(values);
//
//            progressBar.setVisibility(View.VISIBLE);
//            progressBar.setProgress(values[0]);
//
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//
//            // Adapt News get from Background to the adapter
//            CBCNewsMain.this.newsList = newsList;
//            //////// ******* CBCNewsMain.this.adapterListNews.notifyDataSetChanged();
//
//            progressBar.setVisibility(View.INVISIBLE);
//        }
//
//        private InputStream downloadUrl (String urlString) throws IOException {
//            URL url = new URL(urlString);
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setReadTimeout(10000 /* milliseconds */);
//            conn.setConnectTimeout(15000 /* milliseconds */);
//            conn.setRequestMethod("GET");
//            conn.setDoInput(true);
//            // Starts the query
//            conn.connect();
//
//            Log.d(TAG, "Downloaded URL " + urlString);
//            return conn.getInputStream();
//        }
//
//    }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_cbcnews_appbar, menu);
    Log.d(TAG, "Inflated Menu Options");
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle item selection
    switch (item.getItemId()) {
      case R.id.cbc_action_settings:
        launchActivitySavedNews();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  private void launchActivitySavedNews ( ) {
    Intent i = new Intent(this, CBCSavedNewsActivity.class);
    startActivity(i);
    Log.d(TAG, "Launched Saved News activity");
  }

//    private void launchActivityReadNews ( News news ) {
//        Intent i = new Intent(this, CBCReadNewsActivity.class);
//        i.putExtra(CBCReadNewsActivity.INTENT_NEWS_ID, news.getTitle());
//        i.putExtra(CBCReadNewsActivity.INTENT_NEWS_BODY, news.getBody());
//        i.putExtra(CBCReadNewsActivity.INTENT_NEWS_URL, news.getUrl());
//        startActivity(i);
//        Log.d(TAG, "Launched Read News activity");
//    }
}