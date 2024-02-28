package com.agroapp.proyecto_esmeralda.controlador;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.agroapp.proyecto_esmeralda.R;
import com.agroapp.proyecto_esmeralda.adapters.Animals_Recycle_Adapter;
import com.agroapp.proyecto_esmeralda.interfaces.Manejo_Animal_Interface;
import com.agroapp.proyecto_esmeralda.modelos.Animal_Model;
import com.agroapp.proyecto_esmeralda.modelos.Pesaje_Animal_Model;
import com.agroapp.proyecto_esmeralda.views.manejo_animal_view.Manejo_animal_view;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Manejo_Animal_Presenter implements Manejo_Animal_Interface {
    private Animals_Recycle_Adapter animal_viewhollder;
    private int retorno;
    private RecyclerView  recyclerView;
    Context context;
    private CollectionReference animales_refefecnce;

    public Manejo_Animal_Presenter(RecyclerView recyclerView, Context context, CollectionReference animals_refefecnce) {
        this.recyclerView = recyclerView;
        this.context = context;
        this.animales_refefecnce = animals_refefecnce;
    }

    public Manejo_Animal_Presenter(Animals_Recycle_Adapter animal_viewhollder, Context context) {
        this.animal_viewhollder = animal_viewhollder;
        this.context = context;
    }

    public Manejo_Animal_Presenter(Context context, CollectionReference animales_refefecnce) {
        this.context = context;
        this.animales_refefecnce = animales_refefecnce;
    }
    @Override
    public void show_cattle( String paddock_name, EditText edt_search, LinearLayoutManager layoutManager) {
        ArrayList<Animal_Model> list_animal = new ArrayList<>();
        animal_viewhollder = new Animals_Recycle_Adapter(context, R.layout.item_animal_first, list_animal);
        animales_refefecnce.whereEqualTo("anml_tipo", "bovino").whereEqualTo("anml_salida", "vacio").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Animal_Model modelo_animal = new Animal_Model();
                    modelo_animal = documentSnapshot.toObject(Animal_Model.class);
                    String ids = documentSnapshot.getId();
                    modelo_animal.setAnml_tipo(paddock_name);
                    modelo_animal.setAnml_salida(ids);
                    list_animal.add(modelo_animal);
                }
                recyclerView.setAdapter(animal_viewhollder);
                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context, layoutManager.getOrientation());
                recyclerView.addItemDecoration(dividerItemDecoration);
                edt_search.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        filter(editable.toString(), list_animal);

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

    @Override
    public void show_cattle_inpaddock( String paddock_name, EditText edt_search, LinearLayoutManager layoutManager) {
        ArrayList<Animal_Model> list_animal = new ArrayList<>();
        animal_viewhollder = new Animals_Recycle_Adapter(context, R.layout.item_animal_first, list_animal);

        animales_refefecnce.whereEqualTo("anml_pto_estadia", paddock_name).whereEqualTo("anml_salida", "vacio").whereEqualTo("anml_pto_estadia",paddock_name).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Animal_Model modelo_animal;
                    modelo_animal = documentSnapshot.toObject(Animal_Model.class);
                    String ids = documentSnapshot.getId();
                    modelo_animal.setAnml_tipo(paddock_name);
                    modelo_animal.setAnml_salida(ids);
                    list_animal.add(modelo_animal);
                }
                recyclerView.setAdapter(animal_viewhollder);
                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context, layoutManager.getOrientation());
                recyclerView.addItemDecoration(dividerItemDecoration);
                edt_search.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        filter(editable.toString(), list_animal);

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

    @Override
    public void filter(String toString, ArrayList<Animal_Model> list_animal) {
        ArrayList<Animal_Model> filtrar_lista = new ArrayList<>();
        for (Animal_Model animal : list_animal) {
            if (animal.getAnml_nombre().toLowerCase().contains(toString.toLowerCase()) || animal.getAnml_chapeta().toLowerCase().contains(toString.toLowerCase())) {
                filtrar_lista.add(animal);
            }

        }
        animal_viewhollder.filtrar(filtrar_lista);
    }

    @Override
    public int animal_register( Animal_Model modelo_animal, Double peso, String nombre_empleado, ProgressDialog progressDialog) {
        retorno = 0;
        animales_refefecnce.document().set(modelo_animal).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                retorno = 1;
                String fecha = modelo_animal.getAnml_fecha_ingreso();
                String chapeta = modelo_animal.getAnml_chapeta();
                String nombre = modelo_animal.getAnml_nombre();
                if (peso > 0) {
                    registro_peso_animal( "vacio", peso, fecha, chapeta, nombre,progressDialog);
                }else {
                    progressDialog.dismiss();
                    Toast.makeText(context, "Registro Exitoso", Toast.LENGTH_SHORT).show();
                    Intent p_admin = new Intent(context, Manejo_animal_view.class);
                    context.startActivity(p_admin);
                }


            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Se A Producido Un Error De Tipo" + e, Toast.LENGTH_SHORT).show();

            }
        });
        return retorno;
    }


    @Override
    public void registro_peso_animal( String tipo, Double peso, String fecha, String chapeta, String nombre, ProgressDialog progressDialog) {
        animales_refefecnce.get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                Animal_Model animal_model = documentSnapshot.toObject(Animal_Model.class);
                String id = documentSnapshot.getId();
                String chapeta_traida = animal_model.getAnml_chapeta();
                String nombre_traido = animal_model.getAnml_nombre();
                if (nombre.equals(nombre_traido) & chapeta.equals(chapeta_traida)) {
                    final DocumentReference register = animales_refefecnce.document(id).collection("registro_animal").document();
                    Pesaje_Animal_Model pesaje_animal_model;
                    if (tipo.equals("parto")){
                        pesaje_animal_model = new Pesaje_Animal_Model("pesaje", "vacio",peso,0.0, 0.0, 0.0, 0.0, "vacio", "vacio", "vacio", "vacio", fecha, "vacio", 0.0, 0.0);

                    }else {
                        pesaje_animal_model = new Pesaje_Animal_Model("pesaje", "vacio", peso, 0.0, 0.0, 0.0, 0.0,"vacio", "vacio", "vacio", fecha,"vacio", "vacio", 0.0, 0.0);
                    }
                    register.set(pesaje_animal_model).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                Toast.makeText(context, "Registro Exitoso", Toast.LENGTH_SHORT).show();
                                if (!tipo.equals("parto")) {
                                    Intent p_admin = new Intent(context, Manejo_animal_view.class);
                                    context.startActivity(p_admin);
                                }

                            }
                        }
                    });
                    return;
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(context, "ERROR en la conexion o :" + e, Toast.LENGTH_SHORT).show();
            }
        });

    }

}
