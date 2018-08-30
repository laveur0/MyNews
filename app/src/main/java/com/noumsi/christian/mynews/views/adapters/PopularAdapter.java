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
import com.noumsi.christian.mynews.views.PopularViewHolder;
import com.noumsi.christian.mynews.webservices.mostpopular.MostPopular;
import com.noumsi.christian.mynews.webservices.mostpopular.MostPopularResult;

/**
 * Created by christian-noumsi on 30/08/2018.
 */
public class PopularAdapter extends RecyclerView.Adapter<PopularViewHolder> {

    private MostPopular mMostPopular;
    private RequestManager mGlide;
    private static final String TAG = "PopularAdapter";

    public PopularAdapter(MostPopular mostPopular, RequestManager glide) {
        mMostPopular = mostPopular;
        mGlide = glide;
    }

    @NonNull
    @Override
    public PopularViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fragment_article_item, parent, false);
        return new PopularViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PopularViewHolder holder, int position) {
        if (mMostPopular != null) holder.updateWithPopular(mMostPopular.getResults().get(position), mGlide);
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, String.valueOf(mMostPopular != null ? mMostPopular.getResults().size() : 0));
        return mMostPopular != null ? mMostPopular.getResults().size() : 0;
    }

    public void setMostPopular(MostPopular mostPopular) {
        mMostPopular = mostPopular;
    }

    public MostPopularResult getPopular(int position) {
        return mMostPopular.getResults().get(position);
    }
}
