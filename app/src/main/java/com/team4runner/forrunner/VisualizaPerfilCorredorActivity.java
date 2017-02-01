package com.team4runner.forrunner;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.team4runner.forrunner.modelo.Corredor;
import com.team4runner.forrunner.modelo.Treinador;
import com.team4runner.forrunner.tasks.AssociaTreinadorTask;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.ExecutionException;

public class VisualizaPerfilCorredorActivity extends AppCompatActivity {

    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbarLayout;
    Treinador treinador;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualiza_perfil_corredor);
        //Recebe dados da activity anterior
        Intent it = getIntent();
        final Corredor corredor = (Corredor) it.getSerializableExtra("Corredor");
        final String descObj = it.getStringExtra("obj");
        boolean localizarCorredors = it.getBooleanExtra("localizarCorredors", false);

        SharedPreferences prefs = getSharedPreferences("Configuracoes", Context.MODE_PRIVATE);
        treinador = new Treinador();
        treinador.setEmail(prefs.getString("email", ""));


        //Collapsing toolbar com nome e imagem
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("");
        ImageView imagemPerfil = (ImageView) findViewById(R.id.toolbarFotoPerfil);
        if (!corredor.getImagemPerfil().equals("")) {
            Picasso.with(this).load(getResources().getString(R.string.imageserver) + corredor.getImagemPerfil()).into(imagemPerfil);
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        //Demais componentes Simples


        RelativeLayout layoutRespondeSolicitacao = (RelativeLayout) findViewById(R.id.layoutResponderSolicitacao);


        TextView txtNome = (TextView) findViewById(R.id.txtNome);
        TextView txtSexo = (TextView) findViewById(R.id.txtSexo);
        TextView txtIdade = (TextView) findViewById(R.id.txtIdade);
        Integer auxInt = getIdade(corredor.getDataNasc());
        String auxString = auxInt + " " + getResources().getString(R.string.anos);
        txtIdade.setText(auxString);

        TextView txtAltura = (TextView) findViewById(R.id.txtAltura);
        TextView txtPeso = (TextView) findViewById(R.id.txtPeso);
        TextView txtObj = (TextView) findViewById(R.id.txtObj);
        TextView txtSobreMim = (TextView) findViewById(R.id.txtSobreMim);

        txtAltura.setText(String.valueOf(corredor.getAltura()));
        String auxPeso = String.valueOf(corredor.getPeso()) + " Kg";
        txtPeso.setText(auxPeso);
        txtObj.setText(descObj + "K");

        txtSobreMim.setText(corredor.getSobreMim());


        txtNome.setText(corredor.getNome());
        txtSexo.setText(corredor.getSexo());

        FloatingActionButton btnChat = (FloatingActionButton) findViewById(R.id.btnChat);
        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(VisualizaPerfilCorredorActivity.this, ChatActivity.class);
                intent.putExtra("origem", "treinador");
                intent.putExtra("corredor", corredor);
                startActivity(intent);

            }
        });


        FloatingActionButton btnAceitar = (FloatingActionButton) findViewById(R.id.btnAceitar);
        btnAceitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(VisualizaPerfilCorredorActivity.this).setTitle(R.string.associaraocorredor)
                        .setMessage(R.string.desejaassociaraocorredor)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Treinador treinador = new Treinador();
                                SharedPreferences prefs = getSharedPreferences("Configuracoes", MODE_PRIVATE);
                                treinador.setEmail(prefs.getString("email", ""));

                                String method = "treinador_aceita_solicitacao-json";
                                AssociaTreinadorTask task = new AssociaTreinadorTask(VisualizaPerfilCorredorActivity.this, treinador, corredor, method, "");
                                task.execute();

                                try {
                                    String resposta = (String) task.get();
                                    if (resposta.equals("sucesso")) {

                                        new AlertDialog.Builder(VisualizaPerfilCorredorActivity.this).setTitle(R.string.associaraocorredor)
                                                .setMessage(R.string.parabensnovocorredor)
                                                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        Intent intent = new Intent(VisualizaPerfilCorredorActivity.this, MainActivity.class);
                                                        startActivity(intent);

                                                    }
                                                }).show();
                                    } else {
                                        Toast.makeText(VisualizaPerfilCorredorActivity.this, R.string.opserro, Toast.LENGTH_SHORT).show();
                                    }


                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                }


                            }
                        }).setNegativeButton(R.string.cancelar, null)
                        .show();


            }
        });

        FloatingActionButton btnRecusar = (FloatingActionButton) findViewById(R.id.btnRecusar);
        btnRecusar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(VisualizaPerfilCorredorActivity.this).setTitle(R.string.recusar_corredor_titulo_dialog)
                        .setMessage(R.string.recusar_corredor_corpo_dialog)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Treinador treinador = new Treinador();
                                SharedPreferences prefs = getSharedPreferences("Configuracoes", MODE_PRIVATE);
                                treinador.setEmail(prefs.getString("email", ""));

                                String method = "treinador_recusa_solicitacao-json";
                                AssociaTreinadorTask task = new AssociaTreinadorTask(VisualizaPerfilCorredorActivity.this, treinador, corredor, method, "");
                                task.execute();

                                try {
                                    String resposta = (String) task.get();
                                    if (resposta.equals("sucesso")) {

                                        new AlertDialog.Builder(VisualizaPerfilCorredorActivity.this).setTitle(R.string.recusar_corredor_titulo_dialog)
                                                .setMessage(R.string.confirma_recusar_corredor_corpo_dialog)
                                                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        Intent intent = new Intent(VisualizaPerfilCorredorActivity.this, MainActivity.class);
                                                        startActivity(intent);

                                                    }
                                                }).show();
                                    } else {
                                        Toast.makeText(VisualizaPerfilCorredorActivity.this, R.string.opserro, Toast.LENGTH_SHORT).show();
                                    }


                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                }


                            }
                        }).setNegativeButton(R.string.cancelar, null)
                        .show();


            }
        });

        if (localizarCorredors) {
            layoutRespondeSolicitacao.setVisibility(View.GONE);
        } else {
            layoutRespondeSolicitacao.setVisibility(View.VISIBLE);
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


    public int getIdade(Date dataNasc) {
        // Data de hoje.
        GregorianCalendar agora = new GregorianCalendar();
        int ano = 0, mes = 0, dia = 0;
        // Data do nascimento.
        GregorianCalendar nascimento = new GregorianCalendar();
        int anoNasc = 0, mesNasc = 0, diaNasc = 0;
        // Idade.
        int idade = 0;


        nascimento.setTime(dataNasc);
        ano = agora.get(Calendar.YEAR);
        mes = agora.get(Calendar.MONTH) + 1;
        dia = agora.get(Calendar.DAY_OF_MONTH);
        anoNasc = nascimento.get(Calendar.YEAR);
        mesNasc = nascimento.get(Calendar.MONTH) + 1;
        diaNasc = nascimento.get(Calendar.DAY_OF_MONTH);
        idade = ano - anoNasc;
        // Calculando diferencas de mes e dia.
        if (mes < mesNasc) {
            idade--;
        } else {
            if (dia < diaNasc) {
                idade--;
            }
        }
        // Ultimo teste, idade "negativa".
        if (idade < 0) {
            idade = 0;
        }
        Log.i("idade", idade + "  lalala");

        return (idade);
    }


}
