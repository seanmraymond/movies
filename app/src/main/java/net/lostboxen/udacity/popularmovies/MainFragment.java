package net.lostboxen.udacity.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainFragment extends Fragment {

    public PosterCustomAdapter posterAdapter;
    public ArrayList<PopularMovieData> posterList;

    SharedPreferences preferences;
    private Boolean posterOrderChanged;

    public final String POSTER_DATA_KEY = "poster_data";

    public MainFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("==DEBUG==", "ON_CREATE");
        if (savedInstanceState != null && savedInstanceState.containsKey(POSTER_DATA_KEY)) {
            posterList = savedInstanceState.getParcelableArrayList(POSTER_DATA_KEY);
        } else {
            posterList = new ArrayList<PopularMovieData>();
            updateMoviePosters();
        }

        posterOrderChanged = false;
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated( Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.e("==DEBUG==", "ON_ACTIVITY_CREATED");
        this.preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
    }

    @Override
    public void onPause() {
        super.onPause();

        Log.e("==DEBUG==", "ON_PAUSE");
        if (this.preferences != null) {
            registerSettingListener();
        }
    }

    public void registerSettingListener() {
        Log.e("==DEBUG==", "REGISTER_SETTING_LISTENER");
        SharedPreferences.OnSharedPreferenceChangeListener prefListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                if (key.equals(getString(R.string.pref_sort_key))) {
                    posterOrderChanged = true;
                }
            }
        };
        this.preferences.registerOnSharedPreferenceChangeListener(prefListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("==DEBUG==", "ON_RESUME");
        if (posterOrderChanged) {
            updateMoviePosters();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.e("==DEBUG==", "ON_SAVEINSTANCESTATE");
        if (posterAdapter != null) {
            ArrayList<PopularMovieData> posterData = posterAdapter.getItems();
            if (posterData != null) {
                outState.putParcelableArrayList(POSTER_DATA_KEY, posterData);
            }
        }

        super.onSaveInstanceState(outState);
    }

    public void updateMoviePosters() {
        Log.e("==DEBUG==", "UPDATE_MOVIE_POSTERS");
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sort_order = preferences.getString(getString(R.string.pref_sort_key), getString(R.string.pref_sort_value_popularity_desc));

        FetchPopularMoviesTask posterTask = new FetchPopularMoviesTask();
        posterTask.execute(sort_order);
        posterOrderChanged = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        posterAdapter = new PosterCustomAdapter(
                getActivity(),
                R.layout.grid_item_poster,
                posterList);

        GridView gridView = (GridView) rootView.findViewById(R.id.gridview_posters);
        gridView.setAdapter(posterAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                PopularMovieData poster = posterAdapter.getItem(position);

                Intent detailIntent = new Intent(getActivity(), MovieDetailActivity.class);
                detailIntent.putExtra("movie_poster_detil", poster);
                startActivity(detailIntent);
            }
        });

        return rootView;
    }

    public class FetchPopularMoviesTask extends AsyncTask<String, Void, PopularMovieDataPage> {
        private final String LOG_TAG = FetchPopularMoviesTask.class.getSimpleName();

        @Override
        protected PopularMovieDataPage doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String moviesJSONStr = null;

            try {
                final String THEMOVIEDB_SORT = "sort_by";
                final String THEMOVIEDB_KEY = "api_key";

                // Build URL for themoviedb API call
                Uri.Builder builder = new Uri.Builder();
                builder.scheme("http")
                        .authority("api.themoviedb.org")
                        .appendPath("3")
                        .appendPath("discover")
                        .appendPath("movie")
                        .appendQueryParameter(THEMOVIEDB_SORT, params[0])
                        .appendQueryParameter(THEMOVIEDB_KEY, getString(R.string.themoviedb_api_key, "MissingAPIKey"));
                URL url = new URL(builder.build().toString());

                // Connect to themoviedb
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream for JSON response
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }
                if (buffer.length() == 0) {
                    return null;
                }

                moviesJSONStr = buffer.toString();

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;

            } finally {
                // Close connection and reader IO stream
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            return new PopularMovieDataPage(moviesJSONStr);
        }

        @Override
        protected void onPostExecute(PopularMovieDataPage posterResult) {
            super.onPostExecute(posterResult);
            Log.e("==DEBUG==", "ON_POSTEXECUTE");
            if (posterResult != null) {
                posterAdapter.clear();
                posterAdapter.addAll(posterResult.movies);
            }
        }
    }

}

