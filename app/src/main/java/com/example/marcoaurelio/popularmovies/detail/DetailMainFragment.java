/**
 * Copyright (c) 2016 Marco Aurélio Prado dos Santos Vidoca.
 */

package com.example.marcoaurelio.popularmovies.detail;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.percent.PercentRelativeLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.marcoaurelio.popularmovies.MyApp;
import com.example.marcoaurelio.popularmovies.R;
import com.example.marcoaurelio.popularmovies.models.MovieData;
import com.example.marcoaurelio.popularmovies.utils.ProjUtils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import hugo.weaving.DebugLog;

public class DetailMainFragment extends Fragment {

    @BindView(R.id.detail_header_container)
    RelativeLayout mHeaderContainer;
    @BindView(R.id.backdrop)
    ImageView mBackdrop;
    @BindView(R.id.backdrop_filter)
    ImageView mBackdropFilter;
    @BindView(R.id.movie_title)
    TextView mMovieTitle;
    @BindView(R.id.release_date)
    TextView mReleaseDate;
    @BindView(R.id.user_rating)
    TextView mUserRating;
    @BindView(R.id.overview)
    TextView mOverview;
    @BindView(R.id.poster)
    ImageView mPoster;

    PercentRelativeLayout mMovieTitleContainer;

    private Unbinder mUnbinder;

    @SuppressWarnings("FieldCanBeLocal")
    private Target mBackdropTarget; // Reference to the target, so it will not be garbage collected

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @DebugLog
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_main, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMovieTitleContainer = (PercentRelativeLayout) view.findViewById(R.id.movie_title_container);

        MovieData selectedMovieData = MyApp.getSelectedMovieData();

        mMovieTitle.setText(selectedMovieData.basic.originalTitle);
        mReleaseDate.setText(String.format(getString(R.string.release_date), selectedMovieData.basic.releaseDate));
        mUserRating.setText(String.format(getString(R.string.user_rating), selectedMovieData.basic.userRating));
        mOverview.setText(String.format(getString(R.string.overview), selectedMovieData.basic.overview));
        mPoster.setImageBitmap(selectedMovieData.poster);

        initMovieTitleBackdrop();
    }

    // TODO: Turn movie title mBackdrop pixel perfect
    // TODO: Save the mBackdrop image on favorites db
    private void initMovieTitleBackdrop() {
        mBackdropTarget = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {
                if (mBackdrop != null) {
                    mBackdrop.setImageBitmap(bitmap);
                }
                if (mBackdropFilter != null && mHeaderContainer != null) {
                    mBackdropFilter.bringToFront();
                    mHeaderContainer.requestLayout();
                    mHeaderContainer.invalidate();
                }
                if (mMovieTitleContainer != null && mHeaderContainer != null) {
                    mMovieTitleContainer.bringToFront();
                    mHeaderContainer.requestLayout();
                    mHeaderContainer.invalidate();
                }
            }

            @Override
            public void onBitmapFailed(Drawable drawable) {
                // TODO: Handle error
            }

            @Override
            public void onPrepareLoad(Drawable drawable) {
                mBackdrop.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.empty_movie_backdrop));
            }
        };
        Picasso
                .with(getActivity())
                .load(ProjUtils.getBackdropUrl(MyApp.getSelectedMovieData().basic.backdropPath))
                .into(mBackdropTarget);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
