package com.agroapp.proyecto_esmeralda.views.manejo_insumos_view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;


import com.agroapp.proyecto_esmeralda.R;
import com.agroapp.proyecto_esmeralda.activities.Probar_connexion;
import com.agroapp.proyecto_esmeralda.interfaces.Manejo_Insumos_Interface;
import com.agroapp.proyecto_esmeralda.controlador.Manejo_Insumos_Presenter;
import com.agroapp.proyecto_esmeralda.controlador.Share_References_presenter;
import com.google.firebase.firestore.DocumentReference;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class Registro_insumos_potrero_dialog extends AppCompatDialogFragment {

    EditText edt_cantidad_insumo;
    View view;
    Spinner spinner_insumos;
    int dia,mes,ano;
    Probar_connexion probar_connexion;
    SharedPreferences preferences;
    String farm_name, id_propietario,nombre_concentrado,user_name,eleccion_tipo,tipo_spiner;
    Manejo_Insumos_Interface manejo_insumos_interface;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.registro_insumos_potrero_dialog, null);
        iniciar_variables();
        datepikers_hoy();
        Share_References_presenter share_references_presenter = new Share_References_presenter(getContext());



        farm_name = share_references_presenter.farm_name(preferences);
        id_propietario = share_references_presenter.id_propietario(preferences);
        user_name = share_references_presenter.user_name(preferences);
        eleccion_tipo = share_references_presenter.tipo(preferences);
        DocumentReference[] registros_ref_array = share_references_presenter.referencedb_d(id_propietario,farm_name,"vacio");
        DocumentReference fincas_ref = registros_ref_array[0];
        manejo_insumos_interface = new Manejo_Insumos_Presenter(getContext(),fincas_ref);
        tipo_spiner = tipo_gasto();
        if (farm_name == null || id_propietario == null) {
            Toast.makeText(view.getContext(), "no se podra registrar la imformacion", Toast.LENGTH_SHORT).show();
        }else {
            show_spiner();

        }
        builder.setView(view)
                .setTitle(  "GASTO DE CONCENTRADOS")
                .setNegativeButton("CANCELAR", (dialogInterface, i) -> dismiss())
                .setPositiveButton("ENVIAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ProgressDialog progressDialog  = new ProgressDialog(getContext());
                        show_spiner();
                        int cantidad = Integer.parseInt(edt_cantidad_insumo.getText().toString());
                        manejo_insumos_interface.registrar_gasto_insumo( nombre_concentrado, cantidad);
                    }


                });

        return  builder.create();


    }

    private void show_spiner() {
        if (tipo_spiner.equals("abono")){
            nombre_concentrado = manejo_insumos_interface.mostrar_spinner_insumos("abono", spinner_insumos);

        }else if (tipo_spiner.equals("concentrado")){
            nombre_concentrado=  manejo_insumos_interface.mostrar_spinner_insumos("concentrado", spinner_insumos);

        }
    }

    private void iniciar_variables(){
        edt_cantidad_insumo  = view.findViewById(R.id.edt_cantidad_gasto_concentrado);
        spinner_insumos  = view.findViewById(R.id.spinner_concentrados);
        probar_connexion = new Probar_connexion();
        preferences = getContext().getSharedPreferences("preferences", Context.MODE_PRIVATE);


    }
    private void datepikers_hoy( ) {
        Calendar calendarNow = new GregorianCalendar(TimeZone.getTimeZone("Europe/Madrid"));
        dia =calendarNow.get(Calendar.DAY_OF_MONTH);
        mes = calendarNow.get(Calendar.MONTH)+1;
        ano = calendarNow.get(Calendar.YEAR);
    }
    private String tipo_gasto(){
        preferences =  getContext().getSharedPreferences("preferences", getContext().MODE_PRIVATE);
        String tipo_gasto = preferences.getString("tipo_concentrado",null);
        if ( tipo_gasto!=null){
            return tipo_gasto;
        }else {
            return  null;
        }
    }

}
