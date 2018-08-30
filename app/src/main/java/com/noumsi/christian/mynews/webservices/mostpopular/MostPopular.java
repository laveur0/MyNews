package com.noumsi.christian.mynews.webservices.mostpopular;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by christian-noumsi on 20/08/2018.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MostPopular {
    private String status;
    private String copyright;
    private int num_results;
    private List<MostPopularResult> results;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public int getNum_results() {
        return num_results;
    }

    public void setNum_results(int num_results) {
        this.num_results = num_results;
    }

    public List<MostPopularResult> getResults() {
        return results;
    }

    public void setResults(List<MostPopularResult> results) {
        this.results = results;
    }
}
