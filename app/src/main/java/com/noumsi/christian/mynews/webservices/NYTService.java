package com.noumsi.christian.mynews.webservices;

import com.noumsi.christian.mynews.webservices.mostpopular.MostPopular;
import com.noumsi.christian.mynews.webservices.searcharticle.Search;
import com.noumsi.christian.mynews.webservices.topstorie.TopStories;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by christian-noumsi on 20/08/2018.
 */
public interface NYTService {
    @GET("svc/search/v2/articlesearch.json")
    Call<Search> searchArticle(@Query("q") String q, @Query("fq") String fq, @Query("api-key") String apiKey);

    @GET("svc/topstories/v2/home.json")
    Call<TopStories> topStories(@Query("api-key") String apiKey);

    @GET("svc/mostpopular/v2/mostviewed/{section}/1.json")
    Call<MostPopular> mostPopular(@Path("section") String section, @Query("api-key") String apiKey);

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.nytimes.com/")
            .addConverterFactory(JacksonConverterFactory.create())
            .build();
}
