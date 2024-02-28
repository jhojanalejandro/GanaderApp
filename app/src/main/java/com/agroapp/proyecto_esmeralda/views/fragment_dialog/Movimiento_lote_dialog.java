package com.agroapp.proyecto_esmeralda.views.fragment_dialog;

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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.agroapp.proyecto_esmeralda.R;
import com.agroapp.proyecto_esmeralda.interfaces.Share_References_interface;
import com.agroapp.proyecto_esmeralda.controlador.Share_References_presenter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Movimiento_lote_dialog extends AppCompatDialogFragment {

    SharedPreferences preferences;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String n_finca,user_name,id_animal,eleccion,nombre_animal,id_propietario;
    View view;
    int dia,mes,ano;
    ArrayAdapter lote_adapter;
    Spinner spinner_lotes;
    String[] array_lotes;
    TextView tv_ingreso_animal;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.movimiento_lote_dialog,null);
        tv_ingreso_animal = view.findViewById(R.id.tv_r_mvt_n_animal);
        array_lotes = new String[] {"horras","novillos01", "novillos02","novillos03","novillos04","lote01","lote02","lote03","lote04","crias02","crias03","crias04"};
        spinner_lotes = view.findViewById(R.id.spinner_cambio_lote);
        preferences = getContext().getSharedPreferences("preferences", Context.MODE_PRIVATE);
        Share_References_interface share_references_interface = new Share_References_presenter(getContext());
        id_propietario = share_references_interface.id_propietario(preferences);
        n_finca = share_references_interface.farm_name(preferences);
        user_name = share_references_interface.user_name(preferences);
        id_animal = share_references_interface.id_animal(preferences);
        nombre_animal = share_references_interface.animal_name(preferences);

        if (id_animal != null & id_propietario != null ){
            tv_ingreso_animal.setText(nombre_animal);
            spinner_lote();
        }else {
            dismiss();
            Toast.makeText(view.getContext(), "presione nuevamente", Toast.LENGTH_SHORT).show();
        }
        builder.setView(view)
                .setTitle("Cambio lote")
                .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(view.getContext(), "MOVIMIENTO CANCELADO", Toast.LENGTH_SHORT).show();
                    }
                })
                .setPositiveButton("AGREGAR ANIMAL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ingreso_lote();

                    }


                });

        return  builder.create();
    }
    private void spinner_lote(){
        spinner_lotes.setAdapter(lote_adapter);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item,array_lotes);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_lotes.setAdapter(arrayAdapter);
        spinner_lotes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                eleccion = (String) item;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void ingreso_lote(){
        DocumentReference ref_usuarios = db.collection("usuarios").document(id_propietario);
        DocumentReference fincas_ref = ref_usuarios.collection("fincas").document(n_finca);
        final DocumentReference dos = fincas_ref.collection("animales").document(id_animal);
        dos.update("anml_lote",eleccion).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(view.getContext(), "animal agregado al lote ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
