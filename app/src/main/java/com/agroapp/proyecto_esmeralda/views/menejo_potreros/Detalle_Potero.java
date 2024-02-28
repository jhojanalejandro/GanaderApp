package com.agroapp.proyecto_esmeralda.views.menejo_potreros;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;

import com.agroapp.proyecto_esmeralda.interfaces.Manejo_Animal_Interface;
import com.agroapp.proyecto_esmeralda.controlador.Manejo_Animal_Presenter;
import com.agroapp.proyecto_esmeralda.controlador.Share_References_presenter;
import com.agroapp.proyecto_esmeralda.views.inicio_view.Inicio_Ganaderapp;
import com.agroapp.proyecto_esmeralda.views.inicio_view.Login_view;
import com.agroapp.proyecto_esmeralda.views.manejo_insumos_view.Registro_insumos_potrero_dialog;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.agroapp.proyecto_esmeralda.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

public class Detalle_Potero extends AppCompatActivity {

    String  paddock_name, farm_name, id_propietario, user_name;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    RecyclerView recyclerView;
    EditText edt_buscar_aniaml;
    TextView tv_fecha;
    SharedPreferences preferences;

    LinearLayoutManager layoutManager;
    FloatingActionButton fab_d_potrero_ingreso,fab_d_potrero_salida;
    Share_References_presenter share_preferences_presenter;
    Manejo_Animal_Interface manejo_animal_interface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_potero);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_detalle_potrero);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout_detalle_potrero);


        recyclerView = findViewById(R.id.resi_view_animal_potrero);
        edt_buscar_aniaml = findViewById(R.id.edt_buscar_animal_potrero);
        tv_fecha = findViewById(R.id.tv_fecha_datalle_potrero);
        share_preferences_presenter = new Share_References_presenter(Detalle_Potero.this);
        preferences = getSharedPreferences("preferences", MODE_PRIVATE);

        fab_d_potrero_ingreso =  findViewById(R.id.fab_detalle_potrero_ingreso);
        fab_d_potrero_salida =  findViewById(R.id.fab_detalle_potrero_salida);

        user_name = share_preferences_presenter.user_name(preferences);
        farm_name = share_preferences_presenter.farm_name(preferences);
        id_propietario = share_preferences_presenter.id_propietario(preferences);
        paddock_name = share_preferences_presenter.paddock_name(preferences);

        DocumentReference ref_usuarios = db.collection("usuarios").document(id_propietario);
        DocumentReference fincas_ref = ref_usuarios.collection("fincas").document(farm_name);
        CollectionReference coreff = fincas_ref.collection("animales");
        manejo_animal_interface = new Manejo_Animal_Presenter(recyclerView,Detalle_Potero.this,coreff );
        toolBarLayout.setTitle(paddock_name);
        if (farm_name != null & paddock_name != null) {
            layoutManager = new LinearLayoutManager(Detalle_Potero.this);
            recyclerView.setLayoutManager(layoutManager);
            if (paddock_name == null) {
                paddock_name = "bovino";
            }
            manejo_animal_interface.show_cattle_inpaddock( paddock_name, edt_buscar_aniaml, layoutManager);
        } else {
            Toast.makeText(Detalle_Potero.this, "no tienes acceso a la los procedimientos fragment", Toast.LENGTH_LONG).show();
        }
        
        if (recyclerView == null){
            findViewById(R.id.fab_detalle_potrero_ingreso).setVisibility(View.VISIBLE);
            findViewById(R.id.fab_detalle_potrero_salida).setVisibility(View.GONE);
        }else {
            findViewById(R.id.fab_detalle_potrero_ingreso).setVisibility(View.VISIBLE);
            findViewById(R.id.fab_detalle_potrero_salida).setVisibility(View.VISIBLE);
        }

        fab_d_potrero_salida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment_Salida_potrero movimiento_pto_salida = new Fragment_Salida_potrero();
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("paddock_name", paddock_name);
                editor.putString("tipo", "salida");
                editor.apply();
                movimiento_pto_salida.show(getSupportFragmentManager(), "registro de movimientos potrero ");
            }
        });

        fab_d_potrero_ingreso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Movimiento_pto_dialog movimiento_pto = new Movimiento_pto_dialog();
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("paddock_name", paddock_name);
                editor.putString("id_tipo_ingreso", "colectivo");
                editor.apply();
                movimiento_pto.show(getSupportFragmentManager(), "registro de movimientos potrero ");
            }
        });

        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedDate = df.format(c.getTime());
            tv_fecha.setText(formattedDate);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detalle_potrero, menu);
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Boolean b;
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Intent login = new Intent(Detalle_Potero.this, Inicio_Ganaderapp.class);
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

        switch (id){
            case R.id.action_r_insumo_potreros:
                Registro_insumos_potrero_dialog registro_insumo_potrero_dialog = new Registro_insumos_potrero_dialog();
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("tipo_concentrado", "abono");
                editor.apply();
                registro_insumo_potrero_dialog.show(getSupportFragmentManager(), "registro insumo portero ");
                break;
            case R.id.action_volver_inicio_detalle_pto:
                navigateUpTo(new Intent(this, Inicio_Ganaderapp.class));
                preferences.edit().remove("paddock_name").apply();
                break;
            case R.id.action_cerrar_cession_detalle_pto:
                Intent intent = new Intent(Detalle_Potero.this, Login_view.class);
                startActivity(intent);
                break;
        }
        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }
}