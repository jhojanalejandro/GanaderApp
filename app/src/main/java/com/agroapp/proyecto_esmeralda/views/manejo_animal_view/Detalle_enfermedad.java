package com.agroapp.proyecto_esmeralda.views.manejo_animal_view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.agroapp.proyecto_esmeralda.R;
import com.agroapp.proyecto_esmeralda.modelos.Animal_Model;
import com.agroapp.proyecto_esmeralda.modelos.Emfermedad_Model;
import com.agroapp.proyecto_esmeralda.modelos.Vacunacion_Model;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Detalle_enfermedad extends AppCompatActivity {

    TextView tv_fecha_efmd,tv_trat,tv_obs,tv_cant_droga,tv_droga,tv_event,tv_tipo_vacuna;
    SharedPreferences preferences;
    String tipo,n_finca,n_animal,id_efmd,id_animal,id_propietario;
    TextView tv_nombre;
    Emfermedad_Model enfermedad_model;
    Vacunacion_Model vacunacion_model;

    FloatingActionButton fab_editar,fab_delete;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_enfermedad);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        preferences =  getSharedPreferences("preferences", MODE_PRIVATE);
        tv_fecha_efmd = findViewById(R.id.tv_d_efmd_f);
        tv_nombre = findViewById(R.id.tv_d_efmd_nombre);
        tv_trat = findViewById(R.id.tv_d_efmd_trat);
        tv_droga = findViewById(R.id.tv_d_efmd_droga);
        tv_tipo_vacuna = findViewById(R.id.tv_d_efmd_tipo_vacuna);
        tv_event =findViewById(R.id.tv_d_efmd_event);
        tv_cant_droga = findViewById(R.id.tv_d_efmd_cant_droga);
        tv_obs = findViewById(R.id.tv_d_efmd_obs);
        validar_finca_n();

        tipo_registro();
        id_animal();
        id_efmd_animal();
        id_propietario();
        if (validar_finca_n() == null || id_animal == null || tipo  == null){
            Toast.makeText(Detalle_enfermedad.this, "No Sera Posible Guardar la Informacion" + tipo, Toast.LENGTH_SHORT).show();
        }else {
            if (tipo.equals("enfermedad")){

                consultar_enfermedad();
                findViewById(R.id.ln_d_efmd_tipo_vacuna).setVisibility(View.GONE);
            }else if (tipo.equals("vacunacion")){
                findViewById(R.id.ln_d_efmd_tipo_vacuna).setVisibility(View.VISIBLE);
                consultar_vacunacion();


            }
            tv_nombre.setText(n_animal);
        }

        fab_editar = findViewById(R.id.fab_d_efmd_editar);
        fab_delete = findViewById(R.id.fab_d_efmd_eliminar);
        fab_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "estas seguro de eliminar este registro", Snackbar.LENGTH_SHORT)
                        .setAction("Eliminar", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                eliminar_enfermedad();
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
                                editar_enfermedad();
                            }
                        }).show();
            }
        });
    }
    private void eliminar_enfermedad(){
        DocumentReference ref_usuarios = db.collection("usuarios").document(id_propietario);
        DocumentReference docRef = ref_usuarios.collection("fincas").document(n_finca);
        final DocumentReference coreff = docRef.collection("animales").document(id_animal);
        final DocumentReference regis_ref = coreff.collection("registro_animal").document(id_efmd);

        regis_ref.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(Detalle_enfermedad.this, " eliminado exitosamente", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private void editar_enfermedad(){
        String fe_edit = tv_fecha_efmd.getText().toString();
        String obs = tv_obs.getText().toString();
        String event = tv_event.getText().toString();
        String droga = tv_droga.getText().toString();
        String cant_droga = tv_cant_droga.getText().toString();
        int cant_drogas  = Integer.parseInt(cant_droga);

        DocumentReference ref_usuarios = db.collection("usuarios").document(id_propietario);
        DocumentReference docRef = ref_usuarios.collection("fincas").document(n_finca);
        final DocumentReference coreff = docRef.collection("animales").document(id_animal);
        final DocumentReference regis_ref = coreff.collection("registro_animal").document(id_efmd);
        regis_ref.update("efmd_fecha",fe_edit,"efmd_evento",event,"efmd_droga_aplicada",droga,"efmd_cant_droga",cant_drogas,"efmd_observacoines",obs).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(Detalle_enfermedad.this, "se edito la informacion con exito", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    public void consultar_enfermedad(){
        DocumentReference ref_usuarios = db.collection("usuarios").document(id_propietario);
        DocumentReference docRef = ref_usuarios.collection("fincas").document(n_finca);
        final DocumentReference coreff = docRef.collection("animales").document(id_animal);
        coreff.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    Animal_Model animal_model;
                    animal_model = task.getResult().toObject(Animal_Model.class);
                    n_animal = animal_model.getAnml_nombre();
                }
            }
        });
        final DocumentReference regis_ref = coreff.collection("registro_animal").document(id_efmd);

        regis_ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if ( task.getResult().exists()){
                    enfermedad_model = task.getResult().toObject(Emfermedad_Model.class);

                    int cant_droga = enfermedad_model.getCantidad_droga();
                    String obs = enfermedad_model.getObservaciones();
                    String fecha = enfermedad_model.getFecha_registro();
                    String droga = enfermedad_model.getNombre_droga();
                    String event = enfermedad_model.getEfmd_evento();
                    String trat = enfermedad_model.getTratamiento();


                    if (droga != null && cant_droga != 0  ){
                        String droga_cant = String.valueOf(cant_droga);
                        tv_cant_droga.setText(droga_cant);
                        tv_droga.setText(droga);
                    }
                    tv_fecha_efmd.setText(fecha);
                    tv_obs.setText(obs);
                    tv_event.setText(event);
                    tv_trat.setText(trat);

                }
            }
        });


    }
    public void consultar_vacunacion(){
        DocumentReference ref_usuarios = db.collection("usuarios").document(id_propietario);
        DocumentReference docRef = ref_usuarios.collection("fincas").document(n_finca);
        final DocumentReference coreff = docRef.collection("animales").document(id_animal);
        coreff.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    Animal_Model animal_model;
                    animal_model = task.getResult().toObject(Animal_Model.class);
                    n_animal = animal_model.getAnml_nombre();
                }
            }
        });
        final DocumentReference regis_ref = coreff.collection("registro_animal").document(id_efmd);

        regis_ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if ( task.getResult().exists()){
                    vacunacion_model = task.getResult().toObject(Vacunacion_Model.class);

                    int cant_droga = vacunacion_model.getCantidad_droga();
                    String fecha = vacunacion_model.getFecha_registro();
                    String tipo_v = vacunacion_model.getVcn_tipo_eleccion();
                    String obs = vacunacion_model.getObservaciones();
                    String droga = vacunacion_model.getNombre_droga();
                    String event = vacunacion_model.getVcn_evento();
                    String trat = vacunacion_model.getTratamiento();


                    if (droga != null && cant_droga != 0  ){
                        String droga_cant = String.valueOf(cant_droga);
                        tv_cant_droga.setText(droga_cant);
                        tv_droga.setText(droga);
                    }
                    tv_fecha_efmd.setText(fecha);
                    tv_obs.setText(obs);
                    tv_event.setText(event);
                    tv_trat.setText(trat);
                    tv_tipo_vacuna.setText(tipo_v);

                }
            }
        });


    }

    private String validar_finca_n(){
        n_finca = preferences.getString("finca",null);
        if ( n_finca!=null){
            return n_finca;
        }else {
            return  null;
        }
    }
    private String tipo_registro(){

        tipo = preferences.getString("tipo_registro",null);
        if ( tipo!=null){
            return tipo;
        }else {
            return  null;
        }
    }
    private String id_animal(){
        id_animal = preferences.getString("id_animal",null);
        if ( n_animal!=null){
            return n_animal;
        }else {
            return  null;
        }
    }
    private String id_propietario(){
        id_propietario = preferences.getString("id_propietario",null);
        if ( id_propietario!=null){
            return id_propietario;
        }else {
            return  null;
        }
    }

    private String id_efmd_animal(){
        id_efmd = preferences.getString("id_registro",null);
        if ( id_efmd!=null){
            return id_efmd;
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
