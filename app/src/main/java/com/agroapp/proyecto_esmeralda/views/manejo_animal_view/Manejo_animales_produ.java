package com.agroapp.proyecto_esmeralda.views.manejo_animal_view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;


import com.agroapp.proyecto_esmeralda.R;
import com.agroapp.proyecto_esmeralda.interfaces.Share_References_interface;
import com.agroapp.proyecto_esmeralda.controlador.Share_References_presenter;
import com.agroapp.proyecto_esmeralda.views.inicio_view.Inicio_Ganaderapp;
import com.agroapp.proyecto_esmeralda.views.manejo_insumos_view.Manejo_Insumos_View;
import com.agroapp.proyecto_esmeralda.views.inicio_view.MainActivity;
import com.agroapp.proyecto_esmeralda.views.perfil_admin_views.Perfil_admin_view;
import com.google.android.material.tabs.TabLayout;

public class Manejo_animales_produ extends AppCompatActivity implements Vacas_01.OnFragmentInteractionListener, Vacas_02.OnFragmentInteractionListener, Vacas_03.OnFragmentInteractionListener, Vacas_04.OnFragmentInteractionListener {

    SharedPreferences preferences;
    int nivel_acceso = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manejo_animales_prod);
        Toolbar toolbar = findViewById(R.id.toolbar_mnjo_anml_prod);
        setSupportActionBar(toolbar);
        Animal_Produccion_View_SectionsPagerAdapter sectionsPagerAdapter = new Animal_Produccion_View_SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs_manejo_animales_levante);
        tabs.setupWithViewPager(viewPager);
        preferences =  getSharedPreferences("preferences", MODE_PRIVATE);
        Share_References_interface share_references_interface = new Share_References_presenter(Manejo_animales_produ.this);
        nivel_acceso = share_references_interface.nivel_acceso(preferences);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_animales_prod, menu);

        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long

        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        switch (id){
            case R.id.action_cerrar_sesion_anml_prod:
                Intent salir = new Intent(Manejo_animales_produ.this, MainActivity.class);
                preferences.edit().clear().apply();
                startActivity(salir);
                break;
            case R.id.action_atras_anml_prod:
                navigateUpTo(new Intent(Manejo_animales_produ.this, Inicio_Ganaderapp.class));
                break;
            case R.id.action_ir_insumos_anml_prod:
                Intent manejo = new Intent(Manejo_animales_produ.this, Manejo_Insumos_View.class);
                startActivity(manejo);
                break;
                case R.id.action_ir_menu_admin_anml_prod:
                    if (nivel_acceso == 3){
                        Toast.makeText(Manejo_animales_produ.this, "OPCION NO PERMITIDA", Toast.LENGTH_SHORT).show();
                    }else {
                        Intent p_admin = new Intent(Manejo_animales_produ.this, Perfil_admin_view.class);
                        startActivity(p_admin);
                    }
                break;
            case R.id.action_anml_prod_volver_inicio:
                Intent volver = new Intent(Manejo_animales_produ.this, Inicio_Ganaderapp.class);
                startActivity(volver);
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