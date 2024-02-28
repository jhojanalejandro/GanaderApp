package com.agroapp.proyecto_esmeralda.views.manejo_animal_view;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.agroapp.proyecto_esmeralda.R;
import com.agroapp.proyecto_esmeralda.adapters.Animals_Recycle_Adapter;
import com.agroapp.proyecto_esmeralda.interfaces.Share_References_interface;
import com.agroapp.proyecto_esmeralda.modelos.Animal_Model;
import com.agroapp.proyecto_esmeralda.controlador.Share_References_presenter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * A placeholder fragment containing a simple view.
 */
public class Animales_Production_View_Fragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    Animal_Model modelo_animal;
    String farm_name,id_propietario,lotehorras = "horras",nombre_items,animal;
    EditText buscar_nombre;
    ArrayList<Animal_Model> list_h;
    View view;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    Animals_Recycle_Adapter animal_viewhollder;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    SharedPreferences preferences;


    public static Fragment newInstance(int index) {
        Fragment fragment = new Fragment();
        switch (index) {

            case 1:
                fragment = new Vacas_01();
                break;
            case 2:
                fragment = new Vacas_02();
                break;
            case 3:
                fragment = new Vacas_03();
                break;
            case 4:
                fragment = new Vacas_04();
                break;
            case 5:
                fragment = new Animales_Production_View_Fragment();
                break;
            case 6:
                fragment = new Terneras_01();
                break;
            case 7:
                fragment = new Terneras_02();
                break;
            case 8:
                fragment = new Terneras_03();
                break;
            case 9:
                fragment = new Novillas_01();
                break;
            case 10:
                fragment = new Novillas_02();
                break;
            case 11:
                fragment = new Novillas_03();
                break;
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_manejo_animales_prod, container, false);
        variables();
        preferences =  getActivity().getSharedPreferences("preferences", MODE_PRIVATE);
        Share_References_interface share_references_interface = new Share_References_presenter(getContext());
        farm_name = share_references_interface.farm_name(preferences);
        id_propietario = share_references_interface.id_propietario(preferences);
        if ( farm_name != null){
            traer_lotehorras();

        }else {
            Toast.makeText(getContext(), "no tienes acceso a la los procedimientos", Toast.LENGTH_LONG).show();

        }

        return view;
    }

    private void variables() {
        recyclerView = view.findViewById(R.id.resi_view_horras);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        list_h = new ArrayList();
        buscar_nombre = view.findViewById(R.id.edt_buscar_horras);
        modelo_animal = new Animal_Model();
    }
    public void  traer_lotehorras(){
        DocumentReference ref_usuarios = db.collection("usuarios").document(id_propietario);
        DocumentReference fincas_ref = ref_usuarios.collection("fincas").document(farm_name);
        CollectionReference coreff = fincas_ref.collection("animales");
        coreff.whereEqualTo("anml_lote",lotehorras).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    modelo_animal = documentSnapshot.toObject(Animal_Model.class);
                    String id = documentSnapshot.getId();

                    modelo_animal.setAnml_tipo("opciones");
                    modelo_animal.setAnml_salida(id);

                    list_h.add(modelo_animal);
                }

                animal_viewhollder = new Animals_Recycle_Adapter(getContext(), R.layout.item_animal_first,list_h);
                recyclerView.setAdapter(animal_viewhollder);
                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(view.getContext(),layoutManager.getOrientation());
                recyclerView.addItemDecoration(dividerItemDecoration);
                buscar_nombre.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        filtrar(editable.toString());

                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "error" + e, Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void filtrar(String toString) {
        ArrayList<Animal_Model> filtrar_lista = new ArrayList<>();
        for (Animal_Model animal : list_h) {
            if (animal.getAnml_nombre().toLowerCase().contains(toString.toLowerCase())) {
                filtrar_lista.add(animal);
            }

        }
    }
}