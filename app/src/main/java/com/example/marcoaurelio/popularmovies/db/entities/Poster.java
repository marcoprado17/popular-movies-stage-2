/**
 * Copyright (c) 2016 Marco Aurélio Prado dos Santos Vidoca.
 */

package com.example.marcoaurelio.popularmovies.db.entities;

import android.graphics.Bitmap;

import com.example.marcoaurelio.popularmovies.db.FavoritesDbContract;
import com.example.marcoaurelio.popularmovies.utils.Utils;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;

@StorIOSQLiteType(table = FavoritesDbContract.Poster.TABLE_NAME)
public class Poster {
    @StorIOSQLiteColumn(name = FavoritesDbContract.COLUMN_MOVIE_ID, key = true)
    public Integer movieId;

    @StorIOSQLiteColumn(name = FavoritesDbContract.Poster.COLUMN_POSTER)
    public byte[] posterAsByteArray;

    public Poster() {
    }

    public Poster(int movieId, Bitmap posterAsBitmap){
        this.movieId = movieId;
        posterAsByteArray = Utils.bitmapToByteArray(posterAsBitmap);
    }

    public Bitmap getAsBitmap(){
        return Utils.byteArrayToBitmap(posterAsByteArray);
    }
}
