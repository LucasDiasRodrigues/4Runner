package com.team4runner.forrunner.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.team4runner.forrunner.CadastroExercicioActivity;
import com.team4runner.forrunner.R;
import com.team4runner.forrunner.modelo.Exercicio;
import com.team4runner.forrunner.web.ExercicioJson;
import com.team4runner.forrunner.web.HttpConnection;

/**
 * Created by Lucas on 30/06/2015.
 */
public class CadastroExercicioTask extends AsyncTask<Object, String, String> {

    private Context context;
    private Exercicio exercicio;
    private ProgressDialog progress;

    private final String url = "http://www.4runnerapp.com.br/ServerPhp/Ctrl/cadastra_exercicio_json.php";
    private String method;

    public CadastroExercicioTask(Context context,Exercicio exercicio, String metodo){
        this.context = context;
        this.exercicio = exercicio;
        switch (metodo){
            case "cadastrar":
                method = "cadastrarExercicio-json";
                break;
            case "editar":
                method = "editarExercicio-json";
                break;
            case "inativar":
                method = "inativarExercicio-json";
                break;
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progress = ProgressDialog.show(context, "Aguarde...", "Envio de dados para web", true, true);
    }

    @Override
    protected String doInBackground(Object[] params) {

        ExercicioJson json = new ExercicioJson();
        String jsonExercicio = json.ExercicioToJson(exercicio);

        Log.i("JsonExercicio",jsonExercicio);

        String answer = new HttpConnection().getSetDataWeb(url,method,jsonExercicio);


        return answer;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        progress.dismiss();

        switch (s){
            case "editarsucesso":
                Toast.makeText(context, "Editado.", Toast.LENGTH_SHORT).show();
                break;
            case "inativarsucesso":
                Toast.makeText(context, "Inativado.", Toast.LENGTH_SHORT).show();
                break;
            case "cadastrarsucesso":
                Toast.makeText(context, "Cadastrado.", Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(context, context.getString(R.string.opserro), Toast.LENGTH_SHORT).show();
                break;
        }

    }
}
