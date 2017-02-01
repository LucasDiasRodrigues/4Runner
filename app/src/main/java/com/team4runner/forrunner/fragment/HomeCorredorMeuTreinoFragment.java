package com.team4runner.forrunner.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.appodeal.ads.Appodeal;
import com.team4runner.forrunner.R;
import com.team4runner.forrunner.RunActivity;
import com.team4runner.forrunner.adapter.ListViewDiaTreinoAdapter;
import com.team4runner.forrunner.adapter.ListViewListaTestesCorredorAdapter;
import com.team4runner.forrunner.modelo.Corredor;
import com.team4runner.forrunner.modelo.Exercicio;
import com.team4runner.forrunner.modelo.TesteDeCampo;
import com.team4runner.forrunner.modelo.Treino;
import com.team4runner.forrunner.modelo.TreinoExercicio;
import com.team4runner.forrunner.tasks.ConsultaDiaTreinoTask;
import com.team4runner.forrunner.tasks.ListaExerciciosTask;
import com.team4runner.forrunner.tasks.ListaTestesDeCampoTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

/**
 * Created by Lucas on 11/06/2015.
 */
public class HomeCorredorMeuTreinoFragment extends Fragment {

    private ListView mListView;

    private Corredor corredor;

    private String corridaOpcoes = "";

    private LinearLayout layoutAtividades;
    private TextView txtSemAtividades;

    private ProgressBar progressBar;

    private SwipeRefreshLayout mSwipeRefreshLayout;


    List<Exercicio> listaExercicios = new ArrayList<>();

    List<TreinoExercicio> treinoExercicios = new ArrayList<>();

    List<TesteDeCampo> testesDeCampo;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_meutreino_home_corredor, container, false);



        SharedPreferences prefs = getActivity().getSharedPreferences("Configuracoes", Context.MODE_PRIVATE);
        corredor = new Corredor();
        corredor.setEmail(prefs.getString("email", ""));
        corredor.setNome(prefs.getString("nome", ""));
        corredor.setEmailTreinador(prefs.getString("emailTreinador", ""));

        layoutAtividades = (LinearLayout) fragment.findViewById(R.id.layoutAtividades);
        layoutAtividades.setVisibility(View.GONE);
        txtSemAtividades = (TextView) fragment.findViewById(R.id.txtSemAtividades);

        progressBar = (ProgressBar) fragment.findViewById(R.id.progress);


        consultaTreinoDia();

        mListView = (ListView) fragment.findViewById(R.id.list_view);

        FloatingActionButton fabRun = (FloatingActionButton) fragment.findViewById(R.id.fabRun);
        fabRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final Intent intent = new Intent(getActivity(), RunActivity.class);
                if (corridaOpcoes.equals("treino")) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle(R.string.escolhatipocorrida)
                            .setItems(R.array.tipo_corrida_treino, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    switch (which) {
                                        case 0:
                                            intent.putExtra("tipo", "treino");
                                            ArrayList atividadesTreino = new ArrayList(treinoExercicios);
                                            Bundle bundleTreino = new Bundle();
                                            bundleTreino.putSerializable("atividadesTreino", atividadesTreino);
                                            intent.putExtra("bundleTreino", bundleTreino);
                                            startActivity(intent);

                                            break;
                                        default:
                                            intent.putExtra("tipo", "livre");
                                            startActivity(intent);

                                            break;
                                    }

                                }
                            }).show();

                } else if (corridaOpcoes.equals("teste")) {
                    intent.putExtra("tipo", "teste");
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle(R.string.escolhatipocorrida)
                            .setItems(R.array.tipo_corrida_teste, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case 0:
                                            intent.putExtra("tipo", "teste");
                                            ArrayList atividadesTeste = new ArrayList(testesDeCampo);
                                            Bundle bundleTeste = new Bundle();
                                            bundleTeste.putSerializable("atividadesTeste", atividadesTeste);
                                            bundleTeste.putSerializable("corredor", corredor);
                                            intent.putExtra("bundleTeste", bundleTeste);
                                            startActivity(intent);


                                            break;
                                        default:
                                            intent.putExtra("tipo", "livre");
                                            startActivity(intent);
                                            break;
                                    }
                                }
                            }).show();
                } else {
                    intent.putExtra("tipo", "livre");
                    startActivity(intent);
                }

            }
        });

        mSwipeRefreshLayout = (SwipeRefreshLayout) fragment.findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.accent, R.color.primary, R.color.blue);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i("AtualizandoCards", "onRefresh called from SwipeRefreshLayout");
                mListView.setAdapter(null);
                consultaTreinoDia();
            }
        });

        /*/Anunciozinho maroto AdMobs
        AdView mAdView = (AdView) fragment.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("14C364ED1292A3EA3A72FD44B0EBBC24").build();
        mAdView.loadAd(adRequest);
*/



        return fragment;
    }


    @Override
    public void onResume() {
        super.onResume();


    }

    public void consultaTestedeCampoDia() {

        ListaTestesDeCampoTask task = new ListaTestesDeCampoTask(getActivity(), corredor, corredor.getEmailTreinador(), true, this, "corredor");
        task.execute();

    }

    public void listaTesteDia(List<TesteDeCampo> testes) {
        testesDeCampo = testes;

        if (testesDeCampo.size() <= 0) {
            corridaOpcoes = "";
            txtSemAtividades.setVisibility(View.VISIBLE);
            layoutAtividades.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
        } else {
            corridaOpcoes = "teste";
            layoutAtividades.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }

        mSwipeRefreshLayout.setRefreshing(false);

        ListViewListaTestesCorredorAdapter adapter = new ListViewListaTestesCorredorAdapter(getActivity(), testesDeCampo, corredor, true);
        mListView.setAdapter(adapter);
    }


    public void consultaTreinoDia() {

        if (treinoExercicios != null && treinoExercicios.size() > 0) {
            treinoExercicios.clear();
        }
        if (testesDeCampo != null && testesDeCampo.size() > 0) {
            testesDeCampo.clear();
        }

        ConsultaDiaTreinoTask task = new ConsultaDiaTreinoTask(getActivity(), corredor, this);
        task.execute();
    }

    public void listaTreinoDia(List<TreinoExercicio> treinoExercic) {
        List<TreinoExercicio> auxTreinoExercicios = treinoExercic;

        for (TreinoExercicio unidadde : auxTreinoExercicios) {

            if (unidadde.getCodCorrida() <= 0) {
                treinoExercicios.add(unidadde);
            }
        }

        if (treinoExercicios.size() <= 0) {

            consultaTestedeCampoDia();

        } else {
            corridaOpcoes = "treino";
            layoutAtividades.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            ListaExercicios();
            mSwipeRefreshLayout.setRefreshing(false);
        }

        ListViewDiaTreinoAdapter adapter = new ListViewDiaTreinoAdapter(getActivity(), treinoExercicios);
        mListView.setAdapter(adapter);

    }


    public void ListaExercicios() {

        ListaExerciciosTask task = new ListaExerciciosTask(getActivity(), this);
        task.execute();
    }

    public void SelecionaExercicios(List<Exercicio> auxExercicios) {
        listaExercicios = auxExercicios;

        for (int i = 0; i < treinoExercicios.size(); i++) {

            for (int j = 0; j < listaExercicios.size(); j++) {
                Log.i("Master123", treinoExercicios.get(i).getExercicio().getCodExercicio() + "");
                if (treinoExercicios.get(i).getExercicio().getCodExercicio() == listaExercicios.get(j).getCodExercicio()) {
                    TreinoExercicio aux = treinoExercicios.get(i);
                    aux.setExercicio(listaExercicios.get(j));
                    treinoExercicios.remove(i);
                    treinoExercicios.add(i, aux);
                }
            }
        }

        ListViewDiaTreinoAdapter adapter = new ListViewDiaTreinoAdapter(getActivity(), treinoExercicios);
        mListView.setAdapter(adapter);

    }



}
