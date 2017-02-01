package com.team4runner.forrunner.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.team4runner.forrunner.R;
import com.team4runner.forrunner.adapter.ListViewListaTestesCorredorAdapter;
import com.team4runner.forrunner.modelo.Corredor;
import com.team4runner.forrunner.modelo.TesteDeCampo;
import com.team4runner.forrunner.tasks.ListaTestesDeCampoTask;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class TesteDeCampoCorredorFragment extends Fragment {

    private ListView mListView;
    private ListViewListaTestesCorredorAdapter mAdapter;

    private List<TesteDeCampo> mList;

    private Corredor corredor;

    private ProgressBar progressBar;

    private TextView txtSemTestes;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_teste_de_campo_corredor, container, false);

        SharedPreferences prefs = getActivity().getSharedPreferences("Configuracoes", Context.MODE_PRIVATE);
        corredor = new Corredor();
        corredor.setEmail(prefs.getString("email", ""));
        corredor.setEmailTreinador(prefs.getString("emailTreinador", ""));

        progressBar = (ProgressBar) fragment.findViewById(R.id.progress);

        txtSemTestes = (TextView) fragment.findViewById(R.id.txtSemTestes);

        mListView = (ListView) fragment.findViewById(R.id.list_view_testes);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Click na lista
            }
        });


        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();

        BuscaListaTeste();

    }

    public void BuscaListaTeste() {


        ListaTestesDeCampoTask task = new ListaTestesDeCampoTask(getActivity(), corredor, corredor.getEmailTreinador(), false, this, "corredor");
        task.execute();

    }

    public void AtualizaListaTeste(List<TesteDeCampo> testeDeCampos) {
        mList = testeDeCampos;


        mListView.setAdapter(new ListViewListaTestesCorredorAdapter(getActivity(), mList, corredor));
        progressBar.setVisibility(View.GONE);

        if (mList.size() < 1) {
            txtSemTestes.setVisibility(View.VISIBLE);
        } else {
            txtSemTestes.setVisibility(View.GONE);
        }

    }


}
