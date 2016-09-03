/**
 * Copyright (c) 2016 Marco Aurélio Prado dos Santos Vidoca.
 */

package com.example.marcoaurelio.popularmovies.detail;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.marcoaurelio.popularmovies.R;
import com.example.marcoaurelio.popularmovies.db.entities.Review;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    private ArrayList<Review> mAllReviews;
    private Context mContext;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView author;
        public TextView content;

        public ViewHolder(View v) {
            super(v);
            author = (TextView) v.findViewById(R.id.author);
            content = (TextView) v.findViewById(R.id.content);
        }
    }

    public ReviewAdapter(Context context, ArrayList<Review> allReviews) {
        mContext = context;
        mAllReviews = allReviews;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_review, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.author.setText(String.format(mContext.getString(R.string.review_authorship), mAllReviews.get(position).author));
        holder.content.setText(mAllReviews.get(position).content);
    }

    @Override
    public int getItemCount() {
        return mAllReviews.size();
    }
}
