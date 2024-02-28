package com.agroapp.proyecto_esmeralda.interfaces;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.widget.EditText;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.agroapp.proyecto_esmeralda.modelos.Animal_Model;
import com.agroapp.proyecto_esmeralda.modelos.Pesaje_Animal_Model;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public interface Manejo_Animal_Interface {
    void registro_peso_animal( String tipo, Double peso, String fecha, String chapeta, String nombre, ProgressDialog pr);
    void show_cattle( String paddock_name, EditText edt_search, LinearLayoutManager linearLayoutManager);
    void show_cattle_inpaddock(String paddock_name, EditText edt_search, LinearLayoutManager linearLayoutManager);
    void filter(String toString, ArrayList<Animal_Model> list_animal);
    int animal_register( Animal_Model modelo_animal, Double peso, String nombre, ProgressDialog progressDialog);

}
