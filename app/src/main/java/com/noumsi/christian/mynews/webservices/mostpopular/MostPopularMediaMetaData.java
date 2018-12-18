package com.noumsi.christian.mynews.webservices.mostpopular;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by christian-noumsi on 20/08/2018.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MostPopularMediaMetaData {
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
