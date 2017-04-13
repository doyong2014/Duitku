package com.mi1.duitku.Main;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mi1.duitku.R;
import com.mi1.duitku.Tab1.Adapter.TabPagerAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class Tab1Fragment extends Fragment {

    private TabLayout topTap;
    private ViewPager viewPager;

    public Tab1Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_tab, container, false);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);

        // Initializing the TabLayout
        topTap = (TabLayout) view.findViewById(R.id.tab_top);
        topTap.addTab(topTap.newTab().setText("NEWS"));
        topTap.addTab(topTap.newTab().setText("PROMO"));
        topTap.addTab(topTap.newTab().setText("EVENTS"));
        topTap.setTabGravity(TabLayout.GRAVITY_FILL);
        topTap.setTabMode(TabLayout.MODE_FIXED);
        topTap.setSelectedTabIndicatorColor(Color.YELLOW);

        // Initializing ViewPager
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(2);

        // Creating TabPagerAdapter adapter
        TabPagerAdapter pagerAdapter = new TabPagerAdapter(activity.getSupportFragmentManager(), topTap.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(topTap));

        // Set TabSelectedListener
        topTap.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return view;
    }

}
