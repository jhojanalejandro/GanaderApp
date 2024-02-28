package com.agroapp.proyecto_esmeralda.interfaces;

import android.content.Context;
import android.widget.EditText;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

public interface Manejo_Potreros_Interface {
    void  paddock_register(Context context, String id_propietario,String farm_name, Object object_model, String potrero);
    void show_paddock(Context context, FirebaseFirestore db, RecyclerView recyclerView, String id_propietario, String farm_name, EditText edt_search, LinearLayoutManager layoutManager);

}
