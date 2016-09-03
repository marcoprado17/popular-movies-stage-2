/**
 * Copyright (c) 2016 Marco Aurélio Prado dos Santos Vidoca.
 */

package com.example.marcoaurelio.popularmovies;

import android.app.Application;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Looper;

import com.example.marcoaurelio.popularmovies.themoviedbapi.ITheMovieDbApiEndpoints;
import com.example.marcoaurelio.popularmovies.db.FavoritesDbManager;
import com.example.marcoaurelio.popularmovies.db.FavoritesDbOpenHelper;
import com.example.marcoaurelio.popularmovies.db.entities.BasicMovieData;
import com.example.marcoaurelio.popularmovies.db.entities.BasicMovieDataSQLiteTypeMapping;
import com.example.marcoaurelio.popularmovies.models.MovieData;
import com.example.marcoaurelio.popularmovies.db.entities.Poster;
import com.example.marcoaurelio.popularmovies.db.entities.PosterSQLiteTypeMapping;
import com.example.marcoaurelio.popularmovies.db.entities.Review;
import com.example.marcoaurelio.popularmovies.db.entities.ReviewSQLiteTypeMapping;
import com.example.marcoaurelio.popularmovies.db.entities.Trailer;
import com.example.marcoaurelio.popularmovies.db.entities.TrailerSQLiteTypeMapping;
import com.example.marcoaurelio.popularmovies.exceptions.AccessInvalidOutsideUiThreadException;
import com.example.marcoaurelio.popularmovies.themoviedbapi.TheMovieDbApiManager;
import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.impl.DefaultStorIOSQLite;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Store variables that are used in the context of the application.
 */
// TODO: Use Dependency Injection to provide references
// TODO: Use Dagger to automate the generation of DI's
// TODO: Use @null and @nullable annotation
// TODO: Use sync adapter to send a notification to the user when a new movie appears (only to train sync adapter and notification)
// TODO: Put/delete movie to/from favorites when close application in detail view
public class MyApp extends Application {

    private static MyApp sInstance;

    private boolean mTwoPane;
    private int mCatalogExhibition;
    private MovieData mSelectedMovieData;
    private StorIOSQLite mStorioSQLite;
    private FavoritesDbManager mFavoritesDbManager;
    private TheMovieDbApiManager mTheMovieDbApiManager;
    private ITheMovieDbApiEndpoints mApiService;

    @Override
    public void onCreate() {
        sInstance = this;
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }

    private static void assertAccessOnlyOnUiThread() {
        if (!Looper.getMainLooper().equals(Looper.myLooper())) {
            throw new AccessInvalidOutsideUiThreadException();
        }
    }

    public static void setTwoPane(boolean value) {
        sInstance.mTwoPane = value;
    }

    public static boolean getTwoPane() {
        return sInstance.mTwoPane;
    }

    public static int getCatalogExhibition() {
        assertAccessOnlyOnUiThread();
        return sInstance.mCatalogExhibition;
    }

    public static void setCatalogExhibition(int newExhibition) {
        assertAccessOnlyOnUiThread();
        sInstance.mCatalogExhibition = newExhibition;
    }

    public static MovieData getSelectedMovieData() {
        assertAccessOnlyOnUiThread();
        return sInstance.mSelectedMovieData;
    }

    public static void setSelectedMovieData(MovieData movieData) {
        assertAccessOnlyOnUiThread();
        sInstance.mSelectedMovieData = movieData;
    }

    public static StorIOSQLite getStorioSQLite() {
        if (sInstance.mStorioSQLite == null) {
            SQLiteOpenHelper sqLiteOpenHelper = new FavoritesDbOpenHelper(sInstance);
            sInstance.mStorioSQLite = DefaultStorIOSQLite.builder()
                    .sqliteOpenHelper(sqLiteOpenHelper)
                    .addTypeMapping(BasicMovieData.class, new BasicMovieDataSQLiteTypeMapping())
                    .addTypeMapping(Trailer.class, new TrailerSQLiteTypeMapping())
                    .addTypeMapping(Review.class, new ReviewSQLiteTypeMapping())
                    .addTypeMapping(Poster.class, new PosterSQLiteTypeMapping())
                    .build();
        }
        return sInstance.mStorioSQLite;
    }

    public static FavoritesDbManager getFavoritesDbManager() {
        if (sInstance.mFavoritesDbManager == null) {
            sInstance.mFavoritesDbManager = new FavoritesDbManager();
        }
        return sInstance.mFavoritesDbManager;
    }

    public static TheMovieDbApiManager getTheMovieDbApiManager() {
        if (sInstance.mTheMovieDbApiManager == null) {
            sInstance.mTheMovieDbApiManager = new TheMovieDbApiManager();
        }
        return sInstance.mTheMovieDbApiManager;
    }

    private static Retrofit newRetrofitInstance() {
        OkHttpClient okClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .addInterceptor(
                        chain -> {
                            Request request = chain.request();
                            HttpUrl url = request.url().newBuilder().addQueryParameter(Config.API_KEY_QUERY_NAME, Config.API_KEY).build();
                            request = request.newBuilder().url(url).build();
                            return chain.proceed(request);
                        })
                .build();

        RxJavaCallAdapterFactory rxAdapter = RxJavaCallAdapterFactory.create();

        return new Retrofit.Builder()
                .client(okClient)
                .baseUrl(Config.BASE_TMDB_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(rxAdapter)
                .build();
    }

    public static ITheMovieDbApiEndpoints getTheMovieDbApiEndpoints() {
        if (sInstance.mApiService == null) {
            sInstance.mApiService = newRetrofitInstance().create(ITheMovieDbApiEndpoints.class);
        }
        return sInstance.mApiService;
    }
}
