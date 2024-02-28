package com.agroapp.proyecto_esmeralda.views.perfil_admin_views;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.agroapp.proyecto_esmeralda.R;
import com.agroapp.proyecto_esmeralda.interfaces.Perfil_Admin_Interface;
import com.agroapp.proyecto_esmeralda.controlador.Perfil_Admin_Presenter;
import com.agroapp.proyecto_esmeralda.interfaces.Share_References_interface;
import com.agroapp.proyecto_esmeralda.controlador.Share_References_presenter;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

public class EstadisticasFragment_p_admin extends Fragment {
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    String farm_name,id_propietario,id_animal;
    SharedPreferences preferences;
    Perfil_Admin_Interface perfil_admin_interface;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_gasto_ganancia_p_admin, container, false);

        preferences = getContext().getSharedPreferences("preferences", getContext().MODE_PRIVATE);
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);

        Share_References_interface share_references_interface = new Share_References_presenter(getContext());
        recyclerView = view.findViewById(R.id.resi_view_produccion_mensual);
        farm_name = share_references_interface.farm_name(preferences);
        id_animal = share_references_interface.id_animal(preferences);
        id_propietario = share_references_interface.id_propietario(preferences);
        DocumentReference ref_usuarios = db.collection("usuarios").document(id_propietario);
        DocumentReference fincas_ref = ref_usuarios.collection("fincas").document(farm_name);

        perfil_admin_interface = new Perfil_Admin_Presenter(recyclerView,getContext(), fincas_ref);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        perfil_admin_interface.lista_produccion_mes( layoutManager);


        return view;
    }




}