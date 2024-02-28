package com.agroapp.proyecto_esmeralda.views.manejo_usuarios_view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.agroapp.proyecto_esmeralda.R;
import com.agroapp.proyecto_esmeralda.interfaces.Share_References_interface;
import com.agroapp.proyecto_esmeralda.interfaces.User_Interface;
import com.agroapp.proyecto_esmeralda.controlador.Share_References_presenter;
import com.agroapp.proyecto_esmeralda.controlador.User_Presenter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

public class Detalle_Usuario extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    String id_usuario, finca1 = "vacio", finca2 = "vacio", finca3 = "vacio", finca4 = "vacio", id_propietario;
    SharedPreferences preferences;

    Boolean click_button1 = true, click_button2 = true, click_button3 = true, click_button4 = true;

    Long pago_mes;
    int dia, mes, ano;
    EditText edt_pago_mensual, edt_nivel_acceso, edt_tipo;
    TextView tv_nombre, tv_telefono, tv_cedula;
    ArrayList<String> fincas_list;
    RadioButton rb_finca1, rb_finca2, rb_finca3, rb_finca4;
    RadioGroup rd_finca1, rd_finca2, rd_finca3, rd_finca4;
    Spinner spinner_n_fincas;
    String finca_busqueda = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_usuario);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Share_References_interface share_references_interface = new Share_References_presenter(Detalle_Usuario.this);
        User_Interface user_interface = new User_Presenter(Detalle_Usuario.this);
        tv_nombre = findViewById(R.id.tv_detalle_nombre_usuario);
        tv_telefono = findViewById(R.id.tv_detalle_telelefono_user);
        tv_cedula = findViewById(R.id.tv_detalle_cedula_user);
        edt_pago_mensual = findViewById(R.id.edt_detalle_pago_mensual);
        edt_nivel_acceso = findViewById(R.id.edt_nivel_accesso);
        edt_tipo = findViewById(R.id.edt_tipo_usuario);


        spinner_n_fincas = findViewById(R.id.spinner_eleccion_finca_usuario);

        preferences = getSharedPreferences("preferences", MODE_PRIVATE);
        id_usuario = share_references_interface.id_usuario(preferences);
        id_propietario = share_references_interface.id_propietario(preferences);
        fincas_list = new ArrayList<>();
        fincas_list.add(0, "vacio");
        fincas_list.add(1, "vacio");
        fincas_list.add(2, "vacio");
        fincas_list.add(3, "vacio");

        NumberPicker np = findViewById(R.id.numberPicker);
        np.setMinValue(1);
        np.setMaxValue(3);

        np.setOnValueChangedListener(onValueChangeListener_);

        if (id_usuario != null) {
            user_interface.detalle_personal( id_usuario, tv_nombre, tv_cedula, tv_telefono, edt_tipo, edt_nivel_acceso);
        } else {
            Toast.makeText(Detalle_Usuario.this, "No Se Pordra ver la Infromacion", Toast.LENGTH_SHORT).show();
        }

        Query query = db.collection("usuarios").document(id_propietario).collection("fincas");
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    QuerySnapshot queryDocumentSnapshots = task.getResult();
                    final List<String> fincas = new ArrayList<String>();
                    fincas.add(0, "AGREGA LAS FINCAS");
                    for (DocumentSnapshot readData : queryDocumentSnapshots.getDocuments()) {
                        finca_busqueda = readData.get("finca_nombre").toString();

                        fincas.add(finca_busqueda);
                    }
                    if (fincas != null) {
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Detalle_Usuario.this, android.R.layout.simple_list_item_multiple_choice, fincas);
                        arrayAdapter.setDropDownViewResource(android.R.layout.simple_list_item_multiple_choice);
                        spinner_n_fincas.setAdapter(arrayAdapter);
                        spinner_n_fincas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                                if (pos > 0) {
                                    Object item = parent.getItemAtPosition(pos);
                                    String finca_user = String.valueOf(item);
                                    switch (pos) {
                                        case 1:
                                            fincas_list.set(0, finca_user);
                                            Toast.makeText(Detalle_Usuario.this, "Eleccion 1", Toast.LENGTH_SHORT).show();
                                            break;
                                        case 2:
                                            fincas_list.set(1, finca_user);
                                            break;
                                        case 3:
                                            fincas_list.set(2, finca_user);
                                            break;
                                        case 4:
                                            fincas_list.set(3, finca_user);
                                            Toast.makeText(Detalle_Usuario.this, "Eleccion 4", Toast.LENGTH_SHORT).show();
                                            break;
                                    }


                                }

                            }

                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                    }

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("nombre de la finca", e.getMessage());
            }
        });
        FloatingActionButton fab = findViewById(R.id.fab_detalle_insumos);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int nivel_acceso = Integer.parseInt(edt_nivel_acceso.getText().toString());
                if (edt_pago_mensual.length() ==0){
                    pago_mes = 0L;
                }else {
                    pago_mes = Long.parseLong(edt_pago_mensual.getText().toString());

                }
                user_interface.actualizar_datos_personal( id_usuario, nivel_acceso, pago_mes, fincas_list);
            }
        });

    }


    NumberPicker.OnValueChangeListener onValueChangeListener_ =
            (numberPicker, i, i1) -> {
                Toast.makeText(Detalle_Usuario.this,
                        "selected number " + numberPicker.getValue(), Toast.LENGTH_SHORT);
                String value = String.valueOf(numberPicker.getValue());
                edt_nivel_acceso.setText(value);
            };

    public void datepikers_hoy() {
        Calendar calendarNow = new GregorianCalendar(TimeZone.getTimeZone("Europe/Madrid"));
        dia = calendarNow.get(Calendar.DAY_OF_MONTH);
        mes = calendarNow.get(Calendar.MONTH) + 1;
        ano = calendarNow.get(Calendar.YEAR);
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
