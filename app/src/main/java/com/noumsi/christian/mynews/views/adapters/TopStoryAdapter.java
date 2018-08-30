package com.noumsi.christian.mynews.views.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.RequestManager;
import com.noumsi.christian.mynews.R;
import com.noumsi.christian.mynews.views.TopStoryResultViewHolder;
import com.noumsi.christian.mynews.webservices.topstorie.TopStories;
import com.noumsi.christian.mynews.webservices.topstorie.TopStoriesResult;

/**
 * Created by christian-noumsi on 21/08/2018.
 */
public class TopStoryAdapter extends RecyclerView.Adapter<TopStoryResultViewHolder> {

    private TopStories mTopStories;
    private RequestManager mGlide;
    private static final String TAG = "TopStoryAdapter";

    public TopStoryAdapter(TopStories topStories, RequestManager glide) {
        mTopStories = topStories;
        mGlide = glide;
    }

    @NonNull
    @Override
    public TopStoryResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fragment_article_item, parent, false);
        return new TopStoryResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopStoryResultViewHolder holder, int position) {
        if (mTopStories != null) holder.updateWithTopStoriesResult(mTopStories.getResults().get(position), mGlide);
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, String.valueOf(mTopStories != null ? mTopStories.getResults().size() : 0));
        return mTopStories != null ? mTopStories.getResults().size() : 0;
    }

    public void setTopStories(TopStories topStories) {
        mTopStories = topStories;
    }

    public TopStoriesResult getTopStoriesResult(int position) {
        return mTopStories.getResults().get(position);
    }
}
