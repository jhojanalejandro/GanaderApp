package com.agroapp.proyecto_esmeralda.views.manejo_animal_view;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class Novillas_01 extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    SharedPreferences preferences;
    Animal_Model modelo_animal;
    String farm_name, id_propietario;
    private final  String lote_01 = "novillos01";
    EditText buscar_nombre;

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    Animals_Recycle_Adapter animal_viewhollder;
    ArrayAdapter<Animal_Model> adapter_bovino;
    ArrayList<Animal_Model> list_01;
    private View view;

    // TODO: Rename and change types of parameters
    private OnFragmentInteractionListener mListener;

    public Novillas_01() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.

     * @return A new instance of fragment Lista_Equinos.
     */
    // TODO: Rename and change types and number of parameters


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_lista_vacas_01, container, false);
        buscar_nombre = view.findViewById(R.id.edt_buscar_vacas_01);
        recyclerView = view.findViewById(R.id.recycle_view_animal_l_01);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        list_01 = new ArrayList();
        modelo_animal = new Animal_Model();
        Share_References_interface share_references_interface = new Share_References_presenter(getContext());
        preferences = getContext().getSharedPreferences("preferences", Context.MODE_PRIVATE);

        farm_name = share_references_interface.farm_name(preferences);
        id_propietario = share_references_interface.id_propietario(preferences);
        if ( farm_name != null){
            novillas_01();

        }else {
            Toast.makeText(getContext(), "no tienes acceso a la los procedimientos", Toast.LENGTH_LONG).show();

        }


        return  view;
    }
    public void novillas_01() {
        DocumentReference ref_usuarios = db.collection("usuarios").document(id_propietario);
        DocumentReference fincas_ref = ref_usuarios.collection("fincas").document(farm_name);
        CollectionReference coreff = fincas_ref.collection("animales");
        coreff.whereEqualTo("anml_tipo","bovino").whereEqualTo("anml_salida","vacio").whereEqualTo("anml_lote", lote_01).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    if (documentSnapshot.exists()) {
                        modelo_animal = documentSnapshot.toObject(Animal_Model.class);
                        String id = documentSnapshot.getId();
                        modelo_animal.setAnml_tipo("opciones");
                        modelo_animal.setAnml_salida(id);
                        list_01.add(modelo_animal);
                    } else {
                        Toast.makeText(view.getContext(), "no existen datos", Toast.LENGTH_SHORT).show();
                    }

                }
                animal_viewhollder = new Animals_Recycle_Adapter(getContext(), R.layout.item_animal_first, list_01);
                recyclerView.setAdapter(animal_viewhollder);
                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(view.getContext(), layoutManager.getOrientation());
                recyclerView.addItemDecoration(dividerItemDecoration);
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
                        filtrar(editable.toString());
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
    private void filtrar(String toString) {
        ArrayList<Animal_Model> filtrar_lista = new ArrayList<>();
        for (Animal_Model animal  : list_01){
            if (animal.getAnml_nombre().toLowerCase().contains(toString.toLowerCase())){
                filtrar_lista.add(animal);
            }

        }
        animal_viewhollder.filtrar(filtrar_lista);

    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
