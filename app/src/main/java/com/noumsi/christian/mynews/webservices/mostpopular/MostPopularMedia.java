package com.noumsi.christian.mynews.webservices.mostpopular;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by christian-noumsi on 20/08/2018.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MostPopularMedia {
    private String caption;
    @JsonProperty("media-metadata")
    private List<MostPopularMediaMetaData> mediadata;

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public List<MostPopularMediaMetaData> getMediadata() {
        return mediadata;
    }

    public void setMediadata(List<MostPopularMediaMetaData> mediadata) {
        this.mediadata = mediadata;
    }
}
