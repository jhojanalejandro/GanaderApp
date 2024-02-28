package com.agroapp.proyecto_esmeralda.views.manejo_animal_view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.agroapp.proyecto_esmeralda.R;
import com.agroapp.proyecto_esmeralda.interfaces.Share_References_interface;
import com.agroapp.proyecto_esmeralda.modelos.Animal_Model;
import com.agroapp.proyecto_esmeralda.modelos.Palpacion_Model;
import com.agroapp.proyecto_esmeralda.controlador.Share_References_presenter;
import com.agroapp.proyecto_esmeralda.views.perfil_animal_views.Perfil_Animal_view;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Detalle_palpacion extends AppCompatActivity {
    TextView tv_fecha,tv_fecha_pal,tv_droga,tv_obs,tv_cant_droga,tv_result,tv_nombre_vet;
    SharedPreferences preferences;
    String farm_name,n_animal,id_propietario,id_animal,id_palpacion;
    TextView tv_nombre;
    Palpacion_Model palpacion_model;
    String  fecha_s,result;
    FloatingActionButton fab_editar,fab_delete;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_palpacion);
        Toolbar toolbar = findViewById(R.id.toolbar_detalle_palpa);
        setSupportActionBar(toolbar);
        tv_fecha_pal = findViewById(R.id.tv_d_animal_f_serv);
        tv_fecha = findViewById(R.id.tv_d_animal_f_palp);
        tv_nombre = findViewById(R.id.tv_d_animal_nombre);
        tv_droga = findViewById(R.id.tv_d_palp_animal_droga);
        tv_result = findViewById(R.id.tv_d_animal_result_palp);
        tv_cant_droga = findViewById(R.id.tv_d_palp_animal_cant_droga);
        tv_obs = findViewById(R.id.tv_d_animal_obs);
        preferences =  getSharedPreferences("preferences", MODE_PRIVATE);

        tv_nombre_vet  = findViewById(R.id.tv_d_animal_nombre_vet);
        Share_References_interface share_references_interface = new Share_References_presenter(Detalle_palpacion.this);

        n_animal = share_references_interface.animal_name(preferences);
        id_animal = share_references_interface.id_animal(preferences);
        farm_name = share_references_interface.farm_name(preferences);
        id_propietario = share_references_interface.id_propietario(preferences);
        id_palpacion_animal();
        if (farm_name == null || id_animal == null || id_palpacion  == null){
            Toast.makeText(Detalle_palpacion.this, "algunos datos son necesarios", Toast.LENGTH_SHORT).show();
        }else {
            consultar_palpapcion();
            tv_nombre.setText(n_animal);
        }

        fab_editar = findViewById(R.id.fab_detalle_animal_editar);
        fab_delete = findViewById(R.id.fab_detalle_animal_delete);
        fab_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "estas seguro de eliminar este registro", Snackbar.LENGTH_SHORT)
                        .setAction("Eliminar", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                eliminar_palpapcion();
                            }
                        }).show();

            }
        });
        fab_editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "estas seguro de editar este registro", Snackbar.LENGTH_SHORT)
                .setAction("Editar",  new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        editar_palpapcion();
                    }
                }).show();
            }
        });
    }

    public void eliminar_palpapcion(){
        DocumentReference ref_usuarios = db.collection("usuarios").document(id_propietario);
        DocumentReference docRef = ref_usuarios.collection("fincas").document(farm_name);
        final DocumentReference coreff = docRef.collection("animales").document(id_animal);
        coreff.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    Animal_Model animal_model;
                    animal_model = task.getResult().toObject(Animal_Model.class);
                    n_animal = animal_model.getAnml_nombre();
                    tv_nombre.setText(n_animal);
                }
            }
        });
        final DocumentReference regis_ref = coreff.collection("registro_animal").document(id_palpacion);

        regis_ref.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(Detalle_palpacion.this, " eliminado exitosamente", Toast.LENGTH_SHORT).show();
                    Intent manejo = new Intent(Detalle_palpacion.this, Perfil_Animal_view.class);
                    startActivity(manejo);
                }
            }
        });

    }
    public void editar_palpapcion(){
        String fe_edit = tv_fecha_pal.getText().toString();
        String obs = tv_obs.getText().toString();
        String droga = tv_droga.getText().toString();
        String cant_droga = tv_cant_droga.getText().toString();
        String nombre_vet  = tv_nombre_vet.getText().toString();
        DocumentReference ref_usuarios = db.collection("usuarios").document(id_propietario);
        DocumentReference docRef = ref_usuarios.collection("fincas").document(farm_name);        final DocumentReference coreff = docRef.collection("animales").document(id_animal);
        final DocumentReference regis_ref = coreff.collection("registro_animal").document(id_palpacion);

        regis_ref.update("palp_fecha",fe_edit,"palp_observaciones",obs,"palp_droga_aplicada",droga,"palp_cant_droga",cant_droga,"palp_veterinario",nombre_vet,"palp_result",result).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(Detalle_palpacion.this, "exitoso", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    public void consultar_palpapcion(){
        DocumentReference ref_usuarios = db.collection("usuarios").document(id_propietario);
        DocumentReference docRef = ref_usuarios.collection("fincas").document(farm_name);
        final DocumentReference coreff = docRef.collection("animales").document(id_animal);
        final DocumentReference regis_ref = coreff.collection("registro_animal").document(id_palpacion);

        regis_ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if ( task.getResult().exists()){
                    palpacion_model = task.getResult().toObject(Palpacion_Model.class);

                    result= palpacion_model.getPalp_result();
                    fecha_s = palpacion_model.getPalp_fecha_pre();

                    int cant_droga = palpacion_model.getCantidad_droga();
                    String obs = palpacion_model.getObservaciones();
                    String n_vet = palpacion_model.getPalp_veterinario();
                    String droga = palpacion_model.getNombre_droga();
                    String fecha = palpacion_model.getFecha_registro();

                    if (droga != null && cant_droga != 0  ){
                        String cant_drogas = String.valueOf(cant_droga);
                        tv_cant_droga.setText(cant_drogas);
                        tv_droga.setText(droga);
                    }else if (obs != null){
                        tv_obs.setText(obs);

                    }
                    tv_nombre_vet.setText(n_vet);
                    tv_fecha.setText(fecha);
                    tv_fecha_pal.setText(fecha_s);
                    tv_result.setText(result);
                }
            }
        });

    }
    private String id_palpacion_animal(){
        id_palpacion = preferences.getString("id_registro",null);
        if ( id_palpacion!=null){
            return id_palpacion;
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
