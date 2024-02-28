package com.agroapp.proyecto_esmeralda.interfaces;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.agroapp.proyecto_esmeralda.adapters.Gasto_Insumos_Recycle_Anmls_Adapter;

import com.agroapp.proyecto_esmeralda.modelos.Gastos_Insumos;
import com.agroapp.proyecto_esmeralda.modelos.Parto_Model;

import java.util.ArrayList;

public interface Perfil_Animal_Interface<T> {

    void show_measur_animal( TextView textView, int day ,int month, int year );
    void show_weigh_animal(TextView textView, int dia, int mes, int ano);
    void update_photo( String url , ProgressDialog progressDialog);
    int data_measur_anmls_register( int dia, int mes, int ano, String measur_date, String obs_measur, Double leche, Double results_leche_am, Boolean conexion);
    int data_anmls_part_register( String anmls_r_drog_applied, int anmls_r_cant_drog, String id_propietario, String farm_name, String id_animal,Boolean conexion, Parto_Model class_name, Double peso,String ternera, String madre, String fecha_hoy);

    String consultar_padre();
    int data_type_anmls_register(String drog, int cant_drog, String id_propietario ,String anmls_r_farm_name, String id_animal, Boolean conexion, Object tObjects);
    int data_weighing_anmls_register( Double peso_traido, String observaciones, String fecha_pesaje,int dia, int mes, int ano, String id_propietario, String anmls_r_farm_name, String id_animal, Boolean conexion, String eleccion_etapa);
    String spinner_services( EditText edt_services, Spinner spinner);
    void life_spend_register(Boolean connexion, Double valor,String ids_animal, String id_propietario ,String finca);
    int[] parse_date(String date);
    void cant_ins_consult(Boolean connexion, String droga, int cant_droga, String id_propietario, String nombre_finca, String id_nimal);
    void mostrar_registros_animal(String id_animal, String type_data, ArrayList<Gastos_Insumos> list_common_data_type, Gasto_Insumos_Recycle_Anmls_Adapter adapter);
    void show_animal_detail( ImageView foto_animal, String id_propietario , String farm_name, String id, TextView tv_raza, TextView tv_estado_animal, TextView tv_date_birt, TextView tv_precio, TextView tv_genero, TextView tv_chapeta, TextView tv_etapa, TextView tv_padre, TextView tv_madre, TextView tv_peso, TextView tv_animal_nanme);
    void save_photo( Uri imageUri,String id_propietario ,String farm_name, String id );
}
