package com.team4runner.forrunner.Postergado_NaoUsado;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.team4runner.forrunner.R;
import com.team4runner.forrunner.modelo.Corredor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lucas on 15/06/2015.
 */
public class AmigosCorredorListaAmigosFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerViewAdapterAmigosAmigosCorredor mAdapter;
    List<Corredor> mList = new ArrayList<Corredor>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_listaamigos_amigos_corredor,container,false);

        //Area de teste
        Corredor corredor1 = new Corredor();
        corredor1.setNome("Lucas");
        Corredor corredor2 = new Corredor();
        corredor2.setNome("Monica");
        Corredor corredor3 = new Corredor();
        corredor3.setNome("Marcelo");
        Corredor corredor4 = new Corredor();
        corredor4.setNome("Dilminha");
        Corredor corredor5 = new Corredor();
        corredor5.setNome("Evandrex");
        Corredor corredor6 = new Corredor();
        corredor6.setNome("Tio Lolo");
        Corredor corredor7 = new Corredor();
        corredor7.setNome("Silvinha");
        Corredor corredor8 = new Corredor();
        corredor8.setNome("Miro,, que Miro?");
        Corredor corredor9 = new Corredor();
        corredor9.setNome("Nazista");
        Corredor corredor10 = new Corredor();
        corredor10.setNome("Primeiramente... Boa noite!");
        mList.add(corredor1);
        mList.add(corredor2);
        mList.add(corredor3);
        mList.add(corredor4);
        mList.add(corredor5);
        mList.add(corredor6);
        mList.add(corredor7);
        mList.add(corredor8);
        mList.add(corredor9);
        mList.add(corredor10);
        //Fim do teste



        mRecyclerView = (RecyclerView)fragment.findViewById(R.id.recycler_view_amigos);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RecyclerViewAdapterAmigosAmigosCorredor(getActivity(),mList);
        mRecyclerView.setAdapter(mAdapter);


        return fragment;
    }
}

