package com.agroapp.proyecto_esmeralda.views.manejo_animal_view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.agroapp.proyecto_esmeralda.R;
import com.agroapp.proyecto_esmeralda.modelos.Parto_Model;
import com.agroapp.proyecto_esmeralda.modelos.Servicio_Model;
import com.agroapp.proyecto_esmeralda.views.inicio_view.MainActivity;
import com.agroapp.proyecto_esmeralda.views.perfil_admin_views.Perfil_admin_view;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Detalle_calor extends AppCompatActivity {
    TextView tv_fecha_calor,tv_toro,tv_droga,tv_cant_droga,tv_nombre,tv_tratamiento,tv_obs,tv_numero_cria,tv_numero_parto,tv_n_padre,tev_genero,tv_raza,tv_tresult_real;
    SharedPreferences preferences;
    String id_animal,n_finca,n_animal,id_calor,tipo,id_propietario;
    int cant_droga = 0;
    Servicio_Model calor_model;
    Parto_Model parto_model;
    FloatingActionButton fab_delete;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_calor);
        Toolbar toolbar = findViewById(R.id.toolbar_detalle_calor);
        setSupportActionBar(toolbar);

        preferences =  getSharedPreferences("preferences", MODE_PRIVATE);
        tv_fecha_calor = findViewById(R.id.tv_d_calor_f);
        tv_tratamiento = findViewById(R.id.tv_d_calor_trat);
        tv_toro = findViewById(R.id.tv_d_calor_n_toro);
        tv_nombre = findViewById(R.id.tv_d_calor_nombre);
        tv_droga = findViewById(R.id.tv_d_calor_droga);
        tv_cant_droga = findViewById(R.id.tv_d_calor_cant_droga);
        tv_obs = findViewById(R.id.tv_d_calor_obs);
        tv_numero_cria = findViewById(R.id.tv_d_parto_numero_cria);
        tv_numero_parto = findViewById(R.id.tv_d_parto_numero);
        tv_tresult_real = findViewById(R.id.tv_d_parto_resul_real);
        tev_genero = findViewById(R.id.tv_d_parto_genero);
        tv_raza = findViewById(R.id.tv_d_parto_raza);
        tv_n_padre = findViewById(R.id.tv_d_parto_n_padre);
        validar_finca_n();
        n_animal();
        id_animal();
        tipo();
        id_calor_animal();
        id_propietario();

        if (id_animal == null || id_calor == null || tipo  == null){
            Toast.makeText(Detalle_calor.this, "Error en Los Datos", Toast.LENGTH_SHORT).show();
        }else {
            if (tipo.equals("calor")){
                consultar_calor();
                findViewById(R.id.ln_d_calor_n_toro).setVisibility(View.VISIBLE);
                findViewById(R.id.ln_d_part_cria).setVisibility(View.GONE);
                findViewById(R.id.ln_d_part_number_parto).setVisibility(View.GONE);
                findViewById(R.id.ln_d_part_result_real).setVisibility(View.GONE);
                findViewById(R.id.ln_d_part_genero).setVisibility(View.GONE);
                findViewById(R.id.ln_d_part_raza).setVisibility(View.GONE);
                findViewById(R.id.ln_d_part_n_padre).setVisibility(View.GONE);
                
            }else if (tipo.equals("parto")){
                consultar_parto();
                findViewById(R.id.ln_d_calor_n_toro).setVisibility(View.GONE);
                findViewById(R.id.ln_d_part_cria).setVisibility(View.VISIBLE);
                findViewById(R.id.ln_d_part_number_parto).setVisibility(View.VISIBLE);
                findViewById(R.id.ln_d_part_result_real).setVisibility(View.VISIBLE);
                findViewById(R.id.ln_d_part_genero).setVisibility(View.VISIBLE);
                findViewById(R.id.ln_d_part_raza).setVisibility(View.VISIBLE);
                findViewById(R.id.ln_d_part_n_padre).setVisibility(View.VISIBLE);

            }

            tv_nombre.setText(n_animal);
        }

        fab_delete = findViewById(R.id.fab_calor_eliminar);
        fab_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "estas seguro de eliminar este registro", Snackbar.LENGTH_SHORT)
                        .setAction("Eliminar", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                eliminar_calor();
                            }
                        }).show();

            }
        });

    }

    public void eliminar_calor(){
        DocumentReference ref_usuarios = db.collection("usuarios").document(id_propietario);
        DocumentReference docRef = ref_usuarios.collection("fincas").document(n_finca);
        final DocumentReference coreff = docRef.collection("animales").document(id_animal);
        final DocumentReference regis_ref = coreff.collection("registro_animal").document(id_calor);

        regis_ref.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(Detalle_calor.this, " eliminado exitosamente", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    public void editar_calor(){
        String fe_edit = tv_fecha_calor.getText().toString();
        String obs = tv_obs.getText().toString();
        String toro = tv_toro.getText().toString();
        String droga = tv_droga.getText().toString();
        String cant_droga = tv_cant_droga.getText().toString();
        String trat = tv_tratamiento.getText().toString();
        DocumentReference ref_usuarios = db.collection("usuarios").document(id_propietario);
        DocumentReference docRef = ref_usuarios.collection("fincas").document(n_finca);
        final DocumentReference coreff = docRef.collection("animales").document(id_animal);
        final DocumentReference regis_ref = coreff.collection("registro_animal").document(id_calor);

        regis_ref.update("calor_id_animal",fe_edit,"calor_droga_aplicada",droga,"calor_tratamiento",trat,"calor_cant_droga",cant_droga,"calor_observaciones",obs,"calor_n_toro",toro).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(Detalle_calor.this, "exitoso", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    public void consultar_calor(){

        DocumentReference ref_usuarios = db.collection("usuarios").document(id_propietario);
        DocumentReference docRef = ref_usuarios.collection("fincas").document(n_finca);
        final DocumentReference coreff = docRef.collection("animales").document(id_animal);
        final DocumentReference regis_ref = coreff.collection("registro_animal").document(id_calor);
        regis_ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    calor_model = task.getResult().toObject(Servicio_Model.class);

                    cant_droga = calor_model.getCantidad_droga();
                    String fecha = calor_model.getFecha_registro();
                    String obs = calor_model.getObservaciones();
                    String n_toro = calor_model.getCalor_n_toro();
                    String droga = calor_model.getNombre_droga();

                    if (droga != null && cant_droga != 0  ){
                        String cant_drogas = String.valueOf(cant_droga);
                        tv_cant_droga.setText(cant_drogas);
                        tv_droga.setText(droga);
                    }
                    tv_fecha_calor.setText(fecha);
                    tv_obs.setText(obs);
                    tv_toro.setText(n_toro);

                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Detalle_calor.this, "Se Ha Presentado Un error en la red:" , Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void consultar_parto(){
        DocumentReference ref_usuarios = db.collection("usuarios").document(id_propietario);
        DocumentReference docRef = ref_usuarios.collection("fincas").document(n_finca);
        final DocumentReference coreff = docRef.collection("animales").document(id_animal);
        final DocumentReference regis_ref = coreff.collection("registro_animal").document(id_calor);
        regis_ref.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                parto_model = task.getResult().toObject(Parto_Model.class);

                assert parto_model != null;
                cant_droga = parto_model.getCantidad_droga();
                String fecha = parto_model.getFecha_registro();
                String obs = parto_model.getObservaciones();
                String n_padre = parto_model.getPart_father_name();
                String genero = parto_model.getPart_result();
                String raza = parto_model.getPart_raza_cria();
                String result_real = parto_model.getPart_result_real();
                String droga_aplicada = parto_model.getNombre_droga();
                String numero_cria = parto_model.getPart_number_breeding();
                String numero_parto = String.valueOf(parto_model.getPart_number());

                if (droga_aplicada != null &  cant_droga !=0 ){
                    String cant_drogas = String.valueOf(cant_droga);
                    tv_cant_droga.setText(cant_drogas);
                    tv_droga.setText(droga_aplicada);
                }
                tv_fecha_calor.setText(fecha);
                tv_obs.setText(obs);
                tev_genero.setText(genero);
                tv_raza.setText(raza);
                tv_tresult_real.setText(result_real);
                tv_numero_cria.setText(numero_cria);
                tv_numero_parto.setText(numero_parto);
                tv_n_padre.setText(n_padre);

            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Detalle_calor.this, "Se Ha Presentado Un error en la red:" , Toast.LENGTH_SHORT).show();
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
    private String id_animal(){
        id_animal = preferences.getString("id_animal",null);
        if ( id_animal!=null){
            return id_animal;
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

    private String tipo(){
        tipo = preferences.getString("tipo_registro",null);
        if ( tipo!=null){
            return tipo;
        }else {
            return  null;
        }
    }


    private String id_calor_animal(){
        id_calor = preferences.getString("id_registro",null);
        if ( id_calor!=null){
            return id_calor;
        }else {
            return  null;
        }
    }

    private String n_animal(){
        n_animal = preferences.getString("nombre_animal",null);
        if ( n_animal!=null){
            return n_animal;
        }else {
            return  null;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detalle_calor, menu);
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
            case R.id.action_salir_detalle_calor:
                Intent manejo = new Intent(Detalle_calor.this, MainActivity.class);
                preferences.edit().clear().apply();
                startActivity(manejo);
                break;
            case R.id.action_menu_adm_agro:
                Intent manejo_perfil = new Intent(Detalle_calor.this, Perfil_admin_view.class);
                startActivity(manejo_perfil);
                break;
            case R.id.action_d_calor_lista_porero:
                Intent manejo_lista_animal = new Intent(Detalle_calor.this, Manejo_animales_produ.class);
                startActivity(manejo_lista_animal);
                break;

        }

        return super.onOptionsItemSelected(item);
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
