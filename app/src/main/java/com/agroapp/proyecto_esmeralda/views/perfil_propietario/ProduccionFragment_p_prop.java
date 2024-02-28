package com.agroapp.proyecto_esmeralda.views.perfil_propietario;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import com.agroapp.proyecto_esmeralda.R;

import com.agroapp.proyecto_esmeralda.modelos.Animal_Model;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class ProduccionFragment_p_prop extends Fragment {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    SharedPreferences preferences;
    int dia,mes,ano;
    View view;
    Animal_Model modelo_animal;
    String n_finca,animal;
    ArrayList<Animal_Model> list_animales,list_animales_baja;
    private ListView lv_animales_alta,lv_animales_alta_baja;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

         view = inflater.inflate(R.layout.fragment_produccion_p_prop, container, false);
        BottomNavigationView bottomNavigationView = view.findViewById(R.id.nav_view_animales_general);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        iniciar_variables();
        datepikers_hoy();
        validar_finca_n();

        return view;
    }
    private void iniciar_variables(){


        list_animales = new ArrayList();
        list_animales_baja = new ArrayList();


    }
    private void datepikers_hoy( ) {
        Calendar calendarNow = new GregorianCalendar(TimeZone.getTimeZone("Europe/Madrid"));
        dia =calendarNow.get(Calendar.DAY_OF_MONTH);
        mes = calendarNow.get(Calendar.MONTH)+1;
        ano = calendarNow.get(Calendar.YEAR);
    }

    public     BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {


            return true;
        }
    };
    private String validar_finca_n(){
        preferences = getContext().getSharedPreferences("preferences", Context.MODE_PRIVATE);
        n_finca = preferences.getString("finca",null);
        if ( n_finca!=null){
            return n_finca;
        }else {
            return  null;
        }
    }

}