package com.noumsi.christian.mynews.webservices.topstorie;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by christian-noumsi on 20/08/2018.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TopStoriesMultimedia {
    private String url;
    private String caption;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }
}
