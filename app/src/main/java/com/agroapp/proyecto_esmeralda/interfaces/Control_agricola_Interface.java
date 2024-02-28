package com.agroapp.proyecto_esmeralda.interfaces;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.ListView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public interface Control_agricola_Interface  {

    void notify_drying();
    void notify_part();
    void consultar_parto_animales_primer_periodo( int mes, int ano);
    void consultar_parto_animales_segundo_periodo( int mes, int ano);
    void create_channel_notify();
    void consultar_secado_animales_primer_periodo( int mes, int ano);
    void consultar_secado_animales_segundo_periodo( int mes, int ano);
    void show_dry( int mes, int ano,RecyclerView recyclerView, LinearLayoutManager layoutManager);
    void show_part( int mes, int ano,RecyclerView recyclerView, LinearLayoutManager layoutManager);

}
