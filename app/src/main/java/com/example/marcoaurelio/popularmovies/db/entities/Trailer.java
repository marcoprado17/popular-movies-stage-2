/**
 * Copyright (C) 2016 Marco Aurélio Prado dos Santos Vidoca.
 */

package com.example.marcoaurelio.popularmovies.db.entities;

import com.example.marcoaurelio.popularmovies.db.FavoritesDbContract;
import com.google.gson.annotations.SerializedName;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;

@StorIOSQLiteType(table = FavoritesDbContract.Trailer.TABLE_NAME)
public class Trailer {
    @StorIOSQLiteColumn(name = FavoritesDbContract.COLUMN_MOVIE_ID)
    public int movieId;

    @StorIOSQLiteColumn(name = FavoritesDbContract.COLUMN_SORT_ID)
    public int sortId;

    @SerializedName("id")
    @StorIOSQLiteColumn(name = FavoritesDbContract.COLUMN_ID, key = true)
    public String id;

    @SerializedName("key")
    @StorIOSQLiteColumn(name = FavoritesDbContract.Trailer.COLUMN_KEY)
    public String key;

    @SerializedName("name")
    @StorIOSQLiteColumn(name = FavoritesDbContract.Trailer.COLUMN_NAME)
    public String name;

    @SerializedName("site")
    @StorIOSQLiteColumn(name = FavoritesDbContract.Trailer.COLUMN_SITE)
    public String site;

    public Trailer() {
    }

    @Override
    public String toString() {
        return "Trailer: " + name + " | " + id + " | " + key;
    }
}
