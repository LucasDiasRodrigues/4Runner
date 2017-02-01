package com.team4runner.forrunner.Postergado_NaoUsado;

/**
 * Created by Lucas on 03/12/2015.
 */
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.TaskStackBuilder;

import com.team4runner.forrunner.R;

import java.util.List;

public class NotificationUtil {

    public static final String ACTION_VISUALIZAR = "br.com.livroandroid.hellonotification.ACTION_VISUALIZAR";

    private static PendingIntent getPendingIntent(Context context, Intent intent, int id) {
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(intent.getComponent());
        stackBuilder.addNextIntent(intent);
        PendingIntent p = stackBuilder.getPendingIntent(id, PendingIntent.FLAG_UPDATE_CURRENT);
        return p;
    }

    public static void create(Context context, Intent intent, String contentTitle, String contentText, int id) {
        PendingIntent p = getPendingIntent(context, intent, id);

        // Cria a notificacao
        NotificationCompat.Builder b = new NotificationCompat.Builder(context);
        b.setDefaults(Notification.DEFAULT_ALL); // Ativa configura��es padr�o
        b.setSmallIcon(R.drawable.ic_account_grey600_48dp); // �cone
        b.setContentTitle(contentTitle); // T�tulo
        b.setContentText(contentText); // Mensagem
        b.setContentIntent(p); // Intent que ser� chamada ao clicar na notifica��o.
        b.setAutoCancel(true); // Auto cancela a notifica��o ao clicar nela

        b.setColor(Color.GREEN);

        NotificationManagerCompat nm = NotificationManagerCompat.from(context);
        nm.notify(id, b.build());
    }

    public static void createBig(Context context, Intent intent, String contentTitle, String contentText,List<String> lines, int id) {
        PendingIntent p = getPendingIntent(context, intent, id);

        // Configura o estilo Inbox
        int size = lines.size();
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.setBigContentTitle(contentTitle);
        for (String s: lines) {
            inboxStyle.addLine(s);
        }
        inboxStyle.setSummaryText(contentText);

        // Cria a notifica��o
        NotificationCompat.Builder b = new NotificationCompat.Builder(context);
        b.setDefaults(Notification.DEFAULT_ALL); // Ativa configura��es padr�o
        b.setSmallIcon(R.drawable.ic_account_grey600_48dp); // �cone
        b.setContentTitle(contentTitle); // T�tulo
        b.setContentText(contentText); // Mensagem
        b.setContentIntent(p); // Intent que ser� chamada ao clicar na notifica��o.
        b.setAutoCancel(true); // Auto cancela a notifica��o ao clicar nela
        b.setNumber(size); // N�mero para aparecer na notifica��o
        b.setStyle(inboxStyle); // Estilo customizado

        NotificationManagerCompat nm = NotificationManagerCompat.from(context);
        nm.notify(id, b.build());
    }

    public static void createWithAction(Context context, Intent intent, String contentTitle, String contentText, int id) {
        PendingIntent p = getPendingIntent(context, intent, id);

        // Cria a notifica��o
        NotificationCompat.Builder b = new NotificationCompat.Builder(context);
        b.setDefaults(Notification.DEFAULT_ALL); // Ativa configura��es padr�o
        b.setSmallIcon(R.drawable.ic_account_grey600_48dp); // �cone
        b.setContentTitle(contentTitle); // T�tulo
        b.setContentText(contentText); // Mensagem
        b.setContentIntent(p); // Intent que ser� chamada ao clicar na notifica��o.
        b.setAutoCancel(true); // Auto cancela a notifica��o ao clicar nela

        // A��o customizada (deixei a mesma intent para os dois)
      //  PendingIntent actionIntent = PendingIntent.getBroadcast(
        //        context, 0, new Intent(ACTION_VISUALIZAR), 0);
        //b.addAction(R.drawable.ic_acao_pause, "Pause", actionIntent);
        //b.addAction(R.drawable.ic_acao_play, "Play", actionIntent);

        NotificationManagerCompat nm = NotificationManagerCompat.from(context);
        nm.notify(id, b.build());
    }

    // Notification no Android 5.0 Lollipop (Cor vermelha e heads-up e tela de bloqueio)
    public static void createHeadsUpNotification(Context context, Intent intent, String contentTitle, String contentText, int id) {
        PendingIntent p = getPendingIntent(context, intent, id);

        // Cria a notifica��o
        NotificationCompat.Builder b = new NotificationCompat.Builder(context);
        b.setDefaults(Notification.DEFAULT_ALL); // Ativa configura��es padr�o
        b.setSmallIcon(R.drawable.ic_account_grey600_48dp); // �cone
        b.setContentTitle(contentTitle); // T�tulo
        b.setContentText(contentText); // Mensagem
        b.setContentIntent(p); // Intent que ser� chamada ao clicar na notifica��o.
        b.setAutoCancel(true); // Auto cancela a notifica��o ao clicar nela

        // No Android 5.0
        b.setColor(Color.RED);
        // Heads-up notification
        b.setFullScreenIntent(p,false);
        // Privada se estiver na tela de bloqueio
        b.setVisibility(NotificationCompat.VISIBILITY_PRIVATE);

        NotificationManagerCompat nm = NotificationManagerCompat.from(context);
        nm.notify(id, b.build());
    }

    // Notification no Android 5.0 Lollipop (tela de bloqueio)
    public static void createPrivateNotification(Context context, Intent intent, String contentTitle, String contentText, int id) {
        PendingIntent p = getPendingIntent(context, intent, id);

        // Cria a notifica��o
        NotificationCompat.Builder b = new NotificationCompat.Builder(context);
        b.setDefaults(Notification.DEFAULT_ALL); // Ativa configura��es padr�o
        b.setSmallIcon(R.drawable.ic_account_grey600_48dp); // �cone
        b.setContentTitle(contentTitle); // T�tulo
        b.setContentText(contentText); // Mensagem
        b.setContentIntent(p); // Intent que ser� chamada ao clicar na notifica��o.
        b.setAutoCancel(true); // Auto cancela a notifica��o ao clicar nela

        // PUBLIC, PRIVATE, SECRET
        b.setVisibility(NotificationCompat.VISIBILITY_SECRET);

        NotificationManagerCompat nm = NotificationManagerCompat.from(context);
        nm.notify(id, b.build());
    }

    public static void cancell(Context context, int id) {
        NotificationManagerCompat nm = NotificationManagerCompat.from(context);
        nm.cancel(id);
    }

    public static void cancellAll(Context context) {
        NotificationManagerCompat nm = NotificationManagerCompat.from(context);
        nm.cancelAll();
    }
}