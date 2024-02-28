package com.agroapp.proyecto_esmeralda.views.inicio_view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.agroapp.proyecto_esmeralda.R;
import com.agroapp.proyecto_esmeralda.interfaces.Control_agricola_Interface;
import com.agroapp.proyecto_esmeralda.interfaces.Share_References_interface;
import com.agroapp.proyecto_esmeralda.controlador.Control_agricola_Presenter;
import com.agroapp.proyecto_esmeralda.controlador.Share_References_presenter;
import com.agroapp.proyecto_esmeralda.views.manejo_animal_view.Manejo_animal_view;
import com.agroapp.proyecto_esmeralda.views.manejo_animal_view.Manejo_animales_produ;
import com.agroapp.proyecto_esmeralda.views.manejo_insumos_view.Manejo_Insumos_View;
import com.agroapp.proyecto_esmeralda.views.manejo_subastas_view.Inicio_Subasta;
import com.agroapp.proyecto_esmeralda.views.menejo_potreros.Lista_Potreros_View;
import com.agroapp.proyecto_esmeralda.views.perfil_admin_views.Perfil_admin_view;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Inicio_Ganaderapp extends AppCompatActivity implements View.OnClickListener {

    Control_agricola_Interface control_agricola;
    Share_References_interface share_references;
    ArrayList<String> fincas = new ArrayList<>();
    Boolean acceso_finca = true;
    int dia, mes, ano, nivel_acceso;
    ImageButton btn_manejo_ganado, btn_manejo_potreros, btn_manejo_insumos;
    String farm_name, user_type, user_name,id_propietario;
    Spinner spinner_n_finca;
    TextView tv_finca;
    SharedPreferences preferences;
    LinearLayoutManager layoutManager_adelantadas, layoutManager_secas;

    RecyclerView recyclerView_adelantadas, recyclerView_secas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio__ganderapp);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_inicio_ganaderapp);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = findViewById(R.id.toolbar_layout_inicioapp);
        toolBarLayout.setTitle(getTitle());
        getSupportActionBar().setTitle("BIENVENIDO");

        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view_alto_inicio_app);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        iniciar_variables_ganaderapp();

        getSupportActionBar().setSubtitle("fecha");

        share_references = new Share_References_presenter(Inicio_Ganaderapp.this);
        user_type = share_references.user_type(preferences);
        user_name = share_references.user_name(preferences);
        id_propietario = share_references.id_propietario(preferences);
        nivel_acceso = share_references.nivel_acceso(preferences);

        if (id_propietario != null) {
            share_references.spinner_farm(spinner_n_finca,  Inicio_Ganaderapp.this, tv_finca, id_propietario);
        }
        farm_name = tv_finca.getText().toString();
        int[] date = share_references.date_picker();
        dia = date[0];
        mes = date[1];
        ano = date[2];

        share_references.fincas(preferences, fincas);
        if (tv_finca.getText().toString().length() == 0 ||  id_propietario == null) {
            Toast.makeText(Inicio_Ganaderapp.this, "No Hay Fincas Registradas", Toast.LENGTH_SHORT).show();
            findViewById(R.id.fl_inicio_ganaderapp).setVisibility(View.GONE);
        } else {
            CollectionReference[] animales_array = share_references.referencedb_c(id_propietario,farm_name,"vacio");
            CollectionReference animales_ref = animales_array[0];
            control_agricola = new Control_agricola_Presenter(Inicio_Ganaderapp.this, animales_ref);

            findViewById(R.id.fl_inicio_ganaderapp).setVisibility(View.VISIBLE);
            control_agricola.show_dry( mes, ano, recyclerView_secas, layoutManager_secas);
            control_agricola.show_part( mes, ano, recyclerView_adelantadas, layoutManager_adelantadas);

            if (mes <= 7) {
                control_agricola.consultar_secado_animales_segundo_periodo( mes, ano);
            } else {
                mes -= 5;
                control_agricola.consultar_secado_animales_primer_periodo( mes, ano);
            }
            if (mes <= 5) {
                ano -= 1;
                control_agricola.consultar_parto_animales_segundo_periodo( mes, ano);
            } else {
                control_agricola.consultar_parto_animales_primer_periodo( mes, ano);
            }
        }

        FloatingActionButton fab_rigistro_finca = findViewById(R.id.fab_inicio_ganader_app);

        fab_rigistro_finca.setOnClickListener(view -> {
            Intent registro_finca = new Intent(Inicio_Ganaderapp.this, Farm_Register.class);
            SharedPreferences.Editor editors = preferences.edit();
            editors.putString("id_propietario", id_propietario);
            editors.putString("empleado", user_name);
            editors.apply();
            startActivity(registro_finca);

        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Boolean b;
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Intent login = new Intent(Inicio_Ganaderapp.this, Login_view.class);
            startActivity(login);
            b = true;
        } else {
            b = false;
        }

        return b;
    }
    private void iniciar_variables_ganaderapp() {
        btn_manejo_ganado = findViewById(R.id.btn_manejo_animal_inicioapp);
        btn_manejo_potreros = findViewById(R.id.btn_manejo_potreros_inicioapp);
        btn_manejo_insumos = findViewById(R.id.btn_manejo_insumos_inicio_app);
        spinner_n_finca = findViewById(R.id.spinner_eleccion_finca_inicioapp);
        tv_finca = findViewById(R.id.tv_eleccion_spiner_finca_inicioapp);

        btn_manejo_ganado.setOnClickListener(this);
        btn_manejo_potreros.setOnClickListener(Inicio_Ganaderapp.this);
        btn_manejo_insumos.setOnClickListener(Inicio_Ganaderapp.this);
        preferences = getSharedPreferences("preferences", MODE_PRIVATE);
        recyclerView_adelantadas = findViewById(R.id.recycle_view_list_adelantada_inicioapp);
        layoutManager_adelantadas = new LinearLayoutManager(Inicio_Ganaderapp.this);
        recyclerView_adelantadas.setLayoutManager(layoutManager_adelantadas);

        recyclerView_secas = findViewById(R.id.recycle_view_list_secas_inicioapp);
        layoutManager_secas = new LinearLayoutManager(Inicio_Ganaderapp.this);
        recyclerView_secas.setLayoutManager(layoutManager_secas);

    }

    public BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @SuppressLint("NonConstantResourceId")
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_inicio:
                    setTitle("Inicio");
                    findViewById(R.id.ln_ctl_ag_inicio).setVisibility(View.VISIBLE);
                    findViewById(R.id.ln_ctl_ag_adelantadas).setVisibility(View.GONE);
                    findViewById(R.id.ln_ctl_ag_secas).setVisibility(View.GONE);
                    findViewById(R.id.nav_view_alto_inicio_app).setVisibility(View.VISIBLE);
                    break;
                case R.id.navigation_cuenta:
                    setTitle("adelantadas");
                    findViewById(R.id.ln_ctl_ag_inicio).setVisibility(View.GONE);
                    findViewById(R.id.ln_ctl_ag_adelantadas).setVisibility(View.VISIBLE);
                    findViewById(R.id.ln_ctl_ag_secas).setVisibility(View.GONE);
                    findViewById(R.id.nav_view_alto_inicio_app).setVisibility(View.VISIBLE);

                    break;
                case R.id.navigation_perfil:
                    setTitle("secas");
                    findViewById(R.id.nav_view_alto_inicio_app).setVisibility(View.VISIBLE);
                    findViewById(R.id.ln_ctl_ag_inicio).setVisibility(View.GONE);
                    findViewById(R.id.ln_ctl_ag_adelantadas).setVisibility(View.GONE);
                    findViewById(R.id.ln_ctl_ag_secas).setVisibility(View.VISIBLE);
                    break;
            }

            return true;
        }
    };


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {

        farm_name = tv_finca.getText().toString().trim();
        if (farm_name.equals("FINCAS")) {
            Toast.makeText(Inicio_Ganaderapp.this, "ELIGE UNA FINCA O REGISTRA UNA ", Toast.LENGTH_LONG).show();
        } else {
            switch (v.getId()) {
                case R.id.btn_manejo_insumos_inicio_app:
                    Intent manejo_insumos = new Intent(Inicio_Ganaderapp.this, Manejo_Insumos_View.class);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("finca", farm_name);
                    editor.putString("id_propietario", id_propietario);
                    editor.putString("empleado", user_name);
                    editor.apply();
                    startActivity(manejo_insumos);
                    break;
                case R.id.fab_inicio_ganader_registrar_subastas:
                    if (farm_name == null || farm_name.equals("FINCAS")) {
                        Toast.makeText(Inicio_Ganaderapp.this, "ELIGE UNA FINCA O REGISTRA UNA ", Toast.LENGTH_LONG).show();
                    } else {
                        Intent subastas = new Intent(Inicio_Ganaderapp.this, Inicio_Subasta.class);
                        SharedPreferences.Editor editors = preferences.edit();
                        editors.putString("finca", farm_name);
                        editors.putString("id_propietario", id_propietario);
                        editors.putString("empleado", user_name);
                        editors.apply();
                        startActivity(subastas);
                    }
                    break;

                case R.id.btn_manejo_animal_inicioapp:
                    Intent manejo_animal = new Intent(Inicio_Ganaderapp.this, Manejo_animal_view.class);
                    SharedPreferences.Editor editors = preferences.edit();
                    editors.putString("finca", farm_name);
                    editors.putString("id_propietario", id_propietario);
                    editors.putString("empleado", user_name);
                    editors.apply();
                    startActivity(manejo_animal);
                    break;
                case R.id.btn_manejo_potreros_inicioapp:
                    Intent manejo_potreros = new Intent(Inicio_Ganaderapp.this, Lista_Potreros_View.class);
                    SharedPreferences.Editor edit = preferences.edit();
                    edit.putString("finca", farm_name);
                    edit.putString("id_propietario", id_propietario);
                    edit.putString("empleado", user_name);
                    edit.apply();
                    startActivity(manejo_potreros);
                    break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_control_agricola, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        farm_name = tv_finca.getText().toString().trim();
        int id = item.getItemId();

        switch (id) {
            case R.id.action_cerrarcesion_control_agricola:
                Intent login = new Intent(Inicio_Ganaderapp.this, Login_view.class);
                preferences.edit().clear().apply();
                startActivity(login);
                break;
            case R.id.action_menu_adm_agro:
                if (nivel_acceso == 3) {
                    Toast.makeText(Inicio_Ganaderapp.this, "ESTA Opcion No Esta Abilitada Para Ti", Toast.LENGTH_SHORT).show();
                } else {
                    if (farm_name == null || farm_name.equals("FINCAS")) {
                        Toast.makeText(Inicio_Ganaderapp.this, "Elige Una Finca", Toast.LENGTH_SHORT).show();
                    } else {
                        if (fincas.get(0).equals(farm_name)) {
                            acceso_finca = true;
                        } else if ( fincas.get(1).equals(farm_name)) {
                            acceso_finca = true;
                        } else if (fincas.get(2).equals(farm_name)) {
                            acceso_finca = true;
                        } else if (fincas.get(3).equals(farm_name)) {
                            acceso_finca = true;
                        }
                        if (acceso_finca) {
                            Intent perfil_admin = new Intent(Inicio_Ganaderapp.this, Perfil_admin_view.class);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("finca", farm_name);
                            editor.putString("id_propietario", id_propietario);
                            editor.putString("empleado", user_name);
                            editor.apply();
                            startActivity(perfil_admin);
                        } else {
                            Toast.makeText(Inicio_Ganaderapp.this, "No Tienes Permotido Ingresar A Esta Opcion", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
                break;
            case R.id.action_lista_animales_produccion_c_agricola:
                if (farm_name == null || farm_name.equals("FINCAS")) {
                    Toast.makeText(Inicio_Ganaderapp.this, "Elige Una Finca", Toast.LENGTH_SHORT).show();
                } else {
                    Intent manejo_lista_animal = new Intent(Inicio_Ganaderapp.this, Manejo_animales_produ.class);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("finca", farm_name);
                    editor.putString("id_propietario", id_propietario);
                    editor.putString("empleado", user_name);
                    editor.apply();
                    startActivity(manejo_lista_animal);
                }
                break;

        }

        return super.onOptionsItemSelected(item);
    }

}