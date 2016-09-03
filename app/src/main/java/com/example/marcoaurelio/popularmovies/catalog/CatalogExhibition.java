/**
 * Copyright (C) 2016 Marco Aurélio Prado dos Santos Vidoca.
 */

package com.example.marcoaurelio.popularmovies.catalog;

/**
 * Represents the exhibitions mode of the catalog of posters.
 */
public class CatalogExhibition {
    public static final int BY_POPULARITY = 0;
    public static final int BY_TOP_RATING = 1;
    public static final int FAVORITES = 2;

    public static int getDefValue(){
        return BY_POPULARITY;
    }
}
