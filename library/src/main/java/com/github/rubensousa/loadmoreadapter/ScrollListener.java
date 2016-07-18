package com.github.rubensousa.loadmoreadapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;


class ScrollListener extends RecyclerView.OnScrollListener {

    private final OnEventListener mListener;
    private final int mThreshold;
    private final boolean mInversed;
    private final RecyclerView.LayoutManager mLayoutManager;

    public ScrollListener(boolean inversed, int threshold, OnEventListener eventListener,
                          RecyclerView.LayoutManager layoutManager) {
        mInversed = inversed;
        mThreshold = threshold;
        mListener = eventListener;
        mLayoutManager = layoutManager;
    }

    @Override
    public void onScrolled(final RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int lastVisiblePosition = 0;
        int totalItems = mLayoutManager.getItemCount();

        if (totalItems == 0 || (dx == 0 && dy == 0)) {
            return;
        }

        if (mLayoutManager instanceof StaggeredGridLayoutManager) {

            int[] itemPositions = mInversed ?
                    ((StaggeredGridLayoutManager) mLayoutManager).findFirstVisibleItemPositions(null)
                    : ((StaggeredGridLayoutManager) mLayoutManager).findLastCompletelyVisibleItemPositions(null);

            lastVisiblePosition = 0;

            for (int i = 0; i < itemPositions.length; i++) {
                if (i == 0) {
                    lastVisiblePosition = itemPositions[i];
                } else if (itemPositions[i] > lastVisiblePosition) {
                    lastVisiblePosition = itemPositions[i];
                }
            }

        } else if (mLayoutManager instanceof LinearLayoutManager) {
            lastVisiblePosition = mInversed
                    ? ((LinearLayoutManager) mLayoutManager).findFirstVisibleItemPosition()
                    : ((LinearLayoutManager) mLayoutManager).findLastCompletelyVisibleItemPosition();
        }

        if (mInversed && lastVisiblePosition == mThreshold) {
            mListener.onScrollForMore();
        } else if (totalItems <= (lastVisiblePosition + mThreshold)) {
            mListener.onScrollForMore();
        }
    }

    public interface OnEventListener {
        void onScrollForMore();
    }
}
