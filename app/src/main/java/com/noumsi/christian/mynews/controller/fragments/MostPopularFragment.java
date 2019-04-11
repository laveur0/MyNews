package com.noumsi.christian.mynews.controller.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.noumsi.christian.mynews.R;
import com.noumsi.christian.mynews.controller.activities.ArticleContainerActivity;
import com.noumsi.christian.mynews.utils.ItemClickSupport;
import com.noumsi.christian.mynews.views.adapters.PopularAdapter;
import com.noumsi.christian.mynews.webservices.mostpopular.MostPopular;
import com.noumsi.christian.mynews.webservices.mostpopular.MostPopularCall;
import com.noumsi.christian.mynews.webservices.mostpopular.MostPopularResult;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.noumsi.christian.mynews.utils.Constants.EXTRA_TITLE_ARTICLE;
import static com.noumsi.christian.mynews.utils.Constants.EXTRA_URL_ARTICLE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MostPopularFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MostPopularFragment extends Fragment implements MostPopularCall.Callbacks{

    private PopularAdapter mPopularAdapter;
    private MostPopular mMostPopular = null;
    private static final String TAG = "MostPopularFragment";
    @BindView(R.id.fragment_most_popular_swipe_container)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.fragment_most_popular_recycler_view)
    RecyclerView mRecyclerView;

    public MostPopularFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment.
     *
     * @return A new instance of fragment MostPopularFragment.
     */
    public static MostPopularFragment newInstance() {
        return new MostPopularFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_most_popular, container, false);
        ButterKnife.bind(this, view);

        // We configure recycler view
        this.configureRecyclerView();
        // we configure swipe layout
        this.configureSwipeRefreshLayout();
        // We execute http request
        executeHTTPRequestWithRetrofit();
        // We configure click on item of recycler view
        configureOnClickRecyclerView();
        return view;
    }

    private void configureRecyclerView() {
        // we create new adapter
        mPopularAdapter = new PopularAdapter(mMostPopular);
        // we attach adapter to recycler view
        mRecyclerView.setAdapter(mPopularAdapter);
        // we set layout manager
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    // configure item click on Recycler view
    private void configureOnClickRecyclerView() {
        ItemClickSupport.addTo(mRecyclerView, R.layout.fragment_article_item)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        MostPopularResult mostPopularResult = mPopularAdapter.getPopular(position);
                        // We start article container activity
                        Intent articleContainer = new Intent(getContext(), ArticleContainerActivity.class);
                        articleContainer.putExtra(EXTRA_URL_ARTICLE, mostPopularResult.getUrl());
                        articleContainer.putExtra(EXTRA_TITLE_ARTICLE, mostPopularResult.getTitle());
                        startActivity(articleContainer);
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
        MostPopularCall.fetchMostPopularArticle(this, "all-sections", getString(R.string.api_key_nyt));
    }

    private void updateUIWhenStartingHTTPRequest() {

    }

    @Override
    public void onResponse(@Nullable MostPopular mostPopular) {
        Log.d(TAG, "Success");
        if (mostPopular != null) {
            this.updateUIWithMostPopular(mostPopular);
        }
    }

    private void updateUIWithMostPopular(MostPopular mostPopular) {
        mSwipeRefreshLayout.setRefreshing(false);
        mMostPopular = mostPopular;
        mPopularAdapter.setMostPopular(mMostPopular);
        mPopularAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFailure() {
        Log.e(TAG, "Error");
        this.updateUIWhenStoppingHTTPRequest("An error happened");
    }

    private void updateUIWhenStoppingHTTPRequest(String response) {

    }
}
