package com.agroapp.proyecto_esmeralda.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.agroapp.proyecto_esmeralda.R;
import com.agroapp.proyecto_esmeralda.interfaces.Share_References_interface;
import com.agroapp.proyecto_esmeralda.modelos.Animal_Model;
import com.agroapp.proyecto_esmeralda.modelos.Medida_Leche_Model;
import com.agroapp.proyecto_esmeralda.modelos.Pesaje_Animal_Model;
import com.agroapp.proyecto_esmeralda.controlador.Share_References_presenter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Detalle_prod_individual extends AppCompatActivity {

    TextView tv_fecha,tv_dias,tv_obs,tv_cant_litros,tv_cant_kilos,tv_estado,tv_promedio;
    SharedPreferences preferences;
    String n_finca,n_animal,id_animal_doc,id_animal,tipo,id_propietario;
    TextView tv_nombre;
    Double promedio;
    Medida_Leche_Model medida_leche;
    Animal_Model modelo_animal;
    Pesaje_Animal_Model pesaje_animal_model;
    int litros_pasados;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_prod_individual);
        tv_fecha = findViewById(R.id.tv_d_prod_ind_f);
        tv_nombre = findViewById(R.id.tv_d_prod_ind_n);
        tv_cant_litros = findViewById(R.id.tv_d_prod_cant_leche);
        tv_estado = findViewById(R.id.tv_d_prod_ind_estado);
        tv_promedio =findViewById(R.id.tv_d_prod_ind_promedio);
        tv_dias = findViewById(R.id.tv_d_prod_ind_dias);
        tv_obs = findViewById(R.id.tv_d_prod_ind_obs);
        Share_References_interface share_references_interface = new Share_References_presenter(Detalle_prod_individual.this);
        preferences =  getSharedPreferences("preferences", MODE_PRIVATE);
        id_propietario = share_references_interface.id_propietario(preferences);
        id_animal = share_references_interface.id_animal(preferences);
        n_finca = share_references_interface.farm_name(preferences);
        id_animal_doc();
        tipo();
        consultar_nombre();
        if (n_finca == null || id_animal_doc() == null ){
            Toast.makeText(Detalle_prod_individual.this, "ALGO VA MAL", Toast.LENGTH_SHORT).show();
        }else {
            consultar_medida();

        }

    }

    private int[] parsea_Fecha(String fechaEntera) {
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
            month = calendario.get(Calendar.MONTH);
            //obtener el dia del mes (1-31)
            day = calendario.get(Calendar.DAY_OF_MONTH);

            //...mas campos...

        } catch (ParseException ex) {
            //manejar excepcion
        }
        return new int[]{day, month, year};
    }

    public void consultar_medida(){

        DocumentReference ref_usuarios = db.collection("usuarios").document(id_propietario);
        DocumentReference docref = ref_usuarios.collection("fincas").document(n_finca);
        final DocumentReference coreff = docref.collection("animales").document(id_animal);
        final DocumentReference regis_ref = coreff.collection("registro_animal").document(id_animal_doc);

        regis_ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    medida_leche = documentSnapshot.toObject(Medida_Leche_Model.class);
                    String obs = medida_leche.getMedida_observacoines();
                    Double litros = medida_leche.getMedida_litros_medidos_mes();
                    String medida = String.valueOf(litros);
                    int dias = medida_leche.getMedida_count_dias();
                    String fecha = String.valueOf(medida_leche.getMedida_anml_fecha());
                    promedio = dias / litros;
                    String promedios = String.valueOf(promedio);
                    tv_cant_litros.setText(medida);
                    String dia = String.valueOf(dias);
                    tv_dias.setText(dia);
                    tv_fecha.setText(fecha);
                    tv_promedio.setText(promedios);
                    tv_obs.setText(obs);
                    tv_nombre.setText(n_animal);
                    if (litros != 0 && litros_pasados !=0){
                        if (litros ==litros_pasados){
                            tv_estado.setText("estable");
                        }else if (litros > litros_pasados){
                            tv_estado.setText("aumentó");
                        }else {
                            tv_estado.setText("bajó");
                        }

                    }else {
                        tv_estado.setText("estable");
                    }



                }


            }
        });

    }

    public void consultar_nombre(){

        DocumentReference ref_usuarios = db.collection("usuarios").document(id_propietario);
        DocumentReference docref = ref_usuarios.collection("fincas").document(n_finca);
        final DocumentReference coreff = docref.collection("animales").document(id_animal);

        coreff.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    modelo_animal = documentSnapshot.toObject(Animal_Model.class);
                    n_animal = modelo_animal.getAnml_nombre();
                    tv_nombre.setText(n_animal);
                }


            }
        });

    }
    private String id_animal_doc(){
        preferences =  getSharedPreferences("preferences", MODE_PRIVATE);
        id_animal_doc = preferences.getString("id_produccion",null);
        if ( id_animal_doc!=null){
            return id_animal_doc;
        }else {
            return  null;
        }
    }
    private String tipo(){
        preferences =  getSharedPreferences("preferences", MODE_PRIVATE);
        tipo = preferences.getString("tipo",null);
        if ( tipo!=null){
            return tipo;
        }else {
            return  null;
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

}
