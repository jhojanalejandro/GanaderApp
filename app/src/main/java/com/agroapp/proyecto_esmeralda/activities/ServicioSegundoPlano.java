package com.agroapp.proyecto_esmeralda.activities;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.work.ListenableWorker;


import com.agroapp.proyecto_esmeralda.modelos.Palpacion_Model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class ServicioSegundoPlano extends Service {
    ArrayList<Palpacion_Model> list_animal;
    Context context = this;
    MediaPlayer mediaPlayer;
    int dia_hoy, mes_hoy, ano_hoy;

    @Override
    public void onCreate() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        datepikers_hoy();
        if (Probar_connexion.Prueba(context)) {


        }

        return super.onStartCommand(intent, flags, startId);
    }


    public void Builder(Class<? extends ListenableWorker> workerClass, long repeatInterval, TimeUnit repeatIntervalTimeUnit, long flexInterval, TimeUnit flexIntervalTimeUnit){

    }


    private   int[] datepikers_hoy() {
        Calendar calendarNow = new GregorianCalendar(TimeZone.getTimeZone("Europe/Madrid"));
        mes_hoy = calendarNow.get(Calendar.MONTH) + 1;
        ano_hoy = calendarNow.get(Calendar.YEAR);
        dia_hoy = calendarNow.get(Calendar.DAY_OF_MONTH) ;
        return new int[]{dia_hoy, mes_hoy, ano_hoy};

    }


    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
