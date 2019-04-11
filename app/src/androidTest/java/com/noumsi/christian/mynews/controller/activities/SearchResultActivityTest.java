package com.noumsi.christian.mynews.controller.activities;

import android.content.Context;
import android.content.Intent;

import com.noumsi.christian.mynews.R;
import com.noumsi.christian.mynews.webservices.searcharticle.Search;
import com.noumsi.christian.mynews.webservices.searcharticle.SearchArticleDoc;
import com.noumsi.christian.mynews.webservices.searcharticle.SearchArticleMultimedia;
import com.noumsi.christian.mynews.webservices.searcharticle.SearchArticleResponse;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.InstrumentationRegistry.getInstrumentation;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static com.noumsi.christian.mynews.utils.Constants.EXTRA_BEGIN_DATE;
import static com.noumsi.christian.mynews.utils.Constants.EXTRA_END_DATE;
import static com.noumsi.christian.mynews.utils.Constants.EXTRA_FQ;
import static com.noumsi.christian.mynews.utils.Constants.EXTRA_QUERY_TERM;

/**
 * Created by christian-noumsi on 27/11/2018.
 */
@RunWith(AndroidJUnit4.class)
public class SearchResultActivityTest {

    private SearchResultActivity mSearchResultActivity;
    private RecyclerView mRecyclerView;
    private Context context;

    @Rule
    public ActivityTestRule<SearchResultActivity> mActivityTestRule = new ActivityTestRule<>(SearchResultActivity.class, true, false);


    @Before
    public void setUp() throws Throwable {
        context = getInstrumentation().getTargetContext();

        // We define new intent for result activity
        Intent intent = new Intent();
        intent.putExtra(EXTRA_QUERY_TERM, "France");
        intent.putExtra(EXTRA_FQ, "news_desk:(\"Business\")");
        intent.putExtra(EXTRA_BEGIN_DATE, "");
        intent.putExtra(EXTRA_END_DATE, "");

        // We launch activity
        mActivityTestRule.launchActivity(intent);
        mSearchResultActivity = mActivityTestRule.getActivity();
        Intents.init();
    }

    @Test
    public void showArticleContainerWhenClickOnItem() {

        SearchArticleResponse searchArticleResponse = new SearchArticleResponse();
        SearchArticleDoc searchArticleDoc = new SearchArticleDoc();

        SearchArticleMultimedia searchArticleMultimedia = new SearchArticleMultimedia();
        searchArticleMultimedia.setUrl("url");

        List<SearchArticleMultimedia> searchArticleMultimediaList = new ArrayList<>();
        searchArticleMultimediaList.add(searchArticleMultimedia);

        List<SearchArticleDoc> searchArticleDocs = new ArrayList<>();

        searchArticleDoc.setMultimedia(searchArticleMultimediaList);
        searchArticleDoc.setNews_desk("Sport");
        searchArticleDoc.setPub_date("01-01-01T");
        searchArticleDoc.setSnippet("snipet");
        searchArticleDoc.setType_of_material("type_material");
        searchArticleDoc.setWeb_url("web_url");

        searchArticleDocs.add(searchArticleDoc);

        searchArticleResponse.setDocs(searchArticleDocs);
        final Search search = new Search();
        search.setResponse(searchArticleResponse);

        mSearchResultActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mSearchResultActivity.onResponse(search);
            }
        });
        onView(withId(R.id.activity_search_result_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        intended(hasComponent(ArticleContainerActivity.class.getName()));
    }

    @After
    public void setAfter() {
        Intents.release();
    }
}