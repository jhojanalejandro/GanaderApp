package com.agroapp.proyecto_esmeralda.views.menejo_potreros;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;


import com.agroapp.proyecto_esmeralda.R;
import com.agroapp.proyecto_esmeralda.fragment.Movimientos_potrero;
import com.agroapp.proyecto_esmeralda.modelos.Animal_Model;
import com.agroapp.proyecto_esmeralda.views.manejo_insumos_view.Registro_insumos_potrero_dialog;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class Manejo_Potreros extends AppCompatActivity implements  Movimientos_potrero.OnFragmentInteractionListener {

    Registro_insumos_potrero_dialog insumos_potrero_dialog;

    SharedPreferences preferences;
    String paddock_name,farm_name,user_name,tipo_ingreso = "ingreso_lote",tipo_salida = "salida_animal";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<Animal_Model> list_anm_pto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manejo__potreros);
        Toolbar toolbar = findViewById(R.id.toolbar_mnjo_pot);
        setSupportActionBar(toolbar);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager_manejo_potrero);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs_manejo_potreros);
        tabs.setupWithViewPager(viewPager);
        preferences = getSharedPreferences("preferences", Context.MODE_PRIVATE);

        empdo_n();
        validar_finca_n();
        potrero_n();
        if (validar_finca_n() == null || empdo_n() == null){
            Toast.makeText(Manejo_Potreros.this, "no tienes acceso a la los procedimientos DE LOS POTREROS", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }



    private String validar_finca_n(){
        farm_name = preferences.getString("finca",null);
        if ( farm_name!=null){
            return farm_name;
        }else {
            return  null;
        }
    }
    private String empdo_n(){
        user_name = preferences.getString("empleado",null);
        if ( user_name!=null){
            return user_name;
        }else {
            return  null;
        }
    }
    private String potrero_n(){
        paddock_name = preferences.getString("paddock_name",null);
        if ( paddock_name!=null){
            return paddock_name;
        }else {
            return  null;
        }
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