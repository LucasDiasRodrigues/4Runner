package com.team4runner.forrunner.Postergado_NaoUsado;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Lucas on 03/12/2015.
 */
public class OldGcmBroadcastReceiver extends com.google.android.gms.gcm.GcmReceiver {
    private static final String TAG = "gcm";

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i("ReceiverAtivado","Receiver");

        ComponentName comp = new ComponentName(context.getPackageName(), GcmIntentService.class.getName());
        startWakefulService(context, (intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);

    }
}
