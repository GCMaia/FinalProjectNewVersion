package com.example.gabri.finalprojectnewversion.Movie;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Movie;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gabri.finalprojectnewversion.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collection;

public class MoviesSaved extends AppCompatActivity {
    MovieDatabase movieDatabase;
    ArrayList<Integer> ID;
    ArrayList<String> titles;
    ArrayList<String> year;
    ArrayList<String> runtime;
    ArrayList<String> rated;
    ArrayList<String> plot;
    ArrayList<String> actors;
    SavedMovieAdapter savedMovieAdapter;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_saved);

        movieDatabase=new MovieDatabase(this);
        ID=new ArrayList<>();
        titles=new ArrayList<>();
        savedMovieAdapter=new SavedMovieAdapter(this);
        getSavedMovies();
    }

    public void getSavedMovies(){
        Cursor cursor=movieDatabase.getAllSavedMovies();
        if (cursor.getCount()==0){
            //Toast.makeText(this,"No Movies Saved",Toast.LENGTH_LONG).show();
        }
        else{
            while (cursor.moveToNext()){
                ID.add(cursor.getInt(0));
                titles.add(cursor.getString(1));
                year.add(cursor.getString(2));
                rated.add(cursor.getString(3));
                runtime.add(cursor.getString(4));
                actors.add(cursor.getString(5));
                plot.add(cursor.getString(6));
                listView=findViewById(R.id.savedMovieList);
                listView.setAdapter(savedMovieAdapter);
                savedMovieAdapter.notifyDataSetChanged();
            }

        }
    }

    class SavedMovieAdapter extends ArrayAdapter<String> {

        public SavedMovieAdapter(Context context) {
            super(context, 0);
        }
        public int getCount(){
            return titles.size();
        }
        public String getTitle(int position){
            return titles.get(position);
        }

        public View getView(final int position, View convertView, ViewGroup parent){
            LayoutInflater inflater=MoviesSaved.this.getLayoutInflater();
            View result=inflater.inflate(R.layout.saved_movie_item,null);
            Button remove=result.findViewById(R.id.removeButton);
            TextView title=result.findViewById(R.id.movieTitle);

            title.setText(getTitle(position));

            if (getCount()!=0){
                TextView noSaved=findViewById(R.id.noSavedMovies);
                noSaved.setText("");
            }

            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(),"Movie Removed",Toast.LENGTH_LONG).show();
                    MovieDatabase movieDatabase=new MovieDatabase(getContext());
                    SQLiteDatabase database=movieDatabase.getWritableDatabase();
                    database.delete(MovieDatabase.TABLE_NAME,MovieDatabase.KEY_TITLE+"=?",new String[]{titles.get(position)});
                    ID.remove(position);
                    titles.remove(position);
                    savedMovieAdapter.notifyDataSetChanged();
                }
            });

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Intent intent=new Intent(MoviesSaved.this,MovieDetail.class);
                    intent.putExtra("title",titles.get(position));
                    intent.putExtra("year", year.get(position));
                    intent.putExtra("rated", rated.get(position));
                    intent.putExtra("runtime", runtime.get(position));
                    intent.putExtra("actors",actors.get(position));
                    intent.putExtra("plot", plot.get(position));
                    intent.putExtra("origin","save");
                    startActivity(intent);
                }
            });
            return result;
        }

    }
}
