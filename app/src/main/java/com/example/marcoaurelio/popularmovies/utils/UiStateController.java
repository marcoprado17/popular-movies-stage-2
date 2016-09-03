/**
 * Code get from https://github.com/pushtorefresh/storio/tree/master/storio-sample-app
 * under Apache 2.0 license.
 * By @artem_zin (https://twitter.com/artem_zin) and @nikitin-da (https://github.com/nikitin-da)
 */

package com.example.marcoaurelio.popularmovies.utils;

import android.os.Bundle;
import android.support.annotation.AnimRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.security.InvalidParameterException;

@SuppressWarnings("unused")
public class UiStateController {

    private static final String STATE_CURRENT_UI_STATE = "STATE_CURRENT_UI_STATE";
    private static final String STATE_APPEAR_ANIMATION_RES_ID = "STATE_APPEAR_ANIMATION_RES_ID";
    private static final String STATE_DISAPPEAR_ANIMATION_RES_ID = "STATE_DISAPPEAR_ANIMATION_RES_ID";

    public static final int UI_STATE_LOADING = 0;
    public static final int UI_STATE_ERROR = 1;
    public static final int UI_STATE_EMPTY = 2;
    public static final int UI_STATE_CONTENT = 3;

    @Nullable
    private final View loadingUiView, errorUiView, emptyUiView, contentUiView;

    @Nullable
    @AnimRes
    private Integer appearAnimationResId, disappearAnimationResId;

    private int currentUiState;

    private UiStateController(@Nullable View loadingUiView, @Nullable View errorUiView,
                              @Nullable View emptyUiView, @Nullable View contentUiView,
                              @Nullable @AnimRes Integer appearAnimationResId,
                              @Nullable @AnimRes Integer disappearAnimationResId,
                              int initialState) {
        this.loadingUiView = loadingUiView;
        this.errorUiView = errorUiView;
        this.emptyUiView = emptyUiView;
        this.contentUiView = contentUiView;
        this.appearAnimationResId = appearAnimationResId;
        this.disappearAnimationResId = disappearAnimationResId;
        this.currentUiState = initialState;
    }

    private void makeViewAppear(@NonNull final View view) {
        if (view.getVisibility() == View.VISIBLE) {
            return;
        }

        if (appearAnimationResId != null) {
            final Animation animation = AnimationUtils.loadAnimation(view.getContext(), appearAnimationResId);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    view.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            view.startAnimation(animation);
        } else {
            view.setVisibility(View.VISIBLE);
        }
    }

    private void makeViewDisappear(@NonNull final View view) {
        if (view.getVisibility() == View.GONE) {
            return;
        }

        if (disappearAnimationResId != null) {
            final Animation animation = AnimationUtils.loadAnimation(view.getContext(), disappearAnimationResId);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    view.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            view.startAnimation(animation);
        } else {
            view.setVisibility(View.GONE);
        }
    }

    private void makeViewsDisappear(@NonNull View... views) {
        for (View view : views) {
            if (view != null) {
                makeViewDisappear(view);
            }
        }
    }

    public void setUiStateLoading() {
        if (loadingUiView == null) {
            throw new IllegalStateException("Please set loadingUiView");
        }

        makeViewAppear(loadingUiView);
        makeViewsDisappear(errorUiView, emptyUiView, contentUiView);

        currentUiState = UI_STATE_LOADING;
    }

    public void setUiStateError() {
        if (errorUiView == null) {
            throw new IllegalStateException("Please set errorUiView");
        }

        makeViewAppear(errorUiView);
        makeViewsDisappear(loadingUiView, emptyUiView, contentUiView);


        currentUiState = UI_STATE_ERROR;
    }

    public void setUiStateEmpty() {
        if (emptyUiView == null) {
            throw new IllegalStateException("Please set emptyUiView");
        }

        makeViewAppear(emptyUiView);
        makeViewsDisappear(loadingUiView, errorUiView, contentUiView);

        currentUiState = UI_STATE_EMPTY;
    }

    public void setUiStateContent() {
        if (contentUiView == null) {
            throw new IllegalStateException("Please set contentUiView");
        }

        makeViewAppear(contentUiView);
        makeViewsDisappear(loadingUiView, errorUiView, emptyUiView);

        currentUiState = UI_STATE_CONTENT;
    }

    public void saveState(@NonNull Bundle outState) {
        outState.putInt(STATE_CURRENT_UI_STATE, currentUiState);

        if (appearAnimationResId != null) {
            outState.putInt(STATE_APPEAR_ANIMATION_RES_ID, appearAnimationResId);
        }

        if (disappearAnimationResId != null) {
            outState.putInt(STATE_DISAPPEAR_ANIMATION_RES_ID, disappearAnimationResId);
        }
    }

    public void restoreState(@NonNull Bundle savedInstanceState) {
        currentUiState = savedInstanceState.getInt(STATE_CURRENT_UI_STATE, UI_STATE_LOADING);

        // preventing animations on restore state
        appearAnimationResId = null;
        disappearAnimationResId = null;

        // setting state without animations
        if (currentUiState == UI_STATE_LOADING) {
            setUiStateLoading();
        } else if (currentUiState == UI_STATE_ERROR) {
            setUiStateError();
        } else if (currentUiState == UI_STATE_CONTENT) {
            setUiStateContent();
        }

        // check required to allow null value
        if (savedInstanceState.containsKey(STATE_APPEAR_ANIMATION_RES_ID)) {
            appearAnimationResId = savedInstanceState.getInt(STATE_APPEAR_ANIMATION_RES_ID);
        }

        // check required to allow null value
        if (savedInstanceState.containsKey(STATE_DISAPPEAR_ANIMATION_RES_ID)) {
            disappearAnimationResId = savedInstanceState.getInt(STATE_DISAPPEAR_ANIMATION_RES_ID);
        }
    }

    public static class Builder {

        @Nullable
        private View loadingUiView, errorUiView, emptyUiView, contentUiView;
        @Nullable
        private Integer appearAnimationResId = android.R.anim.fade_in;
        @Nullable
        private Integer disappearAnimationResId = android.R.anim.fade_out;

        private int initialState = UI_STATE_LOADING;

        @NonNull
        public Builder withLoadingUi(@NonNull View loadingUiView) {
            this.loadingUiView = loadingUiView;
            return this;
        }

        @NonNull
        public Builder withErrorUi(@NonNull View errorUiView) {
            this.errorUiView = errorUiView;
            return this;
        }

        @NonNull
        public Builder withEmptyUi(@NonNull View emptyUi) {
            this.emptyUiView = emptyUi;
            return this;
        }

        @NonNull
        public Builder withContentUi(@NonNull View contentUiView) {
            this.contentUiView = contentUiView;
            return this;
        }

        @NonNull
        public Builder withAppearAnimation(@Nullable @AnimRes Integer appearAnimation) {
            appearAnimationResId = appearAnimation;
            return this;
        }

        @NonNull
        public Builder withDisappearAnimation(@Nullable @AnimRes Integer disappearAnimation) {
            disappearAnimationResId = disappearAnimation;
            return this;
        }

        @NonNull
        public Builder withInitialState(int initialState) {
            if( initialState != UI_STATE_LOADING &&
                    initialState != UI_STATE_ERROR &&
                    initialState != UI_STATE_CONTENT &&
                    initialState != UI_STATE_EMPTY ) {
                throw new InvalidParameterException();
            }
            this.initialState = initialState;
            return this;
        }

        @NonNull
        public UiStateController build() {
            return new UiStateController(
                    loadingUiView,
                    errorUiView,
                    emptyUiView,
                    contentUiView,
                    appearAnimationResId,
                    disappearAnimationResId,
                    initialState
            );
        }
    }
}