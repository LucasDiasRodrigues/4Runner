package com.team4runner.forrunner.GCM;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.team4runner.forrunner.ChatActivity;
import com.team4runner.forrunner.MainActivity;
import com.team4runner.forrunner.MainTreinadorActivity;
import com.team4runner.forrunner.MeuCorredorTreinadorActivity;
import com.team4runner.forrunner.NovosCorredoresActivity;
import com.team4runner.forrunner.R;
import com.team4runner.forrunner.modelo.Corredor;
import com.team4runner.forrunner.tasks.AtualizaMsgRecebidaTask;
import com.team4runner.forrunner.web.CorredorJson;
import com.team4runner.forrunner.web.MensagemJson;

import java.util.List;

/**
 * Created by Lucas on 09/12/2015.
 */
public class MyGcmListenerService extends com.google.android.gms.gcm.GcmListenerService {


    private static final String TAG = "MyGcmListenerService";


    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("mensagem");
        String tipo = data.getString("tipo");
        String idNotificacao = data.getString("idNotificacao");
        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Message: " + message);
        Log.d(TAG, "idNotificacao:  " + idNotificacao);

        switch (tipo) {
            case "chat":
                onMessageChat(message);
                break;
            case "solicitacaoAssociacaoCorredor":
                onMessageSolicitacao();
                break;
            case "solicitacaoAssociacaoAceita":
                onMessageSolicitacaoAceita();
                break;
            case "testeAgendado":
                onMessageTesteAgendado();
                break;
            case "treinoConcluido":
                onTreinoConcluido(message);
                break;
            case "treinoAgendado":
                onTreinoAgendado(message);
        }

        //   - Store message in local database.
        //    - Update UI.

    }

    private void onTreinoAgendado(String message) {
        SharedPreferences prefs = getSharedPreferences("Configuracoes", Context.MODE_PRIVATE);

        if (prefs.getString("perfil", "").equals("corredor")) {


            String titulo = "4Runner";
            String corpoMsg = getResources().getString(R.string.corpo_notificacao_treino_agendado);

            Intent intent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                    PendingIntent.FLAG_ONE_SHOT);


            sendNotification(titulo, corpoMsg, "treinoAgendado", pendingIntent,this.getResources().getColor(R.color.primary_dark), R.drawable.ic_calendar_text_white_48dp);

        }


    }

    private void onTreinoConcluido(String message) {
        SharedPreferences prefs = getSharedPreferences("Configuracoes", Context.MODE_PRIVATE);

        if (prefs.getString("perfil", "").equals("treinador")) {

            CorredorJson corredorJson = new CorredorJson();
            Corredor corredor = corredorJson.JsonToCorredor(message);

            String corpoMensagem = corredor.getNome() + " " + getString(R.string.treinoconcluidomsg1);

            //Com som
            Log.i(TAG, "NOTIFICTREINADOR");

            Intent intent = new Intent(this, MeuCorredorTreinadorActivity.class);
            intent.putExtra("Corredor", corredor);

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                    PendingIntent.FLAG_ONE_SHOT);
            sendNotification(getString(R.string.treinoconcluido), corpoMensagem, corredor.getEmail(), pendingIntent, this.getResources().getColor(R.color.primary), R.drawable.ic_check_white_24dp);

        }


    }


    private void onMessageSolicitacao() {
        SharedPreferences prefs = getSharedPreferences("Configuracoes", Context.MODE_PRIVATE);

        if (prefs.getString("perfil", "").equals("corredor")) {

            String titulo = "4Runner";
            String corpoMsg = getResources().getString(R.string.corpo_notificacao_associacao_corredor);

            Intent intent = new Intent(this, MainActivity.class);
            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                    PendingIntent.FLAG_ONE_SHOT);

            //Abrir na tab meu treinador
            sendNotification(titulo, corpoMsg, "solicitacaoAssociacaoTreinador", pendingIntent, this.getResources().getColor(R.color.orange), R.drawable.ic_account_multiple_white_48dp);

        } else if (prefs.getString("perfil", "").equals("treinador")) {

            String titulo = "4Runner";
            String corpoMsg = getResources().getString(R.string.corpo_notificacao_associacao_treinador);

            Intent intent = new Intent(this, NovosCorredoresActivity.class);

            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                    PendingIntent.FLAG_ONE_SHOT);

            sendNotification(titulo, corpoMsg, "solicitacaoAssociacaoCorredor", pendingIntent,this.getResources().getColor(R.color.orange), R.drawable.ic_account_multiple_white_48dp);

            //Dispara Broadcast
            Intent broadcastIntent = new Intent("solicitacao-4runner");
            //send broadcast
            this.sendBroadcast(broadcastIntent);


        }
    }

    private void onMessageSolicitacaoAceita() {
        SharedPreferences prefs = getSharedPreferences("Configuracoes", Context.MODE_PRIVATE);

        if (prefs.getString("perfil", "").equals("corredor")) {

            String titulo = "4Runner";
            String corpoMsg = getResources().getString(R.string.corpo_notificacao_associacao_corredor_aceita);

            Intent intent = new Intent(this, MainActivity.class);
            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                    PendingIntent.FLAG_ONE_SHOT);

            sendNotification(titulo, corpoMsg, "solicitacaoAssociacaoAceita", pendingIntent,this.getResources().getColor(R.color.orange), R.drawable.ic_account_multiple_white_48dp);


        } else if (prefs.getString("perfil", "").equals("treinador")) {

            String titulo = "4Runner";
            String corpoMsg = getResources().getString(R.string.corpo_notificacao_associacao_treinador_aceita);

            //Abrir na tab lista de corredores
            Intent intent = new Intent(this, MainTreinadorActivity.class);
            intent.putExtra("litaCorredores", 1);
            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                    PendingIntent.FLAG_ONE_SHOT);

            sendNotification(titulo, corpoMsg, "solicitacaoAssociacaoAceita", pendingIntent,this.getResources().getColor(R.color.orange), R.drawable.ic_account_multiple_white_48dp);
        }

    }

    private void onMessageChat(String message) {

        boolean chatAtivo = false;

        SharedPreferences prefs = getSharedPreferences("Configuracoes", Context.MODE_PRIVATE);
        SharedPreferences prefsChat = getSharedPreferences("Chat", MODE_PRIVATE);

        MensagemJson json = new MensagemJson();

        //Broadcast
        Intent broadcastIntent = new Intent("chat-4runner");
        //send broadcast
        this.sendBroadcast(broadcastIntent);


        if (prefs.getString("perfil", "").equals("corredor")) {

            List<Bundle> bundles = json.JsonToNotificacoesTreinador(message);
            int count = 1;
            for (Bundle bundle : bundles) {

                //Cria Notificacao
                String profPicRemetente = bundle.getString("profPic", "");
                String nomeRemetente = bundle.getString("nome");
                String emailRemetente = bundle.getString("email", "");
                String corpoMsg = getResources().getString(R.string.corpo_notificacao_chat1) + " " +
                        bundle.getString("qtdMsg") + " " + getResources().getString(R.string.corpo_notificacao_chat2);

                if (prefsChat.getBoolean("chatAtivo", false)) {

                    if (!prefsChat.getString("chatDestinatario", "").equals(emailRemetente)) {

                        //Sem som
                        Log.i(TAG, "NOTIFICALUNO");
                        Intent intent = new Intent(this, ChatActivity.class);
                        intent.putExtra("origem", "notificacaoCorredor");
                        intent.putExtra("emailRemetente", emailRemetente);
                        intent.putExtra("nomeRemetente", nomeRemetente);
                        intent.putExtra("profPicRemetente", profPicRemetente);
                        //   intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        PendingIntent pendingIntent = PendingIntent.getActivity(this, count, intent,
                                PendingIntent.FLAG_ONE_SHOT);


                        sendNotificationNoSound(nomeRemetente, corpoMsg, bundle.getString("email"), pendingIntent, this.getResources().getColor(R.color.blue), R.drawable.ic_chat);
                        count++;

                    } else {
                        //Atualizar lista
                        chatAtivo = true;

                    }


                } else {
                    //Com som
                    Log.i(TAG, "NOTIFICALUNO");

                    Intent intent = new Intent(this, ChatActivity.class);
                    intent.putExtra("origem", "notificacaoCorredor");
                    intent.putExtra("emailRemetente", emailRemetente);
                    intent.putExtra("nomeRemetente", nomeRemetente);
                    intent.putExtra("profPicRemetente", profPicRemetente);
                    //   intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    PendingIntent pendingIntent = PendingIntent.getActivity(this, count, intent,
                            PendingIntent.FLAG_ONE_SHOT);
                    sendNotification(nomeRemetente, corpoMsg, bundle.getString("email"), pendingIntent, this.getResources().getColor(R.color.blue), R.drawable.ic_chat);
                    count++;

                }

            }

            //Atualiza servidor para "Recebido"
            AtualizaMsgRecebidaTask task = new AtualizaMsgRecebidaTask(this, prefs.getString("email", ""), "corredor", chatAtivo);
            task.execute();


        } else if (prefs.getString("perfil", "").equals("treinador")) {

            List<Bundle> bundles = json.JsonToNotificacoesTreinador(message);

            for (Bundle bundle : bundles) {

                //Cria Notificacao
                String profPicRemetente = bundle.getString("profPic", "");
                String nomeRemetente = bundle.getString("nome");
                String emailRemetente = bundle.getString("email", "");
                String corpoMsg = getResources().getString(R.string.corpo_notificacao_chat1)  + " " +
                        bundle.getString("qtdMsg") + " " + getResources().getString(R.string.corpo_notificacao_chat2);

                if (prefsChat.getBoolean("chatAtivo", false)) {

                    if (!prefsChat.getString("chatDestinatario", "").equals(emailRemetente)) {

                        //Sem som
                        Log.i(TAG, "NOTIFICTREINADOR");
                        Intent intent = new Intent(this, ChatActivity.class);
                        intent.putExtra("origem", "notificacaoTreinador");
                        intent.putExtra("emailRemetente", emailRemetente);
                        intent.putExtra("nomeRemetente", nomeRemetente);
                        intent.putExtra("profPicRemetente", profPicRemetente);
                        //   intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        String aux = emailRemetente + nomeRemetente;
                        int auxId = aux.length();

                        PendingIntent pendingIntent = PendingIntent.getActivity(this, auxId, intent,
                                PendingIntent.FLAG_UPDATE_CURRENT);


                        sendNotificationNoSound(nomeRemetente, corpoMsg, emailRemetente, pendingIntent, this.getResources().getColor(R.color.blue), R.drawable.ic_chat);


                    } else {
                        //Atualizar Lista
                        chatAtivo = true;
                        //updateChatActivity(this, null);
                    }
                } else {
                    //Com som
                    Log.i(TAG, "NOTIFICTREINADOR");

                    Intent intent = new Intent(this, ChatActivity.class);
                    intent.putExtra("origem", "notificacaoTreinador");
                    intent.putExtra("emailRemetente", emailRemetente);
                    intent.putExtra("nomeRemetente", nomeRemetente);
                    intent.putExtra("profPicRemetente", profPicRemetente);
                    //   intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    String aux = emailRemetente + nomeRemetente;
                    int auxId = aux.length();

                    PendingIntent pendingIntent = PendingIntent.getActivity(this, auxId, intent,
                            PendingIntent.FLAG_UPDATE_CURRENT);
                    sendNotification(nomeRemetente, corpoMsg, emailRemetente, pendingIntent, this.getResources().getColor(R.color.blue), R.drawable.ic_chat);


                }

            }

            //Atualiza servidor para "Recebido"
            AtualizaMsgRecebidaTask task = new AtualizaMsgRecebidaTask(this, prefs.getString("email", ""), "treinador", chatAtivo);
            task.execute();

        }


    }

    private void onMessageTesteAgendado() {
        SharedPreferences prefs = getSharedPreferences("Configuracoes", Context.MODE_PRIVATE);

        if (prefs.getString("perfil", "").equals("corredor")) {

            String titulo = "4Runner";
            String corpoMsg = getResources().getString(R.string.corpo_notificacao_teste_agendado);

            Intent intent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                    PendingIntent.FLAG_ONE_SHOT);

            //Abrir na tab meu treinador
            sendNotification(titulo, corpoMsg, "testeAgendado", pendingIntent,this.getResources().getColor(R.color.yellow), R.drawable.ic_calendar_text_white_48dp);

        }
    }

    private void sendNotification(String titulo, String message, String tag, PendingIntent pendingIntent,int cor, int icone) {

        Log.i(TAG, "Entrou na notific");

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(icone)
                .setColor(cor)
                .setContentTitle(titulo)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(tag, 0, notificationBuilder.build());

    }

    private void sendNotificationNoSound(String titulo, String message, String tag, PendingIntent pendingIntent,int cor, int icone) {

        Log.i(TAG, "Entrou na notific");

        //    Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(icone)
                .setColor(cor)
                .setContentTitle(titulo)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(null)
                .setVibrate(null)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(tag, 0, notificationBuilder.build());

    }


}
