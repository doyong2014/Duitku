package com.mi1.duitku.Tab3;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

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
                DompetFragment tabFragment1 = new DompetFragment();
                return tabFragment1;
            case 1:
                CashInFragment tabFragment2 = new CashInFragment();
                return tabFragment2;
            case 2:
                CashOutFragment tabFragment3 = new CashOutFragment();
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