package com.noumsi.christian.mynews.views.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.noumsi.christian.mynews.controller.fragments.BusinessFragment;
import com.noumsi.christian.mynews.controller.fragments.MostPopularFragment;
import com.noumsi.christian.mynews.controller.fragments.TopStoriesFragment;

/**
 * Created by christian-noumsi on 21/08/2018.
 */
public class PageAdapter extends FragmentPagerAdapter {


    public PageAdapter(@NonNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return TopStoriesFragment.newInstance();
            case 1:
                return MostPopularFragment.newInstance();
            default:
                return BusinessFragment.newInstance();
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
