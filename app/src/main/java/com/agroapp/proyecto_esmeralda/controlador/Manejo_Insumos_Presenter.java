package com.agroapp.proyecto_esmeralda.controlador;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.agroapp.proyecto_esmeralda.R;
import com.agroapp.proyecto_esmeralda.adapters.Supplies_concentrados_Recycle_Adapter_View;
import com.agroapp.proyecto_esmeralda.interfaces.Manejo_Insumos_Interface;
import com.agroapp.proyecto_esmeralda.modelos.Insumo_Finca_Model;
import com.agroapp.proyecto_esmeralda.modelos.Produccion_Model;
import com.agroapp.proyecto_esmeralda.views.manejo_insumos_view.Manejo_Insumos_View;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Manejo_Insumos_Presenter implements Manejo_Insumos_Interface {

    Produccion_Model ficha_produccion;
    String insumo = "vacio", nombre_toro = "vacio";
    private int cant_restante, restado;
    Double precio_unitario = 0.0;
    Context context;
    DocumentReference fincas_ref;
    CollectionReference animales_ref;

    public Manejo_Insumos_Presenter(Context context, DocumentReference fincas_ref) {
        this.context = context;
        this.fincas_ref = fincas_ref;
    }

    public Manejo_Insumos_Presenter(Context context, DocumentReference fincas_ref, CollectionReference animales_ref) {
        this.context = context;
        this.fincas_ref = fincas_ref;
        this.animales_ref = animales_ref;
    }

    @Override
    public void mostrar_insumos(String tipo, RecyclerView recyclerView, LinearLayoutManager layoutManager) {
        Supplies_concentrados_Recycle_Adapter_View supplies_viewhollder;
        ArrayList<Insumo_Finca_Model> Supplies_list = new ArrayList<>();
        supplies_viewhollder = new Supplies_concentrados_Recycle_Adapter_View(context, R.layout.item_insumo_concentrado, Supplies_list);

        fincas_ref.collection("insumos").whereEqualTo("ins_finca_tipo", tipo).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    if (documentSnapshot.exists()) {
                        Insumo_Finca_Model insumo_model;
                        insumo_model = documentSnapshot.toObject(Insumo_Finca_Model.class);
                        Supplies_list.add(insumo_model);

                    }

                }
                recyclerView.setAdapter(supplies_viewhollder);
                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context, layoutManager.getOrientation());
                recyclerView.addItemDecoration(dividerItemDecoration);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "error " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void mostrar_concentrados(String tipo, RecyclerView recyclerView, LinearLayoutManager layoutManager) {
        Supplies_concentrados_Recycle_Adapter_View supplies_viewhollder;
        ArrayList<Insumo_Finca_Model> Supplies_list = new ArrayList<>();
        supplies_viewhollder = new Supplies_concentrados_Recycle_Adapter_View(context, R.layout.item_insumo_concentrado, Supplies_list);

        fincas_ref.collection("insumos").whereNotEqualTo("ins_finca_tipo", "Droga").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    if (documentSnapshot.exists()) {
                        Insumo_Finca_Model insumo_model;
                        insumo_model = documentSnapshot.toObject(Insumo_Finca_Model.class);
                        String tipo_traido = insumo_model.getIns_finca_tipo();
                        if (!tipo_traido.equals(tipo)) {
                            Supplies_list.add(insumo_model);
                        }


                    }

                }
                recyclerView.setAdapter(supplies_viewhollder);
                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context, layoutManager.getOrientation());
                recyclerView.addItemDecoration(dividerItemDecoration);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "error " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public String mostrar_spinner_insumos(String tipo, Spinner spinner_insumos) {

        fincas_ref.collection("insumos").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot queryDocumentSnapshots = task.getResult();
                    if (queryDocumentSnapshots.isEmpty()) {
                        Toast.makeText(context, "no hay insumos: ", Toast.LENGTH_LONG).show();
                    } else {
                        final List<String> insumos = new ArrayList<>();
                        for (DocumentSnapshot readData : queryDocumentSnapshots.getDocuments()) {
                            if (readData.exists()) {
                                insumo = readData.get("ins_finca_nombre").toString();
                                String tipo_traido = readData.get("ins_finca_tipo").toString();
                                if (tipo_traido.equals(tipo)) {
                                    insumos.add(insumo);
                                }
                            } else {
                                insumo = "vacio";
                            }

                        }
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, insumos);
                        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_insumos.setAdapter(arrayAdapter);
                        spinner_insumos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                                Object item = parent.getItemAtPosition(pos);
                                insumo = (String) item;
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    }
                }
            }
        }).addOnFailureListener(e -> Log.d("finca_nombre", e.getMessage()));

        return insumo;
    }

    @Override
    public String mostrar_spinner_toros(TextView tv_toro, Spinner spinner_insumos) {

        animales_ref.whereEqualTo("anml_genero", "macho").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot queryDocumentSnapshots = task.getResult();
                    if (queryDocumentSnapshots.isEmpty()) {
                        Toast.makeText(context, "no hay toros: ", Toast.LENGTH_LONG).show();
                    } else {
                        final List<String> insumos = new ArrayList<>();
                        for (DocumentSnapshot readData : queryDocumentSnapshots.getDocuments()) {
                            if (readData.exists()) {
                                nombre_toro = readData.get("anml_nombre").toString();
                                String etapa = readData.get("anml_etapa_tipo").toString();
                                if (etapa.equals("productiva")) {
                                    insumos.add(nombre_toro);
                                }

                            } else {
                                nombre_toro = "vacio";
                            }

                        }
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, insumos);
                        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_insumos.setAdapter(arrayAdapter);
                        spinner_insumos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                                Object item = parent.getItemAtPosition(pos);
                                nombre_toro = (String) item;
                                tv_toro.setText(nombre_toro);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    }
                }
            }
        }).addOnFailureListener(e -> Log.d("finca_nombre", e.getMessage()));

        return insumo;
    }

    private void progress_error(Exception e, Context context) {
        ProgressDialog progressBar = new ProgressDialog(context);
        progressBar.setTitle("fallo de Conexion...");
        progressBar.setCancelable(true);
        progressBar.setButton("Se Ha Presentado Un Error En El Systema" + e, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                progressBar.dismiss();

            }
        });
        progressBar.show();
    }

    @Override
    public void registrar_gasto_insumo(String droga, int cant_droga) {

        fincas_ref.collection("insumos").document(droga).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                Insumo_Finca_Model supplies_model = document.toObject(Insumo_Finca_Model.class);
                precio_unitario = supplies_model.getIns_finca_precio_unitario();
                String observaciones = supplies_model.getIns_finca_observaciones();
                cant_restante = supplies_model.getIns_finca_restante();
                if (cant_droga > cant_restante) {
                    Toast.makeText(context, "la cantidad escrita supera la cantidad restante", Toast.LENGTH_SHORT).show();
                    precio_unitario = 0.0;

                } else if (cant_droga == cant_restante) {
                    fincas_ref.collection("insumos").document(droga).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(context, "Registro Exitoso, Se Ha Terminado Este Insumo", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } else {
                    restado = cant_restante - cant_droga;
                    fincas_ref.collection("insumos").document(droga).update("ins_finca_restante", restado).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(context, "Gasto De Insumo Actualizado", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }

        });
    }

    @Override
    public void registrar_insumo(String insumo, Double precios, int cant_insumos, Double unitario, String fecha, String tipo, String obs, int mes, int ano) {

        ProgressDialog progressBar = new ProgressDialog(context);
        progressBar.setTitle("cargando...");
        progressBar.setCancelable(false);
        progressBar.setIcon(R.drawable.ic_three_anmls_small);
        progressBar.show();

        fincas_ref.collection("insumos").document(insumo).get().addOnCompleteListener(task -> {
            if (task.getResult().exists()) {
                Insumo_Finca_Model insumo_finca = task.getResult().toObject(Insumo_Finca_Model.class);
                int cantidad_actual = insumo_finca.getIns_finca_restante();
                cantidad_actual += cant_insumos;
                String observaciones = insumo_finca.getIns_finca_observaciones();
                fincas_ref.collection("insumos").document(insumo).update("ins_finca_restante", cantidad_actual, "ins_fina_precio", precios, "ins_finca_precio_unitario", unitario, "ins_finca_observaciones", observaciones + "Nueva Observacion :" + obs).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            gasto_insumo(precios, fecha, mes, ano, progressBar);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progress_error(e, context);
                    }
                });


            } else {
                Insumo_Finca_Model insumo_finca = new Insumo_Finca_Model(insumo, tipo, fecha, cant_insumos, cant_insumos, precios, unitario, obs);
                fincas_ref.collection("insumos").document(insumo).set(insumo_finca).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            gasto_insumo(precios, fecha, mes, ano, progressBar);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Error En el Registro" + e, Toast.LENGTH_LONG).show();
                        progress_error(e, context);
                    }
                });
            }

        });
    }

    public void gasto_insumo(Double precios, String fecha, int mes, int ano, ProgressDialog progressBar) {

        fincas_ref.collection("produccion").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.isEmpty()) {
                    ficha_produccion = new Produccion_Model(precios, 0.0, 0.0, 0, 0, 0, 0, 0.0, fecha, 0.0, 0, false, false);

                    fincas_ref.collection("produccion").document().set(ficha_produccion).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(context, "Registro Exitso", Toast.LENGTH_SHORT).show();
                                Intent p_admin = new Intent(context, Manejo_Insumos_View.class);
                                context.startActivity(p_admin);
                                progressBar.dismiss();
                            }
                        }
                    }).addOnFailureListener(e -> {
                        Toast.makeText(context, "Error En el gasto" + e, Toast.LENGTH_SHORT).show();
                        progressBar.setTitle("fallo de Conexion...");
                        progressBar.setCancelable(true);
                        progressBar.setButton("Se Ha Presentado Un Error En El Systema" + e, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                progressBar.dismiss();
                            }
                        });
                        progressBar.show();
                    });
                } else {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        if (documentSnapshot.exists()) {
                            ficha_produccion = documentSnapshot.toObject(Produccion_Model.class);
                            String fecha_traida = ficha_produccion.getProd_fecha();
                            String id = documentSnapshot.getId();
                            int[] fecha_par = parsea_Fecha(fecha_traida);
                            int mes_tra = fecha_par[1];
                            int anio_tra = fecha_par[2];
                            if (mes_tra == mes & anio_tra == ano) {
                                Double cant_gasto_mensual = ficha_produccion.getProd_gasto_mensual();
                                Double sumado = cant_gasto_mensual + precios;
                                fincas_ref.collection("produccion").document(id).update("prod_gasto_mensual", sumado).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(context, "Registro Exitoso existente", Toast.LENGTH_SHORT).show();
                                            Intent p_admin = new Intent(context, Manejo_Insumos_View.class);
                                            context.startActivity(p_admin);
                                            progressBar.dismiss();
                                        }
                                    }
                                }).addOnFailureListener(e -> {
                                    progressBar.setTitle("Error en el sistema...");
                                    progressBar.setCancelable(true);
                                    progressBar.setButton("Se Ha Presentado Un Error En El Systema" + e, new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            progressBar.dismiss();

                                        }
                                    });
                                    progressBar.show();
                                });
                                return;

                            } else {
                                ficha_produccion = new Produccion_Model(precios, 0.0, 0.0, 0, 0, 0, 0, 0.0, fecha, 0.0, 0, false, false);
                                fincas_ref.collection("produccion").document().set(ficha_produccion).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(context, "Registro Exitso", Toast.LENGTH_SHORT).show();
                                            Intent p_admin = new Intent(context, Manejo_Insumos_View.class);
                                            context.startActivity(p_admin);
                                            progressBar.dismiss();
                                        }
                                    }
                                }).addOnFailureListener(e -> {
                                    progressBar.setTitle("fallo de Conexion...");
                                    progressBar.setCancelable(true);
                                    progressBar.setButton("Se Ha Presentado Un Error En El Systema" + e, new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            progressBar.dismiss();

                                        }
                                    });
                                    progressBar.show();
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
                Toast.makeText(context, "Error Al Realizar la Accion", Toast.LENGTH_SHORT).show();
                progressBar.dismiss();
            }
        });
    }

    public int[] parsea_Fecha(String fechaEntera) {
        int day = 0, month = 0, year = 0;
        try {
            //transforma la cadena en un tipo date
            Date miFecha = new SimpleDateFormat("dd/MM/yyyy").parse(fechaEntera);

            //creo un calendario
            Calendar calendario = Calendar.getInstance();
            //establezco mi fecha
            calendario.setTime(miFecha);

            //obtener el año
            year = calendario.get(Calendar.YEAR);
            //obtener el mes (0-11 ::: enero es 0 y diciembre es 11)
            month = calendario.get(Calendar.MONTH) + 1;
            //obtener el dia del mes (1-31)
            day = calendario.get(Calendar.DAY_OF_MONTH);

            //...mas campos...

        } catch (ParseException ex) {
            //manejar excepcion
        }
        return new int[]{day, month, year};
    }


}
