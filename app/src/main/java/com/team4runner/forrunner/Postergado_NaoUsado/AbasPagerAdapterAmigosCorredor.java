package com.team4runner.forrunner.Postergado_NaoUsado;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.team4runner.forrunner.MainCorredorActivity;
import com.team4runner.forrunner.R;

/**
 * Created by Lucas on 15/06/2015.
 */
public class AbasPagerAdapterAmigosCorredor extends FragmentStatePagerAdapter {

    MainCorredorActivity activity;
    int numTabs;

    public AbasPagerAdapterAmigosCorredor(FragmentManager fm, MainCorredorActivity activity, int numOfTabs) {
        super(fm);

        this.activity = activity;
        this.numTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0){

            AmigosCorredorNovidadesFragment fragment = new AmigosCorredorNovidadesFragment();
            return fragment;


        }else if(position == 1){

            AmigosCorredorListaAmigosFragment fragment = new AmigosCorredorListaAmigosFragment();
            return  fragment;

        }else{

            AmigosCorredorRankingsFragment fragment = new AmigosCorredorRankingsFragment();
            return fragment;
        }

    }


    @Override
    public CharSequence getPageTitle(int position) {

        if(position == 0 ){
            return activity.getResources().getString(R.string.novidades);
        } else if(position == 1){
            return activity.getResources().getString(R.string.amigos);
        } else{
            return activity.getResources().getString(R.string.rankings);
        }
    }


    @Override
    public int getCount() {
        return numTabs;
    }
}
