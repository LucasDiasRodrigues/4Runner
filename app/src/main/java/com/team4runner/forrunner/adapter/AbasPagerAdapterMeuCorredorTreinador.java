package com.team4runner.forrunner.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.team4runner.forrunner.R;
import com.team4runner.forrunner.fragment.MeusCorredoresTreinadorPerfilFragment;
import com.team4runner.forrunner.fragment.MeusCorredoresTreinadorTestesFragment;
import com.team4runner.forrunner.fragment.MeusCorredoresTreinadorTreinosFragment;

/**
 * Created by Lucas on 05/07/2015.
 */
public class AbasPagerAdapterMeuCorredorTreinador extends FragmentStatePagerAdapter {

    private Context context;
    private FragmentManager fragmentManager;
    private int numTabs;


    public AbasPagerAdapterMeuCorredorTreinador(Context context, FragmentManager fm, int numTabs) {
        super(fm);
        this.context = context;
        this.fragmentManager = fm;
        this.numTabs = numTabs;
    }

    @Override
    public Fragment getItem(int position) {

        if(position == 0)
        {
            MeusCorredoresTreinadorPerfilFragment fragment = new MeusCorredoresTreinadorPerfilFragment();
            return fragment;
        }
        else if(position == 1)
        {
            MeusCorredoresTreinadorTreinosFragment fragment = new MeusCorredoresTreinadorTreinosFragment();
            return fragment;
        }
        else
        {
            MeusCorredoresTreinadorTestesFragment fragment = new MeusCorredoresTreinadorTestesFragment();
            return fragment;
        }



    }


    @Override
    public CharSequence getPageTitle(int position) {

        if(position == 0)
        {

            return context.getResources().getString(R.string.perfil);
        }
        else if(position == 1)
        {
            return context.getResources().getString(R.string.treinos);
        }
        else
        {
            return context.getResources().getString(R.string.testesdecampo);
        }



    }

    @Override
    public int getCount() {
        return numTabs;
    }
}
