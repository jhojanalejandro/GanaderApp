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
import com.agroapp.proyecto_esmeralda.interfaces.Perfil_Admin_Interface;
import com.agroapp.proyecto_esmeralda.interfaces.Share_References_interface;
import com.agroapp.proyecto_esmeralda.modelos.Produccion_Model;
import com.agroapp.proyecto_esmeralda.modelos.Finca_model;
import com.agroapp.proyecto_esmeralda.controlador.Perfil_Admin_Presenter;
import com.agroapp.proyecto_esmeralda.controlador.Share_References_presenter;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class AnalizisFragment_p_admin extends Fragment {

    private String farm_name,id_propietario;
    SharedPreferences preferences;
    Perfil_Admin_Interface perfil_admin_interface;
    Share_References_interface share_preferences_interface;

    int dia,mes,ano;
    View view;
    Finca_model finca_model;
    Produccion_Model produccion_model;
    TextView tv_gasto,tv_ingresados,tv_ganancias,tv_kilos,tv_litros,tv_nacidos, tv_vendidos,tv_nacidos_muertos,tv_abortos,tv_trasladados, tv_comprados,tv_muertos;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_analizis_p_admin, container, false);

        iniciar_variables_analizis();
        share_preferences_interface = new Share_References_presenter(getContext());

        Calendar calendarNow = new GregorianCalendar(TimeZone.getTimeZone("Europe/Madrid"));
        dia =calendarNow.get(Calendar.DAY_OF_MONTH);
        mes = calendarNow.get(Calendar.MONTH)+1;
        ano = calendarNow.get(Calendar.YEAR);

        farm_name = share_preferences_interface.farm_name(preferences);
        id_propietario = share_preferences_interface.id_propietario(preferences);

        if (farm_name != null && id_propietario != null) {
            CollectionReference[] registro_animales_array = share_preferences_interface.referencedb_c(id_propietario, farm_name, "vacio" );
            CollectionReference animales_ref = registro_animales_array[0];
            DocumentReference[] fincas_ref = share_preferences_interface.referencedb_d(id_propietario, farm_name, "vacio" );
            DocumentReference finca_ref = fincas_ref[0];
            perfil_admin_interface = new Perfil_Admin_Presenter(getContext(),finca_ref ,animales_ref);
            perfil_admin_interface.show_data_type_animals_out(  "muerte", tv_muertos, ano);
            perfil_admin_interface.show_data_type_animals_out( "vendido", tv_vendidos, ano);
            perfil_admin_interface.show_data_type_animals_on( "nacido", tv_nacidos, ano);
            perfil_admin_interface.show_data_type_animals_on( "trasladado", tv_trasladados, ano);
            perfil_admin_interface.show_data_type_animals_on( "comprado", tv_comprados,ano);
            perfil_admin_interface.show_birth_birthdeath_type_animals( "muerto", tv_nacidos_muertos,ano);
            perfil_admin_interface.show_birth_birthdeath_type_animals( "aborto", tv_abortos,ano);
            int in = Integer.parseInt(tv_comprados.getText().toString().trim());
            int  gre = Integer.parseInt(tv_trasladados.getText().toString().trim());
            String ingresados = String.valueOf(in + gre);
            tv_ingresados.setText(ingresados);
            String comprados = tv_comprados.getText().toString();
            perfil_admin_interface.mostrar_analizis_anual( tv_ganancias, tv_gasto, tv_litros,tv_kilos, ano);
        }

        return view;
    }


    private void iniciar_variables_analizis(){
        tv_ganancias = view.findViewById(R.id.tv_p_admin_ana_ganacia);
        tv_litros = view.findViewById(R.id.tv_p_admin_ana_cant_litros_ano);
        tv_kilos = view.findViewById(R.id.tv_p_ana_kilos_producidos_anio);
        tv_nacidos = view.findViewById(R.id.tv_det_p_admin_ana_cant_animales_nacidos);
        tv_muertos = view.findViewById(R.id.tv_det_p_admin_ana_cant_animales_muertos);
        tv_abortos = view.findViewById(R.id.tv_det_p_admin_ana_avorts);
        tv_nacidos_muertos  = view.findViewById(R.id.tv_det_p_admin_ana_birthdead);
        tv_comprados = view.findViewById(R.id.tv_det_p_ana_buyed_animals);
        tv_trasladados = view.findViewById(R.id.tv_det_p_admin_ana_trasladados);
        tv_vendidos = view.findViewById(R.id.tv_det_p_ana_cant_animales_vendidos);
        tv_gasto = view.findViewById(R.id.tv_p_admin_ana_gasto);
        tv_ingresados = view.findViewById(R.id.tv_det_p_admin_ana_cant_animales_ingresados);
        preferences = getContext().getSharedPreferences("preferences", getContext().MODE_PRIVATE);

    }

}