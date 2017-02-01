package com.team4runner.forrunner.tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.team4runner.forrunner.CadastroTesteActivity;
import com.team4runner.forrunner.CadastroTreinoActivity;
import com.team4runner.forrunner.R;
import com.team4runner.forrunner.fragment.CadastroTreinoGeralFragment;
import com.team4runner.forrunner.modelo.TesteDeCampo;
import com.team4runner.forrunner.modelo.Treino;
import com.team4runner.forrunner.web.HttpConnection;
import com.team4runner.forrunner.web.TesteDeCampoJson;
import com.team4runner.forrunner.web.TreinoJson;

import java.util.List;

/**
 * Created by Lucas on 11/02/2016.
 */
public class ConsultaTreinoTesteAtivoTask extends AsyncTask<Object, Object, String> {
    private final String url = "http://www.4runnerapp.com.br/ServerPhp/Ctrl/cadastra_altera_treino_json.php";
    private String method = "verifica_treino_teste_ativo";

    private ProgressDialog progress;

    private Activity activity;
    private Treino treino;
    private List<Bundle> bundles;
    private CadastroTreinoGeralFragment fragment;

    public ConsultaTreinoTesteAtivoTask(Activity activity, Treino treino, List<Bundle> bundles, CadastroTreinoGeralFragment fragment) {
        this.activity = activity;
        this.treino = treino;
        this.bundles = bundles;
        this.fragment = fragment;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progress = ProgressDialog.show(activity, "Aguarde...", "Envio de dados para web", true, true);
    }

    @Override
    protected String doInBackground(Object[] params) {

        TreinoJson treinoJson = new TreinoJson();
        String data = treinoJson.TreinoToJson(treino);

        String answer = HttpConnection.getSetDataWeb(this.url, this.method, data);
        Log.i("Script", "ANSWER: " + answer);

        return answer;
    }

    @Override
    protected void onPostExecute(String answer) {
        super.onPostExecute(answer);
        progress.dismiss();

        switch (answer) {
            case "sucesso":

                //Codigo para continuar
                onSucesso();


                break;
            case "treinoAtivo":
                new AlertDialog.Builder(activity)
                        .setTitle(R.string.atencao)
                        .setMessage(R.string.errotreinoativo2)
                        .setPositiveButton(R.string.sim, null).show();
                fragment.limpaVariaveis();
                break;
            case "erroNaConsulta":
                Toast.makeText(activity, R.string.opserro, Toast.LENGTH_SHORT).show();
                fragment.limpaVariaveis();
                break;

            default:

                if(answer.contains("testeAtivo")){

                    String aux = answer.replace("testeAtivo","");

                    TesteDeCampoJson testeJson = new TesteDeCampoJson();
                    final TesteDeCampo testeDeCampo = testeJson.JsonToTesteDeCampo(aux);


                    //Codigo para cancelar o teste
                    new AlertDialog.Builder(activity)
                            .setTitle(R.string.atencao)
                            .setMessage(R.string.errotestependentetreino)
                            .setPositiveButton(R.string.continuar, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ((CadastroTreinoActivity)activity).cancelarTeste(testeDeCampo);

                                    //Codigo para continuar
                                    onSucesso();
                                }
                            })
                            .setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    fragment.limpaVariaveis();
                                }
                            }).show();



                }


                break;
        }


    }

    private void onSucesso(){
        ((CadastroTreinoActivity)activity).onCadastroTreinoEspecifico(bundles,treino);
    }
}
