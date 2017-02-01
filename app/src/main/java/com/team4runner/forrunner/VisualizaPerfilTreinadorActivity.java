package com.team4runner.forrunner;

import android.app.AlertDialog;
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
import android.widget.RatingBar;
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

public class VisualizaPerfilTreinadorActivity extends AppCompatActivity {

    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbarLayout;

    RatingBar ratingBar;
    TextView txtQtdVotos;

    Treinador treinador;
    String statusSolicitacaoAssociacao;

    Corredor corredor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualiza_perfil_treinador);

        corredor = new Corredor();
        SharedPreferences prefs = getSharedPreferences("Configuracoes", MODE_PRIVATE);
        corredor.setEmail(prefs.getString("email", ""));

        //Recebe dados da activity anterior
        Intent it = getIntent();
        treinador = (Treinador) it.getSerializableExtra("Treinador");

        ///Consultar solicita��o do corredor
        verificarSolicitacao();

        //Collapsing toolbar com nome e imagem
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("");
        ImageView imagemPerfil = (ImageView) findViewById(R.id.toolbarFotoPerfil);
        if (!treinador.getImagemPerfil().equals("")) {
            Picasso.with(this).load(getResources().getString(R.string.imageserver) + treinador.getImagemPerfil()).into(imagemPerfil);
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        //Demais componentes Simples

        TextView txtNome = (TextView) findViewById(R.id.txtNome);
        TextView txtIdade = (TextView) findViewById(R.id.txtIdade);
        TextView txtSexo = (TextView) findViewById(R.id.txtSexo);
        TextView txtSobreMim = (TextView) findViewById(R.id.txtSobreMim);

        TextView txtCref = (TextView) findViewById(R.id.txtCref);
        TextView txtFormacao = (TextView) findViewById(R.id.txtFormacao);

        txtNome.setText(treinador.getNome());
        Integer auxInt = getIdade(treinador.getDataNasc());
        String auxString = auxInt + " " + getResources().getString(R.string.anos);
        txtIdade.setText(auxString);
        txtSexo.setText(treinador.getSexo());
        txtSobreMim.setText(treinador.getSobreMim());

        txtCref.setText(treinador.getCref());
        txtFormacao.setText(treinador.getFormacao());


        //Chat
        FloatingActionButton btnChat = (FloatingActionButton) findViewById(R.id.btnChat);
        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(VisualizaPerfilTreinadorActivity.this, ChatActivity.class);
                intent.putExtra("origem", "corredor");
                intent.putExtra("treinador", treinador);
                startActivity(intent);

            }
        });

        //RatingBar
        ratingBar = (RatingBar) findViewById(R.id.ratingTreinador);
        float aux = (float) treinador.getMediaAvaliacao() / 2;
        ratingBar.setRating(aux);

        txtQtdVotos = (TextView) findViewById(R.id.txtQtdVotos);
        txtQtdVotos.setText(String.valueOf(treinador.getQtdAvaliacoes()) + " " + getResources().getString(R.string.avaliacoes));


        FloatingActionButton btnAssociar = (FloatingActionButton) findViewById(R.id.btnAssociar);

        if (statusSolicitacaoAssociacao.equals("pendente")) {
            btnAssociar.setEnabled(false);
            btnAssociar.setVisibility(View.GONE);
        }
        btnAssociar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(VisualizaPerfilTreinadorActivity.this).setTitle(R.string.associaraotreinador)
                        .setMessage(R.string.treinadorentraracontato)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                String method = "corredor_envia_solicitacao-json";
                                AssociaTreinadorTask task = new AssociaTreinadorTask(VisualizaPerfilTreinadorActivity.this, treinador, corredor, method, "false");
                                task.execute();

                                try {
                                    String resposta = (String) task.get();
                                    if (resposta.equals("sucesso")) {

                                        new AlertDialog.Builder(VisualizaPerfilTreinadorActivity.this).setTitle(R.string.associaraotreinador)
                                                .setMessage(R.string.solicitacaoenviada)
                                                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        Intent intent = new Intent(VisualizaPerfilTreinadorActivity.this, MainCorredorActivity.class);
                                                        startActivity(intent);

                                                    }
                                                }).show();

                                        //Solicitacao de associacao pendente
                                    } else if (resposta.equals("solicitacaoPendente")) {

                                        new AlertDialog.Builder(VisualizaPerfilTreinadorActivity.this).setTitle(R.string.atencao)
                                                .setMessage(R.string.possuiAssociacaoPendente)
                                                .setPositiveButton(R.string.continuar, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {


                                                        String method = "corredor_envia_solicitacao-json";
                                                        AssociaTreinadorTask task = new AssociaTreinadorTask(VisualizaPerfilTreinadorActivity.this, treinador, corredor, method, "true");
                                                        task.execute();

                                                        try {
                                                            String resposta = (String) task.get();

                                                            if (resposta.equals("sucesso")) {

                                                                new AlertDialog.Builder(VisualizaPerfilTreinadorActivity.this).setTitle(R.string.associaraotreinador)
                                                                        .setMessage(R.string.solicitacaoenviada)
                                                                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(DialogInterface dialog, int which) {
                                                                                Intent intent = new Intent(VisualizaPerfilTreinadorActivity.this, MainCorredorActivity.class);
                                                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                                startActivity(intent);

                                                                                VisualizaPerfilTreinadorActivity.this.finish();

                                                                            }
                                                                        }).show();

                                                            } else {
                                                                Toast.makeText(VisualizaPerfilTreinadorActivity.this, R.string.opserro, Toast.LENGTH_SHORT).show();
                                                            }


                                                        } catch (InterruptedException e) {
                                                            e.printStackTrace();
                                                        } catch (ExecutionException e) {
                                                            e.printStackTrace();
                                                        }


                                                    }
                                                })
                                                .setNegativeButton(R.string.nao, null)
                                                .show();

                                    } else {
                                        Toast.makeText(VisualizaPerfilTreinadorActivity.this, R.string.opserro, Toast.LENGTH_SHORT).show();
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


    }

    @Override
    protected void onStop() {
        super.onStop();


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


    public void verificarSolicitacao() {
        String method = "verifica_status_solicitacao_associacao-json";
        AssociaTreinadorTask task = new AssociaTreinadorTask(VisualizaPerfilTreinadorActivity.this, treinador, corredor, method,"");
        task.execute();
        try {
            statusSolicitacaoAssociacao = (String) task.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
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
