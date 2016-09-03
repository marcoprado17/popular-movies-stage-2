/**
 * Copyright (C) 2016 Marco Aurélio Prado dos Santos Vidoca.
 */

package com.example.marcoaurelio.popularmovies.catalog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.marcoaurelio.popularmovies.models.MovieData;
import com.example.marcoaurelio.popularmovies.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import hugo.weaving.DebugLog;

/**
 * Construct the elements of the GridView of posters. The posters already are in the MovieData object
 * when fetched from favorites db, but when fetched from TMDBApi the posters aren't in the MovieData
 * object and must be fetched from web using picasso.
 */
public class PosterAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<MovieData> mAllMoviesData;
    private GridView mGridView;

    public PosterAdapter(Context context, GridView gridView, ArrayList<MovieData> allMoviesData) {
        mContext = context;
        mAllMoviesData = allMoviesData;
        mGridView = gridView;
    }

    @Override
    public int getCount() {
        return mAllMoviesData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @DebugLog
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;

        if (view == null) {
            LayoutInflater inflater =
                    (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_movie_poster, mGridView, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.imageView.setImageBitmap(mAllMoviesData.get(position).poster);

        return view;
    }

    static class ViewHolder {
        @BindView(R.id.image_view)
        ImageView imageView;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
