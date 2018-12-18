package com.noumsi.christian.mynews.controller.fragments;


import android.content.Intent;
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

import com.bumptech.glide.RequestManager;
import com.noumsi.christian.mynews.R;
import com.noumsi.christian.mynews.controller.activities.ArticleContainerActivity;
import com.noumsi.christian.mynews.utils.ItemClickSupport;
import com.noumsi.christian.mynews.views.adapters.ArticleAdapter;
import com.noumsi.christian.mynews.webservices.searcharticle.Search;
import com.noumsi.christian.mynews.webservices.searcharticle.SearchArticleCall;
import com.noumsi.christian.mynews.webservices.searcharticle.SearchArticleDoc;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.noumsi.christian.mynews.utils.Constants.EXTRA_TITLE_ARTICLE;
import static com.noumsi.christian.mynews.utils.Constants.EXTRA_URL_ARTICLE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BusinessFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BusinessFragment extends Fragment implements SearchArticleCall.Callbacks{

    @BindView(R.id.fragment_business_swipe_container) SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.fragment_business_recycler_view) RecyclerView mRecyclerView;
    private ArticleAdapter mArticleAdapter;
    private Search mSearch = null;
    private RequestManager mGlide;
    private static final String TAG = "BusinessFragment";

    public BusinessFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment.
     *
     * @return A new instance of fragment BusinessFragment.
     */
    public static BusinessFragment newInstance() {
        return new BusinessFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_business, container, false);
        ButterKnife.bind(this, view);

        // We configure recycler view
        this.configureRecyclerView();
        // we configure swipe layout
        this.configureSwipeRefreshLayout();
        // We execute http request
        executeHTTPRequestWithRetrofit();
        // We configure click on item of recycler view
        configureOnClickRecyclerView();
        Log.d(TAG, "onCreateView");
        return view;
    }

    private void configureRecyclerView() {
        // we create new adapter
        mArticleAdapter = new ArticleAdapter(mSearch);
        // we attach adapter to recycler view
        mRecyclerView.setAdapter(mArticleAdapter);
        // we set layout manager
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void configureOnClickRecyclerView() {
        ItemClickSupport.addTo(mRecyclerView, R.layout.fragment_article_item)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        SearchArticleDoc searchArticleDoc = mArticleAdapter.getArticle(position);
                        // We start article container activity
                        Intent articleContainer = new Intent(getContext(), ArticleContainerActivity.class);
                        articleContainer.putExtra(EXTRA_URL_ARTICLE, searchArticleDoc.getWeb_url());
                        articleContainer.putExtra(EXTRA_TITLE_ARTICLE, searchArticleDoc.getSnippet());
                        startActivity(articleContainer);
                    }
                });
    }

    private void executeHTTPRequestWithRetrofit() {
        this.updateUIWhenStartingHTTPRequest();
        SearchArticleCall.fetchSearchArticle(this, "", "news_desk:(\"Business\")", null, null, getString(R.string.api_key_nyt));
    }

    private void updateUIWhenStartingHTTPRequest() { }

    private void configureSwipeRefreshLayout() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                executeHTTPRequestWithRetrofit();
            }
        });
    }

    @Override
    public void onResponse(@Nullable Search search) {
        Log.d(TAG, "Success");
        if (search != null) this.updateUIWithSearchArticle(search);
    }

    private void updateUIWithSearchArticle(Search search) {
        mSwipeRefreshLayout.setRefreshing(false);
        mSearch = search;
        mArticleAdapter.setSearch(mSearch);
        mArticleAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFailure() {
        Log.e(TAG, "Error");
        this.updateUIWhenStoppingHTTPRequest("An error happened");
    }

    private void updateUIWhenStoppingHTTPRequest(String message) { }
}
