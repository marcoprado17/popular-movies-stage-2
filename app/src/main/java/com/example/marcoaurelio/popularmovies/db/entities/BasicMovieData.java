/**
 * Copyright (c) 2016 Marco Aurélio Prado dos Santos Vidoca.
 */

package com.example.marcoaurelio.popularmovies.db.entities;

import com.example.marcoaurelio.popularmovies.db.FavoritesDbContract;
import com.google.gson.annotations.SerializedName;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;

@StorIOSQLiteType(table = FavoritesDbContract.Movie.TABLE_NAME)
public class BasicMovieData {
    @SerializedName("id")
    @StorIOSQLiteColumn(name = FavoritesDbContract.COLUMN_MOVIE_ID, key = true)
    public Integer id;

    @SerializedName("poster_path")
    @StorIOSQLiteColumn(name = FavoritesDbContract.Movie.COLUMN_POSTER_PATH)
    public String posterPath;

    @SerializedName("original_title")
    @StorIOSQLiteColumn(name = FavoritesDbContract.Movie.COLUMN_ORIGINAL_TITLE)
    public String originalTitle;

    @SerializedName("overview")
    @StorIOSQLiteColumn(name = FavoritesDbContract.Movie.COLUMN_OVERVIEW)
    public String overview;

    @SerializedName("vote_average")
    @StorIOSQLiteColumn(name = FavoritesDbContract.Movie.COLUMN_USER_RATING)
    public Double userRating;

    @SerializedName("release_date")
    @StorIOSQLiteColumn(name = FavoritesDbContract.Movie.COLUMN_RELEASE_DATE)
    public String releaseDate;

    @SerializedName("backdrop_path")
    @StorIOSQLiteColumn(name = FavoritesDbContract.Movie.COLUMN_BACKDROP_PATH)
    public String backdropPath;

    @StorIOSQLiteColumn(name = FavoritesDbContract.Movie.COLUMN_TRAILERS_CORRECTLY_FETCHED)
    // Use integers 0 (false) | 1 (true) because SQLite doesn't support boolean type
    public Integer trailersFetched;

    @StorIOSQLiteColumn(name = FavoritesDbContract.Movie.COLUMN_REVIEWS_CORRECTLY_FETCHED)
    // Use integers 0 (false) | 1 (true) because SQLite doesn't support boolean type
    public Integer reviewsFetched;

    public BasicMovieData() {
    }

    @Override
    public String toString() {
        String s = "";
        s += "id: " + id + "\n";
        s += "posterPath: " + posterPath + "\n";
        s += "originalTitle: " + originalTitle + "\n";
        s += "overview: " + overview + "\n";
        s += "userRating: " + userRating + "\n";
        s += "releaseDate: " + releaseDate + "\n";
        return s;
    }
}
