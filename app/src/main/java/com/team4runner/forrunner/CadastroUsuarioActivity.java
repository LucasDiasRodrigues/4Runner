package com.team4runner.forrunner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.team4runner.forrunner.fragment.CadastroCorredorBasicoFragment;
import com.team4runner.forrunner.fragment.CadastroCorredorComplementarFragment;
import com.team4runner.forrunner.fragment.CadastroPerfilFragment;
import com.team4runner.forrunner.fragment.CadastroTreinadorBasicoFragment;
import com.team4runner.forrunner.fragment.CadastroTreinadorComplementarFragment;
import com.team4runner.forrunner.modelo.Corredor;
import com.team4runner.forrunner.modelo.Treinador;

public class CadastroUsuarioActivity extends ActionBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);


        //inicia primeira fragment
        FragmentTransaction transactionCadastroPerfil = getSupportFragmentManager().beginTransaction();
        transactionCadastroPerfil.replace(R.id.frameCadastro, new CadastroPerfilFragment());
        transactionCadastroPerfil.commit();

    }


    public void onCadastroBasicoCorredor(){

        FragmentTransaction transactionCadastroBasico = getSupportFragmentManager().beginTransaction();
        transactionCadastroBasico.replace(R.id.frameCadastro, new CadastroCorredorBasicoFragment());
        transactionCadastroBasico.addToBackStack(null);
        transactionCadastroBasico.commit();

    }

    public void onCadastroComplementarCorredor(Bundle corredorBundle){


        CadastroCorredorComplementarFragment fragment = new CadastroCorredorComplementarFragment();
        fragment.setArguments(corredorBundle);
        FragmentTransaction transactionCadastroComplementar = getSupportFragmentManager().beginTransaction();
        transactionCadastroComplementar.replace(R.id.frameCadastro, fragment);
        transactionCadastroComplementar.addToBackStack(null);
        transactionCadastroComplementar.commit();

    }

    public void onCadastroBasicoTreinador(){

        FragmentTransaction transactionCadastroBasico = getSupportFragmentManager().beginTransaction();
        transactionCadastroBasico.replace(R.id.frameCadastro, new CadastroTreinadorBasicoFragment());
        transactionCadastroBasico.addToBackStack(null);
        transactionCadastroBasico.commit();

    }

    public void onCadastroComplementarTreinador(Bundle treinadorBundle){

        CadastroTreinadorComplementarFragment fragment = new CadastroTreinadorComplementarFragment();
        fragment.setArguments(treinadorBundle);
        FragmentTransaction transactionCadastroComplementar = getSupportFragmentManager().beginTransaction();
        transactionCadastroComplementar.replace(R.id.frameCadastro, fragment);
        transactionCadastroComplementar.addToBackStack(null);
        transactionCadastroComplementar.commit();

    }







    public void onCadastroFinalizado(Object usuario){

        SharedPreferences prefs = getSharedPreferences("Configuracoes",MODE_PRIVATE);

        if(prefs.getString("perfil","").equals("treinador")){
            Treinador treinador = (Treinador)usuario;
            Intent intent = new Intent(this, ObterLocalizacaoTreinadorActivity.class);
            intent.putExtra("treinador",treinador);
            startActivity(intent);
            finish();
        }else if (prefs.getString("perfil","").equals("corredor")){
            Corredor corredor = (Corredor)usuario;
            Intent intent = new Intent(this, ObterLocalizacaoCorredorActivity.class);
            intent.putExtra("corredor", corredor);
            startActivity(intent);
            finish();
        }

    }

    public void onErro(){

        Toast.makeText(this,R.string.erroTenteNovamente,Toast.LENGTH_SHORT).show();
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
