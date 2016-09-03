/**
 * Copyright (C) 2016 Marco Aurélio Prado dos Santos Vidoca
 */

package com.example.marcoaurelio.popularmovies.catalog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.example.marcoaurelio.popularmovies.MyApp;
import com.example.marcoaurelio.popularmovies.models.MovieData;
import com.example.marcoaurelio.popularmovies.R;
import com.example.marcoaurelio.popularmovies.utils.L;
import com.example.marcoaurelio.popularmovies.utils.UiStateController;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.Unbinder;
import hugo.weaving.DebugLog;
import rx.Subscriber;
import rx.subscriptions.CompositeSubscription;

/**
 * Show movies posters from favorites db or from TmdbApi sorted by most popular or sorted by
 * top rating, call MainActivity to launch the detailed view of the selected movie.
 * This class provide an abstraction for a catalog of movies posters.
 */
public class CatalogFragment extends Fragment {

    public static final String GRID_VIEW_INDEX_KEY = "grid_view_index_key";

    @BindView(R.id.loading_ui)
    LinearLayout mCatalogLoadingUi;
    @BindView(R.id.error_ui)
    LinearLayout mCatalogErrorUi;
    @BindView(R.id.posters_grid_view)
    GridView mGridView;
    @BindView(R.id.empty_ui)
    FrameLayout mCatalogEmptyUi;

    private ArrayList<MovieData> mAllMoviesData;
    private IOnMovieSelectedListener mOnMovieSelectedListener;
    private CompositeSubscription mSubscriptions;
    private Unbinder mUnbinder;
    UiStateController mUiStateController;
    private PostersLoader mPostersLoader;
    private Integer mGridViewIndex;

    public CatalogFragment() {
    }

    public static CatalogFragment newInstance() {
        Bundle args = new Bundle();
        CatalogFragment frag = new CatalogFragment();
        frag.setArguments(args);
        return frag;
    }

    @DebugLog
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mOnMovieSelectedListener = (IOnMovieSelectedListener) getActivity();
        mSubscriptions = new CompositeSubscription();
        mGridViewIndex = getGridViewIndex(savedInstanceState);
    }

    private Integer getGridViewIndex(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            return null;
        }
        return savedInstanceState.getInt(GRID_VIEW_INDEX_KEY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_catalog, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        return view;
    }

    @DebugLog
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUiStateController = initUiStateController();
        update();
    }

    private UiStateController initUiStateController() {
        return new UiStateController.Builder()
                .withLoadingUi(mCatalogLoadingUi)
                .withErrorUi(mCatalogErrorUi)
                .withContentUi(mGridView)
                .withEmptyUi(mCatalogEmptyUi)
                .withInitialState(UiStateController.UI_STATE_LOADING)
                .withAppearAnimation(null)
                .withDisappearAnimation(null)
                .build();
    }

    @DebugLog
    @OnItemClick(R.id.posters_grid_view)
    public void onMovieSelected(int position) {
        // TODO: When in two pane mode, check if the detailed view of the selected movie has not already been launched
        MyApp.setSelectedMovieData(mAllMoviesData.get(position));
        mOnMovieSelectedListener.onMovieSelected();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @DebugLog
    @Override
    public void onResume() {
        super.onResume();
    }

    @OnClick(R.id.try_again_button)
    @DebugLog
    public void update() {
        mUiStateController.setUiStateLoading();

        mSubscriptions.clear();
        mAllMoviesData = new ArrayList<>();

        if (MyApp.getCatalogExhibition() == CatalogExhibition.FAVORITES) {
            mSubscriptions.add(MyApp.getFavoritesDbManager().getAll(new Subscriber<MovieData>() {
                @DebugLog
                @Override
                public void onCompleted() {
                    onContentReady();
                }

                @DebugLog
                @Override
                public void onError(Throwable e) {
                    mUiStateController.setUiStateError();
                }

                @Override
                public void onNext(MovieData movieData) {
                    mAllMoviesData.add(movieData);
                }
            }));
        } else {
            mSubscriptions.add(MyApp.getTheMovieDbApiManager().fetchAllMovieData(new Subscriber<MovieData>() {
                @Override
                public void onCompleted() {
                    mPostersLoader = new PostersLoader(getActivity(), mAllMoviesData, new CompletableSubscriber() {
                        @Override
                        public void onCompleted() {
                            onContentReady();
                        }

                        @DebugLog
                        @Override
                        public void onError() {
                            mUiStateController.setUiStateError();
                        }
                    });
                    mPostersLoader.loadAll();
                }

                @DebugLog
                @Override
                public void onError(Throwable e) {
                    mUiStateController.setUiStateError();
                }

                @Override
                public void onNext(MovieData movieData) {
                    mAllMoviesData.add(movieData);
                }
            }));
        }
    }

    @DebugLog
    private void onContentReady() {
        if (mAllMoviesData.isEmpty()) {
            mUiStateController.setUiStateEmpty();
            return;
        }

        PosterAdapter posterAdapter = new PosterAdapter(getActivity(), mGridView, mAllMoviesData);
        mGridView.setAdapter(posterAdapter);
        if (mGridViewIndex != null) {
            L.d("mGridViewIndex: " + mGridViewIndex);
            mGridView.setSelection(mGridViewIndex);
        }
        mUiStateController.setUiStateContent();
    }

    @DebugLog
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        int index = mGridView.getFirstVisiblePosition();
        L.d("index: " + index);
        outState.putInt(GRID_VIEW_INDEX_KEY, index);
    }

    @DebugLog
    @Override
    public void onDestroy() {
        mSubscriptions.clear();
        if (mPostersLoader != null) {
            mPostersLoader.clearSubscription();
        }
        super.onDestroy();
    }
}
