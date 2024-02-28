package com.agroapp.proyecto_esmeralda.views.manejo_usuarios_view;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.agroapp.proyecto_esmeralda.R;
import com.agroapp.proyecto_esmeralda.interfaces.Share_References_interface;
import com.agroapp.proyecto_esmeralda.interfaces.User_Interface;
import com.agroapp.proyecto_esmeralda.modelos.User_Model;
import com.agroapp.proyecto_esmeralda.controlador.Share_References_presenter;
import com.agroapp.proyecto_esmeralda.controlador.User_Presenter;
import com.agroapp.proyecto_esmeralda.views.inicio_view.Control_agricola_view;
import com.agroapp.proyecto_esmeralda.views.inicio_view.MainActivity;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class User_Register extends AppCompatActivity {

    private User_Model modelo_usuario;
    private ArrayList<String> fincas;
    private Boolean registro_por_prop;
    private String eleccion_tipo_usuario, id_propietario, clave_encrip, id_encrip;
    private Button btn_registrar_emdo, btn_cancelar;
    private User_Interface user_interface;
    private Share_References_interface share_references_interface;
    private EditText edt_nombre, edt_id_propietario, edt_cedula, edt_correo, edt_telefono, edt_clave, edt_nombre_de_usuario;
    private static final String ALGORITHM = "AES";
    private static final String KEY = "1Hbfh667adfDEJ78";
    private ProgressDialog progressBar;
    SharedPreferences preferences;
    ArrayAdapter array_adapter_usuario;
    String[] tipo_usuario;
    Spinner spinner_tipo_user;
    Switch simpleSwitch;
    ImageView img_experiencia_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);
        iniciar_variables();

        simpleSwitch = findViewById(R.id.switch_politicas);

        tipo_usuario = new String[]{"empleado", "administrador", "propietario"};
        fincas = new ArrayList();
        fincas.add(0, "vacio");
        fincas.add(1, "vacio");
        fincas.add(2, "vacio");
        fincas.add(3, "vacio");
        validar_registro();
        if (registro_por_prop) {
            id_propietario = share_references_interface.id_propietario(preferences);
            edt_id_propietario.setText(id_propietario);
        }

        spinner_tipo_user.setAdapter(array_adapter_usuario);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(User_Register.this, android.R.layout.simple_spinner_item, tipo_usuario);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_tipo_user.setAdapter(arrayAdapter);
        spinner_tipo_user.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {
                Object item = parent.getItemAtPosition(pos);
                eleccion_tipo_usuario = (String) item;
                if (eleccion_tipo_usuario.equals("propietario")) {
                    findViewById(R.id.ln_id_propietario).setVisibility(View.GONE);
                } else {
                    findViewById(R.id.ln_id_propietario).setVisibility(View.VISIBLE);
                }

            }

            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(User_Register.this, "no se ha seleccionado", Toast.LENGTH_SHORT).show();
            }
        });


        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(User_Register.this, "registro cancelado", Toast.LENGTH_SHORT).show();
                Intent main = new Intent(User_Register.this, MainActivity.class);
                startActivity(main);
            }
        });
        simpleSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent main = new Intent(User_Register.this, Control_agricola_view.class);
                startActivity(main);
            }
        });
        btn_registrar_emdo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( simpleSwitch.isChecked()){
                    if (edt_cedula.getText().toString().length() == 0 || edt_clave.getText().toString().length() == 0 || edt_correo.getText().toString().length() == 0) {
                        Toast.makeText(User_Register.this, "Algunos Campos Son Obligatorios", Toast.LENGTH_SHORT).show();
                        Toast.makeText(User_Register.this, "Rellenalos Porfavor", Toast.LENGTH_SHORT).show();
                    } else {
                        Snackbar.make(v, "desea agregar este registro", Snackbar.LENGTH_INDEFINITE)
                                .setAction("agregar", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        progressBar.setTitle("cargando...");
                                        progressBar.setCancelable(true);
                                        progressBar.show();
                                        String nombre = edt_nombre.getText().toString().trim();
                                        String cedula = String.valueOf(Long.valueOf(edt_cedula.getText().toString()));
                                        String gmail = edt_correo.getText().toString();
                                        String alias_user = edt_nombre_de_usuario.getText().toString();
                                        String clave = edt_clave.getText().toString();
                                        Long telefono = Long.valueOf(edt_telefono.getText().toString());
                                        String img_user = "img_experiencia_user";
                                        try {
                                            clave_encrip = user_interface.encrypt(clave);
                                            id_encrip = user_interface.encrypt(cedula);

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                        if (!eleccion_tipo_usuario.equals("propietario")) {
                                            id_propietario = edt_id_propietario.getText().toString().trim();
                                            modelo_usuario = new User_Model(nombre, clave_encrip, id_encrip, id_propietario, false, false, 0, fincas, eleccion_tipo_usuario, alias_user, 0L, gmail, img_user, telefono, false);
                                        } else {
                                            String vacio = "vacio";
                                            if (registro_por_prop) {
                                                vacio = id_propietario;
                                            }
                                            modelo_usuario = new User_Model(nombre, clave_encrip, id_encrip, vacio, false, false, 1, fincas, eleccion_tipo_usuario, alias_user, 0L, gmail, img_user, telefono, true);
                                        }
                                        user_interface.user_register(modelo_usuario, progressBar, clave, gmail);
                                    }
                                }).show();

                    }
                }else {
                    Toast.makeText(User_Register.this, "Debes Aceptar Las Politicas de Privacidad, Para Poder Registrarse", Toast.LENGTH_LONG).show();
                }


            }
        });

    }


    private Boolean validar_registro() {

        registro_por_prop = preferences.getBoolean("registro_empleado", false);
        if (registro_por_prop != null) {
            return registro_por_prop;
        } else {
            return null;
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


    public void iniciar_variables() {

        edt_nombre = findViewById(R.id.edt_r_nombre_usuario);
        edt_cedula = findViewById(R.id.edt_cedula_user);
        edt_id_propietario = findViewById(R.id.edt_id_propietario);
        edt_telefono = findViewById(R.id.edt_telelefono_user);
        edt_clave = findViewById(R.id.edt_contraseña_user);
        //tv_politicas = findViewById(R.id.tv_politicas);
        edt_nombre_de_usuario = findViewById(R.id.edt_nombre_de_usuario);
        edt_correo = findViewById(R.id.edt_email_user);
        progressBar = new ProgressDialog(User_Register.this);
        img_experiencia_user = findViewById(R.id.img_experiencia_user);
        btn_registrar_emdo = findViewById(R.id.btn_registro_usuario);
        btn_cancelar = findViewById(R.id.btn_cancelar_r_usuario);
        spinner_tipo_user = findViewById(R.id.spinner_personal_tipo_usuario);
        user_interface = new User_Presenter(User_Register.this);
        share_references_interface = new Share_References_presenter(User_Register.this);
        preferences = getSharedPreferences("preferences", MODE_PRIVATE);

    }

}