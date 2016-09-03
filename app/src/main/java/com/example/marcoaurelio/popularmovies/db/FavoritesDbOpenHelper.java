/**
 * Copyright (c) 2016 Marco Aurélio Prado dos Santos Vidoca.
 */

package com.example.marcoaurelio.popularmovies.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FavoritesDbOpenHelper extends SQLiteOpenHelper {
    public FavoritesDbOpenHelper(Context context) {
        super(context, FavoritesDbContract.DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(FavoritesDbContract.Movie.getCreateTableQuery());
        db.execSQL(FavoritesDbContract.Trailer.getCreateTableQuery());
        db.execSQL(FavoritesDbContract.Review.getCreateTableQuery());
        db.execSQL(FavoritesDbContract.Poster.getCreateTableQuery());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // no impl
    }
}
