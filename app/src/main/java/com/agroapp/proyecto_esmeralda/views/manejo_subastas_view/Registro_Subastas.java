package com.agroapp.proyecto_esmeralda.views.manejo_subastas_view;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.agroapp.proyecto_esmeralda.R;
import com.agroapp.proyecto_esmeralda.interfaces.Manejo_Subastas_Interface;
import com.agroapp.proyecto_esmeralda.modelos.Subasta_Model;
import com.agroapp.proyecto_esmeralda.controlador.Manejo_Subastas_Presenter;
import com.agroapp.proyecto_esmeralda.views.manejo_animal_view.Manejo_animal_view;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

public class Registro_Subastas extends AppCompatActivity {
    SharedPreferences preferences;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    RadioButton rd_tipo_venta;
    EditText edt_precio, edt_descripcion, edt_fecha_inicio, edt_cant_animales;
    Button btn_elegir_animales;
    ImageView img_fecha_subasta;
    DatePickerDialog datePickerDialog;
    RadioGroup rdg_tipo_venta, rdg_tipo_cantidad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_subastas);
        edt_cant_animales = findViewById(R.id.edt_cant_animals_sub);
        edt_precio = findViewById(R.id.edt_precio_animales_sub);
        edt_fecha_inicio = findViewById(R.id.edt_fecha_inicio_sub);
        rdg_tipo_venta = findViewById(R.id.rdg_sub_tipo_venta);
        img_fecha_subasta = findViewById(R.id.img_fecha_subasta);
        rdg_tipo_cantidad = findViewById(R.id.rdg_tipo_cantidad);
        edt_descripcion = findViewById(R.id.edt_descrip_animales_sub);
        btn_elegir_animales = findViewById(R.id.btn_select_animales_sub);
        FloatingActionButton fab_subastar_todo = findViewById(R.id.fab_subastar_todo);
        preferences = getSharedPreferences("preferences", Context.MODE_PRIVATE);

        btn_elegir_animales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registro_subasta = new Intent(Registro_Subastas.this, Manejo_animal_view.class);
                startActivity(registro_subasta);
            }
        });

        fab_subastar_todo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Long precio = Long.valueOf(edt_precio.getText().toString());
                int cantidad_animales = Integer.parseInt(edt_precio.getText().toString());
                String fecha_incial = edt_precio.getText().toString();
                String descripcion = edt_descripcion.getText().toString();
                Subasta_Model  subasta_model= new Subasta_Model();
                subasta_model.setPrecio_total(precio);
                subasta_model.setDescripcion(descripcion);
                subasta_model.setFecha_inicio(fecha_incial);
                subasta_model.setCantidad_animales(cantidad_animales);
                Manejo_Subastas_Interface manejo_subastas = new Manejo_Subastas_Presenter(Registro_Subastas.this);
                manejo_subastas.actualizar_animales(subasta_model);


            }
        });

        img_fecha_subasta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datepiker_subasta();
            }
        });

    }

    private String datepiker_subasta() {
        Calendar calendar = Calendar.getInstance();
        final int dia = calendar.get(Calendar.DAY_OF_MONTH);
        final int mes = calendar.get(Calendar.MONTH);
        final int ano = calendar.get(Calendar.YEAR);
        datePickerDialog = new DatePickerDialog(Registro_Subastas.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int ano, int mes, int dia) {
                mes += 1;
                edt_fecha_inicio.setText(dia + "/" + mes + "/" + ano);
            }
        }, ano, mes, dia);
        datePickerDialog.show();
        return String.valueOf(edt_fecha_inicio);
    }



    private void tipo_venta() {
        String radioid = String.valueOf(rdg_tipo_venta.getCheckedRadioButtonId());
        if (!radioid.equals("")) {
            rd_tipo_venta = findViewById(Integer.parseInt(radioid));

        } else {
            Toast.makeText(Registro_Subastas.this, "no elegiste un resultado de parto", Toast.LENGTH_SHORT).show();
        }

    }

}