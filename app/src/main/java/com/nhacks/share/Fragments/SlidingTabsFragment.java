package com.nhacks.share.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nhacks.share.R;
import com.nhacks.share.ui.SlidingTabLayout;

/**
 * Created by SagarkumarDave on 12/17/2015.
 */
public class SlidingTabsFragment extends Fragment {
    private ViewPager mPager;
    private SlidingTabLayout mTabs;

    public static SlidingTabsFragment getInstance(int position){
        SlidingTabsFragment slidingTabsFragment = new SlidingTabsFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        slidingTabsFragment.setArguments(args);
        return slidingTabsFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View layout = inflater.inflate(R.layout.l_sliding_tabs_fragment, container, false);
        mPager = (ViewPager) layout.findViewById(R.id.pager);
        mPager.setAdapter(new MyPagerAdapter((getChildFragmentManager())));
        mTabs = (SlidingTabLayout) layout.findViewById(R.id.tabs);
        mTabs.setDistributeEvenly(true);
        mTabs.setViewPager(mPager);

        return layout;
    }

    class MyPagerAdapter extends FragmentPagerAdapter {
        String[] tabNames;
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
            tabNames = getResources().getStringArray(R.array.tabs);
        }

        @Override
        public Fragment getItem(int i) {
            if(i == 0){
                return SampleFragment.getInstance(i);
            }
            else if(i == 1){
                return SampleFragment.getInstance(i);
            }
            else if(i == 2){
                return SampleFragment.getInstance(i);
            }
            return SampleFragment.getInstance(i);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabNames[position];
        }

        @Override
        public int getCount() {
            return tabNames.length;
        }
    }

}
