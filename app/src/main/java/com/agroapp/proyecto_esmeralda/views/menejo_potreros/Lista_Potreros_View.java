package com.agroapp.proyecto_esmeralda.views.menejo_potreros;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.agroapp.proyecto_esmeralda.R;
import com.agroapp.proyecto_esmeralda.interfaces.Manejo_Potreros_Interface;
import com.agroapp.proyecto_esmeralda.controlador.Manejo_Potreros_Presenter;
import com.agroapp.proyecto_esmeralda.controlador.Share_References_presenter;
import com.agroapp.proyecto_esmeralda.views.inicio_view.Inicio_Ganaderapp;
import com.agroapp.proyecto_esmeralda.views.inicio_view.Login_view;
import com.agroapp.proyecto_esmeralda.views.manejo_insumos_view.Registro_insumos_potrero_dialog;
import com.agroapp.proyecto_esmeralda.views.perfil_admin_views.Perfil_admin_view;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

public class Lista_Potreros_View extends AppCompatActivity {
    EditText buscar_nombre;

    String nombre_animal;

    Registro_potrero_dialog registro_potrero_dialog;
    private String farm_name, id_propietario;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    RecyclerView recyclerView;

    SharedPreferences preferences;

    LinearLayoutManager layoutManager;
    Share_References_presenter share_preferences_presenter;
    Manejo_Potreros_Interface manejo_potreros_interface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_potreros);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        buscar_nombre = findViewById(R.id.edt_buscar_potreros);
        recyclerView = findViewById(R.id.recycle_view_poptrero);
        share_preferences_presenter = new Share_References_presenter(Lista_Potreros_View.this);
        preferences = getSharedPreferences("preferences", MODE_PRIVATE);
        manejo_potreros_interface = new Manejo_Potreros_Presenter();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);

        farm_name = share_preferences_presenter.farm_name(preferences);
        id_propietario = share_preferences_presenter.id_propietario(preferences);
        if (farm_name != null & id_propietario != null) {
            layoutManager = new LinearLayoutManager(Lista_Potreros_View.this);
            recyclerView.setLayoutManager(layoutManager);
            if (farm_name != null) {
                manejo_potreros_interface.show_paddock(Lista_Potreros_View.this, db, recyclerView, id_propietario, farm_name, buscar_nombre, layoutManager);
            } else {
                Toast.makeText(Lista_Potreros_View.this, "no tienes acceso a la los procedimientos ", Toast.LENGTH_LONG).show();
            }

            FloatingActionButton fab = findViewById(R.id.fab_lista_pto);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    registro_potrero_dialog = new Registro_potrero_dialog(Lista_Potreros_View.this);
                    registro_potrero_dialog.show(getSupportFragmentManager(), "registro de potreros ");
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_manejo_potreros, menu);
        return true;
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Boolean b;
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {

            Intent login = new Intent(Lista_Potreros_View.this, Inicio_Ganaderapp.class);
            preferences.edit().remove("paddock_name").apply();
            startActivity(login);
            b = true;
        }else {
            b = false;
        }

        return b;
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_menu_admin_manejo_potreros:
                Intent perfil_admin = new Intent(Lista_Potreros_View.this, Perfil_admin_view.class);
                startActivity(perfil_admin);
                break;
            case R.id.action_cerrar_cession_manejo_pto:
                Intent login = new Intent(Lista_Potreros_View.this, Login_view.class);
                preferences.edit().clear().apply();
                startActivity(login);
                break;
            case R.id.action_back_lista_potreros:
                navigateUpTo(new Intent(this, Inicio_Ganaderapp.class));
                break;
            case R.id.action_r_insumo_potreros:
                Registro_insumos_potrero_dialog insumos_potrero_dialog = new Registro_insumos_potrero_dialog();
                insumos_potrero_dialog.show(getSupportFragmentManager(), "registro de potreros ");
                break;
            case R.id.action_volver_inicio_m_pto:
                Intent inicio_ = new Intent(Lista_Potreros_View.this, Inicio_Ganaderapp.class);
                preferences.edit().remove("paddock_name").apply();
                startActivity(inicio_);
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
