package com.agroapp.proyecto_esmeralda.views.perfil_animal_macho;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.agroapp.proyecto_esmeralda.R;
import com.agroapp.proyecto_esmeralda.interfaces.Share_References_interface;
import com.agroapp.proyecto_esmeralda.modelos.Pesaje_Animal_Model;
import com.agroapp.proyecto_esmeralda.controlador.Share_References_presenter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class Produccion_animal extends Fragment {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView tv_promedio,tv_cant_litros,tv_cant_dias;
    String id_animal,id_propietario,farm_name,id_doc;
    View view;
    Pesaje_Animal_Model pesaje_carne;
    private ListView lv_prod_individual;
    ArrayList<Pesaje_Animal_Model> list_prod_individual;
    Share_References_interface share_references_interface;
    int dia,mes,ano;

    private String kilos_ganados_detete = "0",kilos_ganados_ingreso = "0", kilos_ganados_posingreso = "0",kilos_ganados_presalida = "0",kilos_ganados_salida = "0";

    TextView tv_fecha_salida,tv_fecha_ingreso,tv_fecha_presalida, tv_fecha_posingreso,tv_fecha_nacimineto, tv_fecha_destete,tv_obs,tv_kilos_nacimiento,tv_kilos_destete,tv_kilos_ingreso,tv_kilos_salida,tv_kilos_presalida,tv_kilos_posingreso,tv_kilos_ganados_destete,tv_kilos_ganados_ingreso,tv_kilos_ganados_salida,tv_kilos_ganados_posingreso,tv_kilos_ganados_presalida;
    private SharedPreferences preferences;



    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_tools_p_animal_m, container, false);
        list_prod_individual = new ArrayList<>();
        share_references_interface = new Share_References_presenter(getContext());
        preferences = getContext().getSharedPreferences("preferences", Context.MODE_PRIVATE);

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);
        datepikers_hoy();

        id_animal  = share_references_interface.id_animal(preferences) ;
        farm_name = share_references_interface.farm_name(preferences);
        id_propietario = share_references_interface.id_propietario(preferences);
        tv_fecha_nacimineto = view.findViewById(R.id.tv_p_animal_m_fecha_nacimieto);
        tv_fecha_destete = view.findViewById(R.id.tv_p_animal_m_fecha_destete);
        tv_fecha_ingreso = view.findViewById(R.id.tv_p_animal_m_fecha_ingreso);
        tv_fecha_presalida = view.findViewById(R.id.tv_p_animal_m_fecha_presalida);
        tv_fecha_posingreso = view.findViewById(R.id.tv_p_animal_m_fecha_posingreso);
        tv_fecha_salida = view.findViewById(R.id.tv_p_animal_m_fecha_salida);
        tv_kilos_presalida = view.findViewById(R.id.tv_p_animal_m_peso_presalida);
        tv_kilos_nacimiento = view.findViewById(R.id.tv_p_animal_m_peso_nacimineto);
        tv_kilos_posingreso = view.findViewById(R.id.tv_p_animal_m_peso_posingreso);
        tv_kilos_ingreso = view.findViewById(R.id.tv_p_animal_m_peso_ingreso);
        tv_kilos_ganados_ingreso = view.findViewById(R.id.tv_p_animal_m_peso_ganado_ingreso);
        tv_kilos_ganados_salida = view.findViewById(R.id.tv_p_animal_m_peso_ganado_salida);
        tv_kilos_ganados_destete = view.findViewById(R.id.tv_p_animal_m_peso_ganado_destete);
        tv_kilos_ganados_presalida = view.findViewById(R.id.tv_p_animal_m_peso_ganado_presalida);
        tv_kilos_ganados_posingreso = view.findViewById(R.id.tv_p_animal_m_peso_ganado_posingreso);
        tv_kilos_salida = view.findViewById(R.id.tv_p_animal_m_peso_salida);
        tv_kilos_destete = view.findViewById(R.id.tv_p_animal_m_peso_destete);
        tv_obs = view.findViewById(R.id.tv_p_animal_m_pesaje_obs);

        if (farm_name != null){
            traer_prod_pesaje_animal();
        }else {
            Toast.makeText(view.getContext(), "algo pasa", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    private void datepikers_hoy( ) {
        Calendar calendarNow = new GregorianCalendar(TimeZone.getTimeZone("Europe/Madrid"));
        dia =calendarNow.get(Calendar.DAY_OF_MONTH);
        mes = calendarNow.get(Calendar.MONTH)+1;
        ano = calendarNow.get(Calendar.YEAR);
    }

    private void traer_prod_pesaje_animal() {
        DocumentReference ref_usuarios = db.collection("usuarios").document(id_propietario);
        DocumentReference fincas_ref = ref_usuarios.collection("fincas").document(farm_name);
        final DocumentReference coreff = fincas_ref.collection("animales").document(id_animal);
        final CollectionReference regis_ref = coreff.collection("registro_animal");

        regis_ref.whereEqualTo("pesaje_tipo", "pesaje").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    if (documentSnapshot.exists()) {
                        pesaje_carne = documentSnapshot.toObject(Pesaje_Animal_Model.class);
                        id_doc = documentSnapshot.getId();
                        regis_ref.document(id_doc).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()){
                                    pesaje_carne = task.getResult().toObject(Pesaje_Animal_Model.class);
                                    String fecha_nacimiento =pesaje_carne.getPesaje_fecha_nacimiento();
                                    String fecha_destete =pesaje_carne.getPesaje_fecha_destete();
                                    String fecha_ingreso =pesaje_carne.getPesaje_fecha_ingreso();
                                    String fecha_posingreso =pesaje_carne.getPesaje_fecha_pos_ingreso();
                                    String fecha_presalida =pesaje_carne.getPesaje_fecha_pre_salida();
                                    String fecha_salida =pesaje_carne.getPesaje_fecha_salida();

                                    String obs_nacimiento =pesaje_carne.getPesaje_observacoines();

                                    Double peso_nacimiento =pesaje_carne.getPesaje_peso_nacimiento();
                                    Double peso_destete =pesaje_carne.getPesaje_peso_destete();
                                    Double peso_ingreso =pesaje_carne.getPesaje_peso_ingreso();
                                    Double peso_posingreso =pesaje_carne.getPesaje_peso_pos_ingreso();
                                    Double peso_pre_salida =pesaje_carne.getPesaje_peso_pre_salida();
                                    Double peso_salida =pesaje_carne.getPesaje_peso_salida();
                                    tv_fecha_nacimineto.setText(fecha_nacimiento);
                                    tv_fecha_destete.setText(fecha_destete);
                                    tv_fecha_ingreso.setText(fecha_ingreso);
                                    tv_fecha_posingreso.setText(fecha_posingreso);
                                    tv_fecha_presalida.setText(fecha_presalida);
                                    tv_fecha_salida.setText(fecha_salida);
                                    tv_fecha_nacimineto.setText(fecha_nacimiento);
                                    String kilos_nacimineto = String.valueOf(peso_nacimiento);
                                    String kilos_destete = String.valueOf(peso_nacimiento);
                                    String kilos_ingreso = String.valueOf(peso_ingreso);
                                    String kilos_posingreso = String.valueOf(peso_posingreso);
                                    String kilos_presalida = String.valueOf(peso_pre_salida);
                                    String kilos_salida = String.valueOf(peso_salida);
                                    tv_kilos_nacimiento.setText(kilos_nacimineto);
                                    tv_kilos_destete.setText(kilos_destete);
                                    tv_kilos_ingreso.setText(kilos_ingreso);
                                    tv_kilos_posingreso.setText(kilos_posingreso);
                                    tv_kilos_presalida.setText(kilos_presalida);
                                    tv_kilos_salida.setText(kilos_salida);

                                    if (peso_destete >0 & peso_nacimiento >0){
                                        kilos_ganados_detete = String.valueOf(peso_destete - peso_nacimiento);
                                    }else if (peso_ingreso >0 & peso_destete > 0 ){
                                        kilos_ganados_ingreso = String.valueOf(peso_ingreso - peso_destete);
                                    }else if (peso_ingreso > 0 & peso_posingreso > 0 ){
                                        kilos_ganados_posingreso = String.valueOf(peso_posingreso - peso_ingreso);
                                    }else if (peso_pre_salida >0 & peso_posingreso > 0 ){
                                        kilos_ganados_presalida = String.valueOf(peso_pre_salida - peso_posingreso);
                                    }else if (peso_pre_salida >0 & peso_salida > 0 ){
                                        kilos_ganados_salida = String.valueOf(peso_salida - peso_pre_salida);
                                    }
                                    tv_kilos_ganados_destete.setText(kilos_ganados_detete);
                                    tv_kilos_ganados_ingreso.setText(kilos_ganados_ingreso);
                                    tv_kilos_ganados_posingreso.setText(kilos_ganados_posingreso);
                                    tv_kilos_ganados_presalida.setText(kilos_ganados_presalida);
                                    tv_kilos_ganados_salida.setText(kilos_ganados_salida);
                                    tv_obs.setText(obs_nacimiento);



                                }
                            }
                        });

                    }

                }


            }
        });
    }



}