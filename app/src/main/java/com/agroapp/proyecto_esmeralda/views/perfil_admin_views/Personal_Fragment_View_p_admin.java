package com.agroapp.proyecto_esmeralda.views.perfil_admin_views;

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
import com.agroapp.proyecto_esmeralda.interfaces.User_Interface;
import com.agroapp.proyecto_esmeralda.controlador.Share_References_presenter;
import com.agroapp.proyecto_esmeralda.controlador.User_Presenter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

public class Personal_Fragment_View_p_admin extends Fragment {

    private String farm_name,tipo= "empleado", user_name,id_propietario;

    SharedPreferences preferences;
    View view;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    Share_References_presenter share_preferences_presenter;
    User_Interface user_interface;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_empleados_p_admin, container, false);

        share_preferences_presenter = new Share_References_presenter(getContext());
        preferences = getContext().getSharedPreferences("preferences", getContext().MODE_PRIVATE);
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);

        user_name = share_preferences_presenter.user_name(preferences);
        farm_name  = share_preferences_presenter.farm_name(preferences);
        id_propietario = share_preferences_presenter.id_propietario(preferences);

        if (farm_name != null & user_name != null){
            recyclerView = view.findViewById(R.id.recycle_view_list_users);
            layoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(layoutManager);
            layoutManager = new LinearLayoutManager(getContext());
            user_interface = new User_Presenter(getContext());
            user_interface.show_users(recyclerView, id_propietario ,farm_name, layoutManager);


        }else {
            Toast.makeText(getContext(), "no tienes acceso a la los procedimientos fragment", Toast.LENGTH_LONG).show();

        }
        return view;
    }

}