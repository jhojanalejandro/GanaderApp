package com.agroapp.proyecto_esmeralda.views.menejo_potreros;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.agroapp.proyecto_esmeralda.R;
import com.agroapp.proyecto_esmeralda.fragment.Movimientos_potrero;
import com.agroapp.proyecto_esmeralda.modelos.Potreros_model;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {
    Potreros_model items;
    private String farm_name,id_propietario,user_name, padock_name;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    SharedPreferences preferences;
    ArrayList<Potreros_model> list_p;
    private ListView lv_potrero;
    Potreros_model potreros_model;

    private static final String ARG_SECTION_NUMBER = "section_number";

    public static Fragment newInstance(int index) {
        Fragment fragment = new Fragment();
        switch (index) {
            case 1:
                fragment = new PlaceholderFragment();
                break;
            case 2:
                fragment = new Movimientos_potrero();
                break;
        }
            return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manejo_potreros, container, false);

        lv_potrero = view.findViewById(R.id.lv_potreros);
        list_p = new ArrayList<>();
        potreros_model = new Potreros_model();
        preferences = getContext().getSharedPreferences("preferences", Context.MODE_PRIVATE);

        nombre_potrero();
        empdo_n();
        validar_finca_n();

            if (farm_name != null && user_name !=   null &&  padock_name != null ){
            }else if (validar_finca_n() != null && empdo_n() != null && nombre_potrero() == null){
                Toast.makeText(getContext(), "no tienes potreros", Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(getContext(), "no tienes acceso a la los procedimientos de la aplicacion", Toast.LENGTH_LONG).show();
            }

        return view;
    }
    private String validar_finca_n(){
        farm_name = preferences.getString("finca",null);
        if ( farm_name!=null){
            return farm_name;
        }else {
            return  null;
        }
    }
    private String id_propietario(){
        id_propietario = preferences.getString("id_propietario",null);
        if ( id_propietario!=null){
            return id_propietario;
        }else {
            return  null;
        }
    }

    private String empdo_n(){
        user_name = preferences.getString("empleado",null);
        if ( user_name!=null){
            return user_name;
        }else {
            return  null;
        }
    }
    private String nombre_potrero(){
        padock_name = preferences.getString("padock_name",null);
        if ( padock_name!=null){
            return padock_name;
        }else {
            return  null;
        }
    }

}