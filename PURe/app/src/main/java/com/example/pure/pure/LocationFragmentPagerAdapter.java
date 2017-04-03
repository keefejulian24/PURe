package com.example.pure.pure;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class LocationFragmentPagerAdapter extends FragmentStatePagerAdapter {
    final int TOTAL_PAGE_NUMBER = 2;
    private String tabTitles[] = new String[]{"Maps", "Parks"};

    public LocationFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return TOTAL_PAGE_NUMBER;
    }

    @Override
    public Fragment getItem(int position) {
        return LocationPageFragment.newInstance(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}