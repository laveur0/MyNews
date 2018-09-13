package com.noumsi.christian.mynews.views.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.RequestManager;
import com.noumsi.christian.mynews.R;
import com.noumsi.christian.mynews.views.ArticleViewHolder;
import com.noumsi.christian.mynews.webservices.searcharticle.Search;
import com.noumsi.christian.mynews.webservices.searcharticle.SearchArticleDoc;

/**
 * Created by christian-noumsi on 30/08/2018.
 */
public class ArticleAdapter extends RecyclerView.Adapter<ArticleViewHolder> {
    private Search mSearch;

    public ArticleAdapter(Search search) {
        mSearch = search;
    }

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fragment_article_item, parent, false);
        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
        if (mSearch != null) holder.updateWithArticle(mSearch.getResponse().getDocs().get(position));
    }

    @Override
    public int getItemCount() {
        return mSearch != null ? mSearch.getResponse().getDocs().size() : 0;
    }

    public void setSearch(Search search) {
        mSearch = search;
    }

    public SearchArticleDoc getArticle(int position) {
        return mSearch.getResponse().getDocs().get(position);
    }
}
