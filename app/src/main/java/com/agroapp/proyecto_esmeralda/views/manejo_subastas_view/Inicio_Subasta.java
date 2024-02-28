package com.agroapp.proyecto_esmeralda.views.manejo_subastas_view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.agroapp.proyecto_esmeralda.interfaces.Manejo_Subastas_Interface;
import com.agroapp.proyecto_esmeralda.controlador.Manejo_Subastas_Presenter;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.agroapp.proyecto_esmeralda.R;
import com.google.firebase.firestore.FirebaseFirestore;

public class Inicio_Subasta extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    SharedPreferences preferences;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManagerSubastas;
    Manejo_Subastas_Interface manejoSubastasInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_inicio_subasta);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_inicio_subasta);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = findViewById(R.id.toolbar_layout_inicio_subasta);
        toolBarLayout.setTitle(getTitle());
        getSupportActionBar().setTitle("BIENVENIDO");

        FloatingActionButton fab = findViewById(R.id.fab_inicio_subasta);
        recyclerView = findViewById(R.id.recycle_lista_suabastas);
        layoutManagerSubastas = new LinearLayoutManager(Inicio_Subasta.this);
        recyclerView.setLayoutManager(layoutManagerSubastas);
        manejoSubastasInterface = new Manejo_Subastas_Presenter(Inicio_Subasta.this);

        manejoSubastasInterface.mostrar_subastas(recyclerView,layoutManagerSubastas );

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent registro_subasta = new Intent(Inicio_Subasta.this, Registro_Subastas.class);
                startActivity(registro_subasta);
            }
        });
    }
}