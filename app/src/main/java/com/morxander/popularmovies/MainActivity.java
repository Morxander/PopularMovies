package com.morxander.popularmovies;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

    static public ArrayList<Movie> moviesArrayList;
    static public ArrayList<String> imagesUrls;
    static ImageAdapter imageAdapter;
    static GridView gridview;
    public static ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            setHasOptionsMenu(true);
            progress = ProgressDialog.show(rootView.getContext(), "Loading",
                    "Please Wait...", true);
            initComponents(rootView);
            return rootView;
        }

        public void initComponents(View view) {
            moviesArrayList = new ArrayList<Movie>();
            imagesUrls = new ArrayList<String>();
            gridview = (GridView) view.findViewById(R.id.gridview);
            imageAdapter = new ImageAdapter(getActivity());
            gridview.setAdapter(imageAdapter);
            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {
                    String title = moviesArrayList.get(position).getTitle();
                    Toast.makeText(getActivity(), title,
                            Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), MovieDetails.class);
                    intent.putExtra("movie_id",moviesArrayList.get(position).getMovieId());
                    intent.putExtra("movie_position",position);
                    startActivity(intent);
                }
            });
        }

        public void updateMovies() {
            SharedPreferences sharedPrefs =
                    PreferenceManager.getDefaultSharedPreferences(getActivity());
            String sortingOrder = sharedPrefs.getString(
                    getString(R.string.pref_sorting_order_key),
                    getString(R.string.pref_sorting_order_default_value));

            String sortingCriteria = sharedPrefs.getString(getString(R.string.pref_sorting_criteria_key), getString(R.string.pref_sorting_criteria_default_value));
            new GetMovies().execute(sortingOrder, sortingCriteria, null);
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
                startActivity(intent);
                return true;
            }

            return super.onOptionsItemSelected(item);
        }

        @Override
        public void onResume() {
            Toast.makeText(getActivity(), "Updating", Toast.LENGTH_SHORT).show();
            initComponents(getView());
            updateMovies();
            super.onResume();
        }
    }
}
