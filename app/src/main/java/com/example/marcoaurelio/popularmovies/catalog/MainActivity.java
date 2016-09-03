/**
 * Copyright (C) 2016 Marco Aurélio Prado dos Santos Vidoca.
 */

package com.example.marcoaurelio.popularmovies.catalog;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.marcoaurelio.popularmovies.MyApp;
import com.example.marcoaurelio.popularmovies.R;
import com.example.marcoaurelio.popularmovies.detail.DetailActivity;
import com.example.marcoaurelio.popularmovies.detail.DetailFragment;

import hugo.weaving.DebugLog;
import rx.Completable;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Contains the CatalogFragment and DetailFragment when in two pane mode. Put/delete movies to/from
 * favorites db. Launch or update (when two pane) the detail view of the selected movie.
 */
public class MainActivity
        extends AppCompatActivity
        implements IOnMovieSelectedListener, ISelectedMovieIsInFavoritesDbListener {

    public static final String CATALOG_FRAGMENT_TAG = "CATALOG_FRAGMENT_TAG";

    private static int sDetailActivityResultCode = 0;

    private boolean mBlockPosterClick;
    private DetailFragment mDetailFragment;
    private CatalogFragment mCatalogFragment;
    private SharedPreferences mPrefs;
    private CompositeSubscription mCompositeSubscription;

    @DebugLog
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        MyApp.setTwoPane(isTwoPane());
        MyApp.setCatalogExhibition(getCatalogExhibitionFromSharedPrefs());
        if(savedInstanceState == null){
            mCatalogFragment = createMovieCatalogFragment();
        }
        else{
            mCatalogFragment = (CatalogFragment) getSupportFragmentManager().findFragmentByTag(CATALOG_FRAGMENT_TAG);
        }
        mBlockPosterClick = false;
        mCompositeSubscription = new CompositeSubscription();
    }

    private boolean isTwoPane() {
        return findViewById(R.id.detail_fragment_container) != null;
    }

    private int getCatalogExhibitionFromSharedPrefs() {
        String key = getString(R.string.exhibition_key);
        int defValue = CatalogExhibition.getDefValue();
        return mPrefs.getInt(key, defValue);
    }

    private CatalogFragment createMovieCatalogFragment() {
        CatalogFragment catalogFragment = CatalogFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.catalog_fragment_container, catalogFragment, CATALOG_FRAGMENT_TAG )
                .commit();
        return catalogFragment;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @DebugLog
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_show_by_popularity:
                if (MyApp.getCatalogExhibition() != CatalogExhibition.BY_POPULARITY) {
                    changeCatalogExhibition(CatalogExhibition.BY_POPULARITY);
                }
                return true;
            case R.id.action_show_by_top_rated:
                if (MyApp.getCatalogExhibition() != CatalogExhibition.BY_TOP_RATING) {
                    changeCatalogExhibition(CatalogExhibition.BY_TOP_RATING);
                }
                return true;
            case R.id.action_show_favorites:
                if (MyApp.getCatalogExhibition() != CatalogExhibition.FAVORITES) {
                    changeCatalogExhibition(CatalogExhibition.FAVORITES);
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void changeCatalogExhibition(int newCatalogExhibition) {
        MyApp.setCatalogExhibition(newCatalogExhibition);
        mCatalogFragment.update();
        if (MyApp.getTwoPane()) {
            removeDetailFragment();
        }
    }

    @DebugLog
    @Override
    public void putSelectedMovieToFavoritesDb() {
        final MainActivity mainActivity = this;
        MyApp.getFavoritesDbManager().put(MyApp.getSelectedMovieData(), new Completable.CompletableSubscriber() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(mainActivity, getString(R.string.error_saving_movie_in_favorites), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }

            @Override
            public void onSubscribe(Subscription d) {
                mCompositeSubscription.add(d);
            }
        });
    }

    @Override
    public void deleteSelectedMovieFromFavoritesDb() {
        final MainActivity mainActivity = this;
        MyApp.getFavoritesDbManager().delete(MyApp.getSelectedMovieData().basic.id, new Completable.CompletableSubscriber() {
            @Override
            public void onCompleted() {
                if(MyApp.getCatalogExhibition() == CatalogExhibition.FAVORITES){
                    mCatalogFragment.update();
                    if (MyApp.getTwoPane()) {
                        removeDetailFragment();
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(mainActivity, getString(R.string.error_deleting_movie_from_favorites), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }

            @Override
            public void onSubscribe(Subscription d) {
                mCompositeSubscription.add(d);
            }
        });
    }

    @DebugLog
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mBlockPosterClick = false;

        if (requestCode == sDetailActivityResultCode) {
            // TODO: Consider that a trailer or review can be removed from TmdbApi, so it need to be removed from my SQLite db too
            /**
             * The requirement above isn't a feature with new things to learn, so I will pass it.
             */
            if (MyApp.getSelectedMovieData().isFavorite) {
                putSelectedMovieToFavoritesDb();
            } else {
                deleteSelectedMovieFromFavoritesDb();
            }
        }
    }

    private void removeDetailFragment() {
        if (mDetailFragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .remove(mDetailFragment)
                    .commit();
        }
    }

    @DebugLog
    @Override
    protected void onPause() {
        super.onPause();
        saveExhibitionInSharedPrefs();
    }

    @DebugLog
    private void saveExhibitionInSharedPrefs(){
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putInt(getString(R.string.exhibition_key), MyApp.getCatalogExhibition());
        editor.apply();
    }

    @Override
    protected void onDestroy() {
        mCompositeSubscription.unsubscribe();
        super.onDestroy();
    }

    @DebugLog
    @Override
    public void onMovieSelected() {
        if (MyApp.getTwoPane()) {
            mDetailFragment = DetailFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_container, mDetailFragment)
                    .commit();
        } else if (!mBlockPosterClick) {
            mBlockPosterClick = true;
            Intent detailIntent = new Intent(this, DetailActivity.class);
            startActivityForResult(detailIntent, sDetailActivityResultCode);
        }
    }
}