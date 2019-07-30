package com.noumsi.christian.mynews.webservices.mostpopular;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.noumsi.christian.mynews.webservices.NYTService;

import java.lang.ref.WeakReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by christian-noumsi on 21/08/2018.
 */
public class MostPopularCall {

    // we creating a callback
    public interface Callbacks {
        void onResponse(@Nullable MostPopular mostPopular);
        void onFailure();
    }

    // Public method to start fetching most popular article
    public static void fetchMostPopularArticle(Callbacks callbacks, String section, String apiKey) {
        // We create a weak reference to callback
        final WeakReference<Callbacks> callbacksWeakReference = new WeakReference<>(callbacks);

        // we get a retrofite instance
        NYTService service = NYTService.retrofit.create(NYTService.class);

        Call<MostPopular> call = service.mostPopular(section, apiKey);
        call.enqueue(new Callback<MostPopular>() {
            @Override
            public void onResponse(@NonNull Call<MostPopular> call, @NonNull Response<MostPopular> response) {
                if (callbacksWeakReference.get() != null) callbacksWeakReference.get().onResponse(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<MostPopular> call, @NonNull Throwable t) {
                if (callbacksWeakReference.get() != null) callbacksWeakReference.get().onFailure();
            }
        });
    }
}
