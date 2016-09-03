/**
 * Copyright (C) 2016 Marco Aurélio Prado dos Santos Vidoca.
 */

package com.example.marcoaurelio.popularmovies.models;

import android.graphics.Bitmap;

import com.example.marcoaurelio.popularmovies.db.entities.BasicMovieData;
import com.example.marcoaurelio.popularmovies.db.entities.Review;
import com.example.marcoaurelio.popularmovies.db.entities.Trailer;

import java.util.ArrayList;

/**
 * Store all movie data brought from TmdbApi as a java class.
 */
public class MovieData {

    public boolean isFavorite;
    public BasicMovieData basic;
    public Bitmap poster;
    public ArrayList<Trailer> allTrailers;
    public ArrayList<Review> allReviews;

    public MovieData() {
    }

    @Override
    public String toString() {
        String s = "";

        s += "isFavorite: " + isFavorite + "\n";
        s += basic;

        if (allTrailers != null) {
            s += "Trailers: " + "\n";
            for (Trailer trailer : allTrailers) {
                s += trailer + "\n";
            }
        } else {
            s += "Trailers: None" + "\n";
        }

        if (allReviews != null) {
            s += "Reviews: " + "\n";
            for (Review review : allReviews) {
                s += review + "\n";
            }
        } else {
            s += "Reviews: None" + "\n";
        }

        return s;
    }
}



























