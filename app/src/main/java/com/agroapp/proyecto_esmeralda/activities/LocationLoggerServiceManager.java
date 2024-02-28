package com.agroapp.proyecto_esmeralda.activities;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Toast;

public class LocationLoggerServiceManager extends BroadcastReceiver {

    public static final String TAG = "LocationLoggerServiceManager";
    @Override
    public void onReceive(Context context, Intent intent) {
        // just make sure we are getting the right intent (better safe than sorry)
            if( "android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
                CountDownTimer countDownTimer = new CountDownTimer(300000, 100000) {
                    public void onTick(long millisUntilFinished) {
                        context.startService(new Intent(context, ServicioSegundoPlano.class));

                        ComponentName comp = new ComponentName(context.getPackageName(), ServicioSegundoPlano.class.getName());
                        ComponentName service = context.startService(new Intent().setComponent(comp));

                        if (null == service){
                            // something really wrong here
                            Log.e("TAG", "Could not start service " + comp.toString());
                        }

                    }

                    public void onFinish() {
                        Toast.makeText(context, "Done", Toast.LENGTH_SHORT).show();

                    }
                }.start();


            } else {
                Log.e("TAG", "Received unexpected intent " + intent.toString());
            }
    }
}