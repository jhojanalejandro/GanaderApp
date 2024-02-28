package com.agroapp.proyecto_esmeralda.views.perfil_admin_views;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.agroapp.proyecto_esmeralda.R;
import com.agroapp.proyecto_esmeralda.interfaces.Share_References_interface;
import com.agroapp.proyecto_esmeralda.controlador.Share_References_presenter;
import com.agroapp.proyecto_esmeralda.views.inicio_view.Inicio_Ganaderapp;
import com.agroapp.proyecto_esmeralda.views.inicio_view.Login_view;
import com.agroapp.proyecto_esmeralda.views.manejo_produccion.Registro_leche_diaria_animal_dialog;
import com.agroapp.proyecto_esmeralda.views.manejo_usuarios_view.User_Register;
import com.agroapp.proyecto_esmeralda.views.menejo_potreros.Lista_Potreros_View;
import com.agroapp.proyecto_esmeralda.views.manejo_animal_view.Manejo_animales_produ;
import com.agroapp.proyecto_esmeralda.views.manejo_produccion.Registro_otros_gastos_animal_dialog;
import com.agroapp.proyecto_esmeralda.views.manejo_animal_view.Manejo_animal_view;
import com.google.android.material.navigation.NavigationView;

public class Perfil_admin_view extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    Registro_otros_gastos_animal_dialog gastos_Adicionales;
    String id_propietario;
    Share_References_interface share_references_interface;
    SharedPreferences preferences;

    Registro_leche_diaria_animal_dialog registro_leche_diaria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_admin);
        Toolbar toolbar = findViewById(R.id.toolbar_p_admin);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layou_p_admin);
        NavigationView navigationView = findViewById(R.id.nav_view_p_admin);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_datos_finca_p_admin, R.id.nav_estadisticas_p_admin, R.id.nav_promedio_p_admin,
                R.id.nav_personal_p_admin, R.id.nav_palpacion_p_admin, R.id.nav_analizis_p_admin, R.id.nav_produccion_diaria_p_admin,R.id.nav_lista_general_animales_p_admin)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_p_admin);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        preferences =  getSharedPreferences("preferences", MODE_PRIVATE);
        share_references_interface = new Share_References_presenter(Perfil_admin_view.this);
        id_propietario = share_references_interface.id_propietario(preferences);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_perfil_admin, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_cerrar_sesion_p_admin:
                Intent volver = new Intent(Perfil_admin_view.this, Login_view.class);
                preferences.edit().clear().apply();
                startActivity(volver);
                break;
            case R.id.action_registrar_empleado:
                Intent ir_r_empleado = new Intent(Perfil_admin_view.this, User_Register.class);
                SharedPreferences.Editor editor_registro_usuario = preferences.edit();
                editor_registro_usuario.putBoolean("registro_empleado", true);
                editor_registro_usuario.apply();
                startActivity(ir_r_empleado);
                break;
            case R.id.action_ir_porero:
                Intent ir_potrero = new Intent(Perfil_admin_view.this, Lista_Potreros_View.class);
                startActivity(ir_potrero);
                break;
            case R.id.action_ganancias_adicionales_p_amin:
                gastos_Adicionales = new Registro_otros_gastos_animal_dialog();
                SharedPreferences.Editor editor_ganancia = preferences.edit();
                editor_ganancia.putString("tipo_gasto", "ganancia");
                editor_ganancia.apply();
                gastos_Adicionales.show(getSupportFragmentManager(), "Registrar empleados");
                break;
            case R.id.action_r_gastos_adicionales:
                gastos_Adicionales = new Registro_otros_gastos_animal_dialog();
                SharedPreferences.Editor editor_gasto = preferences.edit();
                editor_gasto.putString("tipo_gasto", "gasto");
                editor_gasto.apply();
                gastos_Adicionales.show(getSupportFragmentManager(), "Registrar empleados");
                break;
            case R.id.action_lista_animales_produccion_p_admin:
                Intent r_tarea = new Intent(Perfil_admin_view.this, Manejo_animales_produ.class);
                startActivity(r_tarea);
                break;
            case R.id.action_lista_animales_general:
                Intent lista_bovinos = new Intent(Perfil_admin_view.this, Manejo_animal_view.class);
                startActivity(lista_bovinos);
                break;

            case R.id.action_codigo_copiar_p_admin:
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("text",  id_propietario);
                Toast.makeText(Perfil_admin_view.this, "CODIGO COPIADO", Toast.LENGTH_SHORT).show();
                clipboard.setPrimaryClip(clip);
                break;
            case R.id.action_produccion_diaria_p_admin:
                registro_leche_diaria = new Registro_leche_diaria_animal_dialog();
                registro_leche_diaria.show(getSupportFragmentManager(), "Registrar leche");
                break;
            case R.id.action_atras_p_admin:
                navigateUpTo(new Intent(this, Inicio_Ganaderapp.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Boolean b;
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {

            Intent login = new Intent(Perfil_admin_view.this, Inicio_Ganaderapp.class);
            preferences.edit().remove("paddock_name").apply();
            startActivity(login);
            b = true;
        }else {
            b = false;
        }

        return b;
    }
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_p_admin);
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
