package com.agroapp.proyecto_esmeralda.views.perfil_animal_views;

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
import com.agroapp.proyecto_esmeralda.adapters.Gasto_Insumos_Recycle_Anmls_Adapter;
import com.agroapp.proyecto_esmeralda.interfaces.Perfil_Animal_Interface;
import com.agroapp.proyecto_esmeralda.interfaces.Share_References_interface;
import com.agroapp.proyecto_esmeralda.modelos.Gastos_Insumos;
import com.agroapp.proyecto_esmeralda.controlador.Perfil_animal_Presenter;
import com.agroapp.proyecto_esmeralda.controlador.Share_References_presenter;
import com.google.firebase.firestore.CollectionReference;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


public class Parts_View_Fragment_p_animal extends Fragment {
    SharedPreferences preferences;
    String farm_name,id_animal, id_propietario;
    LinearLayoutManager layoutManager;
    Gasto_Insumos_Recycle_Anmls_Adapter common_data_viewhollder;
    RecyclerView recyclerView;
    Perfil_Animal_Interface perfil_animal_interface;
    Share_References_interface share_references_interface;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.p_animal_fragment_partos_view, container, false);
        preferences = getContext().getSharedPreferences("preferences", MODE_PRIVATE);

        share_references_interface = new Share_References_presenter(getContext());
        id_animal  = share_references_interface.id_animal(preferences);
        farm_name = share_references_interface.farm_name(preferences);
        id_propietario = share_references_interface.id_propietario(preferences);

        if (farm_name != null && id_animal != null){
            CollectionReference[] registros_ref_array = share_references_interface.referencedb_c(id_propietario,farm_name,id_animal);
            CollectionReference registro_animal_ref = registros_ref_array[1];
            recyclerView = view.findViewById(R.id.recycle_p_animal_parts_view);
            layoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(layoutManager);
            ArrayList<Gastos_Insumos> list_common_data_type = new ArrayList<>();
            common_data_viewhollder = new Gasto_Insumos_Recycle_Anmls_Adapter(getContext(), R.layout.item_data_type_animal, list_common_data_type);
            perfil_animal_interface = new Perfil_animal_Presenter(getContext(), recyclerView,registro_animal_ref,layoutManager);
            perfil_animal_interface.mostrar_registros_animal(  id_animal, "parto",list_common_data_type, common_data_viewhollder);
        }

        return view;
    }

}