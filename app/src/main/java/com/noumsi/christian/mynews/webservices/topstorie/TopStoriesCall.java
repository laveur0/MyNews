package com.noumsi.christian.mynews.webservices.topstorie;

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
public class TopStoriesCall {

    // we creating a callback
    public interface Callbacks {
        void onResponse(@Nullable TopStories topStories);
        void onFailure();
    }

    // Public method to start fetching top stories article
    public static void fetchTopStoriesArticle(Callbacks callbacks, String apiKey) {
        // We create a weak reference to callback
        final WeakReference<Callbacks> callbacksWeakReference = new WeakReference<>(callbacks);

        // we get a retrofite instance
        NYTService service = NYTService.retrofit.create(NYTService.class);

        Call<TopStories> call = service.topStories(apiKey);
        call.enqueue(new Callback<TopStories>() {
            @Override
            public void onResponse(@NonNull Call<TopStories> call, @NonNull Response<TopStories> response) {
                if (callbacksWeakReference.get() != null) callbacksWeakReference.get().onResponse(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<TopStories> call, @NonNull Throwable t) {
                if (callbacksWeakReference.get() != null) callbacksWeakReference.get().onFailure();
            }
        });
    }
}
