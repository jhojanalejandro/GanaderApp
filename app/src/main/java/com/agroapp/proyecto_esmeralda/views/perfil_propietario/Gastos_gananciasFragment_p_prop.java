package com.agroapp.proyecto_esmeralda.views.perfil_propietario;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import com.agroapp.proyecto_esmeralda.R;
import com.agroapp.proyecto_esmeralda.modelos.Produccion_Model;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import static android.content.Context.MODE_PRIVATE;

public class Gastos_gananciasFragment_p_prop extends Fragment {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Produccion_Model ficha_produccion;

    private String n_finca;
    SharedPreferences preferences;
    int dia,mes,ano;
    ArrayList<Produccion_Model> list_prod;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_gallery_p_prop, container, false);
        ficha_produccion = new Produccion_Model();
        list_prod = new ArrayList<>();
        datepikers_hoy();
        validar_finca_n();


        return view;
    }


    private String validar_finca_n(){
        preferences = getContext().getSharedPreferences("preferences", Context.MODE_PRIVATE);
        n_finca = preferences.getString("finca",null);
        if ( n_finca!=null){
            return n_finca;
        }else {
            return  null;
        }
    }

    private void consultar_produccion_anterior(){
        int meses = mes- 1;
        final DocumentReference uno = db.collection("fincas").document(n_finca);
        final DocumentReference coreff = uno.collection("produccion").document(String.valueOf(meses));
        coreff.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    ficha_produccion = documentSnapshot.toObject(Produccion_Model.class);
                }

            }
        });
    }
    private void datepikers_hoy( ) {
        Calendar calendarNow = new GregorianCalendar(TimeZone.getTimeZone("Europe/Madrid"));
        dia =calendarNow.get(Calendar.DAY_OF_MONTH);
        mes = calendarNow.get(Calendar.MONTH)+1;
        ano = calendarNow.get(Calendar.YEAR);
    }

}