package com.noumsi.christian.mynews.webservices.searcharticle;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by christian-noumsi on 21/08/2018.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchArticleResponse {
    private List<SearchArticleDoc> docs;

    public List<SearchArticleDoc> getDocs() {
        return docs;
    }

    public void setDocs(List<SearchArticleDoc> docs) {
        this.docs = docs;
    }
}
