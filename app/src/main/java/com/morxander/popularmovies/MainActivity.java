package com.morxander.popularmovies;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

//    static public
    private static boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (findViewById(R.id.movie_detail_container) != null) {
            mTwoPane = true;
                getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_main, new PlaceholderFragment())
                    .commit();
        }else{
            mTwoPane = false;
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        Bundle bundle = new Bundle();
//        outState.putAll(outPersistentState.getB
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment implements MainJobsInterface {

        ImageAdapter imageAdapter;
        GridView gridview;
        Toast toast;
        String sortingCriteria,sortingOrder;
        ProgressDialog progress;
        final String DETAILFRAGMENT_TAG = "DFTAG";
        ArrayList<String> imagesUrls;
        ArrayList<Movie> moviesArrayList;
        int SETTING_CODE = 666;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            setHasOptionsMenu(true);

            if (savedInstanceState == null) {
                initComponents(rootView,true);
                updateMovies(rootView);
            }else{
            initComponents(rootView,false);
            moviesArrayList = savedInstanceState.getParcelableArrayList("moviesList");
            imagesUrls = savedInstanceState.getStringArrayList("imagesLinks");
            }
            setRetainInstance(true);
            return rootView;
        }

        public void initComponents(View view,boolean savedInstance) {
            // If the movieArraylist and the imagesUrlsList are already exists or not?
            if(savedInstance) {
                moviesArrayList = new ArrayList<Movie>();
                imagesUrls = new ArrayList<String>();
            }
            gridview = (GridView) view.findViewById(R.id.gridview);
            imageAdapter = new ImageAdapter(getActivity(),this);
            gridview.setAdapter(imageAdapter);
            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {
                    if (mTwoPane) {
                        // In two-pane mode, show the detail view in this activity by
                        // adding or replacing the detail fragment using a
                        // fragment transaction.
                        Bundle args = new Bundle();
                        args.putInt("movie_id", moviesArrayList.get(position).getMovieId());
                        args.putInt("movie_position", position);
                        args.putParcelableArrayList("moviesList",moviesArrayList);
                        MovieDetails.PlaceholderDetailsFragment fragment = new MovieDetails.PlaceholderDetailsFragment();
                        fragment.setArguments(args);

                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.movie_detail_container, fragment, DETAILFRAGMENT_TAG)
                                .commit();
                    } else {
                        Intent intent = new Intent(getActivity(), MovieDetails.class);
                        intent.putExtra("movie_id", moviesArrayList.get(position).getMovieId());
                        intent.putExtra("movie_position", position);
                        intent.putExtra("movie_title", moviesArrayList.get(position).getTitle());
                        intent.putExtra("movie_poster", moviesArrayList.get(position).getPosterPath());
                        startActivity(intent);
                    }

                }
            });
            // Temp toast in case of errors
            toast = Toast.makeText(view.getContext(),"", Toast.LENGTH_SHORT);
        }

        public void updateMovies(View view) {
            SharedPreferences sharedPrefs =
                    PreferenceManager.getDefaultSharedPreferences(getActivity());
            sortingOrder = sharedPrefs.getString(
                    getString(R.string.pref_sorting_order_key),
                    getString(R.string.pref_sorting_order_default_value));

            sortingCriteria = sharedPrefs.getString(getString(R.string.pref_sorting_criteria_key), getString(R.string.pref_sorting_criteria_default_value));
            new GetMovies().execute(sortingOrder, sortingCriteria, this);
            gridview.setAdapter(imageAdapter);
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            inflater.inflate(R.menu.main_fragment, menu);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();

            //noinspection SimplifiableIfStatement
            if (id == R.id.action_fragment_settings) {
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                startActivityForResult(intent, SETTING_CODE);
                return true;
            }

            return super.onOptionsItemSelected(item);
        }

        @Override
        public void onResume() {
            super.onResume();
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == SETTING_CODE){
                Toast.makeText(getActivity(), "Updating", Toast.LENGTH_SHORT).show();
                updateMovies(getView());
            }
        }

        @Override
        public void onConnectionError() {
            toast.setText("Connection Error");
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.show();
        }

        @Override
        public void somethingWrongHappened() {
            toast.setText("Something Wrong Happend");
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.show();
        }

        @Override
        public void clearImagesUrls() {
            imagesUrls.clear();
        }

        @Override
        public void addToImagesUrls(String url) {
            imagesUrls.add(url);
        }

        @Override
        public void clearMoviesList() {
            moviesArrayList.clear();
        }

        @Override
        public void addToMoviesList(Movie movie) {
            moviesArrayList.add(movie);
        }

        @Override
        public void notifyAdapter() {
            imageAdapter.notifyDataSetChanged();
        }

        public ArrayList<String> getImagesUrls() {
            return  imagesUrls;
        }

        @Override
        public void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);
            outState.putParcelableArrayList("moviesList", moviesArrayList);
            outState.putStringArrayList("imagesLinks",imagesUrls);
        }

    }

}
