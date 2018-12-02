package com.example.gabri.finalprojectnewversion.CBCNews;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.gabri.finalprojectnewversion.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Fragment that makes the list and is used by Main Activity and Saved_News Activity
 * @author Natalia Nunes
 */
public class CBCNewsListFragment extends Fragment {
    /**
     * log tag
     */
    private String TAG = "CBC-NewsListFragment";
    /**
     * Actibity context
     */
    private Context parentContext;
    /**
     * List of News object
     */
    private ArrayList<News> newsList = new ArrayList<>();
    /**
     * List Adapter
     */
    private NewsAdapter adapterListNews;
    /**
     * progressBar
     */
    private ProgressBar progressBar;
    /**
     * fragment argument that determines if the fragment gets data from database or CBC website
     */
    private boolean argsGetFromDatabase;

    /**
     * Default constructor
     */
    public CBCNewsListFragment() {
        // Required empty public constructor
    }

    /**
     * Creates the View
     * @param inflater inflates the view
     * @param container
     * @param savedInstanceState instance state
     * @return View object
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cbcnews_list, container, false);

        Bundle args = getArguments();
        argsGetFromDatabase = args.getBoolean("GET_FROM_DATABASE");

        loadListNewsAdapter(view);
        if (argsGetFromDatabase) {
            loadDbSavedNews();
        } else {
            progressBar = view.findViewById(R.id.cbc_progress_bar);
            progressBar.setVisibility(View.VISIBLE);
            new DownloadNewsAsyncTask().execute();
        }

        Log.d(TAG, "Fragment has created the view");
        return view;
    }

    /**
     * attaches the Activity context
     * @param context Activity context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        parentContext = context;
        Log.d(TAG, "Fragment has attached Context");
    }

    /**
     * loads the News list from database
     * @return News list object
     */
    private ArrayList<News> loadDbSavedNews ( ) {
        NewsSQLiteOpenHelper db = new NewsSQLiteOpenHelper(parentContext);
        newsList = db.selectAllNewsEntry();
        Log.d(TAG, "Selected News entries from DB");
        return newsList;
    }

    /**
     * sets ListView adapter and the list item click
     * @param view View object
     */
    private void loadListNewsAdapter ( View view ) {
        adapterListNews = new NewsAdapter(parentContext);
        ListView listViewNews = view.findViewById(R.id.cbc_listview_newslist);
        listViewNews.setAdapter(adapterListNews);
        Log.d(TAG, "Loaded List News Adapter");

        listViewNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "Item List Clicked");
                launchActivityReadNews(newsList.get(position));
            }
        });
    }

    /**
     * ListAdapter Class
     * adapts the list of news into the view
     * based on lab6
     */
    private class NewsAdapter extends ArrayAdapter<News> {
        /**
         * default constructor
         * @param c context
         */
        NewsAdapter(Context c) {
            super(c, 0);
        }

        /**
         * gets list size
         * @return list size
         */
        public int getCount() {
            return newsList.size();
        }

        /**
         * gets list item
         * @param position item position
         * @return list item
         */
        public News getItem(int position) {
            return newsList.get(position);
        }

        /**
         * gets list item index
         * @param position index
         * @return index
         */
        public long getItemId(int position) {
            return position;
        }

        /**
         * makes the list item View
         * @param position index
         * @param convertView view of list
         * @param parent ViewGroup
         * @return view
         */
        @NonNull
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = CBCNewsListFragment.this.getLayoutInflater();
            View result = inflater.inflate(R.layout.adapter_cbcnews_main, null);

            TextView newsListItem = result.findViewById(R.id.cbc_textview_main_newslist_item);
            newsListItem.setText(newsList.get(position).getTitle());

            return result;
        }
    }


    /**
     * AsyncTask class that loads news data from CBC website
     */
    private class DownloadNewsAsyncTask extends AsyncTask<String, Integer, String> {
        /**
         * website URL
         */
        private final String API_URL = "https://www.cbc.ca/cmlink/rss-world";
        /**
         * news list
         */
        private ArrayList<News> newsList = new ArrayList<>();

        /**
         * loads news from CBC website
         * @param args
         * @return String
         */
        @Override
        protected String doInBackground(String... args) {

            try {
                InputStream is = downloadUrl(API_URL);

                publishProgress(20);

                try {
                    XmlPullParser parser = Xml.newPullParser();
                    parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                    parser.setInput(is, null);
                    //parser.nextTag();

                    int tagTitleCounter = 0;
                    int eventType = parser.getEventType();
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        String tagName;

                        switch (eventType) {
                            case XmlPullParser.START_TAG: // Open tag

                                String title, body = "", url = "";

                                // Get tag name and check if it's a title
                                tagName = parser.getName();
                                Log.d(TAG, "Parsing XML - Tag Names: " + tagName);

                                if ("title".equals(tagName)) {

                                    // Skiping the first 2 titles
                                    tagTitleCounter++;
                                    Log.d(TAG, "Parsing XML - tagTitleCounter: " + tagTitleCounter);
                                    if (tagTitleCounter < 3) {
                                        break;
                                    }

                                    // The first text is a title
                                    title = parser.nextText();
                                    Log.d(TAG, "Parsing XML - News Object: Title: " + title);
                                    publishProgress(40);

                                    // Next description tag
                                    parser.next();
                                    while (eventType != XmlPullParser.END_DOCUMENT) {
                                        if ("link".equals(parser.getName())) {
                                            url = parser.nextText();
                                            Log.d(TAG, "Parsing XML - News Object: URL: " + url);
                                            publishProgress(60);
                                        }

                                        if ("description".equals(parser.getName())) {
                                            body = parser.nextText();
                                            Log.d(TAG, "Parsing XML - News Object: Body: " + body);
                                            publishProgress(80);
                                            break;
                                        }
                                        parser.next();
                                    }

                                    // Setting up the object
                                    News n = new News(title);
                                    n.setBody(body);
                                    n.setUrl(url);

                                    // Add to list to adapt on PostExecute
                                    newsList.add(n);

                                    publishProgress(100);

                                } else {
                                    break;
                                } // End Check Title
                        }

                        eventType = parser.next();
                    }

                } catch (XmlPullParserException e) {
                    Log.d(TAG, "Could not parse XML");
                }
            } catch (IOException e) {
                Log.d(TAG, "Could not get URL");
            }
            return "";
        }

        /**
         * Updates progressBar data
         * @param values integer
         */
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(values[0]);

        }

        /**
         * Transfer local newslist to fragment class
         * @param s Default string
         */
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            // Adapt News get from Background to the adapter
            CBCNewsListFragment.this.newsList = newsList;
            CBCNewsListFragment.this.adapterListNews.notifyDataSetChanged();

            progressBar.setVisibility(View.INVISIBLE);
        }


        /**
         * Downloads data from URL
         * Copied from lab 6.
         *
         * @param urlString String of the URL
         * @return URL content
         * @throws IOException
         */
        private InputStream downloadUrl (String urlString) throws IOException {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();

            Log.d(TAG, "Downloaded URL " + urlString);
            return conn.getInputStream();
        }

    }


    /**
     * Intent to read News Activity passing news article data
     * @param news News object
     */
    private void launchActivityReadNews ( News news ) {
        Intent i = new Intent(parentContext, CBCReadNewsActivity.class);
        i.putExtra(CBCReadNewsActivity.INTENT_NEWS_ID, news.getTitle());
        i.putExtra(CBCReadNewsActivity.INTENT_NEWS_BODY, news.getBody());
        i.putExtra(CBCReadNewsActivity.INTENT_NEWS_URL, news.getUrl());

        if (! argsGetFromDatabase) {
            i.putExtra(CBCReadNewsActivity.INTENT_BUTTON_SAVE_DISPLAY, true);
        }

        Log.d(TAG, "Fragment put news data for intent - Title: " + news.getTitle());
        Log.d(TAG, "Fragment put news data for intent - URL: " + news.getUrl());
        Log.d(TAG, "Fragment put news data for intent - Body: " + news.getBody());
        startActivity(i);
        Log.d(TAG, "Launched Read News activity");
    }

}
