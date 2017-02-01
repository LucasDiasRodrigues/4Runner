package com.team4runner.forrunner.tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.team4runner.forrunner.MainCorredorActivity;
import com.team4runner.forrunner.MainTreinadorActivity;
import com.team4runner.forrunner.R;
import com.team4runner.forrunner.fragment.HomeCorredorTreinadorFragment;
import com.team4runner.forrunner.modelo.Corredor;
import com.team4runner.forrunner.modelo.Treinador;
import com.team4runner.forrunner.web.CorredorJson;
import com.team4runner.forrunner.web.HttpConnection;
import com.team4runner.forrunner.web.TreinadorJson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by Lucas on 05/08/2015.
 */
public class AtualizarUsuarioTask extends AsyncTask<Object, Object, Object> {

    private Activity activity;
    private String data;
    private ProgressDialog progress;
    private Treinador treinador;
    private Corredor corredor;
    private String perfil;
    private DateFormat brDateFormat;
    private DateFormat sqlDateFormat;


    private final String url = "http://www.4runnerapp.com.br/ServerPhp/Ctrl/atualiza_usuario_json.php";
    private final String methodTreinador = "atualizaUsuarioTreinador-json";
    private final String methodCorredor = "atualizaUsuarioCorredor-json";

    //para atualizar a tela de aluno com treinador
    private HomeCorredorTreinadorFragment fragmentAlunoTreinador;

    public AtualizarUsuarioTask(MainCorredorActivity activity) {
        this.activity = activity;
        this.perfil = "corredor";
    }

    public AtualizarUsuarioTask(MainCorredorActivity activity, HomeCorredorTreinadorFragment fragmentAlunoTreinador) {
        this.activity = activity;
        this.perfil = "corredor";
        this.fragmentAlunoTreinador = fragmentAlunoTreinador;
    }

    public AtualizarUsuarioTask(MainTreinadorActivity activity) {
        this.activity = activity;
        this.perfil = "treinador";

    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progress = ProgressDialog.show(activity, "Aguarde...", "Atualizando dados", true, true);
    }

    @Override
    protected Object doInBackground(Object... params) {


        if (HttpConnection.isNetworkAvailable(activity)) {

            Log.i("CONECXAO", "CONECTADO");


            SharedPreferences prefs = activity.getSharedPreferences("Configuracoes", Context.MODE_PRIVATE);

            brDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            sqlDateFormat = new SimpleDateFormat("yyyy-MM-dd");


            if (prefs.getString("perfil", "").equals("corredor")) {

                corredor = new Corredor();
                corredor.setEmail(prefs.getString("email", ""));
                Log.i("emailCorredor", prefs.getString("email", ""));
                CorredorJson json = new CorredorJson();
                data = json.CorredorToJson(corredor);
                Log.i("data == ", data);


                String answer = HttpConnection.getSetDataWeb(this.url, this.methodCorredor, this.data);
                Log.i("resposta == ", answer);

                if (!answer.equals("")) {

                    corredor = json.JsonToCorredor(answer);

                    if (!corredor.getEmail().equals("") || !corredor.getEmail().equals("null")) {


                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("perfil", "corredor");
                        editor.putString("nome", corredor.getNome());
                        editor.putString("email", corredor.getEmail());
                        editor.putString("imagemperfil", corredor.getImagemPerfil());
                        editor.putString("senha", corredor.getSenha());
                        editor.putString("telefone", String.valueOf(corredor.getTelefone()));
                        editor.putString("localizacao", corredor.getLocalizacao());
                        editor.putString("sexo", corredor.getSexo());
                        editor.putString("peso", String.valueOf(corredor.getPeso()));
                        editor.putString("altura", String.valueOf(corredor.getAltura()));
                        editor.putString("pulsoRepouso", String.valueOf(corredor.getPulsorepouso()));
                        editor.putString("objetivo", String.valueOf(corredor.getObjetivo()));
                        editor.putString("emailTreinador", corredor.getEmailTreinador());
                        editor.putString("situacao", corredor.getSituacao());
                        editor.putString("sobreMim", corredor.getSobreMim());
                        editor.putString("locStatus", corredor.getLocStatus());
                        editor.putString("gcmId", corredor.getGcmId());
                        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
                        editor.putString("dataCadastro", dateFormat.format(corredor.getDataCadastro()));
                        editor.putString("dataNasc", brDateFormat.format(corredor.getDataNasc()));
                        editor.commit();

                    }


                    return answer;
                } else {
                    //sem conexao
                    String erro = "semconexao";
                    Log.i("CONECXAO", "DESCONECTADO");
                    return erro;
                }

            } else if (prefs.getString("perfil", "").equals("treinador")) {

                treinador = new Treinador();
                treinador.setEmail(prefs.getString("email", ""));
                TreinadorJson json = new TreinadorJson();
                data = json.TreinadorToJson(treinador);

                String answer = HttpConnection.getSetDataWeb(this.url, this.methodTreinador, this.data);
                Log.i("resposta == ", answer);

                if (!answer.equals("")) {

                    treinador = json.JsonToTreinador(answer);

                    if (!treinador.getEmail().equals("") || !treinador.getEmail().equals("null")) {

                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("perfil", "treinador");
                        editor.putString("nome", treinador.getNome());
                        editor.putString("email", treinador.getEmail());
                        editor.putString("imagemperfil", treinador.getImagemPerfil());
                        editor.putString("senha", treinador.getSenha());
                        editor.putString("telefone", String.valueOf(treinador.getTelefone()));
                        editor.putString("localizacao", treinador.getLocalizacao());
                        editor.putString("sexo", treinador.getSexo());
                        editor.putString("cref", treinador.getCref());
                        editor.putString("formacao", treinador.getFormacao());
                        editor.putString("situacao", treinador.getSituacao());
                        editor.putString("sobreMim", treinador.getSobreMim());
                        editor.putString("gcmId", treinador.getGcmId());
                        editor.putString("locStatus", treinador.getLocStatus());
                        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
                        editor.putString("dataCadastro", dateFormat.format(treinador.getDataCadastro()));
                        editor.putString("dataNasc", brDateFormat.format(treinador.getDataNasc()));
                        editor.commit();

                    }
                    return answer;
                } else {
                    //sem conexao
                    String erro = "semconexao";
                    Log.i("CONECXAO", "DESCONECTADO");
                    return erro;
                }
            }

        } else {
            //sem conexao
            String erro = "semconexao";
            Log.i("CONECXAO", "DESCONECTADO");
            return erro;
        }

        String erro = "erro";
        return erro;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        progress.dismiss();

        if (o.equals("semconexao")) {
            Toast.makeText(activity, R.string.conexaoIndosponivel, Toast.LENGTH_LONG).show();
        }

        if (perfil.equals("corredor") && !(o.equals("erro"))) {

            SharedPreferences prefs = activity.getSharedPreferences("Configuracoes", Context.MODE_PRIVATE);
            ((MainCorredorActivity) activity).headerNome.setText(prefs.getString("nome", " "));

            if(fragmentAlunoTreinador != null){
                fragmentAlunoTreinador.possuiTreinador(true);
            }

            if (!prefs.getString("imagemperfil", "").equals("")) {
                Picasso.with(activity).load(activity.getResources().getString(R.string.imageservermini) + prefs.getString("imagemperfil", ""))
                        .into(((MainCorredorActivity) activity).headerFotoPerfil);
            }

        } else if (perfil.equals("treinador") && !(o.equals("erro"))) {

            SharedPreferences prefs = activity.getSharedPreferences("Configuracoes", Context.MODE_PRIVATE);
            ((MainTreinadorActivity) activity).headerNome.setText(prefs.getString("nome", " "));

            if (!prefs.getString("imagemperfil", "").equals("")) {
                Picasso.with(activity).load(activity.getResources().getString(R.string.imageservermini) + prefs.getString("imagemperfil", ""))
                        .into(((MainTreinadorActivity) activity).headerFotoPerfil);
            }
        }


    }

}
