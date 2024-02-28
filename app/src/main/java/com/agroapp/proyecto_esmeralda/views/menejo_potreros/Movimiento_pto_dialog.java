package com.agroapp.proyecto_esmeralda.views.menejo_potreros;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;


import com.agroapp.proyecto_esmeralda.R;
import com.agroapp.proyecto_esmeralda.modelos.Animal_Model;
import com.agroapp.proyecto_esmeralda.modelos.Potreros_model;
import com.agroapp.proyecto_esmeralda.interfaces.Share_References_interface;
import com.agroapp.proyecto_esmeralda.controlador.Share_References_presenter;
import com.agroapp.proyecto_esmeralda.views.manejo_animal_view.Manejo_animal_view;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class Movimiento_pto_dialog extends AppCompatDialogFragment {

    SharedPreferences preferences;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String   n_finca,id_propietario,id_animal,id_tipo, estado_pto = "ocupado", eleccion_lote;
    String[] array_lotes;
    View view;
    Spinner spinner_lote;
    ArrayAdapter lote_adapter;
    String dia, paddock_name, mes, ano, fecha;
    TextView  tv_nombre_potrero,tv_lotes_potreros;
    Potreros_model potreros_model;
    Button btn_animales_idividual;
    Animal_Model model_animal;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.movimiento_pto_dialog, null);
        tv_nombre_potrero = view.findViewById(R.id.tv_m_pto_n);
        tv_lotes_potreros = view.findViewById(R.id.tv_pto_spinner);
        spinner_lote = view.findViewById(R.id.spinner_lotes_movimiento);
        btn_animales_idividual = view.findViewById(R.id.btn_agregar_individual_potrero);
        array_lotes = new String[]{"horras", "novillos01", "novillos02", "novillos03", "lote01", "lote02", "lote03", "lote04", "terneras01", "terneras02", "terneras03"};
        Share_References_interface share_references_interface = new Share_References_presenter(getContext());
        preferences = getContext().getSharedPreferences("preferences", Context.MODE_PRIVATE);
        n_finca = share_references_interface.farm_name(preferences);
        id_propietario = share_references_interface.id_propietario(preferences);
        n_finca = share_references_interface.farm_name(preferences);
        id_propietario = share_references_interface.id_propietario(preferences);
        id_animal = share_references_interface.id_animal(preferences);
        paddock_name = share_references_interface.paddock_name(preferences);
        id_tipo();
        if (id_tipo.equals("individual")) {
            view.findViewById(R.id.tv_pto_spinner).setVisibility(View.GONE);
            view.findViewById(R.id.spinner_lotes_movimiento).setVisibility(View.GONE);
        } else if (id_tipo.equals("colectivo")) {
            view.findViewById(R.id.tv_pto_spinner).setVisibility(View.VISIBLE);
            view.findViewById(R.id.spinner_lotes_movimiento).setVisibility(View.VISIBLE);

        } else {
            dismiss();
            Toast.makeText(view.getContext(), "Intenta Nuevamente", Toast.LENGTH_SHORT).show();
        }
        tv_nombre_potrero.setText(paddock_name);
        spinner_lote.setAdapter(lote_adapter);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, array_lotes);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_lote.setAdapter(arrayAdapter);
        spinner_lote.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Object item = parent.getItemAtPosition(pos);
                eleccion_lote = (String) item;
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btn_animales_idividual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent manejo_animal = new Intent(getContext(), Manejo_animal_view.class);
                SharedPreferences.Editor editors = preferences.edit();
                editors.putString("paddock_name", paddock_name);
                editors.putString("tipo", "potreros");
                editors.apply();
                startActivity(manejo_animal);
            }
        });
        builder.setView(view)
                .setTitle("ingreso animal")
                .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dismiss();
                        Toast.makeText(view.getContext(), "MOVIMIENTO CANCELADO", Toast.LENGTH_SHORT).show();
                    }
                })
                .setPositiveButton("AGREGAR ANIMAL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Share_References_interface share_references_interface = new Share_References_presenter(getContext());
                        int[] fecha_hoy  = share_references_interface.date_picker();
                        dia = String.valueOf(fecha_hoy[0]);
                        mes = String.valueOf(fecha_hoy[1]);
                        ano = String.valueOf(fecha_hoy[2]);
                        fecha = dia + "/" + mes + "/" + ano;
                        ingreso_colectivo_pto();


                    }


                });

        return builder.create();
    }


    private String id_tipo() {
        id_tipo= preferences.getString("id_tipo_ingreso", null);
        if (id_tipo != null) {
            return id_tipo;
        } else {
            return null;
        }
    }

    private void ingreso_anml_pto() {

        DocumentReference ref_usuarios = db.collection("usuarios").document(id_propietario);
        DocumentReference fincas_ref = ref_usuarios.collection("fincas").document(n_finca);
        final DocumentReference dos = fincas_ref.collection("potreros").document(paddock_name);
        dos.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    potreros_model = task.getResult().toObject(Potreros_model.class);
                    String estado = potreros_model.getPto_estado();
                    if (estado.equals("vacio")) {
                        dos.update("pto_estado", estado_pto, "pto_fecha", fecha).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    dismiss();
                                    Toast.makeText(view.getContext(), "animal agregado a el potrero ", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }

                }
            }
        });


    }

    private void ingreso_colectivo_pto() {
        DocumentReference ref_usuarios = db.collection("usuarios").document(id_propietario);
        DocumentReference fincas_ref = ref_usuarios.collection("fincas").document(n_finca);
        final DocumentReference dos = fincas_ref.collection("potreros").document(paddock_name);
        dos.update("pto_estado", estado_pto, "pto_lote", eleccion_lote, "pto_fecha_ingreso", fecha).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    ingreso_pto_anmls();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(view.getContext(), "Erro En El Systema" + e, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void ingreso_individual_pto() {
        DocumentReference ref_usuarios = db.collection("usuarios").document(id_propietario);
        DocumentReference fincas_ref = ref_usuarios.collection("fincas").document(n_finca);
        final DocumentReference dos = fincas_ref.collection("potreros").document(paddock_name);

        dos.update("pto_estado", estado_pto, "pto_fecha_ingreso", fecha).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    ingreso_anml_pto();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(view.getContext(), "Erro En El Systema", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void ingreso_pto_anmls() {
        DocumentReference ref_usuarios = db.collection("usuarios").document(id_propietario);
        DocumentReference fincas_ref = ref_usuarios.collection("fincas").document(n_finca);
        final CollectionReference dos = fincas_ref.collection("animales");
        dos.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    model_animal = documentSnapshot.toObject(Animal_Model.class);
                    String id = documentSnapshot.getId();
                    dos.document(id).update("anml_pto_estadia", paddock_name).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(view.getContext(), "Animal Agregado Al Potrero Correctamente", Toast.LENGTH_SHORT).show();
                                dismiss();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(view.getContext(), "Existen Algunos Problemas Porfavor Vuelvalo A Intenterlo ", Toast.LENGTH_LONG).show();
                        }
                    });


                }
            }
        });


    }

}
