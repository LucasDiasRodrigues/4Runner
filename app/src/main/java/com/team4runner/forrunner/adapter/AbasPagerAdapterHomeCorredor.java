package com.team4runner.forrunner.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.team4runner.forrunner.MainCorredorActivity;
import com.team4runner.forrunner.R;
import com.team4runner.forrunner.fragment.HomeCorredorMeuTreinoFragment;
import com.team4runner.forrunner.fragment.HomeCorredorTreinadorFragment;

public class AbasPagerAdapterHomeCorredor extends FragmentStatePagerAdapter {


    int numTabs;
    MainCorredorActivity activity;



    public AbasPagerAdapterHomeCorredor(MainCorredorActivity activity, FragmentManager fm, int numTabs) {
        super(fm);

        this.activity = activity;
        this.numTabs = numTabs;

    }


    @Override
    public Fragment getItem(int position) {

        if(position == 0)
        {
            HomeCorredorMeuTreinoFragment fragment = new HomeCorredorMeuTreinoFragment();
            return fragment;
        }
        else
        {
            HomeCorredorTreinadorFragment fragment = new HomeCorredorTreinadorFragment();
            return fragment;
        }


    }



    @Override
    public CharSequence getPageTitle(int position) {

        if(position == 0)
        {

            return activity.getResources().getString(R.string.meutreino);
        }
        else
        {
            return activity.getResources().getString(R.string.meutreinador);
        }


    }


    @Override
    public int getCount() {
        return numTabs;
    }
}

