package com.agroapp.proyecto_esmeralda.views.perfil_propietario;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.agroapp.proyecto_esmeralda.views.inicio_view.Inicio_Ganaderapp;
import com.agroapp.proyecto_esmeralda.views.inicio_view.MainActivity;
import com.agroapp.proyecto_esmeralda.views.manejo_animal_view.Manejo_animal_view;
import com.agroapp.proyecto_esmeralda.views.perfil_admin_views.Perfil_admin_view;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

public class Perfil_propietario extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    private SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_propietario);
        Toolbar toolbar = findViewById(R.id.toolbar_p_prop);
        setSupportActionBar(toolbar);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);


        DrawerLayout drawer = findViewById(R.id.drawer_layout_p_prop);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home_p_prop, R.id.nav_gallery_p_prop, R.id.nav_slideshow_p_prop,
                R.id.nav_tools_p_prop, R.id.nav_share_p_prop, R.id.nav_send_p_prop)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_p_prop);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_perfil_propietario, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id){
            case R.id.action_atras_perfil_pro:
                finish();
                System.exit(0);
                break;
            case R.id.action_p_prop_ir_ps_admin:
                Intent m_admin = new Intent(Perfil_propietario.this, Perfil_admin_view.class);
                startActivity(m_admin);
                break;
            case R.id.action_p_pro_lista_animal:
                Intent manejo = new Intent(Perfil_propietario.this, Manejo_animal_view.class);
                startActivity(manejo);
                break;
            case R.id.action_back_start_p_p:
                Intent come_back = new Intent(Perfil_propietario.this, Inicio_Ganaderapp.class);
                startActivity(come_back);
                break;
            case R.id.action_p_prop_cerrar_cesion:
                preferences = getSharedPreferences("preferences", Context.MODE_PRIVATE);

                Intent come_backs = new Intent(Perfil_propietario.this, MainActivity.class);
                preferences.edit().apply();
                startActivity(come_backs);
                break;

        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_p_prop);
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
