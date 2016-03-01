package net.lostboxen.udacity.popularmovies;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PopularMovieData implements Parcelable {
    private final String LOG_TAG = PopularMovieData.class.getSimpleName();

    public String posterPath;
    public Boolean adult;
    public String overview;
    public String releaseDate;
    public int[] genreIDs;
    public int id;
    public String originalTitle;
    public String originalLanguage;
    public String title;
    public String backdropPath;
    public double popularity;
    public int voteCount;
    public Boolean video;
    public double voteAverage;



    public PopularMovieData(JSONObject movie){
        final String THEMOVIEDB_POSTER_PATH = "poster_path";
        final String THEMOVIEDB_ADULT = "adult";
        final String THEMOVIEDB_OVERVIEW = "overview";
        final String THEMOVIEDB_RELEASE_DATE = "release_date";
        final String THEMOVIEDB_GENRE_IDS = "genre_ids";
        final String THEMOVIEDB_ID = "id";
        final String THEMOVIEDB_ORIGINAL_TITLE = "original_title";
        final String THEMOVIEDB_ORIGINAL_LANGUAGE = "original_language";
        final String THEMOVIEDB_TITLE = "title";
        final String THEMOVIEDB_BACKDROP_PATH = "backdrop_path";
        final String THEMOVIEDB_POPULARITY = "popularity";
        final String THEMOVIEDB_VOTE_COUNT = "vote_count";
        final String THEMOVIEDB_VIDEO = "video";
        final String THEMOVIEDB_VOTE_AVERAGE = "vote_average";

        try {
            this.posterPath = movie.getString(THEMOVIEDB_POSTER_PATH);
            this.adult = movie.getBoolean(THEMOVIEDB_ADULT);
            this.overview = movie.getString(THEMOVIEDB_OVERVIEW);
            this.releaseDate = movie.getString(THEMOVIEDB_RELEASE_DATE);

            JSONArray genreIDs = movie.getJSONArray(THEMOVIEDB_GENRE_IDS);
            this.genreIDs = new int[genreIDs.length()];
            for (int index = 0; index < genreIDs.length(); index++){
                this.genreIDs[index] = genreIDs.getInt(index);
            }

            this.id = movie.getInt(THEMOVIEDB_ID);
            this.originalTitle = movie.getString(THEMOVIEDB_ORIGINAL_TITLE);
            this.originalLanguage = movie.getString(THEMOVIEDB_ORIGINAL_LANGUAGE);
            this.title = movie.getString(THEMOVIEDB_TITLE);
            this.backdropPath = movie.getString(THEMOVIEDB_BACKDROP_PATH);
            this.popularity = movie.getDouble(THEMOVIEDB_POPULARITY);
            this.voteCount = movie.getInt(THEMOVIEDB_VOTE_COUNT);
            this.video = movie.getBoolean(THEMOVIEDB_VIDEO);
            this.voteAverage = movie.getDouble(THEMOVIEDB_VOTE_AVERAGE);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
    }

    private PopularMovieData(Parcel parcel) {
        this.posterPath = parcel.readString();
        this.adult = parcel.readByte() != 0;
        this.overview = parcel.readString();
        this.releaseDate = parcel.readString();
        this.genreIDs = parcel.createIntArray();
        this.id = parcel.readInt();
        this.originalTitle = parcel.readString();
        this.originalLanguage = parcel.readString();
        this.title = parcel.readString();
        this.backdropPath = parcel.readString();
        this.popularity = parcel.readDouble();
        this.voteCount = parcel.readInt();
        this.video = parcel.readByte() != 0;
        this.voteAverage = parcel.readDouble();
    }

    public String posterURL() {
        final String THEMOVIEDB_IMAGE_BASE = "http://image.tmdb.org/t/p/";
        final String THEMOVIEDB_IMAGE_SIZE = "w185";

        return Uri.parse(THEMOVIEDB_IMAGE_BASE + THEMOVIEDB_IMAGE_SIZE + posterPath).toString();
    }

    public String ratingString() {
        final String RATING_DELIMETER = " / ";
        final String RATING_MAX = "10.0";

        return Double.toString(this.voteAverage) + RATING_DELIMETER + RATING_MAX;
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(this.posterPath);
        parcel.writeByte((byte) (this.adult ? 1 : 0));
        parcel.writeString(this.overview);
        parcel.writeString(this.releaseDate);
        parcel.writeIntArray(this.genreIDs);
        parcel.writeInt(this.id);
        parcel.writeString(this.originalTitle);
        parcel.writeString(this.originalLanguage);
        parcel.writeString(this.title);
        parcel.writeString(this.backdropPath);
        parcel.writeDouble(this.popularity);
        parcel.writeInt(this.voteCount);
        parcel.writeByte((byte) (this.video ? 1 : 0));
        parcel.writeDouble(this.voteAverage);
    }

    public static final Parcelable.Creator<PopularMovieData> CREATOR = new Parcelable.Creator<PopularMovieData>() {
        @Override
        public PopularMovieData createFromParcel(Parcel parcel) { return new PopularMovieData(parcel); }

        @Override
        public PopularMovieData[] newArray(int length) { return new PopularMovieData[length]; }
    };

}


