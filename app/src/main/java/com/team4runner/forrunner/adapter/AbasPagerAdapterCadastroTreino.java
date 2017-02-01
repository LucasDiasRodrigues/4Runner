package com.team4runner.forrunner.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.team4runner.forrunner.CadastroTreinoActivity;
import com.team4runner.forrunner.fragment.CadastroTreinoEspecificoDetalhadoFragment;

/**
 * Created by Lucas on 15/07/2015.
 */
public class AbasPagerAdapterCadastroTreino extends FragmentStatePagerAdapter {

    int numTabs;
    CadastroTreinoActivity activity;
    String semana = "Semana ";

    FragmentManager fm;

    public AbasPagerAdapterCadastroTreino(FragmentManager fm, CadastroTreinoActivity activity, int numTabs) {
        super(fm);
        this.numTabs = numTabs;
        this.activity = activity;
        this.fm = fm;
    }


    @Override
    public CharSequence getPageTitle(int position) {

        return semana + (position + 1);
    }

    @Override
    public Fragment getItem(int position) {

        CadastroTreinoEspecificoDetalhadoFragment fragment = new CadastroTreinoEspecificoDetalhadoFragment();


        Bundle bundle = new Bundle();
        bundle.putInt("semana", position + 1);

        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public int getCount() {
        return numTabs;
    }


}
