package com.morxander.popularmovies;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


public class MovieDetails extends ActionBarActivity {


    public static Movie movie;
    public static Intent intent;
    public static TextView movie_title, movie_year,movie_rate,movie_overview,movie_duration;
    public static RatingBar ratingBar;
    public static ImageView movie_poster;
    public static View black_line;
    public static ProgressDialog progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_movie_details, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);
            WindowManager wm = (WindowManager) rootView.getContext().getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            initComponents(rootView);
            setValues(rootView);
            return rootView;
        }

        public void initComponents(View rootView){
            progress = ProgressDialog.show(rootView.getContext(), "Loading",
                    "Please Wait...", true);
            movie = new Movie();
            MovieDetails.intent = getActivity().getIntent();
            int movie_id = intent.getIntExtra("movie_id",0);
            int movie_position = intent.getIntExtra("movie_position",0);
            new GetMovie().execute(String.valueOf(movie_id), null, null);
            movie = MainActivity.moviesArrayList.get(movie_position);
            movie_title = (TextView)rootView.findViewById(R.id.movie_title);
            movie_year = (TextView)rootView.findViewById(R.id.movie_year);
            movie_rate = (TextView)rootView.findViewById(R.id.movie_rating);
            movie_duration = (TextView)rootView.findViewById(R.id.movie_duration);
            movie_poster = (ImageView)rootView.findViewById(R.id.movie_poster);
            ratingBar = (RatingBar)rootView.findViewById(R.id.ratingBar);
            movie_overview = (TextView)rootView.findViewById(R.id.movie_overview);
            black_line = (View)rootView.findViewById(R.id.black_line);
        }

        public static void setValues(View rootView){
            movie_title.setText(movie.getTitle());

            movie_year.setText(movie.getReleaseDate());
            movie_title.setVisibility(View.VISIBLE);

            movie_rate.setText(movie.getVoteAverage() + "/10");
            movie_overview.setText(movie.getOverview());

            float rating = movie.getVoteAverage() / 2;
            ratingBar.setRating(rating);

            movie_duration.setText(movie.getDuration() + "M");

            String movie_poster_url = Settings.imageBaseURL + Settings.imageSize342 + "/"  +  movie.getPosterPath();
            Picasso.with(rootView.getContext()).load(movie_poster_url).placeholder(R.drawable.poster).into(movie_poster);
            movie_poster.setVisibility(View.VISIBLE);

        }
    }
}
