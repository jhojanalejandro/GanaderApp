package com.agroapp.proyecto_esmeralda.views.perfil_propietario;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.agroapp.proyecto_esmeralda.R;
import com.agroapp.proyecto_esmeralda.interfaces.Share_References_interface;
import com.agroapp.proyecto_esmeralda.modelos.Animal_Model;
import com.agroapp.proyecto_esmeralda.modelos.Produccion_Model;
import com.agroapp.proyecto_esmeralda.modelos.Finca_model;

import com.agroapp.proyecto_esmeralda.controlador.Share_References_presenter;
import com.google.firebase.firestore.FirebaseFirestore;

public class Inicio_Perfil extends Fragment {

    private static final String TAG = "un tag" ;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    String  farm_name, id_propietario;
    private final String tipo_animal_toro = "macho";
    private final String etapa_animal_ternera = "inicial";
    private final String tipo_animal_novillona = "media";
    private final String tipo_animal_bovino = "bovino";
    private final String tipo_animal_equino = "equino";
    View view;
    Animal_Model animales_model;

    Finca_model finca_model;
    Produccion_Model ficha_produccion;
    SharedPreferences preferences;
    TextView tv_cant_animales,tv_cant_equinos,tv_nombre_finca,tv_cant_terneras,tv_cant_novillas,tv_cant_toros,tv_cant_vacas,tv_extension,tv_ubicacion;
    int dia,mes,ano;
    Share_References_interface share_references_interface;




    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

         view = inflater.inflate(R.layout.fragment_pagos_p_prop, container, false);

        iniciar_variables();
        farm_name = share_references_interface.farm_name(preferences);
        id_propietario = share_references_interface.id_propietario(preferences);
        if (farm_name != null){
            share_references_interface.home_data_farm( id_propietario,farm_name, tv_extension, tv_ubicacion);
            share_references_interface.count_cant_type_animal( id_propietario,farm_name, tipo_animal_bovino,tipo_animal_novillona, tv_cant_novillas);
            String etapa_animal_vacas = "productiva";
            share_references_interface.count_cant_type_animal( id_propietario,farm_name, tipo_animal_bovino, etapa_animal_vacas, tv_cant_novillas);
            share_references_interface.count_cant_type_animal(id_propietario, farm_name, tipo_animal_bovino,etapa_animal_ternera, tv_cant_terneras);
            share_references_interface.count_cant_type_animal(id_propietario ,farm_name, tipo_animal_bovino, etapa_animal_vacas, tv_cant_vacas);
            share_references_interface.count_cant_type_animal(id_propietario ,farm_name, tipo_animal_bovino, etapa_animal_vacas, tv_cant_toros);
            share_references_interface.count_cant_animales(id_propietario, farm_name, tv_cant_animales);
            tv_nombre_finca.setText(farm_name);
        }

        return view;
    }

    private  void  iniciar_variables(){
        tv_extension = view.findViewById(R.id.tv_datos_p_prop_finca_extension);
        tv_cant_toros = view.findViewById(R.id.tv_p_prop_estad_cant_toros);
        tv_cant_novillas = view.findViewById(R.id.tv_p_prop_estad_cant_novillos);
        tv_nombre_finca = view.findViewById(R.id.tv_datos_p_prop_finca_nombre);
        tv_ubicacion = view.findViewById(R.id.tv_datos_p_prop_finca_ubicacion);
        tv_cant_terneras= view.findViewById(R.id.tv_p_prop_cant_crias);
        tv_cant_animales = view.findViewById(R.id.tv_det_p_prop_estad_cant_animales);
        tv_cant_equinos = view.findViewById(R.id.tv_det_p_prop_estad_cant_equinos);
        tv_cant_vacas = view.findViewById(R.id.tv_det_p_prop_cant_vacas);
        preferences = getContext().getSharedPreferences("preferences", getContext().MODE_PRIVATE);
        share_references_interface = new Share_References_presenter(getContext());


    }


}