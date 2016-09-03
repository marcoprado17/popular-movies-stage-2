/**
 * Copyright (c) 2016 Marco Aurélio Prado dos Santos Vidoca.
 */

package com.example.marcoaurelio.popularmovies.detail;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.marcoaurelio.popularmovies.MyApp;
import com.example.marcoaurelio.popularmovies.R;
import com.example.marcoaurelio.popularmovies.db.entities.Trailer;
import com.example.marcoaurelio.popularmovies.utils.UiStateController;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import hugo.weaving.DebugLog;
import rx.Subscriber;
import rx.subscriptions.CompositeSubscription;

// TODO: Fix scroll bar
public class DetailTrailersFragment extends Fragment {
    @BindView(R.id.loading_ui)
    LinearLayout mCatalogLoadingUi;
    @BindView(R.id.error_ui)
    LinearLayout mCatalogErrorUi;
    @BindView(R.id.empty_ui)
    FrameLayout mCatalogEmptyUi;

    private Unbinder mUnbinder;
    private RecyclerView mRecyclerView;
    private CompositeSubscription mSubscriptions;
    private UiStateController mUiStateController;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSubscriptions = new CompositeSubscription();
    }

    @DebugLog
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_trailers, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        int firstTrailerViewContainerTopDist = (int) getResources().getDimension(R.dimen.first_card_view_container_top_dist);
        int spaceBetweenTrailerViewsContainer = (int) getResources().getDimension(R.dimen.space_between_card_views);
        int lastTrailerViewContainerBottomDist = (int) getResources().getDimension(R.dimen.last_card_view_container_bottom_dist);
        mRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration(firstTrailerViewContainerTopDist, spaceBetweenTrailerViewsContainer, lastTrailerViewContainerBottomDist));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mRecyclerView.setElevation(2.0f);
        }

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);

        mUiStateController = initUiStateController();
        update();
    }

    private UiStateController initUiStateController() {
        return new UiStateController.Builder()
                .withLoadingUi(mCatalogLoadingUi)
                .withErrorUi(mCatalogErrorUi)
                .withContentUi(mRecyclerView)
                .withEmptyUi(mCatalogEmptyUi)
                .withInitialState(UiStateController.UI_STATE_LOADING)
                .withAppearAnimation(null)
                .withDisappearAnimation(null)
                .build();
    }

    @OnClick(R.id.try_again_button)
    public void update() {
        mUiStateController.setUiStateLoading();

        if (MyApp.getSelectedMovieData().basic.trailersFetched == 0) {
            MyApp.getSelectedMovieData().allTrailers = new ArrayList<>();
            mSubscriptions.add(MyApp.getTheMovieDbApiManager().fetchAllTrailersOfMovie(MyApp.getSelectedMovieData().basic.id, new Subscriber<Trailer>() {
                @Override
                public void onCompleted() {
                    MyApp.getSelectedMovieData().basic.trailersFetched = 1;
                    onContentReady();
                }

                @Override
                public void onError(Throwable e) {
                    mUiStateController.setUiStateError();
                }

                @Override
                public void onNext(Trailer trailer) {
                    trailer.movieId = MyApp.getSelectedMovieData().basic.id;
                    MyApp.getSelectedMovieData().allTrailers.add(trailer);
                }
            }));
        } else {
            onContentReady();
        }
    }

    private void onContentReady() {
        if(MyApp.getSelectedMovieData().allTrailers.isEmpty()){
            mUiStateController.setUiStateEmpty();
            return;
        }

        RecyclerView.Adapter adapter = new TrailerAdapter(getActivity(), MyApp.getSelectedMovieData().allTrailers);
        mRecyclerView.setAdapter(adapter);
        mUiStateController.setUiStateContent();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @Override
    public void onDestroy() {
        mSubscriptions.clear();
        super.onDestroy();
    }
}
