package com.team4runner.forrunner.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.team4runner.forrunner.R;
import com.team4runner.forrunner.modelo.Corredor;
import com.team4runner.forrunner.modelo.Treino;
import com.team4runner.forrunner.tasks.EstatisticasTask;
import com.team4runner.forrunner.tasks.ListaTreinosTask;

import java.text.NumberFormat;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Lucas on 14/06/2015.
 */
public class EstatisticasCorredorFragment extends Fragment {

    Corredor corredor;

    Toolbar toolbarSpinner;
    Spinner spinnerPeriodo;
    Spinner spinnerTreino;

    TextView txtDistancia;
    TextView txtTempo;
    TextView txtVelMax;
    TextView txtCalorias;

    List<Treino> treinos;

    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_estatisticas_corredor, container, false);


        corredor = new Corredor();
        SharedPreferences prefs = getActivity().getSharedPreferences("Configuracoes", Context.MODE_PRIVATE);
        corredor.setEmail(prefs.getString("email", ""));

        progressBar = (ProgressBar) fragment.findViewById(R.id.progress);

        //spinner por treino
        spinnerTreino = new Spinner(getActivity());
        spinnerTreino.setVisibility(View.GONE);
        spinnerTreino.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                estatisticasPorTreino(treinos.get(position));
                Log.i("treinoteste", treinos.get(position).getNome() + "aaa");
                Log.i("treinoteste", treinos.get(position).getCodTreino() + "aaa");

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //Obtem a lista de treinos
        listarTreinos();

        //adapters para o spinner principal
        ArrayAdapter<String> spinnerPeriodoAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, getResources().getStringArray(R.array.spinner_estatisticas));

        //spinner principal
        spinnerPeriodo = new Spinner(getActivity());
        spinnerPeriodo.setAdapter(spinnerPeriodoAdapter);
        spinnerPeriodo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        geralSelected();
                        break;
                    case 1:
                        porTreinoSelected();
                        break;
                    case 2:
                        corridasLivresSelected();
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //toolbar das spinners
        toolbarSpinner = (Toolbar) fragment.findViewById(R.id.toolbarSpinner);
        toolbarSpinner.addView(spinnerPeriodo);
        toolbarSpinner.addView(spinnerTreino);


        //TextViews
        txtDistancia = (TextView) fragment.findViewById(R.id.txtDistancia);
        txtTempo = (TextView) fragment.findViewById(R.id.txtTempo);
        txtVelMax = (TextView) fragment.findViewById(R.id.txtVelMax);
        txtCalorias = (TextView) fragment.findViewById(R.id.txtCalorias);

        //inicia a tela default
        geralSelected();
        return fragment;
    }

    public void listarTreinos() {

        ListaTreinosTask treinosTask = new ListaTreinosTask(getActivity(), corredor, "EstatisticasCorredorFragment", this, "corredor");
        treinosTask.execute();
        progressBar.setVisibility(View.VISIBLE);
    }

    public void preencherSpinnerTreinos(List<Treino> treinos) {
        this.treinos = treinos;

        String nometreinos[] = new String[treinos.size()];

        int count = 0;
        for (Treino treino : treinos) {
            nometreinos[count] = treino.getNome();
            count++;
        }



        //
        ArrayAdapter<String> spinnerTreinoAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, nometreinos);
        spinnerTreino.setAdapter(spinnerTreinoAdapter);
        progressBar.setVisibility(View.GONE);
    }

    public void geralSelected() {
        limparContadores();
        spinnerTreino.setVisibility(View.GONE);
        estatisticasGerais();
    }

    public void porTreinoSelected() {
        limparContadores();
        spinnerTreino.setVisibility(View.VISIBLE);
        int auxItemTreino = spinnerTreino.getSelectedItemPosition();
        if(auxItemTreino>=0 && treinos !=null && treinos.size()>=1){

            estatisticasPorTreino(treinos.get(auxItemTreino));
            Log.i("treinoteste", treinos.get(auxItemTreino).getNome() + "aaa");
            Log.i("treinoteste", treinos.get(auxItemTreino).getCodTreino() + "aaa");
        }

    }

    public void corridasLivresSelected() {
        limparContadores();
        spinnerTreino.setVisibility(View.GONE);
        estatisticasPorCorridasLivres();
    }


    public void estatisticasGerais() {
        EstatisticasTask geralTask = new EstatisticasTask(getActivity(), "geral", corredor, this);
        geralTask.execute();
        progressBar.setVisibility(View.VISIBLE);
    }

    public void preencheEtatisticasGerais(Bundle bundle) {

        NumberFormat format = NumberFormat.getInstance();
        format.setMaximumFractionDigits(2);
        format.setMinimumFractionDigits(2);

        String auxDistancia = String.valueOf(format.format(bundle.getDouble("distancia"))) + " "+ getString(R.string.metros);
        txtDistancia.setText(auxDistancia);
        txtTempo.setText(bundle.getString("duracao"));
        String auxVelMax = String.valueOf(format.format(bundle.getDouble("velocidadeMax") * 3.6)) + " " + "Km/h";
        txtVelMax.setText(auxVelMax);
        txtCalorias.setText(String.valueOf(format.format(bundle.getDouble("calorias"))));

        progressBar.setVisibility(View.GONE);

    }

    public void estatisticasPorTreino(Treino treino) {
        EstatisticasTask porTreinoTask = new EstatisticasTask(getActivity(), "porTreino", treino, this);
        porTreinoTask.execute();
        progressBar.setVisibility(View.VISIBLE);

    }

    public void preencheEstatisticasPorTreino(Bundle bundle) {

        NumberFormat format = NumberFormat.getInstance();
        format.setMaximumFractionDigits(2);
        format.setMinimumFractionDigits(2);

        String auxDistancia = String.valueOf(format.format(bundle.getDouble("distancia"))) + " " + getString(R.string.metros);
        txtDistancia.setText(auxDistancia);
        txtTempo.setText(bundle.getString("duracao"));
        String auxVelMax = String.valueOf(format.format(bundle.getDouble("velocidadeMax") * 3.6)) + " " + "Km/h";
        txtVelMax.setText(auxVelMax);
        txtCalorias.setText(String.valueOf(format.format(bundle.getDouble("calorias"))));

        progressBar.setVisibility(View.GONE);
    }

    public void estatisticasPorCorridasLivres() {
        EstatisticasTask porCorridasTask = new EstatisticasTask(getActivity(), "corridasLivres", corredor, this);
        porCorridasTask.execute();
        progressBar.setVisibility(View.VISIBLE);
    }

    public void preencheEstatisticasPorCorridasLivres(Bundle bundle) {

        NumberFormat format = NumberFormat.getInstance();
        format.setMaximumFractionDigits(2);
        format.setMinimumFractionDigits(2);

        String auxDistancia = String.valueOf(format.format(bundle.getDouble("distancia"))) + " " + getString(R.string.metros);
        txtDistancia.setText(auxDistancia);
        txtTempo.setText(bundle.getString("duracao"));
        String auxVelMax = String.valueOf(format.format(bundle.getDouble("velocidadeMax") * 3.6)) + " " + "Km/h";
        txtVelMax.setText(auxVelMax);
        txtCalorias.setText(String.valueOf(format.format(bundle.getDouble("calorias"))));

        progressBar.setVisibility(View.GONE);
    }

    public void limparContadores(){

        String auxDistancia = "0" + " " + getResources().getString(R.string.metros);
        txtDistancia.setText(auxDistancia);
        txtTempo.setText(String.valueOf("00:00:00"));
        String auxVelMax = String.valueOf("0") + " " + "Km/h";
        txtVelMax.setText(auxVelMax);
        txtCalorias.setText(String.valueOf(0));

        progressBar.setVisibility(View.GONE);

    }

}
