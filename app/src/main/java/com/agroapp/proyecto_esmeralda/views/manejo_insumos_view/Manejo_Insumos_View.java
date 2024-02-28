package com.agroapp.proyecto_esmeralda.views.manejo_insumos_view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.agroapp.proyecto_esmeralda.R;
import com.agroapp.proyecto_esmeralda.interfaces.Share_References_interface;
import com.agroapp.proyecto_esmeralda.controlador.Share_References_presenter;
import com.agroapp.proyecto_esmeralda.views.inicio_view.Inicio_Ganaderapp;
import com.agroapp.proyecto_esmeralda.views.inicio_view.Login_view;
import com.agroapp.proyecto_esmeralda.views.perfil_admin_views.Perfil_admin_view;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

public class Manejo_Insumos_View extends AppCompatActivity implements Herramientas.OnFragmentInteractionListener, Concentrados_Fragment_View.OnFragmentInteractionListener {
    String farm_name;
    SharedPreferences preferences;
    Registro_droga_finca_dialog registro_insuno_finca;
    Registro_herramienta_dialog registro_herramienta;
    int nivel_acceso;
    Registro_insumosconcentrado_finca_dialog registro_insumos_abono_concentrado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_insumos);
        Toolbar toolbar = findViewById(R.id.toolbar_control_insumos);
        setSupportActionBar(toolbar);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        final ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs_manejo_insumos);
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = findViewById(R.id.fab_control_insumos);
        preferences = getSharedPreferences("preferences", Context.MODE_PRIVATE);
        Share_References_interface share_references_interface = new Share_References_presenter(Manejo_Insumos_View.this);
        nivel_acceso = share_references_interface.nivel_acceso(preferences);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = viewPager.getCurrentItem();
                if (position == 0) {
                    registro_insuno_finca  = new Registro_droga_finca_dialog();
                    registro_insuno_finca.show(getSupportFragmentManager(),"Droga");
                }
                else if (position == 1) {
                    registro_herramienta  = new Registro_herramienta_dialog();
                    registro_herramienta.show(getSupportFragmentManager(),"Herramientas");
                }else if (position == 2) {
                    registro_insumos_abono_concentrado  = new Registro_insumosconcentrado_finca_dialog();
                    registro_insumos_abono_concentrado.show(getSupportFragmentManager(),"Concentrados");

                }

            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Boolean b;
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {

            Intent login = new Intent(Manejo_Insumos_View.this, Inicio_Ganaderapp.class);
            preferences.edit().remove("paddock_name").apply();
            startActivity(login);
            b = true;
        }else {
            b = false;
        }

        return b;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_control_insumos, menu);
        return true;
    }


    @SuppressLint({"NonConstantResourceId", "CommitPrefEdits"})
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        switch (id){
            case R.id.action_back_start_insumos:
                navigateUpTo(new Intent(this, Inicio_Ganaderapp.class));
                break;
            case R.id.action_menu_admin:
                if (nivel_acceso == 3){
                    Toast.makeText(Manejo_Insumos_View.this, "OPCION NO PERMITIDA", Toast.LENGTH_SHORT).show();
                }else {
                    Intent manejos = new Intent(Manejo_Insumos_View.this, Perfil_admin_view.class);
                    startActivity(manejos);
                }

                break;

            case R.id.action_cerrar_cession_manejo_ins:
                Intent out_c = new Intent(Manejo_Insumos_View.this, Login_view.class);
                preferences.edit().clear();
                startActivity(out_c);
                break;
            case R.id.action_volver_inicio_manejo_insumos:
                Intent inicio = new Intent(Manejo_Insumos_View.this, Inicio_Ganaderapp.class);
                startActivity(inicio);
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