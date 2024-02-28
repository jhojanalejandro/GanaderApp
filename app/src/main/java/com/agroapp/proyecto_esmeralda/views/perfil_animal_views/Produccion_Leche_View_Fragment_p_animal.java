package com.agroapp.proyecto_esmeralda.views.perfil_animal_views;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.agroapp.proyecto_esmeralda.R;
import com.agroapp.proyecto_esmeralda.adapters.Produccion_Leche_Recycle_Anmls_Adapter;
import com.agroapp.proyecto_esmeralda.interfaces.Share_References_interface;
import com.agroapp.proyecto_esmeralda.modelos.Medida_Leche_Model;
import com.agroapp.proyecto_esmeralda.controlador.Share_References_presenter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Produccion_Leche_View_Fragment_p_animal extends Fragment {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String id_animal, farm_name, id_propietario;
    private View view;
    private Medida_Leche_Model medida_leche;
    Share_References_interface share_references_interface;
    Produccion_Leche_Recycle_Anmls_Adapter leche_viewhollder;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    private ArrayList<Medida_Leche_Model> list_prod_individual_leche;
    private SharedPreferences preferences;

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.fragment_produccion_leche_p_animal, container, false);
        share_references_interface = new Share_References_presenter(getContext());
        recyclerView = view.findViewById(R.id.reclycle_leche);

        preferences = getContext().getSharedPreferences("preferences", Context.MODE_PRIVATE);
        list_prod_individual_leche = new ArrayList<>();

        id_animal = share_references_interface.id_animal(preferences);
        farm_name = share_references_interface.farm_name(preferences);
        id_propietario = share_references_interface.id_propietario(preferences);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        if (farm_name != null && id_animal != null) {
            traer_prod_leche_animal();

        } else {
            Toast.makeText(view.getContext(), "algo pasa", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    private void traer_prod_leche_animal() {
        DocumentReference ref_usuarios = db.collection("usuarios").document(id_propietario);
        DocumentReference fincas_ref = ref_usuarios.collection("fincas").document(farm_name);
        final DocumentReference coreff = fincas_ref.collection("animales").document(id_animal);
        final CollectionReference regis_ref = coreff.collection("registro_animal");

        regis_ref.orderBy("medida_anml_fecha", Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    if (documentSnapshot.exists()) {
                        String id_doc = documentSnapshot.getId();
                        medida_leche = documentSnapshot.toObject(Medida_Leche_Model.class);
                        medida_leche.setMedida_tipo(id_doc);
                        list_prod_individual_leche.add(medida_leche);
                    }

                }
                leche_viewhollder = new Produccion_Leche_Recycle_Anmls_Adapter(getContext(), R.layout.item_produccion_individual, list_prod_individual_leche);
                recyclerView.setAdapter(leche_viewhollder);
                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), layoutManager.getOrientation());
                recyclerView.addItemDecoration(dividerItemDecoration);

            }
        });
    }


}