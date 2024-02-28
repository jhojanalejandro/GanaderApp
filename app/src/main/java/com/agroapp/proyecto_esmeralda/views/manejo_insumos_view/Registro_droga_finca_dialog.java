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
import com.agroapp.proyecto_esmeralda.controlador.Manejo_Insumos_Presenter;
import com.agroapp.proyecto_esmeralda.controlador.Share_References_presenter;
import com.google.firebase.firestore.DocumentReference;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class Registro_droga_finca_dialog extends AppCompatDialogFragment {

    EditText edt_insumo, edt_precio, edt_cant_ins, edt_obs_ins;
    View view;
    SharedPreferences preferences;
    String farm_name, id_propietario, user_name, fecha;
    Spinner spinner_tipos;
    private ProgressDialog progressBar;
    ArrayAdapter adapter_tipo;
    String[] tipo_insumo;
    int dia, mes, ano, cant_insumos;
    Manejo_Insumos_Interface manejo_insumos_presenter;
    Double precios;
    Probar_connexion probar_connexion;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.registro_insumos_para_finca_dialog, null);
        iniciar_variables();
        datepikers_hoy();
        tipo_insumo = new String[]{"Droga", "Insecticida", "Herbicida", "Otros"};
        preferences = getContext().getSharedPreferences("preferences", Context.MODE_PRIVATE);
        Share_References_interface share_references_presenter = new Share_References_presenter(getContext());
        farm_name = share_references_presenter.farm_name(preferences);
        user_name = share_references_presenter.user_name(preferences);
        id_propietario = share_references_presenter.id_propietario(preferences);

        DocumentReference[] registros_ref_array = share_references_presenter.referencedb_d(id_propietario,farm_name,"vacio");
        DocumentReference fincas_ref = registros_ref_array[0];
        manejo_insumos_presenter = new Manejo_Insumos_Presenter(getContext(), fincas_ref);
        spinner_tipo_insumo();
        if (farm_name == null || user_name == null) {
            Toast.makeText(getContext(), "no se podra guardar la imformacion", Toast.LENGTH_SHORT).show();
        }
        builder.setView(view)
                .setTitle("Registro insumos")
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
                            progressBar.setTitle("fallo de Conexion.. regitro finca.");
                            progressBar.setCancelable(true);
                            progressBar.setButton("No Es Permitido Registrar Este tipo De dato sin Internet", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    progressBar.dismiss();

                                }
                            });
                            progressBar.show();
                            Toast.makeText(view.getContext(), "No Estas conectado a internet Vuelve a intentarlo", Toast.LENGTH_LONG).show();

                        }

                    }


                });

        return builder.create();
    }

    private void iniciar_variables() {
        progressBar = new ProgressDialog(getContext());
        edt_insumo = view.findViewById(R.id.edt_r_ins_finca_n);
        edt_cant_ins = view.findViewById(R.id.edt_r_ins_finca_cant);
        edt_obs_ins = view.findViewById(R.id.edt_r_ins_finca_obs);
        edt_precio = view.findViewById(R.id.edt_r_ins_finca_precio);
        spinner_tipos = view.findViewById(R.id.spinner_tipo_insumo_fin);
        probar_connexion = new Probar_connexion();


    }

    private void spinner_tipo_insumo() {
        spinner_tipos.setAdapter(adapter_tipo);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, tipo_insumo);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_tipos.setAdapter(arrayAdapter);

    }


    private void registrar_insumos_finca() {

        String insumo = edt_insumo.getText().toString();
        String tipo = spinner_tipos.getSelectedItem().toString();
        String obs = edt_obs_ins.getText().toString();
        cant_insumos = Integer.parseInt(edt_cant_ins.getText().toString());
        if (edt_precio.getText().toString().length() == 0) {
            Toast.makeText(view.getContext(), "Debes Poner Un Precio", Toast.LENGTH_SHORT).show();
            return;
        } else {
            precios = Double.parseDouble(edt_precio.getText().toString());

        }

        String meses = String.valueOf(mes);
        String anios = String.valueOf(ano);
        String dias = String.valueOf(dia);
        fecha = dias + "/" + meses + "/" + anios;

        Double unitario = precios / cant_insumos;
        manejo_insumos_presenter.registrar_insumo(  insumo, precios, cant_insumos, unitario, fecha, tipo, obs, mes, ano);


    }

    public void datepikers_hoy() {
        Calendar calendarNow = new GregorianCalendar(TimeZone.getTimeZone("Europe/Madrid"));
        dia = calendarNow.get(Calendar.DAY_OF_MONTH);
        mes = calendarNow.get(Calendar.MONTH) + 1;
        ano = calendarNow.get(Calendar.YEAR);
    }


}
