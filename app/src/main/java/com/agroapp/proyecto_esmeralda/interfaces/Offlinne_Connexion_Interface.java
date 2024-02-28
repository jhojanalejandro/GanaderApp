package com.agroapp.proyecto_esmeralda.interfaces;

import android.content.Context;

import com.agroapp.proyecto_esmeralda.modelos.Animal_Model;
import com.agroapp.proyecto_esmeralda.modelos.Gastos_Insumos;
import com.agroapp.proyecto_esmeralda.modelos.Parto_Model;
import com.agroapp.proyecto_esmeralda.modelos.Vacunacion_Model;
import com.agroapp.proyecto_esmeralda.modelos.Emfermedad_Model;
import com.agroapp.proyecto_esmeralda.modelos.Medida_Leche_Model;
import com.agroapp.proyecto_esmeralda.modelos.Palpacion_Model;
import com.agroapp.proyecto_esmeralda.modelos.Pesaje_Animal_Model;
import com.agroapp.proyecto_esmeralda.modelos.Servicio_Model;

import java.util.ArrayList;

public interface Offlinne_Connexion_Interface<T> {

    void openR_data_type_offline();
    void close_data_type_offline();
    ArrayList<Palpacion_Model> show_data_palpation_animal(Context context);
    ArrayList<Parto_Model> show_data_part_animal(Context context);
    ArrayList<Gastos_Insumos> show_data_dry_animal(Context context);
    ArrayList<Emfermedad_Model> show_data_deseas_animal(Context context);
    ArrayList<Pesaje_Animal_Model> show_data_weighing_animal(Context context);
    ArrayList<Medida_Leche_Model> show_data_measur_animal(Context context);
    ArrayList<Servicio_Model> show_data_hot_animal(Context context);
    ArrayList<Vacunacion_Model> show_data_vaccination_animal(Context context);
    void openW_data_type_offline();
    int delet_data_type_animal(int id, String wher, String table_name);
    int data_anmls_register_offline(Context context, Animal_Model animal_model, String id_propietario);
    void update_data_measur_animal(Palpacion_Model per);
    int data_type_anmls_register_offline( Context context ,int anml_r_cant_dias, Double anml_r_result_pm, Double anml_r_resul_am, Double peso,String anmls_r_date_noti, String anmls_r_observations, String anmls_r_treatment, String anmls_r_vet_name, String anmls_r_farm_name, int anmls_r_cant_drog, String anmls_r_result, String anmls_r_type, String anmls_r_event, String anmls_r_date, String anmls_r_number_breeding, int anmls_r_number_part, String anmls_r_father_name, String anmls_result_real, String anmls_r_drog_applied, String anmls_r_type_choice, String anmls_r_bull_name, String id_animal, String id_pro);

}
