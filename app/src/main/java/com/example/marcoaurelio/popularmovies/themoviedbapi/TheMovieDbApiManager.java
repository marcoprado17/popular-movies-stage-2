/**
 * Copyright (c) 2016 Marco Aurélio Prado dos Santos Vidoca.
 */

package com.example.marcoaurelio.popularmovies.themoviedbapi;

import android.database.Cursor;

import com.example.marcoaurelio.popularmovies.MyApp;
import com.example.marcoaurelio.popularmovies.catalog.CatalogExhibition;
import com.example.marcoaurelio.popularmovies.db.FavoritesDbContract;
import com.example.marcoaurelio.popularmovies.models.MovieData;
import com.example.marcoaurelio.popularmovies.db.entities.MoviesPage;
import com.example.marcoaurelio.popularmovies.db.entities.Review;
import com.example.marcoaurelio.popularmovies.db.entities.Trailer;
import com.example.marcoaurelio.popularmovies.exceptions.InvalidExhibitionException;
import com.pushtorefresh.storio.sqlite.queries.Query;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TheMovieDbApiManager {
    public Subscription fetchAllMovieData(Subscriber<MovieData> subscriber) {
        Observable<MoviesPage> apiServiceObservable;

        if (MyApp.getCatalogExhibition() == CatalogExhibition.BY_POPULARITY) {
            apiServiceObservable = MyApp.getTheMovieDbApiEndpoints().getFirstMoviesPageSortedByPopularity();
        } else if (MyApp.getCatalogExhibition() == CatalogExhibition.BY_TOP_RATING) {
            apiServiceObservable = MyApp.getTheMovieDbApiEndpoints().getFirstMoviesPageSortedByTopRating();
        } else {
            throw new InvalidExhibitionException();
        }

        return apiServiceObservable
                .subscribeOn(Schedulers.io())
                .flatMap(moviesPage -> Observable.from(moviesPage.allBasicMoviesData))
                .subscribeOn(Schedulers.computation())
                .map(basicMovieData -> {
                    Cursor cursor = MyApp.getStorioSQLite()
                            .get()
                            .cursor()
                            .withQuery(Query.builder()
                                    .table(FavoritesDbContract.Movie.TABLE_NAME)
                                    .where(FavoritesDbContract.COLUMN_MOVIE_ID + " = ?")
                                    .whereArgs(basicMovieData.id)
                                    .build())
                            .prepare()
                            .executeAsBlocking();
                    MovieData movieData = new MovieData();
                    movieData.basic = basicMovieData;
                    movieData.basic.trailersFetched = 0;
                    movieData.basic.reviewsFetched = 0;
                    movieData.isFavorite = cursor.moveToFirst();
                    cursor.close();
                    return movieData;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public Subscription fetchAllTrailersOfMovie(int movieId, Subscriber<Trailer> subscriber) {
        return MyApp.getTheMovieDbApiEndpoints().getMovieTrailers(movieId)
                .subscribeOn(Schedulers.io())
                .flatMap(movieTrailers -> Observable.from(movieTrailers.allTrailers))
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public Subscription fetchAllReviewsOfMovie(int movieId, Subscriber<Review> subscriber) {
        return MyApp.getTheMovieDbApiEndpoints().getMovieReviews(movieId)
                .subscribeOn(Schedulers.io())
                .flatMap(movieReviews -> Observable.from(movieReviews.allReviews))
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
}
