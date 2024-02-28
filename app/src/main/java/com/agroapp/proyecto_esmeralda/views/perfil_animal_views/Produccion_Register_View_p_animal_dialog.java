package com.agroapp.proyecto_esmeralda.views.perfil_animal_views;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;


import com.agroapp.proyecto_esmeralda.R;
import com.agroapp.proyecto_esmeralda.activities.Probar_connexion;
import com.agroapp.proyecto_esmeralda.adapters.Adapter_produccion_individual;
import com.agroapp.proyecto_esmeralda.adapters.Adapter_produccion_individual_carne;
import com.agroapp.proyecto_esmeralda.interfaces.Offlinne_Connexion_Interface;
import com.agroapp.proyecto_esmeralda.interfaces.Perfil_Animal_Interface;
import com.agroapp.proyecto_esmeralda.interfaces.Share_References_interface;
import com.agroapp.proyecto_esmeralda.modelos.Medida_Leche_Model;
import com.agroapp.proyecto_esmeralda.modelos.Pesaje_Animal_Model;
import com.agroapp.proyecto_esmeralda.modelos.Produccion_Model;
import com.agroapp.proyecto_esmeralda.controlador.Offline_Connexion_animal_Presenter;
import com.agroapp.proyecto_esmeralda.controlador.Perfil_animal_Presenter;
import com.agroapp.proyecto_esmeralda.controlador.Share_References_presenter;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;

public class Produccion_Register_View_p_animal_dialog extends AppCompatDialogFragment {

    EditText edt_medida_leche, edt_obs, edt_pesaje_carne;

    String obs, nombre_animal;
    private int dia_promedi, result_leche_varios_dias, dia_traido, cont_dias, dia, mes, cant_dias_medidos, cant_litros_medidos, ano;

    private Spinner spinner_tipo_produccion, spinner_etapa_pesaje;
    private String[] array_tipo_produccion, array_tipo_etapas;
    Offlinne_Connexion_Interface offlinne_connexion_interface;

    Double result_leche, peso_animal;
    TextView tv_result_leche, tv_result_carne, tv_id_animal;
    View view;
    private ArrayAdapter Adapter_tipo_produccion, adapter_etapa_pesaje;
    Adapter_produccion_individual adapter_medida;
    Adapter_produccion_individual_carne adapter_pesaje;
    Perfil_Animal_Interface perfil_animal_interface;
    Share_References_interface share_references_interface;
    private SharedPreferences preferences;
    private Medida_Leche_Model medida_leche;
    Probar_connexion probar_connexion;
    private String farm_name, dias, meses, anios, id_propietario;
    private String eleccion_tipo_produccion, eleccion_etapa;
    private String fecha;
    private String id_animal;
    private Produccion_Model ficha_produccion;
    private Pesaje_Animal_Model ficha_pesaje_carne;

    public Produccion_Register_View_p_animal_dialog() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.registro_medida_leche_dialog, null);
        iniciar_variables();

        if (farm_name == null || id_animal == null) {
            Toast.makeText(getContext(), "no se podra registrar la informacion", Toast.LENGTH_SHORT).show();

        } else {
            tv_id_animal.setText(nombre_animal);

        }

        spinner_tipo_produccion.setAdapter(Adapter_tipo_produccion);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, array_tipo_produccion);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_tipo_produccion.setAdapter(arrayAdapter);
        spinner_tipo_produccion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {
                Object item = parent.getItemAtPosition(pos);
                eleccion_tipo_produccion = (String) item;
                if (eleccion_tipo_produccion.equals("Pesar Animal")) {
                    view.findViewById(R.id.ln_cant_result_peso_anml).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.ln_cant_peso_anml).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.ln_medida_anml).setVisibility(View.GONE);
                    view.findViewById(R.id.tv_etapa_pesaje_spinner).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.spinner_tipo_etapa_pesaje).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.ln_cant_result_am_anml).setVisibility(View.GONE);

                    perfil_animal_interface.show_weigh_animal(tv_result_carne, dia, mes, ano);

                } else if (eleccion_tipo_produccion.equals("Medir Leche")) {
                    view.findViewById(R.id.ln_cant_result_am_anml).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.ln_cant_peso_anml).setVisibility(View.GONE);
                    view.findViewById(R.id.ln_medida_anml).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.tv_etapa_pesaje_spinner).setVisibility(View.GONE);
                    view.findViewById(R.id.spinner_tipo_etapa_pesaje).setVisibility(View.GONE);
                    view.findViewById(R.id.ln_cant_result_peso_anml).setVisibility(View.GONE
                    );
                    perfil_animal_interface.show_measur_animal(tv_result_leche, dia, mes, ano);
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_etapa_pesaje.setAdapter(adapter_etapa_pesaje);
        ArrayAdapter<String> arrayAdapter_etapa = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, array_tipo_etapas);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_etapa_pesaje.setAdapter(arrayAdapter_etapa);
        spinner_etapa_pesaje.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> p, View v, int pos, long id) {
                Object item = p.getItemAtPosition(pos);
                if (item != null) {
                    eleccion_etapa = (String) item;
                }

            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        builder.setView(view)
                .setTitle("Medida De Leche")
                .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(view.getContext(), "Registro Cancelado ", Toast.LENGTH_SHORT).show();
                    }
                })
                .setPositiveButton("ENVIAR", (dialogInterface, i) -> {

                    dias = String.valueOf(dia);
                    meses = String.valueOf(mes);
                    anios = String.valueOf(ano);
                    fecha = dias + "/" + meses + "/" + anios;
                    obs = edt_obs.getText().toString().trim();
                    if (eleccion_tipo_produccion.equals("Medir Leche")) {
                        Double leche_ = Double.valueOf(edt_medida_leche.getText().toString().trim());
                        result_leche = Double.parseDouble(tv_result_leche.getText().toString().trim());
                        if (Probar_connexion.Prueba(getContext())) {
                            perfil_animal_interface.data_measur_anmls_register(dia, mes, ano, fecha, obs, leche_, result_leche, true);
                        } else {
                            offlinne_connexion_interface.data_type_anmls_register_offline(getContext(), cant_dias_medidos, 0.0, result_leche, peso_animal, "vacio", obs, "vacio", "vacio", farm_name, 0, "vacio", "medida", "vacio", fecha, "vacio", 0, "vacio", "vacio", "vacio", "vacio", "vacio", id_animal, id_propietario);
                        }
                    } else if (eleccion_tipo_produccion.equals("Pesar Animal")) {
                        peso_animal = Double.parseDouble(edt_pesaje_carne.getText().toString().trim());
                        if (Probar_connexion.Prueba(getContext())) {
                            perfil_animal_interface.data_weighing_anmls_register(peso_animal, obs, fecha, dia, mes, ano, id_propietario, farm_name, id_animal, true, eleccion_etapa);
                        } else {
                            offlinne_connexion_interface.data_type_anmls_register_offline(getContext(), cant_dias_medidos, 0.0, result_leche, peso_animal, "vacio", obs, "vacio", "vacio", farm_name, 0, "vacio", "medida", "vacio", fecha, "vacio", 0, "vacio", "vacio", "vacio", "vacio", "vacio", id_animal, id_propietario);
                        }

                    }
                });

        return builder.create();
    }

    private void iniciar_variables() {
        edt_medida_leche = view.findViewById(R.id.edt_aniimal_prod_meida_leche);
        edt_pesaje_carne = view.findViewById(R.id.edt_aniimal_prod_peso_anml);
        edt_obs = view.findViewById(R.id.edt_medida_obs);
        tv_result_leche = view.findViewById(R.id.tv_prod_animal_result_leche);
        tv_result_carne = view.findViewById(R.id.tv_prod_animal_result_carne);
        tv_id_animal = view.findViewById(R.id.tv_nombre_animal_pro);
        spinner_tipo_produccion = view.findViewById(R.id.spinner_tipo_prod);
        spinner_etapa_pesaje = view.findViewById(R.id.spinner_tipo_etapa_pesaje);
        probar_connexion = new Probar_connexion();
        adapter_medida = new Adapter_produccion_individual(getContext());
        adapter_pesaje = new Adapter_produccion_individual_carne(getContext());
        share_references_interface = new Share_References_presenter(getContext());
        offlinne_connexion_interface = new Offline_Connexion_animal_Presenter(getContext());
        preferences = getContext().getSharedPreferences("preferences", Context.MODE_PRIVATE);

        id_animal = share_references_interface.id_animal(preferences);
        nombre_animal = share_references_interface.animal_name(preferences);
        farm_name = share_references_interface.farm_name(preferences);

        int[] date = share_references_interface.date_picker();
        dia = date[0];
        mes = date[1];
        ano = date[2];

        id_propietario = share_references_interface.id_propietario(preferences);
        array_tipo_produccion = new String[]{"Medir Leche", "Pesar Animal"};
        array_tipo_etapas = new String[]{"pesar Para Destete", "Pesar Despues De Ingreso", "pesar antes de vender", "pesar para vender"};

        DocumentReference[] animal_array = share_references_interface.referencedb_d(id_propietario, farm_name, id_animal);
        CollectionReference[] registro_animales_array = share_references_interface.referencedb_c(id_propietario, farm_name, id_animal);
        CollectionReference registro_animales_ref = registro_animales_array[1];
        DocumentReference animal_ref = animal_array[1];
        DocumentReference farm_ref = animal_array[0];
        perfil_animal_interface = new Perfil_animal_Presenter(getContext(), registro_animales_ref, animal_ref,farm_ref );

    }

}
