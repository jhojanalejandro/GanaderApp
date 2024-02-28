package com.agroapp.proyecto_esmeralda.views.perfil_animal_views;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
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
import com.agroapp.proyecto_esmeralda.interfaces.Share_References_interface;
import com.agroapp.proyecto_esmeralda.controlador.Manejo_Insumos_Presenter;
import com.agroapp.proyecto_esmeralda.controlador.Offline_Connexion_animal_Presenter;
import com.agroapp.proyecto_esmeralda.controlador.Perfil_animal_Presenter;
import com.agroapp.proyecto_esmeralda.controlador.Share_References_presenter;
import com.agroapp.proyecto_esmeralda.views.manejo_animal_view.Manejo_animal_view;
import com.agroapp.proyecto_esmeralda.activities.Probar_connexion;
import com.agroapp.proyecto_esmeralda.modelos.Insumo_Finca_Model;
import com.agroapp.proyecto_esmeralda.modelos.Servicio_Model;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;

import java.util.Calendar;

public class Hot_Data_Register_View_dialog extends AppCompatDialogFragment implements View.OnClickListener {

    EditText  edt_cant_droga_calor, edt_obs_calor, edt_trat_calor, edt_f_calor;
    View view;
    TextView tv_tipo_servicio,tv_nombre_toro;
    ImageButton ibt_fecha_calor;
    private ProgressDialog progressBar;
    private RadioGroup rd_tipo_calor;
    private RadioButton radioButton;

    private Servicio_Model ficha_servicio;
    private DatePickerDialog datePickerDialog;

    String farm_name, drog = "", nombre_toro, id_animal, id_propietario;
    private final String tipo = "calor";
    SharedPreferences preferences;
    TextView tv_finca;
    Spinner spinner_insumos, spinner_toros;
    Perfil_Animal_Interface perfil_animal_interface;
    Manejo_Insumos_Interface manejo_insumos_interface;
    Offlinne_Connexion_Interface offlinne_connexion_interface;

    Share_References_interface share_references_interface;
    Insumo_Finca_Model insumo_finca;
    int cant_drog = 0;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.registro_calor_v_dialog, null);
        iniciar_variables();
        farm_name = share_references_interface.farm_name(preferences);
        id_animal = share_references_interface.id_animal(preferences);
        id_propietario = share_references_interface.id_propietario(preferences);

        if (farm_name == null || id_animal == null) {
            Toast.makeText(getContext(), "no sera posible guardar el registro guardar el registro : ", Toast.LENGTH_LONG).show();
            Intent manejo = new Intent(getContext(), Manejo_animal_view.class);
            startActivity(manejo);
        } else {
            DocumentReference[] registros_ref_array = share_references_interface.referencedb_d(id_propietario,farm_name,"vacio");
            DocumentReference fincas_ref = registros_ref_array[0];
            DocumentReference animal_ref = registros_ref_array[1];

            CollectionReference[] registro_animales_array = share_references_interface.referencedb_c(id_propietario, farm_name, id_animal);
            CollectionReference registro_animales = registro_animales_array[1];
            CollectionReference animales_ref = registro_animales_array[0];
            perfil_animal_interface = new Perfil_animal_Presenter(getContext(),registro_animales);
            manejo_insumos_interface = new Manejo_Insumos_Presenter(getContext(), fincas_ref, animales_ref);

            manejo_insumos_interface.mostrar_spinner_insumos("Droga", spinner_insumos);
            manejo_insumos_interface.mostrar_spinner_toros(tv_nombre_toro, spinner_toros);
        }

        builder.setView(view)
                .setTitle("datos de Calor")
                .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getContext(), "datos cancelados", Toast.LENGTH_SHORT).show();
                    }
                })
                .setPositiveButton("ENVIAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String obs = edt_obs_calor.getText().toString();
                        String trat = edt_trat_calor.getText().toString();
                        String fecha = edt_f_calor.getText().toString();
                        if (edt_cant_droga_calor.getText().toString().length() == 0) {
                            cant_drog = 0;
                        } else {
                             cant_drog = Integer.parseInt(edt_cant_droga_calor.getText().toString().trim());
                            drog = manejo_insumos_interface.mostrar_spinner_insumos( "Droga",spinner_insumos);

                        }
                        String radioid = String.valueOf(rd_tipo_calor.getCheckedRadioButtonId());
                        if (!radioid.equals("")) {
                            radioButton = view.findViewById(Integer.parseInt(radioid));
                            tv_tipo_servicio.setText(radioButton.getText());
                        } else {
                            Toast.makeText(view.getContext(), "no elegiste un resultado de parto", Toast.LENGTH_SHORT).show();
                        }
                        nombre_toro = tv_nombre_toro.getText().toString();
                    if (Probar_connexion.Prueba(getContext())) {
                            ficha_servicio = new Servicio_Model();
                            ficha_servicio.setTipo_registro("calor");
                            ficha_servicio.setFecha_registro(fecha);
                            ficha_servicio.setObservaciones(obs);
                            ficha_servicio.setTratamiento(trat);
                            ficha_servicio.setNombre_droga(drog);
                            ficha_servicio.setCalor_n_toro(nombre_toro);
                            ficha_servicio.setCantidad_droga(cant_drog);
                            perfil_animal_interface.data_type_anmls_register( drog, cant_drog, id_propietario, farm_name, id_animal, true, ficha_servicio);
                        } else {
                            offlinne_connexion_interface.data_type_anmls_register_offline(getContext(), 0, 0.0, 0.0, 0.0, "vacio", obs, trat, "vacio", farm_name, cant_drog, "vacio", "calor", "vacio", fecha, "vacio", 0, "vacio", "vacio", drog, "vacio", nombre_toro, id_animal, id_propietario);

                        }
                    }

                });
        return builder.create();
    }


    private void iniciar_variables() {

        rd_tipo_calor = view.findViewById(R.id.rd_r_servicio);
        progressBar = new ProgressDialog(getContext());
        edt_f_calor = view.findViewById(R.id.edt_calor_f_servicio_vaca);
        tv_finca = view.findViewById(R.id.tv_r_calor_drog_spinner);
        edt_cant_droga_calor = view.findViewById(R.id.edt_r_calor_cant_droga);
        edt_obs_calor = view.findViewById(R.id.edt_r_calor_obs);
        edt_trat_calor = view.findViewById(R.id.edt_r_calor_trat);
        tv_tipo_servicio = view.findViewById(R.id.tv_tipo_servicio);
        tv_nombre_toro = view.findViewById(R.id.tv_r_calor_toros_spinner);
        ibt_fecha_calor = view.findViewById(R.id.ibtn_calor_f_servicio_vaca);
        ibt_fecha_calor.setOnClickListener(this);
        preferences = getContext().getSharedPreferences("preferences", Context.MODE_PRIVATE);
        spinner_toros = view.findViewById(R.id.spinner_calor_toros);

        spinner_insumos = view.findViewById(R.id.spinner_calor_droga);
        share_references_interface = new Share_References_presenter(getContext());
        offlinne_connexion_interface = new Offline_Connexion_animal_Presenter(getContext());
    }


    private String datepiker_servi() {
        Calendar calendar = Calendar.getInstance();
        final int dia = calendar.get(Calendar.DAY_OF_MONTH);
        final int mes = calendar.get(Calendar.MONTH);
        final int ano = calendar.get(Calendar.YEAR);
        datePickerDialog = new DatePickerDialog(view.getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int ano, int mes, int dia) {
                mes = mes + 1;
                edt_f_calor.setText(dia + "/" + mes + "/" + ano + "/");
            }
        }, ano, mes, dia);
        datePickerDialog.show();
        return String.valueOf(edt_f_calor);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibtn_calor_f_servicio_vaca:
                datepiker_servi();
                break;
        }

    }
}
