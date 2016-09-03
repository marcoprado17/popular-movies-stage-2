/**
 * Copyright (C) 2016 Marco Aurélio Prado dos Santos Vidoca.
 */

package com.example.marcoaurelio.popularmovies.db.entities;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MovieTrailers {
    @SerializedName("results")
    public List<Trailer> allTrailers = new ArrayList<>();
}
