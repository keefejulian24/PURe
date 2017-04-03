package com.example.pure.pure;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


public class CustomFragmentPagerAdapter extends FragmentPagerAdapter {
    private int totalTabs;
    private String[] tabTitles;
    private int[] layoutId;

    public CustomFragmentPagerAdapter(FragmentManager fm, String[] tabTitles, int[] layoutId) {
        super(fm);
        this.layoutId = layoutId;
        this.tabTitles = tabTitles;
        this.totalTabs = tabTitles.length;
    }

    @Override
    public int getCount() {
        return totalTabs;
    }

    @Override
    public Fragment getItem(int position) {
        return PageFragment.newInstance(position, layoutId[position]);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}