package com.noumsi.christian.mynews.controller.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.noumsi.christian.mynews.R;
import com.noumsi.christian.mynews.utils.ItemClickSupport;
import com.noumsi.christian.mynews.views.adapters.ArticleAdapter;
import com.noumsi.christian.mynews.webservices.searcharticle.Search;
import com.noumsi.christian.mynews.webservices.searcharticle.SearchArticleCall;
import com.noumsi.christian.mynews.webservices.searcharticle.SearchArticleDoc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.noumsi.christian.mynews.utils.Constants.EXTRA_BEGIN_DATE;
import static com.noumsi.christian.mynews.utils.Constants.EXTRA_END_DATE;
import static com.noumsi.christian.mynews.utils.Constants.EXTRA_FQ;
import static com.noumsi.christian.mynews.utils.Constants.EXTRA_QUERY_TERM;
import static com.noumsi.christian.mynews.utils.Constants.EXTRA_TITLE_ARTICLE;
import static com.noumsi.christian.mynews.utils.Constants.EXTRA_URL_ARTICLE;

public class SearchResultActivity extends AppCompatActivity implements SearchArticleCall.Callbacks{

    @BindView(R.id.activity_search_result_refresh_layout) SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.activity_search_result_recycler_view) RecyclerView mRecyclerView;

    String mQueryTerm, mBeginDate, mEndDate, mFQ;
    private ArticleAdapter mArticleAdapter;
    private Search mSearch = null;
    private SimpleDateFormat mSimpleDateFormat;
    private static final String TAG = "SearchResultActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        ButterKnife.bind(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mQueryTerm = extras.getString(EXTRA_QUERY_TERM);
            mBeginDate = extras.getString(EXTRA_BEGIN_DATE);
            mEndDate = extras.getString(EXTRA_END_DATE);
            mFQ = extras.getString(EXTRA_FQ);
        }

        // We set date format
        mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        // We configure recycler view
        this.configureRecyclerView();
        // we configure swipe layout
        this.configureSwipeRefreshLayout();
        // We execute http request
        this.executeHTTPRequestWithRetrofit();
        // We configure click on item of recycler view
        this.configureOnClickRecyclerView();
    }

    private void configureRecyclerView() {
        // we create new adapter
        mArticleAdapter = new ArticleAdapter(mSearch);
        // we attach adapter to recycler view
        mRecyclerView.setAdapter(mArticleAdapter);
        // we set layout manager
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void configureOnClickRecyclerView() {
        ItemClickSupport.addTo(mRecyclerView, R.layout.fragment_article_item)
                .setOnItemClickListener((recyclerView, position, v) -> {
                    SearchArticleDoc searchArticleDoc = mArticleAdapter.getArticle(position);
                    // We start article container activity
                    Intent articleContainer = new Intent(SearchResultActivity.this, ArticleContainerActivity.class);
                    articleContainer.putExtra(EXTRA_URL_ARTICLE, searchArticleDoc.getWeb_url());
                    articleContainer.putExtra(EXTRA_TITLE_ARTICLE, searchArticleDoc.getSnippet());
                    startActivity(articleContainer);
                });
    }

    private void executeHTTPRequestWithRetrofit() {
        this.updateUIWhenStartingHTTPRequest();

        /* We convert format date for research */
        String beginDate = null;
        String endDate = null;

        try {
            beginDate = mSimpleDateFormat.format(Objects.requireNonNull(new SimpleDateFormat("dd/MM/yyyy", Locale.US).parse(mBeginDate)));
            endDate = mSimpleDateFormat.format(Objects.requireNonNull(new SimpleDateFormat("dd/MM/yyyy", Locale.US).parse(mEndDate)));
            Log.d(TAG, beginDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SearchArticleCall.fetchSearchArticle(this, mQueryTerm, mFQ, beginDate, endDate, getString(R.string.api_key_nyt));
    }

    private void updateUIWhenStartingHTTPRequest() {

    }

    private void configureSwipeRefreshLayout() {
        mSwipeRefreshLayout.setOnRefreshListener(this::executeHTTPRequestWithRetrofit);
    }

    @Override
    public void onResponse(@Nullable Search search) {
        Log.d(TAG, "Success");
        if (search != null) {
            if (search.getResponse().getDocs().size() < 1)
                showTextInDialogBox(getString(R.string.no_result_found));
            else this.updateUIWithSearchArticle(search);
        }
    }

    private void showTextInDialogBox(String message) {
        // We instanciate a builder of alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // We set a message to builder
        builder.setMessage(message);
        builder.setTitle(R.string.title_activity_main);

        // We create Alert Dialog from builder
        AlertDialog dialog = builder.create();
        // We show alert dialog
        dialog.show();
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
        this.updateUIWhenStoppingHTTPRequest();
    }

    private void updateUIWhenStoppingHTTPRequest() {
        Log.e(TAG, "An error happened with http request");
    }
}
