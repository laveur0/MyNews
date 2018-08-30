package com.noumsi.christian.mynews.controller.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.noumsi.christian.mynews.R;
import com.noumsi.christian.mynews.utils.ItemClickSupport;
import com.noumsi.christian.mynews.views.adapters.TopStoryAdapter;
import com.noumsi.christian.mynews.webservices.topstorie.TopStories;
import com.noumsi.christian.mynews.webservices.topstorie.TopStoriesCall;
import com.noumsi.christian.mynews.webservices.topstorie.TopStoriesResult;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class TopStoriesFragment extends Fragment implements TopStoriesCall.Callbacks {

    private TopStoryAdapter mStoriesAdapter;
    private TopStories mTopStories = null;
    private static final String TAG = "TopStoriesFragment";

    @BindView(R.id.fragment_top_stories_rv) RecyclerView rv;
    @BindView(R.id.fragment_top_stories_swipe_container) SwipeRefreshLayout mSwipeRefreshLayout;

    public TopStoriesFragment() {
        // Required empty public constructor
    }

    public static TopStoriesFragment newInstance () {
        return new TopStoriesFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_top_stories, container, false);
        ButterKnife.bind(this, view);
        //We configure recycler view
        configureRecyclerView();
        // we configure the SwipeRefreshLayout
        configureSwipeRefreshLayout();
        // We execute http request
        executeHTTPRequestWithRetrofit();
        // We configure click on item of recycler view
        configureOnClickRecyclerView();
        return view;
    }

    private void configureRecyclerView() {
        mStoriesAdapter = new TopStoryAdapter(mTopStories, Glide.with(this));
        // we attach the adapter to recyclerView
        rv.setAdapter(mStoriesAdapter);
        // set layout manager
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    // configure item click on Recycler view
    private void configureOnClickRecyclerView() {
        ItemClickSupport.addTo(rv, R.layout.fragment_article_item)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        TopStoriesResult topStoriesResult = mStoriesAdapter.getTopStoriesResult(position);
                        Toast.makeText(getContext(), topStoriesResult.getTitle(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Configure the SwipeRefreshLayout
    private void configureSwipeRefreshLayout() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                executeHTTPRequestWithRetrofit();
            }
        });
    }

    private void executeHTTPRequestWithRetrofit() {
        this.updateUIWhenStartingHTTPRequest();
        TopStoriesCall.fetchTopStoriesArticle(this, getString(R.string.api_key_nyt));
    }

    private void updateUIWhenStartingHTTPRequest() {

    }

    @Override
    public void onResponse(@Nullable TopStories topStories) {
        Log.d(TAG, "Success");
        if (topStories != null) this.updateUIWithTopStories(topStories);
    }

    private void updateUIWithTopStories(TopStories topStories) {
        mSwipeRefreshLayout.setRefreshing(false);
        mTopStories = topStories;
        mStoriesAdapter.setTopStories(mTopStories);
        mStoriesAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFailure() {
        Log.e(TAG, "Error");
        this.updateUIWhenStoppingHTTPRequest("An error happened");
    }

    private void updateUIWhenStoppingHTTPRequest(String response) {

    }
}
