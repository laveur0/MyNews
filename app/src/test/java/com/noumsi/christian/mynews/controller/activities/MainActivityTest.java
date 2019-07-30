package com.noumsi.christian.mynews.controller.activities;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.noumsi.christian.mynews.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by christian-noumsi on 03/10/2018.
 */
@RunWith(MockitoJUnitRunner.class)
public class MainActivityTest {

    private MainActivity mMainActivity = mock(MainActivity.class);
    @Mock
    private Menu mMenu;
    @Mock
    private MenuItem item;

    @Before
    public void method() {
        when(mMainActivity.getMenuInflater()).thenReturn(mock(MenuInflater.class));
        when(mMainActivity.onCreateOptionsMenu(mMenu)).thenCallRealMethod();
    }

    @Test
    public void GIVEN_Menu_WHEN_CallOnCreateOptionsMenu_THEN_ReturnTrue() {
        assertThat(mMainActivity.onCreateOptionsMenu(mMenu), is(true));
    }

    @Test
    public void GIVEN_MenuItem_WHEN_CallOnOptionsItemSelected_THEN_ReturnTrue() {
        mMainActivity.onCreateOptionsMenu(mMenu);
        when(mMainActivity.onOptionsItemSelected(item)).thenCallRealMethod();
        when(item.getItemId()).thenReturn(R.id.action_about);
        assertThat(mMainActivity.onOptionsItemSelected(item), is(true));
    }
}