package com.team4runner.forrunner;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.johnhiott.darkskyandroidlib.ForecastApi;
import com.johnhiott.darkskyandroidlib.RequestBuilder;
import com.johnhiott.darkskyandroidlib.models.Request;
import com.johnhiott.darkskyandroidlib.models.WeatherResponse;
import com.team4runner.forrunner.modelo.Corredor;
import com.team4runner.forrunner.modelo.Corrida;
import com.team4runner.forrunner.modelo.PontoRota;
import com.team4runner.forrunner.modelo.Treino;
import com.team4runner.forrunner.tasks.AvaliarTreinoCorredorTask;
import com.team4runner.forrunner.tasks.ConsultaCorredorAtingiuObjTask;
import com.team4runner.forrunner.tasks.SalvaCorridaTask;
import com.team4runner.forrunner.tasks.VerificaUltimaCorridaTreinoTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class AfterRunActivity extends AppCompatActivity {

    //Tela Anterior
    private ArrayList<LatLng> pontos;
    private float[] speedPontos;
    private float[] distanciaPontos;
    private float[] tempoPontos;
    private ArrayList<Date> dataHora;
    private Date dtHoraInicio;
    private Date dtHoraFim;
    private float velocidadeMedia;
    private float velocidadeMaxima;
    private float ritmoMedio;
    private double mCaloriaTotal = 0;
    private String emailCorredor;
    private float distanciaTotal;
    private String tipoCorrida;

    private Corredor corredor;
    private Treino treino;

    private boolean fimTreino = false;


    RadioGroup radioGroupTerreno;
    String terreno;
    RadioGroup radioGroupClima;
    String clima;
    SeekBar seekBarPSE;
    TextView txtTemperatura;
    TextView valorSkPse;
    EditText editTextObs;



    Corrida corrida;
    ArrayList<PontoRota> rota;
    int intTemperatura;

    CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_run);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.salvarCorrida);
        setSupportActionBar(toolbar);

        ForecastApi.create("71f272c7342473c0df2c013de37fca3a");

        Intent intent = getIntent();
        Bundle bundleCorrida = intent.getExtras();
        pontos = bundleCorrida.getParcelableArrayList("Pontos");
        speedPontos = bundleCorrida.getFloatArray("Speed");
        distanciaPontos = bundleCorrida.getFloatArray("distancia");
        tempoPontos = bundleCorrida.getFloatArray("tempo");
        distanciaTotal = bundleCorrida.getFloat("distanciaTotal");
        dataHora = (ArrayList<Date>) bundleCorrida.getSerializable("dataHora");
        dtHoraInicio = (Date) bundleCorrida.getSerializable("dtHoraInicio");
        dtHoraFim = (Date) bundleCorrida.getSerializable("dtHoraFim");
        velocidadeMaxima = bundleCorrida.getFloat("velocidadeMaxima");
        velocidadeMedia = bundleCorrida.getFloat("velocidadeMedia");
        ritmoMedio = bundleCorrida.getFloat("ritmoMedio");
        mCaloriaTotal = bundleCorrida.getDouble("Calorias");
        emailCorredor = bundleCorrida.getString("emailCorredor");
        tipoCorrida = bundleCorrida.getString("tipo");

        SharedPreferences prefs = getSharedPreferences("Configuracoes", Context.MODE_PRIVATE);
        corredor = new Corredor();
        corredor.setEmail(bundleCorrida.getString("emailCorredor"));
        corredor.setEmailTreinador(prefs.getString("emailTreinador", ""));
        corredor.setNome(prefs.getString("nome", ""));
        corredor.setObjetivo(Integer.valueOf(prefs.getString("objetivo", "")));
        corredor.setSituacao(String.valueOf(distanciaTotal));


        //Teste
        Log.i("Result = ", String.valueOf(velocidadeMaxima));
        Log.i("Result = ", String.valueOf(ritmoMedio));
        Log.i("Result = ", String.valueOf(mCaloriaTotal));
        Log.i("Result = ", String.valueOf(velocidadeMedia));
        Log.i("Result = ", dtHoraInicio.toString());
        Log.i("Result = ", dtHoraFim.toString());

        //tratar erro de calculo do ritmo medio se a velocidade media for = 0
        if(velocidadeMedia < 0.1){
            ritmoMedio = 0;
        }


        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        radioGroupTerreno = (RadioGroup) findViewById(R.id.rgTerreno);
        radioGroupClima = (RadioGroup) findViewById(R.id.rgClima);
        seekBarPSE = (SeekBar) findViewById(R.id.skbPSE);
        txtTemperatura = (TextView) findViewById(R.id.txtTemperatura);
        valorSkPse = (TextView) findViewById(R.id.txtPSE);
        editTextObs = (EditText) findViewById(R.id.editTextObs);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabOk);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int idRadioSelecionado = radioGroupClima.getCheckedRadioButtonId();
                RadioButton rbClima = (RadioButton) findViewById(idRadioSelecionado);
                clima = rbClima.getText().toString();

                idRadioSelecionado = radioGroupTerreno.getCheckedRadioButtonId();
                RadioButton rbTerreno = (RadioButton) findViewById(idRadioSelecionado);
                terreno = rbTerreno.getText().toString();

                criaRota();

                salvaCorrida();

                SalvaCorridaTask sCT = new SalvaCorridaTask(AfterRunActivity.this, corrida);
                sCT.execute();

                try {
                    String resposta = (String) sCT.get();

                    if (resposta.equals("Sucesso")) {
                        Snackbar.make(view, "Sucesso", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();

                        new AlertDialog.Builder(AfterRunActivity.this).setTitle("Nova corrida")
                                .setMessage("Corrida salva!")
                                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        //Verifica se foi a ultima corida do treino
                                        if (tipoCorrida.equals("treino")) {
                                            verificaFimTreino();
                                        }

                                        //Verifica se atingiu o Obj
                                        verificaAtingiuObj();

                                    }
                                }).show();

                    } else {
                        Snackbar.make(view, "Ops... Algo deu errado.", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        Snackbar.make(view, "Tente novamente", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }


                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }


            }
        });

        configuraSeekBarPSE();

        seekBarPSE.setProgress(5);

    }

    @Override
    protected void onResume() {
        super.onResume();
        GetTemperaturaForecastAPI();

    }


    private void configuraSeekBarPSE() {
        seekBarPSE.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                valorSkPse.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void salvaCorrida() {

        corrida = new Corrida();
        corrida.setDtHoraInicio(dtHoraInicio);
        corrida.setDtHoraFim(dtHoraFim);
        corrida.setVelocidadeMedia(velocidadeMedia);
        corrida.setVelocidadeMax(velocidadeMaxima);
        corrida.setTemperatura(intTemperatura);
        corrida.setpSE(Integer.valueOf(valorSkPse.getText().toString()));
        corrida.setClima(clima);
        corrida.setDistancia(distanciaTotal);
        corrida.setTerreno(terreno);
        corrida.setCalorias(mCaloriaTotal);
        corrida.setRitmoMedio(ritmoMedio);
        corrida.setTipo(tipoCorrida);
        corrida.setEmailCorredor(emailCorredor);
        corrida.setRota(rota);
        corrida.setObs(editTextObs.getText().toString());

    }

    private void criaRota() {
        rota = new ArrayList<>();
        int aux = 0;

        for (LatLng ponto : pontos) {

            PontoRota pontoRota = new PontoRota();
            pontoRota.setLatLng(ponto);
            pontoRota.setVelocidade(speedPontos[aux]);
            pontoRota.setDtHora(dataHora.get(aux));
            pontoRota.setDistancia(distanciaPontos[aux]);
            pontoRota.setTempo(tempoPontos[aux]);

            rota.add(pontoRota);

            aux++;

        }

    }

    private void verificaAtingiuObj() {

        ConsultaCorredorAtingiuObjTask objTask = new ConsultaCorredorAtingiuObjTask(this, corrida);
        objTask.execute();

        try {
            String resposta = objTask.get();

            if (resposta.toLowerCase().contains("atingiu")) {

                String aux = resposta.replace("atingiu", "");
                String mensagem1 = getString(R.string.atingiuobj) + " " + aux + " metros!";

                new AlertDialog.Builder(this).setTitle(R.string.parabens)
                        .setMessage(mensagem1)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new AlertDialog.Builder(AfterRunActivity.this).setMessage(R.string.coloquenovameta)
                                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                //Encerra a corrida
                                                encerrarCorrida();
                                            }
                                        }).setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialog) {
                                        //Encerra a corrida
                                        encerrarCorrida();
                                    }
                                }).show();
                            }
                        }).setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        new AlertDialog.Builder(AfterRunActivity.this).setMessage(R.string.coloquenovameta)
                                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //Encerra a corrida
                                        encerrarCorrida();
                                    }
                                }).setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                //Encerra a corrida
                                encerrarCorrida();
                            }
                        }).show();

                    }
                }).show();


            } else {
                //Encerra a corrida
                encerrarCorrida();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    private void verificaFimTreino() {

        VerificaUltimaCorridaTreinoTask ultCTask = new VerificaUltimaCorridaTreinoTask(this, corrida);
        ultCTask.execute();

        try {
            String resposta = (String) ultCTask.get();

            if (resposta.contains("ultima")) {
                fimTreino = true;
                String aux = resposta.replace("ultima", "");
                treino = new Treino();
                treino.setCodTreino(Integer.valueOf(aux));

            } else {
                fimTreino = false;
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


    }

    private void encerrarCorrida() {

        //Encerra a corrida

        if (fimTreino) {

            avaliarTreino();

            //GCM
            //Avisar o treinador
            //treinador atribuir nota ao desempenho do corredor


        } else {
            Intent intent = new Intent(AfterRunActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }


    }

    public void avaliarTreino() {

        //Criando o Dialog para avaliar o treino
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_fragment_avaliar_treino, null);
        TextView txtMensagem = (TextView) dialogView.findViewById(R.id.txtMensagem);
        txtMensagem.setText(R.string.terminoutreino);
        new AlertDialog.Builder(this).setView(dialogView)
                .setTitle(R.string.parabens)
                .setPositiveButton(R.string.enviaravaliacao, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        EditText txtAvaliacao = (EditText) dialogView.findViewById(R.id.txtAvaliacaoCorredor);
                        treino.setAvaliacaoDoCorredor(txtAvaliacao.getText().toString());
                        AvaliarTreinoCorredorTask avaliarTask = new AvaliarTreinoCorredorTask(AfterRunActivity.this, treino);
                        avaliarTask.execute();
                    }
                }).setNegativeButton(R.string.naoavaliar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Intent intent = new Intent(AfterRunActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }).show();
    }

    public void GetTemperaturaForecastAPI(){

        RequestBuilder weather = new RequestBuilder();

        Request request = new Request();
        request.setLat(String.valueOf(pontos.get(pontos.size()-1).latitude));
        request.setLng(String.valueOf(pontos.get(pontos.size()-1).longitude));
        request.setUnits(Request.Units.SI);
        request.setLanguage(Request.Language.PORTUGUESE);

        weather.getWeather(request, new Callback<WeatherResponse>()

                {
                    @Override
                    public void success(WeatherResponse weatherResponse, Response response) {
                        //Do something


                        intTemperatura = (int) weatherResponse.getCurrently().getTemperature();
                        txtTemperatura.setText(String.valueOf(intTemperatura));

                        Log.d("TesteClima", "Temp: " + weatherResponse.getCurrently().getTemperature());
                        Log.d("TesteClima", "Summary: " + weatherResponse.getCurrently().getSummary());
                        Log.d("TesteClima", "Percent clouds: " + weatherResponse.getCurrently().getCloudClover());
                        Log.d("TesteClima", "SensacaoTermica: " + weatherResponse.getCurrently().getApparentTemperature());
                        Log.d("TesteClima", "Vento: (m/s)" + weatherResponse.getCurrently().getWindSpeed());

                        Log.i("TesteClima", response.toString() + " ///// " + weatherResponse.toString());

                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        Log.d("TesteClima", "Error while calling: " + retrofitError.getUrl());
                        intTemperatura = 0;
                        txtTemperatura.setText("0");

                        Snackbar snackbar = Snackbar
                                .make(coordinatorLayout, R.string.erroTemperatura, Snackbar.LENGTH_LONG)
                                .setAction(R.string.tentarNovamente, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        GetTemperaturaForecastAPI();
                                    }
                                });


                        snackbar.show();

                    }
                }

        );


    }



}
