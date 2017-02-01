package com.team4runner.forrunner.Postergado_NaoUsado;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

/**
 * Created by Lucas on 01/12/2015.
 */
public class OldGCM {
    private static final String TAG = "gcm";
    public static final String PROPERTY_REG_ID = "registration_id";

    // Preferncias para salvar o registration id
    private static SharedPreferences getGCMPreferences(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Configuracoes", Context.MODE_PRIVATE);
        return sharedPreferences;
    }

    // Retorna o registration id salvo nas prefs
    public static String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId == null || registrationId.trim().length() == 0) {
            return null;
        }
        return registrationId;
    }

    // Salva o registration id nas prefs
    private static void saveRegistrationId(Context context, String registrationId) {
        final SharedPreferences prefs = getGCMPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, registrationId);
        editor.commit();
    }

    // Faz o registro no GCM
    public static String register(Context context, String projectNumber) {
        //GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
        InstanceID instanceID = InstanceID.getInstance(context);
        try {
            Log.d(TAG, ">> GCM.registrar(): " + projectNumber);
            //String registrationId = gcm.register(projectNumber);
            String registrationId = instanceID.getToken(projectNumber,
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE,
                    null);

            if(registrationId != null) {
                // Salva nas prefs
                saveRegistrationId(context,registrationId);
            }

            Log.d(TAG, "<< GCM.registrar() OK, registration id: " + registrationId);
            return registrationId;
        } catch (IOException e) {
            Log.e(TAG, "<< GCM.registrar() ERRO: " + e.getMessage(), e);
        }
        return null;
    }

    // Cancelar o registro no GCM
    public static void unregister(Context context) {
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
        try {
            gcm.unregister();
            saveRegistrationId(context, null);
            Log.d(TAG, "GCM cancelado com sucesso.");
        } catch (IOException e) {
            Log.e(TAG, "GCM erro ao desregistrar: " + e.getMessage(), e);
        }
    }
}
