package com.agroapp.proyecto_esmeralda.views.menejo_potreros;

import android.app.AlertDialog;
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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.agroapp.proyecto_esmeralda.R;
import com.agroapp.proyecto_esmeralda.interfaces.Share_References_interface;
import com.agroapp.proyecto_esmeralda.modelos.Potreros_model;
import com.agroapp.proyecto_esmeralda.controlador.Share_References_presenter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Registro_potrero_dialog extends AppCompatDialogFragment {

    EditText edt_nombre, edt_obs_potrero, edt_extension;
    View view;
    Potreros_model modelo_potrero;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    SharedPreferences preferences;
    String farm_name,id_propietario, user_name, eleccion_tipo;
    Spinner spinner_tipo_pasto;
    ProgressDialog progressDialog;
    ArrayAdapter comboAdapter;
    Context context;
    String[] tipo;

    public Registro_potrero_dialog(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.registro_potrero_dialog, null);
        iniciar_variables();
        tipo = new String[]{"angleton", "pangola", "estrella africana", "guinea"};
        Share_References_interface share_references_interface = new Share_References_presenter(getContext());
        farm_name = share_references_interface.farm_name(preferences);
        user_name = share_references_interface.user_name(preferences);
        id_propietario = share_references_interface.id_propietario(preferences);
        if (farm_name  == null || user_name == null) {
            Toast.makeText(getContext(), "no hay un nombre de finca valido", Toast.LENGTH_SHORT).show();
            System.exit(0);
        }


        spinner_tipo_pasto.setAdapter(comboAdapter);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, tipo);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_tipo_pasto.setAdapter(arrayAdapter);
        spinner_tipo_pasto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {
                Object item = parent.getItemAtPosition(pos);
                eleccion_tipo = (String) item;
            }

            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(view.getContext(), "error: campo vacio", Toast.LENGTH_SHORT).show();
            }
        });


        builder.setView(view)
                .setTitle("Registro potrero")
                .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dismiss();
                    }
                })
                .setPositiveButton("ENVIAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        agregar_potreros();
                    }


                });

        return builder.create();

    }

    private void iniciar_variables() {
        spinner_tipo_pasto = view.findViewById(R.id.spinner_tipo_pasto_r_pto);
        edt_nombre = view.findViewById(R.id.et_registro_potrero_nombre);
        edt_obs_potrero = view.findViewById(R.id.edt_r_pto_obs);
        edt_extension = view.findViewById(R.id.edt_r_pto_hect);
        progressDialog = new ProgressDialog(getContext());
        preferences = getContext().getSharedPreferences("preferences", Context.MODE_PRIVATE);


    }
    private void agregar_potreros() {

        progressDialog.setTitle("cargando...");
        progressDialog.setCancelable(false);
        progressDialog.setIcon(R.drawable.ic_three_anmls_small);
        progressDialog.show();
        String potrero = edt_nombre.getText().toString();
        String obs_potrero = edt_obs_potrero.getText().toString();
        String extension = edt_extension.getText().toString();
        DocumentReference ref_usuarios = db.collection("usuarios").document(id_propietario);
        DocumentReference fincas_ref = ref_usuarios.collection("fincas").document(farm_name);
        DocumentReference coreff = fincas_ref.collection("potreros").document(potrero);

        int ext = Integer.parseInt(extension);
        modelo_potrero = new Potreros_model();
        modelo_potrero.setPto_nombre(potrero);
        modelo_potrero.setPto_observaciones(obs_potrero);
        modelo_potrero.setPto_tipo_pasto(eleccion_tipo);
        modelo_potrero.setPto_estado("vacio");
        modelo_potrero.setPto_cant_anml_pto(0);
        modelo_potrero.setPto_lote("vacio");
        modelo_potrero.setPto_extension(ext);

        coreff.set(modelo_potrero).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(view.getContext(), "registrio Exitoso", Toast.LENGTH_SHORT).show();
                    Intent manejo_p = new Intent(context, Lista_Potreros_View.class);
                    context.startActivity(manejo_p);
                    progressDialog.dismiss();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "registrio denegado", Toast.LENGTH_SHORT).show();

            }
        });
    }

}
