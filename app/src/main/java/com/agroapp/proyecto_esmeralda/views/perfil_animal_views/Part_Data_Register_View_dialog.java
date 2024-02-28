package com.agroapp.proyecto_esmeralda.views.perfil_animal_views;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;


import com.agroapp.proyecto_esmeralda.R;
import com.agroapp.proyecto_esmeralda.interfaces.Manejo_Insumos_Interface;
import com.agroapp.proyecto_esmeralda.interfaces.Offlinne_Connexion_Interface;
import com.agroapp.proyecto_esmeralda.interfaces.Perfil_Animal_Interface;
import com.agroapp.proyecto_esmeralda.modelos.Parto_Model;
import com.agroapp.proyecto_esmeralda.controlador.Manejo_Insumos_Presenter;
import com.agroapp.proyecto_esmeralda.controlador.Offline_Connexion_animal_Presenter;
import com.agroapp.proyecto_esmeralda.controlador.Perfil_animal_Presenter;
import com.agroapp.proyecto_esmeralda.views.manejo_animal_view.Manejo_animal_view;
import com.agroapp.proyecto_esmeralda.activities.Probar_connexion;
import com.agroapp.proyecto_esmeralda.interfaces.Share_References_interface;
import com.agroapp.proyecto_esmeralda.controlador.Share_References_presenter;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import static android.content.Context.MODE_PRIVATE;

public class Part_Data_Register_View_dialog extends AppCompatDialogFragment {

    EditText edt_cant_droga, edt_n_ternera, edt_r_parto_n_padre, edt_f_parto, edt_obs_parto, edt_numero_hijo, edt_trat_parto, edt_parto_peso_animal, edt_numero_parto;
    View view;
    RadioButton radioButton, radioButton_real;
    RadioGroup rd_reproduccion, rd_reproduccion_real;
    TextView result_part, result_part_real;
    ImageButton ibt_f_parto;
    Boolean boleano_insumos = false;
    DatePickerDialog datePickerDialog;

    int dia_hoy, mes_hoy, ano_hoy, cant_drog = 0, numero_parto;
    String farm_name, drog, id_animal, user_name, animal_name, id_propietario, eleccion_raza, numero_hijo;
    String[] array_raza_bovinos;
    SharedPreferences preferences;
    Spinner spinner_raza;

    Spinner spinner_insumos;
    ArrayAdapter raza_adapter;
    Double peso_nacimiento;
    Share_References_interface share_references_interface;
    Perfil_Animal_Interface perfil_animal_interface;
    Manejo_Insumos_Interface manejo_insumos_interface;
    Offlinne_Connexion_Interface offlinne_connexion_interface;

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.registro_datos_parto_dialog, null);
        iniciar_variables();
        share_references_interface = new Share_References_presenter(getContext());
        farm_name = share_references_interface.farm_name(preferences);
        user_name = share_references_interface.user_name(preferences);
        animal_name = share_references_interface.animal_name(preferences);
        id_propietario = share_references_interface.id_propietario(preferences);
        id_animal = share_references_interface.id_animal(preferences);

        DocumentReference[] registros_ref_array = share_references_interface.referencedb_d(id_propietario,farm_name,"vacio");
        DocumentReference fincas_ref = registros_ref_array[0];
        DocumentReference animal_ref = registros_ref_array[1];

        CollectionReference[] farm_ref_array = share_references_interface.referencedb_c(id_propietario, farm_name, id_animal);
        CollectionReference registros_animales_ref = farm_ref_array[1];
        CollectionReference animales_ref = farm_ref_array[0];
        perfil_animal_interface = new Perfil_animal_Presenter(getContext(), registros_animales_ref,animales_ref, animal_ref);
        manejo_insumos_interface = new Manejo_Insumos_Presenter(getContext(), fincas_ref,animales_ref );
        offlinne_connexion_interface = new Offline_Connexion_animal_Presenter(getContext());
        int[] date = share_references_interface.date_picker();
        dia_hoy = date[0];
        mes_hoy = date[1];
        ano_hoy = date[2];
        array_raza_bovinos = new String[]{"Pardo-suiza", "Roja-sueca", "Holstein-Roja", "Brahman", "Erchi", "Guir", "Angus", "Simbra", "Befmaster", "guirolando", "Simental", "Bon Blanco-oreja-negra", "Bon-Negro-oreja-Blanca", "Holstein", "Jersey", "Cebu", "Normando", "Beefmaster", "Simmental", "Hereford"};

        ibt_f_parto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datepiker_parto();
            }
        });

        if (farm_name == null || id_animal == null) {
            Toast.makeText(getContext(), "no sera posible guardar el registro: ", Toast.LENGTH_LONG).show();
            Intent manejo = new Intent(getContext(), Manejo_animal_view.class);
            startActivity(manejo);
        } else {
            manejo_insumos_interface.mostrar_spinner_insumos("Droga", spinner_insumos);
        }
        spinner_raza.setAdapter(raza_adapter);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, array_raza_bovinos);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_raza.setAdapter(arrayAdapter);
        spinner_raza.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Object item = parent.getItemAtPosition(pos);
                eleccion_raza = (String) item;
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        builder.setView(view)
                .setTitle("datos de parto")
                .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dismiss();
                    }
                })
                .setPositiveButton("ENVIAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        checkButton();
                        checkButton_real();

                        String nombre_animal = edt_n_ternera.getText().toString();
                        String fecha = edt_f_parto.getText().toString();
                        String obs = edt_obs_parto.getText().toString();
                        String trat = edt_trat_parto.getText().toString();
                        String result = result_part.getText().toString();
                        String result_real = result_part_real.getText().toString();
                        String n_padre = edt_r_parto_n_padre.getText().toString();
                        String dia = String.valueOf(dia_hoy);
                        String mes = String.valueOf(mes_hoy);
                        String anio = String.valueOf(ano_hoy);
                        String fecha_ingreso = dia + "/" + mes + "/" + anio;

                        if (edt_parto_peso_animal.getText().toString().length() == 0) {
                            peso_nacimiento = 0.0;
                        } else {
                            peso_nacimiento = Double.parseDouble(edt_parto_peso_animal.getText().toString().trim());
                        }
                        if (edt_numero_hijo.getText().toString().length() == 0) {
                            numero_hijo = "NOASIGNADO";
                        } else {
                            numero_hijo = edt_numero_hijo.getText().toString().trim();
                        }
                        if (edt_cant_droga.getText().toString().length() == 0) {
                            cant_drog = 0;
                        } else {
                            cant_drog = Integer.parseInt(edt_cant_droga.getText().toString().trim());
                            drog = manejo_insumos_interface.mostrar_spinner_insumos( "Droga",spinner_insumos);
                        }
                        if (edt_numero_parto.getText().toString().length() == 0) {
                            numero_parto = 0;
                        } else {
                            numero_parto = Integer.parseInt(edt_numero_parto.getText().toString().trim());
                        }
                        if (Probar_connexion.Prueba(getContext())) {
                            Parto_Model part_model = new Parto_Model();
                            part_model.setFecha_registro(fecha);
                            part_model.setObservaciones(obs);
                            part_model.setTratamiento(trat);
                            part_model.setObservaciones(obs);
                            part_model.setPart_result_real(result_real);
                            part_model.setPart_result(result);
                            part_model.setPart_number_breeding(numero_hijo);
                            part_model.setPart_number(numero_parto);
                            part_model.setPart_raza_cria(eleccion_raza);
                            part_model.setCantidad_droga(cant_drog);
                            part_model.setNombre_droga(drog);
                            part_model.setTipo_registro("parto");
                            part_model.setPart_father_name(n_padre);
                            perfil_animal_interface.data_anmls_part_register(drog, cant_drog, id_propietario, farm_name, id_animal, true, part_model, peso_nacimiento, nombre_animal, animal_name, fecha_ingreso);
                        } else {
                            offlinne_connexion_interface.data_type_anmls_register_offline(getContext(), 0, 0.0, 0.0, 0.0, "vacio", obs, trat, "vacio", farm_name, cant_drog, result, "parto", "vacio", fecha, numero_hijo, numero_parto, n_padre, result_real, drog, "vacio", "vacio", id_animal, id_propietario);
                        }

                    }

                });

        return builder.create();

    }

    private void datepikers_hoy() {
        Calendar calendarNow = new GregorianCalendar(TimeZone.getTimeZone("Europe/Madrid"));
        mes_hoy = calendarNow.get(Calendar.MONTH) + 1;
        ano_hoy = calendarNow.get(Calendar.YEAR);
        dia_hoy = calendarNow.get(Calendar.DAY_OF_MONTH);


    }

    private void datepiker_parto() {
        Calendar calendar = Calendar.getInstance();
        final int dia = calendar.get(Calendar.DAY_OF_MONTH);
        final int mes = calendar.get(Calendar.MONTH);
        final int ano = calendar.get(Calendar.YEAR);
        datePickerDialog = new DatePickerDialog(view.getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int ano, int mes, int dia) {
                mes = mes + 1;
                edt_f_parto.setText(dia + "/" + mes + "/" + ano);
            }
        }, ano, mes, dia);
        datePickerDialog.show();

    }

    private void iniciar_variables() {

        edt_f_parto = view.findViewById(R.id.et_parto_f);
        edt_obs_parto = view.findViewById(R.id.edt_r_parto_obs);
        rd_reproduccion = view.findViewById(R.id.rd_reproduccicion);
        rd_reproduccion_real = view.findViewById(R.id.rd_parto_real);
        edt_trat_parto = view.findViewById(R.id.edt_r_parto_trat);
        edt_parto_peso_animal = view.findViewById(R.id.edt_r_parto_peso_nacimiento);
        edt_numero_parto = view.findViewById(R.id.edt_r_parto_numero_parto);
        edt_numero_hijo = view.findViewById(R.id.edt_r_parto_numero_hijo);
        edt_r_parto_n_padre = view.findViewById(R.id.edt_r_parto_n_padre);
        result_part = view.findViewById(R.id.result_part);
        result_part_real = view.findViewById(R.id.result_parto_reales);
        edt_cant_droga = view.findViewById(R.id.edt_r_parto_cant_droga);
        edt_n_ternera = view.findViewById(R.id.edt_r_nombre_ternera);
        ibt_f_parto = view.findViewById(R.id.ibtn_r_date_part);
        preferences = getContext().getSharedPreferences("preferences", MODE_PRIVATE);

        spinner_insumos = view.findViewById(R.id.spinner_parto_droga);
        spinner_raza = view.findViewById(R.id.spinner_parto_eleccion_raza);


    }

    private void checkButton() {
        String radioid = String.valueOf(rd_reproduccion.getCheckedRadioButtonId());
        if (!radioid.equals("")) {
            radioButton = view.findViewById(Integer.parseInt(radioid));
            result_part.setText(radioButton.getText().toString());

        } else {
            Toast.makeText(view.getContext(), "no elegiste un resultado de parto", Toast.LENGTH_SHORT).show();
        }

    }

    private void checkButton_real() {
        String radioid = String.valueOf(rd_reproduccion_real.getCheckedRadioButtonId());
        if (!radioid.equals("")) {
            radioButton_real = view.findViewById(Integer.parseInt(radioid));
            result_part_real.setText(radioButton_real.getText().toString());
        } else {
            Toast.makeText(view.getContext(), "no elegiste un resultado efectivo de parto", Toast.LENGTH_SHORT).show();
        }

    }


}
