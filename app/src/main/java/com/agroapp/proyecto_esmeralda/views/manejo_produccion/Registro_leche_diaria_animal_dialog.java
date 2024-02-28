package com.agroapp.proyecto_esmeralda.views.manejo_produccion;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.agroapp.proyecto_esmeralda.R;
import com.agroapp.proyecto_esmeralda.activities.Probar_connexion;
import com.agroapp.proyecto_esmeralda.modelos.Medida_Leche_Model;
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

import java.util.Calendar;

public class Registro_leche_diaria_animal_dialog extends AppCompatDialogFragment {

    EditText edt_medida_leche, edt_obs, edt_fecha_produccion;
    TextView tv_result_leche;
    View view;
    Probar_connexion probar_connexion;
    int dia, mes_, ano_;

    ImageView img_fecha;
    Double leche;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    SharedPreferences preferences;
    Medida_Leche_Model medida_leche;
    String farm_name, user_name, id_propietario,fecha,obs;
    Produccion_Model ficha_produccion;
    private ProgressDialog progressBar;
    Finca_model finca_model;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.produccion_diaria_animal, null);
        iniciar_variables();
        Share_References_presenter share_references_presenter = new Share_References_presenter(getContext());
        user_name = share_references_presenter.user_name(preferences);
        farm_name = share_references_presenter.farm_name(preferences);
        id_propietario = share_references_presenter.id_propietario(preferences);
        consultar_precio_leche();
        if (farm_name == null || id_propietario == null) {
            Toast.makeText(getContext(), "no se podra registrar la imformacion", Toast.LENGTH_SHORT).show();
        }

        img_fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datepikers_fecha_evento();
            }
        });
        builder.setView(view)
                .setTitle(" Leche Diaria")
                .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dismiss();

                    }
                })
                .setPositiveButton("ENVIAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        progressBar.setTitle("cargando...");
                        progressBar.setCancelable(true);
                        progressBar.setIcon(R.drawable.ic_three_anmls_small);
                        progressBar.show();
                        if (edt_fecha_produccion.getText().toString().length() == 0 ||edt_medida_leche.getText().toString().length() == 0){
                            Toast.makeText(getContext(), "Por favor ingrese una fecha y cantidad", Toast.LENGTH_SHORT).show();
                        }else {
                            guardar_leche();
                        }


                    }

                });

        return builder.create();


    }

    private void iniciar_variables() {
        edt_medida_leche = view.findViewById(R.id.edt_media_leche_cant);
        edt_obs = view.findViewById(R.id.edt_leche_producida_obs);
        edt_fecha_produccion = view.findViewById(R.id.edt_fecha_produccion_diaria);
        img_fecha = view.findViewById(R.id.ibtn_anml_date_picker);
        probar_connexion = new Probar_connexion();
        preferences = getContext().getSharedPreferences("preferences", Context.MODE_PRIVATE);
        progressBar = new ProgressDialog(getContext());

    }

    public String datepikers_fecha_evento() {
        Calendar calendar = Calendar.getInstance();
        final int dias = calendar.get(calendar.DAY_OF_MONTH);
        final int meses = calendar.get(Calendar.MONTH)+ 1;
        final int anos = calendar.get(Calendar.YEAR);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int ano, int mes, int dia) {
                mes_ = mes;
                ano_  =  ano;
                edt_fecha_produccion.setText(dia + "/" + mes + "/" + ano );
            }
        }, anos, meses, dias);
        datePickerDialog.show();
        return String.valueOf(edt_fecha_produccion);
    }
    private void guardar_leche() {
        leche = Double.parseDouble(edt_medida_leche.getText().toString().trim());
        fecha = edt_fecha_produccion.getText().toString();
        obs = edt_obs.getText().toString();

        DocumentReference ref_usuarios = db.collection("usuarios").document(id_propietario);
        DocumentReference fincas_ref = ref_usuarios.collection("fincas").document(farm_name);
        final CollectionReference coreff = fincas_ref.collection("produccion");
        coreff.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.isEmpty()){
                    ficha_produccion = new Produccion_Model(0.0, 0.0, 0.0, 0, 0, 0, 0, leche,fecha,0.0, 0,false,false);
                    ficha_produccion.setProd_obs("FECHA " + fecha + ":" + obs);
                    coreff.document().set(ficha_produccion).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(view.getContext(), "Registro Produccion Dairia Exitosa", Toast.LENGTH_SHORT).show();
                                progressBar.dismiss();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
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
                    });
                }else {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        ficha_produccion = documentSnapshot.toObject(Produccion_Model.class);
                        String id = documentSnapshot.getId();
                        String fecha_traida = ficha_produccion.getProd_fecha();
                        String observciones = ficha_produccion.getProd_obs();

                        int[] fecha_par = probar_connexion.parsea_Fecha(fecha_traida);
                        Double leche_producida_mes = ficha_produccion.getProd_cant_leche_producida_mes();
                        int mes_tra = fecha_par[1];
                        int anio_tra = fecha_par[2];
                        if (mes_tra == mes_ & anio_tra == ano_) {
                            leche_producida_mes += leche;
                            if (obs == null){
                                obs = "FECHA " + fecha + ":" + obs;
                            }else {
                                obs += "FECHA " + fecha  +":" + observciones;
                            }
                            coreff.document(id).update( "prod_cant_leche_producida_mes",leche_producida_mes ,"prod_obs", obs).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(view.getContext(), "Registro Actualizado", Toast.LENGTH_SHORT).show();
                                        progressBar.dismiss();
                                    }
                                }
                            });
                            return;
                        } else {
                            ficha_produccion = new Produccion_Model(0.0, 0.0, 0.0, 0, 0, 0, 0, leche,fecha,0.0, 0,false,false);
                            ficha_produccion.setProd_obs("FECHA " + fecha + ":" + obs);
                            coreff.document().set(ficha_produccion).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(view.getContext(), "Registro Produccion Dairia Exitosa", Toast.LENGTH_SHORT).show();
                                        progressBar.dismiss();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
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
                            });
                            return;
                        }

                    }
                }

            }
        });

    }

    /*
        private void datepikers_hoy() {
        Calendar calendarNow = new GregorianCalendar(TimeZone.getTimeZone("Europe/Madrid"));
        dia = calendarNow.get(Calendar.DAY_OF_MONTH);
        mes = calendarNow.get(Calendar.MONTH) + 1;
        ano = calendarNow.get(Calendar.YEAR);
    }
     */


    private void consultar_precio_leche() {
        DocumentReference ref_usuarios = db.collection("usuarios").document(id_propietario);
        DocumentReference fincas_ref = ref_usuarios.collection("fincas").document(farm_name);
        fincas_ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    finca_model = documentSnapshot.toObject(Finca_model.class);
                }

            }
        });
    }

}
