package com.noumsi.christian.mynews.webservices.searcharticle;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by christian-noumsi on 21/08/2018.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchArticleMultimedia {
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
