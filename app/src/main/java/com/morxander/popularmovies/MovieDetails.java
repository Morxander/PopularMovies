package com.morxander.popularmovies;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.morxander.popularmovies.db.models.MovieDB;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class MovieDetails extends ActionBarActivity {


    public static Movie movie;
    public static int movieID, moviePosition;
    public static Intent intent;
    public static TextView movie_title, movie_year,movie_rate,movie_overview,movie_runtime,reviews,reviews_header;
    public static RatingBar ratingBar;
    public static ImageView movie_poster;
    public static View black_line,black_line2,black_line3;
    public static ProgressDialog loading;
    public static Bundle arguments;
    public static Button favButton;
    public static ListView videosList;
    public static ArrayAdapter<String> arrayAdapter;
    public static ArrayList<String> arrayList;
    public static ScrollView scrollview;
    public static LinearLayout videosContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putParcelable("movie", getIntent().getData());
            PlaceholderDetailsFragment fragment = new PlaceholderDetailsFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_detail_container, fragment)
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
    public static class PlaceholderDetailsFragment extends Fragment {

        static Toast toast;

        public PlaceholderDetailsFragment() {
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);
            WindowManager wm = (WindowManager) rootView.getContext().getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            initComponents(rootView);
            setPoster(rootView);
            return rootView;
        }
        // This is the init metho
        public void initComponents(View rootView){
            // Current Moview Object
            movie = new Movie();
            arguments = getArguments();
            if (arguments.getInt("movie_id") > 0)
            {
                int movie_id = arguments.getInt("movie_id");
                int movie_position = arguments.getInt("movie_position");
                movie = MainActivity.moviesArrayList.get(movie_position);
            }else {
                MovieDetails.intent = getActivity().getIntent();
                int movie_id = intent.getIntExtra("movie_id", 0);
                int movie_position = intent.getIntExtra("movie_position", 0);
                movie = MainActivity.moviesArrayList.get(movie_position);
            }
            loading = ProgressDialog.show(rootView.getContext(), "Loading",
                    "Please Wait...", true);

            // Setting the views
            new GetMovie().execute(String.valueOf(movie.getMovieId()));
            movie_title = (TextView)rootView.findViewById(R.id.movie_title);
            movie_year = (TextView)rootView.findViewById(R.id.movie_year);
            movie_runtime = (TextView)rootView.findViewById(R.id.runtime);
            movie_rate = (TextView)rootView.findViewById(R.id.movie_rating);
            movie_poster = (ImageView)rootView.findViewById(R.id.movie_poster);
            ratingBar = (RatingBar)rootView.findViewById(R.id.ratingBar);
            movie_overview = (TextView)rootView.findViewById(R.id.movie_overview);
            reviews_header = (TextView)rootView.findViewById(R.id.reviews_header);
            black_line = (View)rootView.findViewById(R.id.black_line);
            black_line2 = (View)rootView.findViewById(R.id.black_line2);
            black_line3 = (View)rootView.findViewById(R.id.black_line3);
            favButton = (Button)rootView.findViewById(R.id.bt_fav);
            videosList = (ListView)rootView.findViewById(R.id.videos_list);
            reviews = (TextView)rootView.findViewById(R.id.reviews);
            scrollview = (ScrollView)rootView.findViewById(R.id.scrollview);
            arrayList = new ArrayList<String>();
            arrayAdapter = new ArrayAdapter<String>(rootView.getContext(),R.layout.video_list,R.id.Itemname,arrayList);
            videosList.setAdapter(arrayAdapter);
            videosList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + movie.getVideos().get(position).getKey())));
                }
            });
            videosContainer = (LinearLayout) rootView.findViewById(R.id.videos_listview);

        }

        public static void setPoster(View rootView){
            String movie_poster_url;
            if (movie.getPosterPath() == Utils.image_not_found) {
                movie_poster_url = Utils.image_not_found;
            }else {
                movie_poster_url = Utils.imageBaseURL + Utils.imageSize185 + "/" + movie.getPosterPath();
            }
            Picasso.with(rootView.getContext()).load(movie_poster_url).placeholder(R.drawable.poster).into(movie_poster);
            movie_poster.setVisibility(View.VISIBLE);
        }

        // Setting the values of the views
        public static void setValues(){
            movie_title.setText(movie.getTitle());
            movie_runtime.setText(String.valueOf(movie.getDuration()) + "M");
            movie_year.setText(movie.getReleaseDate());
            movie_title.setVisibility(View.VISIBLE);
            movie_rate.setText(movie.getVoteAverage() + "/10");
            movie_overview.setText(movie.getOverview());
            float rating = movie.getVoteAverage() / 2;
            ratingBar.setRating(rating);
            ratingBar.setVisibility(View.VISIBLE);
            // changing the button text based on if the movie is already in the fav list or not
            if(!Utils.isFavorite(movie.getMovieId())) {
                favButton.setText("Mark As Favorite");
            }else{
                favButton.setText("Unmark As Favorite");
            }
            // Setting the onclick listener based on if the movie is already in the fav list or not
            favButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(Utils.isFavorite(movie.getMovieId())){
                        MovieDB movieDB = new Select().from(MovieDB.class).where("movie_pid = ?",movie.getMovieId()).executeSingle();
                        movieDB.delete();
                        favButton.setText("Mark As Favorite");
                    }else {
                        MovieDB movieDB = new MovieDB(movie);
                        movieDB.save();
                        favButton.setText("Unmark As Favorite");
                    }
                }
            });
            // Append the Videos to the videos list
            for(int i=0;i < movie.getVideos().size();i++){
                arrayList.add("Trailer " + (i + 1));
            }
            // Because of the listview inside the scrollview
            // I had to set the height manually to show all the items
            ViewGroup.LayoutParams params = videosContainer.getLayoutParams();
            params.height = movie.getVideos().size() * 160;
            params.width = LinearLayout.LayoutParams.MATCH_PARENT;
            videosContainer.setLayoutParams(params);
            arrayAdapter.notifyDataSetChanged();
            // Append the reviews if exists
            if(movie.getReviews().size() > 0) {
                for (int i = 0; i < movie.getReviews().size(); i++) {
                    reviews.append(movie.getReviews().get(i).getAuthor() + " : " + movie.getReviews().get(i).getContent() + "\n");
                    reviews.append("------- \n");
                }
            }else{
                reviews.setText("There are no reviews");
            }
        }
    }
}
