package com.agroapp.proyecto_esmeralda.views.perfil_animal_views;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.agroapp.proyecto_esmeralda.R;
import com.agroapp.proyecto_esmeralda.interfaces.Manejo_Insumos_Interface;
import com.agroapp.proyecto_esmeralda.interfaces.Offlinne_Connexion_Interface;
import com.agroapp.proyecto_esmeralda.interfaces.Perfil_Animal_Interface;
import com.agroapp.proyecto_esmeralda.interfaces.Share_References_interface;
import com.agroapp.proyecto_esmeralda.controlador.Manejo_Insumos_Presenter;
import com.agroapp.proyecto_esmeralda.controlador.Offline_Connexion_animal_Presenter;
import com.agroapp.proyecto_esmeralda.controlador.Perfil_animal_Presenter;
import com.agroapp.proyecto_esmeralda.controlador.Share_References_presenter;
import com.agroapp.proyecto_esmeralda.views.manejo_animal_view.Manejo_animal_view;
import com.agroapp.proyecto_esmeralda.activities.Probar_connexion;
import com.agroapp.proyecto_esmeralda.modelos.Insumo_Finca_Model;
import com.agroapp.proyecto_esmeralda.modelos.Vacunacion_Model;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;

import java.util.Calendar;


public class Vaccination_Data_Register_dialog extends AppCompatDialogFragment implements View.OnClickListener{


    EditText edt_cant_droga, edt_event_vcn, edt_obs_vcn, edt_trat_vcn, edt_vet, edt_fecha_evento;
    View view;


    SharedPreferences preferences;
    Spinner tipos_spinner_tipo_vcn, spinner_insumos;
    String[] tipo_vcn;
    ArrayAdapter comboAdapter;
    ImageButton ibtn_fecha_evento;
    Manejo_Insumos_Interface manejo_insumos_interface;
    Perfil_Animal_Interface perfil_animal_interface;
    Share_References_interface share_references_interface;
    String farm_name, user_name,drog = "",id_propietario;
    String eleccion_vcn,id_animal;

    private final  String tipo_vacunacion  = "vacunacion";
    Insumo_Finca_Model insumo_finca;
    private int cant_drog = 0;
    private ProgressDialog progressBar;
    Offlinne_Connexion_Interface offlinne_connexion_interface;

    private DatePickerDialog datePickerDialog;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.registro_datos_vacuna_dialog, null);
        iniciar_variables();

        farm_name = share_references_interface.farm_name(preferences);
        id_animal = share_references_interface.id_animal(preferences);
        user_name = share_references_interface.user_name(preferences);
        id_propietario = share_references_interface.id_propietario(preferences);

        DocumentReference[] registros_ref_array = share_references_interface.referencedb_d(id_propietario,farm_name,"vacio");
        DocumentReference fincas_ref = registros_ref_array[0];
        manejo_insumos_interface = new Manejo_Insumos_Presenter(getContext(), fincas_ref);
        if (farm_name == null || id_animal == null) {
            Toast.makeText(getContext(), "no sera posible guardar el registro guardar el registro : ", Toast.LENGTH_LONG).show();
            Intent manejo = new Intent(getContext(), Manejo_animal_view.class);
            startActivity(manejo);
        } else {
            CollectionReference[] registros_animal_array = share_references_interface.referencedb_c(id_propietario, farm_name, id_animal);
            CollectionReference registros_animal_ref = registros_animal_array[1];
            DocumentReference animal_ref = registros_ref_array[1];
            perfil_animal_interface = new Perfil_animal_Presenter(getContext(),animal_ref,registros_animal_ref);
            manejo_insumos_interface.mostrar_spinner_insumos("Droga",spinner_insumos);
        }

        builder.setView(view)
                .setTitle("datos de vacunacion")
                .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getContext(), "registro cancelado", Toast.LENGTH_SHORT).show();
                    }
                })
                .setPositiveButton("ENVIAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String event_vcn = edt_event_vcn.getText().toString();
                        String obs = edt_obs_vcn.getText().toString();

                        String trat = edt_trat_vcn.getText().toString();
                        String vet = edt_vet.getText().toString();
                        String fecha_vento = edt_fecha_evento.getText().toString();
                        if (edt_cant_droga.getText().toString().length() == 0) {
                            cant_drog = 0;
                        } else {
                            cant_drog = Integer.parseInt(edt_cant_droga.getText().toString().trim());
                            drog =  manejo_insumos_interface.mostrar_spinner_insumos("Droga",spinner_insumos);
                        }
                        if (Probar_connexion.Prueba(getContext())) {

                            Vacunacion_Model ficha_vcn = new Vacunacion_Model();
                            ficha_vcn.setObservaciones(obs);
                            ficha_vcn.setTratamiento(trat);
                            ficha_vcn.setFecha_registro(fecha_vento);
                            ficha_vcn.setTipo_registro(tipo_vacunacion);
                            ficha_vcn.setCantidad_droga(cant_drog);
                            ficha_vcn.setNombre_droga(drog);
                            ficha_vcn.setVcn_evento(event_vcn);
                            ficha_vcn.setVcn_veterinario(vet);
                            ficha_vcn.setVcn_tipo_eleccion(eleccion_vcn);
                            perfil_animal_interface.data_type_anmls_register( drog, cant_drog, id_propietario,farm_name, id_animal, true, ficha_vcn);
                        } else {
                            offlinne_connexion_interface.data_type_anmls_register_offline( getContext() , 0,  0.0, 0.0,0.0, "vacio", obs, trat,  "vacio",  farm_name,  cant_drog,  "vacio",  "parto",  event_vcn,  fecha_vento,  "vacio",  0,  "vacio", "vacio",  drog,  eleccion_vcn, "vacio", id_animal,id_propietario);
                        }
                    }

                });

        return builder.create();
    }


    private void iniciar_variables() {

        progressBar = new ProgressDialog(getContext());
        edt_vet = view.findViewById(R.id.edt_r_vcn_n_vet);
        edt_fecha_evento = view.findViewById(R.id.edt_vcn_f_evento_animal);
        edt_cant_droga = view.findViewById(R.id.edt_r_vcn_cant_droga);
        edt_event_vcn = view.findViewById(R.id.edt_r_vcn_event);
        edt_obs_vcn = view.findViewById(R.id.edt_r_vcn_obs);
        ibtn_fecha_evento = view.findViewById(R.id.ibtn_anml_f_evento_animal);
        edt_trat_vcn = view.findViewById(R.id.edt_r_vcn_trat);
        ibtn_fecha_evento.setOnClickListener(this);
        preferences = getContext().getSharedPreferences("preferences", Context.MODE_PRIVATE);
        share_references_interface = new Share_References_presenter(getContext());

        tipos_spinner_tipo_vcn = view.findViewById(R.id.spinner_tipo_vcn);
        spinner_insumos = view.findViewById(R.id.spinner_vcn_drogas);
        tipo_vcn = new String[]{"fiebre aptosa", "urocelosis"};
        tipos_spinner_tipo_vcn.setAdapter(comboAdapter);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, tipo_vcn);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tipos_spinner_tipo_vcn.setAdapter(arrayAdapter);
        tipos_spinner_tipo_vcn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Object item = parent.getItemAtPosition(pos);
                eleccion_vcn = (String) item;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        offlinne_connexion_interface = new Offline_Connexion_animal_Presenter(getContext());



    }

    public String datepikers_fecha_evento() {
        Calendar calendar = Calendar.getInstance();
        final int dias = calendar.get(calendar.DAY_OF_MONTH);
        final int meses = calendar.get(Calendar.MONTH);
        final int anos = calendar.get(Calendar.YEAR);
        datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int ano, int mes, int dia) {
                mes = mes + 1;
                edt_fecha_evento.setText(ano + "/" + mes + "/" + dia + "/");
            }
        }, anos, meses, dias);
        datePickerDialog.show();
        return String.valueOf(edt_fecha_evento);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ibtn_anml_f_evento_animal:
                datepikers_fecha_evento();
                break;
        }
    }
}
