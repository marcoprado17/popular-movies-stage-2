/**
 * Copyright (c) 2016 Marco Aurélio Prado dos Santos Vidoca.
 */

package com.example.marcoaurelio.popularmovies.db;

import com.example.marcoaurelio.popularmovies.MyApp;
import com.example.marcoaurelio.popularmovies.exceptions.InvalidMovieDataException;
import com.example.marcoaurelio.popularmovies.db.entities.BasicMovieData;
import com.example.marcoaurelio.popularmovies.models.MovieData;
import com.example.marcoaurelio.popularmovies.db.entities.Poster;
import com.example.marcoaurelio.popularmovies.db.entities.Review;
import com.example.marcoaurelio.popularmovies.db.entities.Trailer;
import com.pushtorefresh.storio.sqlite.queries.DeleteQuery;
import com.pushtorefresh.storio.sqlite.queries.Query;

import org.javatuples.Pair;
import org.javatuples.Quartet;
import org.javatuples.Triplet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import rx.Completable;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Provides access to favorites db through a MovieData object.
 */
public class FavoritesDbManager {

    public void put(MovieData movieData, Completable.CompletableSubscriber completableSubscriber) {
        assertValidMovieData(movieData);

        putObjectCompletable(movieData.basic)
                .subscribeOn(Schedulers.io())
                .andThen(putObjectsCompletable(movieData.allTrailers))
                .subscribeOn(Schedulers.io())
                .andThen(putObjectsCompletable(movieData.allReviews))
                .subscribeOn(Schedulers.io())
                .andThen(putObjectCompletable(new Poster(movieData.basic.id, movieData.poster)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(completableSubscriber);
    }

    private <T> Completable putObjectCompletable(T object) {
        return MyApp.getStorioSQLite()
                .put()
                .object(object)
                .prepare()
                .asRxCompletable();
    }

    private <T> Completable putObjectsCompletable(Collection<T> objects) {
        return MyApp.getStorioSQLite()
                .put()
                .objects(objects)
                .prepare()
                .asRxCompletable();
    }

    public void delete(int movieId, Completable.CompletableSubscriber subscriber) {
        getDeleteAllWithIdCompletable(movieId, FavoritesDbContract.Movie.TABLE_NAME)
                .subscribeOn(Schedulers.io())
                .andThen(getDeleteAllWithIdCompletable(movieId, FavoritesDbContract.Trailer.TABLE_NAME))
                .subscribeOn(Schedulers.io())
                .andThen(getDeleteAllWithIdCompletable(movieId, FavoritesDbContract.Review.TABLE_NAME))
                .subscribeOn(Schedulers.io())
                .andThen(getDeleteAllWithIdCompletable(movieId, FavoritesDbContract.Poster.TABLE_NAME))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    private Completable getDeleteAllWithIdCompletable(int movieId, String tableName) {
        return MyApp.getStorioSQLite()
                .delete()
                .byQuery(DeleteQuery.builder()
                        .table(tableName)
                        .where(FavoritesDbContract.COLUMN_MOVIE_ID + " = ?")
                        .whereArgs(movieId)
                        .build())
                .prepare()
                .asRxCompletable();
    }

    public Subscription getAll(Subscriber<MovieData> subscriber) {
        return Observable.create(new Observable.OnSubscribe<List<BasicMovieData>>() {
            @Override
            public void call(Subscriber<? super List<BasicMovieData>> subscriber) {
                List<BasicMovieData> list = MyApp.getStorioSQLite()
                        .get()
                        .listOfObjects(BasicMovieData.class)
                        .withQuery(Query.builder()
                                .table(FavoritesDbContract.Movie.TABLE_NAME)
                                .build())
                        .prepare()
                        .executeAsBlocking();
                subscriber.onNext(list);
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.io())
                .flatMap(Observable::from)
                .subscribeOn(Schedulers.computation())
                .map(basicMovieData -> {
                    List<Trailer> trailersList = MyApp.getStorioSQLite()
                            .get()
                            .listOfObjects(Trailer.class)
                            .withQuery(Query.builder()
                                    .table(FavoritesDbContract.Trailer.TABLE_NAME)
                                    .where(FavoritesDbContract.COLUMN_MOVIE_ID + " = ?")
                                    .whereArgs(basicMovieData.id)
                                    .build())
                            .prepare()
                            .executeAsBlocking();
                    return new Pair<>(basicMovieData, trailersList);
                })
                .subscribeOn(Schedulers.io())
                .map(pair -> {
                    List<Review> reviewsList = MyApp.getStorioSQLite()
                            .get()
                            .listOfObjects(Review.class)
                            .withQuery(Query.builder()
                                    .table(FavoritesDbContract.Review.TABLE_NAME)
                                    .where(FavoritesDbContract.COLUMN_MOVIE_ID + " = ?")
                                    .whereArgs(pair.getValue0().id)
                                    .build())
                            .prepare()
                            .executeAsBlocking();
                    return new Triplet<>(pair.getValue0(), pair.getValue1(), reviewsList);
                })
                .subscribeOn(Schedulers.io())
                .map(triplet -> {
                    Poster poster = MyApp.getStorioSQLite()
                            .get()
                            .object(Poster.class)
                            .withQuery(Query.builder()
                                    .table(FavoritesDbContract.Poster.TABLE_NAME)
                                    .where(FavoritesDbContract.COLUMN_MOVIE_ID + " = ?")
                                    .whereArgs(triplet.getValue0().id)
                                    .build())
                            .prepare()
                            .executeAsBlocking();
                    return new Quartet<>(triplet.getValue0(), triplet.getValue1(), triplet.getValue2(), poster);
                })
                .subscribeOn(Schedulers.io())
                .map(quartet -> {
                    MovieData movieData = new MovieData();
                    movieData.isFavorite = true;
                    movieData.basic = quartet.getValue0();
                    movieData.poster = quartet.getValue3().getAsBitmap();
                    if (quartet.getValue0().trailersFetched == 1) {
                        movieData.allTrailers = new ArrayList<>(quartet.getValue1());
                    }
                    if (quartet.getValue0().reviewsFetched == 1) {
                        movieData.allReviews = new ArrayList<>(quartet.getValue2());
                    }
                    return movieData;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    // TODO: Always movieData.allTrailers and movieData.allReviews mute be non null
    private void assertValidMovieData(MovieData movieData) {
        if (movieData == null) {
            throw new InvalidMovieDataException();
        }
        if (movieData.allTrailers == null) {
            movieData.allTrailers = new ArrayList<>();
        }
        if (movieData.allReviews == null) {
            movieData.allReviews = new ArrayList<>();
        }
        if (movieData.basic == null
                || movieData.poster == null) {
            throw new InvalidMovieDataException();
        }
    }
}
