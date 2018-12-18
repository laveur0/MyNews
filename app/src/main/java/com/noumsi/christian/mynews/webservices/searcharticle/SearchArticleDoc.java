package com.noumsi.christian.mynews.webservices.searcharticle;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by christian-noumsi on 21/08/2018.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchArticleDoc {
    private String news_desk;
    private String type_of_material;
    private String web_url;
    private String pub_date;
    private String snippet;
    private List<SearchArticleMultimedia> multimedia;

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public String getNews_desk() {
        return news_desk;
    }

    public String getPub_date() {
        return pub_date;
    }

    public void setPub_date(String pub_date) {
        this.pub_date = pub_date;
    }

    public void setNews_desk(String news_desk) {
        this.news_desk = news_desk;
    }

    public String getType_of_material() {
        return type_of_material;
    }

    public void setType_of_material(String type_of_material) {
        this.type_of_material = type_of_material;
    }

    public String getWeb_url() {
        return web_url;
    }

    public void setWeb_url(String web_url) {
        this.web_url = web_url;
    }

    public List<SearchArticleMultimedia> getMultimedia() {
        return multimedia;
    }

    public void setMultimedia(List<SearchArticleMultimedia> multimedia) {
        this.multimedia = multimedia;
    }
}
