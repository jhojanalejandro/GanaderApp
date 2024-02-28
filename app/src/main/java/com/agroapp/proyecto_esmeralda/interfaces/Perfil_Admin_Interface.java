package com.agroapp.proyecto_esmeralda.interfaces;

import android.content.Context;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.agroapp.proyecto_esmeralda.modelos.Produccion_Model;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public interface Perfil_Admin_Interface {
    void show_data_type_animals_on(  String data_type, TextView tv_type, int anio);
    void show_data_type_animals_out( String data_type, TextView tv_type, int anio);
    void count_animals( String lote, String etapa_tipo, TextView tv_count_animal);
    void production_consult_weigh(  int mes, TextView tv_cant_animales,TextView tv_kilos, TextView tv_fecha);
    void show_birth_birthdeath_type_animals( String type, TextView tv_type, int anio) ;
    void mostrar_analizis_anual( TextView tv_ganancias, TextView tv_gasto,  TextView kilos_producidos, TextView tv_leche_producida, int anio);
    String consult_tipo_produccion();
    void lista_produccion_mes( LinearLayoutManager layoutManager);

    void consultar_palpapcion_parto(RecyclerView recyclerView, LinearLayoutManager layoutManager, int mes, int ano);
    void show_palpations( RecyclerView recyclerView, LinearLayoutManager layoutManager, int mes, int ano);

    void show_all_cattle( RecyclerView recyclerView, EditText edt_search, LinearLayoutManager layoutManager);
    void mostrar_estadisticas_mensual( String id_propietario,TextView tv_ganancias, TextView tv_gasto, TextView tv_litros_medidos, TextView tv_promedio_leche, TextView tv_promedio_dia , TextView tv_promedio_carne, TextView kilos_producidos, TextView tv_ledche_producida, int mes, int anio, TextView tv_estado_leche, TextView tv_estado_carne, TextView tv_animales_medidos, TextView tv_animales_pesados, TextView tv_fecha);
}
