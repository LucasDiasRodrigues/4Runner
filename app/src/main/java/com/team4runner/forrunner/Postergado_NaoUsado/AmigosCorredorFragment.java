package com.team4runner.forrunner.Postergado_NaoUsado;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.team4runner.forrunner.MainCorredorActivity;
import com.team4runner.forrunner.R;
import com.team4runner.forrunner.Postergado_NaoUsado.AbasPagerAdapterAmigosCorredor;

/**
 * Created by Lucas on 14/06/2015.
 */
public class AmigosCorredorFragment extends Fragment {

    private ViewPager mViewPager;
    private int numTabs = 3;
    private TabLayout mTabLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        View fragmentAmigos = inflater.inflate(R.layout.fragment_amigos_corredor,container,false);

        AbasPagerAdapterAmigosCorredor pagerAdapter = new AbasPagerAdapterAmigosCorredor(getChildFragmentManager(),(MainCorredorActivity)getActivity(),numTabs);
        mViewPager = (ViewPager)fragmentAmigos.findViewById(R.id.pager);
        mViewPager.setAdapter(pagerAdapter);

        mTabLayout = (TabLayout)fragmentAmigos.findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        return fragmentAmigos;
    }
}
