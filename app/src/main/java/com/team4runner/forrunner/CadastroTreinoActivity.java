package com.team4runner.forrunner;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.team4runner.forrunner.fragment.CadastroTreinoEspecificoDetalhadoFragment;
import com.team4runner.forrunner.fragment.CadastroTreinoEspecificoFragment;
import com.team4runner.forrunner.fragment.CadastroTreinoGeralFragment;
import com.team4runner.forrunner.fragment.CadastroTreinoObservacaoFragment;
import com.team4runner.forrunner.modelo.Corredor;
import com.team4runner.forrunner.modelo.Exercicio;
import com.team4runner.forrunner.modelo.Objetivo;
import com.team4runner.forrunner.modelo.TesteDeCampo;
import com.team4runner.forrunner.modelo.Treinador;
import com.team4runner.forrunner.modelo.Treino;
import com.team4runner.forrunner.modelo.TreinoExercicio;
import com.team4runner.forrunner.tasks.CancelarTesteDeCampoTask;
import com.team4runner.forrunner.tasks.ListaExerciciosTask;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CadastroTreinoActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Corredor corredor;
    private List<Exercicio> exercicios;

    private boolean erroValidCampo = false;

    private List<Objetivo> objetivos;

    private Treinador treinador;

    //Variaveis preenchidas na fragment de cadastro Geral
    List<Bundle> bundlesDiasDeTreino;
    Treino treino;

    //Variavel do cadastro especifico detalhado
    List<TreinoExercicio> treinoExerciciosFinal = new ArrayList<>();

    DateFormat brDateFormat = new SimpleDateFormat("dd-MM-yyyy");

    //Lista com as referencias das fragments instanciadas das semanas de treino
    List<Fragment> mlistFragment = new ArrayList<>();
    int count = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_treino);

        SharedPreferences prefs = getSharedPreferences("Configuracoes", Context.MODE_PRIVATE);
        treinador = new Treinador();
        treinador.setEmail(prefs.getString("email", ""));
        treinador.setNome(prefs.getString("nome", ""));

        listaExercicios();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Cadastro de treino");
        setSupportActionBar(toolbar);

        corredor = (Corredor) getIntent().getSerializableExtra("corredor");

        FragmentTransaction cadastroTreino = getSupportFragmentManager().beginTransaction();
        cadastroTreino.replace(R.id.frame_fragment, new CadastroTreinoGeralFragment());
        cadastroTreino.commit();


    }



    public void onCadastroTreinoEspecifico(List<Bundle> bundles, Treino treino) {
        this.bundlesDiasDeTreino = bundles;
        this.treino = treino;

        for (Bundle auxBun : bundlesDiasDeTreino) {

            //Teste
            DateFormat auxDat = new SimpleDateFormat("yyyy-MM-dd");
            Log.i("recTaskBundle", " Semana " + auxBun.getInt("semana", 666) + " data ="
                    + auxDat.format(auxBun.getSerializable("data")));
            //Fim Teste
        }

        FragmentTransaction cadastroTreinoEspecifico = getSupportFragmentManager().beginTransaction();
        cadastroTreinoEspecifico.replace(R.id.frame_fragment, new CadastroTreinoEspecificoFragment());
        cadastroTreinoEspecifico.addToBackStack(null);
        cadastroTreinoEspecifico.commit();

    }

    public void onCadastroTreinoObservacoes() {

        FragmentTransaction cadastroTreinoObservacoes = getSupportFragmentManager().beginTransaction();
        cadastroTreinoObservacoes.replace(R.id.frame_fragment, new CadastroTreinoObservacaoFragment());
        cadastroTreinoObservacoes.addToBackStack(null);
        cadastroTreinoObservacoes.commit();

    }

    public void addArrayTreinoExerciciosFinal(List<TreinoExercicio> treinoExercicios, boolean Ok){

        if(Ok){

            treinoExerciciosFinal.addAll(treinoExercicios);

            for (TreinoExercicio treinoExercicio : treinoExerciciosFinal) {

                String auxTeste = treinoExercicio.getSemana() + " // " +
                        brDateFormat.format(treinoExercicio.getData()) + " // " +
                        (treinoExercicio.getExercicio()).getNomeExercicio() + " // " +
                        treinoExercicio.getOrdem() + " // " +
                        treinoExercicio.getPosIntervalo();
                Log.i("Array final",auxTeste);

            }


            //n�o Ok
        } else{
            treinoExerciciosFinal.clear();
            erroValidCampo = true;
            Log.i("NaoOk", "semana" + (count+1));
            Toast.makeText(this,R.string.camposerrados,Toast.LENGTH_LONG).show();
        }

        //apos todas as semanas
        count++;
        Log.i("geral", "semana" +count);

        if (count == treino.getQtdSemanas()) {

            if(!erroValidCampo) {
                //Encerra a fragment
                onCadastroTreinoObservacoes();
            } else{
                treinoExerciciosFinal.clear();
                erroValidCampo = false;
                count = 0;
            }
        }


    }

    //Metodos para as fragments terem acesso as variaveis
    public Corredor getCorredor() {
        return this.corredor;
    }

    public void setListObj(List<Objetivo> objetivos){
        this.objetivos = objetivos;
    }
    public List<Objetivo> getListObj(){
        return objetivos;
    }

    public List<Bundle> getBundlesDiasDeTreino(){

        for (Bundle auxBun : bundlesDiasDeTreino) {

            //Teste
            DateFormat auxDat = new SimpleDateFormat("yyyy-MM-dd");
            Log.i("EnvBundlesFrag", " Semana " + auxBun.getInt("semana", 666) + " data ="
                    + auxDat.format(auxBun.getSerializable("data")));
            //Fim Teste
        }

        return this.bundlesDiasDeTreino;
    }

    public Treino getTreino(){
        return this.treino;
    }

    public List<Exercicio> getExercicios(){
        return this.exercicios;
    }

    public List<TreinoExercicio> getTreinoExerciciosFinal(){
        return treinoExerciciosFinal;
    }


    //Cancelar o teste de campo se necessario
    public void cancelarTeste(TesteDeCampo teste) {
        CancelarTesteDeCampoTask task = new CancelarTesteDeCampoTask(this, teste);
        task.execute();
    }

    //Lista exercicios
    public void listaExercicios(){
        ListaExerciciosTask task = new ListaExerciciosTask(this, treinador);
        task.execute();

        try {
            exercicios = task.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void setFragment(Fragment fragment){
        mlistFragment.add(fragment);
        ((CadastroTreinoEspecificoDetalhadoFragment)fragment).testeLog();
    }

    public void deleteFragmentsList(){
        mlistFragment.clear();
    }


    public void finalizaCadastroDetalhado(){

        for (Fragment fragmentSemana : mlistFragment) {
            ((CadastroTreinoEspecificoDetalhadoFragment) fragmentSemana).finalizarCadastro();
        }

    }

    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(this).setTitle(R.string.atencao)
                .setMessage(R.string.descartarAlteracoes)
                .setPositiveButton(R.string.sair, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CadastroTreinoActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setCancelable(false)
                .show();
    }

}
