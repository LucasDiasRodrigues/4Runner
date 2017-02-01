package com.team4runner.forrunner.Postergado_NaoUsado;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.team4runner.forrunner.R;
import com.team4runner.forrunner.RunActivity;
import com.team4runner.forrunner.fragment.HomeCorredorMeuTreinoFragment;
import com.team4runner.forrunner.Postergado_NaoUsado.NAOUSADORunEstatisticasFragment;

/**
 * Created by Lucas on 17/09/2015.
 */
public class NAOUSADOAbasPagerAdapterRun extends FragmentStatePagerAdapter {



    int numTabs;
    RunActivity activity;



    public NAOUSADOAbasPagerAdapterRun(FragmentManager fm, RunActivity activity, int numTabs) {
        super(fm);
        this.activity = activity;
        this.numTabs = numTabs;
    }

    @Override
    public Fragment getItem(int position) {

        if(position == 0)
        {
            NAOUSADORunEstatisticasFragment fragment = new NAOUSADORunEstatisticasFragment();
            return fragment;
        }
        else
        {
            HomeCorredorMeuTreinoFragment fragment = new HomeCorredorMeuTreinoFragment();
            return fragment;
        }

    }


    @Override
    public CharSequence getPageTitle(int position) {

        if(position == 0)
        {

            return activity.getResources().getString(R.string.estatisticas);
        }
        else
        {
            return activity.getResources().getString(R.string.meutreino);
        }


    }


    @Override
    public int getCount() {
        return numTabs;
    }
}
