package com.agroapp.proyecto_esmeralda.views.manejo_insumos_view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;


import com.agroapp.proyecto_esmeralda.R;
import com.agroapp.proyecto_esmeralda.interfaces.Share_References_interface;
import com.agroapp.proyecto_esmeralda.modelos.Insumo_Finca_Model;
import com.agroapp.proyecto_esmeralda.modelos.Insumo_Animal_Model;
import com.agroapp.proyecto_esmeralda.controlador.Share_References_presenter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

public class Registro_insumos_animal_dialog extends AppCompatDialogFragment {

    EditText edt_cant_droga, edt_trat, edt_obs;
    View view;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    SharedPreferences preferences;
    Insumo_Animal_Model insumo_vaca;
    String farm_name, id_propietario, droga, n_empleado, id_animal;
    Spinner spinner_insumos;
    String insumo;
    Insumo_Finca_Model insumo_finca;
    double cant_ins;
    Boolean boleano_insumos = false;
    int dia, mes, ano, cant_droga, gasto;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.registro_insumos_animal_dialog, null);
        iniciar_variables();


        builder.setView(view)
                .setTitle("Registro insumo")
                .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dismiss();

                    }
                })
                .setPositiveButton("GUARDAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        guardar_insumo_vaca();
                    }


                });
        return builder.create();
    }

    private void iniciar_variables() {
        edt_cant_droga = view.findViewById(R.id.edt_r_ins_cant_droga);
        edt_trat = view.findViewById(R.id.edt_r_ins_trat);
        edt_obs = view.findViewById(R.id.edt_r_ins_obs);
        spinner_insumos = view.findViewById(R.id.spinner_animal_ins);
        datepikers_hoy();
        preferences = getContext().getSharedPreferences("preferences", Context.MODE_PRIVATE);
        Share_References_interface share_references_presenter = new Share_References_presenter(getContext());
        farm_name = share_references_presenter.farm_name(preferences);
        id_propietario = share_references_presenter.id_propietario(preferences);
        id_animal = share_references_presenter.id_animal(preferences);

        if (farm_name == null || id_propietario == null) {
            Toast.makeText(getContext(), "no se podra guardar la imformacion", Toast.LENGTH_SHORT).show();
        } else {
            spinner_traer_insumos();
        }
    }

    private void guardar_insumo_vaca() {
        consultar_cant_insumo();
        droga = spinner_insumos.getSelectedItem().toString();
        cant_droga = Integer.parseInt(edt_cant_droga.getText().toString());
        String trat = edt_obs.getText().toString();
        String tipo = edt_obs.getText().toString();
        String obs = edt_trat.getText().toString();

        DocumentReference ref_usuarios = db.collection("usuarios").document(id_propietario);
        DocumentReference fincas_ref = ref_usuarios.collection("fincas").document(farm_name);
        DocumentReference dos = fincas_ref.collection("animales").document();

        insumo_vaca = new Insumo_Animal_Model();
        insumo_vaca.setIns_cant(cant_droga);
        insumo_vaca.setIns_animal_n_empleado(n_empleado);
        insumo_vaca.setIns_animal_tipo(tipo);
        insumo_vaca.setIns_animal_tratamiento(trat);
        insumo_vaca.setIns_animal_observaciones(obs);
        insumo_vaca.setIns_nombre(droga);

        dos.set(insumo_vaca);
    }

    public void spinner_traer_insumos() {
        DocumentReference ref_usuarios = db.collection("usuarios").document(id_propietario);
        DocumentReference fincas_ref = ref_usuarios.collection("fincas").document(farm_name);
        Query query = fincas_ref.collection("insumos");
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot queryDocumentSnapshots = task.getResult();
                    if (queryDocumentSnapshots.isEmpty()) {
                        Toast.makeText(view.getContext(), "no hay insumos: ", Toast.LENGTH_LONG).show();
                        boleano_insumos = false;

                    } else {
                        boleano_insumos = true;
                        final List<String> insumos = new ArrayList<>();
                        for (DocumentSnapshot readData : queryDocumentSnapshots.getDocuments()) {
                            insumo = readData.get("ins_finca_nombre").toString();
                            insumos.add(insumo);
                        }
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, insumos);
                        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_insumos.setAdapter(arrayAdapter);
                        spinner_insumos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                                Object item = parent.getItemAtPosition(pos);
                                droga = (String) item;
                            }

                            public void onNothingSelected(AdapterView<?> parent) {
                                Toast.makeText(getContext(), "se produjo un error" + parent, Toast.LENGTH_SHORT).show();

                            }
                        });

                    }

                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("finca_nombre", e.getMessage());
                    }
                });

    }
    private void datepikers_hoy() {
        Calendar calendarNow = new GregorianCalendar(TimeZone.getTimeZone("Europe/Madrid"));
        dia = calendarNow.get(Calendar.DAY_OF_MONTH);
        mes = calendarNow.get(Calendar.MONTH) + 1;
        ano = calendarNow.get(Calendar.YEAR);
    }
    private void consultar_cant_insumo() {
        if (droga != null) {
            DocumentReference ref_usuarios = db.collection("usuarios").document(id_propietario);
            DocumentReference fincas_ref = ref_usuarios.collection("fincas").document(farm_name);
            final DocumentReference insumo_ref = fincas_ref.collection("insumos").document(droga);
            insumo_ref.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    insumo_finca = document.toObject(Insumo_Finca_Model.class);
                    gasto = insumo_finca.getIns_finca_restante();
                    gasto = -cant_droga;
                    insumo_ref.update("ins_finca_restante", gasto);

                } else {
                    Toast.makeText(getContext(), " error " + task.getException(), Toast.LENGTH_LONG).show();
                }


            });

        }

    }

}
