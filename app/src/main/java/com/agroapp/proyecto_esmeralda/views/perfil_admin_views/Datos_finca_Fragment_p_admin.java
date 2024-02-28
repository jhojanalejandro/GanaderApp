package com.agroapp.proyecto_esmeralda.views.perfil_admin_views;

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
import com.agroapp.proyecto_esmeralda.controlador.Share_References_presenter;
import com.google.firebase.firestore.FirebaseFirestore;


public class Datos_finca_Fragment_p_admin extends Fragment {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String farm_name, id_propietario, etapa_animal_vacas = "productiva",cant_animales,genero_animal_toro = "macho",etapa_animal_ternera = "inicial",tipo_animal_novillona = "media",tipo_animal_bovino = "bovino",tipo_animal_equino = "equino",extesion,capacidad;
    View view;
    private int cant_novillas = 0, cant_equinos, cant_vacas,cant_terneras = 0, cant_toros = 0;
    SharedPreferences preferences;
    Share_References_interface  share_references_interface;

    TextView tv_cant_animales,tv_cant_equinos,tv_cant_terneras,tv_cant_novillas,tv_cant_toros,tv_cant_vacas,tv_finca,tv_extension,tv_ubicacion;
    int dia,mes,ano;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_datos_finca_p_admin, container, false);
        iniciar_variables();


        farm_name = share_references_interface.farm_name(preferences);
        id_propietario = share_references_interface.id_propietario(preferences);
        share_references_interface.home_data_farm( id_propietario,farm_name, tv_extension, tv_ubicacion);
        share_references_interface.count_cant_type_animal(id_propietario,farm_name, tipo_animal_bovino,tipo_animal_novillona, tv_cant_novillas);
        share_references_interface.count_cant_type_animal(id_propietario, farm_name, tipo_animal_bovino,etapa_animal_ternera, tv_cant_terneras);
        share_references_interface.count_cant_type_animal(id_propietario, farm_name, tipo_animal_equino,etapa_animal_vacas, tv_cant_equinos);

        share_references_interface.count_cant_animals_adult(id_propietario, farm_name, tipo_animal_bovino,etapa_animal_vacas, tv_cant_vacas);
        share_references_interface.count_cant_toros_animal(id_propietario, farm_name, tipo_animal_bovino,etapa_animal_vacas, tv_cant_toros,"macho");
        share_references_interface.count_cant_animales(id_propietario, farm_name, tv_cant_animales);

        cant_equinos = Integer.parseInt(tv_cant_equinos.getText().toString().trim());
        cant_vacas = Integer.parseInt(tv_cant_vacas.getText().toString().trim());
        cant_terneras = Integer.parseInt(tv_cant_terneras.getText().toString().trim());
        cant_toros = Integer.parseInt(tv_cant_toros.getText().toString().trim());
        cant_novillas = Integer.parseInt(tv_cant_novillas.getText().toString().trim());
        tv_finca.setText(farm_name);
        return view;
    }

    private  void  iniciar_variables(){
        tv_ubicacion = view.findViewById(R.id.tv_datos_p_admin_finca_ubi_prod);
        tv_extension = view.findViewById(R.id.tv_datos_p_admin_finca_extension);
        tv_cant_toros = view.findViewById(R.id.tv_p_admin_estad_cant_toros);
        tv_cant_novillas = view.findViewById(R.id.tv_p_admin_estad_cant_novillos);
        tv_cant_terneras= view.findViewById(R.id.tv_p_admin_cant_crias);
        tv_finca = view.findViewById(R.id.tv_datos_p_admin_finca_nombre);
        tv_cant_animales = view.findViewById(R.id.tv_det_p_admin_estad_cant_animales);
        tv_cant_equinos = view.findViewById(R.id.tv_det_p_admin_estad_cant_equinos);
        tv_cant_vacas = view.findViewById(R.id.tv_det_p_admin_cant_vacas);
        share_references_interface = new Share_References_presenter(getContext());
        preferences = getContext().getSharedPreferences("preferences", getContext().MODE_PRIVATE);

    }

}