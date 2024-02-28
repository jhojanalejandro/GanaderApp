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
import com.agroapp.proyecto_esmeralda.interfaces.Share_References_interface;
import com.agroapp.proyecto_esmeralda.modelos.Insumo_Finca_Model;
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


public class Registro_herramienta_dialog extends AppCompatDialogFragment {

    EditText edt_cant_herra,edt_nombre_herra,edt_precio,edt_obs;
    Spinner spinner_precedencia;
    private ProgressDialog progressBar;
    ArrayAdapter Adapter_concentrados;
    View view;

    SharedPreferences preferences;
    String[] array_tipo_insumos;
    Insumo_Finca_Model insumo_finca_model;
    Produccion_Model ficha_produccion;
    Double precios,cant_gasto_mensual;
    Manejo_Insumos_Interface manejo_insumos_presenter;
    String farm_name, id_propietario,nombre_herra,eleccio_procedencia,tipo= "herramienta",obs;
    int dia,mes,ano,cant_insumos;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.registro_herramienta_dialog,null);
        iniciar_variables();

        array_tipo_insumos = new String[] {"nuevo", "usado"};
        preferences = getContext().getSharedPreferences("preferences", Context.MODE_PRIVATE);
        datepikers_hoy();
        Share_References_interface share_references_presenter = new Share_References_presenter(getContext());
        farm_name = share_references_presenter.farm_name(preferences);
        id_propietario = share_references_presenter.id_propietario(preferences);
        DocumentReference[] registros_ref_array = share_references_presenter.referencedb_d(id_propietario,farm_name,"vacio");
        DocumentReference fincas_ref = registros_ref_array[0];
        manejo_insumos_presenter = new Manejo_Insumos_Presenter(getContext(), fincas_ref);
        if (farm_name == null  ){
            Toast.makeText(getContext(), "no se podra guardar la informacion", Toast.LENGTH_SHORT).show();
        }

        spinner_precedencia.setAdapter(Adapter_concentrados);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item,array_tipo_insumos);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_precedencia.setAdapter(arrayAdapter);
        spinner_precedencia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {
                Object item = parent.getItemAtPosition(pos);
                eleccio_procedencia = (String) item;
                if (eleccio_procedencia.equals("usado")){
                    view.findViewById(R.id.ln_r_herra_precio).setVisibility(View.GONE);
                }else {
                    view.findViewById(R.id.ln_r_herra_precio).setVisibility(View.VISIBLE);
                }
            } public void onNothingSelected(AdapterView<?> parent) {

            } });
        builder.setView(view)
                .setTitle("Registro De  Herramienta")
                .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
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
        return  builder.create();
    }
    private  void   iniciar_variables(){

        edt_nombre_herra = view.findViewById(R.id.edt_r_herra_nombre);
        edt_cant_herra = view.findViewById(R.id.edt_r_herra_cant);
        edt_obs = view.findViewById(R.id.edt_r_herra_obs);
        edt_precio = view.findViewById(R.id.edt_r_herra_precio_herra);
        spinner_precedencia = view.findViewById(R.id.spinner_tipo_procedencia_herra);
        progressBar = new ProgressDialog(getContext());
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

    private void registrar_insumos_finca() {

        insumo_finca_model= new Insumo_Finca_Model();
        nombre_herra = edt_nombre_herra.getText().toString();
        cant_insumos = Integer.parseInt(edt_cant_herra.getText().toString());
        if (edt_obs.getText().toString().length() == 0){
            obs = "vacio";
        }else {
            obs = edt_obs.getText().toString();
        }

        String meses = String.valueOf(mes);
        String anios = String.valueOf(ano);
        String dias = String.valueOf(dia);
        String fecha =   dias +"/" +  meses + "/" + anios;
        if ( edt_precio.getText().toString().trim().length() == 0){
            precios = 0.0;
        }else {
            precios = Double.parseDouble(edt_precio.getText().toString());
        }
        Double unitario = (precios / cant_insumos);
        manejo_insumos_presenter.registrar_insumo( nombre_herra, precios ,cant_insumos, unitario, fecha, tipo,obs, mes ,ano);
    }

    private void datepikers_hoy( ) {
        Calendar calendarNow = new GregorianCalendar(TimeZone.getTimeZone("Europe/Madrid"));
        dia =calendarNow.get(Calendar.DAY_OF_MONTH);
        mes = calendarNow.get(Calendar.MONTH)+1;
        ano = calendarNow.get(Calendar.YEAR);
    }

}
