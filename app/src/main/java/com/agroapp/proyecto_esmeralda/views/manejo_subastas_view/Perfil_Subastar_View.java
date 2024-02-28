package com.agroapp.proyecto_esmeralda.views.manejo_subastas_view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import com.agroapp.proyecto_esmeralda.R;
import com.agroapp.proyecto_esmeralda.modelos.Animal_Model;
import com.agroapp.proyecto_esmeralda.views.inicio_view.Inicio_Ganaderapp;
import com.agroapp.proyecto_esmeralda.views.inicio_view.Login_view;
import com.agroapp.proyecto_esmeralda.views.manejo_animal_view.Manejo_animales_produ;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

public class Perfil_Subastar_View extends AppCompatActivity  {
    TextView tv_nombre, tv_raza, tv_n_padre, tv_n_madre, tv_chapeta, tv_genero, tv_etapa, tv_f_na, tv_estado;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    SharedPreferences preferences;
    String n_finca, id_animal;
    TextView tv_fecha;
    Animal_Model modelo_animal;
    private Chronometer chronometer;
    private long pauseOffset;
    private boolean running;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_subasta);
        Toolbar toolbar = findViewById(R.id.toolbar_detalle_animal);
        setSupportActionBar(toolbar);
        tv_fecha = findViewById(R.id.edt_sub_anml_tipo);
        validar_finca_n();
        date_picker();

        chronometer = findViewById(R.id.chronometer);
        chronometer.setFormat("Time: %s");
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                if ((SystemClock.elapsedRealtime() - chronometer.getBase()) >= 10000) {
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    Toast.makeText(Perfil_Subastar_View.this, "Bing!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    public void startChronometer(View v) {
        if (!running) {
            chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
            chronometer.start();
            running = true;
        }
    }
    public void pauseChronometer(View v) {
        if (running) {
            chronometer.stop();
            pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
            running = false;
        }
    }
    public void resetChronometer(View v) {
        chronometer.setBase(SystemClock.elapsedRealtime());
        pauseOffset = 0;
    }

    public void date_picker() {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedDate = df.format(c.getTime());
            tv_fecha.setText(formattedDate);
        }


    }

    private void consultar_animal() {
        DocumentReference docRef = db.collection("fincas").document(n_finca);
        final DocumentReference coreff = docRef.collection("animales").document(id_animal);

        coreff.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                modelo_animal = documentSnapshot.toObject(Animal_Model.class);
                String chapeta = modelo_animal.getAnml_chapeta();
                String genero = modelo_animal.getAnml_genero();
                String etapa = modelo_animal.getAnml_etapa_tipo();
                String f_na = modelo_animal.getAnml_fecha_nacimiento();
                String padre = modelo_animal.getAnml_padre();
                String raza_animal = modelo_animal.getAnml_raza();
                String nombre_animal = modelo_animal.getAnml_nombre();
                String madre = modelo_animal.getAnml_madre();
                String estado = modelo_animal.getAnml_salida();

                tv_chapeta.setText(chapeta);
                tv_etapa.setText(etapa);
                tv_genero.setText(genero);
                tv_f_na.setText(f_na);
                tv_n_madre.setText(madre);
                tv_n_padre.setText(padre);
                tv_nombre.setText(nombre_animal);
                tv_raza.setText(raza_animal);


            }
        });

    }

    public void eliminar_palpapcion() {
        DocumentReference docRef = db.collection("fincas").document(n_finca);
        final DocumentReference coreff = docRef.collection("animales").document(id_animal);

        coreff.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(Perfil_Subastar_View.this, " eliminado exitosamente", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void editar_animal() {
        String fe_edit = tv_f_na.getText().toString();
        String nombre = tv_nombre.getText().toString();
        String chapeta = tv_chapeta.getText().toString();
        String etapa = tv_etapa.getText().toString();
        String genero = tv_genero.getText().toString();
        String padre = tv_n_padre.getText().toString();
        String madre = tv_n_madre.getText().toString();
        String raza = tv_raza.getText().toString();
        String estado = tv_estado.getText().toString();
        DocumentReference docRef = db.collection("fincas").document(n_finca);
        final DocumentReference coreff = docRef.collection("subasta").document(id_animal);

        coreff.update("anml_fecha_nacimiento", fe_edit, "anml_salida", estado, "anml_raza", raza, "anml_nombre", nombre, "anml_chapeta", chapeta, "anml_genero", genero, "anml_etapa_tipo", etapa, "anml_padre", padre, "anml_madre", madre).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(Perfil_Subastar_View.this, "exitoso", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private String validar_finca_n() {
        preferences = getSharedPreferences("preferences", MODE_PRIVATE);
        n_finca = preferences.getString("finca", null);
        if (n_finca != null) {
            return n_finca;
        } else {
            return null;
        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_perfil_subastar, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_atras_perfil_sub:
                Intent manejo = new Intent(Perfil_Subastar_View.this, Inicio_Ganaderapp.class);
                startActivity(manejo);
                break;
            case R.id.action_p_sub_add_animal:
                Intent manejo_lista_animal = new Intent(Perfil_Subastar_View.this, Manejo_animales_produ.class);
                startActivity(manejo_lista_animal);
                break;
            case R.id.action_p_sub_cerrar_cession:
                Intent subasta = new Intent(Perfil_Subastar_View.this, Login_view.class);
                preferences.edit().clear().apply();
                startActivity(subasta);
                break;


        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
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
