package com.team4runner.forrunner.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.team4runner.forrunner.fragment.HomeCorredorMeuTreinoFragment;
import com.team4runner.forrunner.fragment.MeusCorredoresTreinadorTestesFragment;
import com.team4runner.forrunner.fragment.TesteDeCampoCorredorFragment;
import com.team4runner.forrunner.modelo.Corredor;
import com.team4runner.forrunner.modelo.TesteDeCampo;
import com.team4runner.forrunner.web.CorredorJson;
import com.team4runner.forrunner.web.HttpConnection;
import com.team4runner.forrunner.web.TesteDeCampoJson;

import java.util.List;

/**
 * Created by Lucas on 06/01/2016.
 */
public class ListaTestesDeCampoTask extends AsyncTask {
    private final String url = "http://www.4runnerapp.com.br/ServerPhp/Ctrl/lista_teste_json.php";
    private String method;


    private Context context;
    private Corredor corredor;
    private String emailTreinador;
    private Fragment fragment;


    public ListaTestesDeCampoTask(Context context, Corredor corredor, String emailTreinador, Boolean porDia, Fragment fragment, String perfil) {
        this.context = context;
        this.corredor = corredor;
        this.emailTreinador = emailTreinador;
        this.corredor.setEmailTreinador(emailTreinador);
        this.fragment = fragment;
        if (porDia) {
            method = "listaTestePorCorredorDia-json";
        } else {

            if (perfil.equals("treinador")) {
                method = "listaTestePorCorredor-json";
            } else if (perfil.equals("corredor")) {
                method = "listaTestes-json";
            }

        }
    }


    @Override
    protected Object doInBackground(Object[] params) {

        CorredorJson corredorJson = new CorredorJson();
        String data = corredorJson.CorredorToJson(corredor);


        String answer = HttpConnection.getSetDataWeb(this.url, this.method, data);
        Log.i("ResultadoTreino =", answer);


        TesteDeCampoJson testeJson = new TesteDeCampoJson();
        List<TesteDeCampo> mList = testeJson.ArrayJsonToListTesteDeCampo(answer);

        return mList;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

        if (fragment != null && fragment instanceof TesteDeCampoCorredorFragment) {

            ((TesteDeCampoCorredorFragment) fragment).AtualizaListaTeste((List<TesteDeCampo>) o);

        }

        if (fragment != null && fragment instanceof HomeCorredorMeuTreinoFragment) {

            ((HomeCorredorMeuTreinoFragment) fragment).listaTesteDia((List<TesteDeCampo>) o);

        }

        if (fragment != null && fragment instanceof MeusCorredoresTreinadorTestesFragment) {

            ((MeusCorredoresTreinadorTestesFragment) fragment).AtualizaListaTeste((List<TesteDeCampo>) o);

        }

    }
}
