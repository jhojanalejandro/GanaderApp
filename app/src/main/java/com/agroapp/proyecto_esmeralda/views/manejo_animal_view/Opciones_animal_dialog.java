package com.agroapp.proyecto_esmeralda.views.manejo_animal_view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;


import com.agroapp.proyecto_esmeralda.R;
import com.agroapp.proyecto_esmeralda.views.fragment_dialog.Movimiento_lote_dialog;
import com.agroapp.proyecto_esmeralda.views.perfil_animal_views.Perfil_Animal_view;
import com.agroapp.proyecto_esmeralda.views.perfil_animal_views.Produccion_Register_View_p_animal_dialog;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class Opciones_animal_dialog extends AppCompatDialogFragment {

    TextView tv_id_animal;
    private  View views;
    int dia, mes, ano;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    SharedPreferences preferences;
    Movimiento_animal_dialog movimiento_animal;
    Movimiento_lote_dialog cambio_lote;
    Produccion_Register_View_p_animal_dialog registro_medida_leche;
    ImageView btn_medir_leche, btn_perfil_animal, btn_cambio_lote, btn_movimineto_aniaml;
    String  id_animal,nombre_animal;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        views = inflater.inflate(R.layout.opciones_animal, null);

        iniciar_variables();
        datepikers_hoy();
        id_animal();
        nombre_animal();
        if ( id_animal() == null) {
            Toast.makeText(getContext(), "no se podra registrar la informacion", Toast.LENGTH_SHORT).show();
        }
        tv_id_animal.setText("Nombre Animal: " + nombre_animal);
        btn_movimineto_aniaml.setOnClickListener(v -> ir_movimiento());
        btn_cambio_lote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cambio_lote = new Movimiento_lote_dialog();
                cambio_lote.show(getActivity().getSupportFragmentManager(), "registro de movimientos potrero ");

            }
        });

        btn_medir_leche.setOnClickListener(v -> {
            registro_medida_leche = new Produccion_Register_View_p_animal_dialog();
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("id_animal", id_animal);
            editor.putString("nombre_animal", nombre_animal);
            editor.apply();
            registro_medida_leche.show(getActivity().getSupportFragmentManager(), "registro de medicion de leche ");

        });
        btn_perfil_animal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detalle = new Intent(getContext(), Perfil_Animal_view.class);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("id_animal", id_animal);
                editor.apply();
                startActivity(detalle);

            }
        });
        builder.setView(views);

        return builder.create();

    }
    private void iniciar_variables() {
        btn_medir_leche = views.findViewById(R.id.ibtn_opc_medir_leche);
        btn_perfil_animal = views.findViewById(R.id.ibt_opc_perfil);
        tv_id_animal = views.findViewById(R.id.tv_nombre_animal_opc);
        btn_cambio_lote = views.findViewById(R.id.ibtn_opc_cambio_lote);
        btn_movimineto_aniaml = views.findViewById(R.id.ibtn_opc_movimiento);
        
    }
    private void ir_movimiento() {
        movimiento_animal = new Movimiento_animal_dialog();
        movimiento_animal.show(getActivity().getSupportFragmentManager(), "registro de movimientos potrero ");

    }

    private String id_animal() {
        preferences = getContext().getSharedPreferences("preferences", Context.MODE_PRIVATE);
        id_animal = preferences.getString("id_animal", null);
        if (id_animal != null) {
            return id_animal;
        } else {
            return null;
        }
    }
    private String nombre_animal() {
        preferences = getContext().getSharedPreferences("preferences", Context.MODE_PRIVATE);
        nombre_animal = preferences.getString("nombre_animal", null);
        if (id_animal != null) {
            return id_animal;
        } else {
            return null;
        }
    }
    private void datepikers_hoy() {
        Calendar calendarNow = new GregorianCalendar(TimeZone.getTimeZone("Europe/Madrid"));
        dia = calendarNow.get(Calendar.DAY_OF_MONTH);
        mes = calendarNow.get(Calendar.MONTH) + 1;
        ano = calendarNow.get(Calendar.YEAR);
    }

}
