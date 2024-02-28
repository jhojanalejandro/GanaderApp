package com.agroapp.proyecto_esmeralda.views.perfil_admin_views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.agroapp.proyecto_esmeralda.R;
import com.agroapp.proyecto_esmeralda.adapters.Recycle_Adapter_Produccion_animal;

import com.agroapp.proyecto_esmeralda.interfaces.Share_References_interface;
import com.agroapp.proyecto_esmeralda.modelos.Animal_Model;
import com.agroapp.proyecto_esmeralda.controlador.Share_References_presenter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;


public class Produccion_animal_Fragment_p_admin extends Fragment {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    SharedPreferences preferences;
    int dia, mes, ano;
    View view;
    Recycle_Adapter_Produccion_animal animal_viewhollder;
    RecyclerView recyclerView_alta_peso, recyclerView_alta_leche, recyclerView_baja_peso, recyclerView_baja_leche;
    LinearLayoutManager layoutManager_alta_l,layoutManager_baja_l,layoutManager_alta_p,layoutManager_baja_p;
    Animal_Model modelo_animal;
    String farm_name, id_propietario, animal;
    ArrayList<Animal_Model> list_animales_alta_leche, list_animales_alta_peso, list_animales_baja_peso, list_animales_baja_leche;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_produccion_p_admin, container, false);
        BottomNavigationView bottomNavigationView = view.findViewById(R.id.nav_view_animales_general);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        iniciar_variables();
        datepikers_hoy();
        Share_References_interface share_references_interface = new Share_References_presenter(getContext());
        farm_name = share_references_interface.farm_name(preferences);
        id_propietario = share_references_interface.id_propietario(preferences);

        traer_animales_alta_medida();
        traer_animales_baja_medida();
        traer_animales_alta_peso();
        traer_animales_baja_peso();
        return view;
    }

    private void iniciar_variables() {

        recyclerView_alta_leche = view.findViewById(R.id.resi_view_animal_produccion_alta_leche);
        recyclerView_alta_peso = view.findViewById(R.id.resi_view_animal_produccion_alta_pesaje);
        recyclerView_baja_leche = view.findViewById(R.id.resi_view_animal_produccion_baja_leche);
        recyclerView_baja_peso = view.findViewById(R.id.resi_view_animal_produccion_baja_pesaje);
        list_animales_alta_leche = new ArrayList();
        list_animales_baja_leche = new ArrayList();
        list_animales_baja_peso = new ArrayList();
        list_animales_alta_peso = new ArrayList();

        layoutManager_alta_l = new LinearLayoutManager(getContext());
        layoutManager_alta_p = new LinearLayoutManager(getContext());
        layoutManager_baja_l = new LinearLayoutManager(getContext());
        layoutManager_baja_p = new LinearLayoutManager(getContext());
        recyclerView_baja_leche.setLayoutManager(layoutManager_baja_l);
        recyclerView_alta_peso.setLayoutManager(layoutManager_alta_p);
        recyclerView_baja_peso.setLayoutManager(layoutManager_baja_p);
        recyclerView_alta_leche.setLayoutManager(layoutManager_alta_l);

        preferences = getContext().getSharedPreferences("preferences", Context.MODE_PRIVATE);


    }

    private void datepikers_hoy() {
        Calendar calendarNow = new GregorianCalendar(TimeZone.getTimeZone("Europe/Madrid"));
        dia = calendarNow.get(Calendar.DAY_OF_MONTH);
        mes = calendarNow.get(Calendar.MONTH) + 1;
        ano = calendarNow.get(Calendar.YEAR);
    }

    public void traer_animales_alta_medida() {
        DocumentReference ref_usuarios = db.collection("usuarios").document(id_propietario);
        DocumentReference fincas_ref = ref_usuarios.collection("fincas").document(farm_name);
        CollectionReference coreff = fincas_ref.collection("animales");
        coreff.whereGreaterThan("anml_prod_litros", 11).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot1 : queryDocumentSnapshots) {
                    if (documentSnapshot1.exists()) {
                        modelo_animal = documentSnapshot1.toObject(Animal_Model.class);
                        String id = documentSnapshot1.getId();
                        modelo_animal.setAnml_salida(id);
                        modelo_animal.setAnml_tipo("medida");
                        list_animales_alta_leche.add(modelo_animal);
                    } else {
                        Toast.makeText(view.getContext(), "No hay Datos", Toast.LENGTH_SHORT).show();
                    }
                }
                animal_viewhollder = new Recycle_Adapter_Produccion_animal(getContext(), R.layout.item_produccion_alta_baja, list_animales_alta_leche);
                recyclerView_alta_leche.setAdapter(animal_viewhollder);
                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(view.getContext(), layoutManager_alta_l.getOrientation());
                recyclerView_alta_leche.addItemDecoration(dividerItemDecoration);


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(view.getContext(), "algo pasa", Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void traer_animales_baja_medida() {
        DocumentReference ref_usuarios = db.collection("usuarios").document(id_propietario);
        DocumentReference fincas_ref = ref_usuarios.collection("fincas").document(farm_name);
        CollectionReference coreff = fincas_ref.collection("animales");
        coreff.whereLessThan("anml_prod_litros", 10).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot1 : queryDocumentSnapshots) {
                    if (documentSnapshot1.exists()) {
                        modelo_animal = documentSnapshot1.toObject(Animal_Model.class);
                        String id = documentSnapshot1.getId();
                        Double litros = modelo_animal.getAnml_prod_litros();
                        if (litros > 0){
                            modelo_animal.setAnml_salida(id);
                            modelo_animal.setAnml_tipo("medida");
                            list_animales_baja_leche.add(modelo_animal);
                        }


                    }
                }

                animal_viewhollder = new Recycle_Adapter_Produccion_animal(getContext(), R.layout.item_produccion_alta_baja, list_animales_baja_leche);
                recyclerView_baja_leche.setAdapter(animal_viewhollder);
                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(view.getContext(), layoutManager_baja_l.getOrientation());
                recyclerView_baja_leche.addItemDecoration(dividerItemDecoration);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(view.getContext(), "algo pasa", Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void traer_animales_alta_peso() {

        DocumentReference ref_usuarios = db.collection("usuarios").document(id_propietario);
        DocumentReference fincas_ref = ref_usuarios.collection("fincas").document(farm_name);
        CollectionReference coreff = fincas_ref.collection("animales");
        coreff.whereGreaterThan("anml_prod_kilos_ganados", 25).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot1 : queryDocumentSnapshots) {
                    if (documentSnapshot1.exists()) {
                        modelo_animal = documentSnapshot1.toObject(Animal_Model.class);
                        String id = documentSnapshot1.getId();
                        modelo_animal.setAnml_salida(id);
                        modelo_animal.setAnml_tipo("pesaje");
                        list_animales_alta_peso.add(modelo_animal);
                    } else {
                        Toast.makeText(view.getContext(), "No hay Datos", Toast.LENGTH_SHORT).show();
                    }
                }

                animal_viewhollder = new Recycle_Adapter_Produccion_animal(getContext(), R.layout.item_produccion_alta_baja, list_animales_alta_peso);
                recyclerView_alta_peso.setAdapter(animal_viewhollder);
                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(view.getContext(), layoutManager_alta_p.getOrientation());
                recyclerView_alta_peso.addItemDecoration(dividerItemDecoration);


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(view.getContext(), "algo pasa", Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void traer_animales_baja_peso() {
        DocumentReference ref_usuarios = db.collection("usuarios").document(id_propietario);
        DocumentReference fincas_ref = ref_usuarios.collection("fincas").document(farm_name);
        CollectionReference coreff = fincas_ref.collection("animales");
        coreff.whereLessThan("anml_prod_kilos_ganados", 25).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot1 : queryDocumentSnapshots) {
                    if (documentSnapshot1.exists()) {
                        modelo_animal = documentSnapshot1.toObject(Animal_Model.class);
                        String id = documentSnapshot1.getId();
                        Double peso = modelo_animal.getAnml_prod_kilos_ganados();
                        if (peso > 0 ){
                            modelo_animal.setAnml_salida(id);
                            modelo_animal.setAnml_tipo("pesaje");
                            list_animales_baja_peso.add(modelo_animal);
                        }

                    }
                }

                animal_viewhollder = new Recycle_Adapter_Produccion_animal(getContext(), R.layout.item_produccion_alta_baja, list_animales_baja_peso);
                recyclerView_baja_peso.setAdapter(animal_viewhollder);
                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(view.getContext(), layoutManager_baja_p.getOrientation());
                recyclerView_baja_peso.addItemDecoration(dividerItemDecoration);


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(view.getContext(), "algo pasa", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @SuppressLint("NonConstantResourceId")
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_prod_alta_medida:
                    view.findViewById(R.id.resi_view_animal_produccion_baja_leche).setVisibility(View.GONE);
                    view.findViewById(R.id.resi_view_animal_produccion_alta_leche).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.resi_view_animal_produccion_alta_pesaje).setVisibility(View.GONE);
                    view.findViewById(R.id.resi_view_animal_produccion_baja_pesaje).setVisibility(View.GONE);
                    break;
                case R.id.navigation_prod_baja_medida:
                    view.findViewById(R.id.resi_view_animal_produccion_baja_leche).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.resi_view_animal_produccion_alta_leche).setVisibility(View.GONE);
                    view.findViewById(R.id.resi_view_animal_produccion_alta_pesaje).setVisibility(View.GONE);
                    view.findViewById(R.id.resi_view_animal_produccion_baja_pesaje).setVisibility(View.GONE);
                    break;
                case R.id.navigation_prod_alta_pesaje:
                    view.findViewById(R.id.resi_view_animal_produccion_baja_leche).setVisibility(View.GONE);
                    view.findViewById(R.id.resi_view_animal_produccion_alta_leche).setVisibility(View.GONE);
                    view.findViewById(R.id.resi_view_animal_produccion_alta_pesaje).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.resi_view_animal_produccion_baja_pesaje).setVisibility(View.GONE);

                    break;
                case R.id.navigation_prod_baja_pesaje:
                    view.findViewById(R.id.resi_view_animal_produccion_baja_leche).setVisibility(View.GONE);
                    view.findViewById(R.id.resi_view_animal_produccion_alta_leche).setVisibility(View.GONE);
                    view.findViewById(R.id.resi_view_animal_produccion_alta_pesaje).setVisibility(View.GONE);
                    view.findViewById(R.id.resi_view_animal_produccion_baja_pesaje).setVisibility(View.VISIBLE);
                    break;
            }

            return true;
        }
    };

}