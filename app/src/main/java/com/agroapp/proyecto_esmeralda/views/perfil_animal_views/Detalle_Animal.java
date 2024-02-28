package com.agroapp.proyecto_esmeralda.views.perfil_animal_views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.agroapp.proyecto_esmeralda.modelos.Animal_Model;
import com.agroapp.proyecto_esmeralda.controlador.Share_References_presenter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.agroapp.proyecto_esmeralda.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Detalle_Animal extends AppCompatActivity {

    EditText edt_nombre, edt_raza, edt_n_padre, edt_n_madre, edt_chapeta, edt_genero, edt_etapa, edt_f_na, edt_estado;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    SharedPreferences preferences;
    Share_References_presenter share_references_presenter;
    String farm_name, id_animal, id_propietario;
    DocumentReference animal_ref;
    Animal_Model modelo_animal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_animal);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        edt_chapeta = findViewById(R.id.edt_d_animal_chapeta);
        edt_nombre = findViewById(R.id.edt_d_animal_nombre);
        edt_etapa = findViewById(R.id.edt_d_animal_etapa);
        edt_estado = findViewById(R.id.edt_d_animal_estado);
        edt_n_madre = findViewById(R.id.edt_d_animal_n_madre);
        edt_n_padre = findViewById(R.id.edt_d_animal_n_padre);
        edt_genero = findViewById(R.id.edt_d_animal_genero);
        edt_raza = findViewById(R.id.edt_d_animal_raza);
        edt_f_na = findViewById(R.id.edt_d_animal_f_nacimiento);
        preferences = getSharedPreferences("preferences", MODE_PRIVATE);
        share_references_presenter = new Share_References_presenter(Detalle_Animal.this);

        id_animal = share_references_presenter.id_animal(preferences);
        id_propietario = share_references_presenter.id_propietario(preferences);
        farm_name = share_references_presenter.farm_name(preferences);
        DocumentReference[] registro_animales_array = share_references_presenter.referencedb_d(id_propietario, farm_name,id_animal);
        animal_ref = registro_animales_array[1];

        if (id_animal != null || farm_name != null) {
            consultar_animal();
        } else {
            Toast.makeText(Detalle_Animal.this, "algo pasa en la info", Toast.LENGTH_SHORT).show();

        }


        FloatingActionButton fab = findViewById(R.id.fab_d_animal_editar);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Desea Editar Los Datos", Snackbar.LENGTH_LONG)
                        .setAction("EDITAR", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                editar_animal();

                            }
                        }).show();
            }
        });
    }

    private void consultar_animal() {
        animal_ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
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

                if (estado == null) {
                    edt_estado.setText("activo");
                } else {
                    edt_estado.setText(estado);
                }
                edt_chapeta.setText(chapeta);
                edt_etapa.setText(etapa);
                edt_genero.setText(genero);
                edt_f_na.setText(f_na);
                edt_n_madre.setText(madre);
                edt_n_padre.setText(padre);
                edt_nombre.setText(nombre_animal);
                edt_raza.setText(raza_animal);


            }
        });

    }

    public void editar_animal() {

        String fe_edit = edt_f_na.getText().toString();
        String nombre = edt_nombre.getText().toString();
        String chapeta = edt_chapeta.getText().toString();
        String etapa = edt_etapa.getText().toString();
        String genero = edt_genero.getText().toString();
        String padre = edt_n_padre.getText().toString();
        String madre = edt_n_madre.getText().toString();
        String raza = edt_raza.getText().toString();
        String estado = edt_estado.getText().toString();
        animal_ref.update("anml_fecha_nacimiento", fe_edit, "anml_salida", estado, "anml_raza", raza, "anml_nombre", nombre, "anml_chapeta", chapeta, "anml_genero", genero, "anml_etapa_tipo", etapa, "anml_padre", padre, "anml_madre", madre).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(Detalle_Animal.this, "Actualizo exitoso", Toast.LENGTH_SHORT).show();
                    Intent intent_perfil_animal = new Intent(Detalle_Animal.this, Perfil_Animal_view.class);
                    startActivity(intent_perfil_animal);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Detalle_Animal.this, " Error " + e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

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