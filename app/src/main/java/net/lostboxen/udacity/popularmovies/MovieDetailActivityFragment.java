package net.lostboxen.udacity.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieDetailActivityFragment extends Fragment {

    public MovieDetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        Intent intent = getActivity().getIntent();
        PopularMovieData movieData = intent.getParcelableExtra("movie_poster_detil");

        ((TextView) rootView.findViewById(R.id.detail_title_textview)).setText(movieData.title);
        ((TextView) rootView.findViewById(R.id.detail_date_textview)).setText(movieData.releaseDate);
        ((TextView) rootView.findViewById(R.id.detail_rating_textview)).setText(movieData.ratingString());
        ((TextView) rootView.findViewById(R.id.detail_overview_textview)).setText(movieData.overview);
        Picasso.with(getActivity()).load(movieData.posterURL()).into((ImageView) rootView.findViewById(R.id.detail_poster_imageview));

        return rootView;
    }
}
