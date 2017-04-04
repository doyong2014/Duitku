package com.mi1.duitku.Tab1.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.mi1.duitku.Tab1.EventsFragment;
import com.mi1.duitku.Tab1.NewsFragment;
import com.mi1.duitku.Tab1.PromovFragment;


/**
 * Created by owner on 3/4/2017.
 */

public class TabPagerAdapter extends FragmentStatePagerAdapter {

    // Count number of tabs
    private int tabCount;

    public TabPagerAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {

        // Returning the current tabs
        switch (position) {
            case 0:
                NewsFragment tabFragment1 = new NewsFragment();
                return tabFragment1;
            case 1:
                PromovFragment tabFragment2 = new PromovFragment();
                return tabFragment2;
            case 2:
                EventsFragment tabFragment3 = new EventsFragment();
                return tabFragment3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}