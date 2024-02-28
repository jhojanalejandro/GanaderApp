package com.agroapp.proyecto_esmeralda.controlador;

import android.content.Context;
import android.content.Intent;

import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.agroapp.proyecto_esmeralda.R;
import com.agroapp.proyecto_esmeralda.adapters.Adapter_List_Subastas;
import com.agroapp.proyecto_esmeralda.interfaces.Manejo_Subastas_Interface;
import com.agroapp.proyecto_esmeralda.modelos.Subasta_Model;
import com.agroapp.proyecto_esmeralda.views.manejo_subastas_view.Lista_Subatas_usuarios;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class Manejo_Subastas_Presenter implements Manejo_Subastas_Interface {
    Adapter_List_Subastas asubasta_viewhollder;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Context context;

    public Manejo_Subastas_Presenter(Context context) {
        this.context = context;
    }

    @Override
    public void mostrar_subastas( RecyclerView recyclerView, LinearLayoutManager layoutManager) {
        CollectionReference subastas_ref = db.collection("subastas");
        ArrayList<Subasta_Model> lista_subastas = new ArrayList<>();
        asubasta_viewhollder = new Adapter_List_Subastas(context, R.layout.item_list_subastas, lista_subastas);

        subastas_ref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Subasta_Model subasta_model = documentSnapshot.toObject(Subasta_Model.class);
                    String asignada = subasta_model.getResultado();
                    if(asignada.equals("ENPROCESO")){
                        lista_subastas.add(subasta_model);
                    }

                }
                recyclerView.setAdapter(asubasta_viewhollder);
                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context, layoutManager.getOrientation());
                recyclerView.addItemDecoration(dividerItemDecoration);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void mostrar_subastas_propietario( RecyclerView recyclerView, LinearLayoutManager layoutManager, String cedula) {

        CollectionReference subastas_ref = db.collection("subastas");
        ArrayList<Subasta_Model> lista_subastas = new ArrayList<>();
        asubasta_viewhollder = new Adapter_List_Subastas(context, R.layout.item_list_subastas_propietario, lista_subastas);

        subastas_ref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Subasta_Model subasta_model = documentSnapshot.toObject(Subasta_Model.class);
                    String id_vendedor = subasta_model.getId_vendedor();
                    String id_comprador = subasta_model.getId_comprador();
                    if (cedula.equals(id_comprador) || cedula.equals(id_vendedor)) {
                        lista_subastas.add(subasta_model);
                    }

                }
                recyclerView.setAdapter(asubasta_viewhollder);
                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context, layoutManager.getOrientation());
                recyclerView.addItemDecoration(dividerItemDecoration);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public void agregar_animales_asubastar(String id_propietario, String ids_animal, String finca, String id_subasta) {
        DocumentReference ref_usuarios = db.collection("usuarios").document(id_propietario);
        DocumentReference fincas_ref = ref_usuarios.collection("fincas").document(finca);
        final DocumentReference query = fincas_ref.collection("animales").document(ids_animal);
        query.update("anml_id_subasta", id_subasta, "anml_subasta", "esperando").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(context, "ANIMAL AGREGADO ALA LISTA", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    public void multimedia_subasta( StorageReference storageReference, ImageView img_01, ImageView img_02, ImageView img_03, ImageView img_04, VideoView vd_anaml, String id_animal, String id_propietario, String farm_name) {
        DocumentReference ref_usuarios = db.collection("usuarios").document(id_propietario);
        DocumentReference fincas_ref = ref_usuarios.collection("fincas").document(farm_name);
        DocumentReference sub_ref = fincas_ref.collection("animales").document(id_animal);

        sub_ref.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Subasta_Model subasta_model = new Subasta_Model();
                subasta_model = task.getResult().toObject(Subasta_Model.class);
                String img_011 = subasta_model.getImagenes_desmostracion().get(0);
                String img_021 = subasta_model.getImagenes_desmostracion().get(1);
                String img_031 = subasta_model.getImagenes_desmostracion().get(2);
                String img_041 = subasta_model.getImagenes_desmostracion().get(3);
                String video = subasta_model.getVideo_demostracion();

            }
        });
    }

    @Override
    public void pujar_subasta( Long puja, String id) {

        DocumentReference subasta_ref = db.collection("subastas").document(id);
        subasta_ref.update("sub_puja", puja).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(context, "Pujado", Toast.LENGTH_SHORT).show();
                }
            }
        }).notifyAll();


    }

    @Override
    public String subastar_animales( Subasta_Model class_name) {

        DocumentReference docRef = db.collection("subastas").document();
        String id = docRef.getId();
        class_name.setId_venta(id);
        docRef.set(class_name).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(context, "Registro Exitoso", Toast.LENGTH_SHORT).show();

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Error En Los Datos" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return id;

    }

    @Override
    public String actualizar_animales( Subasta_Model class_name) {

        DocumentReference docRef = db.collection("subastas").document();
        String id = docRef.getId();
        class_name.setId_venta(id);
        String descripcion = class_name.getDescripcion();
        Long precio = class_name.getPrecio_total();
        String obsservaciones = class_name.getObservaciones();
        int cantidad_animales = class_name.getCantidad_animales();

        docRef.update("sub_descripcion", descripcion, "").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(context, "Registro Exitoso", Toast.LENGTH_SHORT).show();
                    Intent registro_subasta = new Intent(context, Lista_Subatas_usuarios.class);
                    context.startActivity(registro_subasta);

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Error En Los Datos" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return id;

    }
}
