package com.agroapp.proyecto_esmeralda.controlador;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.agroapp.proyecto_esmeralda.R;
import com.agroapp.proyecto_esmeralda.adapters.Gasto_Insumos_Recycle_Anmls_Adapter;
import com.agroapp.proyecto_esmeralda.interfaces.Manejo_Animal_Interface;
import com.agroapp.proyecto_esmeralda.interfaces.Perfil_Animal_Interface;
import com.agroapp.proyecto_esmeralda.modelos.Animal_Model;
import com.agroapp.proyecto_esmeralda.modelos.Gastos_Insumos;
import com.agroapp.proyecto_esmeralda.modelos.Insumo_Finca_Model;
import com.agroapp.proyecto_esmeralda.modelos.Parto_Model;
import com.agroapp.proyecto_esmeralda.modelos.Medida_Leche_Model;
import com.agroapp.proyecto_esmeralda.modelos.Palpacion_Model;
import com.agroapp.proyecto_esmeralda.modelos.Pesaje_Animal_Model;
import com.agroapp.proyecto_esmeralda.modelos.Produccion_Model;
import com.agroapp.proyecto_esmeralda.modelos.Servicio_Model;
import com.agroapp.proyecto_esmeralda.views.manejo_animal_view.Manejo_animales_produ;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

public class Perfil_animal_Presenter implements Perfil_Animal_Interface {

    RecyclerView recyclerView_registros_animal;
    Produccion_Model ficha_produccion = new Produccion_Model();
    private int retorno = 0, cant_restante, restado;
    private Double resta = 0.0, precio_unitario;
    private String fecha_palp_noti, chapeta_traida, tipo;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference registros_animales_ref, animales_ref;
    DocumentReference farm_ref, animal_ref;
    Context context;
    LinearLayoutManager layoutManager;
    private String nombre_animal, date_birt, chapeta, precio, estado_animal, ruta_foto, nombre_madre, nombre_padre, raza, etapa_animal, genero;
    Pesaje_Animal_Model ficha_pesaje_carne = new Pesaje_Animal_Model();

    public Perfil_animal_Presenter(Context context, RecyclerView recyclerView_registros_animal, CollectionReference registros_animales_ref, LinearLayoutManager layoutManager) {
        this.recyclerView_registros_animal = recyclerView_registros_animal;
        this.registros_animales_ref = registros_animales_ref;
        this.context = context;
        this.layoutManager = layoutManager;

    }

    public Perfil_animal_Presenter(Context context, CollectionReference registros_animales_ref, CollectionReference animales_ref, DocumentReference animal_ref) {
        this.registros_animales_ref = registros_animales_ref;
        this.animales_ref = animales_ref;
        this.context = context;
        this.animal_ref = animal_ref;
    }

    public Perfil_animal_Presenter(Context context, DocumentReference animal_ref, CollectionReference registros_animales_ref) {
        this.registros_animales_ref = registros_animales_ref;
        this.context = context;
        this.animal_ref = animal_ref;

    }

    public Perfil_animal_Presenter(Context context, CollectionReference animales_ref) {
        this.registros_animales_ref = animales_ref;
        this.context = context;
    }

    public Perfil_animal_Presenter(Context context, DocumentReference animal_ref) {
        this.animal_ref = animal_ref;
        this.context = context;
    }


    public Perfil_animal_Presenter(Context context, CollectionReference registros_animales_ref, DocumentReference animal_ref, DocumentReference farm_ref) {
        this.registros_animales_ref = registros_animales_ref;
        this.animal_ref = animal_ref;
        this.farm_ref = farm_ref;
        this.context = context;
    }

    public Perfil_animal_Presenter(Context context) {
        this.context = context;
    }


    @Override
    public void life_spend_register(Boolean connexion, Double valor, String ids_animal, String id_propietario, String finca) {

        animal_ref.get().addOnSuccessListener(documentSnapshot -> animal_ref.update("anml_g_vida", valor).addOnCompleteListener(task -> {
            if (connexion) {
                Toast.makeText(context, "Registro de gasto de vida exitoso", Toast.LENGTH_SHORT).show();

            }
        }));

    }

    @Override
    public void update_photo(String url, ProgressDialog progressDialog) {

        animal_ref.update("anml_imagen", url).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(context, "FOTO SUBIDA EXITOSAMENTE", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(context, "SE PRODUJO UN  ERROR VUELVA AINTENTARLO", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });

    }

    @Override
    public int[] parse_date(String date) {
        int day = 0, month = 0, year = 0;
        try {
            //transforma la cadena en un tipo date
            Date miFecha = new SimpleDateFormat("dd/MM/yyyy").parse(date);

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

    @Override
    public void cant_ins_consult(Boolean connexion, String droga, int cant_droga, String id_propietario, String farm_name, String id_animal) {
        if (connexion) {
            DocumentReference ref_usuarios = db.collection("usuarios").document(id_propietario);
            DocumentReference fincas_ref = ref_usuarios.collection("fincas").document(farm_name);
            final DocumentReference insumo_ref = fincas_ref.collection("insumos").document(droga);
            insumo_ref.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    Insumo_Finca_Model supplies_model = document.toObject(Insumo_Finca_Model.class);
                    precio_unitario = supplies_model.getIns_finca_precio_unitario();
                    cant_restante = supplies_model.getIns_finca_restante();
                    if (cant_droga > cant_restante) {
                        Toast.makeText(context, "la cantidad escrita supera la cantidad restante", Toast.LENGTH_SHORT).show();
                        precio_unitario = 0.0;

                    } else if (cant_droga == cant_restante) {
                        insumo_ref.delete().addOnCompleteListener(task12 -> {
                            if (task12.isSuccessful()) {
                                Toast.makeText(context, "Registro Exitoso, Se Ha Terminado la Droga", Toast.LENGTH_SHORT).show();
                                precio_unitario *= cant_droga;
                                life_spend_register(connexion, precio_unitario, id_animal, id_propietario, farm_name);
                            }
                        });

                    } else {
                        restado = cant_restante - cant_droga;
                        insumo_ref.update("ins_finca_restante", restado).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                Toast.makeText(context, "Gasto De Droga Actualizado", Toast.LENGTH_SHORT).show();
                                life_spend_register(connexion, precio_unitario, id_animal, id_propietario, farm_name);

                            }
                        });
                    }

                }

            });
        }
    }

    @Override
    public void mostrar_registros_animal(String id_animal, String type_data, ArrayList list_common_data_type, Gasto_Insumos_Recycle_Anmls_Adapter common_data_viewhollder) {

        registros_animales_ref.orderBy("fecha_registro", Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Gastos_Insumos opcion_datos_model;
                    String id_ = documentSnapshot.getId();
                    opcion_datos_model = documentSnapshot.toObject(Gastos_Insumos.class);
                    String type = opcion_datos_model.getTipo_registro();
                    if (type.equals(type_data)) {
                        opcion_datos_model.setObservaciones(id_);
                        opcion_datos_model.setTratamiento(id_animal);
                        list_common_data_type.add(opcion_datos_model);
                    }

                }
                recyclerView_registros_animal.setAdapter(common_data_viewhollder);
                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context, layoutManager.getOrientation());
                recyclerView_registros_animal.addItemDecoration(dividerItemDecoration);

            }
        });


    }

    @Override
    public int data_measur_anmls_register(int dia, int mes, int ano, String measur_date, String obs_measur, Double leche, Double results_leche_am, Boolean conexion) {

        ProgressDialog progressBar = new ProgressDialog(context);
        progressBar.setTitle("Cargando...");
        progressBar.setCancelable(false);
        progressBar.setIcon(R.drawable.ic_three_anmls_small);
        progressBar.show();

        registros_animales_ref.whereEqualTo("medida_tipo", "medida").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.isEmpty()) {
                    Double leche_am = 0.0, leche_pm = 0.0, result;
                    int count_dias = 0;
                    if (results_leche_am > 0.0) {
                        count_dias = 1;
                        result = leche + results_leche_am;
                        leche_pm = leche;
                        leche_am = leche;
                    } else {
                        result = leche;
                        leche_am = leche;
                    }

                    Medida_Leche_Model medida_leche = new Medida_Leche_Model("medida", obs_measur, leche_am, leche_pm, measur_date, 1, result);
                    registros_animales_ref.document().set(medida_leche).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                retorno = 1;
                                if (conexion) {
                                    Double result_llevar = results_leche_am + leche;
                                    registrar_medida_animal(progressBar, result_llevar, dia, mes, ano, true);

                                }

                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.dismiss();
                            Toast.makeText(context, "algo pasa en la base de datos", Toast.LENGTH_SHORT).show();

                        }
                    });

                } else {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        if (documentSnapshot.exists()) {
                            Medida_Leche_Model medida_leche = documentSnapshot.toObject(Medida_Leche_Model.class);
                            String id = documentSnapshot.getId();
                            int count_dias = medida_leche.getMedida_count_dias();
                            String fecha_traida = medida_leche.getMedida_anml_fecha();
                            int[] fe_pars = parse_date(fecha_traida);
                            int dia_traido = fe_pars[0];
                            int mes_tra = fe_pars[1];
                            int anio_tra = fe_pars[2];

                            if (mes == mes_tra & anio_tra == ano) {
                                Double result_leche_am = medida_leche.getMedida_leche_am();
                                Double result_leche_varios_dias = medida_leche.getMedida_litros_medidos_mes();
                                result_leche_varios_dias += leche;
                                if (dia_traido == dia) {
                                    registros_animales_ref.document(id).update("medida_leche_pm", leche, "medida_litros_medidos_mes", result_leche_varios_dias).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                retorno = 1;
                                                if (conexion) {
                                                    registrar_medida_animal(progressBar, leche, dia, mes, ano, false);
                                                }

                                            }
                                        }
                                    });

                                    return;
                                } else {
                                    registros_animales_ref.document(id).update("medida_leche_am", leche, "medida_litros_medidos_mes", result_leche_varios_dias).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                retorno = 1;
                                                if (conexion) {
                                                    registrar_medida_animal(progressBar, leche, dia, mes, ano, true);
                                                }

                                            }
                                        }
                                    });
                                }
                                return;
                            } else {

                                Double leche_am, leche_pm = 0.0, result;
                                if (results_leche_am > 0.0) {
                                    result = Double.valueOf(leche + results_leche_am);
                                    leche_am = Double.valueOf(results_leche_am);
                                    leche_pm = Double.valueOf(leche);

                                } else {
                                    result = Double.valueOf(leche);
                                    leche_am = Double.valueOf(leche);

                                }

                                Medida_Leche_Model medidas_leche = new Medida_Leche_Model("medida", obs_measur, leche_am, leche_pm, measur_date, 1, result);

                                registros_animales_ref.document().set(medidas_leche).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            retorno = 1;
                                            if (conexion) {
                                                Double result_llevar = results_leche_am + leche;
                                                registrar_medida_animal(progressBar, result_llevar, dia, mes, ano, true);
                                            }

                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context, "algo pasa en la base de datos", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(context, "No Se Ha podido Compretar la action" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return retorno;
    }

    private void registrar_medida_animal(ProgressDialog progressBar, Double leche_llevar, int dia, int mes, int ano, Boolean b_animal) {

        animal_ref.update("anml_prod_litros", leche_llevar).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    registrar_produccion_leche(progressBar, leche_llevar, b_animal, dia, mes, ano);
                }
            }
        });
    }

    @Override
    public void show_measur_animal(TextView textView, int dia, int mes, int ano) {
        registros_animales_ref.whereEqualTo("medida_tipo", "medida").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.isEmpty()) {
                    textView.setText("0");
                } else {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Medida_Leche_Model medida_leche = documentSnapshot.toObject(Medida_Leche_Model.class);
                        Double result_leche_am = medida_leche.getMedida_leche_am();
                        String fecha_medida = medida_leche.getMedida_anml_fecha();
                        Double litros_tarde = medida_leche.getMedida_leche_pm();
                        int[] fecha_traida = parse_date(fecha_medida);
                        int dia_traido = fecha_traida[0];
                        int mes_traido = fecha_traida[1];
                        int ano_traido = fecha_traida[2];

                        if (mes_traido == mes & dia_traido == dia & ano == ano_traido) {
                            if (litros_tarde == 0) {
                                String result_am = String.valueOf(result_leche_am);
                                textView.setText(result_am);
                                return;
                            } else {
                                Toast.makeText(context, "no es permitido medir leche mas de dos veces en un mismo dia", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            textView.setText("0");
                            return;
                        }

                    }
                }

            }
        });

    }

    @Override
    public void show_weigh_animal(TextView textView, int dia, int mes, int ano) {

        registros_animales_ref.whereEqualTo("pesaje_tipo", "pesaje").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    if (documentSnapshot.exists()) {
                        ficha_pesaje_carne = documentSnapshot.toObject(Pesaje_Animal_Model.class);
                        String fecha_pesaje = ficha_pesaje_carne.getPesaje_fecha_ingreso();
                        int[] date = parse_date(fecha_pesaje);
                        int day = date[0];
                        int month = date[1];
                        int year = date[2];
                        if (year == ano & month == mes & dia == day) {
                            Toast.makeText(context, "No Es Permitido Persar El Animal Dos Veces El Mismo Dia", Toast.LENGTH_LONG).show();
                            return;
                        } else {
                            animal_ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        Animal_Model animal_model;
                                        animal_model = task.getResult().toObject(Animal_Model.class);
                                        String kilos_animal = String.valueOf(animal_model.getAnml_prod_kilos());
                                        textView.setText(kilos_animal);
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(context, "Error EN El Systema " + e, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }


                    }
                }
            }
        });


    }

    @Override
    public int data_weighing_anmls_register(Double peso_traido, String observaciones, String fecha_pesaje, int dia, int mes, int ano, String id_propietario, String anmls_r_farm_name, String id_animal, Boolean conexion, String eleccion_etapa) {

        ProgressDialog progressBar = new ProgressDialog(context);
        progressBar.setTitle("Cargando...");
        progressBar.setCancelable(false);
        progressBar.setIcon(R.drawable.ic_three_anmls_small);
        progressBar.show();
        DocumentReference ref_usuarios = db.collection("usuarios").document(id_propietario);
        DocumentReference fincas_ref = ref_usuarios.collection("fincas").document(anmls_r_farm_name);
        final DocumentReference dos_ref = fincas_ref.collection("animales").document(id_animal);
        final CollectionReference registros_ref = dos_ref.collection("registro_animal");
        registros_ref.whereEqualTo("pesaje_tipo", "pesaje").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    if (documentSnapshot.exists()) {
                        ficha_pesaje_carne = documentSnapshot.toObject(Pesaje_Animal_Model.class);
                        String id = documentSnapshot.getId();

                        dos_ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    Animal_Model animal_model;
                                    animal_model = task.getResult().toObject(Animal_Model.class);
                                    Double prod_traida = animal_model.getAnml_prod_kilos();
                                    resta = peso_traido - prod_traida;
                                    switch (eleccion_etapa) {
                                        case "pesar Para Destete":
                                            registros_ref.document(id).update("pesaje_fecha_destete", fecha_pesaje, "pesaje_peso_destete", peso_traido, "pesaje_result", resta).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        if (conexion) {
                                                            dos_ref.update("anml_prod_kilos", peso_traido, "anml_prod_kilos_ganados", resta).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
                                                                        consultar_produccion_carne(context, id_propietario, anmls_r_farm_name, peso_traido, progressBar, mes, dia);
                                                                    }
                                                                }
                                                            });
                                                        }
                                                    }
                                                }
                                            });
                                            break;
                                        case "Pesar Despues De Ingreso":
                                            registros_ref.document(id).update("pesaje_fecha_pos_ingreso", fecha_pesaje, "pesaje_pos_ingreso", peso_traido, "pesaje_result", resta).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        if (conexion) {
                                                            dos_ref.update("anml_prod_kilos", peso_traido, "anml_prod_kilos_ganados", resta).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
                                                                        consultar_produccion_carne(context, id_propietario, anmls_r_farm_name, peso_traido, progressBar, mes, dia);
                                                                    }
                                                                }
                                                            });
                                                        }
                                                    }
                                                }
                                            });
                                            break;
                                        case "pesar antes de vender":
                                            registros_ref.document(id).update("pesaje_pre_salida", peso_traido, "pesaje_fecha_pre_salida", fecha_pesaje, "pesaje_result", resta).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        if (conexion) {
                                                            dos_ref.update("anml_prod_kilos", peso_traido, "anml_prod_kilos_ganados", resta).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
                                                                        consultar_produccion_carne(context, id_propietario, anmls_r_farm_name, peso_traido, progressBar, mes, dia);
                                                                    }
                                                                }
                                                            });
                                                        }
                                                    }
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(context, "Se a Producido un error" + e, Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                            break;
                                        case "pesar para vender":
                                            registros_ref.document(id).update("pesaje_fecha_salida", fecha_pesaje, "pesaje_peso_salida", peso_traido, "pesaje_result", resta).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        if (conexion) {
                                                            dos_ref.update("anml_prod_kilos", peso_traido, "anml_prod_kilos_ganados", resta).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
                                                                        consultar_produccion_carne(context, id_propietario, anmls_r_farm_name, peso_traido, progressBar, mes, dia);
                                                                    }
                                                                }
                                                            });
                                                        }
                                                    }
                                                }
                                            });
                                            break;
                                    }


                                }

                            }
                        });


                    }
                }
            }
        });
        return retorno;

    }

    private void registrar_produccion_leche(ProgressDialog progressDialog, Double leche, Boolean bollean_animal, int dia, int mes, int ano) {

        final CollectionReference coreff = farm_ref.collection("produccion");
        coreff.get().addOnSuccessListener((OnSuccessListener<QuerySnapshot>) queryDocumentSnapshots -> {
            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                if (documentSnapshot.exists()) {
                    String id_document = documentSnapshot.getId();
                    ficha_produccion = documentSnapshot.toObject(Produccion_Model.class);
                    String fecha_traida = ficha_produccion.getProd_fecha();
                    int cant_dias_medidos = ficha_produccion.getProd_cant_dias_prom_leche();
                    Double cant_litros_medidos = ficha_produccion.getProd_cant_leche_medida_mes();
                    int cant_v_prod = ficha_produccion.getProd_cant_animales_medidos_mes();
                    int dia_traiido = ficha_produccion.getProd_dia_promedio();

                    int[] date = parse_date(fecha_traida);
                    int mes_traido = date[1];
                    int anio_traido = date[2];

                    if (mes_traido == mes & anio_traido == ano) {
                        if (dia_traiido < dia) {
                            cant_dias_medidos += 1;
                        }
                        if (bollean_animal) {
                            cant_v_prod += 1;
                        }
                        cant_litros_medidos += leche;
                        coreff.document(id_document).update("prod_cant_leche_medida_mes", cant_litros_medidos, "prod_cant_animales_medidos_mes", cant_v_prod, "prod_cant_dias_prom_leche", cant_dias_medidos, "prod_dia_promedio", dia).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(context, "PRODUCCION ACTULIZADA", Toast.LENGTH_SHORT).show();
                                    Intent manejo_animaml = new Intent(context, Manejo_animales_produ.class);
                                    context.startActivity(manejo_animaml);
                                    progressDialog.dismiss();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, "error " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                        return;
                    } else {
                        String dia_ = String.valueOf(dia);
                        String mes_ = String.valueOf(mes);
                        String anio_ = String.valueOf(ano);
                        String fecha = dia_ + "/" + mes_ + "/" + anio_;

                        ficha_produccion = new Produccion_Model(0.0, 0.0, 0.0, 0, 0, 0, 0, 0.0, fecha, 0.0, dia, false, false);
                        coreff.document().set(ficha_produccion).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(context, "Registro Exitoso", Toast.LENGTH_SHORT).show();
                                    Intent manejo_animaml = new Intent(context, Manejo_animales_produ.class);
                                    context.startActivity(manejo_animaml);
                                    progressDialog.dismiss();
                                }
                            }
                        });
                        return;
                    }


                } else {
                    String dia_ = String.valueOf(dia);
                    String mes_ = String.valueOf(mes);
                    String anio_ = String.valueOf(ano);
                    String fecha = dia_ + "/" + mes_ + "/" + anio_;

                    ficha_produccion = new Produccion_Model(0.0, 0.0, 0.0, 1, 0, 0, 0, 0.0, fecha, 0.0, dia, false, false);
                    coreff.document().set(ficha_produccion).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(context, "Registro Exitoso", Toast.LENGTH_SHORT).show();
                                Intent manejo_animaml = new Intent(context, Manejo_animales_produ.class);
                                context.startActivity(manejo_animaml);
                                progressDialog.dismiss();
                            }
                        }
                    });
                    return;
                }
            }

        }).addOnFailureListener(e -> Toast.makeText(context, "Se Ha Presentado Un Error en el sistema" + e, Toast.LENGTH_SHORT).show());
    }

    private void consultar_produccion_carne(Context context, String id_propietario, String n_finca, Double kilos, ProgressDialog progressDialog, int mes, int dia) {
        DocumentReference ref_usuarios = db.collection("usuarios").document(id_propietario);
        DocumentReference fincas_ref = ref_usuarios.collection("fincas").document(n_finca);
        final CollectionReference coreff = fincas_ref.collection("produccion");
        coreff.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    if (documentSnapshot.exists()) {
                        String id_document = documentSnapshot.getId();
                        ficha_produccion = documentSnapshot.toObject(Produccion_Model.class);
                        assert ficha_produccion != null;
                        String fecha = ficha_produccion.getProd_fecha();
                        int cant_v_prod = ficha_produccion.getProd_cant_animales_pesados_mes();

                        Double cant_kilos_pesados_mes = 0.0;
                        int[] date = parse_date(fecha);
                        int month = date[1];
                        if (mes == month) {
                            cant_kilos_pesados_mes = ficha_produccion.getProd_cant_kilos_pesados_mes();
                        }
                        cant_v_prod += 1;
                        cant_kilos_pesados_mes += kilos;

                        coreff.document(id_document).update("prod_cant_kilos_pesados_mes", cant_kilos_pesados_mes, "prod_cant_animales_pesados_mes", cant_v_prod, "prod_dia_pesaje", dia).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(context, "Actualizada la produccion animal", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }
                            }
                        });
                        return;

                    } else {
                        String fecha = "";
                        DocumentReference ref_usuarios = db.collection("usuarios").document(id_propietario);
                        DocumentReference fincas_ref = ref_usuarios.collection("fincas").document(n_finca);
                        final DocumentReference coreff = fincas_ref.collection("produccion").document();
                        ficha_produccion = new Produccion_Model(0.0, 0.0, kilos, 0, 1, 0, 1, 0.0, fecha, 0.0, 0, false, false);
                        coreff.set(ficha_produccion).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    progressDialog.dismiss();
                                    Toast.makeText(context, "Registro Exitoso", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Se Ha Presentado Un Error en el sistema", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int data_type_anmls_register(String anmls_r_drog_applied, int anmls_r_cant_drog, String id_propietario, String anmls_r_farm_name,
                                        String id_animal, Boolean conexion, Object class_name) {

        ProgressDialog progressBar = new ProgressDialog(context);
        progressBar.setTitle("Cargando...");
        progressBar.setCancelable(false);
        progressBar.setIcon(R.drawable.ic_three_anmls_small);
        progressBar.show();

        if (anmls_r_cant_drog > 0) {
            cant_ins_consult(conexion, anmls_r_drog_applied, anmls_r_cant_drog, id_propietario, anmls_r_farm_name, id_animal);
        }
        registros_animales_ref.document().set(class_name).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    retorno = 1;
                    if (conexion) {
                        progressBar.dismiss();
                        Toast.makeText(context, "Registro Exitoso", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setTitle("fallo En El Registro ...");
                progressBar.setCancelable(true);
                progressBar.show();
                progressBar.setButton("volver a intentar", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        progressBar.dismiss();

                    }
                });
                Toast.makeText(context, "No Estas conectado a internet Vuelve a intentarlo", Toast.LENGTH_LONG).show();

            }
        });
        return retorno;

    }

    @Override
    public int data_anmls_part_register(String anmls_r_drog_applied, int anmls_r_cant_drog, String id_propietario, String anmls_r_farm_name, String id_animal, Boolean conexion, Parto_Model parto_model, Double peso, String n_ternera, String madre, String fecha_hoy) {

        ProgressDialog progressBar = new ProgressDialog(context);
        progressBar.setTitle("Cargando...");
        progressBar.setCancelable(false);
        progressBar.setIcon(R.drawable.ic_three_anmls_small);
        progressBar.show();

        if (anmls_r_cant_drog != 0) {
            cant_ins_consult(conexion, anmls_r_drog_applied, anmls_r_cant_drog, id_propietario, anmls_r_farm_name, id_animal);
        }
        String tipo_nacimiento = parto_model.getPart_result_real();
        String raza_ternera = parto_model.getPart_raza_cria();
        String genero = parto_model.getPart_result();
        String fecha_nacimiento = parto_model.getFecha_registro();
        chapeta_traida = parto_model.getPart_number_breeding();
        if (tipo_nacimiento.equals("real")) {
            nombre_padre = "";
            tipo = "";
            tipo = consultar_tipo_animal_cria(id_animal, anmls_r_farm_name, id_propietario);
            nombre_padre = consultar_padre();
            Animal_Model animal_model = new Animal_Model(n_ternera, fecha_hoy, chapeta_traida, "nacido", anmls_r_farm_name, genero, "bovino", "inicial", 0.0, peso, 0.0, "vacio", nombre_padre, "vacio", madre, "vacio", "vacio", "terneras01", 0.0, 0.0, fecha_nacimiento, "vacio", raza_ternera, "obs");
            animales_ref.document().set(animal_model).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Manejo_Animal_Interface manejo_animal_interface = new Manejo_Animal_Presenter(context, animales_ref);
                        manejo_animal_interface.registro_peso_animal("parto", peso, fecha_nacimiento, chapeta_traida, n_ternera, progressBar);
                    }
                }
            });
        }
        registros_animales_ref.document().set(parto_model).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    retorno = 1;
                    if (conexion) {
                        Toast.makeText(context, "Registro Exitoso", Toast.LENGTH_SHORT).show();
                        progressBar.dismiss();
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "No Estas conectado a internet Vuelve a intentarlo", Toast.LENGTH_LONG).show();
                progressBar.dismiss();
            }
        });
        return retorno;

    }

    @Override
    public String consultar_padre() {
        tipo = "";

        registros_animales_ref.whereEqualTo("tipo", "palpacion").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    if (documentSnapshot.exists()) {
                        Palpacion_Model palpacion_model = new Palpacion_Model();
                        String fecha = palpacion_model.getPalp_fecha_pre();
                        int[] date = parse_date(fecha);
                        int month = date[1];
                        int year = date[2];
                        int[] fecha_hoy = datepikers_hoy();
                        int ano_hoy = fecha_hoy[2];
                        int mont_hoy = fecha_hoy[1];
                        mont_hoy -= 8;
                        if (year == ano_hoy & mont_hoy > 8) {
                            registros_animales_ref.whereEqualTo("tipo", "calor").whereEqualTo("calor_fecha", fecha).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    for (DocumentSnapshot documentSnapshot1 : queryDocumentSnapshots) {
                                        if (documentSnapshot1.exists()) {
                                            Servicio_Model servicio_model = new Servicio_Model();
                                            servicio_model = documentSnapshot.toObject(Servicio_Model.class);
                                            tipo = servicio_model.getCalor_n_toro();
                                            return;
                                        }
                                    }

                                }
                            });

                        } else if (year < ano_hoy & mont_hoy < 9) {
                            registros_animales_ref.whereEqualTo("tipo", "calor").whereEqualTo("calor_fecha", fecha).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    for (DocumentSnapshot documentSnapshot1 : queryDocumentSnapshots) {
                                        if (documentSnapshot1.exists()) {
                                            Servicio_Model servicio_model = new Servicio_Model();
                                            servicio_model = documentSnapshot.toObject(Servicio_Model.class);
                                            tipo = servicio_model.getCalor_n_toro();
                                            return;
                                        }
                                    }

                                }
                            });
                        }


                    }
                }
            }
        });
        return tipo;
    }

    private int[] datepikers_hoy() {
        Calendar calendarNow = new GregorianCalendar(TimeZone.getTimeZone("Europe/Madrid"));
        int mes_hoy = calendarNow.get(Calendar.MONTH) + 1;
        int ano_hoy = calendarNow.get(Calendar.YEAR);
        int dia_hoy = calendarNow.get(Calendar.DAY_OF_MONTH);
        return new int[]{dia_hoy, mes_hoy, ano_hoy};

    }

    private String consultar_tipo_animal_cria(String id_animal, String farm_name, String id_propietario) {
        DocumentReference ref_usuarios = db.collection("usuarios").document(id_propietario);
        DocumentReference fincas_ref = ref_usuarios.collection("fincas").document(farm_name);
        final DocumentReference dos_ref = fincas_ref.collection("animales").document(id_animal);
        dos_ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    Animal_Model animal_model;
                    animal_model = task.getResult().toObject(Animal_Model.class);
                    tipo = animal_model.getAnml_tipo();
                }
            }
        });
        return tipo;


    }


    @Override
    public String spinner_services(EditText edt_services, Spinner spinner_f_calor) {

        registros_animales_ref.whereEqualTo("tipo_registro", "calor").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot queryDocumentSnapshots = task.getResult();
                    final List<String> insumos = new ArrayList<String>();
                    for (DocumentSnapshot readData : queryDocumentSnapshots.getDocuments()) {
                        String insumo = readData.get("fecha_registro").toString();
                        insumos.add(insumo);
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, insumos);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_f_calor.setAdapter(arrayAdapter);
                    spinner_f_calor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Object item = parent.getItemAtPosition(position);
                            fecha_palp_noti = (String) item;
                            edt_services.setText(fecha_palp_noti);

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("nombre_insumo", e.getMessage());
                    }
                });
        return fecha_palp_noti;
    }

    @Override
    public void save_photo(Uri imageUri, String id_propietario, String farm_name, String id) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();

        final StorageReference foto_ref = storageReference.child("imagenes").child(id_propietario).child(id).child(imageUri.getLastPathSegment());
        foto_ref.putFile(imageUri).continueWithTask(task -> {
            //Preguntamos si el task de subir la imagen fue exitoso, si lo es retornamos la url de descarga del archivo subido , sino lanzamos una exception.
            if (!task.isSuccessful()) {
                throw task.getException();
            }
            return foto_ref.getDownloadUrl();
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                //Luego si se utiliza addOnCompleteListener para obtener ese enlace
                if (task.isSuccessful()) {
                    //Obtenemos el uri del enlace
                    Toast.makeText(context, "upload failed successful: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "upload failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "error" + e, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void show_animal_detail(ImageView foto_animal, String id_propietario, String farm_name, String id, TextView tv_raza, TextView tv_estado_animal, TextView tv_date_birt, TextView tv_precio, TextView tv_genero, TextView tv_chapeta, TextView tv_etapa, TextView tv_padre, TextView tv_madre, TextView tv_peso, TextView tv_animal_nanme) {
        DocumentReference ref_usuarios = db.collection("usuarios").document(id_propietario);
        DocumentReference fincas_ref = ref_usuarios.collection("fincas").document(farm_name);
        DocumentReference dos = fincas_ref.collection("animales").document(id);
        dos.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Animal_Model animal_model;
                animal_model = task.getResult().toObject(Animal_Model.class);
                nombre_animal = animal_model.getAnml_nombre();
                if (!id.equals("")) {

                    chapeta = animal_model.getAnml_chapeta();
                    genero = animal_model.getAnml_genero();
                    raza = animal_model.getAnml_raza();
                    estado_animal = animal_model.getAnml_salida();
                    precio = String.valueOf(animal_model.getAnml_precio());
                    nombre_madre = animal_model.getAnml_madre();
                    nombre_padre = animal_model.getAnml_padre();
                    date_birt = String.valueOf(animal_model.getAnml_fecha_nacimiento());
                    ruta_foto = animal_model.getAnml_imagen();
                    etapa_animal = animal_model.getAnml_etapa_tipo();
                }


            }

            if (estado_animal == null) {
                tv_estado_animal.setText("activo");
            } else {
                tv_estado_animal.setText(estado_animal

                );
            }
            tv_madre.setText(nombre_madre);
            tv_padre.setText(nombre_padre);
            tv_precio.setText(precio);
            tv_chapeta.setText(chapeta);
            tv_animal_nanme.setText(nombre_animal);

            tv_etapa.setText(etapa_animal);
            tv_genero.setText(genero);
            tv_raza.setText(raza);
            tv_date_birt.setText(date_birt);
            //datos foto
            if (ruta_foto != null) {
                Glide
                        .with(context)
                        .load(ruta_foto)
                        .apply(RequestOptions.circleCropTransform())
                        .into(foto_animal);
            }


        });

    }

}
