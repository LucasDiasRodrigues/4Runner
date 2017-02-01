package com.team4runner.forrunner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.appjolt.sdk.Appjolt;
import com.johnhiott.darkskyandroidlib.ForecastApi;
import com.johnhiott.darkskyandroidlib.RequestBuilder;
import com.johnhiott.darkskyandroidlib.models.Request;
import com.johnhiott.darkskyandroidlib.models.WeatherResponse;
import com.team4runner.forrunner.fragment.LoginFragment;
import com.team4runner.forrunner.fragment.MainFragment;
import com.team4runner.forrunner.fragment.RecuperarSenhaFragment;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //obrigatorio para apps no google play
        if (Appjolt.isGooglePlayInstall(this))
        {
            Appjolt.showEULA(this);
        }


        //Iniciando a primeira Fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_main, new LoginFragment());
        transaction.commit();
        //--

        //Tratar usuario previamente logado
        String logado = EstaLogado();
        if (logado != null) {
            if (logado.equals("corredor")) {
                Intent it = new Intent(this, MainCorredorActivity.class);
                startActivity(it);
                finish();
            } else if (logado.equals("instrutor")) {
                Intent it = new Intent(this, MainTreinadorActivity.class);
                startActivity(it);
                finish();
            }
        }
        //--


    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void Entrar() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_main, new LoginFragment());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void RecuperarSenha(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_main, new RecuperarSenhaFragment());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public String EstaLogado() {
        try {
            SharedPreferences prefs = getSharedPreferences("Configuracoes", MODE_PRIVATE);
            if (prefs != null) {
                boolean logado = prefs.getBoolean("logado", false);
                String perfil = prefs.getString("perfil", "");
                if (logado) {
                    if (perfil.equals("corredor")) {
                        return "corredor";
                    } else {
                        return "instrutor";
                    }
                }
            }
        } catch (NullPointerException exception) {


        }


        return null;
    }




}
