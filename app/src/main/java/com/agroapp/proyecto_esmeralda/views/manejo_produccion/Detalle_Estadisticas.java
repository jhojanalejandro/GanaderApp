package com.agroapp.proyecto_esmeralda.views.manejo_produccion;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.agroapp.proyecto_esmeralda.R;
import com.agroapp.proyecto_esmeralda.interfaces.Perfil_Admin_Interface;
import com.agroapp.proyecto_esmeralda.interfaces.Share_References_interface;
import com.agroapp.proyecto_esmeralda.controlador.Perfil_Admin_Presenter;
import com.agroapp.proyecto_esmeralda.controlador.Share_References_presenter;
import com.agroapp.proyecto_esmeralda.views.inicio_view.Inicio_Ganaderapp;
import com.google.firebase.firestore.DocumentReference;

public class Detalle_Estadisticas extends AppCompatActivity {

    TextView tv_fecha, tv_ganancias, tv_gastos, tv_cant_litros_producidos, tv_cant_litros_medidos, tv_cant_kilos, tv_cant_animales_pesados, tv_cant_animales_medidos, tv_estado_leche, tv_estado_carne, tv_promedio_animal_prod_leche, tv_promedio_leche_dia , tv_promedio_carne;
    SharedPreferences preferences;
    String farm_name, id_produccion, id_propietario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_produccion);
        tv_fecha = findViewById(R.id.tv_d_prod_f);

        tv_ganancias = findViewById(R.id.edt_d_prod_ganancia);
        tv_gastos = findViewById(R.id.tv_d_prod_gasto);
        tv_cant_litros_producidos = findViewById(R.id.tv_d_litros_producidos);
        tv_cant_litros_medidos = findViewById(R.id.tv_d_litros_medidos);
        tv_promedio_carne = findViewById(R.id.tv_d_prod_promedio_carne);

        tv_estado_carne = findViewById(R.id.tv_d_estado_productivo_carne);
        tv_estado_leche = findViewById(R.id.tv_d_estado_productivo_leche);
        tv_cant_animales_medidos = findViewById(R.id.tv_d_esta_prod_cant_aniamales_medidos);
        tv_cant_animales_pesados = findViewById(R.id.tv_d_esta_prod_cant_aniamales_pesados);
        tv_cant_kilos = findViewById(R.id.tv_d_estad_cant_kilos_pesados);
        tv_promedio_animal_prod_leche = findViewById(R.id.tv_d_prod_promedio_leche);
        tv_promedio_leche_dia = findViewById(R.id.tv_d_prod_promedio_leche_dia);
        Share_References_interface share_references_interface = new Share_References_presenter(Detalle_Estadisticas.this);
        preferences = getSharedPreferences("preferences", MODE_PRIVATE);
        id_propietario = share_references_interface.id_propietario(preferences);
        farm_name = share_references_interface.farm_name(preferences);
        int[] date = share_references_interface.date_picker();
        int dia = date[0];
        int mes = date[1];
        int anio = date[2];
        DocumentReference[] fincas_ref_array = share_references_interface.referencedb_d(id_propietario, farm_name, "vacio" );
        DocumentReference fincas_ref = fincas_ref_array[0];
        Perfil_Admin_Interface perfil_admin_interface =new Perfil_Admin_Presenter(Detalle_Estadisticas.this, fincas_ref);

        id_produccion();
        if (farm_name == null || id_produccion() == null) {
            Toast.makeText(Detalle_Estadisticas.this, "hay errores con la informacion", Toast.LENGTH_SHORT).show();
        } else {
            perfil_admin_interface.mostrar_estadisticas_mensual( id_produccion,tv_ganancias, tv_gastos, tv_cant_litros_medidos, tv_promedio_animal_prod_leche, tv_promedio_leche_dia, tv_promedio_carne, tv_cant_kilos, tv_cant_litros_producidos, mes, anio ,tv_estado_leche, tv_estado_carne, tv_cant_animales_medidos, tv_cant_animales_pesados , tv_fecha);
        }

    }

    private String id_produccion() {
        id_produccion = preferences.getString("id_produccion", null);
        if (id_produccion != null) {
            return id_produccion;
        } else {
            return null;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            navigateUpTo(new Intent(this, Inicio_Ganaderapp.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
