package com.team4runner.forrunner.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.team4runner.forrunner.CadastroTreinoActivity;
import com.team4runner.forrunner.R;
import com.team4runner.forrunner.adapter.AbasPagerAdapterCadastroTreino;
import com.team4runner.forrunner.modelo.Treino;

import java.util.List;

/**
 * Created by Lucas on 14/07/2015.
 * Fragment utilizada para controlar as Tabs das semanas de treino
 */
public class CadastroTreinoEspecificoFragment extends Fragment {

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private AbasPagerAdapterCadastroTreino pagerAdapter;

    //Informa��es obtidas de primeira fragment
    private Treino treino;
    private List<Bundle> bundles;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundles = ((CadastroTreinoActivity)getActivity()).getBundlesDiasDeTreino();
        treino = ((CadastroTreinoActivity)getActivity()).getTreino();
        setRetainInstance(true);
        pagerAdapter = new AbasPagerAdapterCadastroTreino(getChildFragmentManager(), (CadastroTreinoActivity) getActivity(), treino.getQtdSemanas());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_cadastro_treino_especifico, container, false);


        mViewPager = (ViewPager) fragment.findViewById(R.id.viewPager);
        mViewPager.setAdapter(pagerAdapter);
        //define a qtd de abas que ser�o preservadas fora da tela no viewpager
        mViewPager.setOffscreenPageLimit(treino.getQtdSemanas()-1);

        mTabLayout = (TabLayout) fragment.findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


        return fragment;
    }
}
