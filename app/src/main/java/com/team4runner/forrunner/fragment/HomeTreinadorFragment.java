package com.team4runner.forrunner.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.team4runner.forrunner.MainTreinadorActivity;
import com.team4runner.forrunner.R;

import com.team4runner.forrunner.adapter.AbasPagerAdapterHomeTreinador;

/**
 * Created by Lucas on 29/06/2015.
 */
public class HomeTreinadorFragment extends Fragment {

    private ViewPager mViewPager;
    private int numTabs = 2;
    private TabLayout mTabLayout;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_home_treinador, container, false);

        AbasPagerAdapterHomeTreinador pagerAdapter = new AbasPagerAdapterHomeTreinador((MainTreinadorActivity) getActivity(), getChildFragmentManager(), numTabs);
        mViewPager = (ViewPager) fragment.findViewById(R.id.pager);
        mViewPager.setAdapter(pagerAdapter);

        if(((MainTreinadorActivity) getActivity()).origemNotificacao()){
            mViewPager.setCurrentItem(1);
        }

        mTabLayout = (TabLayout) fragment.findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);





        //actionBar.setSelectedNavigationItem(1);


        return fragment;
    }
}
