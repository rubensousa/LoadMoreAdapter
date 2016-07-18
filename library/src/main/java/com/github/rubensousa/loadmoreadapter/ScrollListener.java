/*
 * Copyright 2016 Rúben Sousa
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
