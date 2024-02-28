package com.agroapp.proyecto_esmeralda.views.perfil_admin_views;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.agroapp.proyecto_esmeralda.R;
import com.agroapp.proyecto_esmeralda.interfaces.Perfil_Admin_Interface;
import com.agroapp.proyecto_esmeralda.controlador.Perfil_Admin_Presenter;
import com.agroapp.proyecto_esmeralda.controlador.Share_References_presenter;
import com.google.firebase.firestore.CollectionReference;

public class General_baseAnimalFragment_p_admin extends Fragment {

    EditText buscar_nombre;

    private String farm_name, id_propietario;

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;


    SharedPreferences preferences;
    View view;
    Share_References_presenter share_preferences_presenter;
    Perfil_Admin_Interface manejo_animal_interface;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.fragment_lista_general_animales, container, false);

        buscar_nombre = view.findViewById(R.id.edt_buscar_lista_animal);
        recyclerView = view.findViewById(R.id.resi_view_animal_lista_general);
        share_preferences_presenter = new Share_References_presenter(getContext());
        preferences = getContext().getSharedPreferences("preferences", getContext().MODE_PRIVATE);
        farm_name = share_preferences_presenter.farm_name(preferences);
        id_propietario = share_preferences_presenter.id_propietario(preferences);

        if (farm_name != null & id_propietario != null) {
            CollectionReference[] registro_animales_array = share_preferences_presenter.referencedb_c(id_propietario, farm_name, "vacio");
            CollectionReference animales_ref = registro_animales_array[0];
            manejo_animal_interface = new Perfil_Admin_Presenter(getContext(), animales_ref);
            layoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(layoutManager);
            if (farm_name != null) {
                manejo_animal_interface.show_all_cattle(recyclerView, buscar_nombre, layoutManager);
            }

        }
        return view;

    }

}