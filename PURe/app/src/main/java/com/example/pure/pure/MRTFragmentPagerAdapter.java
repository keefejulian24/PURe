package com.example.pure.pure;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class MRTFragmentPagerAdapter extends FragmentStatePagerAdapter {
    final int TOTAL_PAGE_NUMBER = 5;
    private String tabTitles[] = new String[]{"NSL", "NEL", "EWL", "CL", "DTL"};

    public MRTFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return TOTAL_PAGE_NUMBER;
    }

    @Override
    public Fragment getItem(int position) {
        return MRTPageFragment.newInstance(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}