package com.noumsi.christian.mynews.controller.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.noumsi.christian.mynews.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;


/**
 * Created by christian-noumsi on 27/11/2018.
 */
@RunWith(AndroidJUnit4.class)
public class SearchActivityTest {

    private SearchActivity mSearchActivity;
    private SharedPreferences mSharedPreferences;

    @Rule
    public ActivityTestRule<SearchActivity> mActivityTestRule = new ActivityTestRule<>(SearchActivity.class, true, false);

    @Before
    public void setUp() {
        mSearchActivity = mActivityTestRule.getActivity();
        Context context = getInstrumentation().getTargetContext();
        mSharedPreferences = context.getSharedPreferences("SearchActivity", Context.MODE_PRIVATE);

        mSharedPreferences.edit().clear().apply();
        mActivityTestRule.launchActivity(new Intent());

        Intents.init();
    }

    public void configureWidgets() {
        onView(withId(R.id.search_widget_edit_text_query))
                .check(matches(isDisplayed()))
                .perform(clearText(), typeText("France"), closeSoftKeyboard());

        onView(withId(R.id.search_widget_arts_cat))
                .perform(scrollTo())
                .check(matches(isDisplayed()))
                .perform(click());
    }

    @Test
    public void searchButtonIsEnable() throws InterruptedException {
        configureWidgets();

        Thread.sleep(1000);

        onView(withId(R.id.activity_search_search_button))
                .perform(scrollTo())
                .check(matches(isDisplayed()))
                .check(matches(isEnabled()));
    }

    @Test
    public void openingNewActivityWhenSearchButtonClicked() throws InterruptedException {
        configureWidgets();

        Thread.sleep(1000);

        onView(withId(R.id.activity_search_search_button))
                .perform(scrollTo())
                .check(matches(isDisplayed()))
                .perform(click());

        intended(hasComponent(SearchResultActivity.class.getName()));
    }

    @After
    public void setAfter () {
        Intents.release();
    }
}