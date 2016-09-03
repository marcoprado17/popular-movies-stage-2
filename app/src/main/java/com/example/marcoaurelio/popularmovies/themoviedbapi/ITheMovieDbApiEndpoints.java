/**
 * Copyright (c) 2016 Marco Aurélio Prado dos Santos Vidoca.
 */

package com.example.marcoaurelio.popularmovies.themoviedbapi;

import com.example.marcoaurelio.popularmovies.db.entities.MovieReviews;
import com.example.marcoaurelio.popularmovies.db.entities.MovieTrailers;
import com.example.marcoaurelio.popularmovies.db.entities.MoviesPage;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

public interface ITheMovieDbApiEndpoints {
    @GET("/3/movie/popular")
    Observable<MoviesPage> getFirstMoviesPageSortedByPopularity();

    @GET("/3/movie/top_rated")
    Observable<MoviesPage> getFirstMoviesPageSortedByTopRating();

    @GET("/3/movie/{id}/videos")
    Observable<MovieTrailers> getMovieTrailers(@Path("id") int movieId);

    @GET("/3/movie/{id}/reviews")
    Observable<MovieReviews> getMovieReviews(@Path("id") int movieId);
}
