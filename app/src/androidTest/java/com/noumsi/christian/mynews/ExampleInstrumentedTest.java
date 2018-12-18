package com.noumsi.christian.mynews;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.TextView;

import com.noumsi.christian.mynews.controller.activities.MainActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class ExampleInstrumentedTest
{
    private MainActivity mTestActivity;
    private TextView mTestEmptyText;

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>( MainActivity.class);

    @Before
    public void setUp() {
        mTestActivity = activityTestRule.getActivity();
        //mTestEmptyText = mTestActivity.findViewById(R.id.test);
    }
    // Starts the activity under test using // the default Intent with: // action = {@link Intent#ACTION_MAIN} // flags = {@link Intent#FLAG_ACTIVITY_NEW_TASK} // All other fields are null or empty. mTestActivity = getActivity(); mTestEmptyText = (TextView) mTestActivity .findViewById(R.id.empty); mFab = (FloatingActionButton) mTestActivity .findViewById(R.id.fab); }


    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.noumsi.christian.mynews", appContext.getPackageName());
    }

    @Test
    public void testInstrumented() {
        //onView(withId(R.id.fragment_article_item_section)).perform(typeText("Installed"));
        assertNotNull(mTestEmptyText);
        assertNotNull(mTestActivity);
    }

}
