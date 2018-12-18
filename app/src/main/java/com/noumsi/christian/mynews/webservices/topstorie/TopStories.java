package com.noumsi.christian.mynews.webservices.topstorie;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by christian-noumsi on 20/08/2018.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TopStories {
    private String status;
    private String copyright;
    private String section;
    private int num_results;
    private List<TopStoriesResult> results;

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

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public int getNum_results() {
        return num_results;
    }

    public void setNum_results(int num_results) {
        this.num_results = num_results;
    }

    public List<TopStoriesResult> getResults() {
        return results;
    }

    public void setResults(List<TopStoriesResult> results) {
        this.results = results;
    }
}
