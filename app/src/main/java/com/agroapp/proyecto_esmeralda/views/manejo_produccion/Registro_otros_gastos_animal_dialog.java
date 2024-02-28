package com.agroapp.proyecto_esmeralda.views.manejo_produccion;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;


import com.agroapp.proyecto_esmeralda.R;
import com.agroapp.proyecto_esmeralda.activities.Probar_connexion;
import com.agroapp.proyecto_esmeralda.modelos.Produccion_Model;
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

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class Registro_otros_gastos_animal_dialog extends AppCompatDialogFragment {

    EditText edt_gasto, edt_descripcion, edt_obs;
    View view;
    int dia, mes, i, ano, precio_leche, precio_carne;
    Double produccion, cant_insumo_tra;
    Probar_connexion probar_connexion;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    SharedPreferences preferences;
    String farm_name, id_propietario, fecha, user_name, eleccion_tipo;
    Produccion_Model ficha_produccion;
    private static final String CERO = "0";
    private static final String DOS_PUNTOS = ":";

    //Calendario para obtener fecha & hora
    public final Calendar c = Calendar.getInstance();

    //Variables para obtener la hora hora
    final int hora = c.get(Calendar.HOUR_OF_DAY);
    final int minuto = c.get(Calendar.MINUTE);

    //Widgets
    EditText etHora;
    ImageButton ibObtenerHora;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.registro_otros_gastos, null);
        iniciar_variables();
        datepikers_hoy();
        Share_References_presenter share_references_presenter = new Share_References_presenter(getContext());
        farm_name = share_references_presenter.farm_name(preferences);
        id_propietario = share_references_presenter.id_propietario(preferences);
        user_name = share_references_presenter.user_name(preferences);
        eleccion_tipo = tipo_gasto();
        if (farm_name == null || id_propietario == null) {
            Toast.makeText(view.getContext(), "no se podra registrar la imformacion", Toast.LENGTH_SHORT).show();
        }

        builder.setView(view)
                .setTitle(eleccion_tipo + " Adicional")
                .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dismiss();
                    }
                })
                .setPositiveButton("ENVIAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ProgressDialog progressDialog = new ProgressDialog(getContext());
                        produccion = Double.parseDouble(edt_gasto.getText().toString());

                        if (eleccion_tipo.equals("gasto")) {
                            guardar_otros_gastos(progressDialog);
                        } else if (eleccion_tipo.equals("ganancia")) {
                            guardar_otras_ganancias(progressDialog);
                        }
                    }


                });

        return builder.create();


    }

    private void obtenerHora() {
        TimePickerDialog recogerHora = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                //Formateo el hora obtenido: antepone el 0 si son menores de 10
                String horaFormateada = (hourOfDay < 10) ? String.valueOf(CERO + hourOfDay) : String.valueOf(hourOfDay);
                //Formateo el minuto obtenido: antepone el 0 si son menores de 10
                String minutoFormateado = (minute < 10) ? String.valueOf(CERO + minute) : String.valueOf(minute);
                //Obtengo el valor a.m. o p.m., dependiendo de la selección del usuario
                String AM_PM;
                if (hourOfDay < 12) {
                    AM_PM = "a.m.";
                } else {
                    AM_PM = "p.m.";
                }
                //Muestro la hora con el formato deseado
                etHora.setText(horaFormateada + DOS_PUNTOS + minutoFormateado + " " + AM_PM);
            }
            //Estos valores deben ir en ese orden
            //Al colocar en false se muestra en formato 12 horas y true en formato 24 horas
            //Pero el sistema devuelve la hora en formato 24 horas
        }, hora, minuto, false);

        recogerHora.show();
    }

    public String tipo_gasto() {
        String tipo_gasto = preferences.getString("tipo_gasto", null);
        if (tipo_gasto != null) {
            return tipo_gasto;
        } else {
            return null;
        }
    }

    private void iniciar_variables() {
        edt_descripcion = view.findViewById(R.id.edt_descrip_gasto_ganancia_adicional_p_admin);
        edt_gasto = view.findViewById(R.id.edt_cant_gasto_ganancia_adicional_p_admin);
        edt_obs = view.findViewById(R.id.edt_gasto_ganancia_obs);
        probar_connexion = new Probar_connexion();
        preferences = getContext().getSharedPreferences("preferences", Context.MODE_PRIVATE);


    }

    public void guardar_otros_gastos(ProgressDialog progressDialog) {

        DocumentReference ref_usuarios = db.collection("usuarios").document(id_propietario);
        DocumentReference fincas_ref = ref_usuarios.collection("fincas").document(farm_name);
        final CollectionReference coreff = fincas_ref.collection("produccion");
        coreff.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.isEmpty()) {
                    String meses = String.valueOf(mes);
                    String anios = String.valueOf(ano);
                    String dias = String.valueOf(dia);
                    fecha = dias + "/" + meses + "/" + anios;
                    ficha_produccion = new Produccion_Model(produccion, 0.0, 0.0, 0, 0, 0, 0, 0.0, fecha, 0.0, 0, false, false);
                    coreff.document().set(ficha_produccion).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(view.getContext(), "Registro Exitoso", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();

                        }
                    });
                } else {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        if (documentSnapshot.exists()) {
                            ficha_produccion = documentSnapshot.toObject(Produccion_Model.class);
                            fecha = ficha_produccion.getProd_fecha();
                            String id = documentSnapshot.getId();
                            int[] fecha_par = probar_connexion.parsea_Fecha(fecha);
                            int mes_tra = fecha_par[1];
                            int anio_tra = fecha_par[2];
                            if (mes_tra == mes & anio_tra == ano) {
                                cant_insumo_tra = ficha_produccion.getProd_gasto_mensual();
                                if (produccion != 0) {
                                    cant_insumo_tra += produccion;
                                    coreff.document(id).update("prod_gasto_mensual", cant_insumo_tra).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(view.getContext(), "Registro Actualizado Exitosamente", Toast.LENGTH_SHORT).show();
                                                progressDialog.dismiss();
                                            }

                                        }
                                    });
                                    return;
                                }

                            } else {
                                String meses = String.valueOf(mes);
                                String anios = String.valueOf(ano);
                                String dias = String.valueOf(dia);
                                fecha = dias + "/" + meses + "/" + anios;
                                ficha_produccion = new Produccion_Model(produccion, 0.0, 0.0, 0, 0, 0, 0, 0.0, fecha, 0.0, 0, false, false);
                                coreff.document().set(ficha_produccion).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(view.getContext(), "Registro Exitoso", Toast.LENGTH_SHORT).show();
                                            progressDialog.dismiss();
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

    public void guardar_otras_ganancias(ProgressDialog progressDialog) {

        DocumentReference ref_usuarios = db.collection("usuarios").document(id_propietario);
        DocumentReference fincas_ref = ref_usuarios.collection("fincas").document(farm_name);
        final CollectionReference coreff = fincas_ref.collection("produccion");
        coreff.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.isEmpty()) {
                    String meses = String.valueOf(mes);
                    String anios = String.valueOf(ano);
                    String dias = String.valueOf(dia);
                    fecha = dias + "/" + meses + "/" + anios;
                    ficha_produccion = new Produccion_Model(0.0, produccion, 0.0, 0, 0, 0, 0, 0.0, fecha, 0.0, 0, false, false);
                    coreff.document().set(ficha_produccion).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(view.getContext(), "Registro Exitoso", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();

                        }
                    });
                } else {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        if (documentSnapshot.exists()) {
                            ficha_produccion = documentSnapshot.toObject(Produccion_Model.class);
                            fecha = ficha_produccion.getProd_fecha();
                            String id = documentSnapshot.getId();
                            int[] fecha_par = probar_connexion.parsea_Fecha(fecha);
                            int mes_tra = fecha_par[1];
                            int anio_tra = fecha_par[2];
                            if (mes_tra == mes & anio_tra == ano) {
                                cant_insumo_tra = ficha_produccion.getProd_ganancia_mensual();
                                if (produccion != 0) {
                                    cant_insumo_tra += produccion;
                                    coreff.document(id).update("prod_ganancia_mensual", cant_insumo_tra).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(view.getContext(), "Registro Actualizado Exitosamente", Toast.LENGTH_SHORT).show();
                                                progressDialog.dismiss();
                                            }

                                        }
                                    });
                                    return;
                                }

                            } else {
                                String meses = String.valueOf(mes);
                                String anios = String.valueOf(ano);
                                String dias = String.valueOf(dia);
                                fecha = dias + "/" + meses + "/" + anios;
                                ficha_produccion = new Produccion_Model(0.0, produccion, 0.0, 0, 0, 0, 0, 0.0, fecha, 0.0, 0, false, false);
                                coreff.document().set(ficha_produccion).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(view.getContext(), "Registro Exitoso", Toast.LENGTH_SHORT).show();
                                            progressDialog.dismiss();
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

    private void datepikers_hoy() {
        Calendar calendarNow = new GregorianCalendar(TimeZone.getTimeZone("Europe/Madrid"));
        dia = calendarNow.get(Calendar.DAY_OF_MONTH);
        mes = calendarNow.get(Calendar.MONTH) + 1;
        ano = calendarNow.get(Calendar.YEAR);
    }
}
