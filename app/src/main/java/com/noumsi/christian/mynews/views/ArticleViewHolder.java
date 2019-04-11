package com.noumsi.christian.mynews.views;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;
import com.noumsi.christian.mynews.GlideApp;
import com.noumsi.christian.mynews.R;
import com.noumsi.christian.mynews.webservices.searcharticle.SearchArticleDoc;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by christian-noumsi on 30/08/2018.
 */
public class ArticleViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.fragment_article_item_imagev) ImageView mImageView;
    @BindView(R.id.fragment_article_item_section) TextView mSection;
    @BindView(R.id.fragment_article_item_updated_date) TextView mUpdateDate;
    @BindView(R.id.fragment_article_item_title) TextView mTitle;
    private static final String TAG = "ArticleViewHolder";

    public ArticleViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void updateWithArticle(SearchArticleDoc result) {
        GlideApp.with(mImageView)
                .load(result.getMultimedia().size() > 0 ? "https://static01.nyt.com/" + result.getMultimedia().get(0).getUrl() : "")
                .error(R.drawable.ic_launcher_background)
                .apply(RequestOptions.centerCropTransform())
                .into(mImageView);

        mSection.setText(result.getNews_desk());
        mUpdateDate.setText(result.getPub_date() != null ? this.convertDate(result.getPub_date()) : "");
        mTitle.setText(result.getSnippet() != null ? result.getSnippet() : "");
    }

    private String convertDate(String pub_date) {
        String onlyDate = pub_date.split("T")[0];
        // we split date
        String[] splitDate = onlyDate.split("-");
        // we reorganize date
        return splitDate[2] + "/" + splitDate[1] + "/" + splitDate[0];
    }
}
