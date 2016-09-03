/**
 * Copyright (c) 2016 Marco Aurélio Prado dos Santos Vidoca.
 */

package com.example.marcoaurelio.popularmovies.detail;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {

    private final int mTopSpaceHeight;
    private final int mVerticalSpaceHeight;
    private final int mBottomSpaceHeight;

    public VerticalSpaceItemDecoration(int mTopSpaceHeight, int mVerticalSpaceHeight, int mBottomSpaceHeight) {
        this.mTopSpaceHeight = mTopSpaceHeight;
        this.mVerticalSpaceHeight = mVerticalSpaceHeight;
        this.mBottomSpaceHeight = mBottomSpaceHeight;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        if(parent.getChildAdapterPosition(view) == 0){
            outRect.top = mTopSpaceHeight;
            outRect.bottom = mVerticalSpaceHeight;
        }
        else if(parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() - 1){
            outRect.bottom = mVerticalSpaceHeight;
        }
        else {
            outRect.bottom = mBottomSpaceHeight;
        }
    }
}