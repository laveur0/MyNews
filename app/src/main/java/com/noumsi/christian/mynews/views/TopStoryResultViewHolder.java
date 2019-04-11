package com.noumsi.christian.mynews.views;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;
import com.noumsi.christian.mynews.GlideApp;
import com.noumsi.christian.mynews.R;
import com.noumsi.christian.mynews.webservices.topstorie.TopStoriesResult;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by christian-noumsi on 21/08/2018.
 */
public class TopStoryResultViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.fragment_article_item_imagev) ImageView mImageView;
    @BindView(R.id.fragment_article_item_section) TextView mSection;
    @BindView(R.id.fragment_article_item_updated_date) TextView mUpdateDate;
    @BindView(R.id.fragment_article_item_title) TextView mTitle;
    private static final String TAG = "TopStoryResultViewHol";

    public TopStoryResultViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void updateWithTopStoriesResult(TopStoriesResult result) {
        GlideApp.with(mImageView)
                .load(result.getMultimedia().size() > 0 ? result.getMultimedia().get(0).getUrl() : "")
                .error(R.drawable.ic_launcher_background)
                .apply(RequestOptions.centerInsideTransform())
                .into(mImageView);

        mSection.setText(result.getSection() != null ?
                !result.getSection().isEmpty() ?
                        result.getSubsection() != null ?
                                !result.getSubsection().isEmpty() ?
                                        result.getSection() + " > " + result.getSubsection() :
                                        result.getSection()
                                : result.getSection()
                        : ""
                : "");
        mUpdateDate.setText(this.convertDate(result.getUpdated_date()));
        mTitle.setText(result.getTitle());
    }

    private String convertDate(String updated_date) {
        String onlyDate = updated_date.split("T")[0];
        // we split date
        String[] splitDate = onlyDate.split("-");
        // we reorganize date
        return splitDate[2] + "/" + splitDate[1] + "/" + splitDate[0];
    }
}
