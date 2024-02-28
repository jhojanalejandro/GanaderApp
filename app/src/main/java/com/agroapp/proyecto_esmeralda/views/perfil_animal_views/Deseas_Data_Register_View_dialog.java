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
import com.agroapp.proyecto_esmeralda.modelos.Emfermedad_Model;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;

import java.util.Calendar;

public class Deseas_Data_Register_View_dialog extends AppCompatDialogFragment implements View.OnClickListener {

    EditText edt_cant_droga, edt_event_efmd, edt_obs_efmd, edt_trat_efmd, edt_vet, edt_fecha_evento;
    View view;
    String farm_name, id_animal,cant_droga_s = "",drog, id_propietario;
    private final String tipo = "enfermedad";
    SharedPreferences preferences;
    Spinner spinner_insumos;
    Perfil_Animal_Interface perfil_animal_interface;
    Manejo_Insumos_Interface manejo_insumos_interface;
    Offlinne_Connexion_Interface offlinne_connexion_interface;
    Share_References_interface share_references_interface;


    private ProgressDialog progressBar;
    ImageButton ibtn_fecha_evento;
    private int cant_drog = 0;
    Boolean boleano_insumos = false;
    private DatePickerDialog datePickerDialog;
    private String TAG = "algo pasa";

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.registro_datos_efmd_dialog, null);
        iniciar_variables();
        farm_name = share_references_interface.farm_name(preferences);
        id_animal = share_references_interface.id_animal(preferences);
        id_propietario = share_references_interface.id_propietario(preferences);


        if (farm_name == null || id_animal == null) {
            Toast.makeText(getContext(), "no sera posible guardar el registro guardar el registro : ", Toast.LENGTH_LONG).show();
            Intent manejo = new Intent(getContext(), Manejo_animal_view.class);
            startActivity(manejo);
        } else {
            CollectionReference[] registro_animales_array = share_references_interface.referencedb_c(id_propietario,farm_name, id_animal);
            CollectionReference registro_animales = registro_animales_array[1];
            DocumentReference[] registros_ref_array = share_references_interface.referencedb_d(id_propietario,farm_name,"vacio");
            DocumentReference fincas_ref = registros_ref_array[0];
            DocumentReference animal_ref = registros_ref_array[1];
            manejo_insumos_interface = new Manejo_Insumos_Presenter(getContext(), fincas_ref);
            perfil_animal_interface = new Perfil_animal_Presenter(getContext(),animal_ref,registro_animales);

            manejo_insumos_interface.mostrar_spinner_insumos("Droga",spinner_insumos);
        }

        builder.setView(view)
                .setTitle("datos de enfermedad")
                .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getContext(), "registro cancelado", Toast.LENGTH_SHORT).show();
                    }
                })
                .setPositiveButton("ENVIAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String event_efmd = edt_event_efmd.getText().toString();
                        String obs = edt_obs_efmd.getText().toString();
                        String trat = edt_trat_efmd.getText().toString();
                        String fecha_vento = edt_fecha_evento.getText().toString();
                        String vet = edt_vet.getText().toString();
                        if (edt_cant_droga.toString().trim().length() == 0) {
                            cant_drog = 0;
                        } else {
                            cant_drog = Integer.parseInt(edt_cant_droga.getText().toString().trim());
                            drog = manejo_insumos_interface.mostrar_spinner_insumos("droga",spinner_insumos);
                        }
                        if (Probar_connexion.Prueba(getContext())) {

                            Emfermedad_Model ficha_efmd = new Emfermedad_Model();
                            ficha_efmd.setFecha_registro(fecha_vento);
                            ficha_efmd.setEfmd_evento(event_efmd);
                            ficha_efmd.setTratamiento(trat);
                            ficha_efmd.setObservaciones(obs);
                            ficha_efmd.setTipo_registro("enfermedad");
                            ficha_efmd.setCantidad_droga(cant_drog);
                            ficha_efmd.setNombre_droga(drog);
                            ficha_efmd.setEfmd_veterinario(vet);
                            perfil_animal_interface.data_type_anmls_register(drog,cant_drog,id_propietario ,farm_name,id_animal,true, ficha_efmd);
                        } else {
                            offlinne_connexion_interface.data_type_anmls_register_offline( getContext(), 0 ,0.0, 0.0,0.0, "vacio", obs, trat,  vet,  farm_name,  cant_drog,  "vacio",  tipo,  event_efmd,  fecha_vento,  "vacio",  0,  "vacio",  "vacio",  drog,  "vacio",  "vacio", id_animal,id_propietario);
                        }
                    }


                });

        return builder.create();
    }

    private void iniciar_variables() {
        progressBar = new ProgressDialog(getContext());
        edt_vet = view.findViewById(R.id.edt_r_efmd_n_vet);
        edt_fecha_evento = view.findViewById(R.id.edt_r_efmd_f_evento);
        edt_cant_droga = view.findViewById(R.id.edt_r_efmd_cant_droga);
        spinner_insumos = view.findViewById(R.id.spinner_efm_droga);

        edt_event_efmd = view.findViewById(R.id.edt_r_efmd_event);
        edt_obs_efmd = view.findViewById(R.id.edt_r_efmd_obs);
        ibtn_fecha_evento = view.findViewById(R.id.ibtn_anml_f_evento_animal);
        edt_trat_efmd = view.findViewById(R.id.edt_r_efmd_trat);
        ibtn_fecha_evento.setOnClickListener(this);
        preferences = getContext().getSharedPreferences("preferences", Context.MODE_PRIVATE);
        share_references_interface = new Share_References_presenter(getContext());
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
                mes = meses + 1;
                edt_fecha_evento.setText(dia + "/" + mes + "/" + ano );
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
