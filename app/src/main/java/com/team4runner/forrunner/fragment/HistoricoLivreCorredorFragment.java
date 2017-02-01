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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.team4runner.forrunner.R;
import com.team4runner.forrunner.VisualizarCorridaActivity;
import com.team4runner.forrunner.adapter.ListViewListaCorridasAdapter;
import com.team4runner.forrunner.modelo.Corredor;
import com.team4runner.forrunner.modelo.Corrida;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lucas on 23/10/2015.
 */
public class HistoricoLivreCorredorFragment extends Fragment {

    private List<Corrida> mListCorridas;
    private ListView mListView;

    private Corredor corredor;
    private ProgressBar progressBar;

    private TextView txtSemCorridas;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_historico_livre_corredor, container, false);

        SharedPreferences prefs = getActivity().getSharedPreferences("Configuracoes", Context.MODE_PRIVATE);
        corredor = new Corredor();
        corredor.setEmail(prefs.getString("email", ""));
        corredor.setNome(prefs.getString("nome",""));

        txtSemCorridas = (TextView) fragment.findViewById(R.id.txtSemCorridas);

        mListCorridas = new ArrayList<>();

        mListView = (ListView)fragment.findViewById(R.id.list_view_historico);
        progressBar = (ProgressBar)fragment.findViewById(R.id.progress);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Corrida corrida = mListCorridas.get(position);
                Intent intent = new Intent(getActivity(), VisualizarCorridaActivity.class);
                intent.putExtra("livre", true);
                intent.putExtra("corrida",corrida);
                intent.putExtra("corredor", corredor);
                getActivity().startActivity(intent);
            }
        });


        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        buscaCorridasLivres();
    }

    public void buscaCorridasLivres(){
        corredor.buscaCorridasLivres(getActivity(), this);
    }

    public void atualizaListaCorridas(List<Corrida> mListCorridas){
       this.mListCorridas = mListCorridas;
        ListViewListaCorridasAdapter adapter = new ListViewListaCorridasAdapter(getActivity(), mListCorridas);
        mListView.setAdapter(adapter);
        progressBar.setVisibility(View.GONE);

        if(mListCorridas.size() < 1){
            txtSemCorridas.setVisibility(View.VISIBLE);
        } else {
            txtSemCorridas.setVisibility(View.GONE);
        }

    }




}
