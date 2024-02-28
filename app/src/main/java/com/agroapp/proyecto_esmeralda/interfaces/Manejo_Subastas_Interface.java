package com.agroapp.proyecto_esmeralda.interfaces;

import android.content.Context;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.agroapp.proyecto_esmeralda.modelos.Subasta_Model;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

public interface Manejo_Subastas_Interface {
    void mostrar_subastas(  RecyclerView recyclerView, LinearLayoutManager layoutManager);
    void mostrar_subastas_propietario( RecyclerView recyclerView, LinearLayoutManager layoutManager, String cedula);
    void multimedia_subasta(StorageReference storageReference, ImageView img_01, ImageView img_02, ImageView img_03, ImageView img_04,
                            VideoView vd_anaml, String id_animal, String id_propietario,String farm_name);
    void pujar_subasta( Long puja, String id);
    String subastar_animales( Subasta_Model class_name);
    String actualizar_animales( Subasta_Model class_name);
    void agregar_animales_asubastar( String id_propietario,String ids_animal, String finca, String id_subasta);
}
