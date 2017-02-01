package com.team4runner.forrunner;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.team4runner.forrunner.modelo.Objetivo;
import com.team4runner.forrunner.modelo.Treino;
import com.team4runner.forrunner.tasks.ListaObjetivosTask;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import lecho.lib.hellocharts.listener.PieChartOnValueSelectListener;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.PieChartView;

public class DetalheTreinoActivity extends AppCompatActivity {

    private Treino treino;
    private String objTreino = "";
    private String perfil;
    private Boolean treinoConcluido;
    private CoordinatorLayout coordinatorLayout;
    private LinearLayout layoutEmailTreinador;
    private PieChartView chart;
    private PieChartData data;

    TextView txtObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe_treino);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Detalhes do treino");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        SharedPreferences prefs = getSharedPreferences("Configuracoes", Context.MODE_PRIVATE);
        perfil = prefs.getString("perfil", "");

        treino = (Treino) getIntent().getSerializableExtra("treino");

        //Verifica se treino esta concluido
        if(treino.getSituacao().equals("concluido")){
            treinoConcluido = true;
        } else{
            //Remove Hora da data para calculo
            DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            Date data = new Date();
            Date dataHoje = new Date();
            String dataHojeS = format.format(data);
            try {dataHoje = format.parse(dataHojeS);} catch (ParseException e){e.printStackTrace();}
            //

            if(treino.getDtFim().before(dataHoje) && treino.getSituacao().equals("ativo")){
                treinoConcluido = true;
            } else {
                treinoConcluido = false;
            }
        }


        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        layoutEmailTreinador = (LinearLayout) findViewById(R.id.layoutTreinador);

        TextView txtNomeTreino = (TextView) findViewById(R.id.nomeTreino);
        txtNomeTreino.setText(treino.getNome());

        txtObj = (TextView) findViewById(R.id.objTreino);

        selecionaObj();


        TextView txtDuracao = (TextView) findViewById(R.id.duracaoSemanas);

        if(treino.getQtdSemanas() > 1) {
            txtDuracao.setText(treino.getQtdSemanas() + " " + getResources().getString(R.string.semanas));
        } else{
            txtDuracao.setText(treino.getQtdSemanas() + " " + getResources().getString(R.string.semana));
        }
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        TextView txtDataInicio = (TextView) findViewById(R.id.dataInicio);
        txtDataInicio.setText(format.format(treino.getDtInicio()));

        TextView txtDataTermino = (TextView) findViewById(R.id.dataTermino);
        txtDataTermino.setText(format.format(treino.getDtFim()));

        TextView txtObs = (TextView) findViewById(R.id.observacoes);
        txtObs.setText(treino.getObservacao());

        TextView txtTreinador = (TextView) findViewById(R.id.emailTreinador);
        txtTreinador.setText(treino.getEmailTreinador());


        //Assiduidade
        TextView txtPercentualAssiduidade = (TextView) findViewById(R.id.percentualAssiduidade);
        chart = (PieChartView) findViewById(R.id.chart);
        chart.setOnValueTouchListener(new ValueTouchListener());
        Log.i("ExibeAssiduidade", treino.getDiasTotais() + "");
        if (treino.getDiasTotais() != 0) {
            txtPercentualAssiduidade.setText(String.valueOf((treino.getDiasRealizados() * 100) / treino.getDiasTotais() + "%"));

            List<SliceValue> values = new ArrayList<SliceValue>();
            SliceValue sliceValue = new SliceValue((treino.getDiasRealizados() * 100) / treino.getDiasTotais(), ChartUtils.COLOR_GREEN);
            values.add(sliceValue);
            Float auxPerc = (float) 100 - ((treino.getDiasRealizados() * 100) / treino.getDiasTotais());
            SliceValue sliceValue2 = new SliceValue(auxPerc, ChartUtils.DEFAULT_DARKEN_COLOR);
            values.add(sliceValue2);

            data = new PieChartData(values);
            data.setHasLabels(false);
            data.setHasLabelsOnlyForSelected(false);
            data.setHasLabelsOutside(false);
            data.setHasCenterCircle(false);
            chart.setPieChartData(data);
        }

        //Avaliacao do corredor
        final EditText txtAvaliacaoCorredor = (EditText) findViewById(R.id.txtAvaliacaoCorredor);
        txtAvaliacaoCorredor.setText(treino.getAvaliacaoDoCorredor());
        Button btnAvaliacaoCorredor = (Button) findViewById(R.id.btnAvaliacaoCorredor);

        if (treino.getAvaliacaoDoCorredor().equals("")) {
            btnAvaliacaoCorredor.setText(R.string.avaliartreino);
        } else {
            btnAvaliacaoCorredor.setText(R.string.alteraravaliacao);
        }

        btnAvaliacaoCorredor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Avaliar
                treino.setAvaliacaoDoCorredor(txtAvaliacaoCorredor.getText().toString());
                treino.avaliarTreinoCorredor(DetalheTreinoActivity.this);


            }
        });

        //Nota do treinador
        Button btnNotaTreinador = (Button) findViewById(R.id.btnNotaTreinador);
        final TextView txtNotaTreinador = (TextView) findViewById(R.id.txtNotaTreinador);
        SeekBar skNota = (SeekBar) findViewById(R.id.seekBarNota);
        skNota.setMax(10);
        skNota.setProgress(treino.getNota());
        skNota.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                txtNotaTreinador.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        if (treino.getNota() <= 0) {
            btnNotaTreinador.setText(R.string.atribuirnota);
        } else {
            btnNotaTreinador.setText(R.string.alterarnota);
        }
        txtNotaTreinador.setText(String.valueOf(treino.getNota()));

        btnNotaTreinador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Atribuir/AlterarNota
                treino.setNota(Integer.parseInt(txtNotaTreinador.getText().toString()));
                treino.atribuirNotaTreinador(DetalheTreinoActivity.this);
            }
        });


        //Controle de itens que aparecerao na tela
        if (perfil.equals("corredor")) {
            btnNotaTreinador.setVisibility(View.GONE);
            skNota.setVisibility(View.GONE);
            layoutEmailTreinador.setVisibility(View.VISIBLE);

        } else if (perfil.equals("treinador")) {
            btnAvaliacaoCorredor.setVisibility(View.GONE);
            txtAvaliacaoCorredor.setKeyListener(null);
            layoutEmailTreinador.setVisibility(View.GONE);
        }

        if (!treinoConcluido) {
            LinearLayout layoutAvaliacoes = (LinearLayout) findViewById(R.id.layoutAvaliacoes);
            layoutAvaliacoes.setVisibility(View.GONE);

            LinearLayout layoutAvaliacoes2 = (LinearLayout) findViewById(R.id.layoutAvaliacoes2);
            layoutAvaliacoes2.setVisibility(View.GONE);
        }


    }


    public void selecionaObj() {

        ListaObjetivosTask task = new ListaObjetivosTask(this);
        task.execute();

        try {
            List<Objetivo> objs = (List) task.get();

            for (Objetivo objetivo : objs) {
                Log.i("vinculaObj", "for");
                if (objetivo.getCodObj() == treino.getObjetivo()) {
                    Log.i("vinculaObj", objetivo.getDescricao());
                    objTreino = objetivo.getDescricao();
                    txtObj.setText(objTreino + "K");
                }
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


    }

    //Toque no grafico
    private class ValueTouchListener implements PieChartOnValueSelectListener {

        @Override
        public void onValueSelected(int arcIndex, SliceValue value) {
            if (arcIndex == 0) {
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, (int) value.getValue() + "% " + getResources().getString(R.string.concluido), Snackbar.LENGTH_SHORT);
                snackbar.show();
            } else if (arcIndex == 1) {
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, (int) value.getValue() + "% " + getResources().getString(R.string.pendente), Snackbar.LENGTH_SHORT);
                snackbar.show();


            }

        }

        @Override
        public void onValueDeselected() {
            // TODO Auto-generated method stub

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
