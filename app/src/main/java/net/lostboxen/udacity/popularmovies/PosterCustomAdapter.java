package net.lostboxen.udacity.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PosterCustomAdapter extends ArrayAdapter<PopularMovieData> {
    private final String LOG_TAG = PosterCustomAdapter.class.getSimpleName();

        Context context;
        int layoutResourceID;

    public PosterCustomAdapter(Context context, int layoutResourceID, ArrayList<PopularMovieData> popularMovies) {
        super(context, layoutResourceID, popularMovies);

        this.context = context;
        this.layoutResourceID = layoutResourceID;
    }

    static class PosterViewHolder {
        ImageView imageView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PosterViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = ((Activity) this.context).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceID, parent, false);

            viewHolder = new PosterViewHolder();
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.grid_item_poster_imageview);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (PosterViewHolder) convertView.getTag();
        }

        PopularMovieData movieData = getItem(position);

        if (movieData != null && movieData.posterURL() != null) {
            Picasso.with(getContext()).load(movieData.posterURL()).into(viewHolder.imageView);
        }

        return convertView;
    }

    // Means for retrieving Array items from the Adapter that remains consistent with the
    //  list data that is being held in the Adapter
    public ArrayList<PopularMovieData> getItems() {
        ArrayList<PopularMovieData> adapterList = new ArrayList<PopularMovieData>();

        for (int index = 0; index < this.getCount(); index++) {
            adapterList.add(getItem(index));
        }

        return adapterList;
    }
}
