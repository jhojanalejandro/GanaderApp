package com.agroapp.proyecto_esmeralda.controlador;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.agroapp.proyecto_esmeralda.R;
import com.agroapp.proyecto_esmeralda.adapters.Paddock_Recycle_Adapter;
import com.agroapp.proyecto_esmeralda.interfaces.Manejo_Potreros_Interface;
import com.agroapp.proyecto_esmeralda.modelos.Potreros_model;
import com.agroapp.proyecto_esmeralda.views.menejo_potreros.Lista_Potreros_View;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Manejo_Potreros_Presenter implements Manejo_Potreros_Interface {

    Paddock_Recycle_Adapter paddock_viewhollder;
    ArrayAdapter<Potreros_model> adapter_potrero;
    int cant_animales = 0;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public void paddock_register(Context context,String id_propietario, String farm_name,Object object_model, String potrero) {

        DocumentReference ref_usuarios = db.collection("usuarios").document(id_propietario);
        DocumentReference fincas_ref = ref_usuarios.collection("fincas").document(farm_name);
        DocumentReference coreff = fincas_ref.collection("potreros").document(potrero);
        coreff.set(object_model).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(context, "registrio Exitoso", Toast.LENGTH_SHORT).show();
                    Intent manejo = new Intent(context, Lista_Potreros_View.class);
                    context.startActivity(manejo);

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "registrio denegado", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void show_paddock(Context context, FirebaseFirestore db, RecyclerView recyclerView, String id_propietario, String farm_name, EditText edt_search, LinearLayoutManager layoutManager) {
        ArrayList<Potreros_model> list_potreros = new ArrayList<>();
        paddock_viewhollder = new Paddock_Recycle_Adapter(context, R.layout.item_potrero, list_potreros);

        DocumentReference ref_usuarios = db.collection("usuarios").document(id_propietario);
        DocumentReference fincas_ref = ref_usuarios.collection("fincas").document(farm_name);
        CollectionReference potreros_ref = fincas_ref.collection("potreros");
        
        potreros_ref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Potreros_model potreros_model = documentSnapshot.toObject(Potreros_model.class);
                    String potrero = potreros_model.getPto_nombre();
                    fincas_ref.collection("animales").whereEqualTo("anml_pto_estadia", potrero).whereEqualTo("anml_salida", "vacio").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots1) {
                            for (DocumentSnapshot documentSnapshot1 : queryDocumentSnapshots1){
                                if (documentSnapshot1.exists()){
                                    cant_animales += 1;
                                }
                            }
                        }
                    });
                    potreros_model.setPto_cant_anml_pto(cant_animales);
                    list_potreros.add(potreros_model);
                }
                recyclerView.setAdapter(paddock_viewhollder);
                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context, layoutManager.getOrientation());
                recyclerView.addItemDecoration(dividerItemDecoration);
                edt_search.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        adapter_potrero.getFilter().filter(charSequence);

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        filter(editable.toString(), list_potreros);

                    }
                });


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void filter(String toString, ArrayList<Potreros_model> list_potreros) {
        ArrayList<Potreros_model> filtrar_lista = new ArrayList<>();
        for (Potreros_model potreros : list_potreros) {
            if (potreros.getPto_nombre().toLowerCase().contains(toString.toLowerCase())) {
                filtrar_lista.add(potreros);
            }

        }
        paddock_viewhollder.filtrar(filtrar_lista);
    }


}
