package com.noumsi.christian.mynews.views;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.noumsi.christian.mynews.GlideApp;
import com.noumsi.christian.mynews.R;
import com.noumsi.christian.mynews.webservices.mostpopular.MostPopularResult;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by christian-noumsi on 30/08/2018.
 */
public class PopularViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.fragment_article_item_imagev) ImageView mImageView;
    @BindView(R.id.fragment_article_item_section) TextView mSection;
    @BindView(R.id.fragment_article_item_updated_date) TextView mUpdateDate;
    @BindView(R.id.fragment_article_item_title) TextView mTitle;
    private static final String TAG = "PopularViewHolder";

    public PopularViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void updateWithPopular(MostPopularResult result) {
        mSection.setText(result.getSection() != null ? result.getSection() : "");
        mUpdateDate.setText(result.getPublished_date() != null ? this.convertDate(result.getPublished_date()) : "");
        mTitle.setText(result.getTitle() != null ? result.getTitle() : "");

        GlideApp.with(mImageView)
                .load(result.getMedia().size() > 0 ? result.getMedia().get(0).getMediadata().get(0).getUrl() : "")
                .error(R.drawable.ic_launcher_background)
                .apply(RequestOptions.centerInsideTransform())
                .into(mImageView);
    }

    private String convertDate(String published_date) {
        // we split date
        String[] splitDate = published_date.split("-");
        // we reorganize date
        return splitDate[2] + "/" + splitDate[1] + "/" + splitDate[0];
    }
}
