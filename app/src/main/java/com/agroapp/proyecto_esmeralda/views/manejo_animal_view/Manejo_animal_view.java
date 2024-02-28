package com.agroapp.proyecto_esmeralda.views.manejo_animal_view;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import com.agroapp.proyecto_esmeralda.views.inicio_view.MainActivity;
import com.agroapp.proyecto_esmeralda.views.manejo_insumos_view.Registro_insumos_potrero_dialog;
import com.agroapp.proyecto_esmeralda.views.perfil_admin_views.Perfil_admin_view;
import com.agroapp.proyecto_esmeralda.adapters.Animals_Recycle_Adapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

public class Manejo_animal_view extends AppCompatActivity implements Equines_View_Fragment.OnFragmentInteractionListener, Pigs_View_Fragment.OnFragmentInteractionListener {

    SharedPreferences preferences;
    Animal_Register_View_Fragment registro_animal;
    String farm_name, id_propietario;
    int nivel_acceso = 0;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Share_References_interface share_references_interface;

    Animals_Recycle_Adapter animal_viewhollder;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manejo_animal);
        Toolbar toolbar = findViewById(R.id.toolbar_mnjo_anml);
        setSupportActionBar(toolbar);

        Manejo_Animal_View_SectionsPagerAdapter sectionsPagerAdapter = new Manejo_Animal_View_SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager_manejo_animal);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs_manejo_animal);
        tabs.setupWithViewPager(viewPager);
        iniciar_v_manejo_animal();
        FloatingActionButton fab = findViewById(R.id.fab_registrar_animal);

        farm_name = share_references_interface.farm_name(preferences);
        nivel_acceso = share_references_interface.nivel_acceso(preferences);
        id_propietario = share_references_interface.id_propietario(preferences);

        if (farm_name == null || id_propietario == null) {
            Toast.makeText(Manejo_animal_view.this, "no tienes acceso a la los procedimientos " + farm_name, Toast.LENGTH_LONG).show();
        }
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected() & networkInfo.isAvailable()) {

        } else {
            Toast.makeText(Manejo_animal_view.this, "No Estas conectado a internet (off line)", Toast.LENGTH_LONG).show();
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registro_animal = new Animal_Register_View_Fragment();
                registro_animal.show(getSupportFragmentManager(), "registro de Animales ");

            }
        });

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_manejo_animal, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        switch (id) {
            case R.id.action_cerrar_cession_manejo_a:
                preferences.edit().clear().apply();
                share_references_interface.next_Intent(Manejo_animal_view.this, MainActivity.class);
                break;
            case R.id.action_atras_manejo_animal:
                navigateUpTo(new Intent(this, Inicio_Ganaderapp.class));
                break;
            case R.id.action_lista_lote_levante:
                share_references_interface.next_Intent(Manejo_animal_view.this, Manejo_animales_produ.class);
                break;
            case R.id.action_registro_gasto_concentrados:
                Registro_insumos_potrero_dialog registro_insumo_potrero_dialog = new Registro_insumos_potrero_dialog();
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("tipo_concentrado", "concentrado");
                editor.apply();
                registro_insumo_potrero_dialog.show(getSupportFragmentManager(), "registro insumo portero ");
                break;

            case R.id.action_mna_ir_menu_admin:
                if (nivel_acceso == 3) {
                    Toast.makeText(Manejo_animal_view.this, "OPCION NO PERMITIDA", Toast.LENGTH_SHORT).show();
                } else {
                    share_references_interface.next_Intent(Manejo_animal_view.this, Perfil_admin_view.class);
                }

                break;


        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Boolean b;
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Intent login = new Intent(Manejo_animal_view.this, Inicio_Ganaderapp.class);
            startActivity(login);
            b = true;
        } else {
            b = false;
        }

        return b;
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        // User pressed the search button
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        // User changed the text
        return true;
    }

    private void iniciar_v_manejo_animal() {

        share_references_interface = new Share_References_presenter(Manejo_animal_view.this);
        preferences = getSharedPreferences("preferences", MODE_PRIVATE);

    }

    private BroadcastReceiver networkStateReceiver = new BroadcastReceiver() {
        boolean connected = false;

        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo ni = manager.getActiveNetworkInfo();
            onNetworkChange(ni);
        }
    };

    public static boolean compruebaConexion(Context context) {

        boolean connected = false;

        ConnectivityManager connec = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        // Recupera todas las redes (tanto móviles como wifi)
        NetworkInfo[] redes = connec.getAllNetworkInfo();

        for (int i = 0; i < redes.length; i++) {
            // Si alguna red tiene conexión, se devuelve true
            if (redes[i].getState() == NetworkInfo.State.CONNECTED) {
                connected = true;
            }
        }
        return connected;
    }


    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(networkStateReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    public void onPause() {
        unregisterReceiver(networkStateReceiver);
        super.onPause();
    }

    private void onNetworkChange(NetworkInfo networkInfo) {
        if (networkInfo != null) {
            if (networkInfo.isAvailable()) {
                Toast.makeText(Manejo_animal_view.this, "Conectado", Toast.LENGTH_SHORT).show();
            } else {
                FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                        .setPersistenceEnabled(true)
                        .build();
                db.setFirestoreSettings(settings);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}