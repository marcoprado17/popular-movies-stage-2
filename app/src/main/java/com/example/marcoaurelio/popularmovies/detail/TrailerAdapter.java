/**
 * Copyright (c) 2016 Marco Aurélio Prado dos Santos Vidoca.
 */

package com.example.marcoaurelio.popularmovies.detail;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.marcoaurelio.popularmovies.Config;
import com.example.marcoaurelio.popularmovies.R;
import com.example.marcoaurelio.popularmovies.db.entities.Trailer;

import java.util.ArrayList;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder> {
    private ArrayList<Trailer> mAllTrailers;
    private Context mContext;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView trailerName;
        public ImageButton playTrailerButton;

        public ViewHolder(View v) {
            super(v);
            trailerName = (TextView) v.findViewById(R.id.trailer_name);
            playTrailerButton = (ImageButton) v.findViewById(R.id.play_button);
        }
    }

    public TrailerAdapter(Context context, ArrayList<Trailer> allTrailers) {
        mAllTrailers = allTrailers;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_trailer, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.trailerName.setText(mAllTrailers.get(position).name);
        final String trailerId = mAllTrailers.get(position).key;
        holder.playTrailerButton.setOnClickListener(view -> {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Config.YOUTUBE_INTENT_SCHEME + ":" + trailerId));
                mContext.startActivity(intent);
            } catch (ActivityNotFoundException ex) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(Config.BASE_YOUTUBE_URL + trailerId));
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mAllTrailers.size();
    }
}
