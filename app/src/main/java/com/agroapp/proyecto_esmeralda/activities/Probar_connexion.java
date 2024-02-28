package com.agroapp.proyecto_esmeralda.activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.agroapp.proyecto_esmeralda.modelos.Insumo_Finca_Model;
import com.agroapp.proyecto_esmeralda.modelos.Produccion_Model;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Probar_connexion extends AppCompatActivity {
    View view;
    private Boolean boleano_insumos = true;
    private int dia_hoy, mes_hoy, ano_hoy, cant_droga, cant_restante, dia, mes, ano;

    private String fecha_palp_noti, fecha_palp, cant_droga_s, n_finca, droga, n_empleado, nombre_animal, tipo_palpacion = "palpacion", tipo_calor = "calor";
    private SharedPreferences preferences;
    private Spinner spinner_insumos, spinner_f_calor;
    private Produccion_Model ficha_produccion;
    private List<String> insumos;
    Context context;
    private ProgressDialog progressBar;
    Insumo_Finca_Model insumo_finca;
    private DatePickerDialog datePickerDialog, datePickerDialog_noti;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    public static boolean Prueba(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager)  context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (connectivityManager != null){
            if (networkInfo != null && networkInfo.isConnected()) {
                return  networkInfo.isConnected();
            }

        }

        return false ;


    }



    public int[] parsea_Fecha(String fechaEntera) {
        int day = 0, month = 0, year = 0;
        try {
            //transforma la cadena en un tipo date
            Date miFecha = new SimpleDateFormat("dd/MM/yyyy").parse(fechaEntera);

            //creo un calendario
            Calendar calendario = Calendar.getInstance();
            //establezco mi fecha
            calendario.setTime(miFecha);

            //obtener el año
            year = calendario.get(Calendar.YEAR);
            //obtener el mes (0-11 ::: enero es 0 y diciembre es 11)
            month = calendario.get(Calendar.MONTH) + 1;
            //obtener el dia del mes (1-31)
            day = calendario.get(Calendar.DAY_OF_MONTH);

            //...mas campos...

        } catch (ParseException ex) {
            //manejar excepcion
        }
        return new int[]{day, month, year};
    }

}
