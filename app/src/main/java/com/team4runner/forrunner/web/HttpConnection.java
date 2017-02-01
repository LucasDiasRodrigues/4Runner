package com.team4runner.forrunner.web;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by Lucas on 03/06/2015.
 */
public class HttpConnection {

    public static String getSetDataWeb(String url, String method, String data) {
        String answer = "";

        try {
            URL endereco = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) endereco.openConnection();

            conn.setRequestMethod("POST");
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(5000);
            //was 30*1000 - changed 01/04/2016
            //conn.setRequestProperty("Content-Type", "application/json");
            //conn.setRequestProperty("Content-Length", Integer.toString(data.length()));
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.connect();


            //Enviar
            String urlParameters =
                    "method=" + URLEncoder.encode(method, "UTF-8") +
                            "&json=" + URLEncoder.encode(data, "UTF-8");

            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            //Receber
            int resposta = conn.getResponseCode();
            if (resposta == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                answer = bytesToString(is);
            }

        } catch (SocketTimeoutException ex) {
            ex.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return answer;
    }

    public static String bytesToString(InputStream is) throws IOException {

        byte[] buffer = new byte[1024];
        ByteArrayOutputStream bigBuffer = new ByteArrayOutputStream();
        int bytesLidos;
        while ((bytesLidos = is.read(buffer)) != -1) {
            bigBuffer.write(buffer, 0, bytesLidos);
        }
        return new String(bigBuffer.toByteArray(), "UTF-8");
    }


    public static boolean isNetworkAvailable(Context context) {

        ConnectivityManager connectivity = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivity.getActiveNetworkInfo();

        if (info != null && info.isConnected()) {
            return true;
        } else {
            return false;
        }
    }
}
