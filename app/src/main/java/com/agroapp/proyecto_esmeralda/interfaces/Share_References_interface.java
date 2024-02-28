package com.agroapp.proyecto_esmeralda.interfaces;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public interface Share_References_interface<T> {

    String farm_name(SharedPreferences preference);
    String user_type( SharedPreferences preference);
    String id_subasta( SharedPreferences preference);
    String tipo( SharedPreferences preference);
    String id_propietario( SharedPreferences preference);
    String id_usuario( SharedPreferences preference);
    String user_name(SharedPreferences preference);
    String paddock_name(SharedPreferences preference);
    ProgressDialog progressDialog_cargando(Context context);
    ProgressDialog progressDialog_fallo(Context context);
    int nivel_acceso(SharedPreferences preferences);

    CollectionReference[] referencedb_c(String id_propietario,String farm_name,String id_animal);
    DocumentReference[] referencedb_d(String id_propietario,String farm_name,String id_animal);
    void fincas(SharedPreferences preferences, ArrayList fincas);
    String animal_name(SharedPreferences preference);
    String cedula(SharedPreferences preference);
    String id_animal(SharedPreferences preference);
    int[] date_picker();
    int[] parse_date(String date);
    void next_Intent(Context context, Class<T> tClass);
    public void count_cant_animals_adult(String id_propietario,String farm_name, String type_animal, String etapa_tipo, TextView textView);
    void spinner_farm(Spinner spinner_fam,  Context context, TextView tv_farm, String id_usuario );
    void count_cant_toros_animal(String id_propietario ,String farm_name, String type_animal, String etapa_tipo, TextView textView, String genero);
    void home_data_farm( String id_propietario, String farm_name, TextView tv_extension, TextView tv_ubicacion);
    void count_cant_type_animal(String id_propietario ,String farm_name, String type_animal, String etapa, TextView textView);
    void count_cant_animales(String id_propietario , String farm_name, TextView textView);

}
