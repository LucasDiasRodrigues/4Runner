package com.team4runner.forrunner.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.team4runner.forrunner.R;
import com.team4runner.forrunner.VisualizarCorridaActivity;
import com.team4runner.forrunner.fragment.VisualizaGraficoCorridasFragment;
import com.team4runner.forrunner.fragment.VisualizaRotaCorridasFragment;

/**
 * Created by Lucas on 29/12/2015.
 */
public class AbasPagerAdapterVisualizaCorrida extends FragmentStatePagerAdapter{


    int numTabs;
    VisualizarCorridaActivity activity;



    public AbasPagerAdapterVisualizaCorrida(VisualizarCorridaActivity activity, FragmentManager fm, int numTabs) {
        super(fm);

        this.activity = activity;
        this.numTabs = numTabs;

    }


    @Override
    public Fragment getItem(int position) {

        if(position == 0)
        {
            VisualizaRotaCorridasFragment fragment = new VisualizaRotaCorridasFragment();
            return fragment;
        }
        else
        {
            VisualizaGraficoCorridasFragment fragment = new VisualizaGraficoCorridasFragment();
            return fragment;
        }


    }



    @Override
    public CharSequence getPageTitle(int position) {

        if(position == 0)
        {

            return activity.getResources().getString(R.string.geral);
        }
        else
        {
            return activity.getResources().getString(R.string.estatisticas);
        }


    }


    @Override
    public int getCount() {
        return numTabs;
    }
}
