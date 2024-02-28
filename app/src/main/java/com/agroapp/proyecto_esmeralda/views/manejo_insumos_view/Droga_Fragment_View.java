package com.agroapp.proyecto_esmeralda.views.manejo_insumos_view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.agroapp.proyecto_esmeralda.R;
import com.agroapp.proyecto_esmeralda.interfaces.Manejo_Insumos_Interface;
import com.agroapp.proyecto_esmeralda.interfaces.Share_References_interface;
import com.agroapp.proyecto_esmeralda.controlador.Manejo_Insumos_Presenter;
import com.agroapp.proyecto_esmeralda.controlador.Share_References_presenter;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import static android.content.Context.MODE_PRIVATE;

/**
 * A placeholder fragment containing a simple view.
 */
public class Droga_Fragment_View extends Fragment {
    private String farm_name, user_name, id_propietario;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    SharedPreferences preferences;
    View view;
    LinearLayoutManager layoutManager;
    RecyclerView recyclerView;
    Manejo_Insumos_Interface manejo_insumos_interface;

    private static final String ARG_SECTION_NUMBER = "section_number";

    public static Fragment newInstance(int index) {


        Fragment fragment = new Fragment();
        switch (index) {

            case 1:
                fragment = new Droga_Fragment_View();
                break;
            case 2:
                fragment = new Herramientas();
                break;
            case 3:
                fragment = new Concentrados_Fragment_View();
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
        view = inflater.inflate(R.layout.fragment_control_insumos, container, false);
        preferences = getActivity().getSharedPreferences("preferences", MODE_PRIVATE);
        Share_References_interface  share_references_interface = new Share_References_presenter(getContext());
        farm_name = share_references_interface.farm_name(preferences);
        user_name = share_references_interface.user_name(preferences);
        id_propietario = share_references_interface.id_propietario(preferences);

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);

        if (farm_name != null & user_name != null){
            DocumentReference[] registros_ref_array = share_references_interface.referencedb_d(id_propietario,farm_name,"vacio");
            DocumentReference fincas_ref = registros_ref_array[0];
            manejo_insumos_interface = new Manejo_Insumos_Presenter(getContext(), fincas_ref);
            recyclerView = view.findViewById(R.id.recycle_view_list_drog);
            layoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(layoutManager);
            layoutManager = new LinearLayoutManager(getContext());
            manejo_insumos_interface.mostrar_insumos("Droga", recyclerView,  layoutManager);
        }else {
            Toast.makeText(getContext(), "no tienes acceso a la los procedimientos fragment", Toast.LENGTH_LONG).show();

        }
        return view;
    }

}