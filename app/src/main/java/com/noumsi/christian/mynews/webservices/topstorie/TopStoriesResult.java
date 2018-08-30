package com.noumsi.christian.mynews.webservices.topstorie;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by christian-noumsi on 20/08/2018.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TopStoriesResult {
    private String section;
    private String subsection;
    private String updated_date;
    private String title;
    private String url;
    private List<TopStoriesMultimedia> multimedia;

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getSubsection() {
        return subsection;
    }

    public void setSubsection(String subsection) {
        this.subsection = subsection;
    }

    public String getUpdated_date() {
        return updated_date;
    }

    public void setUpdated_date(String updated_date) {
        this.updated_date = updated_date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<TopStoriesMultimedia> getMultimedia() {
        return multimedia;
    }

    public void setMultimedia(List<TopStoriesMultimedia> multimedia) {
        this.multimedia = multimedia;
    }
}
