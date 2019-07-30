package com.noumsi.christian.mynews.controller.activities;

import android.content.Intent;
import android.os.Bundle;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static com.noumsi.christian.mynews.utils.Constants.EXTRA_TITLE_ARTICLE;
import static com.noumsi.christian.mynews.utils.Constants.EXTRA_URL_ARTICLE;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by christian-noumsi on 22/10/2018.
 */
@RunWith(MockitoJUnitRunner.class)
public class ArticleContainerActivityTest {

    private ArticleContainerActivity mArticleContainerActivity = mock(ArticleContainerActivity.class);
    @Mock
    Bundle mBundle;

    @Test
    public void getParametersInBundle() {

        when(mBundle.getString(EXTRA_URL_ARTICLE, "")).thenReturn("url");
        when(mBundle.getString(EXTRA_TITLE_ARTICLE, "")).thenReturn("title");
        assertThat(mBundle.getString(EXTRA_URL_ARTICLE, ""), is("url"));

        Intent intent = mock(Intent.class);
        when(intent.getExtras()).thenReturn(mBundle);

        when(mArticleContainerActivity.getIntent()).thenReturn(intent);

        when(mArticleContainerActivity.getParametersInBundle()).thenCallRealMethod();
        assertThat(mArticleContainerActivity.getParametersInBundle(), is(true));
    }
}