package com.noumsi.christian.mynews.webservices.searcharticle;

import android.support.annotation.Nullable;

import com.noumsi.christian.mynews.webservices.NYTService;

import java.lang.ref.WeakReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by christian-noumsi on 30/08/2018.
 */
public class SearchArticleCall {

    // we creating a callback
    public interface Callbacks {
        void onResponse(@Nullable Search search);
        void onFailure();
    }

    public static void fetchSearchArticle(Callbacks callbacks, String q, String fq, String beginDate, String endDate, String apiKey) {
        final WeakReference<Callbacks> callbacksWeakReference = new WeakReference<>(callbacks);
        NYTService service = NYTService.retrofit.create(NYTService.class);

        Call<Search> searchCall = service.searchArticle(q, fq, beginDate, endDate, apiKey);
        searchCall.enqueue(new Callback<Search>() {
            @Override
            public void onResponse(Call<Search> call, Response<Search> response) {
                if (callbacksWeakReference.get() != null) callbacksWeakReference.get().onResponse(response.body());
            }

            @Override
            public void onFailure(Call<Search> call, Throwable t) {
                if (callbacksWeakReference.get() != null) callbacksWeakReference.get().onFailure();
            }
        });
    }
}
