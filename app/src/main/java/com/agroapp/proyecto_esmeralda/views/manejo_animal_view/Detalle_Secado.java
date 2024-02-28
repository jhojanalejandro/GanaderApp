package com.agroapp.proyecto_esmeralda.views.manejo_animal_view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.agroapp.proyecto_esmeralda.R;
import com.agroapp.proyecto_esmeralda.interfaces.Share_References_interface;

import com.agroapp.proyecto_esmeralda.modelos.Gastos_Insumos;
import com.agroapp.proyecto_esmeralda.controlador.Share_References_presenter;
import com.agroapp.proyecto_esmeralda.views.perfil_animal_views.Perfil_Animal_view;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Detalle_Secado extends AppCompatActivity {

    TextView tv_nombre, edt_fecha_secado, edt_droga, edt_cant_droga;
    SharedPreferences preferences;
    String n_finca, n_animal, id_animal, id_secado, id_propietario;
    EditText edt_tratamiento, edt_obs;
    Gastos_Insumos secado_model;
    int cant_droga = 0;
    FloatingActionButton fab_editar, fab_delete;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_secado);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        edt_fecha_secado = findViewById(R.id.tv_d_secado_f);
        edt_tratamiento = findViewById(R.id.edt_d_secado_trat);

        tv_nombre = findViewById(R.id.tv_d_secado_nombre);
        edt_droga = findViewById(R.id.tv_d_secado_droga);
        edt_cant_droga = findViewById(R.id.tv_d_secado_cant_droga);
        edt_obs = findViewById(R.id.edt_d_secado_obs);
        preferences = getSharedPreferences("preferences", MODE_PRIVATE);

        Share_References_interface share_references_interface = new Share_References_presenter(Detalle_Secado.this);
        n_finca = share_references_interface.farm_name(preferences);
        id_propietario = share_references_interface.id_propietario(preferences);
        id_animal = share_references_interface.id_animal(preferences);
        n_animal();
        id_secado_animal();

        if (n_finca == null || id_secado_animal() == null || id_animal== null) {
            Toast.makeText(Detalle_Secado.this, "Error en Los Datos", Toast.LENGTH_SHORT).show();
        } else {
            consultar_secado();
            tv_nombre.setText(n_animal);
        }

        fab_editar = findViewById(R.id.fab_secado_editar);
        fab_delete = findViewById(R.id.fab_secado_eliminar);
        fab_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "estas seguro de eliminar este registro", Snackbar.LENGTH_SHORT)
                        .setAction("Eliminar", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                eliminar_secado();
                            }
                        }).show();

            }
        });
        fab_editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "estas seguro de editar este registro", Snackbar.LENGTH_SHORT)
                        .setAction("Editar", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                editar_secado();
                            }
                        }).show();
            }
        });
    }

    public void eliminar_secado() {
        DocumentReference ref_usuarios = db.collection("usuarios").document(id_propietario);
        DocumentReference docRef = ref_usuarios.collection("fincas").document(n_finca);
        final DocumentReference coreff = docRef.collection("animales").document(id_animal);
        final DocumentReference regis_ref = coreff.collection("registro_animal").document(id_secado);

        regis_ref.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(Detalle_Secado.this, " eliminado exitosamente", Toast.LENGTH_SHORT).show();
                    Intent detalle = new Intent(Detalle_Secado.this, Perfil_Animal_view.class);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("id_animal", id_animal);
                    editor.putString("finca", n_finca);
                    editor.commit();
                    startActivity(detalle);
                }
            }
        });

    }

    public void editar_secado() {
        String obs = edt_obs.getText().toString();
        String trat = edt_tratamiento.getText().toString();
        DocumentReference ref_usuarios = db.collection("usuarios").document(id_propietario);
        DocumentReference docRef = ref_usuarios.collection("fincas").document(n_finca);
        final DocumentReference coreff = docRef.collection("animales").document(id_animal);
        final DocumentReference regis_ref = coreff.collection("registro_animal").document(id_secado);

        regis_ref.update("scd_tratamiento", trat, "scd_observaciones", obs).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(Detalle_Secado.this, "exitoso", Toast.LENGTH_SHORT).show();
                    Intent detalle = new Intent(Detalle_Secado.this, Perfil_Animal_view.class);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("id_animal", id_animal);
                    editor.putString("finca", n_finca);
                    editor.commit();
                    startActivity(detalle);
                }
            }
        });

    }

    public void consultar_secado() {
        DocumentReference ref_usuarios = db.collection("usuarios").document(id_propietario);
        DocumentReference docRef = ref_usuarios.collection("fincas").document(n_finca);
        final DocumentReference coreff = docRef.collection("animales").document(id_animal);
        final DocumentReference regis_ref = coreff.collection("registro_animal").document(id_secado);
        regis_ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.getResult().exists()) {
                    secado_model = task.getResult().toObject(Gastos_Insumos.class);
                    cant_droga = secado_model.getCantidad_droga();
                    String fecha = secado_model.getFecha_registro();
                    String obs = secado_model.getObservaciones();
                    String tratamiento = secado_model.getTratamiento();
                    String droga = secado_model.getNombre_droga();

                    if (droga != null && cant_droga != 0) {
                        String cant_drogas = String.valueOf(cant_droga);
                        edt_cant_droga.setText(cant_drogas);
                        edt_droga.setText(droga);
                    }
                    edt_fecha_secado.setText(fecha);
                    edt_obs.setText(obs);
                    edt_tratamiento.setText(tratamiento);
                }
            }
        });
    }

    private String id_secado_animal() {
        preferences = getSharedPreferences("preferences", MODE_PRIVATE);
        id_secado = preferences.getString("id_registro", null);
        if (id_secado != null) {
            return id_secado;
        } else {
            return null;
        }
    }

    private String n_animal() {
        n_animal = preferences.getString("nombre_animal", null);
        if (n_animal != null) {
            return n_animal;
        } else {
            return null;
        }
    }
}