package com.team4runner.forrunner.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


import com.team4runner.forrunner.MainTreinadorActivity;
import com.team4runner.forrunner.R;

import com.team4runner.forrunner.fragment.HomeTreinadorMeusCorredoresFragment;
import com.team4runner.forrunner.fragment.HomeTreinadorNovidadesFragment;

/**
 * Created by Lucas on 29/06/2015.
 */
public class AbasPagerAdapterHomeTreinador extends FragmentStatePagerAdapter {

    int numTabs;
    MainTreinadorActivity activity;

    public AbasPagerAdapterHomeTreinador(MainTreinadorActivity activity, FragmentManager fm, int numTabs) {
        super(fm);
        this.activity = activity;
        this.numTabs = numTabs;
    }

    @Override
    public Fragment getItem(int position) {

        if(position == 0)
        {
            HomeTreinadorNovidadesFragment fragment = new HomeTreinadorNovidadesFragment();
            return fragment;
        }
        else
        {
            HomeTreinadorMeusCorredoresFragment fragment = new HomeTreinadorMeusCorredoresFragment();
            return fragment;
        }

    }


    @Override
    public CharSequence getPageTitle(int position) {

        if(position == 0)
        {

            return activity.getResources().getString(R.string.novidades);
        }
        else
        {
            return activity.getResources().getString(R.string.meuscorredores);
        }


    }

    @Override
    public int getCount() {
        return numTabs;
    }
}
