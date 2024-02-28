package com.agroapp.proyecto_esmeralda.views.menejo_potreros;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.agroapp.proyecto_esmeralda.R;
import com.agroapp.proyecto_esmeralda.interfaces.Share_References_interface;
import com.agroapp.proyecto_esmeralda.modelos.Animal_Model;
import com.agroapp.proyecto_esmeralda.controlador.Share_References_presenter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class Fragment_Salida_potrero extends AppCompatDialogFragment {

    Button btn_enviar, btn_cancelar;
    SharedPreferences preferences;
    Animal_Model model_animal;

    String  paddock_name, farm_name, id_propietario, user_name;
    String dia, mes, ano, fecha;

    View view;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.salida_potrero_dialog, null);

        btn_cancelar = view.findViewById(R.id.btn_cancelar_salida_pto);
        btn_enviar = view.findViewById(R.id.btn_enviar_salida_pto);

        Share_References_interface share_references_interface = new Share_References_presenter(getContext());
        preferences = getContext().getSharedPreferences("preferences", getContext().MODE_PRIVATE);
        id_propietario = share_references_interface.id_propietario(preferences);
        farm_name = share_references_interface.farm_name(preferences);
        user_name = share_references_interface.user_name(preferences);
        paddock_name = share_references_interface.paddock_name(preferences);

        builder.setView(view)
                .setTitle("Registro de  Finca");
        btn_enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Share_References_interface share_references_interface = new Share_References_presenter(getContext());
                int[] fecha_hoy  = share_references_interface.date_picker();
                dia = String.valueOf(fecha_hoy[0]);
                mes = String.valueOf(fecha_hoy[1]);
                ano = String.valueOf(fecha_hoy[2]);
                fecha = dia + "/" + mes + "/" + ano;
                salida_pto_colectivo();

            }

        });
        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return builder.create();

    }
    private void salida_pto_colectivo() {
        DocumentReference ref_usuarios = db.collection("usuarios").document(id_propietario);
        DocumentReference fincas_ref = ref_usuarios.collection("fincas").document(farm_name);
        final DocumentReference dos = fincas_ref.collection("potreros").document(paddock_name);
        dos.update("pto_estado", "vacio", "pto_lote", "vacio", "pto_fecha_salida", fecha).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    salida_pto_anmls();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(view.getContext(), "Erro En El Systema" + e, Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public void onResume() {
        super.onResume();
    }

    private void salida_pto_anmls() {
        DocumentReference ref_usuarios = db.collection("usuarios").document(id_propietario);
        DocumentReference fincas_ref = ref_usuarios.collection("fincas").document(farm_name);
        final CollectionReference dos = fincas_ref.collection("animales");
        dos.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    model_animal = documentSnapshot.toObject(Animal_Model.class);
                    String id = documentSnapshot.getId();
                    dos.document(id).update("anml_pto_estadia", "vacio").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(view.getContext(), "Animales SACADOS DEL POTRERO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
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
