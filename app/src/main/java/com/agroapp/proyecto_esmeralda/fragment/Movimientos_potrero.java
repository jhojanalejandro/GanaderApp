package com.agroapp.proyecto_esmeralda.fragment;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import com.agroapp.proyecto_esmeralda.R;
import com.agroapp.proyecto_esmeralda.adapters.Adapter_movt_pto;
import com.agroapp.proyecto_esmeralda.interfaces.Share_References_interface;
import com.agroapp.proyecto_esmeralda.controlador.Share_References_presenter;
import com.agroapp.proyecto_esmeralda.modelos.Animal_Model;
import com.agroapp.proyecto_esmeralda.modelos.Potreros_model;
import com.agroapp.proyecto_esmeralda.views.menejo_potreros.Fragment_Salida_potrero;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.

 */
public class Movimientos_potrero extends Fragment {

    private String farm_name,id_propietario, user_name, paddock_name,estado_pto ="oupado",estado_actual,id;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    SharedPreferences preferences;
    Potreros_model potreros_model;
    ArrayList<Animal_Model> list_anm_pto;
    private ListView lv_mvt_potrero;
    Share_References_interface share_references_interface;

    Animal_Model ficha_animal;
    View view;

    // TODO: Rename and change types of parameters
    private String n_empleado;

    private OnFragmentInteractionListener mListener;

    public Movimientos_potrero() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_movimientos_potrero, container, false);

        share_references_interface = new Share_References_presenter(getContext());
        preferences = getContext().getSharedPreferences("preferences", getContext().MODE_PRIVATE);

        lv_mvt_potrero = view.findViewById(R.id.lv_movi_pto);
        list_anm_pto = new ArrayList<>();
        ficha_animal = new Animal_Model();
        farm_name  = share_references_interface.farm_name(preferences);
        id_propietario = share_references_interface.id_propietario(preferences);
        user_name = share_references_interface.user_name(preferences);
        paddock_name = share_references_interface.paddock_name(preferences);
        if (paddock_name != null && farm_name != null){
            estado_potrero();
        }
        return view;

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    private void consultar_animales_pto(){

        DocumentReference ref_usuarios = db.collection("usuarios").document(id_propietario);
        DocumentReference fincas_ref = ref_usuarios.collection("fincas").document(farm_name);
        final CollectionReference dos = fincas_ref.collection("animales");
        dos.whereEqualTo("anml_pto_estadia",paddock_name).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    ficha_animal = documentSnapshot.toObject(Animal_Model.class);
                    id = documentSnapshot.getId();
                    list_anm_pto.add(ficha_animal);
                }
                lv_mvt_potrero.setAdapter(new Adapter_movt_pto(list_anm_pto,getContext()));

                lv_mvt_potrero.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        ficha_animal = (Animal_Model) adapterView.getItemAtPosition(i);
                        if (ficha_animal.getAnml_nombre() !=null){
                            Fragment_Salida_potrero fragment_salida_potrero = new Fragment_Salida_potrero();
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("salida_animal", ficha_animal.getAnml_nombre());
                            editor.putString("id_propietario", id_propietario);
                            editor.putString("id_animal", id);
                            editor.apply();
                            fragment_salida_potrero.show(getFragmentManager(),"registro de movimientos potrero ");
                        }
                    }
                });
            }
        });

    }
    private void estado_potrero(){
        DocumentReference ref_usuarios = db.collection("usuarios").document(id_propietario);
        DocumentReference fincas_ref = ref_usuarios.collection("fincas").document(farm_name);
        final DocumentReference dos = fincas_ref.collection("potreros").document(paddock_name);
        dos.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    potreros_model = task.getResult().toObject(Potreros_model.class);
                    estado_actual = potreros_model.getPto_estado();
                    if (estado_actual.equals(estado_pto)){
                        consultar_animales_pto();
                    }

                }

            }
        });

    }


}
