package com.noumsi.christian.mynews.controller.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.noumsi.christian.mynews.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.InstrumentationRegistry.getInstrumentation;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

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
    public void switchIsEnable() throws InterruptedException {
        mSharedPreferences.edit().clear().apply();
        activityTestRule.launchActivity(new Intent());

        onView(withId(R.id.search_widget_edit_text_query))
                .perform(scrollTo())
                .check(matches(isDisplayed()))
                .perform(clearText(), typeText("Cameroun"), closeSoftKeyboard());

        onView(withId(R.id.search_widget_arts_cat))
                .perform(scrollTo())
                .check(matches(isDisplayed()))
                .perform(click())
                .check(matches(isChecked()));

        Thread.sleep(1000);

        onView(withId(R.id.search_widget_switch))
                .perform(scrollTo())
                .check(matches(isDisplayed()))
                .check(matches(isEnabled()));
    }
}