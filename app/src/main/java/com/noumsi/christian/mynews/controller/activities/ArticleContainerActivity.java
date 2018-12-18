package com.noumsi.christian.mynews.controller.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.webkit.WebView;

import com.noumsi.christian.mynews.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.noumsi.christian.mynews.utils.Constants.EXTRA_TITLE_ARTICLE;
import static com.noumsi.christian.mynews.utils.Constants.EXTRA_URL_ARTICLE;

public class ArticleContainerActivity extends AppCompatActivity {

    @BindView(R.id.activity_article_container_web_view) WebView mWebView;
    private String mUrlArticle;
    private String mTitleArticle;
    private static final String TAG = "ArticleContainerActivit";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_container);
        ButterKnife.bind(this);

        // We get parameters passed in bundle
        if (getParametersInBundle()) {
            // We set title to Tool bar
            this.configureToolBar();

            // We load this url in web view
            this.configureWebView();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void configureToolBar() {
        setTitle(mTitleArticle);
    }

    private void configureWebView() {
        mWebView.loadUrl(mUrlArticle);
    }

    protected boolean getParametersInBundle() {
        Bundle extras = getIntent().getExtras();
        mUrlArticle = extras.getString(EXTRA_URL_ARTICLE, "");
        mTitleArticle = extras.getString(EXTRA_TITLE_ARTICLE, "");
        if (mUrlArticle.isEmpty())
            return false;
        else
            return true;
    }
}
