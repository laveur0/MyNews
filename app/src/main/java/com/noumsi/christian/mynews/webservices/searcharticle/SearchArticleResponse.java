package com.noumsi.christian.mynews.webservices.searcharticle;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by christian-noumsi on 21/08/2018.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchArticleResponse {

    private SearchArticleMeta meta;

    private List<SearchArticleDoc> docs;

    public SearchArticleMeta getMeta() {
        return meta;
    }

    public void setMeta(SearchArticleMeta meta) {
        this.meta = meta;
    }

    public List<SearchArticleDoc> getDocs() {
        return docs;
    }

    public void setDocs(List<SearchArticleDoc> docs) {
        this.docs = docs;
    }
}
