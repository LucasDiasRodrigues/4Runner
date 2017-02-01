
package com.team4runner.forrunner.fragment;


import android.os.Bundle;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.team4runner.forrunner.MainCorredorActivity;
import com.team4runner.forrunner.R;
import com.team4runner.forrunner.adapter.AbasPagerAdapterHomeCorredor;

/**
 * Created by Lucas on 11/06/2015.
 */
public class HomeCorredorFragment extends Fragment {

    private ViewPager mViewPager;
    private int numTabs = 2;
    private TabLayout mTabLayout;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentHome = inflater.inflate(R.layout.fragment_home_corredor,container,false);

        AbasPagerAdapterHomeCorredor pagerAdapter = new AbasPagerAdapterHomeCorredor((MainCorredorActivity)getActivity(),getChildFragmentManager(),numTabs);
        mViewPager = (ViewPager) fragmentHome.findViewById(R.id.pager);
        mViewPager.setAdapter(pagerAdapter);

        mTabLayout = (TabLayout)fragmentHome.findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);



        return fragmentHome;
    }
}
