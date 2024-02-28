package com.agroapp.proyecto_esmeralda.interfaces;

import android.content.Context;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.time.LocalDate;

public interface Manejo_Insumos_Interface {
    void registrar_gasto_insumo( String droga, int cant_droga);
    String mostrar_spinner_insumos(String tipo, Spinner spinner_insumos);
    String mostrar_spinner_toros(TextView tv_toro, Spinner spinner_toro);
    void mostrar_concentrados(String tipo,  RecyclerView recyclerVie, LinearLayoutManager layoutManager);

    void mostrar_insumos(String tipo,  RecyclerView recyclerVie, LinearLayoutManager layoutManager);
    void registrar_insumo(String insumo, Double precios, int cant_insumos , Double unitario, String fecha, String tipo, String obs, int mes , int ano);
}
