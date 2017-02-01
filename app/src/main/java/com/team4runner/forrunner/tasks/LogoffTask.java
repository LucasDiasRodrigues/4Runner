package com.team4runner.forrunner.tasks;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.Toast;

import com.team4runner.forrunner.MainActivity;
import com.team4runner.forrunner.R;
import com.team4runner.forrunner.web.HttpConnection;
import com.team4runner.forrunner.web.LoginJson;

/**
 * Created by Lucas on 07/12/2015.
 */
public class LogoffTask extends AsyncTask {

    private final String url = "http://www.4runnerapp.com.br/ServerPhp/Ctrl/login.php";
    private final String method = "logoff-json";
    private final Context context;
    private String origem;
    private String email;
    private ProgressDialog progress;

    public LogoffTask (String email, String origem, Context context) {
        this.email = email;
        this.origem = origem;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progress = ProgressDialog.show(context, "Aguarde...", "Saindo", true, true);
    }

    @Override
    protected Object doInBackground(Object[] params) {
        LoginJson json = new LoginJson();
        String data = json.logoffToJson(email,origem);

        String answer = HttpConnection.getSetDataWeb(this.url, this.method, data);
        Log.i("LogoffTask", answer+"");


        return answer;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

        progress.dismiss();

        if(o.equals("Sucesso")){
            SharedPreferences prefs = context.getSharedPreferences("Configuracoes", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear();
            editor.commit();

            //CancelaNotifications se houver
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancelAll();


            Intent intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);

            ((Activity)context).finish();

        } else {
            Toast.makeText(context, R.string.conexaoIndosponivel,Toast.LENGTH_SHORT).show();

        }
    }
}
