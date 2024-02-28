package com.agroapp.proyecto_esmeralda.views.perfil_animal_views;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
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
import com.agroapp.proyecto_esmeralda.modelos.Gastos_Insumos;
import com.agroapp.proyecto_esmeralda.controlador.Manejo_Insumos_Presenter;
import com.agroapp.proyecto_esmeralda.controlador.Offline_Connexion_animal_Presenter;
import com.agroapp.proyecto_esmeralda.controlador.Perfil_animal_Presenter;
import com.agroapp.proyecto_esmeralda.controlador.Share_References_presenter;
import com.agroapp.proyecto_esmeralda.views.manejo_animal_view.Manejo_animal_view;
import com.agroapp.proyecto_esmeralda.activities.Probar_connexion;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;

import java.util.Calendar;

public class Dry_Data_Register_Data_View_dialog extends AppCompatDialogFragment implements View.OnClickListener {

    EditText edt_f_secado, edt_cant_droga, edt_obs_secado, edt_trat_secado;
    View view;
    ImageButton ibt_f_secado;
    Gastos_Insumos ficha_secado;
    Perfil_Animal_Interface perfil_animal_interface;
    Manejo_Insumos_Interface manejo_insumos_interface;
    Offlinne_Connexion_Interface offlinne_connexion_interface;

    Share_References_interface share_references_interface;
    String farm_name,drog = "", id_animal,id_propietario;
    SharedPreferences preferences;
    DatePickerDialog datePickerDialog;
    Spinner spinner_insumos;

    int  cant_drog = 0;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.registro_datos_secado_dialog, null);
        iniciar_variables();
        farm_name = share_references_interface.farm_name(preferences);
        id_animal = share_references_interface.id_animal(preferences);
        id_propietario = share_references_interface.id_propietario(preferences);


        if (farm_name != null & id_propietario != null){
            CollectionReference[] registro_animales_array = share_references_interface.referencedb_c(id_propietario, farm_name, id_animal);
            CollectionReference registro_animales = registro_animales_array[1];

            DocumentReference[] registros_ref_array = share_references_interface.referencedb_d(id_propietario,farm_name,"vacio");
            DocumentReference fincas_ref = registros_ref_array[0];
            DocumentReference animal_ref = registros_ref_array[1];
            perfil_animal_interface = new Perfil_animal_Presenter(getContext(),animal_ref,registro_animales);

            manejo_insumos_interface = new Manejo_Insumos_Presenter(getContext(), fincas_ref);
        }
        builder.setView(view)
                .setTitle("datos de secado")
                .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("ENVIAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String obs_secado = edt_obs_secado.getText().toString();
                        String fecha = edt_f_secado.getText().toString();
                        String tratamiento = edt_trat_secado.getText().toString();
                        if (edt_cant_droga.getText().toString().trim().length() == 0){
                            cant_drog = 0;
                        }else {
                            cant_drog = Integer.parseInt(edt_cant_droga.getText().toString().trim());
                            drog = manejo_insumos_interface.mostrar_spinner_insumos("Droga",spinner_insumos);

                        }
                        if (Probar_connexion.Prueba(getContext())) {
                            Toast.makeText(getContext() , "valor nombre" + drog, Toast.LENGTH_SHORT).show();
                            ficha_secado = new Gastos_Insumos();
                            ficha_secado.setObservaciones(obs_secado);
                            ficha_secado.setTratamiento(tratamiento);
                            ficha_secado.setCantidad_droga(cant_drog);
                            ficha_secado.setTipo_registro("secado");
                            ficha_secado.setFecha_registro(fecha);
                            ficha_secado.setNombre_droga(drog);
                            perfil_animal_interface.data_type_anmls_register(drog,cant_drog, id_propietario ,farm_name,id_animal,true, ficha_secado);
                        } else {
                            offlinne_connexion_interface.data_type_anmls_register_offline( getContext(), 0 ,   0.0, 0.0,0.0, "vacio", obs_secado, tratamiento,  "vacio",  farm_name,  cant_drog,  "vacio",  "secado",  "vacio",  fecha,  "vacio",  0,  "vacio",  "vacio",  drog,  "vacio",  "vacio", id_animal, id_propietario);
                        }
                    }


                });

        return builder.create();
    }


    private void date_picker_hot(){
        Calendar calendar = Calendar.getInstance();
        final int dias = calendar.get(calendar.DAY_OF_MONTH);
        final int meses = calendar.get(calendar.MONTH);
        final int anos = calendar.get(calendar.YEAR);

        datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int anos, int meses, int dias) {
                meses = meses + 1;
                edt_f_secado.setText(dias + "/" + meses + "/" + anos + "/");

            }

        }, anos, meses, dias);
        datePickerDialog.show();
    }

    private void iniciar_variables() {
        edt_cant_droga = view.findViewById(R.id.edt_r_scd_cant_droga);
        edt_f_secado = view.findViewById(R.id.edt_secado_f);
        edt_obs_secado = view.findViewById(R.id.edt_r_scd_obs);
        edt_trat_secado = view.findViewById(R.id.edt_r_scd_trat);
        ibt_f_secado = view.findViewById(R.id.ibtn_dry_r_date);
        spinner_insumos = view.findViewById(R.id.spinner_secado_droga);
        ibt_f_secado.setOnClickListener(this);
        preferences = getContext().getSharedPreferences("preferences", Context.MODE_PRIVATE);
        offlinne_connexion_interface = new Offline_Connexion_animal_Presenter(getContext());
        share_references_interface = new Share_References_presenter(getContext());

    }


    @Override
    public void onResume() {
        super.onResume();
        if (farm_name == null || id_animal == null) {
            Toast.makeText(getContext(), "no sera posible guardar el registro guardar el registro : ", Toast.LENGTH_LONG).show();
            Intent manejo = new Intent(getContext(), Manejo_animal_view.class);
            startActivity(manejo);
        } else {
            manejo_insumos_interface.mostrar_spinner_insumos("Droga",spinner_insumos);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ibtn_dry_r_date:
                date_picker_hot();
                break;
        }
    }
}
