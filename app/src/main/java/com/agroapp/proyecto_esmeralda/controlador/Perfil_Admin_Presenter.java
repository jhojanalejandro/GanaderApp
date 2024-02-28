package com.agroapp.proyecto_esmeralda.controlador;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.agroapp.proyecto_esmeralda.R;
import com.agroapp.proyecto_esmeralda.adapters.Gasto_Insumos_Recycle_Anmls_Adapter;
import com.agroapp.proyecto_esmeralda.adapters.Lista_General_Animals_Recycle_Adapter;
import com.agroapp.proyecto_esmeralda.adapters.Produccion_Mensual_Recycle_Adapter;
import com.agroapp.proyecto_esmeralda.interfaces.Perfil_Admin_Interface;
import com.agroapp.proyecto_esmeralda.interfaces.Perfil_Animal_Interface;
import com.agroapp.proyecto_esmeralda.interfaces.Share_References_interface;
import com.agroapp.proyecto_esmeralda.modelos.Animal_Model;
import com.agroapp.proyecto_esmeralda.modelos.Finca_model;
import com.agroapp.proyecto_esmeralda.modelos.Gastos_Insumos;
import com.agroapp.proyecto_esmeralda.modelos.Parto_Model;
import com.agroapp.proyecto_esmeralda.modelos.Produccion_Model;
import com.agroapp.proyecto_esmeralda.modelos.User_Model;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Perfil_Admin_Presenter implements Perfil_Admin_Interface {
    int count = 0;
    DocumentReference farm_ref;
    CollectionReference animales_ref;
    String tipo, fecha, cant_litros_producidos;
    Context context;
    private RecyclerView  recyclerView;
    private Produccion_Mensual_Recycle_Adapter produccion_viewhollder;
    Double kilos_pesados_traidos, leche_producida, leche_medida_traida;
    int animales_medidos, animales_pesados, dias_medidos;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    Lista_General_Animals_Recycle_Adapter animal_viewhollder;
    Gasto_Insumos_Recycle_Anmls_Adapter insumos_viewhollder;
    Double kilos_pesados_ano = 0.0, ganancias = 0.0, gastos = 0.0, leche_producida_ano = 0.0, kilos_pesados_anterior = 0.0;
    Double promedio_por_dia_mes_actual = 0.0,dividir_prom = 0.0, promedio_actual_carne = 0.0, promedio_anterior_carne = 0.0, promedio_por_dia_mes_anterior = 0.0;
    int ganancia_anual = 0, gasto_anual = 0, ganancias_mes = 0, gasto_mensual = 0;

    public Perfil_Admin_Presenter(Context context) {
        this.context = context;
    }

    public Perfil_Admin_Presenter(Context context, CollectionReference animales_ref) {
        this.context = context;
        this.animales_ref = animales_ref;
    }

    public Perfil_Admin_Presenter( RecyclerView recyclerView, Context context,DocumentReference farm_ref ) {
        this.farm_ref = farm_ref;
        this.context = context;
        this.recyclerView = recyclerView;
    }

    public Perfil_Admin_Presenter(Context context, DocumentReference farm_ref, CollectionReference animales_ref) {
        this.farm_ref = farm_ref;
        this.animales_ref = animales_ref;
        this.context = context;
    }
    @Override
    public void lista_produccion_mes( LinearLayoutManager layoutManager) {
        CollectionReference produccion_ref = farm_ref.collection("produccion");

        ArrayList<Produccion_Model> list_animal = new ArrayList<>();
        produccion_viewhollder = new Produccion_Mensual_Recycle_Adapter(context, R.layout.item_produccion, list_animal);
        produccion_ref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Produccion_Model modelo_produccion = documentSnapshot.toObject(Produccion_Model.class);
                    String ids = documentSnapshot.getId();
                    modelo_produccion.setProd_obs(ids);
                    list_animal.add(modelo_produccion);
                }
                recyclerView.setAdapter(produccion_viewhollder);
                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context, layoutManager.getOrientation());
                recyclerView.addItemDecoration(dividerItemDecoration);


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public Perfil_Admin_Presenter(Context context, DocumentReference farm_ref) {
        this.farm_ref = farm_ref;
        this.context = context;
    }

    @Override
    public void show_data_type_animals_out(String data_type, TextView tv_type, int anio) {

        animales_ref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Animal_Model model_animal;
                    model_animal = documentSnapshot.toObject(Animal_Model.class);
                    String fecha_salida = model_animal.getAnml_fecha_salida();
                    String tipo_traido = model_animal.getAnml_salida();
                    Perfil_Animal_Interface perfil_animal_interface = new Perfil_animal_Presenter(context);
                    if (tipo_traido.equals(data_type)) {
                        int[] date = perfil_animal_interface.parse_date(fecha_salida);
                        int year = date[2];
                        if (year == anio) {
                            count += 1;
                        }
                    }

                }
                String cont = String.valueOf(count);
                tv_type.setText(cont);
            }
        });

    }

    @Override
    public void show_all_cattle(RecyclerView recyclerView, EditText edt_search, LinearLayoutManager layoutManager) {
        ArrayList<Animal_Model> list_animal = new ArrayList<>();
        animal_viewhollder = new Lista_General_Animals_Recycle_Adapter(context, R.layout.item_lista_general_animal, list_animal);

        animales_ref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Animal_Model modelo_animal = new Animal_Model();
                    modelo_animal = documentSnapshot.toObject(Animal_Model.class);
                    String ids = documentSnapshot.getId();
                    modelo_animal.setAnml_lote(ids);
                    list_animal.add(modelo_animal);
                }
                recyclerView.setAdapter(animal_viewhollder);
                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context, layoutManager.getOrientation());
                recyclerView.addItemDecoration(dividerItemDecoration);
                edt_search.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        filter(editable.toString(), list_animal);

                    }
                });


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void consultar_palpapcion_parto(RecyclerView recyclerView, LinearLayoutManager layoutManager, int mes, int ano) {
        Share_References_interface share_references_interface = new Share_References_presenter(context);
        ArrayList<Gastos_Insumos> list_animal = new ArrayList<>();
        insumos_viewhollder = new Gasto_Insumos_Recycle_Anmls_Adapter(context, R.layout.item_lista_general_animal, list_animal);

        if (mes == 1) {
            int mes_bus = 12;
            int ano_bus = ano - 1;

            animales_ref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        if (documentSnapshot.exists()) {
                            Animal_Model modelo_animal = documentSnapshot.toObject(Animal_Model.class);
                            String animal_name = modelo_animal.getAnml_nombre();
                            String id_animal = documentSnapshot.getId();
                            final CollectionReference regis_ref = animales_ref.document(id_animal).collection("registro_animal");

                            regis_ref.whereEqualTo("tipo_registro", "parto").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                        if (documentSnapshot.exists()) {
                                            Parto_Model parto_model = documentSnapshot.toObject(Parto_Model.class);
                                            String fecha_parto = parto_model.getFecha_registro();
                                            int[] date = share_references_interface.parse_date(fecha_parto);
                                            int mes_traido = date[1];
                                            int ano_traido = date[2];
                                            if (mes_bus == mes_traido && ano == ano_traido) {
                                                parto_model.setTipo_registro(animal_name);
                                                list_animal.add(parto_model);

                                            }
                                        }
                                    }
                                    recyclerView.setAdapter(insumos_viewhollder);
                                    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context, layoutManager.getOrientation());
                                    recyclerView.addItemDecoration(dividerItemDecoration);

                                }

                            });
                        }
                    }

                }
            });
        } else {
            int mes_bus = mes - 1;

            animales_ref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        if (documentSnapshot.exists()) {
                            Animal_Model modelo_animal = documentSnapshot.toObject(Animal_Model.class);

                            String animal_name = modelo_animal.getAnml_nombre();
                            String id_animal = documentSnapshot.getId();
                            final CollectionReference regis_ref = animales_ref.document(id_animal).collection("registro_animal");

                            regis_ref.whereEqualTo("tipo_registro", "parto").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                        if (documentSnapshot.exists()) {
                                            Parto_Model ficha_parto = documentSnapshot.toObject(Parto_Model.class);
                                            String fecha_parto = ficha_parto.getFecha_registro();
                                            int[] date = share_references_interface.parse_date(fecha_parto);
                                            int mes_traido = date[1];
                                            int ano_traido = date[2];
                                            if (mes_bus == mes_traido && ano == ano_traido) {
                                                ficha_parto.setTipo_registro(animal_name);
                                                list_animal.add(ficha_parto);
                                            }
                                            recyclerView.setAdapter(insumos_viewhollder);
                                            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context, layoutManager.getOrientation());
                                            recyclerView.addItemDecoration(dividerItemDecoration);
                                        }
                                    }


                                }

                            });

                        }
                    }
                }
            });
        }

    }

    @Override
    public void show_palpations(RecyclerView recyclerView, LinearLayoutManager layoutManager, int mes, int ano) {

        Share_References_interface share_references_interface = new Share_References_presenter(context);
        ArrayList<Gastos_Insumos> list_animal = new ArrayList<>();
        insumos_viewhollder = new Gasto_Insumos_Recycle_Anmls_Adapter(context, R.layout.item_lista_general_animal, list_animal);
        if (mes == 1) {
            int mes_bus = 11;
            int ano_bus = ano - 1;

            animales_ref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        if (documentSnapshot.exists()) {

                            String id_animal = documentSnapshot.getId();
                            Animal_Model modelo_animal = documentSnapshot.toObject(Animal_Model.class);
                            String animal_name = modelo_animal.getAnml_nombre();
                            final CollectionReference regis_ref = animales_ref.document(id_animal).collection("registro_animal");

                            regis_ref.whereEqualTo("tipo_registro", "calor").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                        if (documentSnapshot.exists()) {
                                            Gastos_Insumos modelo_calor = documentSnapshot.toObject(Gastos_Insumos.class);
                                            String fecha_calor = modelo_calor.getFecha_registro();
                                            int[] date = share_references_interface.parse_date(fecha_calor);
                                            int mes_traido = date[1];
                                            int ano_traido = date[2];
                                            int dia_traido = date[0];
                                            if (ano_bus == ano_traido) {
                                                if (mes_bus == mes_traido && dia_traido > 15) {
                                                    modelo_calor.setTipo_registro(animal_name);
                                                    list_animal.add(modelo_calor);
                                                }

                                            }

                                        }

                                    }
                                    recyclerView.setAdapter(insumos_viewhollder);
                                    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context, layoutManager.getOrientation());
                                    recyclerView.addItemDecoration(dividerItemDecoration);

                                }

                            });

                        }
                    }
                }
            });


        } else {
            int mes_bus = mes - 2;

            animales_ref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        if (documentSnapshot.exists()) {

                            Animal_Model modelo_animal = documentSnapshot.toObject(Animal_Model.class);
                            String animal_name = modelo_animal.getAnml_nombre();
                            String id_animal = documentSnapshot.getId();
                            final CollectionReference regis_ref = animales_ref.document(id_animal).collection("registro_animal");
                            regis_ref.whereEqualTo("tipo_registro", "calor").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                        Gastos_Insumos gastos_insumos = documentSnapshot.toObject(Gastos_Insumos.class);
                                        String fecha_calor = gastos_insumos.getFecha_registro();
                                        int[] date = share_references_interface.parse_date(fecha_calor);
                                        int dia_traido = date[0];
                                        int mes_traido = date[1];
                                        int ano_traido = date[2];
                                        if (ano == ano_traido) {
                                            if (mes_bus == mes_traido & dia_traido > 15) {
                                                gastos_insumos.setTipo_registro(animal_name);
                                                list_animal.add(gastos_insumos);
                                            }
                                        }

                                    }
                                    recyclerView.setAdapter(insumos_viewhollder);
                                    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context, layoutManager.getOrientation());
                                    recyclerView.addItemDecoration(dividerItemDecoration);
                                }

                            });

                        }
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });

        }
    }

    public void filter(String toString, ArrayList<Animal_Model> list_animal) {
        ArrayList<Animal_Model> filtrar_lista = new ArrayList<>();
        for (Animal_Model animal : list_animal) {
            if (animal.getAnml_nombre().toLowerCase().contains(toString.toLowerCase()) || animal.getAnml_chapeta().toLowerCase().contains(toString.toLowerCase())) {
                filtrar_lista.add(animal);
            }

        }
        animal_viewhollder.filtrar(filtrar_lista);
    }

    @Override
    public void show_birth_birthdeath_type_animals(String data_type, TextView tv_type, int ano) {

        animales_ref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                count = 0;
                String cont = "0";
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    String id = documentSnapshot.getId();
                    animales_ref.document(id).collection("registro_animal").whereEqualTo("parto_tipo", "parto").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocument_registros) {

                            for (DocumentSnapshot documentSnapshot1 : queryDocument_registros) {
                                Parto_Model parto_model = new Parto_Model();
                                parto_model = documentSnapshot.toObject(Parto_Model.class);
                                String fecha = parto_model.getFecha_registro();
                                String resul_real = parto_model.getPart_result_real();
                                int[] date = parse_date(fecha);
                                int year = date[2];
                                if (resul_real.equals(data_type) & year == ano) {
                                    count += 1;
                                }

                            }
                        }
                    });

                }
                cont = String.valueOf(count);
                tv_type.setText(cont);
            }
        });

    }

    public int[] parse_date(String fechaEntera) {
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

    @Override
    public String consult_tipo_produccion() {

        farm_ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    Finca_model produccion_model;
                    produccion_model = task.getResult().toObject(Finca_model.class);
                    List<String> tipo_produccion = produccion_model.getFinca_tipos_produccion();
                    tipo = tipo_produccion.get(0);
                }


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Se Presento Un Error" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return tipo;
    }

    @Override
    public void show_data_type_animals_on(String data_type, TextView tv_type, int anio) {
        animales_ref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int contador = 0;
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                    Animal_Model model_animal = new Animal_Model();
                    model_animal = documentSnapshot.toObject(Animal_Model.class);
                    String fecha_ingreso = String.valueOf(model_animal.getAnml_fecha_ingreso());
                    String tipo_traido = model_animal.getAnml_procedencia();
                    if (tipo_traido.equals(data_type)) {
                        int[] date = parse_date(fecha_ingreso);
                        int day = date[0];
                        int month = date[1];
                        int year = date[2];
                        if (year == anio) {
                            contador += 1;
                        }
                    }

                }
                String cont = String.valueOf(contador);
                tv_type.setText(cont);
            }
        });

    }

    @Override
    public void count_animals(String lote, String etapa, TextView tv_count_animal) {

        animales_ref.whereEqualTo("anml_lote", lote).whereEqualTo("anml_etapa_tipo", etapa).whereEqualTo("anml_salida", "vacio").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                String cant_animals = "0";
                int contador_animal = 0;
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    if (documentSnapshot.exists()) {
                        Animal_Model modelo_animal = new Animal_Model();
                        modelo_animal = documentSnapshot.toObject(Animal_Model.class);
                        contador_animal += 1;
                        cant_animals = String.valueOf(contador_animal);
                    }

                }
                tv_count_animal.setText(cant_animals);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "algo pasa en la base de datos", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void production_consult_weigh(int mes, TextView tv_cant_animales, TextView tv_kilos, TextView tv_fecha) {

        final CollectionReference coreff = farm_ref.collection("produccion");
        coreff.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    if (documentSnapshot.exists()) {
                        Produccion_Model ficha_produccion = new Produccion_Model();
                        ficha_produccion = documentSnapshot.toObject(Produccion_Model.class);
                        assert ficha_produccion != null;
                        String kilos = String.valueOf(ficha_produccion.getProd_cant_kilos_pesados_mes());
                        String cant_animales_pesados = String.valueOf(ficha_produccion.getProd_cant_animales_pesados_mes());
                        String fecha = ficha_produccion.getProd_fecha();
                        tv_fecha.setText(fecha);
                        tv_cant_animales.setText(cant_animales_pesados);
                        tv_kilos.setText(kilos);

                    } else {
                        Toast.makeText(context, "no existen datos de produccion recientes", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    public void mostrar_analizis_anual(TextView tv_ganancias, TextView tv_gasto, TextView tv_litros_producidos, TextView tv_kilos_producidos, int anio) {

        farm_ref.collection("produccion").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    if (documentSnapshot.exists()) {
                        Produccion_Model ficha_produccion;
                        ficha_produccion = documentSnapshot.toObject(Produccion_Model.class);
                        String fecha = ficha_produccion.getProd_fecha();
                        kilos_pesados_traidos = ficha_produccion.getProd_cant_kilos_pesados_mes();
                        leche_producida = ficha_produccion.getProd_cant_leche_producida_mes();
                        ganancias = ficha_produccion.getProd_ganancia_mensual();
                        gastos = ficha_produccion.getProd_gasto_mensual();

                        int[] date = parse_date(fecha);
                        int year = date[2];
                        if (year == anio) {
                            ganancia_anual += ganancias;
                            gasto_anual += gastos;
                            kilos_pesados_ano += kilos_pesados_traidos;
                            leche_producida_ano += leche_producida;
                        }
                    }
                }
                String ganancia = String.valueOf(ganancia_anual);
                String gastos = String.valueOf(gasto_anual);
                tv_ganancias.setText(ganancia);
                tv_gasto.setText(gastos);
                String kilos = String.valueOf(kilos_pesados_ano);
                String litros_producidos = String.valueOf(leche_producida_ano);
                tv_kilos_producidos.setText(kilos);
                tv_litros_producidos.setText(litros_producidos);
            }
        });
    }

    @Override
    public void mostrar_estadisticas_mensual(String id_propietario, TextView tv_ganancias, TextView tv_gasto, TextView tv_litros_medidos, TextView tv_promedio_animal, TextView tv_promedio_dia, TextView tv_promedio_carne, TextView kilos_producidos, TextView tv_litros_producidos, int mes, int anio, TextView tv_estado_leche, TextView tv_estado_carne, TextView tv_animales_medidos, TextView tv_animales_pesados, TextView tv_fecha) {

        DocumentReference produc_ref = farm_ref.collection("produccion").document(id_propietario);
        produc_ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    if (mes == 1) {
                        Toast.makeText(context, "Primer Mes Del AÑO Trabaja Duro Y veras Los Resultados", Toast.LENGTH_SHORT).show();
                    } else {

                        Produccion_Model ficha_produccion = task.getResult().toObject(Produccion_Model.class);
                        fecha = ficha_produccion.getProd_fecha();
                        int[] date = parse_date(fecha);
                        int mes_traido = date[1];
                        int anio_traido = date[2];
                        leche_medida_traida = ficha_produccion.getProd_cant_leche_medida_mes();
                        kilos_pesados_traidos = ficha_produccion.getProd_cant_kilos_pesados_mes();
                        cant_litros_producidos = String.valueOf(ficha_produccion.getProd_cant_leche_producida_mes());
                        leche_producida = ficha_produccion.getProd_cant_leche_producida_mes();
                        animales_medidos = ficha_produccion.getProd_cant_animales_medidos_mes();
                        animales_pesados = ficha_produccion.getProd_cant_animales_pesados_mes();
                        dias_medidos = ficha_produccion.getProd_cant_dias_prom_leche();
                        Double profit = ficha_produccion.getProd_ganancia_mensual();
                        ganancias_mes += profit;
                        Double spend = ficha_produccion.getProd_gasto_mensual();
                        gasto_mensual += spend;

                        int mes_pasado = mes_traido - 1;
                        if (mes_traido == mes & anio_traido == anio) {
                            Boolean nomina = ficha_produccion.getPago_nomina();
                            Boolean pago_arriendo_popiedad = ficha_produccion.getPago_arriendo();
                            if (!nomina) {
                                CollectionReference ref_u = db.collection("usuarios");
                                ref_u.whereEqualTo("id_user", id_propietario).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshot1) {
                                        for (DocumentSnapshot documentSnapshot1 : queryDocumentSnapshot1) {
                                            if (!queryDocumentSnapshot1.isEmpty()) {
                                                User_Model user_model = documentSnapshot1.toObject(User_Model.class);
                                                Long pago = user_model.getUser_pago_mensual();
                                                if (pago > 0.0) {
                                                    gasto_mensual += pago;
                                                    produc_ref.update("pago_nomina", true, "prod_gasto_mensual", gasto_mensual);
                                                }
                                            }
                                        }
                                    }
                                });
                            }

                            farm_ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        Finca_model finca_model = task.getResult().toObject(Finca_model.class);
                                        Long precio_arriendo = finca_model.getFinca_precio_arriendo();
                                        Long precio_leche = finca_model.getFinca_precio_leche();
                                        if (!pago_arriendo_popiedad) {
                                            if (precio_arriendo > 0.0) {
                                                gasto_mensual += precio_arriendo;
                                                produc_ref.update("pago_arriendo", true, "prod_gasto_mensual", gasto_mensual);
                                            }
                                        }
                                        leche_producida *= precio_leche;

                                    }
                                }
                            });


                        } else if (mes_pasado == mes) {
                            int animales_pesados_anterior = 0;
                            if (mes <= 3) {
                                animales_pesados_anterior = ficha_produccion.getProd_cant_animales_pesados_mes();
                                kilos_pesados_anterior = ficha_produccion.getProd_cant_kilos_pesados_mes();
                            }
                            promedio_por_dia_mes_anterior = (double) (leche_producida / 30);
                            promedio_anterior_carne = kilos_pesados_anterior / animales_pesados_anterior;

                        }


                    }
                    promedio_por_dia_mes_actual = leche_producida / 30;
                    if (kilos_pesados_traidos > 0 & animales_pesados > 0) {
                        promedio_actual_carne = kilos_pesados_traidos / animales_pesados;
                    }
                    if (leche_medida_traida >0 & dias_medidos > 0){
                        dividir_prom = leche_medida_traida / dias_medidos;
                        dividir_prom += animales_medidos;
                        dividir_prom /= animales_medidos;
                    }

                    tv_fecha.setText(fecha);
                    String promedios_carne = String.valueOf(promedio_actual_carne);
                    String promedios_dia = String.valueOf(promedio_por_dia_mes_actual);
                    String cant_animales_medidos = String.valueOf(animales_medidos);
                    String cant_animales_pesados = String.valueOf(animales_pesados);
                    String gasto = String.valueOf(gasto_mensual);
                    tv_promedio_carne.setText(promedios_carne);

                    tv_gasto.setText(gasto);
                    tv_promedio_dia.setText(promedios_dia);

                    ganancias_mes += leche_producida;
                    String ganancias = String.valueOf(ganancias_mes);
                    tv_ganancias.setText(ganancias);
                    tv_animales_medidos.setText(cant_animales_medidos);
                    tv_animales_pesados.setText(cant_animales_pesados);

                    tv_litros_producidos.setText(cant_litros_producidos);
                    String kilos = String.valueOf(kilos_pesados_traidos);
                    String litros_producidos = String.valueOf(leche_medida_traida);
                    String promedio_actual_leche = String.valueOf(dividir_prom);
                    kilos_producidos.setText(kilos);
                    tv_litros_medidos.setText(litros_producidos);
                    tv_promedio_animal.setText(promedio_actual_leche);
                    if (promedio_por_dia_mes_actual != null && promedio_por_dia_mes_anterior != null) {
                        if (promedio_por_dia_mes_actual > promedio_por_dia_mes_anterior) {
                            tv_estado_leche.setText("AUMENTO");
                        } else if (promedio_por_dia_mes_actual.equals(promedio_por_dia_mes_anterior)) {
                            tv_estado_leche.setText("ESTABLE");
                        } else if (promedio_por_dia_mes_actual < promedio_por_dia_mes_anterior) {
                            tv_estado_leche.setText("BAJO");
                        }
                    }
                    if (promedio_actual_carne != null && promedio_anterior_carne != null) {
                        if (promedio_actual_carne > promedio_anterior_carne) {
                            tv_estado_carne.setText("AUMENTO");
                        } else if (promedio_actual_carne.equals(promedio_anterior_carne)) {
                            tv_estado_carne.setText("ESTABLE");
                        } else if (promedio_actual_carne < promedio_anterior_carne) {
                            tv_estado_carne.setText("BAJO");
                        }
                    }

                }

            }
        });
    }

}
