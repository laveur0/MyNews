package com.noumsi.christian.mynews.controller.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_container);
        ButterKnife.bind(this);

        // We get parameters passed in bundle
        getParametersInBundle();

        // We set title to Tool bar
        this.configureToolBar();

        // We load this url in web view
        this.configureWebView();
    }

    private void configureToolBar() {
        setTitle(mTitleArticle);
    }

    private void configureWebView() {
        mWebView.loadUrl(mUrlArticle);
    }

    private void getParametersInBundle() {
        Bundle extras = getIntent().getExtras();
        mUrlArticle = extras.getString(EXTRA_URL_ARTICLE, "");
        mTitleArticle = extras.getString(EXTRA_TITLE_ARTICLE, "");
    }
}
