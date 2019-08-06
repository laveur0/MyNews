package com.noumsi.christian.mynews.webservices.searcharticle;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by christian-noumsi on 2019-08-01.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchArticleMeta {

    private int hits;

    public int getHits() {
        return hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }
}
