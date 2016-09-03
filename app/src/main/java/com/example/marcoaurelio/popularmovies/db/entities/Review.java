/**
 * Copyright (C) 2016 Marco Aurélio Prado dos Santos Vidoca.
 */

package com.example.marcoaurelio.popularmovies.db.entities;

import com.example.marcoaurelio.popularmovies.db.FavoritesDbContract;
import com.google.gson.annotations.SerializedName;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;

@StorIOSQLiteType(table = FavoritesDbContract.Review.TABLE_NAME)
public class Review {
    @StorIOSQLiteColumn(name = FavoritesDbContract.COLUMN_MOVIE_ID)
    public int movieId;

    @StorIOSQLiteColumn(name = FavoritesDbContract.COLUMN_SORT_ID)
    public int sortId;

    @SerializedName("id")
    @StorIOSQLiteColumn(name = FavoritesDbContract.COLUMN_ID, key = true)
    public String id;

    @SerializedName("author")
    @StorIOSQLiteColumn(name = FavoritesDbContract.Review.COLUMN_AUTHOR)
    public String author;

    @SerializedName("content")
    @StorIOSQLiteColumn(name = FavoritesDbContract.Review.COLUMN_CONTENT)
    public String content;

    public Review() {
    }

    @Override
    public String toString() {
        return "Review " + id + " | " + author;
    }
}
