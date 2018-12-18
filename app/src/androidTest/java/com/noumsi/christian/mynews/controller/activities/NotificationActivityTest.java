package com.noumsi.christian.mynews.controller.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.noumsi.christian.mynews.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by christian-noumsi on 23/11/2018.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class NotificationActivityTest {


    private NotificationActivity mNotificationActivity;
    private SharedPreferences mSharedPreferences;

    @Rule
    public ActivityTestRule<NotificationActivity> activityTestRule = new ActivityTestRule<>( NotificationActivity.class, true, false);

    @Before
    public void setUp() throws Exception {

        mNotificationActivity = activityTestRule.getActivity();
        Context context = getInstrumentation().getTargetContext();
        mSharedPreferences = context.getSharedPreferences("NotificationActivity", Context.MODE_PRIVATE);
    }

    @Test
    public void switchIsEnable() {
        mSharedPreferences.edit().clear().apply();
        activityTestRule.launchActivity(new Intent());

        onView(withId(R.id.search_widget_edit_text_query))
                .check(matches(isDisplayed()))
                .perform(clearText(), typeText("Cameroun"));

        onView(withId(R.id.search_widget_arts_cat))
                .check(matches(isDisplayed()))
                .perform(click())
                .check(matches(isChecked()));

        onView(withId(R.id.search_widget_switch))
                .check(matches(isDisplayed()))
                .check(matches(isEnabled()));
    }
}