package com.agroapp.proyecto_esmeralda.views.perfil_admin_views;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.agroapp.proyecto_esmeralda.R;
import com.agroapp.proyecto_esmeralda.interfaces.Perfil_Admin_Interface;
import com.agroapp.proyecto_esmeralda.interfaces.Share_References_interface;
import com.agroapp.proyecto_esmeralda.modelos.Animal_Model;
import com.agroapp.proyecto_esmeralda.modelos.Produccion_Model;
import com.agroapp.proyecto_esmeralda.controlador.Perfil_Admin_Presenter;
import com.agroapp.proyecto_esmeralda.controlador.Share_References_presenter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import static android.content.Context.MODE_PRIVATE;

public class Promedio_ProduccionFragment_p_admin extends Fragment {
    private String farm_name, eleccion_tipo, id_propietario;
    Produccion_Model ficha_produccion;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Animal_Model modelo_animal;
    int animales_pesados, animales_medidos = 0, dias_medidos;
    TextView tv_promedio_medida, tv_promedio_pesaje, tv_fecha_pesaje, tv_fech_medida, tv_cant_litros_medidos, tv_cant_kilos_pesados, tv_cant_dias_prom, tv_cant_animales_medidos, tv_cant_animales_pesados, tv_lote_01, tv_nov_01, tv_nov_02, tv_nov_03, tv_crias_01, tv_crias_02, tv_crias_03, tv_lote_02, tv_anml_prod_g, tv_lote_03, tv_lote_04, tv_horras;
    String fecha_pesaje, lote_01 = "lote01", lote_02 = "lote02", lote_03 = "lote03", lote_04 = "lote04", lote_horras = "horras", lote_n1 = "novillas01", lote_n2 = "novillas02", lote_n3 = "novillas03", lote_c1 = "terneras01", lote_c2 = "terneras02", lote_c3 = "terneras03", cant_horras = "0";
    View view;
    Double listros_medidos = 0.0, promedio_litros_mes_actual = 0.0, promedio_actual_carne = 0.0,kilos_pesados;
    String[] tipo;
    ArrayAdapter comboAdapter;
    Spinner spinner_tipo_prod;
    Perfil_Admin_Interface perfil_admin_interface;
    int dia, mes, ano;
    SharedPreferences preferences;
    Share_References_interface share_preferences_interface;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_promedio_p_admin, container, false);
        datepikers_hoy();
        spinner_tipo_prod = view.findViewById(R.id.spinner_tipo_prod_p_admin);



        tv_cant_litros_medidos = view.findViewById(R.id.tv_pro_p_admn_cant_litros_medidos);
        tv_cant_kilos_pesados = view.findViewById(R.id.tv_pro_p_admn_prod_cant_kilos_pesados);

        tv_cant_dias_prom = view.findViewById(R.id.tv_det_p_admn_prod_cant_dias_medidos);
        tv_cant_animales_medidos = view.findViewById(R.id.tv_pro_p_admn_cant_animales_medidos);
        tv_cant_animales_pesados = view.findViewById(R.id.tv_pro_p_admn_cant_animales_pesados);
        tv_promedio_medida = view.findViewById(R.id.tv_pro_p_admn_promedio_mes_medida);
        tv_promedio_pesaje = view.findViewById(R.id.tv_pro_p_admn_promedio_pesaje);
        tv_fecha_pesaje = view.findViewById(R.id.tv_det_p_admn_prod_fecha_pesaje);
        tv_fech_medida = view.findViewById(R.id.tv_det_p_admn_pro_fecha_medida);

        tv_lote_01 = view.findViewById(R.id.tv_det_p_admn_prod_cant_anml_lote1);
        tv_lote_02 = view.findViewById(R.id.tv_det_p_admn_prod_cant_anml_lote2);
        tv_lote_03 = view.findViewById(R.id.tv_det_p_admn_prod_cant_anml_lote3);
        tv_lote_04 = view.findViewById(R.id.tv_det_p_admn_prod_cant_anml_lote4);

        tv_nov_01 = view.findViewById(R.id.tv_det_p_admn_prod_cant_nov1);
        tv_nov_02 = view.findViewById(R.id.tv_det_p_admn_prod_cant_nov2);
        tv_nov_03 = view.findViewById(R.id.tv_det_p_admn_prod_cant_nov3);
        tv_crias_01 = view.findViewById(R.id.tv_det_p_admn_prod_cant_crias1);
        tv_crias_02 = view.findViewById(R.id.tv_det_p_admn_prod_cant_crias2);
        tv_crias_03 = view.findViewById(R.id.tv_det_p_admn_prod_cant_crias3);

        tv_horras = view.findViewById(R.id.tv_pro_p_admn_cant_anml_horras);
        preferences = getContext().getSharedPreferences("preferences", MODE_PRIVATE);

        share_preferences_interface = new Share_References_presenter(getContext());
        farm_name = share_preferences_interface.farm_name(preferences);
        id_propietario = share_preferences_interface.id_propietario(preferences);
        CollectionReference[] registro_animales_array = share_preferences_interface.referencedb_c(id_propietario, farm_name, "vacio" );
        CollectionReference animales = registro_animales_array[0];
        perfil_admin_interface = new Perfil_Admin_Presenter(getContext(),animales );

        if (farm_name != null) {
            perfil_admin_interface.count_animals( lote_01, "productiva", tv_lote_01);
            perfil_admin_interface.count_animals( lote_02, "productiva", tv_lote_02);
            perfil_admin_interface.count_animals( lote_03, "productiva", tv_lote_03);
            perfil_admin_interface.count_animals( lote_04, "productiva", tv_lote_04);
            perfil_admin_interface.count_animals( lote_horras, "productiva", tv_horras);
            perfil_admin_interface.count_animals( lote_n1, "media", tv_nov_01);
            perfil_admin_interface.count_animals( lote_n2, "media", tv_nov_02);
            perfil_admin_interface.count_animals( lote_n3, "media", tv_nov_03);
            perfil_admin_interface.count_animals( lote_c3, "inicial", tv_crias_03);
            perfil_admin_interface.count_animals( lote_c2, "inicial", tv_crias_02);
            perfil_admin_interface.count_animals( lote_c1, "inicial", tv_crias_01);
            perfil_admin_interface.count_animals( lote_horras, "productiva", tv_horras);
            consultar_produccion_medida();
            consultar_produccion_pesaje();


        }

        tipo = new String[]{"Medida Leche", "Pesaje Carne"};

        spinner_tipo_prod.setAdapter(comboAdapter);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, tipo);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_tipo_prod.setAdapter(arrayAdapter);
        spinner_tipo_prod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {
                Object item = parent.getItemAtPosition(pos);
                eleccion_tipo = (String) item;
                if (eleccion_tipo.equals("Medida Leche")) {
                    view.findViewById(R.id.ln_fecha_medida).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.ln_pro_p_admin_promedio_mes_media).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.ln_pro_p_admin_cant_dias_medidos).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.ln_prod_p_admin_cant_litros_medidos_mes).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.ln_prod_p_admin_cant_anml_medidos).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.ln_p_admin_fecha_pesaje).setVisibility(View.GONE);
                    view.findViewById(R.id.ln_pro_p_admin_cant_anml_pesados).setVisibility(View.GONE);
                    view.findViewById(R.id.ln_p_admin_prom_mes_pesaje).setVisibility(View.GONE);
                    view.findViewById(R.id.ln_pro_p_admin_cant_kilos_pesados).setVisibility(View.GONE);

                } else if (eleccion_tipo.equals("Pesaje Carne")) {
                    view.findViewById(R.id.ln_p_admin_fecha_pesaje).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.ln_p_admin_prom_mes_pesaje).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.ln_pro_p_admin_cant_kilos_pesados).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.ln_pro_p_admin_cant_anml_pesados).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.ln_fecha_medida).setVisibility(View.GONE);
                    view.findViewById(R.id.ln_pro_p_admin_promedio_mes_media).setVisibility(View.GONE);
                    view.findViewById(R.id.ln_pro_p_admin_cant_dias_medidos).setVisibility(View.GONE);
                    view.findViewById(R.id.ln_prod_p_admin_cant_litros_medidos_mes).setVisibility(View.GONE);
                    view.findViewById(R.id.ln_prod_p_admin_cant_anml_medidos).setVisibility(View.GONE);

                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getContext(), "error: campo vacio", Toast.LENGTH_SHORT).show();
            }
        });

        return view;

    }

    private void datepikers_hoy() {
        Calendar calendarNow = new GregorianCalendar(TimeZone.getTimeZone("Europe/Madrid"));
        dia = calendarNow.get(Calendar.DAY_OF_MONTH);
        mes = calendarNow.get(Calendar.MONTH) + 1;
        ano = calendarNow.get(Calendar.YEAR);
    }

    private void consultar_produccion_medida() {

        DocumentReference ref_usuarios = db.collection("usuarios").document(id_propietario);
        DocumentReference fincas_ref = ref_usuarios.collection("fincas").document(farm_name);
        final CollectionReference coreff = fincas_ref.collection("produccion");
        coreff.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    if (documentSnapshot.exists()) {
                        ficha_produccion = documentSnapshot.toObject(Produccion_Model.class);
                        String fecha = ficha_produccion.getProd_fecha();
                        Share_References_interface share_references_interface = new Share_References_presenter(getContext());
                        int[] date = share_references_interface.parse_date(fecha);
                        int year = date[2];
                        int month = date[1];
                        if (month == mes & ano == year) {
                            ficha_produccion = documentSnapshot.toObject(Produccion_Model.class);
                            assert ficha_produccion != null;
                            listros_medidos = ficha_produccion.getProd_cant_leche_medida_mes();
                            animales_medidos = ficha_produccion.getProd_cant_animales_medidos_mes();
                            dias_medidos = ficha_produccion.getProd_cant_dias_prom_leche();
                            String litros_mes = String.valueOf(listros_medidos);
                            tv_cant_litros_medidos.setText(litros_mes);
                            String dias = String.valueOf(dias_medidos);
                            tv_cant_dias_prom.setText(dias);
                            tv_fech_medida.setText("MES: " + mes +" "+ "AÑO: " + ano);
                            String animales_medidos_ = String.valueOf(animales_medidos);
                            tv_cant_animales_medidos.setText(animales_medidos_);

                            if (listros_medidos > 0 & dias_medidos >0 & animales_medidos>0 ){
                                promedio_litros_mes_actual = listros_medidos / dias_medidos;
                                promedio_litros_mes_actual += animales_medidos;
                                promedio_litros_mes_actual /= animales_medidos;
                            }


                        }

                        String promedio = String.valueOf(promedio_litros_mes_actual);
                        tv_promedio_medida.setText(promedio);

                    } else {
                        Toast.makeText(getContext(), "no existen datos de produccion recientes", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void consultar_produccion_pesaje() {
        DocumentReference ref_usuarios = db.collection("usuarios").document(id_propietario);
        DocumentReference fincas_ref = ref_usuarios.collection("fincas").document(farm_name);
        final CollectionReference coreff = fincas_ref.collection("produccion");
        coreff.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    if (documentSnapshot.exists()) {
                        ficha_produccion = documentSnapshot.toObject(Produccion_Model.class);
                        String fecha = ficha_produccion.getProd_fecha();
                        Share_References_interface share_references_interface = new Share_References_presenter(getContext());
                        int[] date = share_references_interface.parse_date(fecha);
                        int year = date[2];
                        int month = date[1];
                        if (month == mes & ano == year) {

                            assert ficha_produccion != null;
                            fecha_pesaje = ficha_produccion.getProd_fecha();
                            kilos_pesados = ficha_produccion.getProd_cant_kilos_pesados_mes();
                            animales_pesados = ficha_produccion.getProd_cant_animales_pesados_mes();
                            String kilos_p = String.valueOf(kilos_pesados);
                            tv_cant_kilos_pesados.setText(kilos_p);
                            String animales_pesados_ = String.valueOf(animales_pesados);
                            tv_cant_animales_pesados.setText(animales_pesados_);
                            tv_fecha_pesaje.setText("MES : " + month + "AÑO :" + year);

                            if (kilos_pesados > 0 & animales_pesados > 0) {
                                promedio_actual_carne = (double) (kilos_pesados / animales_pesados);

                            }
                            String promedio_carne = String.valueOf(promedio_actual_carne);
                            tv_promedio_pesaje.setText(promedio_carne);
                        }

                    } else {
                        Toast.makeText(getContext(), "no existen datos de produccion recientes", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }


}