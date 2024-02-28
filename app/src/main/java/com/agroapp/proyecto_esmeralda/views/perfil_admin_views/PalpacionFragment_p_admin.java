package com.agroapp.proyecto_esmeralda.views.perfil_admin_views;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.agroapp.proyecto_esmeralda.R;
import com.agroapp.proyecto_esmeralda.interfaces.Perfil_Admin_Interface;
import com.agroapp.proyecto_esmeralda.interfaces.Share_References_interface;
import com.agroapp.proyecto_esmeralda.modelos.Gastos_Insumos;
import com.agroapp.proyecto_esmeralda.controlador.Perfil_Admin_Presenter;
import com.agroapp.proyecto_esmeralda.controlador.Share_References_presenter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.CollectionReference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import static android.content.Context.MODE_PRIVATE;

public class PalpacionFragment_p_admin extends Fragment {

    SharedPreferences preferences;
    int dia,mes,ano;
    String farm_name, id_propietario;
    View view;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    ArrayList<Gastos_Insumos> list_palp,list_posparto;
    Perfil_Admin_Interface perfil_admin_interface;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_palpacion_p_admin, container, false);

        iniciar_variables_promedio();
        datepikers_hoy();
        Share_References_interface share_references_interface = new Share_References_presenter(getContext());
        farm_name = share_references_interface.farm_name(preferences);
        id_propietario = share_references_interface.id_propietario(preferences);
        CollectionReference[] registro_animales_array = share_references_interface.referencedb_c(id_propietario, farm_name, "vacio");
        CollectionReference animales_ref = registro_animales_array[0];
        perfil_admin_interface = new Perfil_Admin_Presenter(getContext(), animales_ref);

        BottomNavigationView bottomNavigationView = view.findViewById(R.id.nav_aniamles_palpacion);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        return view;
    }

    private void iniciar_variables_promedio(){
        list_palp = new ArrayList<>();
        list_posparto = new ArrayList<>();
        recyclerView = view.findViewById(R.id.resi_view_animal_lista_palpacion);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        preferences =  getContext().getSharedPreferences("preferences", MODE_PRIVATE);


    }

    public     BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @SuppressLint("NonConstantResourceId")
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_palpar_posparto:
                    perfil_admin_interface.consultar_palpapcion_parto(recyclerView, layoutManager, mes, ano);
                    break;
                case R.id.navigation_palpar_preparto:
                    perfil_admin_interface.show_palpations(recyclerView, layoutManager, mes, ano);
                    break;
            }
            return true;
        }
    };

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