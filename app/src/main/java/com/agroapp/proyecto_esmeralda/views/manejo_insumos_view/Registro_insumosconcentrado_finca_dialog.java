package com.agroapp.proyecto_esmeralda.views.manejo_insumos_view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.agroapp.proyecto_esmeralda.R;
import com.agroapp.proyecto_esmeralda.activities.Probar_connexion;
import com.agroapp.proyecto_esmeralda.interfaces.Manejo_Insumos_Interface;
import com.agroapp.proyecto_esmeralda.modelos.Produccion_Model;
import com.agroapp.proyecto_esmeralda.controlador.Manejo_Insumos_Presenter;
import com.agroapp.proyecto_esmeralda.controlador.Share_References_presenter;
import com.google.firebase.firestore.DocumentReference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class Registro_insumosconcentrado_finca_dialog extends AppCompatDialogFragment {

    EditText edt_precio, edt_nombre, edt_cant_ins, edt_obs_ins;
    View view;
    SharedPreferences preferences;
    String farm_name, user_name,id_propietario, eleccion_spinner;
    Produccion_Model ficha_produccion;
    int dia, mes, ano, cant_ins = 0;
    Probar_connexion probar_connexion;
    Spinner spinner_tipo_insumo;
    Manejo_Insumos_Interface manejo_insumos_presenter;
    private ProgressDialog progressBar;
    Double precios;

    String fecha;
    String[] array_tipo_insumos;
    ArrayAdapter Adapter_concentrados;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.registro_insumos_abono_concentrado_finca_dialog, null);
        iniciar_variables();
        datepikers_hoy();
        preferences = getContext().getSharedPreferences("preferences", Context.MODE_PRIVATE);
        Share_References_presenter share_references_presenter = new Share_References_presenter(getContext());
        farm_name = share_references_presenter.farm_name(preferences);
        user_name = share_references_presenter.user_name(preferences);
        id_propietario = share_references_presenter.id_propietario(preferences);

        DocumentReference[] registros_ref_array = share_references_presenter.referencedb_d(id_propietario,farm_name,"vacio");
        DocumentReference fincas_ref = registros_ref_array[0];
        manejo_insumos_presenter = new Manejo_Insumos_Presenter(getContext(), fincas_ref);

        if (farm_name == null || id_propietario == null) {
            Toast.makeText(getContext(), "no se podra guardar la imformacion", Toast.LENGTH_SHORT).show();
        }
        array_tipo_insumos = new String[] {"abono", "cuido","sal mineral"};
        spinner_tipo_insumo.setAdapter(Adapter_concentrados);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item,array_tipo_insumos);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_tipo_insumo.setAdapter(arrayAdapter);
        spinner_tipo_insumo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Object item = parent.getItemAtPosition(pos);
                eleccion_spinner= (String) item;
            } public void onNothingSelected(AdapterView<?> parent) {

            } });

        builder.setView(view)
                .setTitle("Registro insumos")
                .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(view.getContext(), "Registro  Cancelado", Toast.LENGTH_SHORT).show();
                        dismiss();
                    }
                })
                .setPositiveButton("GUARDAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (Probar_connexion.Prueba(getContext())) {
                            registrar_insumos_finca();
                        } else {
                            progressBar.setTitle("fallo de Conexion...");
                            progressBar.setCancelable(true);
                            progressBar.setButton("No Es Permitido Registrar Este tipo De dato sin Internet", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    progressBar.dismiss();

                                }
                            });
                            progressBar.show();
                            Toast.makeText(getContext(), "No Estas conectado a internet Vuelve a intentarlo", Toast.LENGTH_LONG).show();

                        }
                    }


                });

        return builder.create();
    }

    private void iniciar_variables() {
        edt_cant_ins = view.findViewById(R.id.edt_r_ins_cant_con);
        edt_obs_ins = view.findViewById(R.id.edt_r_ins_obs_con);
        edt_precio = view.findViewById(R.id.edt_r_ins_price_unity);
        edt_nombre = view.findViewById(R.id.edt_r_ins_n_concentrado);
        spinner_tipo_insumo = view.findViewById(R.id.spinner_tipo_insumo_concentrados);
        probar_connexion = new Probar_connexion();
        progressBar = new ProgressDialog(getContext());


    }
    private void registrar_insumos_finca() {

        String insumo_finca = edt_nombre.getText().toString();
        String obs = edt_obs_ins.getText().toString();
        cant_ins = Integer.parseInt(edt_cant_ins.getText().toString());
        String meses = String.valueOf(mes);
        String anios = String.valueOf(ano);
        String dias = String.valueOf(dia);
        fecha = dias + "/" + meses + "/" + anios;
        if (edt_cant_ins.getText().toString().length() ==0 ){
            precios = 0.0;
        }else {
            precios = Double.parseDouble(edt_precio.getText().toString());

        }
        Double unitario = precios * cant_ins;
        manejo_insumos_presenter.registrar_insumo(  insumo_finca, unitario, cant_ins, precios, fecha, eleccion_spinner, obs, mes, ano);
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

    private void datepikers_hoy( ) {
        Calendar calendarNow = new GregorianCalendar(TimeZone.getTimeZone("Europe/Madrid"));
        dia =calendarNow.get(Calendar.DAY_OF_MONTH);
        mes = calendarNow.get(Calendar.MONTH)+1;
        ano = calendarNow.get(Calendar.YEAR);
    }


}
