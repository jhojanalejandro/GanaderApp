package com.agroapp.proyecto_esmeralda.views.perfil_animal_views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.agroapp.proyecto_esmeralda.R;
import com.agroapp.proyecto_esmeralda.views.inicio_view.Inicio_Ganaderapp;
import com.agroapp.proyecto_esmeralda.views.inicio_view.MainActivity;
import com.agroapp.proyecto_esmeralda.views.manejo_insumos_view.Registro_insumos_animal_dialog;
import com.agroapp.proyecto_esmeralda.interfaces.Share_References_interface;
import com.agroapp.proyecto_esmeralda.modelos.Animal_Model;
import com.agroapp.proyecto_esmeralda.controlador.Share_References_presenter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

public class Perfil_Animal_view extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    private ActionBar actionBar;
    private View head;
    private NavigationView navigationView;
    private NavController navController;
    private ImageView perfil_animal;
    private Hot_Data_Register_View_dialog hot_register_dialog;
    private Palpations_Data_Register_view_dialog palpation_register_dialog;
    private Part_Data_Register_View_dialog part_register_dialog;
    private Dry_Data_Register_Data_View_dialog dry_register_dislog;
    Share_References_interface share_references_interface;
    private Vaccination_Data_Register_dialog vaccination_register_dialog;
    private Deseas_Data_Register_View_dialog deseas_register_dialog;
    private Animal_Model modelo_animal;
    private String farm_name;
    private Registro_insumos_animal_dialog insumos_vaca;
    private SharedPreferences preferences;
    String n_finca, n_empleado, id_animal, id;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_animal);
        Toolbar toolbar = findViewById(R.id.toolbar_p_animal);
        setSupportActionBar(toolbar);
        preferences = getSharedPreferences("preferences", Context.MODE_PRIVATE);
        share_references_interface = new Share_References_presenter(Perfil_Animal_view.this);

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);


        actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_impresora_3d);
        actionBar.setDisplayHomeAsUpEnabled(true);
        DrawerLayout drawer = findViewById(R.id.drawer_layout_p_animal);
        navigationView = findViewById(R.id.nav_view_p_animal);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_inicio_p_animal,
                R.id.nav_secado_p_animal, R.id.nav_gallery_p_animal, R.id.nav_slideshow_p_animal,
                R.id.nav_tools_p_animal, R.id.nav_share_p_animal, R.id.nav_send_p_animal, R.id.nav_produccion_pesaje_p_animal, R.id.nav_produccion_leche_p_animal)
                .setDrawerLayout(drawer)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_p_animal);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        head = navigationView.getHeaderView(0);
        perfil_animal = head.findViewById(R.id.imv_nav_header_animal);
        loadNavHeader();
        id_animal();

    }

    private String id_animal() {
        preferences = getSharedPreferences("preferences", MODE_PRIVATE);
        id_animal = preferences.getString("id_animal", null);
        if (id_animal != null) {
            return id_animal;
        } else {
            return null;
        }
    }

    private void loadNavHeader() {
        // name, website

        // loading header background image
        Glide.with(this).load("")
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(perfil_animal);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_perfil__animal, menu);
        return true;
    }


    @SuppressLint({"NonConstantResourceId", "CommitPrefEdits"})
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically
        //
        // handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_atras_p_animal:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
            case R.id.action_cerrar_sesion_p_animal:
                Intent regreso = new Intent(Perfil_Animal_view.this, MainActivity.class);
                preferences.edit().clear();
                startActivity(regreso);
                break;
            case R.id.action_detalle_animal:
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("id_animal", id_animal);
                editor.apply();
                share_references_interface.next_Intent(Perfil_Animal_view.this, Detalle_Animal.class);
                break;
            case R.id.action_r_calor_animal:
                hot_register_dialog = new Hot_Data_Register_View_dialog();
                hot_register_dialog.show(getSupportFragmentManager(), "registro de calor o servicio ");
                break;
            case R.id.action_r_palpacion_animal:
                palpation_register_dialog = new Palpations_Data_Register_view_dialog();
                palpation_register_dialog.show(getSupportFragmentManager(), "registro de palpacion ");
                break;
            case R.id.action_r_parto_animal:
                part_register_dialog = new Part_Data_Register_View_dialog();
                part_register_dialog.show(getSupportFragmentManager(), "registro de parto ");
                break;
            case R.id.action_r_secado_animal:
                dry_register_dislog = new Dry_Data_Register_Data_View_dialog();
                dry_register_dislog.show(getSupportFragmentManager(), "registro de secado ");
                break;
            case R.id.action_r_vacunacion_animal:
                vaccination_register_dialog = new Vaccination_Data_Register_dialog();
                vaccination_register_dialog.show(getSupportFragmentManager(), "registro de vacunacion ");
                break;
            case R.id.action_r_efmd:
                deseas_register_dialog = new Deseas_Data_Register_View_dialog();
                deseas_register_dialog.show(getSupportFragmentManager(), "registro de vacuna o emfermedad ");
                break;
            case R.id.action_r_insumo_animal:
                insumos_vaca = new Registro_insumos_animal_dialog();
                insumos_vaca.show(getSupportFragmentManager(), "registro de insusmo ");
                break;
            case R.id.action_volver_inicio_p_anml:
                Intent detalles = new Intent(Perfil_Animal_view.this, Inicio_Ganaderapp.class);
                startActivity(detalles);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_p_animal);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Boolean b;
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Intent login = new Intent(Perfil_Animal_view.this, MainActivity.class);
            startActivity(login);
            b = true;
        }else {
            b = false;
        }

        return b;
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
