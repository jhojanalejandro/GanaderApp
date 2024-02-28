package com.agroapp.proyecto_esmeralda.views.perfil_animal_macho;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


import com.agroapp.proyecto_esmeralda.R;
import com.agroapp.proyecto_esmeralda.views.manejo_animal_view.Manejo_animal_view;
import com.agroapp.proyecto_esmeralda.views.perfil_animal_views.Detalle_Animal;
import com.agroapp.proyecto_esmeralda.views.inicio_view.Inicio_Ganaderapp;
import com.agroapp.proyecto_esmeralda.views.inicio_view.MainActivity;
import com.agroapp.proyecto_esmeralda.views.manejo_animal_view.Mostrar_imagen;
import com.agroapp.proyecto_esmeralda.views.perfil_animal_views.Deseas_Data_Register_View_dialog;
import com.agroapp.proyecto_esmeralda.views.perfil_animal_views.Vaccination_Data_Register_dialog;
import com.agroapp.proyecto_esmeralda.views.manejo_insumos_view.Registro_insumos_animal_dialog;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

public class Perfil_animal_macho extends AppCompatActivity {
    Vaccination_Data_Register_dialog registro_datos_vacunacion;
    Deseas_Data_Register_View_dialog registro_datos_emfd_dialog;
    SharedPreferences preferences;

    String id_animal;
    Registro_insumos_animal_dialog registro_insumos_animal;
    Mostrar_imagen mostrarimagen;
    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_animal_macho);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);

        preferences =  getSharedPreferences("preferences", MODE_PRIVATE);
        id_animal();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home_p_aniaml_m, R.id.nav_gallery_p_aniaml_m, R.id.nav_slideshow_p_aniaml_m,
                R.id.nav_tools_p_aniaml_m)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_perfil_animal_macho, menu);
        return true;
    }

    private String id_animal(){
        id_animal = preferences.getString("id_animal",null);
        if ( id_animal !=null){
            return id_animal;
        }else {
            return  null;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Boolean b;
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {

            Intent login = new Intent(Perfil_animal_macho.this, Manejo_animal_view.class);

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
            //noinspection SimplifiableIfStatement
            case R.id.action_atras_p_animal_m:
                finish();
                System.exit(0);
                break;
            case R.id.action_r_vacunacion_animal_m:
                registro_datos_vacunacion = new Vaccination_Data_Register_dialog();
                registro_datos_vacunacion.show(getSupportFragmentManager(), "registro de vacunacion ");
                break;

            case R.id.action_r_efmd_m:
                registro_datos_emfd_dialog = new Deseas_Data_Register_View_dialog();
                registro_datos_emfd_dialog.show(getSupportFragmentManager(), "registro de Enfermedad ");

                break;

            case R.id.action_r_insumo_animal_m:
                registro_insumos_animal = new Registro_insumos_animal_dialog();
                registro_insumos_animal.show(getSupportFragmentManager(), "registro de insusmo ");

                break;
            case R.id.action_volver_inicio_p_animal_m:
                Intent detalles = new Intent(Perfil_animal_macho.this, Inicio_Ganaderapp.class);
                startActivity(detalles);
                break;
            case R.id.action_cerrar_sesion_p_animal_m:
                Intent det = new Intent(Perfil_animal_macho.this, MainActivity.class);
                preferences.edit().clear();
                startActivity(det);
                break;
            case R.id.action_detalle_animal_m:
                Intent deta = new Intent(Perfil_animal_macho.this, Detalle_Animal.class);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("id_animal", id_animal);
                editor.apply();
                startActivity(deta);
                break;
        }

            return super.onOptionsItemSelected(item);
        }



    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
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
