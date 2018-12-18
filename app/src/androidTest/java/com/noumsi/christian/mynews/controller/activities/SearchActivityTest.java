package com.noumsi.christian.mynews.controller.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.noumsi.christian.mynews.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;


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
    public void setUp() throws Exception {
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
                .check(matches(isDisplayed()))
                .perform(click());
    }

    @Test
    public void searchButtonIsEnable() {
        configureWidgets();

        onView(withId(R.id.activity_search_search_button))
                .check(matches(isDisplayed()))
                .check(matches(isEnabled()));
    }

    @Test
    public void openingNewActivityWhenSearchButtonClicked() {
        configureWidgets();

        onView(withId(R.id.activity_search_search_button))
                .check(matches(isDisplayed()))
                .perform(click());

        intended(hasComponent(SearchResultActivity.class.getName()));
    }

    @After
    public void setAfter () {
        Intents.release();
    }
}