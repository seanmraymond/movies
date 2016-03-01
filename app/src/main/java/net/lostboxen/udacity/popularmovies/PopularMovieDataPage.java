package net.lostboxen.udacity.popularmovies;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PopularMovieDataPage {
    private final String LOG_TAG = PopularMovieDataPage.class.getSimpleName();

    private String movieJSONString;
    private int page;
    private JSONArray results;
    private int totalResults;
    private int totalPages;

    public ArrayList<PopularMovieData> movies;

    private final int THEMOVIEDB_MAX_RESULTS_PER_PAGE = 20;
    private final String THEMOVIEDB_PAGE = "page";
    private final String THEMOVIEDB_RESULTS = "results";
    private final String THEMOVIEDB_TOTAL_RESULTS = "total_results";
    private final String THEMOVIEDB_TOTAL_PAGES = "total_pages";

    public PopularMovieDataPage(String movieJSONString) {
        this.movieJSONString = movieJSONString;

        try {
            parseJSON(this.movieJSONString);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
    }

    private void parseJSON(String rawJSONString) throws JSONException {
        JSONObject rawJSON = new JSONObject(rawJSONString);

        this.page = rawJSON.getInt(THEMOVIEDB_PAGE);
        this.results = rawJSON.getJSONArray(THEMOVIEDB_RESULTS);
        this.totalResults = rawJSON.getInt(THEMOVIEDB_TOTAL_RESULTS);
        this.totalPages = rawJSON.getInt(THEMOVIEDB_TOTAL_PAGES);

        this.movies = new ArrayList<PopularMovieData>();
        for(int index = 0; index < this.results.length() && index < THEMOVIEDB_MAX_RESULTS_PER_PAGE; index++) {
            JSONObject movie = results.getJSONObject(index);
            this.movies.add(new PopularMovieData(movie));
        }
    }
}