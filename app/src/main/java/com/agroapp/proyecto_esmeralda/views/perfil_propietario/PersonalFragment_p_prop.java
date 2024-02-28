package com.agroapp.proyecto_esmeralda.views.perfil_propietario;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import com.agroapp.proyecto_esmeralda.R;
import com.agroapp.proyecto_esmeralda.modelos.User_Model;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class PersonalFragment_p_prop extends Fragment {

    private String n_finca;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    SharedPreferences preferences;
    ArrayList<User_Model> list_epdo;
    private ListView lv_empleaado;
    View view;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

         view = inflater.inflate(R.layout.fragment_share_p_pro, container, false);


        lv_empleaado = view.findViewById(R.id.lv_personal);
        list_epdo = new ArrayList<>();

        validar_finca_n();


        return view;
    }
    private String validar_finca_n(){
        preferences =  getContext().getSharedPreferences("preferences", MODE_PRIVATE);
        n_finca = preferences.getString("finca",null);
        if ( n_finca!=null){
            return n_finca;
        }else {
            return  null;
        }
    }
}