package com.agroapp.proyecto_esmeralda.views.perfil_animal_views;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.agroapp.proyecto_esmeralda.R;
import com.agroapp.proyecto_esmeralda.interfaces.Manejo_Insumos_Interface;
import com.agroapp.proyecto_esmeralda.interfaces.Offlinne_Connexion_Interface;
import com.agroapp.proyecto_esmeralda.interfaces.Perfil_Animal_Interface;
import com.agroapp.proyecto_esmeralda.controlador.Manejo_Insumos_Presenter;
import com.agroapp.proyecto_esmeralda.controlador.Offline_Connexion_animal_Presenter;
import com.agroapp.proyecto_esmeralda.controlador.Perfil_animal_Presenter;
import com.agroapp.proyecto_esmeralda.views.manejo_animal_view.Manejo_animal_view;
import com.agroapp.proyecto_esmeralda.activities.Probar_connexion;
import com.agroapp.proyecto_esmeralda.modelos.Palpacion_Model;
import com.agroapp.proyecto_esmeralda.interfaces.Share_References_interface;
import com.agroapp.proyecto_esmeralda.controlador.Share_References_presenter;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;

import java.util.Calendar;

public class Palpations_Data_Register_view_dialog extends AppCompatDialogFragment implements View.OnClickListener {

    private EditText edt_n_vet, edt_cant_droga, edt_obs_palp, edt_trat_palp, edt_f_palp, edt_f_palp_noti;
    private View view;
    private ImageButton ibtn_palp, ibtn_palp_r;
    private RadioButton radioButton;
    private int dia_hoy, mes_hoy, ano_hoy, cant_drog = 0;
    private TextView result_palp;
    private RadioGroup rd_palpacion;
    private Palpacion_Model ficha_palpacion;
    private String fecha_palp_noti, fecha_palp, farm_name, drog, id_animal, id_propietario;
    private final String tipo_palpacion = "palpacion";

    private Spinner spinner_insumos, spinner_f_calor;
    Share_References_interface share_references_interface;
    Perfil_Animal_Interface perfil_animal_interface;
    Manejo_Insumos_Interface manejo_insumos_interface;
    SharedPreferences preferences;
    Offlinne_Connexion_Interface offlinne_connexion_interface;

    private DatePickerDialog datePickerDialog, datePickerDialog_noti;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.registro_datos_palpacion_dialog, null);

        iniciar_variables();
        share_references_interface = new Share_References_presenter(getContext());

        int[] fecha_hoy  = share_references_interface.date_picker();
        dia_hoy = fecha_hoy[0];
        mes_hoy = fecha_hoy[1];
        ano_hoy = fecha_hoy[2];
        farm_name = share_references_interface.farm_name(preferences);
        id_animal = share_references_interface.id_animal(preferences);
        id_propietario = share_references_interface.id_propietario(preferences);


        if (id_animal == null || farm_name == null) {
            Toast.makeText(getContext(), "no sera posible guardar el registro ", Toast.LENGTH_LONG).show();
            Intent manejo = new Intent(getContext(), Manejo_animal_view.class);
            startActivity(manejo);
        } else {
            DocumentReference[] registros_ref_array = share_references_interface.referencedb_d(id_propietario,farm_name,"vacio");
            DocumentReference fincas_ref = registros_ref_array[0];
            manejo_insumos_interface = new Manejo_Insumos_Presenter(getContext(), fincas_ref);

            CollectionReference[] registro_animales_array = share_references_interface.referencedb_c(id_propietario, farm_name, id_animal);
            CollectionReference registro_animales = registro_animales_array[1];
            DocumentReference animal_ref = registros_ref_array[1];
            perfil_animal_interface = new Perfil_animal_Presenter(getContext(),animal_ref,registro_animales);
            manejo_insumos_interface.mostrar_spinner_insumos("Droga", spinner_insumos);
            perfil_animal_interface.spinner_services(edt_f_palp_noti, spinner_f_calor);
        }

        builder.setView(view)
                .setTitle("datos de palpacion")
                .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("ENVIAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        checkButton1();
                        if (edt_f_palp_noti.getText().toString().length() == 0){
                            fecha_palp_noti = perfil_animal_interface.spinner_services(edt_f_palp_noti, spinner_f_calor);
                        }else {
                            fecha_palp_noti = edt_f_palp_noti.getText().toString();
                        }
                        fecha_palp = edt_f_palp.getText().toString();
                        String vet = edt_n_vet.getText().toString();
                        String result = result_palp.getText().toString();
                        String trat = edt_trat_palp.getText().toString();
                        String obs = edt_obs_palp.getText().toString();
                        if (edt_cant_droga.getText().toString().trim().length() == 0){
                            cant_drog = 0;
                        }else {

                            cant_drog = Integer.parseInt(edt_cant_droga.getText().toString().trim());
                            drog = manejo_insumos_interface.mostrar_spinner_insumos("Droga",spinner_insumos);
                        }
                        if (Probar_connexion.Prueba(getContext())) {
                            ficha_palpacion = new Palpacion_Model();
                            ficha_palpacion.setCantidad_droga(cant_drog);
                            ficha_palpacion.setNombre_droga(drog);
                            ficha_palpacion.setObservaciones(obs);
                            ficha_palpacion.setTratamiento(trat);
                            ficha_palpacion.setTipo_registro(tipo_palpacion);
                            ficha_palpacion.setPalp_result(result);
                            ficha_palpacion.setPalp_veterinario(vet);
                            ficha_palpacion.setPalp_fecha_pre(fecha_palp_noti);
                            ficha_palpacion.setFecha_registro(fecha_palp);
                            perfil_animal_interface.data_type_anmls_register(drog,cant_drog, id_propietario,farm_name,id_animal,true, ficha_palpacion);
                        } else {
                            offlinne_connexion_interface.data_type_anmls_register_offline( getContext(), 0 , 0.0, 0.0,0.0, fecha_palp_noti, obs, trat,  vet,  farm_name,  cant_drog,  result,  tipo_palpacion,  "vacio",  fecha_palp,  "vacio",  0,  "vacio",  result,  drog,  "vacio", "vacio", id_animal,id_propietario);
                        }

                    }


                });

        return builder.create();


    }

    private void iniciar_variables() {
        edt_trat_palp = view.findViewById(R.id.edt_r_palp_trat);
        edt_obs_palp = view.findViewById(R.id.edt_r_palp_obs);
        edt_f_palp = view.findViewById(R.id.edt_palp_f);
        edt_f_palp_noti = view.findViewById(R.id.edt_palp_f_noti);
        ibtn_palp_r = view.findViewById(R.id.ibtn_palp_r_date);
        ibtn_palp = view.findViewById(R.id.ibtn_palp_r_date_noti);
        edt_cant_droga = view.findViewById(R.id.edt_r_palp_cant_droga);
        result_palp = view.findViewById(R.id.result_pal);
        spinner_insumos = view.findViewById(R.id.spinner_palp_drogra);
        spinner_f_calor = view.findViewById(R.id.spinner_palp_f_servicio);
        rd_palpacion = view.findViewById(R.id.rd_r_palpacion);
        preferences = getContext().getSharedPreferences("preferences", Context.MODE_PRIVATE);
        edt_n_vet = view.findViewById(R.id.edt_r_palp_n_vet);
        ibtn_palp.setOnClickListener(this);
        ibtn_palp_r.setOnClickListener(this);
        offlinne_connexion_interface = new Offline_Connexion_animal_Presenter(getContext());

    }

    private String datepiker_palp() {
        Calendar calendar = Calendar.getInstance();
        final int dia_ = calendar.get(Calendar.DAY_OF_MONTH);
        final int mes_ = calendar.get(Calendar.MONTH);
        final int ano_ = calendar.get(Calendar.YEAR);
        datePickerDialog = new DatePickerDialog(view.getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int ano_, int mes_, int dia_) {
                mes_ = mes_ + 1;
                edt_f_palp.setText( dia_+ "/" + mes_ + "/" + ano_ + "/");
            }
        }, ano_, mes_, dia_);
        datePickerDialog.show();
        return String.valueOf(edt_f_palp);
    }

    private String datepiker_palp_notis() {
        Calendar calendarnoti = Calendar.getInstance();
        final int dia_n = calendarnoti.get(calendarnoti.DAY_OF_MONTH);
        final int mes_n = calendarnoti.get(calendarnoti.MONTH);
        final int ano_n = calendarnoti.get(calendarnoti.YEAR);
        datePickerDialog_noti = new DatePickerDialog(view.getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int ano_n, int mes_n, int dia_n) {
                mes_n = mes_n + 1;
                edt_f_palp_noti.setText(dia_n + "/" + mes_n + "/" + ano_n + "/");
            }
        }, ano_n, mes_n, dia_n);
        datePickerDialog_noti.show();
        return String.valueOf(edt_f_palp_noti);
    }

    private void checkButton1() {
        String radioid = String.valueOf(rd_palpacion.getCheckedRadioButtonId());
        if (!radioid.equals("")) {
            radioButton = view.findViewById(Integer.parseInt(radioid));
            result_palp.setText(radioButton.getText());
        } else {
            Toast.makeText(view.getContext(), "no elegiste un resultado de parto", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ibtn_palp_r_date:
                datepiker_palp();
                break;
            case R.id.ibtn_palp_r_date_noti:
                datepiker_palp_notis();
                break;
        }
    }
}
