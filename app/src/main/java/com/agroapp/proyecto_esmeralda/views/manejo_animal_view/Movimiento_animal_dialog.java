package com.agroapp.proyecto_esmeralda.views.manejo_animal_view;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.agroapp.proyecto_esmeralda.R;
import com.agroapp.proyecto_esmeralda.activities.Probar_connexion;
import com.agroapp.proyecto_esmeralda.modelos.Produccion_Model;
import com.agroapp.proyecto_esmeralda.modelos.Finca_model;
import com.agroapp.proyecto_esmeralda.controlador.Share_References_presenter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class Movimiento_animal_dialog extends AppCompatDialogFragment {

    ImageButton ibtn_registro_animales_calendario;
    SharedPreferences preferences;
    Spinner tipos_spinner_tipo_movimiento, spinner_farm_name;
    String[] tipo_movimiento;
    TextView tv_nombre_anial;
    EditText edt_fecha_m, edt_obs, edt_precio;
    String movimiento, fecha_traida,fecha_registro,obs, muerte = "muerto", vendido = "vendido", id_animal, traslado = "trasladado", farm_name, nombre_animal, id_propietario;
    ArrayAdapter comboAdapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DatePickerDialog datePickerDialog;
    Produccion_Model ficha_produccion;
    Finca_model modelo_finca;
    View view;
    Double cant_ganancias_traida;
    int dia, mes_r, ano;
    Probar_connexion probar_connexion;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.movimiento_animal_dialog, null);
        inicio_variables();
        Share_References_presenter share_references_presenter = new Share_References_presenter(getContext());
        id_animal = share_references_presenter.id_animal(preferences);
        id_propietario = share_references_presenter.id_propietario(preferences);
        nombre_animal = share_references_presenter.animal_name(preferences);

        if (id_propietario != null ) {
            tv_nombre_anial.setText(nombre_animal);
            spinner_finca();
        }

        tipos_spinner_tipo_movimiento.setAdapter(comboAdapter);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, tipo_movimiento);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tipos_spinner_tipo_movimiento.setAdapter(arrayAdapter);
        tipos_spinner_tipo_movimiento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                movimiento = (String) item;
                if (movimiento.equals(traslado)) {
                    view.findViewById(R.id.ln_r_mvo_spiner_fincas).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.ln_r_mvo_price).setVisibility(View.GONE);

                } else if (movimiento.equals(vendido)) {
                    view.findViewById(R.id.ln_r_mvo_price).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.ln_r_mvo_spiner_fincas).setVisibility(View.GONE);

                } else {
                    view.findViewById(R.id.ln_r_mvo_price).setVisibility(View.GONE);
                    view.findViewById(R.id.ln_r_mvo_spiner_fincas).setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ibtn_registro_animales_calendario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                dia = calendar.get(calendar.DAY_OF_MONTH);
                mes_r = calendar.get(Calendar.MONTH);
                ano = calendar.get(Calendar.YEAR);

                datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int ano, int mes, int dia) {
                        mes_r =mes + 1;
                        edt_fecha_m.setText(dia + "/" + mes_r + "/" + ano);
                    }
                }, ano, mes_r , dia);
                datePickerDialog.show();
            }
        });
        builder.setView(view)
                .setTitle("movimiento animal")
                .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(view.getContext(), "Registro Cancelado ", Toast.LENGTH_SHORT).show();

                    }
                })
                .setPositiveButton("ENVIAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        obs = edt_obs.getText().toString();
                        if (edt_fecha_m.getText().toString().length()== 0){
                            Toast.makeText(getContext(), "NECESITAS UNA FECHA", Toast.LENGTH_SHORT).show();
                        }else {
                            fecha_registro = edt_fecha_m.getText().toString();

                            if (movimiento.equals(traslado)) {
                                enviar_traslado();
                            } else if (movimiento.equals(vendido)) {
                                enviar_venta();

                            } else if (movimiento.equals(muerte)) {
                                enviar_muerte();
                            }
                        }


                    }


                });

        return builder.create();


    }

    private void enviar_traslado() {
        DocumentReference ref_usuarios = db.collection("usuarios").document(id_propietario);
        DocumentReference fincas_ref = ref_usuarios.collection("fincas").document(farm_name);
        DocumentReference dos = fincas_ref.collection("animales").document(id_animal);
        dos.update("anml_salida", traslado, "anml_farm_name", farm_name, "anml_lote", null, "anml_observaciones", obs,"anml_fecha_salida", fecha_registro);
    }

    private void enviar_muerte() {


        DocumentReference ref_usuarios = db.collection("usuarios").document(id_propietario);
        DocumentReference fincas_ref = ref_usuarios.collection("fincas").document(farm_name);
        DocumentReference dos = fincas_ref.collection("animales").document(id_animal);
        dos.update("anml_salida", muerte, "anml_lote", "vacio", "anml_fecha_salida", fecha_registro, "anml_observaciones", obs);

    }

    private void spinner_finca() {

        DocumentReference ref_usuarios = db.collection("usuarios").document(id_propietario);
        CollectionReference fincas_ref = ref_usuarios.collection("fincas");
        fincas_ref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot queryDocumentSnapshots = task.getResult();
                    final List<String> fincas = new ArrayList<String>();
                    for (DocumentSnapshot readData : queryDocumentSnapshots.getDocuments()) {
                        String finca = readData.get("finca_nombre").toString();
                        fincas.add(finca);
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, fincas);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_farm_name.setAdapter(arrayAdapter);
                    spinner_farm_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Object item = parent.getItemAtPosition(position);
                            farm_name = (String) item;
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {


                        }
                    });

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(t,e.getMessage(),Toast.LENGTH_LONG).show();
                Log.d("finca_nombre", e.getMessage());
            }
        });
    }

    private void inicio_variables() {
        spinner_farm_name = view.findViewById(R.id.spinner_mov_animal_fincas);
        ibtn_registro_animales_calendario = view.findViewById(R.id.ibtn_movi_fecha);
        edt_fecha_m = view.findViewById(R.id.edt_movi_fecha);
        edt_obs = view.findViewById(R.id.edt_r_mvo_obs);
        edt_precio = view.findViewById(R.id.edt_r_mvo_price);
        tv_nombre_anial = view.findViewById(R.id.tv_r_mvo_n_animal);
        tipos_spinner_tipo_movimiento = view.findViewById(R.id.spinner_movi_animal);
        tipo_movimiento = new String[]{traslado, muerte, vendido};
        probar_connexion = new Probar_connexion();
        preferences = getContext().getSharedPreferences("preferences", MODE_PRIVATE);

    }

    private void enviar_venta() {

        Double precio_animal = Double.parseDouble(edt_precio.getText().toString());

        DocumentReference ref_usuarios = db.collection("usuarios").document(id_propietario);
        DocumentReference fincas_ref = ref_usuarios.collection("fincas").document(farm_name);
        DocumentReference dos = fincas_ref.collection("animales").document(id_animal);
        dos.update("anml_salida", vendido, "anml_lote", "vacio", "anml_observaciones", obs, "anml_precio", precio_animal,"anml_fecha_salida", fecha_registro).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(view.getContext(), "Registro Exitoso", Toast.LENGTH_SHORT).show();
                    guardar_precio_animal(precio_animal);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(view.getContext(), " Error : " + e, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void guardar_precio_animal(Double precios) {

        DocumentReference ref_usuarios = db.collection("usuarios").document(id_propietario);
        DocumentReference fincas_ref = ref_usuarios.collection("fincas").document(farm_name);
        final CollectionReference coreff = fincas_ref.collection("produccion");
        coreff.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.isEmpty()) {
                    ficha_produccion = new Produccion_Model(0.0, precios, 0.0, 0, 0, 0, 0, 0.0, fecha_registro, 0.0,0,false,false);
                    coreff.document().set(ficha_produccion).addOnCompleteListener(task -> {
                        Toast.makeText(view.getContext(), "Registro Nuevo Exitoso ", Toast.LENGTH_SHORT).show();
                    });
                } else {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        if (documentSnapshot.exists()) {
                            ficha_produccion = documentSnapshot.toObject(Produccion_Model.class);
                            fecha_traida = ficha_produccion.getProd_fecha();
                            String id = documentSnapshot.getId();
                            int[] fecha_par = probar_connexion.parsea_Fecha(fecha_traida);
                            int mes_tra = fecha_par[1];
                            int anio_tra = fecha_par[2];
                            if (mes_tra == mes_r & anio_tra == ano) {
                                Toast.makeText(view.getContext(), "todo va bien  ", Toast.LENGTH_SHORT).show();
                                cant_ganancias_traida = ficha_produccion.getProd_ganancia_mensual();
                                if (cant_ganancias_traida > 0) {
                                    cant_ganancias_traida += precios;
                                    coreff.document(id).update("prod_ganancia_mensual", cant_ganancias_traida).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(view.getContext(), "Registro Actualizado ", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                }
                                return;
                            }else {
                                Toast.makeText(view.getContext(), "algo va mal" + "mes_traido " + mes_tra + "ano_traido" + anio_tra, Toast.LENGTH_SHORT).show();
                                ficha_produccion = new Produccion_Model(0.0, precios, 0.0, 0, 0, 0, 0, 0.0, fecha_registro, 0.0,0,false,false);
                                coreff.document().set(ficha_produccion).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            dismiss();
                                            Toast.makeText(view.getContext(), "Registro Exitoso " +"mes "+ mes_r + "ano" + ano , Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                                return;
                            }
                        }

                    }
                }


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(view.getContext(), "Error Al Realizar la Accion", Toast.LENGTH_SHORT).show();
            }
        });
    }



}
