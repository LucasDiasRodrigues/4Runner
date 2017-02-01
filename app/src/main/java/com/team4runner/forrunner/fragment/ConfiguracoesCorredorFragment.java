package com.team4runner.forrunner.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

//import com.team4runner.forrunner.ObterLocalizacaoCorredorActivity;
import com.team4runner.forrunner.ConfiguracaoContaActivity;
import com.team4runner.forrunner.ObterLocalizacaoCorredorActivity;
import com.team4runner.forrunner.PerfilCorredorActivity;
import com.team4runner.forrunner.R;
import com.team4runner.forrunner.adapter.ListViewConfiguracoesAdapter;
import com.team4runner.forrunner.modelo.Corredor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lucas on 14/06/2015.
 */
public class ConfiguracoesCorredorFragment extends Fragment {
    Corredor corredor;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_configuracoes_corredor,container,false);

        SharedPreferences prefs = getActivity().getSharedPreferences("Configuracoes", Context.MODE_PRIVATE);
        corredor = new Corredor();
        corredor.setEmail(prefs.getString("email", ""));

        final List<String> mList = new ArrayList<>();
        mList.add(getString(R.string.perfil));
        mList.add("Localizacao");
        mList.add(getString(R.string.conta));


        final ListView listView = (ListView)fragment.findViewById(R.id.listView);
        ListViewConfiguracoesAdapter adapter = new ListViewConfiguracoesAdapter(getActivity(),mList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selecionado = (String) listView.getItemAtPosition(position);
                switch (selecionado) {
                    case "Perfil":
                        Intent intent1 = new Intent(getActivity(), PerfilCorredorActivity.class);
                        startActivity(intent1);
                        break;
                    case "Localizacao":
                        Intent intent2 = new Intent(getActivity(), ObterLocalizacaoCorredorActivity.class);
                        startActivity(intent2);
                        break;
                    case "Conta":
                        Intent intent3 = new Intent(getActivity(), ConfiguracaoContaActivity.class);
                        startActivity(intent3);
                        break;
                }

            }
        });


        return fragment;
    }
}
