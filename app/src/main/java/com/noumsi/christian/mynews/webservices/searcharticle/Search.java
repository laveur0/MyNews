package com.noumsi.christian.mynews.webservices.searcharticle;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by christian-noumsi on 20/08/2018.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Search {
    private SearchArticleResponse response;

    public SearchArticleResponse getResponse() {
        return response;
    }

    public void setResponse(SearchArticleResponse response) {
        this.response = response;
    }
}
