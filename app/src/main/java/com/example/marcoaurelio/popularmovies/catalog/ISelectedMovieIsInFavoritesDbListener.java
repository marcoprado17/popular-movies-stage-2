/**
 * Copyright (c) 2016 Marco Aurélio Prado dos Santos Vidoca.
 */

package com.example.marcoaurelio.popularmovies.catalog;

public interface ISelectedMovieIsInFavoritesDbListener {
    void deleteSelectedMovieFromFavoritesDb();
    void putSelectedMovieToFavoritesDb();
}
