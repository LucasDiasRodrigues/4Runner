package com.team4runner.forrunner.tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Switch;
import android.widget.Toast;

import com.team4runner.forrunner.CadastroTesteActivity;
import com.team4runner.forrunner.R;
import com.team4runner.forrunner.fragment.MeusCorredoresTreinadorTestesFragment;
import com.team4runner.forrunner.modelo.TesteDeCampo;
import com.team4runner.forrunner.web.HttpConnection;
import com.team4runner.forrunner.web.TesteDeCampoJson;

import java.io.StringReader;

/**
 * Created by Lucas on 12/01/2016.
 */
public class CadastroTesteDeCampoTask extends AsyncTask<Object, String, String> {
    private final String url = "http://www.4runnerapp.com.br/ServerPhp/Ctrl/cadastra_teste_json.php";
    private String method;

    private ProgressDialog progress;

    private Activity activity;
    private TesteDeCampo testeDeCampo;
    private String editar;
    private DialogFragment dialogFragment;
    private MeusCorredoresTreinadorTestesFragment fragment;


    public CadastroTesteDeCampoTask(Activity activity, TesteDeCampo testeDeCampo, String editar, DialogFragment dialogFragment, MeusCorredoresTreinadorTestesFragment fragment) {
        this.activity = activity;
        this.testeDeCampo = testeDeCampo;
        this.editar = editar;
        this.dialogFragment = dialogFragment;
        this.fragment = fragment;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progress = ProgressDialog.show(activity, "Aguarde...", "Envio de dados para web", true, true);
    }

    @Override
    protected String doInBackground(Object[] params) {

        switch (editar) {
            case "editar":
                method = "editaTeste-json";
                break;
            case "cadastrar":
                method = "cadastraTeste-json";
                break;
            case "substituir":
                method = "substituirTeste-json";
                break;
        }


        TesteDeCampoJson testeJson = new TesteDeCampoJson();
        String data = testeJson.TesteDeCampoToJson(testeDeCampo);

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
                Toast.makeText(activity, "Agendamento efetuado", Toast.LENGTH_SHORT).show();
               dialogFragment.dismiss();
                fragment.ListaTestes();
                break;
            case "treino ativo":
                new AlertDialog.Builder(activity)
                        .setTitle(R.string.atencao)
                        .setMessage(R.string.errotreinoativo)
                        .setPositiveButton(R.string.sim, null).show();
                break;
            case "teste pendente":
                new AlertDialog.Builder(activity)
                        .setTitle(R.string.atencao)
                        .setMessage(R.string.errotestependente)
                        .setPositiveButton(R.string.sim, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                CadastroTesteDeCampoTask task = new CadastroTesteDeCampoTask(activity, testeDeCampo, "substituir",dialogFragment, fragment);
                                task.execute();
                               // ((CadastroTesteActivity)activity).substituirTesteExistente();
                            }
                        })
                        .setNegativeButton(R.string.cancelar, null).show();

                break;
            case "falha":
                Toast.makeText(activity, R.string.opserro, Toast.LENGTH_SHORT).show();
                break;
        }


    }
}
