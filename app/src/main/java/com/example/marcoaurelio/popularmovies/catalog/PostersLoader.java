/**
 * Copyright (c) 2016 Marco Aurélio Prado dos Santos Vidoca.
 */

package com.example.marcoaurelio.popularmovies.catalog;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.example.marcoaurelio.popularmovies.models.MovieData;
import com.example.marcoaurelio.popularmovies.utils.ProjUtils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.HashSet;

public class PostersLoader {

    private Context mContext;
    private ArrayList<MovieData> mAllMoviesData;
    /**
     * mAllTargets is used to maintain a reference to the Target object, otherwise the instances of
     * Target are garbage collected before the bitmap has been loaded.
     */
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private HashSet<Target> mAllTargets;
    private CompletableSubscriber mCompletableSubscriber;
    private int nPostersLoaded;

    public PostersLoader(Context context, ArrayList<MovieData> allMovieData, CompletableSubscriber completableSubscriber){
        mContext = context;
        mAllMoviesData = allMovieData;
        mCompletableSubscriber = completableSubscriber;
        mAllTargets = new HashSet<>();
        nPostersLoaded = 0;
    }

    public void loadAll(){
        for(MovieData movieData : mAllMoviesData){
            if (movieData.poster == null) {
                Target target = getTarget(movieData);
                mAllTargets.add(target);
                String posterUrl = ProjUtils.getPosterUrl(movieData.basic.posterPath);
                Picasso.with(mContext)
                        .load(posterUrl)
                        .into(target);
            } else {
                nPostersLoaded++;
                checkAllLoadsCompleted();
            }
        }
    }

    public Target getTarget(final MovieData movieData) {
        return new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {
                movieData.poster = bitmap;
                nPostersLoaded++;
                checkAllLoadsCompleted();
            }

            @Override
            public void onBitmapFailed(Drawable drawable) {
                if(mCompletableSubscriber != null){
                    mCompletableSubscriber.onError();
                }
            }

            @Override
            public void onPrepareLoad(Drawable drawable) {

            }
        };
    }

    private void checkAllLoadsCompleted(){
        if(nPostersLoaded == mAllMoviesData.size()){
            if(mCompletableSubscriber != null){
                mCompletableSubscriber.onCompleted();
            }
        }
    }

    public void clearSubscription(){
        mCompletableSubscriber = null;
    }
}
