package com.kpi.money.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.kpi.money.fragments.HomeFragment;
import com.kpi.money.fragments.TransactionsFragment;

/**
 * Developed by DroidOXY
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private CharSequence Titles[];
    private int NumbOfTabs;

    // Constructor
    public ViewPagerAdapter(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb) {
        super(fm);
        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        if(position == 0) {

            return new HomeFragment();

        }else{

            return new TransactionsFragment();

        }
    }

    @Override
    public CharSequence getPageTitle(int position){ return Titles[position]; }

    @Override
    public int getCount(){ return NumbOfTabs; }

}