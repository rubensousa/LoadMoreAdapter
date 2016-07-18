package com.github.rubensousa.loadmoreadapter.sample;


import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.rubensousa.loadmoreadapter.LoadMoreAdapter;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends LoadMoreAdapter {

    public static final String STATE_DATA = "state_data";

    private ArrayList<String> mData = new ArrayList<>();

    public CustomAdapter(RecyclerView recyclerView) {
        super(recyclerView);
    }

    @Override
    public void saveState(Bundle outState) {
        super.saveState(outState);
        outState.putStringArrayList(STATE_DATA, mData);
    }

    @Override
    public void restoreState(Bundle restoreState) {
        super.restoreState(restoreState);
        if (restoreState != null) {
            mData = restoreState.getStringArrayList(STATE_DATA);
        }
    }


    public void addData(List<String> data) {
        int previousSize = mData.size();
        mData.addAll(data);
        notifyItemRangeInserted(previousSize, data.size());
    }

    @Override
    public List getItems() {
        return mData;
    }

    @Override
    public ViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter, parent, false));
    }

    @Override
    public void onBindViewHolder(LoadMoreAdapter.ViewHolder holder, int position) {
        if (holder.getItemViewType() == VIEW_NORMAL) {
            ((ViewHolder) holder).setData(mData.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends LoadMoreAdapter.ViewHolder {

        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.textView);
        }

        public void setData(String data) {
            textView.setText(data);
        }
    }
}
