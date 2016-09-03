/**
 * Copyright (c) 2016 Marco Aurélio Prado dos Santos Vidoca.
 */

package com.example.marcoaurelio.popularmovies.utils;

import com.example.marcoaurelio.popularmovies.Config;

public class ProjUtils {
    public static String getPosterUrl(String posterPath) {
        return Config.POSTER_BASE_URL + posterPath;
    }

    public static String getBackdropUrl(String backdropPath) {
        return Config.BACKDROP_BASE_URL + backdropPath;
    }
}
