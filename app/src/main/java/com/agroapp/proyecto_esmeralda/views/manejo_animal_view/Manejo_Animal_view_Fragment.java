package com.agroapp.proyecto_esmeralda.views.manejo_animal_view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.agroapp.proyecto_esmeralda.R;

import com.agroapp.proyecto_esmeralda.interfaces.Manejo_Animal_Interface;
import com.agroapp.proyecto_esmeralda.controlador.Manejo_Animal_Presenter;
import com.agroapp.proyecto_esmeralda.controlador.Share_References_presenter;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

/**
 * A placeholder fragment containing a simple view.
 */
public class Manejo_Animal_view_Fragment extends Fragment {
    EditText buscar_nombre;
    private String paddock_name, farm_name, user_name, id_propietario, id_tipo_subasta;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    RecyclerView recyclerView;

    SharedPreferences preferences;
    View view;
    LinearLayoutManager layoutManager;
    Share_References_presenter share_preferences_presenter;
    Manejo_Animal_Interface manejo_animal_interface;

    public static Fragment newInstance(int index) {
        Fragment fragment = new Fragment();
        switch (index) {
            case 1:
                fragment = new Manejo_Animal_view_Fragment();
                break;
            case 2:
                fragment = new Equines_View_Fragment();
                break;
            case 3:
                fragment = new Pigs_View_Fragment();
                break;
        }

        return fragment;
    }


    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_manejo_animal, container, false);

        buscar_nombre = view.findViewById(R.id.edt_buscar_bovino_nombre);
        recyclerView = view.findViewById(R.id.resi_view_animal);
        share_preferences_presenter = new Share_References_presenter(getContext());

        preferences = getContext().getSharedPreferences("preferences", getContext().MODE_PRIVATE);
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);

        user_name = share_preferences_presenter.user_name(preferences);
        farm_name = share_preferences_presenter.farm_name(preferences);
        id_propietario = share_preferences_presenter.id_propietario(preferences);
        paddock_name = share_preferences_presenter.paddock_name(preferences);
        id_tipo_subasta = share_preferences_presenter.id_subasta(preferences);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        if (farm_name != null & id_propietario != null) {
            DocumentReference ref_usuarios = db.collection("usuarios").document(id_propietario);
            DocumentReference fincas_ref = ref_usuarios.collection("fincas").document(farm_name);
            CollectionReference coreff = fincas_ref.collection("animales");
            manejo_animal_interface = new Manejo_Animal_Presenter(recyclerView, getContext(), coreff);

        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (farm_name != null & user_name != null) {
            if (paddock_name == null) {
                paddock_name = "bovino";
            }
            if (id_tipo_subasta != null) {
                paddock_name = "subasta";
            }
            manejo_animal_interface.show_cattle(paddock_name, buscar_nombre, layoutManager);

        } else {
            Toast.makeText(getContext(), "no tienes acceso a la los procedimientos fragment", Toast.LENGTH_LONG).show();

        }
    }


}