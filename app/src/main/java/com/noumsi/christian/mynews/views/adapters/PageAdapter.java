package com.noumsi.christian.mynews.views.adapters;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.noumsi.christian.mynews.controller.fragments.BusinessFragment;
import com.noumsi.christian.mynews.controller.fragments.MostPopularFragment;
import com.noumsi.christian.mynews.controller.fragments.TopStoriesFragment;

/**
 * Created by christian-noumsi on 21/08/2018.
 */
public class PageAdapter extends FragmentPagerAdapter {

    public PageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return TopStoriesFragment.newInstance();
            case 1:
                return MostPopularFragment.newInstance();
            case 2:
                return BusinessFragment.newInstance();
                default:
                    return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Top Stories";
            case 1:
                return "Most Popular";
            case 2:
                return "Business";
                default:
                    return "No Name";
        }
    }
}
