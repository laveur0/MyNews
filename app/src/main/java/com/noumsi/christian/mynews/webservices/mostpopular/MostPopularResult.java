package com.noumsi.christian.mynews.webservices.mostpopular;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by christian-noumsi on 20/08/2018.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MostPopularResult {
    private String url;
    private String section;
    private String byline;
    private String title;
    @JsonProperty("abstract")
    private String mAbstract;
    private String published_date;
    private List<MostPopularMedia> media;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getByline() {
        return byline;
    }

    public void setByline(String byline) {
        this.byline = byline;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAbstract() {
        return mAbstract;
    }

    public void setAbstract(String anAbstract) {
        mAbstract = anAbstract;
    }

    public String getPublished_date() {
        return published_date;
    }

    public void setPublished_date(String published_date) {
        this.published_date = published_date;
    }

    public List<MostPopularMedia> getMedia() {
        return media;
    }

    public void setMedia(List<MostPopularMedia> media) {
        this.media = media;
    }
}
