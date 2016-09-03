/**
 * Copyright (C) 2016 Marco Aurélio Prado dos Santos Vidoca.
 */

package com.example.marcoaurelio.popularmovies.detail;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marcoaurelio.popularmovies.MyApp;
import com.example.marcoaurelio.popularmovies.R;
import com.example.marcoaurelio.popularmovies.catalog.MainActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import hugo.weaving.DebugLog;
import rx.subscriptions.CompositeSubscription;

/**
 * Shows detailed information of the selected movie.
 */
// TODO: Change tabs with horizontal slide
public class DetailFragment extends Fragment {

    private CompositeSubscription mSubscriptions;
    private Unbinder mUnbinder;

    @DebugLog
    public DetailFragment() {
    }

    @DebugLog
    public static DetailFragment newInstance() {
        DetailFragment df = new DetailFragment();
        Bundle b = new Bundle();
        df.setArguments(b);
        return df;
    }

    @DebugLog
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.detail, menu);

        for (int i = 0; i < menu.size(); i++) {
            MenuItem menuItem = menu.getItem(i);
            if (menuItem.getItemId() == R.id.action_favorite_toggle) {
                updateFavoriteMenuItem(menuItem);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_favorite_toggle:
                MyApp.getSelectedMovieData().isFavorite = !MyApp.getSelectedMovieData().isFavorite;
                updateFavoriteMenuItem(item);
                if (MyApp.getSelectedMovieData().isFavorite) {
                    Toast.makeText(getActivity(), getString(R.string.movie_added_to_favorites), Toast.LENGTH_SHORT).show();
                    if (MyApp.getTwoPane()) {
                        MainActivity mainActivity = (MainActivity) getActivity();
                        mainActivity.putSelectedMovieToFavoritesDb();
                    }
                } else {
                    Toast.makeText(getActivity(), getString(R.string.movie_removed_from_favorites), Toast.LENGTH_SHORT).show();
                    if (MyApp.getTwoPane()) {
                        MainActivity mainActivity = (MainActivity) getActivity();
                        mainActivity.deleteSelectedMovieFromFavoritesDb();
                    }
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateFavoriteMenuItem(MenuItem menuItem) {
        if (MyApp.getSelectedMovieData().isFavorite) {
            menuItem.setIcon(ContextCompat.getDrawable(getActivity(), R.drawable.ic_favorite_filled));
            menuItem.setTitle(R.string.remove_movie_to_favorites);
        } else {
            menuItem.setIcon(ContextCompat.getDrawable(getActivity(), R.drawable.ic_favorite_empty));
            menuItem.setTitle(R.string.add_movie_to_favorites);
        }
    }

    @DebugLog
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        // TODO: Create my own TabHost view to turn the code more clean
        FragmentTabHost tabHost = ButterKnife.findById(view, R.id.tab_host);
        tabHost.setup(getActivity(), getChildFragmentManager(), R.id.tab_content);

        tabHost.addTab(tabHost.newTabSpec(getString(R.string.main)).setIndicator(createTabView(getActivity(), getString(R.string.main))),
                DetailMainFragment.class, null);
        tabHost.addTab(tabHost.newTabSpec(getString(R.string.trailers)).setIndicator(createTabView(getActivity(), getString(R.string.trailers))),
                DetailTrailersFragment.class, null);
        tabHost.addTab(tabHost.newTabSpec(getString(R.string.reviews)).setIndicator(createTabView(getActivity(), getString(R.string.reviews))),
                DetailReviewsFragment.class, null);

        int tabWidgetHeight = (int) getResources().getDimension(R.dimen.tab_widget_height);
        tabHost.getTabWidget().getChildAt(0).getLayoutParams().height = tabWidgetHeight;
        FrameLayout tabWidgetOffsetFrameLayout = ButterKnife.findById(view, R.id.tab_widget_offset);
        tabWidgetOffsetFrameLayout.getLayoutParams().height = tabWidgetHeight;
        return view;
    }

    @DebugLog
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private static View createTabView(final Context context, final String text) {
        View view = LayoutInflater.from(context).inflate(R.layout.tab_detail, null);
        TextView tv = (TextView) view.findViewById(R.id.tabsText);
        tv.setText(text);
        return view;
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
