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
import com.agroapp.proyecto_esmeralda.modelos.Produccion_Model;
import com.agroapp.proyecto_esmeralda.controlador.Share_References_presenter;
import com.google.firebase.firestore.FirebaseFirestore;

    public class Analizis_fincaFragment_p_prop extends Fragment {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        private String farm_name,user_name;
        Share_References_interface share_references_interface;
        SharedPreferences preferences;
        int mes,ano;
        View view;
        double items;
        Produccion_Model produccion_model;
        TextView tv_gasto,tv_ganancias,tv_litros,tv_nacidos,tv_muertos;


        public View onCreateView(@NonNull LayoutInflater inflater,
                                 ViewGroup container, Bundle savedInstanceState) {

            view = inflater.inflate(R.layout.fragment_estadisticas_p_prop, container, false);

            iniciar_variables_analizis();
            farm_name = share_references_interface.farm_name(preferences);
            if (farm_name != null){
            }


            return view;
        }
        private void iniciar_variables_analizis(){
            tv_ganancias = view.findViewById(R.id.tv_p_prop_ana_ganacia);
            tv_litros = view.findViewById(R.id.tv_p_prop_ana_cant_litros_ano);
            tv_nacidos = view.findViewById(R.id.tv_det_p_prop_ana_cant_animales_nacidos);
            tv_muertos = view.findViewById(R.id.tv_det_p_prop_ana_cant_animales_muertos);
            tv_gasto = view.findViewById(R.id.tv_p_prop_ana_gasto);
            share_references_interface = new Share_References_presenter(getContext());
            preferences = getContext().getSharedPreferences("preferences", getContext().MODE_PRIVATE);
        }


    }