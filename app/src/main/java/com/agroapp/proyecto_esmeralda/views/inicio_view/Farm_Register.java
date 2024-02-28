package com.agroapp.proyecto_esmeralda.views.inicio_view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.agroapp.proyecto_esmeralda.R;
import com.agroapp.proyecto_esmeralda.interfaces.Share_References_interface;
import com.agroapp.proyecto_esmeralda.modelos.Finca_model;
import com.agroapp.proyecto_esmeralda.modelos.User_Model;
import com.agroapp.proyecto_esmeralda.controlador.Share_References_presenter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Farm_Register extends AppCompatActivity {
    EditText edt_nombre, edt_extension_finca, edt_precio_arriendo,edt_precio_leche,edt_municipio, edt_vereda;
    CheckBox checkBox, checkBox_levante, checkBox_ceva, checkBox_leche;
    Finca_model modelo_finca;
    List<String> tipo_pro;
    Boolean precio_b = true;
    Long precio, precio_leche;
    private ProgressDialog progressBar;

    Button btn_enviar, btn_cancelar;
    String tipo_prod_ceva, id_propietario, tipo_prod_levante, tipo_prod_leche, eleccion_region,municipio,vereda, eleccion_propiedad, extension_finca;
    SharedPreferences preferences;

    ArrayAdapter array_adapter_ubicacion, array_adapter_propiedad;
    String[] tipo_ubicacion, tipo_propiedad;
    Spinner spinner_regiones, spinner_propiedad;
    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm_register);

        iniciar_variables();
        Share_References_interface share_references_interface = new Share_References_presenter(Farm_Register.this);
        preferences = Farm_Register.this.getSharedPreferences("preferences", MODE_PRIVATE);
        id_propietario = share_references_interface.id_propietario(preferences);

        tipo_ubicacion = new String[]{"antioquia", "arauca", "boyaca", "cesar", "cordoba", "cauca", "caqueta", "caqueta", "casanare", "la guajira", "magdalena", "nariño", "sucre", "bolivar", "meta", "putumayo", "santander", "vaupes"};
        tipo_propiedad = new String[]{"arrendada", "propia"};

        tipo_prod_ceva = (checkBox_ceva.isChecked() ? "ceva" : "No Marcado");
        tipo_prod_levante = (checkBox_levante.isChecked() ? "levante" : "No Marcado");
        tipo_prod_leche = (checkBox_leche.isChecked() ? "leche" : "No Marcado");
        spinner_propiedad.setAdapter(array_adapter_propiedad);
        ArrayAdapter<String> arrayAdapter_propiedad = new ArrayAdapter<String>(Farm_Register.this, android.R.layout.simple_spinner_item, tipo_propiedad);
        arrayAdapter_propiedad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_propiedad.setAdapter(arrayAdapter_propiedad);
        spinner_propiedad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {
                Object item = parent.getItemAtPosition(pos);
                eleccion_propiedad = (String) item;
                if (eleccion_propiedad.equals("arrendada")) {
                    findViewById(R.id.ln_r_precio_arriendo).setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.ln_r_precio_arriendo).setVisibility(View.GONE);
                }
            }
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_regiones.setAdapter(array_adapter_ubicacion);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Farm_Register.this, android.R.layout.simple_spinner_item, tipo_ubicacion);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_regiones.setAdapter(arrayAdapter);
        spinner_regiones.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> p, View v, int posicion, long id) {
                Object item = p.getItemAtPosition(posicion);
                eleccion_region = (String) item;
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setTitle("cargando...");
                progressBar.setCancelable(false);
                progressBar.show();
                registrar_finca();
            }

        });
        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent main = new Intent(Farm_Register.this, Inicio_Ganaderapp.class);
                startActivity(main);
            }
        });
        checkBox_leche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (precio_b){
                    findViewById(R.id.ln_r_finca_precio_leche).setVisibility(View.VISIBLE);
                    precio_b = false;
                }else {
                    findViewById(R.id.ln_r_finca_precio_leche).setVisibility(View.GONE);
                    precio_b = true;
                }

            }
        });



    }


    @Override
    public void onResume() {
        super.onResume();

    }

    private void iniciar_variables() {
        edt_nombre = findViewById(R.id.edt_r_finca_n);
        edt_precio_arriendo = findViewById(R.id.edt_cant_pago_arriendo);

        edt_extension_finca = findViewById(R.id.edt_r_finca_ext);

        db = FirebaseFirestore.getInstance();
        progressBar = new ProgressDialog(Farm_Register.this);
        spinner_regiones = findViewById(R.id.spinner_regiones);
        edt_municipio = findViewById(R.id.edt_r_finca_municipio);
        edt_vereda = findViewById(R.id.edt_r_finca_vereda);
        edt_precio_leche = findViewById(R.id.edt_r_finca_milk_price);
        spinner_propiedad = findViewById(R.id.spinner_tipo_propiedad);

        checkBox = new CheckBox(Farm_Register.this);
        btn_enviar = findViewById(R.id.btn_finca_r);
        btn_cancelar = findViewById(R.id.btn_finca_cancelar);
        checkBox_ceva = findViewById(R.id.checkbox_ceva);
        checkBox_leche = findViewById(R.id.checkbox_leche);
        checkBox_levante = findViewById(R.id.checkbox_levante);
    }


    @SuppressLint("NonConstantResourceId")
    public void registrar_finca() {
        final String nombre = edt_nombre.getText().toString();
        if (eleccion_propiedad.equals("arrendada")) {
            precio = Long.parseLong(edt_precio_arriendo.getText().toString().trim());
        } else {
            precio = 0L;
        }

        tipo_pro = Arrays.asList(tipo_prod_ceva, tipo_prod_levante, tipo_prod_leche);
        Share_References_interface share_references_interface = new Share_References_presenter(Farm_Register.this);
        int[] date = share_references_interface.date_picker();

        int ano = date[2];

        DocumentReference ref_usuarios = db.collection("usuarios").document(id_propietario);
        ref_usuarios.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    User_Model user_model = task.getResult().toObject(User_Model.class);
                    ArrayList<String> fincas = user_model.getFincas();
                    String finca1 = fincas.get(0);
                    String finca2 = fincas.get(1);
                    String finca3 = fincas.get(2);
                    String finca4 = fincas.get(3);
                    if (!finca4.equals("vacio")) {
                        Toast.makeText(Farm_Register.this, "HA LLENADO EL CUPO LIMITE DE  FINCAS", Toast.LENGTH_SHORT).show();
                        Toast.makeText(Farm_Register.this, "COMUNIQUESE CON EL PROVEEDOR PARA AMPLIAR SU PLAN", Toast.LENGTH_SHORT).show();
                    } else {
                        if (finca1.equals("vacio")) {
                            finca1 = nombre;
                        } else if (finca2.equals("vacio")) {
                            finca2 = nombre;

                        } else if (finca3.equals("vacio")) {
                            finca3 = nombre;
                        }else {
                            finca4 = nombre;
                        }
                        fincas.set(0, finca1);
                        fincas.set(1, finca2);
                        fincas.set(2, finca3);
                        fincas.set(3, finca4);
                        ref_usuarios.update("fincas", fincas).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    guadar_finca(nombre, ref_usuarios);
                                }
                            }
                        });
                    }


                }

            }
        });


    }

    private void guadar_finca(String nombre, DocumentReference ref_usuarios) {
        extension_finca = edt_extension_finca.getText().toString();
        municipio = edt_municipio.getText().toString();
        vereda = edt_vereda.getText().toString();
        if (edt_precio_leche.getText().toString().length() == 0){
            precio_leche = 0L;
        }else {
            precio_leche = Long.valueOf(edt_precio_leche.getText().toString());
        }

        extension_finca = edt_extension_finca.getText().toString();

        DocumentReference fincas_ref = ref_usuarios.collection("fincas").document(nombre);
        int ext = Integer.parseInt(extension_finca);
        modelo_finca = new Finca_model(nombre, ext, eleccion_region, tipo_pro, precio,eleccion_propiedad,municipio,vereda,precio_leche,false);

        fincas_ref.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().exists()) {
                    Toast.makeText(Farm_Register.this, "EL NOMBRE DE LA FINCA YA EXISTE, POR FAVOR  CAMBIELO", Toast.LENGTH_SHORT).show();
                } else {
                    fincas_ref.set(modelo_finca).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                progressBar.dismiss();
                                Toast.makeText(Farm_Register.this, "REGISTRO EXITSO DE LA FINCA: " + nombre, Toast.LENGTH_LONG).show();
                                Intent p_admin = new Intent(Farm_Register.this, Inicio_Ganaderapp.class);
                                Farm_Register.this.startActivity(p_admin);
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setTitle("fallo En El Registro ..." + e.getMessage());
                            progressBar.setCancelable(true);
                            progressBar.show();
                            progressBar.setButton("volver a intentar", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    progressBar.dismiss();

                                }
                            });

                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.dismiss();
                Toast.makeText(Farm_Register.this, "se han presentado problemas d: " + e.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }
}