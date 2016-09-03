/**
 * Copyright (C) 2016 Marco Aurélio Prado dos Santos Vidoca.
 */

package com.example.marcoaurelio.popularmovies.db;

import android.provider.BaseColumns;

// TODO: Discover why are two columns movie_id in movies and poster tables
public final class FavoritesDbContract {
    public static final String DATABASE_NAME = "favorites";

    public static final String COLUMN_ID = "id__";
    public static final String COLUMN_MOVIE_ID = "movie_id__";
    public static final String COLUMN_SORT_ID = "sort_id__";

    public static final class Movie {
        public static final String TABLE_NAME = "movies__";

        public static final String COLUMN_POSTER_PATH = "poster_url__";
        public static final String COLUMN_ORIGINAL_TITLE = "original_title__";
        public static final String COLUMN_OVERVIEW = "overview__";
        public static final String COLUMN_USER_RATING = "user_rating__";
        public static final String COLUMN_RELEASE_DATE = "release_date__";
        public static final String COLUMN_TRAILERS_CORRECTLY_FETCHED = "trailers_correctly_fetched__";
        public static final String COLUMN_REVIEWS_CORRECTLY_FETCHED = "reviews_correctly_fetched__";
        public static final String COLUMN_BACKDROP_PATH = "backdrop_path__";

        public static String getCreateTableQuery(){
            return "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_MOVIE_ID + " INTEGER NOT NULL PRIMARY KEY, " +
                    COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                    COLUMN_ORIGINAL_TITLE + " TEXT NOT NULL, " +
                    COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                    COLUMN_USER_RATING + " REAL NOT NULL, " +
                    COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                    COLUMN_TRAILERS_CORRECTLY_FETCHED + " INTEGER NOT NULL, " +
                    COLUMN_REVIEWS_CORRECTLY_FETCHED + " INTEGER NOT NULL, " +
                    COLUMN_BACKDROP_PATH + " TEXT NOT NULL" + ");";
        }
    }

    public static final class Trailer {
        public static final String TABLE_NAME = "trailers__";

        public static final String COLUMN_KEY = "key__";
        public static final String COLUMN_NAME = "name__";
        public static final String COLUMN_SITE = "site__";

        public static String getCreateTableQuery(){
            return "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " TEXT NOT NULL PRIMARY KEY, " +
                    COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                    COLUMN_SORT_ID + " INTEGER NOT NULL, " +
                    COLUMN_KEY + " TEXT NOT NULL, " +
                    COLUMN_NAME + " TEXT NOT NULL, " +
                    COLUMN_SITE + " TEXT NOT NULL " + ");";
        }
    }

    public static final class Review {
        public static final String TABLE_NAME = "reviews__";

        public static final String COLUMN_AUTHOR = "author__";
        public static final String COLUMN_CONTENT = "content__";

        public static String getCreateTableQuery(){
            return "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " TEXT NOT NULL PRIMARY KEY, " +
                    COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                    COLUMN_SORT_ID + " INTEGER NOT NULL, " +
                    COLUMN_AUTHOR + " TEXT NOT NULL, " +
                    COLUMN_CONTENT + " TEXT NOT NULL " + ");";
        }
    }

    public static final class Poster implements BaseColumns {
        public static final String TABLE_NAME = "posters__";

        public static final String COLUMN_POSTER = "poster__";

        public static String getCreateTableQuery(){
            return "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_MOVIE_ID + " INTEGER NOT NULL PRIMARY KEY, " +
                    COLUMN_POSTER + " BLOB NOT NULL " + ");";
        }
    }
}
