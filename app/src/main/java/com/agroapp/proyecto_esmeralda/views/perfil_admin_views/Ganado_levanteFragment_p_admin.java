package com.agroapp.proyecto_esmeralda.views.perfil_admin_views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.agroapp.proyecto_esmeralda.R;
import com.agroapp.proyecto_esmeralda.adapters.Animals_Recycle_Adapter;
import com.agroapp.proyecto_esmeralda.modelos.Animal_Model;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import static android.content.Context.MODE_PRIVATE;


public class Ganado_levanteFragment_p_admin extends Fragment {

    EditText buscar_nombre;
    ArrayList<Animal_Model> list_animals,list_c2;
    Animal_Model modelo_animal;
    Context context;
    ArrayAdapter<Animal_Model> adapter_bovino;

    private String n_finca, tipo = "cliente", cliente;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    SharedPreferences preferences;
    String animal, lote_01 = "novillos01", lote_02 = "novillos02", lote_t03 = "nolvallos03", lote_c01 = "crias01", lote_c03 = "crias03", lote_c02 = "crias02";
    int dia, mes, ano;
    View view;
    RecyclerView recyclerView_c1,recyclerView_c2,recyclerView_c3,recyclerView_nov1,recyclerView_nov2;
    Animals_Recycle_Adapter animal_viewhollder_c1,animal_viewhollder_c2;
    LinearLayoutManager layoutManager_c1,layoutManager_c2;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_ganado_produccion_p_admin, container, false);


        iniciar_variables();
        validar_finca_n();
        datepikers_hoy();
        BottomNavigationView bottomNavigationView = view.findViewById(R.id.nav_aniamles_produccion);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        return view;
    }

    private void iniciar_variables() {
        buscar_nombre = view.findViewById(R.id.edt_buscar_animal_levante);
        recyclerView_c1 = view.findViewById(R.id.recycle_ceva_levante_crias1);
        layoutManager_c1 = new LinearLayoutManager(view.getContext());
        recyclerView_c1.setLayoutManager(layoutManager_c1);

        list_animals = new ArrayList();
        list_c2 = new ArrayList();

    }

    private String validar_finca_n() {
        preferences = getActivity().getSharedPreferences("preferences", MODE_PRIVATE);
        n_finca = preferences.getString("finca", null);

        if (n_finca != null) {
            return n_finca;
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

    public void novillas_02() {
        CollectionReference doref = db.collection("fincas");
        CollectionReference coreff = doref.document(n_finca).collection("animales");
        coreff.whereEqualTo("anml_lote", lote_01).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    if (documentSnapshot.exists()) {
                        modelo_animal = documentSnapshot.toObject(Animal_Model.class);

                        list_animals.add(modelo_animal);
                    } else {
                        Toast.makeText(view.getContext(), "no existen datos", Toast.LENGTH_SHORT).show();
                    }

                }
                animal_viewhollder_c1 = new Animals_Recycle_Adapter(getContext(), R.layout.item_animal_first, list_animals);
                recyclerView_c1.setAdapter(animal_viewhollder_c1);
                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(view.getContext(), layoutManager_c1.getOrientation());
                recyclerView_c1.addItemDecoration(dividerItemDecoration);

                buscar_nombre.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        adapter_bovino.getFilter().filter(charSequence);

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "necesitas ingresar todos los campos", Toast.LENGTH_SHORT).show();

            }
        });


    }

    public void novillas_01() {
        CollectionReference doref = db.collection("fincas");
        CollectionReference coreff = doref.document(n_finca).collection("animales");
        coreff.whereEqualTo("anml_lote", lote_02).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    if (documentSnapshot.exists()) {
                        modelo_animal = documentSnapshot.toObject(Animal_Model.class);
                        list_animals.add(modelo_animal);
                    } else {
                        Toast.makeText(view.getContext(), "no existen datos", Toast.LENGTH_SHORT).show();
                    }

                }


                buscar_nombre.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        adapter_bovino.getFilter().filter(charSequence);

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "necesitas ingresar todos los campos", Toast.LENGTH_SHORT).show();

            }
        });


    }

    public void crias_03() {
        CollectionReference doref = db.collection("fincas");
        CollectionReference coreff = doref.document(n_finca).collection("animales");
        coreff.whereEqualTo("anml_lote", lote_c03).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    if (documentSnapshot.exists()) {
                        modelo_animal = documentSnapshot.toObject(Animal_Model.class);
                        list_animals.add(modelo_animal);
                    } else {
                        Toast.makeText(view.getContext(), "no existen datos", Toast.LENGTH_SHORT).show();
                    }

                }

                animal_viewhollder_c1 = new Animals_Recycle_Adapter(getContext(), R.layout.item_animal_first, list_animals);
                recyclerView_c1.setAdapter(animal_viewhollder_c1);
                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(view.getContext(), layoutManager_c1.getOrientation());
                recyclerView_c1.addItemDecoration(dividerItemDecoration);

                buscar_nombre.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        adapter_bovino.getFilter().filter(charSequence);

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "necesitas ingresar todos los campos", Toast.LENGTH_SHORT).show();

            }
        });


    }

    public void novillas_03() {
        CollectionReference doref = db.collection("fincas");
        CollectionReference coreff = doref.document(n_finca).collection("animales");
        coreff.whereEqualTo("anml_lote", lote_t03).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    if (documentSnapshot.exists()) {
                        modelo_animal = documentSnapshot.toObject(Animal_Model.class);
                        list_animals.add(modelo_animal);
                    } else {
                        Toast.makeText(view.getContext(), "no existen datos", Toast.LENGTH_SHORT).show();
                    }

                }

                buscar_nombre.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        adapter_bovino.getFilter().filter(charSequence);

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "necesitas ingresar todos los campos", Toast.LENGTH_SHORT).show();

            }
        });


    }

    public void crias_02() {
        CollectionReference doref = db.collection("fincas");
        CollectionReference coreff = doref.document(n_finca).collection("animales");
        coreff.whereEqualTo("anml_tipo", "bovino").whereEqualTo("anml_salida", "vacio").whereEqualTo("anml_lote", lote_c02).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    if (documentSnapshot.exists()) {
                        modelo_animal = documentSnapshot.toObject(Animal_Model.class);
                        list_c2.add(modelo_animal);
                    } else {
                        Toast.makeText(view.getContext(), "no existen datos", Toast.LENGTH_SHORT).show();
                    }

                }
                animal_viewhollder_c2 = new Animals_Recycle_Adapter(getContext(), R.layout.item_animal_first, list_c2);
                recyclerView_c2.setAdapter(animal_viewhollder_c2);
                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(view.getContext(), layoutManager_c2.getOrientation());
                recyclerView_c2.addItemDecoration(dividerItemDecoration);

                buscar_nombre.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        adapter_bovino.getFilter().filter(charSequence);

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "necesitas ingresar todos los campos", Toast.LENGTH_SHORT).show();

            }
        });


    }

    public void crias_01() {
        CollectionReference doref = db.collection("fincas");
        CollectionReference coreff = doref.document(n_finca).collection("animales");
        coreff.whereEqualTo("anml_tipo","bovino").whereEqualTo("anml_salida","vacio").whereEqualTo("anml_lote", lote_c01).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    if (documentSnapshot.exists()) {
                        modelo_animal = documentSnapshot.toObject(Animal_Model.class);
                        list_animals.add(modelo_animal);
                    } else {
                        Toast.makeText(view.getContext(), "no existen datos", Toast.LENGTH_SHORT).show();
                    }

                }
                animal_viewhollder_c1 = new Animals_Recycle_Adapter(getContext(), R.layout.item_animal_first, list_animals);
                recyclerView_c1.setAdapter(animal_viewhollder_c1);
                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(view.getContext(), layoutManager_c1.getOrientation());
                recyclerView_c1.addItemDecoration(dividerItemDecoration);
                buscar_nombre.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        adapter_bovino.getFilter().filter(charSequence);

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "necesitas ingresar todos los campos", Toast.LENGTH_SHORT).show();

            }
        });


    }

    public BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @SuppressLint("NonConstantResourceId")
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_terneras_01:
                    crias_01();
                    break;
                case R.id.navigation_terneras_02:
                    crias_03();
                    break;
                case R.id.navigation_terneras_03:
                    crias_02();
                    break;
                case R.id.navigation_novillas1:
                    novillas_01();
                    break;
                case R.id.navigation_novillas2:
                    novillas_02();
                    break;
            }

            return true;
        }
    };

}